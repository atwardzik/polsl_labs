package model;

import repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class holding all users
 *
 * @author Artur Twardzik
 * @version 0.6
 */
public class UserManager {
    /**
     * Repository used to perform CRUD operations on {@link User} entities.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new {@link UserManager} with the specified repository.
     *
     * @param userRepository the repository for accessing user data; must not be null
     */
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user with the given details.
     * <p>
     * Throws {@link UserAlreadyExistsException} if a user with the same username already exists.
     * The password is hashed automatically using the {@link User#create} method.
     *
     * @param name     the first name of the user
     * @param surname  the surname of the user
     * @param username the unique username for the user
     * @param password the plain text password for the user
     * @return the newly created {@link User} object
     * @throws UserAlreadyExistsException if a user with the same username already exists
     */
    public User registerUser(
            String name,
            String surname,
            String username,
            String password
    ) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }

        User user = User.create(name, surname, username, password);
        userRepository.save(user);
        return user;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the {@link User} object if found, or {@code null} if no user exists with that username
     */
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the string representation of the user's UUID
     * @return the {@link User} object if found, or {@code null} if no user exists with that ID
     */
    public User getUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElse(null);
    }

    /**
     * Returns a list of simplified user records for all users.
     * <p>
     * Each record contains the user's ID, username, and full name.
     *
     * @return a list of {@link UserListRecord} objects representing all users
     */
    public List<UserListRecord> getUserList() {
        return userRepository.findAll().stream()
                .map(User::getRecord)
                .collect(Collectors.toList());
    }
}

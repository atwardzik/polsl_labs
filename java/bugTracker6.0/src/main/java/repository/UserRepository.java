package repository;

import model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    /**
     * Adds a new user to the manager or updates existing (determined based on existence of ID in the table)
     *
     * @param user the user to be added
     */
    void save(User user);

    /**
     * Gets the user
     *
     * @param username specifying the user to be returned
     * @return user or null if no such user exists
     */
    Optional<User> findByUsername(String username);

    /**
     * Gets the user
     *
     * @param id specifying the user to be returned
     * @return user or null if no such user exists
     */
    Optional<User> findById(UUID id);

    /**
     * Checks if the username exists in the database of users
     *
     * @param username to be checked
     * @return true if this username is already in the db
     */
    boolean existsByUsername(String username);

    /**
     * Fetch all users
     *
     * @return all users list
     */
    List<User> findAll();
}

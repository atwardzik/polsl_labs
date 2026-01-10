package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class holding all users
 *
 * @author Artur Twardzik
 * @version 0.5
 */
public class UserManager {
    /**
     * The set of all users managed by this manager.
     */
    private Set<User> users = new HashSet<>();

    /**
     * Checks if the username exists in the database of users
     *
     * @param username to be checked
     * @return true if this username is already in the db
     */
    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }

        for (User user : users) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds new user to the database of users
     *
     * @param user to be added
     * @throws UserAlreadyExistsException if user with such username already exists
     */
    public void addUser(User user) throws UserAlreadyExistsException {
        if (usernameExists(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        users.add(user);
    }

    /**
     * Gets the user
     *
     * @param username specifying the user to be returned
     * @return user or null if no such user exists
     */
    public User getUser(String username) {
        for (User user : users) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }

        return null;
    }

    /**
     * Retrieves all users records
     *
     * @return user set as String records
     */
    public Set<UserListRecord> getUserList() {
        Set<UserListRecord> userListRecords = new HashSet<>();

        users.forEach(user -> userListRecords.add(user.getRecord()));

        return userListRecords;
    }
}

package model;

/**
 * Custom exception for signalizing that such user already exists in the database
 *
 * @author Artur Twardzik
 * @version 0.5
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User with username: `" + username + "` already exists");
    }
}

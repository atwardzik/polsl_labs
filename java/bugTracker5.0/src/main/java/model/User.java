package model;

import com.password4j.Hash;
import com.password4j.Password;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represents a user in the system with basic profile information,
 * unique identifier, creation and activity timestamps, and a list of roles.
 *
 * @author Artur Twardzik
 * @version 0.5
 */
public class User {
    /**
     * Unique identifier for the user.
     */
    @Getter
    private UUID id;
    /**
     * First name of the user.
     */
    @Getter
    private String name;
    /**
     * Surname (last name) of the user.
     */
    @Getter
    private String surname;
    /**
     * Username used for login or identification.
     */
    @Getter
    private String username;
    /**
     * Hash of the password
     */
    @Getter
    private String passwordHash;
    /**
     * Timestamp when the user was created.
     */
    @Getter
    private LocalDateTime createdOn;
    /**
     * Timestamp of the user's last activity or presence.
     */
    private LocalDateTime lastSeenOn;
    /**
     * Set of roles assigned to the user.
     */
    @Getter
    Set<UserRole> roles = new HashSet<>();

    /**
     * Constructs a new User with the specified name, surname, and username.
     * Initializes the user with a unique ID, the current timestamp, and a default role.
     *
     * @param name     The first name of the user.
     * @param surname  The last name of the user.
     * @param username The username for the user.
     * @param password Password in plaintext
     */
    public User(String name, String surname, String username, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.username = username;
        createdOn = LocalDateTime.now();
        lastSeenOn = createdOn;

        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .addRandomSalt(32)
                .withArgon2();
        passwordHash = hash.getResult();

        roles.add(UserRole.VIEW_ONLY_USER);
    }

    /**
     * Updates the user's last seen time to the current time.
     */
    public void setLastSeenAtNow() {
        lastSeenOn = LocalDateTime.now();
    }

    /**
     * Retrieves the last seen timestamp of the user.
     *
     * @return An {@link Optional} containing the last seen time, or empty if never set.
     */
    public Optional<LocalDateTime> getLastSeenOn() {
        return Optional.ofNullable(lastSeenOn);
    }

    public void addRole(UserRole role) {
        roles.add(role);
    }

    public UserListRecord getRecord() {
        return new UserListRecord(
                id.toString(),
                username,
                name + " " + surname
        );
    }

    public UserFullRecord getFullRecord() {
        Set<String> roleNames = new HashSet<>();
        roles.forEach(role -> roleNames.add(role.getRoleName()));

        return new UserFullRecord(
                id.toString(),
                name,
                surname,
                username,
                createdOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                lastSeenOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                roleNames
        );
    }
}

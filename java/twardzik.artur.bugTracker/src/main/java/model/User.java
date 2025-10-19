package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a user in the system with basic profile information,
 * unique identifier, creation and activity timestamps, and a list of roles.
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class User {
    /**
     * Unique identifier for the user.
     */
    private UUID id;
    /**
     * First name of the user.
     */
    private String name;
    /**
     * Surname (last name) of the user.
     */
    private String surname;
    /**
     * Username used for login or identification.
     */
    private String username;
    /**
     * Timestamp when the user was created.
     */
    private LocalDateTime createdAt;
    /**
     * Timestamp of the user's last activity or presence.
     */
    private LocalDateTime lastSeenAt;
    /**
     * List of roles assigned to the user.
     */
    List<UserRole> roles;

    /**
     * Constructs a new User with the specified name, surname, and username.
     * Initializes the user with a unique ID, the current timestamp, and a default role.
     *
     * @param name     The first name of the user.
     * @param surname  The last name of the user.
     * @param username The username for the user.
     */
    public User(String name, String surname, String username) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.username = username;
        createdAt = LocalDateTime.now();

        roles = new ArrayList<>();
        roles.add(UserRole.VIEW_ONLY_USER);
    }

    /**
     * Updates the user's last seen time to the current time.
     */
    public void setLastSeenAtNow() {
        lastSeenAt = LocalDateTime.now();
    }

    /**
     * Retrieves the last seen timestamp of the user.
     *
     * @return An {@link Optional} containing the last seen time, or empty if never set.
     */
    public Optional<LocalDateTime> getLastSeenAt() {
        return Optional.ofNullable(lastSeenAt);
    }

    /**
     * Gets the user's first name.
     *
     * @return The first name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's surname.
     *
     * @return The surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Gets the user's username.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the timestamp when the user was created.
     *
     * @return The creation timestamp of the user.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the list of roles assigned to the user.
     *
     * @return A list of {@link UserRole} assigned to the user.
     */
    public List<UserRole> getRoles() {
        return roles;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The UUID of the user.
     */
    public UUID getId() {
        return id;
    }
}

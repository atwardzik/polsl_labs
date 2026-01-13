package model;

import com.password4j.Hash;
import com.password4j.Password;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user in the system with basic profile information,
 * unique identifier, creation and activity timestamps, and a list of roles.
 *
 * @author Artur Twardzik
 * @version 0.6
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
@Getter
@ToString
@NoArgsConstructor
public class User {
    /**
     * Unique identifier for the user.
     * Auto-generated and cannot be updated.
     */
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    /**
     * The user's first name.
     * Cannot be blank and has a maximum length of 30 characters.
     */
    @NotBlank
    @Column(nullable = false, length = 30)
    private String name;

    /**
     * The user's surname.
     * Must start with an uppercase letter followed by lowercase letters.
     * Cannot be blank and has a maximum length of 30 characters.
     */
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Surname must contain letters only")
    @Column(nullable = false, length = 30)
    private String surname;

    /**
     * The user's unique username.
     * Cannot be blank and has a maximum length of 30 characters.
     */
    @NotBlank
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    /**
     * Hashed representation of the user's password.
     * Cannot be null.
     */
    @NotNull
    @Column(nullable = false, length = 255)
    private String passwordHash;

    /**
     * The timestamp when the user was created.
     * Cannot be null.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdOn;

    /**
     * The last time the user was seen (last login or activity).
     * Can be null if the user has never been active after creation.
     */
    private LocalDateTime lastSeenOn;

    /**
     * Creates a new {@link User} instance with the specified details.
     * <p>
     * The password is hashed using Argon2 with salt and a shared secret pepper.
     * The creation timestamp and last seen timestamp are initialized to the current time.
     *
     * @param name          the first name of the user; must not be null
     * @param surname       the surname of the user; must not be null
     * @param username      the unique username of the user; must not be null
     * @param plainPassword the plain text password to be hashed; must not be null or blank
     * @return a new {@link User} instance
     * @throws NullPointerException     if name, surname, or username is null
     * @throws IllegalArgumentException if the password is null or blank
     */
    public static User create(
            String name,
            String surname,
            String username,
            String plainPassword
    ) {
        User user = new User();

        user.name = Objects.requireNonNull(name);
        user.surname = Objects.requireNonNull(surname);
        user.username = Objects.requireNonNull(username);

        user.passwordHash = hashPassword(plainPassword);

        user.createdOn = LocalDateTime.now();
        user.lastSeenOn = user.createdOn;

        return user;
    }

    /**
     * Hashes the provided plain text password using Argon2 with random salt and a shared secret pepper.
     *
     * @param password the plain text password; must not be null or blank
     * @return the hashed password
     * @throws IllegalArgumentException if the password is null or blank
     */
    private static String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .addRandomSalt(32)
                .withArgon2();

        return hash.getResult();
    }

    /**
     * Returns a simplified record of the user for list displays.
     * Combines the user's full name and username.
     *
     * @return a {@link UserListRecord} containing user id, username, and full name
     */
    public UserListRecord getRecord() {
        return new UserListRecord(
                id.toString(),
                username,
                name + " " + surname
        );
    }

    /**
     * Returns a full record of the user including roles and timestamps.
     *
     * @return a {@link UserFullRecord} containing id, name, surname, username,
     *         creation date, last seen date (or "N/A" if never seen), and roles
     */
    public UserFullRecord getFullRecord() {
        Set<String> roleNames = new HashSet<>();
        roleNames.add("STANDARD_USER");

        return new UserFullRecord(
                id.toString(),
                name,
                surname,
                username,
                createdOn.toLocalDate().toString(),
                lastSeenOn != null ? lastSeenOn.toLocalDate().toString() : "N/A",
                roleNames
        );
    }
}

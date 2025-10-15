package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class representing a user
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class User {
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastSeenAt;
    List<UserRole> roles;

    public User(String name, String surname, String username) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.username = username;
        createdAt = LocalDateTime.now();

        roles = new ArrayList<>();
        roles.add(UserRole.VIEW_ONLY_USER);
    }

    public void setLastSeenAtNow() {
        lastSeenAt = LocalDateTime.now();
    }

    public Optional<LocalDateTime> getLastSeenAt() {
        return Optional.ofNullable(lastSeenAt);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public UUID getId() {
        return id;
    }
}

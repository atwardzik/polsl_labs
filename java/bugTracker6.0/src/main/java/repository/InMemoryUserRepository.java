package repository;

import model.Issue;
import model.User;

import java.lang.reflect.Field;
import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            try {
                Field idField = User.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(user, UUID.randomUUID());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to set ID via reflection", e);
            }
        }
        storage.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
}
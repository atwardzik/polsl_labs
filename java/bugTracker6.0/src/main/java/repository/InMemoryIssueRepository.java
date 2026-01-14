package repository;

import model.BugStatus;
import model.Issue;
import model.Priority;
import model.User;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryIssueRepository implements IssueRepository {

    private final Map<UUID, Issue> storage = new HashMap<>();

    @Override
    public void save(Issue issue) {
        if (issue.getId() == null) {
            try {
                Field idField = Issue.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(issue, UUID.randomUUID());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to set ID via reflection", e);
            }
        }

        storage.put(issue.getId(), issue);
    }

    @Override
    public Optional<Issue> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Issue> findByTitle(String title) {
        return storage.values().stream()
                .filter(i -> i.getTitle().equals(title))
                .findFirst();
    }

    @Override
    public List<Issue> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Issue> findByStatus(BugStatus status) {
        return storage.values().stream()
                .filter(i -> i.getStatus() == status)
                .toList();
    }

    @Override
    public List<Issue> findByPriority(Priority priority) {
        return storage.values().stream()
                .filter(i -> i.getPriority() == priority)
                .toList();
    }

    @Override
    public List<Issue> findByAuthor(User user) {
        return storage.values().stream()
                .filter(i -> i.getReporter().equals(user))
                .toList();
    }

    @Override
    public List<Issue> findCreatedAfter(LocalDateTime date) {
        return storage.values().stream()
                .filter(i -> i.getCreatedAt().isAfter(date))
                .toList();
    }

    @Override
    public List<Issue> findCreatedBefore(LocalDateTime date) {
        return storage.values().stream()
                .filter(i -> i.getCreatedAt().isBefore(date))
                .toList();
    }

    @Override
    public List<Issue> findByTitleContaining(String fragment) {
        String lower = fragment.toLowerCase();
        return storage.values().stream()
                .filter(i -> i.getTitle().toLowerCase().contains(lower))
                .toList();
    }
}

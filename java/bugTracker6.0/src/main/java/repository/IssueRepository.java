package repository;

import model.BugStatus;
import model.Issue;
import model.Priority;
import model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueRepository {
    void save(Issue issue);

    Optional<Issue> findById(UUID id);

    Optional<Issue> findByTitle(String title);

    List<Issue> findAll();

    List<Issue> findByStatus(BugStatus status);

    List<Issue> findByPriority(Priority priority);

    List<Issue> findByReporter(User reporter);

    List<Issue> findCreatedAfter(LocalDateTime date);

    List<Issue> findCreatedBefore(LocalDateTime date);

    List<Issue> findByTitleContaining(String titleFragment);
}

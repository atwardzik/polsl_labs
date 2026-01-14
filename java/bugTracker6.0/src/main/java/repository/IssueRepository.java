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
    /**
     * Adds a new issue to the manager or updates existing (determined based on existence of ID in the table)
     *
     * @param issue the issue to be added
     */
    void save(Issue issue);

    /**
     * Finds an issue by its unique identifier.
     *
     * @param id the UUID of the issue to search for
     * @return an Optional containing the issue if found, otherwise empty
     */
    Optional<Issue> findById(UUID id);

    /**
     * Finds an issue by its title.
     *
     * @param title the title of the issue to search for
     * @return an Optional containing the issue if found, otherwise empty
     */
    Optional<Issue> findByTitle(String title);

    /**
     * Finds all issues
     *
     * @return list of existing issues
     */
    List<Issue> findAll();

    /**
     * Filters issues with given status
     *
     * @param status given status
     * @return a list of issues with given status
     */
    List<Issue> findByStatus(BugStatus status);

    /**
     * Filters issues with given priority
     *
     * @param priority given priority
     * @return a list of issues with given priority
     */
    List<Issue> findByPriority(Priority priority);

    /**
     * Filters issues by a given author.
     *
     * @param user the user to filter by
     * @return a list of issues created by specified author
     */
    List<Issue> findByAuthor(User user);

    /**
     * Filters issues created after specified date
     *
     * @param date the date
     * @return a list of issues created after specified date
     */
    List<Issue> findCreatedAfter(LocalDateTime date);

    /**
     * Filters issues created before specified date
     *
     * @param date the date
     * @return a list of issues created before specified date
     */
    List<Issue> findCreatedBefore(LocalDateTime date);

    /**
     * Filters issues similar to given title
     *
     * @param titleFragment title to search for
     * @return a list of issues similar to given title
     */
    List<Issue> findByTitleContaining(String titleFragment);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class for storing Issue details
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class Issue {
    /**
     * Unique identifier for this issue.
     */
    private UUID id;
    /**
     * Short descriptive title summarizing the issue.
     */
    private String title;
    /**
     * Detailed explanation of the issue or bug.
     */
    private String description;
    /**
     * Timestamp indicating when the issue was created.
     */
    private LocalDateTime createdAt;
    /**
     * Timestamp indicating the last time the issue was updated. May be null.
     */
    private LocalDateTime updatedAt;
    /**
     * The deadline by which the issue should be resolved. May be null.
     */
    private LocalDateTime dueDate;
    /**
     * The user who reported this issue.
     */
    private User reporter;
    /**
     * The user currently assigned to work on this issue. May be null.
     */
    private User assignee;
    /**
     * The current status of the issue (e.g., OPEN, CLOSED, IN_PROGRESS).
     */
    private BugStatus status = BugStatus.OPEN;
    /**
     * The priority level of the issue (e.g., LOW, MEDIUM, HIGH).
     */
    private Priority priority = Priority.LOW;
    /**
     * A set of textual tags associated with this issue for categorization or filtering.
     */
    private Set<String> tags = new HashSet<>();
    /**
     * A list of comments made on this issue.
     */
    private List<Comment> comments = new ArrayList<>();

    /**
     * Creates a new Issue with the given title, description, and reporter.
     *
     * @param title       the title of the issue; must not be empty
     * @param description the detailed description of the issue; must not be empty
     * @param reporter    the user reporting the issue; must not be null
     * @throws InvalidIssueDataException if any of the required fields are invalid or empty
     */
    public Issue(String title, String description, User reporter) throws InvalidIssueDataException {
        id = UUID.randomUUID();

        setTitle(title);

        setDescription(description);

        if (reporter == null) {
            throw new InvalidIssueDataException("Reporter cannot be empty.");
        }
        this.reporter = reporter;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Creates a new Issue with additional optional data such as due date, assignee, status, priority, and tags.
     *
     * @param title       the issue title; must not be empty
     * @param description the issue description; must not be empty
     * @param reporter    the reporter of the issue; must not be null
     * @param dueDate     the due date; may be null
     * @param assignee    the user assigned to the issue; may be null
     * @param status      the current bug status; defaults to BugStatus::OPEN if null
     * @param priority    the issue priority; defaults to Priority::LOW if null
     * @param tags        the set of tags; may be null or empty
     * @throws InvalidIssueDataException if title, description, or reporter are invalid
     */
    public Issue(String title, String description, User reporter, LocalDateTime dueDate, User assignee, BugStatus status, Priority priority, Set<String> tags) throws InvalidIssueDataException {
        this(title, description, reporter);

        this.priority = Objects.requireNonNullElse(priority, Priority.LOW);
        this.status = Objects.requireNonNullElse(status, BugStatus.OPEN);

        this.dueDate = dueDate;
        this.assignee = assignee;
        this.tags = tags;
    }

    /**
     * Assigns a user to this issue.
     *
     * @param user the user to assign; must not be null
     * @throws InvalidIssueDataException if user is null
     */
    public void assignUser(User user) throws InvalidIssueDataException {
        if (user == null) {
            throw new InvalidIssueDataException("Cannot assign to non-existing user.");
        }

        assignee = user;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the description of the issue.
     *
     * @param description the new description; must not be empty
     * @throws InvalidIssueDataException if the description is empty
     */
    public void setDescription(String description) throws InvalidIssueDataException {
        if (description.isEmpty()) {
            throw new InvalidIssueDataException("Issue description cannot be empty.");
        }

        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the status of this issue.
     *
     * @param status the new status; must not be null
     * @throws InvalidIssueDataException if status is null
     */
    public void setStatus(BugStatus status) throws InvalidIssueDataException {
        if (status == null) {
            throw new InvalidIssueDataException("Status cannot be null.");
        }

        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the title of this issue.
     *
     * @param title the new title; must not be empty
     * @throws InvalidIssueDataException if the title is empty
     */
    public void setTitle(String title) throws InvalidIssueDataException {
        if (title.isEmpty()) {
            throw new InvalidIssueDataException("Issue name cannot be empty.");
        }

        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the due date of this issue.
     *
     * @param dueDate the new due date; must be in the future
     * @throws InvalidIssueDataException if dueDate is before the current time
     */
    public void setDueDate(LocalDateTime dueDate) throws InvalidIssueDataException {
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new InvalidIssueDataException("Due date must be in the future.");
        }

        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the priority of this issue.
     *
     * @param priority the new priority; must not be null
     * @throws InvalidIssueDataException if priority is null
     */
    public void setPriority(Priority priority) throws InvalidIssueDataException {
        if (priority == null) {
            throw new InvalidIssueDataException("Priority cannot be null.");
        }

        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the assignee for this issue.
     *
     * @param assignee the new assignee; may be null
     */
    public void setAssignee(User assignee) {
        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Adds a tag to the issue.
     *
     * @param tag the tag to add; ignored if blank
     */
    public void addTag(String tag) {
        if (!tag.isBlank()) {
            tags.add(tag);
        }

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Adds a comment to this issue.
     *
     * @param comment the comment to add
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // getters

    /**
     * @return the unique identifier of this issue
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the title of this issue
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the detailed description of this issue
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the user who reported this issue
     */
    public User getReporter() {
        return reporter;
    }

    /**
     * @return the current bug status
     */
    public BugStatus getStatus() {
        return status;
    }

    /**
     * @return the current priority level
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @return the timestamp when this issue was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return the set of tags assigned to this issue
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * @return the list of comments associated with this issue
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @return an Optional containing the timestamp at which the issue was updated, or empty if not set
     */
    public Optional<LocalDateTime> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    /**
     * @return an Optional containing the due date, or empty if not set
     */
    public Optional<LocalDateTime> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    /**
     * @return an Optional containing the assignee, or empty if unassigned
     */
    public Optional<User> getAssignee() {
        return Optional.ofNullable(assignee);
    }
}

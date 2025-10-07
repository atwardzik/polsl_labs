/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * To be honest I am not sure if it shouldn't be a totally opaque class. What is the purpose of making a class
 * with million of getters and setters and no logic behind?
 *
 * @author SuperStudent-PL
 */
public class Issue {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private User reporter;
    private User assignee;
    private BugStatus status = BugStatus.OPEN;
    private Priority priority = Priority.LOW;
    private Set<String> tags = new HashSet<>();
    private List<Comment> comments = new ArrayList<>();

    public Issue(String title, String description, User reporter) throws Exception {
        id = UUID.randomUUID();

        setTitle(title);

        setDescription(description);

        if (reporter == null) {
            throw new Exception("Reporter cannot be empty.");
        }
        this.reporter = reporter;

        this.createdAt = LocalDateTime.now();
    }

    public Issue(String title, String description, User reporter, LocalDateTime dueDate, User assignee, BugStatus status, Priority priority, Set<String> tags) throws Exception {
        this(title, description, reporter);

        this.priority = Objects.requireNonNullElse(priority, Priority.LOW);
        this.status = Objects.requireNonNullElse(status, BugStatus.OPEN);

        this.dueDate = dueDate;
        this.assignee = assignee;
        this.tags = tags;
    }

    public void assignUser(User user) throws Exception {
        if (user == null) {
            throw new Exception("Cannot assign to non-existing user.");
        }

        assignee = user;
    }

    public void setDescription(String description) throws Exception {
        if (description.isEmpty()) {
            throw new Exception("Issue description cannot be empty.");
        }

        this.description = description;
    }

    public void setStatus(BugStatus status) throws Exception {
        if (status == null) {
            throw new Exception("Status cannot be null.");
        }

        this.status = status;
    }

    public void setTitle(String title) throws Exception {
        if (title.isEmpty()) {
            throw new Exception("Issue name cannot be empty.");
        }

        this.title = title;
    }

    public void setDueDate(LocalDateTime dueDate) throws Exception {
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new Exception("Due date must be in the future.");
        }

        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) throws Exception {
        if (priority == null) {
            throw new Exception("Priority cannot be null.");
        }

        this.priority = priority;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public void addTag(String tag) {
        if (!tag.isBlank()) {
            tags.add(tag);
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // getters

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public User getReporter() {
        return reporter;
    }

    public User getAssignee() {
        return assignee;
    }

    public BugStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Set<String> getTags() {
        return tags;
    }

    public List<Comment> getComments() {
        return comments;
    }
}

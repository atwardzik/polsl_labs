/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class for storing Issue details
 *
 * @author Artur Twardzik
 * @version 0.6
 */
@Entity
@Table(name = "issues")
@Valid
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    /**
     * Unique identifier for the issue.
     * Auto-generated and cannot be updated.
     */
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    /**
     * Title of the issue.
     * Cannot be blank and has a maximum length of 200 characters.
     */
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Detailed description of the issue.
     * Cannot be blank and has a maximum length of 4000 characters.
     */
    @NotBlank
    @Size(max = 4000)
    @Column(nullable = false, length = 4000)
    private String description;

    /**
     * Timestamp when the issue was created.
     * Cannot be null.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the issue was last updated.
     * Automatically updated on any change.
     */
    private LocalDateTime updatedAt;

    /**
     * Optional due date of the issue.
     * Must be in the future if set.
     */
    private LocalDateTime dueDate;

    /**
     * The user who reported the issue.
     * Cannot be null.
     */
    @NotNull
    @ManyToOne(optional = false)
    private User reporter;

    /**
     * The user assigned to handle the issue.
     * Can be null if unassigned.
     */
    @ManyToOne
    private User assignee;

    /**
     * The current status of the issue.
     * Defaults to {@link BugStatus#OPEN} if not specified.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugStatus status = BugStatus.OPEN;

    /**
     * The priority of the issue.
     * Defaults to {@link Priority#LOW} if not specified.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.LOW;

    /**
     * Constructs a new Issue with the required fields.
     * Automatically sets the creation and update timestamps to now.
     *
     * @param title    the issue title; must not be null or blank
     * @param description the issue description; must not be null or blank
     * @param reporter the user who reported the issue; must not be null
     * @throws InvalidIssueDataException if title, description, or reporter are invalid
     */
    public Issue(String title, String description, User reporter) {
        setTitle(title);
        setDescription(description);

        if (reporter == null) {
            throw new InvalidIssueDataException("Reporter cannot be null");
        }

        this.reporter = reporter;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
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
     * @throws InvalidIssueDataException if title, description, or reporter are invalid
     */
    public Issue(String title, String description, User reporter, LocalDateTime dueDate, User assignee, BugStatus status, Priority priority) throws InvalidIssueDataException {
        this(title, description, reporter);

        this.priority = Objects.requireNonNullElse(priority, Priority.LOW);
        this.status = Objects.requireNonNullElse(status, BugStatus.OPEN);

        this.dueDate = dueDate;
        this.assignee = assignee;
    }

    /**
     * Assigns a user to this issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param user the user to assign; must not be null
     */
    public void assignUser(@NotNull User user) {
        this.assignee = Objects.requireNonNull(user, "Assignee cannot be null");
        touch();
    }

    /**
     * Sets the status of the issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param status the new status; must not be null
     */
    public void setStatus(@NotNull BugStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        touch();
    }

    /**
     * Sets the priority of the issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param priority the new priority; must not be null
     */
    public void setPriority(@NotNull Priority priority) {
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        touch();
    }

    /**
     * Sets or updates the due date of the issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param dueDate the new due date; must be in the future if not null
     * @throws InvalidIssueDataException if the due date is in the past
     */
    public void setDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new InvalidIssueDataException("Due date must be in the future");
        }
        this.dueDate = dueDate;
        touch();
    }

    /**
     * Sets or updates the issue's title.
     * Updates the issue's updated timestamp automatically.
     *
     * @param title the new title; must not be null or blank
     * @throws InvalidIssueDataException if the title is null or blank
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidIssueDataException("Title cannot be empty");
        }
        this.title = title;
        touch();
    }

    /**
     * Sets or updates the issue's description.
     * Updates the issue's updated timestamp automatically.
     *
     * @param description the new description; must not be null or blank
     * @throws InvalidIssueDataException if the description is null or blank
     */
    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidIssueDataException("Description cannot be empty");
        }
        this.description = description;
        touch();
    }

    /**
     * Updates the issue's updated timestamp to the current time.
     */
    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the current assignee of the issue, if any.
     *
     * @return an {@link Optional} containing the assignee, or empty if unassigned
     */
    public Optional<User> getAssignee() {
        return Optional.ofNullable(assignee);
    }

    /**
     * Returns the current due date of the issue, if any.
     *
     * @return an {@link Optional} containing the due date, or empty if not set
     */
    public Optional<LocalDateTime> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    /**
     * Returns a simplified record of the issue for list displays.
     *
     * @return an {@link IssueListRecord} containing status, ID, title, reporter username,
     *         assignee username, creation date, and due date
     */
    public IssueListRecord getRecord() {
        String due = "N/A";
        if (dueDate != null) {
            due = dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        String assignedUser = "N/A";
        if (assignee != null) {
            assignedUser = assignee.getUsername();
        }

        return new IssueListRecord(status,
                "#" + id.toString(),
                title,
                reporter.getUsername(),
                assignedUser,
                createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                due
        );
    }

    /**
     * Returns a full record of the issue with detailed information.
     *
     * @return an {@link IssueFullRecord} containing status, priority, ID, title,
     *         reporter details, creation date, due date, and description
     */
    public IssueFullRecord getFullRecord() {
        String due = "N/A";
        if (dueDate != null) {
            due = dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        return new IssueFullRecord(status,
                priority,
                "#" + id.toString(),
                title,
                reporter.getUsername(),
                reporter.getName() + " " + reporter.getSurname(),
                createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                due,
                description
        );
    }
}

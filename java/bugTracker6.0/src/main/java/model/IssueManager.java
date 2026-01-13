/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import repository.IssueRepository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class holding all issues
 *
 * @author Artur Twardzik
 * @version 0.6
 */
public class IssueManager {
    /**
     * Repository used to perform CRUD operations on {@link Issue} entities.
     */
    private final IssueRepository issueRepository;

    /**
     * Constructs a new {@link IssueManager} with the specified repository.
     *
     * @param issueRepository the repository for accessing issue data; must not be null
     */
    public IssueManager(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * Creates a new issue with required fields: title, description, and reporter.
     * Automatically sets creation and update timestamps.
     *
     * @param title       the issue title; must not be blank
     * @param description the issue description; must not be blank
     * @param reporter    the reporter of the issue; must not be null
     * @return the newly created {@link Issue} object
     * @throws InvalidIssueDataException if title, description, or reporter are invalid
     */
    public Issue createIssue(String title, String description, User reporter) {
        Issue issue = new Issue(title, description, reporter);
        issueRepository.save(issue);
        return issue;
    }

    /**
     * Creates a new issue with additional optional data such as due date, assignee, status, and priority.
     *
     * @param title       the issue title; must not be blank
     * @param description the issue description; must not be blank
     * @param reporter    the reporter of the issue; must not be null
     * @param dueDate     optional due date; must be in the future if provided
     * @param assignee    optional user assigned to the issue
     * @param status      optional bug status; defaults to {@link BugStatus#OPEN} if null
     * @param priority    optional issue priority; defaults to {@link Priority#LOW} if null
     * @return the newly created {@link Issue} object
     * @throws InvalidIssueDataException if title, description, or reporter are invalid
     */
    public Issue createIssue(String title, String description, User reporter, LocalDateTime dueDate, User assignee, BugStatus status, Priority priority) {
        Issue issue = new Issue(title, description, reporter, dueDate, assignee, status, priority);
        issueRepository.save(issue);
        return issue;
    }

    /**
     * Assigns a user to an existing issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param issueId the ID of the issue to assign
     * @param user    the user to assign; must not be null
     * @throws IssueNotFoundException if no issue exists with the given ID
     */
    public void assignIssue(UUID issueId, User user) {
        Issue issue = getIssue(issueId);
        issue.assignUser(user);
        issueRepository.save(issue);
    }

    /**
     * Changes the status of an existing issue.
     * Updates the issue's updated timestamp automatically.
     *
     * @param issueId the ID of the issue to update
     * @param status  the new status; must not be null
     * @throws IssueNotFoundException if no issue exists with the given ID
     */
    public void changeStatus(UUID issueId, BugStatus status) {
        Issue issue = getIssue(issueId);
        issue.setStatus(status);
        issueRepository.save(issue);
    }

    /**
     * Finds all issues with the specified status.
     *
     * @param status the status to filter by
     * @return a list of {@link Issue} objects matching the status
     */
    public List<Issue> findByStatus(BugStatus status) {
        return issueRepository.findByStatus(status);
    }

    /**
     * Finds all issues with the specified priority.
     *
     * @param priority the priority to filter by
     * @return a list of {@link Issue} objects matching the priority
     */
    public List<Issue> findByPriority(Priority priority) {
        return issueRepository.findByPriority(priority);
    }

    /**
     * Finds all issues reported by the specified user.
     *
     * @param reporter the reporter to filter by
     * @return a list of {@link Issue} objects reported by the given user
     */
    public List<Issue> findByReporter(User reporter) {
        return issueRepository.findByReporter(reporter);
    }

    /**
     * Finds all issues created after the specified date.
     *
     * @param date the cutoff date
     * @return a list of {@link Issue} objects created after the specified date
     */
    public List<Issue> findCreatedAfter(LocalDateTime date) {
        return issueRepository.findCreatedAfter(date);
    }

    /**
     * Finds all issues with a title containing the specified fragment.
     *
     * @param titleFragment a string fragment to search for in issue titles
     * @return a list of {@link Issue} objects with titles containing the fragment
     */
    public List<Issue> findByTitle(String titleFragment) {
        return issueRepository.findByTitleContaining(titleFragment);
    }

    /**
     * Retrieves a single issue by its ID.
     *
     * @param id the UUID of the issue
     * @return the {@link Issue} object
     * @throws IssueNotFoundException if no issue exists with the given ID
     */
    public Issue getIssue(UUID id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
    }

    /**
     * Returns a list of simplified issue records for all issues.
     * Useful for displaying issues in lists or tables.
     *
     * @return a list of {@link IssueListRecord} objects representing all issues
     */
    public List<IssueListRecord> getAllIssueRecords() {
        return issueRepository.findAll().stream()
                .map(Issue::getRecord)
                .toList();
    }
}

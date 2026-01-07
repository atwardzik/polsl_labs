/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class holding all issues
 *
 * @author Artur Twardzik
 * @version 0.5
 */
public class IssueManager {
    /**
     * The list of all issues managed by this manager.
     */
    private List<Issue> issues = new ArrayList<>();

    /**
     * Adds a new issue to the manager.
     *
     * @param issue the issue to be added
     */
    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    /**
     * Finds an issue by its unique identifier.
     *
     * @param issueID the UUID of the issue to search for
     * @return an Optional containing the issue if found, otherwise empty
     */
    public Optional<Issue> findIssue(UUID issueID) {
        for (Issue issue : issues) {
            if (issue.getId().equals(issueID)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    /**
     * Finds an issue by its title.
     *
     * @param title the title of the issue to search for
     * @return an Optional containing the issue if found, otherwise empty
     */
    public Optional<Issue> findIssue(String title) {
        for (Issue issue : issues) {
            if (issue.getTitle().equals(title)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    /**
     * Finds an issue by a fragment of its ID string.
     *
     * @param issueID the beginning part of the issue ID to search for
     * @return an Optional containing the issue if a match is found, otherwise empty
     */
    public Optional<Issue> findIssueByIdFragment(String issueID) {
        for (Issue issue : issues) {
            if (issue.getId().toString().startsWith(issueID)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    /**
     * Assigns a user to a specific issue.
     *
     * @param issue the issue to assign
     * @param user  the user to assign to the issue
     * @throws IssueNotFoundException if the issue does not exist in the manager
     */
    public void assignIssue(Issue issue, User user) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.assignUser(user);
    }

    /**
     * Updates the status of an issue.
     *
     * @param issue  the issue to update
     * @param status the new status to set
     * @throws IssueNotFoundException if the issue does not exist in the manager
     */
    public void setIssueStatus(Issue issue, BugStatus status) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.setStatus(status);
    }

    /**
     * Adds a comment to a specific issue.
     *
     * @param issue   the issue to comment on
     * @param comment the comment to add
     * @throws IssueNotFoundException if the issue does not exist in the manager
     */
    public void commentIssue(Issue issue, Comment comment) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.addComment(comment);
    }

    /**
     * Filters issues by a given tag.
     *
     * @param tag the tag to filter by
     * @return a list of issues that contain the specified tag
     */
    public List<Issue> filterByTag(String tag) {
        return issues.stream().filter(issue -> issue.getTags().contains(tag)).toList();
    }

    /**
     * Filters issues by a given author.
     *
     * @param user the user to filter by
     * @return a list of issues created by specified author
     */
    public List<Issue> filterByAuthor(User user) {
        return issues.stream().filter(issue -> issue.getReporter().equals(user)).toList();
    }

    /**
     * Filters issues created after specified date
     *
     * @param start the date
     * @return a list of issues created after specified date
     */
    public List<Issue> filterByDateAfter(LocalDateTime start) {
        return issues.stream().filter(issue -> issue.getCreatedAt().isAfter(start)).toList();
    }

    /**
     * Filters issues created before specified date
     *
     * @param end the date
     * @return a list of issues created before specified date
     */
    public List<Issue> filterByDateBefore(LocalDateTime end) {
        return issues.stream().filter(issue -> issue.getCreatedAt().isBefore(end)).toList();
    }

    /**
     * Filters issues starting with given id
     *
     * @param issueID the id fragment
     * @return a list of issues starting with specified if fragment
     */
    public List<Issue> filterByIdFragment(String issueID) {
        return issues.stream().filter(issue -> issue.getId().toString().startsWith(issueID)).toList();
    }

    /**
     * Filters issues with given priority
     *
     * @param priority given priority
     * @return a list of issues with given priority
     */
    public List<Issue> filterByPriority(Priority priority) {
        return issues.stream().filter(issue -> issue.getPriority() == priority).toList();
    }

    /**
     * Filters issues with given status
     *
     * @param status given status
     * @return a list of issues with given status
     */
    public List<Issue> filterByStatus(BugStatus status) {
        return issues.stream().filter(issue -> issue.getStatus() == status).toList();
    }

    /**
     * Filters issues similar to given title
     *
     * @param title title to search for
     * @return a list of issues similar to given title
     */
    public List<Issue> filterByTitle(String title) {
        if (title == null || title.isBlank()) {
            return new ArrayList<>(issues); // return all if no filter
        }

        String lowerTitle = title.toLowerCase();

        return issues.stream().filter(issue -> issue.getTitle().toLowerCase().contains(lowerTitle)).toList();
    }

    /**
     * Retrieves all issues managed by this instance.
     *
     * @return a list of all issues
     */
    public List<Issue> getAllIssues() {
        return new ArrayList<>(issues);
    }

    /**
     * Retrieves all issues records
     *
     * @return issues list as String records
     */
    public List<IssueListRecord> getAllIssuesRecords() {
        List<IssueListRecord> recs = new ArrayList<>();

        issues.forEach(elem -> recs.add(elem.getRecord()));

        return recs;
    }
}

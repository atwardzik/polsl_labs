/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.*;

/**
 * Class holding all issues
 *
 * @author Artur Twardzik
 * @version 0.1
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
        List<Issue> issuesWithTag = new ArrayList<>();

        for (Issue issue : issues) {
            Set<String> issueTags = issue.getTags();

            if (issueTags.contains(tag)) {
                issuesWithTag.add(issue);
            }
        }

        return issuesWithTag;
    }

    /**
     * Retrieves all issues managed by this instance.
     *
     * @return a list of all issues
     */
    public List<Issue> getAllIssues() {
        return issues;
    }
}

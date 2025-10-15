/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.*;

/**
 *
 * @author SuperStudent-PL
 */
public class IssueManager {
    private List<Issue> issues = new ArrayList<>();

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public Optional<Issue> findIssue(UUID issueID) {
        for (Issue issue : issues) {
            if (issue.getId().equals(issueID)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    public Optional<Issue> findIssue(String title) {
        for (Issue issue : issues) {
            if (issue.getTitle().equals(title)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    public Optional<Issue> findIssueByIdFragment(String issueID) {
        for (Issue issue : issues) {
            if (issue.getId().toString().startsWith(issueID)) {
                return Optional.of(issue);
            }
        }

        return Optional.empty();
    }

    public void assignIssue(Issue issue, User user) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.assignUser(user);
    }

    public void setIssueStatus(Issue issue, BugStatus status) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.setStatus(status);
    }

    public void commentIssue(Issue issue, Comment comment) throws IssueNotFoundException {
        if (findIssue(issue.getId()).isEmpty()) {
            throw new IssueNotFoundException(issue.getId());
        }

        issue.addComment(comment);
    }

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

    public List<Issue> getAllIssues() {
        return issues;
    }
}

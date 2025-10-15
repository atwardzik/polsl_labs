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

    public void assignIssue(UUID issueID, User user) throws Exception {
        Issue issue = findIssue(issueID).orElseThrow(() -> new IssueNotFoundException(issueID));

        issue.assignUser(user);
    }

    public void setIssueStatus(UUID issueID, BugStatus status) throws Exception {
        Issue issue = findIssue(issueID).orElseThrow(() -> new IssueNotFoundException(issueID));

        issue.setStatus(status);
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

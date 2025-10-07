/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author SuperStudent-PL
 */
public class IssueManager {
    private List<Issue> issues = new ArrayList<>();

    public void addTask(Issue issue) {
        issues.add(issue);
    }

    public Issue findIssue(UUID issueID) {
        for (Issue issue : issues) {
            if (issue.getId().equals(issueID)) {
                return issue;
            }
        }

        return null;
    }

    public Issue findIssue(String title) {
        for (Issue issue : issues) {
            if (issue.getTitle().equals(title)) {
                return issue;
            }
        }

        return null;
    }

    public void assignIssue(UUID issueID, User user) throws Exception {
        Issue issue = findIssue(issueID);

        if (issue == null) {
            throw new Exception("There is no such task.");
        }

        issue.assignUser(user);
    }

    public void setIssueStatus(UUID issueID, BugStatus status) throws Exception {
        Issue issue = findIssue(issueID);

        if (issue == null) {
            throw new Exception("There is no such task.");
        }

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

    public List<Issue> getAllTasks() {
        return issues;
    }
}

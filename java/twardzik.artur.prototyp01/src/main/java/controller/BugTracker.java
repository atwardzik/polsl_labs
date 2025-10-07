package controller;

import model.IssueManager;

public abstract class BugTracker {
    protected IssueManager manager;

    protected BugTracker(IssueManager manager) {
        this.manager = manager;
    }

    public abstract void createIssue();

    public abstract void updateIssue();

    public abstract void viewIssue();

    public abstract void listIssues();

    public abstract void filterIssues();

    public abstract void changeUILanguage();
}

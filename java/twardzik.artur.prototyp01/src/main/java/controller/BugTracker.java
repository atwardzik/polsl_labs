package controller;

import model.Issue;
import model.IssueManager;
import view.BugTrackerView;

import java.util.List;

public class BugTracker {
    private IssueManager manager;
    private BugTrackerView view;

    public BugTracker(IssueManager manager, BugTrackerView view) {
        this.manager = manager;
        this.view = view;
    }

    public void run() {
        while (true) {
            view.showMainMenu();
            int choice = view.getMainMenuChoice();

            if (choice == 6) {
                break;
            }

            switch (choice) {
                case 1:
                    Issue issue = view.createIssue();
                    manager.addIssue(issue);
                    break;
                case 2:
//                    view.updateIssue();
                    break;
                case 3:
//                    view.showIssueDetails();
                    break;
                case 4:
                    List<Issue> issues = manager.getAllIssues();
                    view.showIssueList(issues);
                    break;
                case 5:
                    break;
                default:
                    view.showMainMenuError();
                    break;
            }
        }
    }
}

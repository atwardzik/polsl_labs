package view;

import model.Issue;

import java.util.List;

public interface BugTrackerView {
    void showMainMenu();

    void showMainMenuError();

    int getMainMenuChoice();

    Issue createIssue();

    void updateIssue(Issue issue);

    void filterIssues();

    void changeUILanguage();

    void showIssueDetails(Issue issue);

    void showIssueList(List<Issue> issues);

}

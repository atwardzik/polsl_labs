package view;

import model.*;

import java.util.List;

public interface BugTrackerView {
    //information
    void showMainMenu();

    void showMainMenuError();

    void showUserList(List<User> users);

    void showIssueList(List<Issue> issues);

    void showIssueDetails(Issue issue);

    void showStatusList();

    void showError(String message);

    void showErrorNoSuchUser();

    void showErrorNoSuchIssue();

    void showErrorIdTooShort();

    //user choice
    int getMainMenuChoice();

    int getChosenUser();

    BugStatus getStatus() throws IllegalArgumentException;

    String getIssueId();

    //builders/editors
    Issue createIssue(User reporter) throws InvalidIssueDataException;

    void updateIssue(Issue issue);

    void filterIssues();

    Comment createComment(User author);

    // other
    void changeUILanguage();
}

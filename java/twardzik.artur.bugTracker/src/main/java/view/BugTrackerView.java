package view;

import model.*;

import java.util.List;

/**
 * Interface for the user interface. All Views should implement this interface
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public interface BugTrackerView {
    //information

    /**
     * Shows the main menu with options to choose
     */
    void showMainMenu();

    /**
     * Shows error upon choosing incorrect option
     */
    void showMainMenuError();

    /**
     * Shows available users
     *
     * @param users list of users
     */
    void showUserList(List<User> users);

    /**
     * Prints all issues in a short form
     *
     * @param issues issues list
     */
    void showIssueList(List<Issue> issues);

    /**
     * Shows short details about issue - Short-ID, Title, Status, Reporter
     *
     * @param issue issue to be displayed
     */
    void showIssueShortDetails(Issue issue);

    /**
     * Shows comprehensive details about the issue, inclusive comments
     *
     * @param issue issue to be displayed
     */
    void showIssueDetails(Issue issue);

    /**
     * Shows available statuses for issues
     */
    void showStatusList();

    /**
     * Shows error with given message
     *
     * @param message message to be printed into standard error
     */
    void showError(String message);

    /**
     * Shows no such user exists error
     */
    void showErrorNoSuchUser();

    /**
     * Shows no such issue exists error
     */
    void showErrorNoSuchIssue();

    /**
     * Shows ID too short for determination error
     */
    void showErrorIdTooShort();

    /**
     * Shows available languages
     */
    void showUiLanguageList();

    //user choice

    /**
     * Gets the choice from main menu
     *
     * @return number specified by user
     */
    int getMainMenuChoice();

    /**
     * Gets the user
     *
     * @return index specified by user
     */
    int getChosenUser();

    /**
     * Gets bug status specified by user
     *
     * @return bug status
     * @throws IllegalArgumentException on wrong option choosen
     */
    BugStatus getStatus() throws IllegalArgumentException;

    /**
     * Gets the issue ID
     *
     * @return ID specified by user
     */
    String getIssueId();

    //builders/editors

    /**
     * Asks for all details required to create an Issue
     *
     * @param reporter user that reports an issue
     * @return created Issue
     * @throws InvalidIssueDataException on wrong data given
     */
    Issue createIssue(User reporter) throws InvalidIssueDataException;

    /**
     * Asks for all details required to update an Issue
     *
     * @param issue issue to be edited
     */
    void updateIssue(Issue issue);

    /**
     * Filters issues by specified tags. Currently unavailable
     */
    void filterIssues();

    /**
     * Creates a comment
     *
     * @param author of a comment
     * @return created comment
     */
    Comment createComment(User author);

    /**
     * Specifies the user interface language
     *
     * @return chosen language
     */
    UiLanguage chooseUiLanguage();
}

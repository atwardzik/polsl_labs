package controller;

import model.*;
import view.BugTrackerView;
import view.CliEnglishView;
import view.CliGermanView;
import view.UiLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Bug tracker Application Run point
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class BugTracker {
    private IssueManager manager;
    private BugTrackerView view;

    /**
     * Bug tracker constructor
     *
     * @param manager specified issue manager
     * @param view    specified view (CLI/GUI/web)
     */
    public BugTracker(IssueManager manager, BugTrackerView view) {
        this.manager = manager;
        this.view = view;
    }

    /**
     * Searches for issue in internal issue list
     *
     * @return Issue or none
     * @throws IssueNotFoundException
     */
    private Optional<Issue> findIssueByShortId() throws IssueNotFoundException {
        String idFragment = view.getIssueId();
        if (idFragment.length() < 6) {
            view.showErrorIdTooShort();
            return Optional.empty();
        }

        return manager.findIssueByIdFragment(idFragment);
    }

    /**
     * Changes the app language
     *
     * @param language specified UI language
     */
    private void changeLanguage(UiLanguage language) {
        if (Objects.requireNonNull(language) == UiLanguage.ENGLISH) {
            this.view = new CliEnglishView();
        } else if (language == UiLanguage.GERMAN) {
            this.view = new CliGermanView();
        }
    }

    /**
     * Runs the application
     */
    public void run() {
        // static users for development only
        User user1 = new User("Artur", "Twardzik", "at");
        User user2 = new User("", "", "dt");
        User user3 = new User("", "", "jt");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        while (true) {
            view.showMainMenu();
            int choice = view.getMainMenuChoice();

            if (choice == 10) {
                break;
            }

            switch (choice) {
                case 1: {// create issue
                    view.showUserList(userList);

                    try {
                        User user = userList.get(view.getChosenUser());

                        Issue issue = view.createIssue(user);

                        manager.addIssue(issue);
                    } catch (IndexOutOfBoundsException _) {
                        view.showErrorNoSuchUser();
                    } catch (InvalidIssueDataException e) {
                        view.showError(e.getMessage());
                    }
                    break;
                }
                case 2: {// update issue
                    Optional<Issue> issue = findIssueByShortId();

                    if (issue.isEmpty()) {
                        view.showErrorNoSuchIssue();
                        break;
                    }

                    view.updateIssue(issue.get());

                    break;
                }
                case 3: {// change issue status
                    Optional<Issue> issue = findIssueByShortId();

                    if (issue.isEmpty()) {
                        view.showErrorNoSuchIssue();
                        break;
                    }

                    try {
                        view.showStatusList();
                        BugStatus status = view.getStatus();

                        issue.get().setStatus(status);
                    } catch (Exception e) {
                        view.showError(e.getMessage());
                    }
                    break;
                }
                case 4: {// assign issue
                    Optional<Issue> issue = findIssueByShortId();

                    if (issue.isEmpty()) {
                        view.showErrorNoSuchIssue();
                        break;
                    }

                    view.showUserList(userList);
                    try {
                        User user = userList.get(view.getChosenUser());

                        issue.get().assignUser(user);
                    } catch (IndexOutOfBoundsException _) {
                        view.showErrorNoSuchUser();
                    }
                    break;
                }
                case 5: {// comment issue
                    Optional<Issue> issue = findIssueByShortId();

                    if (issue.isEmpty()) {
                        view.showErrorNoSuchIssue();
                        break;
                    }

                    view.showUserList(userList);
                    try {
                        User user = userList.get(view.getChosenUser());

                        Comment comment = view.createComment(user);
                        if (!comment.getText().isBlank()) {
                            issue.get().addComment(comment);
                        }
                    } catch (IndexOutOfBoundsException _) {
                        view.showErrorNoSuchUser();
                    }

                    break;
                }
                case 6: { // show issue details for specified issue
                    Optional<Issue> issue = findIssueByShortId();

                    if (issue.isEmpty()) {
                        view.showErrorNoSuchIssue();
                        break;
                    }

                    view.showIssueDetails(issue.get());

                    break;
                }
                case 7: {// show all issues
                    List<Issue> issues = manager.getAllIssues();
                    view.showIssueList(issues);
                    break;
                }
                case 8: {// filter issues
                    view.showError("Unavailable feature at the moment");
                    break;
                }
                case 9: {//change UI language
                    view.showUiLanguageList();
                    changeLanguage(view.chooseUiLanguage());
                    break;
                }
                default:
                    view.showMainMenuError();
                    break;
            }
        }
    }
}

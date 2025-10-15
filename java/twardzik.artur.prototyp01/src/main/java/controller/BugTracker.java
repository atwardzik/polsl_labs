package controller;

import model.*;
import view.BugTrackerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BugTracker {
    private IssueManager manager;
    private BugTrackerView view;

    public BugTracker(IssueManager manager, BugTrackerView view) {
        this.manager = manager;
        this.view = view;
    }

    private Optional<Issue> findIssueByShortId() throws IssueNotFoundException {
        String idFragment = view.getIssueId();
        if (idFragment.length() < 6) {
            view.showErrorIdTooShort();
            return Optional.empty();
        }

        return manager.findIssueByIdFragment(idFragment);
    }


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

            if (choice == 9) {
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

                    try {
                        User user = userList.get(view.getChosenUser());

                        Comment comment = view.createComment(user);
                        if (!comment.getCommentText().isBlank()) {
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
                default:
                    view.showMainMenuError();
                    break;
            }
        }
    }
}

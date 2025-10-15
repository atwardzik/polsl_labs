package view;


import model.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * English language CLI user interface
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class CliEnglishView implements BugTrackerView {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void showMainMenu() {
        System.out.println("--- Bug-Tracker-Prototyp ---");
        System.out.println("[1] Add new issue");
        System.out.println("[2] Edit issue");
        System.out.println("[3] Change issue status");
        System.out.println("[4] Assign issue");
        System.out.println("[5] Comment issue");
        System.out.println("[6] Show issue");
        System.out.println("[7] Show all issues");
        System.out.println("[8] Filter issues");
        System.out.println("[9] Change UI language");
        System.out.println("[10] Finish");
    }


    @Override
    public int getMainMenuChoice() {
        System.out.print("Choose number: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return choice;
    }

    @Override
    public void showMainMenuError() {
        System.out.println("Chosen option is not correct.");
    }

    @Override
    public void showUserList(List<User> users) {
        System.out.println("Available users: ");
        int i = 1;
        for (User user : users) {
            System.out.println(" [" + i + "] " + user.getUsername());
            i += 1;
        }
    }

    @Override
    public int getChosenUser() {
        System.out.print("Choose a user: ");
        int choice = scanner.nextInt() - 1;
        scanner.nextLine(); //consume endline

        return choice;
    }

    @Override
    public Issue createIssue(User reporter) throws InvalidIssueDataException {
        System.out.print("Give title of a new Issue: ");
        String issueName = scanner.nextLine();

        System.out.print("Give description of an Issue: ");
        String issueDesc = scanner.nextLine();

        return new Issue(issueName, issueDesc, reporter);
    }

    @Override
    public String getIssueId() {
        System.out.print("Give first characters of an issue-ID [min. 6]: ");
        return scanner.nextLine();
    }

    @Override
    public void showStatusList() {
        System.out.println("Available status options: ");
        System.out.println("[1] Open");
        System.out.println("[2] In progress");
        System.out.println("[3] Closed");
        System.out.println("[4] Reopened");
    }

    @Override
    public BugStatus getStatus() throws IllegalArgumentException {
        System.out.print("Choose the new status: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return BugStatus.fromCode(choice);
    }

    @Override
    public void updateIssue(Issue issue) {
        System.out.print("Give a NEW title for the issue: ");
        String issueName = scanner.nextLine();
        if (!issueName.isBlank()) {
            issue.setTitle(issueName);
        }

        System.out.print("Give a NEW description for the issue: ");
        String issueDesc = scanner.nextLine();
        if (!issueDesc.isBlank()) {
            issue.setDescription(issueDesc);
        }
    }

    @Override
    public void showIssueShortDetails(Issue issue) {
        System.out.printf("| %s | %-40s | Status %-10s| Created by %-15s |", issue.getId().toString().split("-")[0], issue.getTitle(), issue.getStatus(), issue.getReporter().getUsername());
    }

    @Override
    public void showIssueDetails(Issue issue) {
        System.out.println("Issue: " + issue.getId().toString());
        System.out.println("\tTitle: " + issue.getTitle());
        System.out.println("\tDescription: " + issue.getDescription());
        System.out.println("\tStatus: " + issue.getStatus());
        System.out.println("\tAuthor: " + issue.getReporter().getUsername());
        System.out.println("\tCreated at: " + issue.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("\tUpdated at: " + issue.getUpdatedAt().map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(""));
        System.out.println();
        System.out.println("\tAssignee: " + issue.getAssignee().map(User::getUsername).orElse(""));
        System.out.println("\tDue date: " + issue.getDueDate().map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(""));
        System.out.println("\n");

        List<Comment> comments = issue.getComments();

        if (comments.isEmpty()) {
            return;
        }

        System.out.println("Comments: ");
        for (Comment comment : comments) {
            System.out.printf("[%s](%s): %s%n", comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), comment.getAuthor().getUsername(), comment.getText());
        }
    }

    @Override
    public void showIssueList(List<Issue> issues) {
        for (Issue issue : issues) {
            this.showIssueShortDetails(issue);
            System.out.println();
        }
    }

    @Override
    public void filterIssues() {

    }

    @Override
    public Comment createComment(User author) {
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        return new Comment(author, comment);
    }

    @Override
    public void showUiLanguageList() {
        System.out.println("Available languages: ");
        System.out.println("[1] English");
        System.out.println("[2] German");
    }

    @Override
    public UiLanguage chooseUiLanguage() {
        System.out.print("Choose a language: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return UiLanguage.fromCode(choice);
    }

    @Override
    public void showError(String error) {
        System.err.println(error);
    }

    @Override
    public void showErrorNoSuchUser() {
        System.err.println("User not in user list");
    }

    @Override
    public void showErrorNoSuchIssue() {
        System.err.println("Issue not in issue list");
    }

    @Override
    public void showErrorIdTooShort() {
        System.err.println("ID shall be at least 6 characters long");
    }
}

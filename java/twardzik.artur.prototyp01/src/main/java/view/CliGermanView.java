package view;

import model.Issue;
import model.User;

import java.util.List;
import java.util.Scanner;

public class CliGermanView implements BugTrackerView {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void showMainMenu() {
        System.out.println("--- Bug-Tracker-Prototyp ---");
        System.out.println("[1] Neues Issue hinzufügen");
        System.out.println("[2] Issue bearbeiten");
        System.out.println("[3] Issue anzeigen");
        System.out.println("[4] Alle Issues anzeigen");
        System.out.println("[5] Issues filtern");
        System.out.println("[6] Beenden");
        System.out.println("Wähle eine Nummer: ");
    }


    @Override
    public int getMainMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return choice;
    }

    @Override
    public void showMainMenuError() {
        System.out.println("Gewählte Option ist nicht korrekt.");
    }


    @Override
    public Issue createIssue() {
        System.out.println("Task name: ");
        String issueName = scanner.nextLine();

        System.out.println("Task description: ");
        String issueDesc = scanner.nextLine();

        System.out.println("Task user: ");
        String reporterName = scanner.nextLine();
        User reporter = new User("", "", reporterName);

        Issue issue;
        try {
            issue = new Issue(issueName, issueDesc, reporter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return issue;
    }

    @Override
    public void updateIssue(Issue issue) {

    }

    @Override
    public void showIssueDetails(Issue issue) {

    }

    @Override
    public void showIssueList(List<Issue> issues) {
        for (Issue issue : issues) {
            System.out.println("Issue: " + issue.getId());
            System.out.println("\tTitle: " + issue.getTitle());
            System.out.println("\tDescription" + issue.getDescription());
            System.out.println("\tAuthor: " + issue.getReporter().getUsername());
            System.out.println();
        }
    }

    @Override
    public void filterIssues() {

    }

    @Override
    public void changeUILanguage() {

    }
}

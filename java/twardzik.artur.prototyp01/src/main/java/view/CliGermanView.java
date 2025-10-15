package view;

import model.BugStatus;
import model.InvalidIssueDataException;
import model.Issue;
import model.User;

import java.util.List;
import java.util.Scanner;

// public class CliView implements, that would be abstract with localization strings

public class CliGermanView implements BugTrackerView {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void showMainMenu() {
        System.out.println("--- Bug-Tracker-Prototyp ---");
        System.out.println("[1] Neues Issue hinzufügen");
        System.out.println("[2] Issue bearbeiten");
        System.out.println("[3] Issue Status ändern");
        System.out.println("[4] Issue einem Benutzer zuweisen");
        System.out.println("[5] Issue Kommentieren");
        System.out.println("[6] Issue anzeigen");
        System.out.println("[7] Alle Issues anzeigen");
        System.out.println("[8] Issues filtern");
        System.out.println("[9] Beenden");
    }


    @Override
    public int getMainMenuChoice() {
        System.out.print("Wähle eine Nummer: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return choice;
    }

    @Override
    public void showMainMenuError() {
        System.out.println("Gewählte Option ist nicht korrekt.");
    }

    @Override
    public void showUserList(List<User> users) {
        System.out.println("Verfügbare Benutzer: ");
        int i = 1;
        for (User user : users) {
            System.out.println(" [" + i + "] " + user.getUsername());
            i += 1;
        }
    }

    @Override
    public int getChosenUser() {
        System.out.print("Wähle einen Benutzer: ");
        int choice = scanner.nextInt() - 1;
        scanner.nextLine(); //consume endline

        return choice;
    }

    @Override
    public Issue createIssue(User reporter) throws InvalidIssueDataException {
        System.out.print("Bitte geben Sie den Titel des Issues ein: ");
        String issueName = scanner.nextLine();

        System.out.print("Bitte geben Sie die Beschreibung des Issues ein: ");
        String issueDesc = scanner.nextLine();

        return new Issue(issueName, issueDesc, reporter);
    }

    @Override
    public String getIssueId() {
        System.out.print("Bitte geben Sie die ersten Zeichen der Issue-ID ein [mindestens 6]: ");
        return scanner.nextLine();
    }

    @Override
    public void showStatusList() {
        System.out.println("Verfügbare Status Optionen: ");
        System.out.println("[1] Eröffnet");
        System.out.println("[2] In Bearbeitung");
        System.out.println("[3] Geschlossen");
        System.out.println("[4] Wiedereröffnet");
    }

    @Override
    public BugStatus getStatus() throws IllegalArgumentException {
        System.out.print("Bitte wählen Sie den neuen Status: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); //consume endline

        return BugStatus.fromCode(choice);
    }

    @Override
    public void updateIssue(Issue issue) {

    }

    @Override
    public void showIssueDetails(Issue issue) {
        System.out.println("Issue: " + issue.getId().toString().split("-")[0]);
        System.out.println("\tTitel: " + issue.getTitle());
        System.out.println("\tBeschreibung: " + issue.getDescription());
        System.out.println("\tStatus: " + issue.getStatus());
        System.out.println("\tAutor: " + issue.getReporter().getUsername());

        if (issue.getAssignee() != null) {
            System.out.println("\tZugewiesener: " + issue.getAssignee().getUsername());
        }
    }

    @Override
    public void showIssueList(List<Issue> issues) {
        for (Issue issue : issues) {
            this.showIssueDetails(issue);
            System.out.println();
        }
    }

    @Override
    public void filterIssues() {

    }

    @Override
    public void changeUILanguage() {

    }

    @Override
    public void showError(String error) {
        System.err.println(error);
    }

    @Override
    public void showErrorNoSuchUser() {
        System.err.println("Benutzer nicht in der Benutzerliste");
    }

    @Override
    public void showErrorNoSuchIssue() {
        System.err.println("Issue nicht in der Issueliste");
    }

    @Override
    public void showErrorIdTooShort() {
        System.err.println("ID soll mindestens 6 Zeichen lang sein");
    }
}

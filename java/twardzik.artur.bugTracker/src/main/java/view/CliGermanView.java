package view;

import model.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * German language CLI user interface
 *
 * @author Artur Twardzik
 * @version 0.1
 */
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
        System.out.println("[9] Sprache wählen");
        System.out.println("[10] Beenden");
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
        System.out.print("Bitte geben Sie den NEUEN Titel des Issues ein: ");
        String issueName = scanner.nextLine();
        if (!issueName.isBlank()) {
            issue.setTitle(issueName);
        }

        System.out.print("Bitte geben Sie die NEUE Beschreibung des Issues ein: ");
        String issueDesc = scanner.nextLine();
        if (!issueDesc.isBlank()) {
            issue.setDescription(issueDesc);
        }
    }

    @Override
    public void showIssueShortDetails(Issue issue) {
        System.out.printf("| %s | %-40s | Status %-10s| Erstellt von %-15s |", issue.getId().toString().split("-")[0], issue.getTitle(), issue.getStatus(), issue.getReporter().getUsername());
    }

    @Override
    public void showIssueDetails(Issue issue) {
        System.out.println("Issue: " + issue.getId().toString().split("-")[0]);
        System.out.println("\tTitel: " + issue.getTitle());
        System.out.println("\tBeschreibung: " + issue.getDescription());
        System.out.println("\tStatus: " + issue.getStatus());
        System.out.println("\tAutor: " + issue.getReporter().getUsername());

        System.out.println("\tErstellt am: " + issue.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("\tAktualisiert am: " + issue.getUpdatedAt().map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(""));
        System.out.println();
        System.out.println("\tZuständiger: " + issue.getAssignee().map(User::getUsername).orElse(""));
        System.out.println("\tFälligkeitsdatum: " + issue.getDueDate().map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(""));
        System.out.println("\n");

        List<Comment> comments = issue.getComments();

        if (comments.isEmpty()) {
            return;
        }

        System.out.println("Kommentare: ");
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
        System.out.print("Kommentar: ");
        String comment = scanner.nextLine();

        return new Comment(author, comment);
    }

    @Override
    public void showUiLanguageList() {
        System.out.println("Verfügbare Sprachen: ");
        System.out.println("[1] Englisch");
        System.out.println("[2] Deutsch");
    }

    @Override
    public UiLanguage chooseUiLanguage() {
        System.out.print("Wähle eine Sprache: ");

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

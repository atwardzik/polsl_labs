package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import lombok.Setter;
import model.BugStatus;
import model.Issue;

import java.time.format.DateTimeFormatter;

/**
 * Controller for displaying the detailed view of a single {@link Issue}.
 * Handles initialization of UI components, displaying issue information,
 * managing status updates, and delegating actions such as editing or returning
 * to the previous view to the main application window controller.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class IssueViewController {
    /**
     * Reference to the main window controller for navigation and shared data access.
     * -- SETTER --
     *  Sets the parent main window controller, which provides navigation
     *  methods and access to shared application data.
     *
     * @param mainWindow the main application window controller

     */
    @Setter
    MainAppWindowController parent;

    /**
     * The currently displayed issue whose details are shown in this view.
     */
    Issue issue;

    /**
     * Button used to assign the issue to a user (functionality to be implemented).
     */
    @FXML
    private Button assignBtn;

    /**
     * Label displaying the username of the issue's reporter.
     */
    @FXML
    private Label authorLabel;

    /**
     * Button for navigating back to the previous view.
     */
    @FXML
    private Button backBtn;

    /**
     * Label showing the date and time when the issue was created.
     */
    @FXML
    private Label dateOpenedLabel;

    /**
     * WebView used to display the issue description with rich text support.
     */
    @FXML
    private WebView descriptionWebView;

    /**
     * Label displaying the due date of the issue, if present.
     */
    @FXML
    private Label dueDateLabel;

    /**
     * Button for opening the current issue in edit mode.
     */
    @FXML
    private Button editIssueBtn;

    /**
     * Root HBox of the issue view scene, used for scene-level event handling.
     */
    @FXML
    private HBox issueViewScene;

    /**
     * ComboBox allowing the user to change the issue's status.
     */
    @FXML
    private ComboBox<BugStatus> statusComboBox;

    /**
     * Label showing the priority level of the issue.
     */
    @FXML
    private Label priorityLabel;

    /**
     * Label showing the current status of the issue.
     */
    @FXML
    private Label statusLabel;

    /**
     * Label displaying the title of the issue.
     */
    @FXML
    private Label titleLabel;

    /**
     * JavaFX initialization method. Sets up tooltips, keyboard shortcuts
     * (Ctrl/Cmd + R for back, Ctrl/Cmd + E for edit), and populates the
     * status combo box with all possible {@link BugStatus} values.
     */
    @FXML
    public void initialize() {
        String shortcutSymbol = System.getProperty("os.name").toLowerCase().contains("mac") ? "⌘" : "Ctrl+";
        Tooltip.install(backBtn, new Tooltip("Get Back (" + shortcutSymbol + "R)"));
        Tooltip.install(editIssueBtn, new Tooltip("Edit (" + shortcutSymbol + "E)"));
        issueViewScene.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                        () -> backBtnClicked(null)
                );
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN),
                        () -> editIssueBtnClicked(null)
                );
            }
        });

        statusComboBox.getItems().addAll(BugStatus.values());
        statusComboBox.setOnAction((action) -> updateStatus());
    }

    /**
     * Updates the status of the currently displayed issue based on the
     * value selected in the {@link #statusComboBox} and updates the
     * {@link #statusLabel}. Also shows a toast message confirming the change.
     */
    private void updateStatus() {
        issue.setStatus(statusComboBox.getValue());
        statusLabel.setText("Status: " + issue.getStatus().getStatusName());
        parent.showToast("Status changed");
    }

    /**
     * Event handler triggered when the assign button is clicked.
     * Currently a placeholder for future implementation of issue assignment.
     *
     * @param event the action event
     */
    @FXML
    void assignBtnClicked(ActionEvent event) {

    }

    /**
     * Event handler triggered when the back button is clicked. Delegates
     * the navigation to the main controller to return to the previous view.
     *
     * @param event the action event
     */
    @FXML
    void backBtnClicked(ActionEvent event) {
        parent.goBack();
    }

    /**
     * Event handler triggered when the edit button is clicked. Delegates
     * opening the current issue in edit mode to the main controller.
     *
     * @param event the action event
     */
    @FXML
    void editIssueBtnClicked(ActionEvent event) {
        parent.setEditIssuePane(issue);
    }

    /**
     * Populates the issue view with data from the given {@link Issue},
     * including labels, WebView description, and status combo box.
     *
     * @param issue the issue whose details should be displayed
     */
    public void initializeData(Issue issue) {
        authorLabel.setText("Author: " + issue.getReporter().getUsername());
        dateOpenedLabel.setText("Opened on: " + issue.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dueDateLabel.setText("Due on: " + issue.getDueDate()
                .map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .orElse("N/A"));
        statusLabel.setText("Status: " + issue.getStatus().getStatusName());
        priorityLabel.setText("Priority: " + issue.getPriority().toString());
        titleLabel.setText("Title: " + issue.getTitle());

        descriptionWebView.getEngine().loadContent(issue.getDescription());
        descriptionWebView.setMaxWidth(Double.MAX_VALUE);
        descriptionWebView.addEventFilter(KeyEvent.KEY_TYPED, Event::consume);

        statusComboBox.setValue(issue.getStatus());

        this.issue = issue;
    }

}
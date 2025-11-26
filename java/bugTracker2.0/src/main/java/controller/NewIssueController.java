package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import model.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for creating and editing issues in the application.
 * Manages the new issue form, including fields for title, description, assignee,
 * status, priority, due date, and tags. Handles validation, issue creation or
 * update, and delegates persistence and navigation actions to the main controller.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class NewIssueController {
    /**
     * Reference to the main application controller for navigation, issue management, and shared data access.
     */
    private MainAppWindowController parent;

    /**
     * The issue currently being created or edited; null if creating a new issue.
     */
    private Issue issue;

    /**
     * ComboBox allowing the selection of a user to assign the issue to.
     */
    @FXML
    private ComboBox<User> assigneeComboBox;

    /**
     * Button for returning to the previous view without saving changes.
     */
    @FXML
    private Button backBtn;

    /**
     * Button used to create a new issue or save edits to an existing issue.
     */
    @FXML
    private Button createBtn;

    /**
     * Root HBox of the new issue scene, used for scene-level event handling.
     */
    @FXML
    private HBox newIssueScene;

    /**
     * DatePicker used to select the issue's due date.
     */
    @FXML
    private DatePicker dueDate;

    /**
     * ComboBox for selecting the priority level of the issue.
     */
    @FXML
    private ComboBox<Priority> priorityComboBox;

    /**
     * ComboBox for selecting the status of the issue (e.g., OPEN, CLOSED).
     */
    @FXML
    private ComboBox<BugStatus> statusComboBox;

    /**
     * TextField for entering tags associated with the issue.
     */
    @FXML
    private TextField tagsField;

    /**
     * HTMLEditor used to enter the issue description with rich text formatting.
     */
    @FXML
    private HTMLEditor textEditor;

    /**
     * TextField for entering the title of the issue.
     */
    @FXML
    private TextField titleField;

    /**
     * Callback used to report exceptions back to the parent controller.
     */
    private ChildControllerListener listener;

    /**
     * JavaFX initialization method. Sets up tooltips, keyboard shortcuts
     * (Ctrl/Cmd + Enter for create/save, Ctrl/Cmd + R for back), and populates
     * ComboBoxes for assignees, status, and priority with default values.
     */
    @FXML
    private void initialize() {
        String shortcutSymbol = System.getProperty("os.name").toLowerCase().contains("mac") ? "⌘" : "Ctrl+";
        Tooltip.install(createBtn, new Tooltip("Save (" + shortcutSymbol + "↩)"));
        Tooltip.install(backBtn, new Tooltip("Get Back (" + shortcutSymbol + "R)"));
        newIssueScene.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN),
                        () -> createBtnClicked(null)
                );
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                        () -> backBtnClicked(null)
                );

                assigneeComboBox.getItems().addAll(parent.getUsersList());
                assigneeComboBox.setCellFactory(listView -> new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        setText(empty || user == null ? null : user.getUsername());
                    }
                });
                assigneeComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        setText(empty || user == null ? null : user.getUsername());
                    }
                });
            }
        });

        statusComboBox.getItems().addAll(BugStatus.values());
        statusComboBox.setValue(BugStatus.OPEN);
        priorityComboBox.getItems().addAll(Priority.values());
        priorityComboBox.setValue(Priority.LOW);
    }

    /**
     * Event handler triggered when the back button is clicked.
     * Delegates returning to the previous view to the main controller.
     *
     * @param event the action event
     */
    @FXML
    void backBtnClicked(ActionEvent event) {
        parent.goBack();
    }

    /**
     * Event handler triggered when the create/save button is clicked.
     * Validates title and description fields, creates a new {@link Issue} or
     * updates an existing one, assigns tags and due date, and delegates persistence
     * to the main controller. Displays toast notifications for feedback.
     *
     * @param event the action event
     */
    @FXML
    void createBtnClicked(ActionEvent event) {
        titleField.getStyleClass().remove("error-field");
        textEditor.getStyleClass().remove("error-field");

        if (titleField.getText().isBlank()) {
            titleField.getStyleClass().add("error-field");

            parent.showToast("Title should not be empty!");
            return;
        }

        String htmlContent = textEditor.getHtmlText();
        String textOnly = htmlContent.replaceAll("<[^>]*>", "").trim();
        if (textOnly.isBlank()) {
            textEditor.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

            parent.showToast("Description should not be empty!");
            return;
        }

        User user = parent.getReporter();
        if (user == null) {
            parent.showToast("You are not logged in!");
            return;
        }

        String[] tokens = tagsField.getText().split("\\s+");
        Set<String> tokenSet = new HashSet<>(Arrays.asList(tokens));

        LocalDateTime dueDateStart = null;
        if (dueDate.getValue() != null) {
            dueDateStart = dueDate.getValue().atStartOfDay();
        }

        Issue newIssue = null;
        try {
            newIssue = new Issue(titleField.getText(), textEditor.getHtmlText(),
                    user, dueDateStart,
                    null, statusComboBox.getValue(),
                    priorityComboBox.getValue(), tokenSet);

        } catch (Exception e) {
            if (this.listener != null) {
                this.listener.onError(e);
                return;
            }
        }

        if (newIssue == null) {
            return;
        }

        if (issue == null) {
            issue = newIssue;
            createBtn.setText("Save");
            parent.addIssue(issue);
            parent.showToast("New Issue Created");
        } else {
            parent.updateIssue(issue, newIssue);
            parent.showToast("Issue updated successfully");
        }

        parent.setIssuePane(issue);
    }

    /**
     * Sets the current issue to be edited. Populates all relevant UI fields
     * with the issue's data and updates the create/save button label.
     *
     * @param issue the issue to edit
     */
    public void setIssue(Issue issue) {
        this.issue = issue;

        titleField.setText(issue.getTitle());
        textEditor.setHtmlText(issue.getDescription());
//        if (issue.getDueDate().isPresent()) {
//            dueDate.setValue(LocalDate.from(issue.getDueDate().get()));
//        }

        createBtn.setText("Save");
    }

    /**
     * Sets the parent main window controller for navigation and shared data access.
     *
     * @param mainWindow the main application window controller
     */
    public void setParent(MainAppWindowController mainWindow) {
        this.parent = mainWindow;
    }

    /**
     * Registers an exception listener to report errors that occur during
     * issue creation or update.
     *
     * @param eventListerner the listener to notify on exceptions
     */
    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

    /**
     * Requests focus on the title field, typically used when the view is first
     * displayed to improve user experience.
     */
    public void setFocusOnTitle() {
        Platform.runLater(() -> titleField.requestFocus());
    }
}

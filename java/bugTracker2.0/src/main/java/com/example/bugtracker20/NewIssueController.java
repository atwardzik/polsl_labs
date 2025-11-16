package com.example.bugtracker20;

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
import java.util.List;
import java.util.Set;

public class NewIssueController {
    private MainAppWindowController parent;

    private Issue issue;

    @FXML
    private ComboBox<User> assigneeComboBox;

    @FXML
    private Button backBtn;

    @FXML
    private Button createBtn;

    @FXML
    private HBox newIssueScene;

    @FXML
    private DatePicker dueDate;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private ComboBox<BugStatus> statusComboBox;

    @FXML
    private TextField tagsField;

    @FXML
    private HTMLEditor textEditor;

    @FXML
    private TextField titleField;

    private ChildControllerListener listener;

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

    @FXML
    void backBtnClicked(ActionEvent event) {
        parent.goBack();
    }

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

    public void setIssue(Issue issue) {
        this.issue = issue;

        titleField.setText(issue.getTitle());
        textEditor.setHtmlText(issue.getDescription());
//        if (issue.getDueDate().isPresent()) {
//            dueDate.setValue(LocalDate.from(issue.getDueDate().get()));
//        }

        createBtn.setText("Save");
    }

    public void setParent(MainAppWindowController mainWindow) {
        this.parent = mainWindow;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

    public void setFocusOnTitle() {
        Platform.runLater(() -> titleField.requestFocus());
    }
}

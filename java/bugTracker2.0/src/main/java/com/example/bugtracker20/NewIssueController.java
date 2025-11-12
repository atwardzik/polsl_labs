package com.example.bugtracker20;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import model.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewIssueController {
    private MainAppWindowController mainWindow;

    private Issue issue;

    @FXML
    private Spinner<User> asigneeSpinner;

    @FXML
    private Button backBtn;

    @FXML
    private Button createBtn;

    @FXML
    private DatePicker dueDate;

    @FXML
    private Spinner<Priority> prioritySpinner;

    @FXML
    private Spinner<BugStatus> statusSpinner;

    @FXML
    private TextField tagsField;

    @FXML
    private HTMLEditor textEditor;

    @FXML
    private TextField titleField;

    private ChildControllerListener listener;

    @FXML
    private void initialize() {
//        textEditor.
    }

    @FXML
    void backBtnClicked(ActionEvent event) {
        mainWindow.goBack();
    }

    @FXML
    void createBtnClicked(ActionEvent event) {
        titleField.getStyleClass().remove("error-field");
        textEditor.getStyleClass().remove("error-field");

        if (titleField.getText().isBlank()) {
            titleField.getStyleClass().add("error-field");

            mainWindow.showToast("Title should not be empty!");
            return;
        }

        String htmlContent = textEditor.getHtmlText();
        String textOnly = htmlContent.replaceAll("<[^>]*>", "").trim();
        if (textOnly.isBlank()) {
            textEditor.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

            mainWindow.showToast("Description should not be empty!");
            return;
        }

        User user = mainWindow.getReporter();
        if (user == null) {
            mainWindow.showToast("You are not logged in!");
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
                    asigneeSpinner.getValue(), statusSpinner.getValue(),
                    prioritySpinner.getValue(), tokenSet);

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
            mainWindow.addIssue(issue);
        } else {
            issue.updateFrom(newIssue);
        }

        mainWindow.setIssuePane(issue);
    }

    public void setIssue(Issue manager) {
        this.issue = manager;
    }

    public void setParent(MainAppWindowController mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }
}

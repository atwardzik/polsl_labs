package com.example.bugtracker20;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Popup;
import javafx.util.Duration;
import model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewIssueController {
    private MainAppWindowController mainWindow;

    private IssueManager manager;

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

        try {
            String[] tokens = tagsField.getText().split("\\s+");

            Set<String> tokenSet = new HashSet<>(Arrays.asList(tokens));

            LocalDateTime dueDateStart = null;
            if (dueDate.getValue() != null) {
                dueDateStart = dueDate.getValue().atStartOfDay();
            }

            Issue issue = new Issue(titleField.getText(), textEditor.getHtmlText(),
                    user, dueDateStart,
                    asigneeSpinner.getValue(), statusSpinner.getValue(),
                    prioritySpinner.getValue(), tokenSet);

            manager.addIssue(issue);
            mainWindow.setIssuePane(issue);
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    public void setIssueManager(IssueManager manager) {
        this.manager = manager;
    }

    public void setParent(MainAppWindowController mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

}

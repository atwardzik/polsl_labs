package com.example.bugtracker20;

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
import model.BugStatus;
import model.Issue;

import java.time.format.DateTimeFormatter;


public class IssueViewController {
    MainAppWindowController parent;

    Issue issue;

    @FXML
    private Button assignBtn;

    @FXML
    private Label authorLabel;

    @FXML
    private Button backBtn;

    @FXML
    private Label dateOpenedLabel;

    @FXML
    private WebView descriptionWebView;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Button editIssueBtn;

    @FXML
    private HBox issueViewScene;

    @FXML
    private ComboBox<BugStatus> statusComboBox;

    @FXML
    private Label priorityLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        String shortcutSymbol = System.getProperty("os.name").toLowerCase().contains("mac") ? "⌘" : "Ctrl+";
        Tooltip.install(backBtn, new Tooltip("Get Back (" + shortcutSymbol + "R)"));
        issueViewScene.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                        () -> backBtnClicked(null)
                );
            }
        });

        statusComboBox.getItems().addAll(BugStatus.values());
        statusComboBox.setOnAction((action) -> updateStatus());
    }

    private void updateStatus() {
        issue.setStatus(statusComboBox.getValue());
        statusLabel.setText("Status: " + issue.getStatus().getStatusName());
        parent.showToast("Status changed");
    }

    @FXML
    void assignBtnClicked(ActionEvent event) {

    }

    @FXML
    void backBtnClicked(ActionEvent event) {
        parent.goBack();
    }

    @FXML
    void editIssueBtnClicked(ActionEvent event) {
        parent.setEditIssuePane(issue);
    }

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

    public void setParent(MainAppWindowController mainWindow) {
        this.parent = mainWindow;
    }
}
package com.example.bugtracker20;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import model.Issue;

import java.time.format.DateTimeFormatter;


public class IssueViewController {
    MainAppWindowController mainWindow;

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
    private Button markStatusBtn;

    @FXML
    private Label priorityLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        issueViewScene.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                        () -> backBtnClicked(null)
                );
            }
        });
    }

    @FXML
    void assignBtnClicked(ActionEvent event) {

    }

    @FXML
    void backBtnClicked(ActionEvent event) {
        mainWindow.goBack();
    }

    @FXML
    void editIssueBtnClicked(ActionEvent event) {
        mainWindow.setEditIssuePane(issue);
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

        this.issue = issue;
    }

    @FXML
    void markStatusBtnClicked(ActionEvent event) {

    }

    public void setParent(MainAppWindowController mainWindow) {
        this.mainWindow = mainWindow;
    }
}
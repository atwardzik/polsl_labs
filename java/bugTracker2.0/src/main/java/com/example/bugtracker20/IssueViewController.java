package com.example.bugtracker20;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
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
    private Label dateLabel;

    @FXML
    private WebView descriptionWebView;

    @FXML
    private Button editIssueBtn;

    @FXML
    private Button markStatusBtn;

    @FXML
    private Label titleLabel;

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
        dateLabel.setText("Date: " + issue.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
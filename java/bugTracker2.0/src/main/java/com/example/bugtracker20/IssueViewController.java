package com.example.bugtracker20;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import model.Issue;


public class IssueViewController {
    MainAppWindowController mainWindow;

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
    private Label titleLabel;

    @FXML
    void backBtnClicked(ActionEvent event) {
        mainWindow.goBack();
    }

    @FXML
    void editIssueBtnClicked(ActionEvent event) {

    }

    public void initializeData(Issue issue) {
        authorLabel.setText("Author: " + issue.getReporter().getUsername());
        dateLabel.setText("Date: " + issue.getCreatedAt());
        titleLabel.setText("Title: " + issue.getTitle());

        descriptionWebView.getEngine().loadContent(issue.getDescription());
        descriptionWebView.setMaxWidth(Double.MAX_VALUE);
    }

    public void setParent(MainAppWindowController mainWindow) {
        this.mainWindow = mainWindow;
    }
}
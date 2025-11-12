package com.example.bugtracker20;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Issue;
import model.IssueManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainAppWindowController {
    private final IssueManager manager;

    public MainAppWindowController(IssueManager manager) {
        this.manager = manager;
    }

    @FXML
    private Button filterButton;

    @FXML
    private Button issuesButton;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private HBox rightSidePane;

    @FXML
    private Button newButton;

    @FXML
    private Button userButton;

    @FXML
    void onFilterButtonClick(ActionEvent event) {

    }

    @FXML
    void onIssuesButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssuesListView.fxml"));
        HBox newContent = loader.load();
        IssuesListController controller = loader.getController();

        controller.setParent(this);
        controller.initializeData();

        rightSidePane.getChildren().clear();
        rightSidePane.getChildren().add(newContent);
    }

    @FXML
    void onNewButtonClick(ActionEvent event) {

    }

    @FXML
    void onUserButtonClicked(ActionEvent event) {
    }

    public void setIssuePane(UUID id) throws IOException {
        System.out.println("Selected issue: #" + id);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssueView.fxml"));
        HBox newContent = loader.load();

        rightSidePane.getChildren().clear();
        rightSidePane.getChildren().add(newContent);
    }

    public List<Issue> getIssuesList() {
        return manager.getAllIssues();
    }
}

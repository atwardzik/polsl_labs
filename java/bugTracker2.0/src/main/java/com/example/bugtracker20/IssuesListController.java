package com.example.bugtracker20;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Issue;
import model.User;

import java.io.IOException;
import java.util.List;

public class IssuesListController {
    @FXML
    private ListView<Issue> issuesList;

    MainAppWindowController mainWindowController;

    public void setParent(MainAppWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void initializeData() {
        issuesList.setCellFactory(lv -> new IssueCell());
        issuesList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double-click
                Issue selectedIssue = issuesList.getSelectionModel().getSelectedItem();
                if (selectedIssue != null) {
                    try {
                        mainWindowController.setIssuePane(selectedIssue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        ObservableList<Issue> list = FXCollections.observableArrayList();

        list.addAll(mainWindowController.getIssuesList());
        issuesList.setItems(list);
    }
}
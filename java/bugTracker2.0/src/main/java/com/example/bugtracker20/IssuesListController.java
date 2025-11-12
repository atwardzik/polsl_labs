package com.example.bugtracker20;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Issue;

public class IssuesListController {
    @FXML
    private ListView<Issue> issuesList;

    MainAppWindowController mainWindowController;

    private ChildControllerListener listener;

    public void initializeData() {
        issuesList.setCellFactory(lv -> new IssueCell());
        issuesList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Issue selectedIssue = issuesList.getSelectionModel().getSelectedItem();
                if (selectedIssue != null) {
                    try {
                        mainWindowController.setIssuePane(selectedIssue);
                    } catch (Exception e) {
                        if (this.listener != null) {
                            this.listener.onError(e);
                        }
                    }
                }
            }
        });
        ObservableList<Issue> list = FXCollections.observableArrayList();

        list.addAll(mainWindowController.getIssuesList());
        issuesList.setItems(list);
    }

    public void setParent(MainAppWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }
}
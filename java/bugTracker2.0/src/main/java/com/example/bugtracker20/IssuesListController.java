package com.example.bugtracker20;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import model.Issue;

public class IssuesListController {
    @FXML
    private ListView<Issue> issuesList;

    MainAppWindowController parent;

    private ChildControllerListener listener;

    public void initializeData() {
        issuesList.setCellFactory(lv -> new IssueCell());
        issuesList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Issue selectedIssue = issuesList.getSelectionModel().getSelectedItem();
                if (selectedIssue != null) {
                    try {
                        parent.setIssuePane(selectedIssue);
                    } catch (Exception e) {
                        if (this.listener != null) {
                            this.listener.onError(e);
                        }
                    }
                }
            }
        });
        issuesList.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN),
                        () -> {
                            Issue selected = issuesList.getSelectionModel().getSelectedItem();
                            if (selected != null) {
                                parent.setIssuePane(selected);
                            }
                        }
                );
            }
        });
        ObservableList<Issue> list = FXCollections.observableArrayList();

        list.addAll(parent.getIssuesList());
        issuesList.setItems(list);
    }

    public void setParent(MainAppWindowController mainWindowController) {
        this.parent = mainWindowController;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }
}
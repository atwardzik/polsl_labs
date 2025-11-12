package com.example.bugtracker2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class IssuesListController {
    @FXML
    private ListView<String> issuesList;

    public void initializeData() {
        issuesList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || this.itemProperty() == null) {
                    setText(null);
                } else {
                    setText("Fruit: " + item); // Custom display
                }
            }
        });
        ObservableList<String> list = FXCollections.observableArrayList("ha", "he");
        issuesList.setItems(list);
    }
}
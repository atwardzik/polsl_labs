package com.example.bugtracker2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class HelloController {

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

}

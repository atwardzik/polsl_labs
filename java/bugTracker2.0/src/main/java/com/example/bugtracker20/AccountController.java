package com.example.bugtracker20;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.BugStatus;
import model.Issue;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountController {
    private MainAppWindowController parent;

    private ObservableList<Issue> userIssues;


    @FXML
    private Label activeSinceLabel;

    @FXML
    private TableColumn<Issue, String> dueDateCol;

    @FXML
    private TableColumn<Issue, String> issueTitleCol;

    @FXML
    private Label lastSeenAtLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profilePhoto;

    @FXML
    private TableColumn<Issue, String> reporterCol;

    @FXML
    private Label roleLabel;

    @FXML
    private TableColumn<Issue, String> statusCol;

    @FXML
    private Label surnameLabel;

    @FXML
    private TableView<Issue> tableView;

    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() {
        tableView.setStyle(
                "-fx-font-family: 'JetBrains Mono';" + "-fx-font-size: 14px;"
        );

        tableView.setEditable(true);
        statusCol.setEditable(true);

        initializeIssueTitleCol();
        initializeDueDateCol();
        initializeStatusCol();
        initializeReporterCol();
    }

    private void initializeIssueTitleCol() {
        issueTitleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));
        issueTitleCol.setCellFactory(col -> {
            TableCell<Issue, String> cell = new TableCell<>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);

                        Tooltip tooltip = new Tooltip(item);
                        setTooltip(tooltip);

                        setStyle("-fx-text-overrun: ellipsis;");
                    }

                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    Issue issue = cell.getTableView().getItems().get(cell.getIndex());

                    parent.setIssuePane(issue);
                }
            });

            return cell;
        });
    }

    private void initializeReporterCol() {
        reporterCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporter().getUsername()));
    }

    private void initializeDueDateCol() {
        dueDateCol.setCellValueFactory(cellData -> {
            String dueDate = cellData.getValue().getDueDate()
                    .map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                    .orElse("N/A");

            return new SimpleStringProperty(dueDate);
        });
    }

    private void initializeStatusCol() {
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().name()));
        statusCol.setCellFactory(ComboBoxTableCell.<Issue, String>forTableColumn(BugStatus.toArrayOfStrings()));
        statusCol.setOnEditCommit(t -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setStatus(BugStatus.valueOf(t.getNewValue()));
        });
    }

    public void setParent(MainAppWindowController parent) {
        this.parent = parent;
    }

    public void setUserIssues(List<Issue> userIssues) {
        this.userIssues = FXCollections.observableArrayList(userIssues);
        tableView.setItems(this.userIssues);
    }
}

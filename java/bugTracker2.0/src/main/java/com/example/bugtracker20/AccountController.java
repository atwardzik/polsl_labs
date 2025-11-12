package com.example.bugtracker20;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import model.BugStatus;
import model.Issue;

public class AccountController {

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
    private TableColumn<Issue, BugStatus> statusCol;

    @FXML
    private Label surnameLabel;

    @FXML
    private TableView<Issue> tableView;

    @FXML
    private Label usernameLabel;


//    public TableController(People people) {
//        this.people = people;
//        data = FXCollections.observableArrayList(people.getData());
//        data.addListener((ListChangeListener.Change<? extends Person> change) -> {
//            while (change.next()) {
//                if (change.wasPermutated()) {
//                    for (int i = change.getFrom(); i < change.getTo(); ++i) {
//                        System.out.println("zamiana");
//                    }
//                } else if (change.wasUpdated()) {
//                    System.out.println("uaktualnienie");
//                } else {
//                    for (var remitem : change.getRemoved()) {
//                        people.getData().remove(remitem);
//                    }
//                    for (var additem : change.getAddedSubList()) {
//                        people.getData().add(additem);
//                    }
//                }
//            }
//        });
//    }
//
//    private void initializeFirstNameColumn() {
//        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        firstNameColumn.setOnEditCommit(t -> {
//            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
//        });
//    }
//      ...
//
//
//    public void initialize() {
//
//        var tooltip = new Tooltip("Przycisk aktywuje proces dodania nowej osoby.");
//        tooltip.setStyle("-fx-font: normal bold 14 Langdon; -fx-base: #AE3522; -fx-text-fill: orange;");
//        addButton.setTooltip(tooltip);
//
//        genderCombobox.getItems().addAll(Gender.toArrayOfStrings());
//
//        table.setItems(data);
//        table.setEditable(true);
//
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        initializeFirstNameColumn();
//        initializeLastNameColumn();
//        initializeEmailColumn();
//        initializeVipColumn();
//        initializeGenderColumn();
//        initializeDateOfBirthColumnColumn();
//    }

}

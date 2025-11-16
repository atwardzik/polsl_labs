package com.example.bugtracker20;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.ArrayList;
import java.util.List;

public class FilterController {
    private MainAppWindowController parent;

    private ChildControllerListener listener;

    @FXML
    private ComboBox<User> authorComboBox;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button filterButton;

    @FXML
    private HBox filterScene;

    @FXML
    private TextField idTextField;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private ListView<Issue> resultsList;

    @FXML
    private ComboBox<BugStatus> statusComboBox;

    @FXML
    private TextField titleTextField;


    @FXML
    public void initialize() {
        String shortcutSymbol = System.getProperty("os.name").toLowerCase().contains("mac") ? "⌘" : "Ctrl+";
        Tooltip.install(filterButton, new Tooltip("Filter Issues (" + shortcutSymbol + "↩)"));

        filterScene.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN),
                        () -> onFilterButtonClicked(null)
                );
            }
        });
        resultsList.setCellFactory(lv -> new IssueCell());
        resultsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Issue selectedIssue = resultsList.getSelectionModel().getSelectedItem();
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
        resultsList.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN),
                        () -> {
                            Issue selected = resultsList.getSelectionModel().getSelectedItem();
                            if (selected != null) {
                                parent.setIssuePane(selected);
                            }
                        }
                );

                authorComboBox.getItems().addAll(parent.getUsersList());
                authorComboBox.setCellFactory(listView -> new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        setText(empty || user == null ? null : user.getUsername());
                    }
                });
                authorComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        setText(empty || user == null ? null : user.getUsername());
                    }
                });
            }
        });

        statusComboBox.getItems().addAll(BugStatus.values());
        priorityComboBox.getItems().addAll(Priority.values());


    }

    @FXML
    void onFilterButtonClicked(ActionEvent event) {
        IssueManager manager = parent.getManager();

        List<Issue> issueList = manager.getAllIssues();


        if (statusComboBox.getValue() != null) {
            List<Issue> res = manager.filterByStatus(statusComboBox.getValue());
            issueList.retainAll(res);
        }

        if (priorityComboBox.getValue() != null) {
            List<Issue> res = manager.filterByPriority(priorityComboBox.getValue());
            issueList.retainAll(res);
        }

        if (authorComboBox.getValue() != null) {
            List<Issue> res = manager.filterByAuthor(authorComboBox.getValue());
            issueList.retainAll(res);
        }


        if (dateFrom.getValue() != null) {
            List<Issue> res = manager.filterByDateAfter(dateFrom.getValue().atStartOfDay());
            issueList.retainAll(res);
        }

        if (dateTo.getValue() != null) {
            List<Issue> res = manager.filterByDateBefore(dateTo.getValue().atStartOfDay());
            issueList.retainAll(res);
        }

        if (!idTextField.getText().isBlank()) {
            List<Issue> res = manager.filterByIdFragment(idTextField.getText());
            issueList.retainAll(res);
        }

        if (!titleTextField.getText().isBlank()) {
            List<Issue> res = manager.filterByTitle(titleTextField.getText());
            issueList.retainAll(res);
        }


        ObservableList<Issue> list = FXCollections.observableArrayList();
        list.addAll(issueList);
        resultsList.setItems(list);
    }

    public void setParent(MainAppWindowController mainWindowController) {
        this.parent = mainWindowController;
    }

    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

    public void refreshIssues() {
        resultsList.refresh();
    }
}
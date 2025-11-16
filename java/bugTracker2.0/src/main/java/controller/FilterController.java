package controller;

import com.example.bugtracker20.ChildControllerListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import model.*;

import java.util.List;

/**
 * Controller responsible for filtering issues based on multiple criteria such as
 * status, priority, author, date range, ID, and title. Displays the results in a
 * list and supports opening an issue in the main window. Initializes UI controls,
 * keyboard shortcuts, and list cell rendering.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class FilterController {
    /**
     * Reference to the main window controller for navigation and data access.
     */
    private MainAppWindowController parent;

    /**
     * Listener used for reporting exceptions back to the main controller.
     */
    private ChildControllerListener listener;

    /**
     * ComboBox selecting the author (reporter or assignee) to filter issues by.
     */
    @FXML
    private ComboBox<User> authorComboBox;

    /**
     * DatePicker representing the start of the filter date range.
     */
    @FXML
    private DatePicker dateFrom;

    /**
     * DatePicker representing the end of the filter date range.
     */
    @FXML
    private DatePicker dateTo;

    /**
     * Button that triggers filtering of issues.
     */
    @FXML
    private Button filterButton;

    /**
     * Root HBox of the filter view used for attaching keyboard accelerators.
     */
    @FXML
    private HBox filterScene;

    /**
     * Text field for filtering issues by partial or full ID.
     */
    @FXML
    private TextField idTextField;

    /**
     * ComboBox used to filter issues by priority.
     */
    @FXML
    private ComboBox<Priority> priorityComboBox;

    /**
     * ListView that displays filtered issue results.
     */
    @FXML
    private ListView<Issue> resultsList;

    /**
     * ComboBox for filtering issues by status.
     */
    @FXML
    private ComboBox<BugStatus> statusComboBox;

    /**
     * Text field for filtering issues by title.
     */
    @FXML
    private TextField titleTextField;

    /**
     * Initializes the filter view once the FXML is loaded. Sets up tooltips, keyboard
     * shortcuts (Ctrl/Cmd + Enter for filtering, Ctrl/Cmd + O to open selected issue),
     * configures list cell behavior, loads ComboBox contents, and sets up event
     * handlers for double-clicking an issue.
     */
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

    /**
     * Performs filtering based on the currently selected or entered criteria:
     * status, priority, author, date range, ID fragment, and title. The results
     * are updated in the results list.
     *
     * @param event the action event triggered by clicking the filter button
     *              (may be null when triggered by keyboard shortcut)
     */
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

    /**
     * Sets the parent controller to allow navigation to issue details and access to
     * the IssueManager.
     *
     * @param mainWindowController the parent main application window controller
     */
    public void setParent(MainAppWindowController mainWindowController) {
        this.parent = mainWindowController;
    }

    /**
     * Sets the exception listener so child controller errors can be forwarded to
     * the main application for handling.
     *
     * @param eventListerner the listener to notify on errors
     */
    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

    /**
     * Refreshes the ListView of filtered issues. Useful when external changes occur
     * that modify the displayed issues (e.g., issue edited elsewhere).
     */
    public void refreshIssues() {
        resultsList.refresh();
    }
}
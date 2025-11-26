package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import model.Issue;

/**
 * Controller responsible for displaying the list of all issues in the application.
 * This class initializes and manages the Issues List view, including configuring
 * list cell rendering, handling user interaction (mouse double-click and keyboard
 * shortcuts), and delegating navigation to the main application window controller.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class IssuesListController {
    /**
     * The ListView displaying all issues in the system.
     */
    @FXML
    private ListView<Issue> issuesList;

    /**
     * Root container of the issues list view, used for scene-level event handling.
     */
    @FXML
    private HBox issuesListScene;

    /**
     * Reference to the main window controller used for navigation and shared data access.
     */
    private MainAppWindowController parent;

    /**
     * Callback used to report exceptions or errors back to the parent controller.
     */
    private ChildControllerListener listener;

    /**
     * Initializes the issues list view by configuring its cell factory,
     * setting up mouse and keyboard shortcuts for opening issue details,
     * loading issue data from the parent controller, and establishing focus
     * behavior when the scene becomes active.
     */
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
        issuesList.getSelectionModel().select(0);

        issuesListScene.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                issuesList.requestFocus();
            }
        });
    }

    /**
     * Sets the parent {@link MainAppWindowController} responsible for
     * providing issue data and handling navigation actions.
     *
     * @param mainWindowController the parent controller
     */
    public void setParent(MainAppWindowController mainWindowController) {
        this.parent = mainWindowController;
    }

    /**
     * Registers an exception listener to receive error notifications
     * triggered by interactions inside the issues list view.
     *
     * @param eventListerner the listener to notify on exceptions
     */
    public void setExceptionListerner(ChildControllerListener eventListerner) {
        this.listener = eventListerner;
    }

    /**
     * Refreshes the ListView of filtered issues. Useful when external changes occur
     * that modify the displayed issues (e.g., issue edited elsewhere).
     */
    public void refreshIssues() {
        issuesList.refresh();
    }
}
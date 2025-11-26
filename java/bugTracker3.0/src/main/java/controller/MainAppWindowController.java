package controller;

import com.example.bugtracker20.HelloApplication;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import model.User;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import model.Issue;
import model.IssueManager;

import java.util.List;
import java.util.Stack;

/**
 * Controller for the main application window. Manages navigation between views,
 * handles menu button activation, keyboard shortcuts, view caching, and
 * communication with child controllers. Also provides common UI utilities such
 * as toast messages.
 *
 * @author Artur Twardzik
 * @version 0.3
 */
public class MainAppWindowController implements ChildControllerListener {
    /**
     * Manager responsible for storing, updating, and retrieving issues.
     */
    @Getter
    private IssueManager manager;

    /**
     * List of all users available in the system.
     */
    @Getter
    private List<User> users;

    /**
     * The currently active or logged-in user.
     */
    @Getter
    private User user;

    /**
     * Stack storing previous right-side UI content for back navigation.
     */
    private Stack<Node> lastRightSideContents;
    /**
     * Stack storing previously active menu buttons for restoring appearance.
     */
    private Stack<Button> lastMenuButtons;

    /**
     * True when the New Issue view is currently active or cached.
     */
    private boolean issueUnderCreation;
    /**
     * Cached UI content for the New Issue view.
     */
    private Node newIssueContents;
    /**
     * Controller associated with the New Issue view.
     */
    private NewIssueController newIssueController;

    /**
     * True when the Filter Issues view is active or cached.
     */
    private boolean issuesFiltered;
    /**
     * Cached UI content for the Filter Issues view.
     */
    private Node filterContents;
    /**
     * Controller for the Filter Issues view.
     */
    private FilterController filterController;

    /**
     * True when the Issues List view is active or cached.
     */
    private boolean issuesListed;
    /**
     * Cached UI content for the Issues List view.
     */
    private Node issuesListContents;
    /**
     * Controller for the Issue List View
     */
    private IssuesListController issuesListController;

    /**
     * Menu bar button for opening the Filter Issues view.
     */
    @FXML
    private Button filterButton;

    /**
     * Menu bar button for opening the Issues list view.
     */
    @FXML
    private Button issuesButton;

    /**
     * Root pane of the main application window.
     */
    @FXML
    private BorderPane mainBorderPane;

    /**
     * StackPane used for overlay UI (toast messages, overlays, etc.).
     */
    @FXML
    private StackPane middleStackPane;

    /**
     * Right-side container displaying main content views.
     */
    @FXML
    private HBox rightSidePane;

    /**
     * Menu bar button for creating a new issue.
     */
    @FXML
    private Button newButton;

    /**
     * Menu bar button for user account management.
     */
    @FXML
    private Button userButton;

    /**
     * Currently active menu button.
     */
    private Button currentButton;

    /**
     * Constructs the main window controller with the required issue manager and
     * list of users. Initializes navigation stacks and internal state.
     *
     * @param manager the issue manager to be used by the application
     * @param users   list of users available in the system
     */
    public MainAppWindowController(IssueManager manager, List<User> users) {
        this.manager = manager;
        this.users = users;

        lastRightSideContents = new Stack<>();
        lastMenuButtons = new Stack<>();
        issueUnderCreation = false;
        issuesFiltered = false;

        //TODO: REPLACE
        user = users.get(0);
    }

    /**
     * JavaFX initialization method. Configures menu button icons and tooltips,
     * sets up keyboard shortcuts, and loads the initial user account view.
     */
    @FXML
    private void initialize() {
        String shortcutSymbol = System.getProperty("os.name").toLowerCase().contains("mac") ? "⌘" : "Ctrl+";

        FontIcon issuesBtnIcon = new FontIcon(MaterialDesign.MDI_VIEW_LIST);
        issuesBtnIcon.setIconSize(20);
        issuesBtnIcon.setIconColor(Color.WHITE);
        issuesButton.setGraphic(issuesBtnIcon);
        issuesButton.setText("");
        Tooltip.install(issuesButton, new Tooltip("View Issues (" + shortcutSymbol + "I)"));

        FontIcon filterBtnIcon = new FontIcon(MaterialDesign.MDI_MAGNIFY);
        filterBtnIcon.setIconSize(20);
        filterBtnIcon.setIconColor(Color.WHITE);
        filterButton.setGraphic(filterBtnIcon);
        filterButton.setText("");
        Tooltip.install(filterButton, new Tooltip("Filter Issues (" + shortcutSymbol + "F)"));

        FontIcon newBtnIcon = new FontIcon(MaterialDesign.MDI_PENCIL);
        newBtnIcon.setIconSize(20);
        newBtnIcon.setIconColor(Color.WHITE);
        newButton.setGraphic(newBtnIcon);
        newButton.setText("");
        Tooltip.install(newButton, new Tooltip("New Issue (" + shortcutSymbol + "N)"));

        FontIcon userBtnIcon = new FontIcon(MaterialDesign.MDI_ACCOUNT);
        userBtnIcon.setIconSize(20);
        userBtnIcon.setIconColor(Color.WHITE);
        userButton.setGraphic(userBtnIcon);
        userButton.setText("");
        Tooltip.install(userButton, new Tooltip("Manage User Account (" + shortcutSymbol + "U)"));


        currentButton = userButton;

        mainBorderPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                registerShortcuts(newScene);
                onUserButtonClicked(null);
            }
        });
    }

    /**
     * Called by child controllers when an exception occurs. Displays a toast
     * message and logs the stack trace.
     *
     * @param e the exception thrown by a child controller
     */
    @Override
    public void onError(Exception e) {
        showToast("Error: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Registers keyboard shortcuts (Ctrl/Cmd + N/I/F/U) for major navigation
     * actions in the active scene.
     *
     * @param scene the scene to register accelerators in
     */
    private void registerShortcuts(Scene scene) {
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN),
                () -> onNewButtonClick(null)
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN),
                () -> onIssuesButtonClick(null)
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN),
                () -> onFilterButtonClick(null)
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN),
                () -> onUserButtonClicked(null)
        );
    }

    /**
     * Replaces the entire right-side content area with the provided UI node.
     *
     * @param content the node to display on the right side
     */
    private void setRightPane(Node content) {
        rightSidePane.getChildren().clear();
        rightSidePane.getChildren().add(content);

        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    /**
     * Pushes the current right-side content to the navigation stack and then
     * displays the provided content.
     *
     * @param content the new UI node to display
     */
    private void setNewRightPane(Node content) {
        if (!rightSidePane.getChildren().isEmpty()) {
            lastRightSideContents.push(rightSidePane.getChildren().get(0));
        }

        setRightPane(content);
    }

    /**
     * Navigates back to the previously displayed right-side view and restores
     * the corresponding menu button state.
     */
    public void goBack() {
        if (lastRightSideContents == null) {
            return;
        }

        Node lastNode = lastRightSideContents.pop();
        if (lastNode.equals(filterContents)) {
            restoreFilteredIssuesView();
        } else if (lastNode.equals(newIssueContents)) {
            restoreCreatedIssueView();
        } else if (lastNode.equals(issuesListContents)) {
            restoreIssuesListView();
        } else {
            setRightPane(lastNode);
        }

        makeButtonActive(lastMenuButtons.pop());
    }

    /**
     * Restores the cached New Issue view and sets input focus appropriately.
     */
    private void restoreCreatedIssueView() {
        setRightPane(newIssueContents);
        newIssueController.setFocusOnTitle();
    }

    /**
     * Restores the cached Filter Issues view and refreshes its content.
     */
    private void restoreFilteredIssuesView() {
        setRightPane(filterContents);
        filterController.refreshIssues();
    }

    /**
     * Restores the cached Issues List view.
     */
    private void restoreIssuesListView() {
        setRightPane(issuesListContents);
        issuesListController.refreshIssues();
    }

    /**
     * Visually marks the given menu button as active (highlighted) and restores
     * the previous button's appearance.
     *
     * @param button the button to activate, or null to deactivate all
     */
    private void makeButtonActive(Button button) {
        if (currentButton != null) {
            FontIcon currentIcon = (FontIcon) currentButton.getGraphic();
            currentIcon.setIconColor(Color.WHITE);
            currentButton.setGraphic(currentIcon);
        }

        currentButton = button;

        if (currentButton != null) {
            FontIcon currentIcon = (FontIcon) currentButton.getGraphic();
            currentIcon.setIconColor(Color.LIGHTBLUE);
            currentButton.setGraphic(currentIcon);
        }
    }

    /**
     * Opens or restores the Filter Issues view. If already loaded once, restores
     * cached content instead of reloading FXML.
     *
     * @param event the UI action event (may be null)
     */
    @FXML
    void onFilterButtonClick(ActionEvent event) {
        makeButtonActive(filterButton);

        if (issuesFiltered) {
            restoreFilteredIssuesView();
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("FilterView.fxml"));
        try {
            issuesFiltered = true;
            filterContents = loader.load();
            filterController = loader.getController();

            filterController.setParent(this);
            filterController.setExceptionListerner(this);

            setNewRightPane(filterContents);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Opens or restores the Issues List view. Initializes the controller on first
     * load.
     *
     * @param event the UI action event (may be null)
     */
    @FXML
    void onIssuesButtonClick(ActionEvent event) {
        makeButtonActive(issuesButton);

        if (issuesListed) {
            restoreIssuesListView();
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssuesListView.fxml"));
        try {
            issuesListed = true;
            issuesListContents = loader.load();
            issuesListController = loader.getController();

            issuesListController.setParent(this);
            issuesListController.setExceptionListener(this);
            issuesListController.initializeData();

            setNewRightPane(issuesListContents);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Opens or restores the New Issue creation view. If editing an issue, loads
     * it into the view.
     *
     * @param event the UI action event (may be null)
     */
    @FXML
    void onNewButtonClick(ActionEvent event) {
        makeButtonActive(newButton);

        if (issueUnderCreation) {
            restoreCreatedIssueView();
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewIssueView.fxml"));
        try {
            issueUnderCreation = true;
            newIssueContents = loader.load();
            newIssueController = loader.getController();

            newIssueController.setParent(this);
            newIssueController.setExceptionListerner(this);
            newIssueController.setFocusOnTitle();

            setNewRightPane(newIssueContents);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Opens the user account management view and populates it with user data.
     *
     * @param event the UI action event (may be null)
     */
    @FXML
    void onUserButtonClicked(ActionEvent event) {
        makeButtonActive(userButton);

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("AccountView.fxml"));
        try {
            HBox newContent = loader.load();
            AccountController controller = loader.getController();
            controller.setParent(this);
            controller.setUser(user);
            controller.setUserIssues(getIssuesList());

            setNewRightPane(newContent);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Displays the Issue View for the specified issue.
     *
     * @param issue the issue to display
     */
    public void setIssuePane(Issue issue) {
        makeButtonActive(null);

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssueView.fxml"));
        try {
            HBox newContent = loader.load();
            IssueViewController controller = loader.getController();

            controller.setParent(this);
            controller.initializeData(issue);

            setNewRightPane(newContent);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Adds a new issue to the IssueManager and resets creation state.
     *
     * @param issue the issue to add
     */
    public void addIssue(Issue issue) {
        try {
            manager.addIssue(issue);
            issueUnderCreation = false;
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Updates the specified issue with values from another Issue instance and
     * resets creation state.
     *
     * @param issue    the target issue to update
     * @param newValue the source containing updated field values
     */
    public void updateIssue(Issue issue, Issue newValue) {
        try {
            issue.updateFrom(newValue);
            issueUnderCreation = false;
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * Opens the New Issue view pre-populated with the provided issue for editing.
     *
     * @param issue the issue to edit
     */
    public void setEditIssuePane(Issue issue) {
        makeButtonActive(null);

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewIssueView.fxml"));
        try {
            issueUnderCreation = true;
            newIssueContents = loader.load();
            newIssueController = loader.getController();

            newIssueController.setParent(this);
            newIssueController.setIssue(issue);
            newIssueController.setExceptionListerner(this);

            setNewRightPane(newIssueContents);
        } catch (Exception e) {
            this.onError(e);
        }
    }

    /**
     * @return a list of all issues managed by the system
     */
    public List<Issue> getIssuesList() {
        return manager.getAllIssues();
    }

    /**
     * Displays a temporary toast-style message at the bottom of the screen.
     *
     * @param message the message text to display
     */
    public void showToast(String message) {
        Label toast = new Label(message);
        toast.getStyleClass().add("toast");
        toast.setOpacity(0);

        middleStackPane.getChildren().add(toast);
        StackPane.setAlignment(toast, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toast, new Insets(0, 0, 20, 0));

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition stay = new PauseTransition(Duration.seconds(3));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, stay, fadeOut);
        seq.setOnFinished(e -> middleStackPane.getChildren().remove(toast));
        seq.play();
    }
}

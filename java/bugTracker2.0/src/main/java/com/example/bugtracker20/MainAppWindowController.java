package com.example.bugtracker20;

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
import model.User;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import model.Issue;
import model.IssueManager;

import java.util.List;

public class MainAppWindowController implements ChildControllerListener {
    private IssueManager manager;

    private List<User> users;

    private User user;

    private Node lastRightSideContent;

    private boolean issueUnderCreation;

    private Node newIssueContents;
    private NewIssueController newIssueController;


    @FXML
    private Button filterButton;

    @FXML
    private Button issuesButton;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private StackPane middleStackPane;

    @FXML
    private HBox rightSidePane;

    @FXML
    private Button newButton;

    @FXML
    private Button userButton;

    private Button currentButton;

    public MainAppWindowController(IssueManager manager, List<User> users) {
        this.manager = manager;
        this.users = users;

        issueUnderCreation = false;

        //TODO: REPLACE
        user = users.get(0);
    }

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
        Tooltip.install(userButton, new Tooltip("Manage Account (" + shortcutSymbol + "A)"));


        currentButton = userButton;

        mainBorderPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                registerShortcuts(newScene);
                onUserButtonClicked(null);
            }
        });
    }

    @Override
    public void onError(Exception e) {
        showToast("Error: " + e.getMessage());
        e.printStackTrace();
    }

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
                new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN),
                () -> onUserButtonClicked(null)
        );
    }

    private void setRightPane(Node content) {
        rightSidePane.getChildren().clear();
        rightSidePane.getChildren().add(content);

        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    private void setNewRightPane(Node content) {
        if (!rightSidePane.getChildren().isEmpty()) {
            lastRightSideContent = rightSidePane.getChildren().get(0);
        }

        setRightPane(content);
    }

    public void goBack() {
        if (lastRightSideContent == null) {
            return;
        }

        setRightPane(lastRightSideContent);
    }

    public void restoreCreatedIssueView() {
        setRightPane(newIssueContents);
        newIssueController.setFocusOnTitle();
    }

    private void makeButtonActive(Button button) {
        FontIcon currentIcon = (FontIcon) currentButton.getGraphic();
        currentIcon.setIconColor(Color.WHITE);
        currentButton.setGraphic(currentIcon);

        currentButton = button;
        currentIcon = (FontIcon) currentButton.getGraphic();
        currentIcon.setIconColor(Color.LIGHTBLUE);
        currentButton.setGraphic(currentIcon);
    }

    @FXML
    void onFilterButtonClick(ActionEvent event) {

    }

    @FXML
    void onIssuesButtonClick(ActionEvent event) {
        makeButtonActive(issuesButton);

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssuesListView.fxml"));
        try {
            HBox newContent = loader.load();
            IssuesListController controller = loader.getController();

            controller.setParent(this);
            controller.setExceptionListerner(this);
            controller.initializeData();

            setNewRightPane(newContent);
        } catch (Exception e) {
            this.onError(e);
        }
    }

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

    @FXML
    void onUserButtonClicked(ActionEvent event) {
        makeButtonActive(userButton);

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("AccountView.fxml"));
        try {
            HBox newContent = loader.load();
            AccountController controller = loader.getController();
            controller.setParent(this);
            controller.setUserIssues(getIssuesList());

            setNewRightPane(newContent);
        } catch (Exception e) {
            this.onError(e);
        }
    }


    public void setIssuePane(Issue issue) {
        makeButtonActive(issuesButton);
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

    public void addIssue(Issue issue) {
        try {
            manager.addIssue(issue);
            issueUnderCreation = false;
        } catch (Exception e) {
            this.onError(e);
        }
    }

    public void updateIssue(Issue issue, Issue newValue) {
        try {
            issue.updateFrom(newValue);
            issueUnderCreation = false;
        } catch (Exception e) {
            this.onError(e);
        }
    }

    public void setEditIssuePane(Issue issue) {
        makeButtonActive(newButton);
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

    public List<Issue> getIssuesList() {
        return manager.getAllIssues();
    }

    public User getReporter() {
        return user;
    }

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

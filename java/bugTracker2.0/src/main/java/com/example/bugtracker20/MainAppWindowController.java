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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;
import model.User;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import model.Issue;
import model.IssueManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainAppWindowController implements ChildControllerListener {
    private IssueManager manager;

    private List<User> users;

    private User user;

    private Node lastRightSideContent;


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

    @FXML
    private void initialize() {
        FontIcon issuesBtnIcon = new FontIcon(MaterialDesign.MDI_VIEW_LIST);
        issuesBtnIcon.setIconSize(20);
        issuesBtnIcon.setIconColor(Color.WHITE);
        issuesButton.setGraphic(issuesBtnIcon);
        issuesButton.setText("");

        FontIcon filterBtnIcon = new FontIcon(MaterialDesign.MDI_MAGNIFY);
        filterBtnIcon.setIconSize(20);
        filterBtnIcon.setIconColor(Color.WHITE);
        filterButton.setGraphic(filterBtnIcon);
        filterButton.setText("");

        FontIcon newBtnIcon = new FontIcon(MaterialDesign.MDI_PENCIL);
        newBtnIcon.setIconSize(20);
        newBtnIcon.setIconColor(Color.WHITE);
        newButton.setGraphic(newBtnIcon);
        newButton.setText("");

        FontIcon userBtnIcon = new FontIcon(MaterialDesign.MDI_ACCOUNT);
        userBtnIcon.setIconSize(20);
        userBtnIcon.setIconColor(Color.WHITE);
        userButton.setGraphic(userBtnIcon);
        userButton.setText("");
    }

    @Override
    public void onError(Exception e) {
        showToast("Error: " + e.getMessage());
        e.printStackTrace();
    }

    public MainAppWindowController(IssueManager manager, List<User> users) {
        this.manager = manager;
        this.users = users;

        //TODO: REPLACE
        user = users.get(0);
    }

    private void setRightSidePane(Node content) {
        if (!rightSidePane.getChildren().isEmpty()) {
            lastRightSideContent = rightSidePane.getChildren().get(0);
        }
        rightSidePane.getChildren().clear();
        rightSidePane.getChildren().add(content);

        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    public void goBack() {
        if (lastRightSideContent != null) {
            rightSidePane.getChildren().clear();
            rightSidePane.getChildren().add(lastRightSideContent);

            HBox.setHgrow(lastRightSideContent, Priority.ALWAYS);
            VBox.setVgrow(lastRightSideContent, Priority.ALWAYS);
        }
    }

    @FXML
    void onFilterButtonClick(ActionEvent event) {

    }

    @FXML
    void onIssuesButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssuesListView.fxml"));
        HBox newContent = loader.load();
        IssuesListController controller = loader.getController();

        controller.setParent(this);
        controller.initializeData();

        setRightSidePane(newContent);
    }

    @FXML
    void onNewButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewIssueView.fxml"));
        HBox newContent = loader.load();
        NewIssueController controller = loader.getController();

        controller.setParent(this);
        controller.setIssueManager(manager);
        controller.setExceptionListerner(this);

        setRightSidePane(newContent);
    }

    @FXML
    void onUserButtonClicked(ActionEvent event) {

    }


    public void setIssuePane(Issue issue) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("IssueView.fxml"));
        HBox newContent = loader.load();
        IssueViewController controller = loader.getController();

        controller.setParent(this);
        controller.initializeData(issue);

        setRightSidePane(newContent);
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

        PauseTransition stay = new PauseTransition(Duration.seconds(4));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(fadeIn, stay, fadeOut);
        seq.setOnFinished(e -> middleStackPane.getChildren().remove(toast));
        seq.play();
    }
}

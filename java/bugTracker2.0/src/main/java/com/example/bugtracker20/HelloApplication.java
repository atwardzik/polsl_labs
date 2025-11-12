package com.example.bugtracker20;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Issue;
import model.IssueManager;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        List<User> users = new ArrayList<>();
        users.add(new User("Artur", "Twardzik", "at"));
        users.add(new User("J", "Twardzik", "jt"));

        IssueManager manager = new IssueManager();
        manager.addIssue(
                new Issue("Off by one is a very, very long issue title, lorem ipsum dolor sit amet will be longer than expected. " +
                        "Moreover, will contain some special chars@@@", "Serious damage", users.get(0))
        );
        manager.addIssue(
                new Issue("Buffer overflow", "Serious damage", users.get(1))
        );


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainAppWindow.fxml"));
        fxmlLoader.setControllerFactory(param -> {
            if (param == MainAppWindowController.class) {
                return new MainAppWindowController(manager, users);
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Scene scene = new Scene(fxmlLoader.load(), 720, 680);
//        stage.initStyle(StageStyle.UNIFIED);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("app.css")).toExternalForm());
        stage.setTitle("Bug Tracker");
        stage.setScene(scene);

        stage.setMinWidth(1200);

        stage.show();
    }
}

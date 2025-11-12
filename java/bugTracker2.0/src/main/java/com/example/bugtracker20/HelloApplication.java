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
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        IssueManager manager = new IssueManager();
        manager.addIssue(
                new Issue("Off by one is a very, very long issue title, lorem ipsum dolor sit amet will be longer than expected. " +
                        "Moreover, will contain some special chars@@@", "Serious damage", new User("Artur", "Twardzik", "at"))
        );
        manager.addIssue(
                new Issue("Buffer overflow", "Serious damage", new User("J", "Twardzik", "jt"))
        );

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainAppWindow.fxml"));
        fxmlLoader.setControllerFactory(param -> {
            if (param == MainAppWindowController.class) {
                return new MainAppWindowController(manager);
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
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.setMinWidth(1200);

        stage.show();
    }
}

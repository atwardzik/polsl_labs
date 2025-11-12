package com.example.bugtracker2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 680);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("app.css")).toExternalForm());
//        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.show();
    }
}

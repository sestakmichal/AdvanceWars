package ija.ija2025.homework2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // This is where your UI starts
        Label label = new Label("IJA Homework 2: JavaFX is Running!");
        Scene scene = new Scene(new StackPane(label), 640, 480);

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // This tells Java to launch the JavaFX application
        launch();
    }
}
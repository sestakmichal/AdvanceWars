package ija.ija2025.project.view.Elements;

import ija.ija2025.project.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WinnerScreen extends VBox {

    public WinnerScreen(String winnerName, Controller controller, Runnable onSave) {
        setSpacing(25);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 1), CornerRadii.EMPTY, Insets.EMPTY)));

        // block clicks going through
        setPickOnBounds(true);
        setOnMousePressed(javafx.event.Event::consume);

        Label winLabel = new Label("VICTORY!");
        winLabel.setTextFill(Color.GOLD);
        winLabel.setFont(new Font("Arial Bold", 60));

        Label subLabel = new Label(winnerName + " has conquered the battlefield!");
        subLabel.setTextFill(Color.WHITE);
        subLabel.setFont(new Font("Arial", 24));

        Button saveBtn = new Button("Save Final State (Replay)");
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-font-size: 16px; -fx-base: #2d5a27;");
        saveBtn.setOnAction(e -> onSave.run());

        Button menuBtn = new Button("Return to Menu");
        menuBtn.setPrefWidth(200);
        menuBtn.setStyle("-fx-font-size: 16px;");
        menuBtn.setOnAction(e -> controller.handleReturnToMenu());

        getChildren().addAll(winLabel, subLabel, saveBtn, menuBtn);
    }
}
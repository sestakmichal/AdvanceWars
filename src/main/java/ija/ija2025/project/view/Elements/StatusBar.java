/**
 * Authors: Michal Šesták
 * Description: Class describing the status bar for displaying infomration about the game.
 */

package ija.ija2025.project.view.Elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
public class StatusBar extends HBox {

    private final Label turnLabel;
    private final Label p1MoneyLabel;
    private final Label p2MoneyLabel;

    public StatusBar(Runnable onSave, Runnable onUndo) {
        setSpacing(40);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        setPrefWidth(800);
        setPadding(new Insets(0, 5, 0, 5));

        p1MoneyLabel = new Label("Red Money: 10000");
        p1MoneyLabel.setTextFill(Color.GOLD);

        turnLabel = new Label("Turn: Red");
        turnLabel.setTextFill(Color.WHITE);
        turnLabel.setFont(new Font("Arial", 18));

        p2MoneyLabel = new Label("Blue Money: 10000");
        p2MoneyLabel.setTextFill(Color.GOLD);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button undoButton = new Button("Undo");
        undoButton.setOnAction(e -> onUndo.run());

        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> onSave.run());

        getChildren().addAll(p1MoneyLabel, turnLabel, p2MoneyLabel, spacer, undoButton, saveButton);
    }

    public void updateMoney(String player, int amount) {
        if (player.equals("Player1")) p1MoneyLabel.setText("Red Money: " + amount);
        else if (player.equals("Player2")) p2MoneyLabel.setText("Blue Money: " + amount);
    }

    public void updateTurn(String player) {
        turnLabel.setText("Turn: " + player);
    }
}
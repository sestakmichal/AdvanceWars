package ija.ija2025.project.view;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Game.GameAction;
import ija.ija2025.project.model.Map.Position;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ActionMenu extends VBox {
    public ActionMenu(Position pos, Controller controller, Runnable onClose) {
        this.setSpacing(5);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #333; " +
                "-fx-padding: 10; " +
                "-fx-spacing: 8; " +
                "-fx-border-color: white; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 10, 0, 0, 0);");

        for (GameAction action : GameAction.values()) {
            Button button = new Button(action.toString());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> {
                controller.handleActionClicked(action);
                onClose.run();
            });
            getChildren().add(button);
        }
    }

}
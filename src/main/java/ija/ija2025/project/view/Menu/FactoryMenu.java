package ija.ija2025.project.view.Menu;

import ija.ija2025.project.controller.Controller;

import ija.ija2025.project.model.Map.Position;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static javax.swing.text.StyleConstants.setBackground;

public class FactoryMenu extends VBox {
    private static final Background MENU_BG = new Background(new BackgroundFill(Color.DARKSLATEGRAY, new CornerRadii(6), Insets.EMPTY));
    private static final Border MENU_BORDER = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1)));
    private static final DropShadow SHADOW = new DropShadow(10, Color.rgb(0, 0, 0, 0.6));

    public FactoryMenu(Controller controller, Position pos, Runnable onClose) {
        setSpacing(8);
        setPadding(new Insets(10));
        setBackground(MENU_BG);
        setBorder(MENU_BORDER);
        setEffect(SHADOW);

        // Add the specific spawn buttons
        getChildren().add(createSpawnButton("Infantry", 1000, Color.web("#4CAF50"), controller, pos, onClose));
        getChildren().add(createSpawnButton("Tank", 7000, Color.web("#F44336"), controller, pos, onClose));
        getChildren().add(createSpawnButton("Artillery", 6000, Color.web("#2196F3"), controller, pos, onClose));

        // Add a cancel button at the bottom
        Button cancel = new Button("Cancel");
        cancel.setMaxWidth(Double.MAX_VALUE);
        cancel.setOnAction(e -> onClose.run());
        getChildren().add(cancel);
    }

    private Button createSpawnButton(String name, int price, Color color, Controller controller, Position pos, Runnable onClose) {
        boolean canAfford = controller.canAffordUnit(price);

        // Label with Name and Price
        Label label = new Label(name + " ($" + price + ")");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setEffect(new DropShadow(2, Color.BLACK));

        Button button = new Button();
        button.setGraphic(label);
        button.setMaxWidth(Double.MAX_VALUE);

        if (canAfford) {
            // Normal state
            button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(6), Insets.EMPTY)));

            button.setOnMousePressed(e -> { button.setOpacity(0.7); button.setScaleX(0.95); button.setScaleY(0.95); });
            button.setOnMouseReleased(e -> { button.setOpacity(1.0); button.setScaleX(1.0); button.setScaleY(1.0); });

            button.setOnAction(e -> {
                controller.handleSpawnRequest(name, pos, price);
                onClose.run();
            });
        }
        // grayed out button => can't afford
        else {
            Color darkenedColor = color.deriveColor(0, 1, 0.3, 1.0);
            button.setBackground(new Background(new BackgroundFill(darkenedColor, new CornerRadii(6), Insets.EMPTY)));

            label.setTextFill(Color.LIGHTGRAY);

            button.setDisable(true);
            button.setOpacity(0.6);
        }

        return button;
    }
}

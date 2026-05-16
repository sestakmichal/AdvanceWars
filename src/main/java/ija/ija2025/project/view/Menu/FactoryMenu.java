/**
 * Authors: Michal Šesták
 * Description: Class for creating the unit factory menu.
 */

package ija.ija2025.project.view.Menu;

import ija.ija2025.project.controller.Controller;

import ija.ija2025.project.model.Game.UnitPrices;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.view.Theme;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

        getChildren().add(createSpawnButton("Infantry", UnitPrices.Infantry, Theme.Units.INFANTRY, controller, pos, onClose));
        getChildren().add(createSpawnButton("Tank", UnitPrices.Tank, Theme.Units.TANK, controller, pos, onClose));
        getChildren().add(createSpawnButton("Artillery", UnitPrices.Artillery, Theme.Units.ARTILLERY, controller, pos, onClose));

        getChildren().add(createCancelButton(onClose));
    }

    private Button createSpawnButton(String name, int price, Color color, Controller controller, Position pos, Runnable onClose) {
        boolean canAfford = controller.canAffordUnit(price);

        Label label = new Label(name + " ($" + price + ")");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setEffect(new DropShadow(2, Color.BLACK));

        Button button = new Button();
        button.setGraphic(label);
        button.setMaxWidth(Double.MAX_VALUE);

        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(6), Insets.EMPTY)));

        button.setOnMousePressed(e -> { button.setOpacity(0.7); button.setScaleX(0.95); button.setScaleY(0.95); });
        button.setOnMouseReleased(e -> { button.setOpacity(1.0); button.setScaleX(1.0); button.setScaleY(1.0); });

        button.setOnAction(e -> {
            controller.handleBuyRequest(name, pos, price);
            onClose.run();
        });

        if (!canAfford) {
            button.setDisable(true);
        }
        return button;
    }

    private Button createCancelButton(Runnable onClose) {
        Label label = new Label("Cancel");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setEffect(new DropShadow(2, Color.BLACK));

        Button button = new Button();
        button.setGraphic(label);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setBackground(new Background(new BackgroundFill(Theme.Basic.CANCEL, new CornerRadii(6), Insets.EMPTY)));

        button.setOnMousePressed(e -> { button.setOpacity(0.7); button.setScaleX(0.95); button.setScaleY(0.95); });
        button.setOnMouseReleased(e -> { button.setOpacity(1.0); button.setScaleX(1.0); button.setScaleY(1.0); });
        button.setOnAction(e -> onClose.run());

        return button;
    }
}

package ija.ija2025.project.view.Menu;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Game.GameAction;

import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.view.Theme;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.EnumMap;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class ActionMenu extends VBox {

    private final Map<GameAction, Button> buttons = new EnumMap<>(GameAction.class);

    private static final Background MENU_BG = new Background(new BackgroundFill(Color.DARKSLATEGRAY, new CornerRadii(6), Insets.EMPTY));
    private static final Border MENU_BORDER = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1)));
    private static final Background BUTTON_BG = new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(6), Insets.EMPTY));
    private static final DropShadow SHADOW = new DropShadow(10, Color.rgb(0, 0, 0, 0.6));

    public ActionMenu(Controller controller, Runnable onClose) {
        setSpacing(8);
        setPadding(new Insets(10));
        setBackground(MENU_BG);
        setBorder(MENU_BORDER);
        setEffect(SHADOW);

        for (GameAction action : GameAction.values()) {
            Button button = createButton(action, controller, onClose);
            buttons.put(action, button);
            getChildren().add(button);
        }
    }

    private Button createButton(GameAction action, Controller controller, Runnable onClose) {
        Label label = new Label(action.toString());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setEffect(SHADOW);

        Button button = new Button();
        button.setGraphic(label); // only visual text
        button.setMaxWidth(Double.MAX_VALUE);
        button.setBackground(new Background(new BackgroundFill(getColor(action), new CornerRadii(6), Insets.EMPTY)));

        button.setOnMousePressed(e -> {button.setOpacity(0.7);button.setScaleX(0.95);button.setScaleY(0.95);});
        button.setOnMouseReleased(e -> {button.setOpacity(1.0);button.setScaleX(1.0);button.setScaleY(1.0);});
        button.setOnAction(e -> {
            controller.handleActionClicked(action);
            onClose.run();
        });

        return button;
    }

    private Color getColor(GameAction action) {
        return switch (action) {
            case MOVE -> Theme.Actions.MOVE;
            case ATTACK -> Theme.Actions.ATTACK;
            case CAPTURE -> Theme.Actions.CAPTURE;
            case WAIT -> Theme.Actions.WAIT;
        };
    }

    public void updateButtons(Unit unit) {
        buttons.get(GameAction.MOVE).setDisable(!unit.canMove());
        buttons.get(GameAction.ATTACK).setDisable(!unit.canAct());
        buttons.get(GameAction.CAPTURE).setDisable(!unit.canAct());
        buttons.get(GameAction.WAIT).setDisable(!unit.canAct());
    }
}
package ija.ija2025.project.view.Observers;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.view.Menu.ActionMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UIObserver implements Observer {
    private final Pane uiLayer;
    private final Controller controller;
    private ActionMenu currentActionMenu = null;
    private FactoryMenu currentFactoryMenu = null;

    // status bar components
    private HBox statusBar;
    private Label turnLabel;
    private Label p1MoneyLabel;
    private Label p2MoneyLabel;

    public UIObserver(Controller controller) {
        this.uiLayer = new Pane();
        this.controller = controller;

        // click passthrough, so the grid can be selected
        this.uiLayer.setPickOnBounds(false);

        initStatusBar();
    }

    public Pane getPane() { return uiLayer; }

    public Node getStatusBar() { return this.statusBar; }

    private void showMenu(Position pos, Unit unit) {
        hideMenu();

        currentActionMenu = new ActionMenu(controller, this::hideMenu);
        currentActionMenu.updateButtons(unit);

        double x = pos.getCol() * 60;
        double y = pos.getRow() * 60;

        // offset the menu
        currentActionMenu.setLayoutX(x + 50);
        currentActionMenu.setLayoutY(y + 10);

        uiLayer.getChildren().add(currentActionMenu);
        currentActionMenu.toFront();
    }

    private void showFactoryMenu(Position pos) {
        hideMenu();

        // FactoryMenu would be a new class you create (VBox or Pane)
        currentFactoryMenu = new FactoryMenu(controller, pos, this::hideMenu);

        double x = pos.getCol() * 60;
        double y = pos.getRow() * 60;

        currentFactoryMenu.setLayoutX(x + 65);
        currentFactoryMenu.setLayoutY(y);

        uiLayer.getChildren().add(currentFactoryMenu);
    }

    private void hideMenu() {
        if (currentActionMenu != null) {
            uiLayer.getChildren().remove(currentActionMenu);
            currentActionMenu = null;
        }
        if (currentFactoryMenu != null) {
            uiLayer.getChildren().remove(currentFactoryMenu);
            currentFactoryMenu = null;
        }
    }

    private void initStatusBar() {
        statusBar = new HBox(40); // 40px spacing
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.7),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        statusBar.setPrefWidth(800);


        p1MoneyLabel = new Label("P1 Money: 10000");
        p1MoneyLabel.setTextFill(Color.GOLD);

        turnLabel = new Label("Turn: Player1");
        turnLabel.setTextFill(Color.WHITE);
        turnLabel.setFont(new Font("Arial", 18));

        p2MoneyLabel = new Label("P2 Money: 10000");
        p2MoneyLabel.setTextFill(Color.GOLD);

        statusBar.getChildren().addAll(p1MoneyLabel, turnLabel, p2MoneyLabel);
    }

    @Override
    public void update(Event event) {
        if (event instanceof UnitPositionShownEvent(Position position, Unit unit)) {
            showMenu(position, unit);
        }
        if (event instanceof PositionsHiddenEvent) {
            hideMenu();
        }
        if (event instanceof MenuHiddenEvent) {
            hideMenu();
        }

        if (event instanceof MoneyEarnedEvent(String player, int amount)) {
            if (player.equals("Player1")) {
                p1MoneyLabel.setText("P1 Money: " + amount);
            }
            else if (player.equals("Player2")) {
                p2MoneyLabel.setText("P2 Money: " + amount);
            }
        }
        if (event instanceof TurnChangedEvent(String player)) {
            turnLabel.setText("Turn: " + player);
        }
    }
}
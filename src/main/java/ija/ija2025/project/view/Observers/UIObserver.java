package ija.ija2025.project.view.Observers;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Units.Infantry;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.view.Elements.StatusBar;
import ija.ija2025.project.view.Elements.WinnerScreen;
import ija.ija2025.project.view.Menu.ActionMenu;
import ija.ija2025.project.view.Menu.FactoryMenu;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UIObserver implements Observer {
    private final Pane uiLayer;
    private final Controller controller;
    private ActionMenu currentActionMenu = null;
    private FactoryMenu currentFactoryMenu = null;

    // status bar components
    private StatusBar statusBar;

    public UIObserver(Controller controller) {
        this.uiLayer = new Pane();
        this.controller = controller;
        this.uiLayer.setPickOnBounds(false);    // click passthrough, so the grid can be selected

        this.statusBar = new StatusBar(() -> {
            Stage stage = (Stage) statusBar.getScene().getWindow();
            openSaveDialog(stage);
        });
    }

    public Pane getPane() { return uiLayer; }

    public Node getStatusBar() { return this.statusBar; }

    private void showActionMenu(Position pos, Unit unit) {
        hideMenu();

        boolean skipCapture = !(unit instanceof Infantry);
        currentActionMenu = new ActionMenu(controller, this::hideMenu, skipCapture);
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

        currentFactoryMenu = new FactoryMenu(controller, pos, this::hideMenu);

        double x = pos.getCol() * 60;
        double y = pos.getRow() * 60;

        currentFactoryMenu.setLayoutX(x + 50);
        currentFactoryMenu.setLayoutY(y + 10);

        uiLayer.getChildren().add(currentFactoryMenu);
        currentFactoryMenu.toFront();
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

    private void showWinnerScreen(String winnerName) {
        hideMenu();
        Scene scene = uiLayer.getScene();
        if (scene == null) return;

        WinnerScreen screen = new WinnerScreen(winnerName, controller, () -> {
            Stage stage = (Stage) uiLayer.getScene().getWindow();
            openSaveDialog(stage);
        });

        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.prefHeightProperty().bind(scene.heightProperty());

        if (scene.getRoot() instanceof StackPane rootStack) {
            rootStack.getChildren().add(screen);
            screen.toFront();
        }
    }

    private void openSaveDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        // ... setup ...
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                controller.handleSaveGame(file.getAbsolutePath());
                showNotification("Success", "Game saved to: " + file.getName(), false);
            } catch (Exception e) {
                showNotification("Save Error", e.getMessage(), true);
            }
        }
    }

    private void showNotification(String title, String message, boolean isError) {
        Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @Override
    public void update(Event event) {
        if (event instanceof UnitPositionShownEvent(Position position, Unit unit)) {
            showActionMenu(position, unit);
        }
        if (event instanceof FactoryMenuShownEvent(Position position)) {
            showFactoryMenu(position);
        }
        if (event instanceof PositionsHiddenEvent) {
            hideMenu();
        }
        if (event instanceof MenuHiddenEvent) {
            hideMenu();
        }

        if (event instanceof MoneyChangedEvent(String player, int money)) {
            statusBar.updateMoney(player, money);
        }

        if (event instanceof TurnChangedEvent(String player)) {
            statusBar.updateTurn(player);
        }

        if (event instanceof HqCapturedEvent(String whichPlayerHQ)) {
            String winner = whichPlayerHQ.equals("Player1") ? "Red" : "Blue";
            showWinnerScreen(winner);
        }
    }
}
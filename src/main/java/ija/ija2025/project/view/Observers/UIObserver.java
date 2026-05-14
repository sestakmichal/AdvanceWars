package ija.ija2025.project.view.Observers;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Units.Infantry;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.view.Menu.ActionMenu;
import ija.ija2025.project.view.Menu.FactoryMenu;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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

    private void initStatusBar() {
        statusBar = new HBox(40); // 40px spacing
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.7),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        statusBar.setPrefWidth(800);
        statusBar.setPadding(new Insets(0, 10, 0, 10));

        // game status
        p1MoneyLabel = new Label("Red Money: 10000");
        p1MoneyLabel.setTextFill(Color.GOLD);

        turnLabel = new Label("Turn: Red");
        turnLabel.setTextFill(Color.WHITE);
        turnLabel.setFont(new Font("Arial", 18));

        p2MoneyLabel = new Label("Blue Money: 10000");
        p2MoneyLabel.setTextFill(Color.GOLD);

        // save btn
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> {
            Stage stage = (Stage) statusBar.getScene().getWindow();
            openSaveDialog(stage);
        });

        statusBar.getChildren().addAll(p1MoneyLabel, turnLabel, p2MoneyLabel, spacer, saveButton);
    }

    private void showWinnerScreen(String winnerName) {
        hideMenu();

        VBox winnerOverlay = new VBox(25);
        winnerOverlay.setAlignment(Pos.CENTER);

        Scene scene = uiLayer.getScene();
        if (scene == null) return;

        winnerOverlay.prefWidthProperty().bind(scene.widthProperty());
        winnerOverlay.prefHeightProperty().bind(scene.heightProperty());

        winnerOverlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 1),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        // not allow clicks beyond win screen
        winnerOverlay.setPickOnBounds(true);
        winnerOverlay.setOnMousePressed(javafx.event.Event::consume);

        Label winLabel = new Label("VICTORY!");
        winLabel.setTextFill(Color.GOLD);
        winLabel.setFont(new Font("Arial Bold", 60));

        Label subLabel = new Label(winnerName + " has conquered the battlefield!");
        subLabel.setTextFill(Color.WHITE);
        subLabel.setFont(new Font("Arial", 24));

        Button saveBtn = new Button("Save Final State (Replay)");
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-font-size: 16px; -fx-base: #2d5a27;");
        saveBtn.setOnAction(e -> {
            Stage stage = (Stage) uiLayer.getScene().getWindow();
            openSaveDialog(stage);
        });

        Button menuBtn = new Button("Return to Menu");
        menuBtn.setPrefWidth(200);
        menuBtn.setStyle("-fx-font-size: 16px;");
        menuBtn.setOnAction(e -> controller.handleReturnToMenu());

        winnerOverlay.getChildren().addAll(winLabel, subLabel, saveBtn, menuBtn);

        if (scene.getRoot() instanceof StackPane rootStack) {
            rootStack.getChildren().add(winnerOverlay);
            winnerOverlay.toFront();
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
            if (player.equals("Player1")) {
                p1MoneyLabel.setText("Red Money: " + money);
            }
            else if (player.equals("Player2")) {
                p2MoneyLabel.setText("Blue Money: " + money);
            }
        }

        if (event instanceof TurnChangedEvent(String player)) {
            turnLabel.setText("Turn: " + player);
        }

        if (event instanceof HqCapturedEvent(String whichPlayerHQ)) {
            String winner = whichPlayerHQ.equals("Player1") ? "Red" : "Blue";
            showWinnerScreen(winner);
        }
    }
}
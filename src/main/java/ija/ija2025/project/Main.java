package ija.ija2025.project;

import ija.ija2025.project.model.Map.GameMap;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.controller.*;

import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private Game game;
    Controller controller;

    @Override
    public void start(Stage stage) {
        showMenu(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void showMenu(Stage stage) {
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("IJA 2025 - ADVANCE WARS");
        title.setTextFill(Color.WHITE);
        title.setFont(new Font("Arial Bold", 32));

        // load preset map
        Button btnLoadMap1 = new Button("Load Preset Map1");
        btnLoadMap1.setOnAction(e -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map1.json")));
        Button btnLoadMap2 = new Button("Load Preset Map2 - win Showcase");
        btnLoadMap2.setOnAction(e -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map2.json")));

        // load save file
        Button btnLoadSave = new Button("Load Save File");
        btnLoadSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/Maps"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                launchGame(stage, GameFactory.createGameFromJson(file.getAbsolutePath()));
            }
        });

        // random map
        Button btnGenerate = new Button("Generate Random Map");
        btnGenerate.setOnAction(e -> {
            Game generatedGame = GameFactory.generateNewGame(10, 15);
            launchGame(stage, generatedGame);
        });

        String btnStyle = "-fx-min-width: 200; -fx-font-size: 14;";
        btnLoadMap1.setStyle(btnStyle);
        btnLoadMap2.setStyle(btnStyle);
        btnLoadSave.setStyle(btnStyle);
        btnGenerate.setStyle(btnStyle);

        menuLayout.getChildren().addAll(title, btnLoadMap1, btnLoadMap2, btnLoadSave, btnGenerate);

        Scene menuScene = new Scene(menuLayout, 800, 600);
        stage.setTitle("IJA 2025 - Main Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    private void launchGame(Stage stage, Game game) {
        Controller controller = new Controller(game);
        controller.setOnReturnToMenu(() -> showMenu(stage)); // add callback to return to the menu

        MapObserver mapView = new MapObserver(game.getSize());
        UIObserver UIView = new UIObserver(controller);

        game.addObserver(mapView);
        game.addObserver(UIView);

        mapView.initializeMap(controller, game.getMap());
        game.notifyInitialState();

        Scene scene = SceneFactory.initializeScene(stage, mapView.getGrid(), UIView.getPane(), UIView.getStatusBar());

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }
}
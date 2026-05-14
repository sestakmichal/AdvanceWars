package ija.ija2025.project;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.controller.*;

import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
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
        VBox menuLayout = new VBox(15);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #2c3e50; -fx-padding: 30;");

        Label title = new Label("IJA 2025 - ADVANCE WARS");
        title.setTextFill(Color.WHITE);
        title.setFont(new Font("Arial Bold", 32));

        // players
        HBox playerSelection = new HBox(20);
        playerSelection.setAlignment(Pos.CENTER);
        ComboBox<String> p1Type = new ComboBox<>(FXCollections.observableArrayList("Player", "Bot"));
        p1Type.setValue("Player");
        ComboBox<String> p2Type = new ComboBox<>(FXCollections.observableArrayList("Player", "Bot"));
        p2Type.setValue("Bot");
        playerSelection.getChildren().addAll(
                new Label("P1:") {{ setTextFill(Color.WHITE); }}, p1Type,
                new Label("P2:") {{ setTextFill(Color.WHITE); }}, p2Type
        );

        VBox genSettings = new VBox(10);
        genSettings.setAlignment(Pos.CENTER);
        genSettings.setMaxWidth(300);
        genSettings.setStyle("-fx-background-color: #34495e; -fx-padding: 15; -fx-background-radius: 10;");

        Label genTitle = new Label("Random Map Settings");
        genTitle.setTextFill(Color.LIGHTBLUE);

        // map size
        Slider sizeSlider = createConfiguredSlider(10, 20, 15);
        Label sizeLabel = new Label("Size: 15x15");
        sizeLabel.setTextFill(Color.WHITE);
        sizeSlider.valueProperty().addListener((obs, oldV, newV) ->
                sizeLabel.setText("Size: " + newV.intValue() + "x" + newV.intValue()));

        // city slider
        Slider citySlider = createConfiguredSlider(0, 20, 5);
        Label cityLabel = new Label("Extra Cities: 5");
        cityLabel.setTextFill(Color.WHITE);
        citySlider.valueProperty().addListener((obs, oldV, newV) ->
                cityLabel.setText("Extra Cities: " + newV.intValue()));

        // factory slider
        Slider factorySlider = createConfiguredSlider(0, 10, 2);
        Label factoryLabel = new Label("Extra Factories: 2");
        factoryLabel.setTextFill(Color.WHITE);
        factorySlider.valueProperty().addListener((obs, oldV, newV) ->
                factoryLabel.setText("Factories per player: " + newV.intValue()));

        genSettings.getChildren().addAll(genTitle, sizeLabel, sizeSlider, cityLabel, citySlider, factoryLabel, factorySlider);

        Button btnGenerate = new Button("Generate & Launch");
        btnGenerate.setOnAction(e -> {
            // Předáme hodnoty ze sliderů do vaší Factory
            Game generatedGame = GameFactory.generateNewGame(
                    (int) sizeSlider.getValue(),
                    (int) sizeSlider.getValue(),
                    (int) citySlider.getValue(),
                    (int) factorySlider.getValue()
            );
            launchGame(stage, generatedGame, p1Type.getValue(), p2Type.getValue());
        });

        // load preset map
        Button btnLoadMap1 = new Button("Load Preset Map 1");
        btnLoadMap1.setOnAction(e -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map1.json"), p1Type.getValue(), p2Type.getValue()));
        Button btnLoadMap2 = new Button("Load Preset Map2 - win Showcase");
        btnLoadMap2.setOnAction(e -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map2.json"), p1Type.getValue(), p2Type.getValue()));

        // load save file
        Button btnLoadSave = new Button("Load Save File");
        btnLoadSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/Maps"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                launchGame(stage, GameFactory.createGameFromJson(file.getAbsolutePath()), p1Type.getValue(), p2Type.getValue());
            }
        });

        String btnStyle = "-fx-min-width: 200; -fx-font-size: 14;";
        btnGenerate.setStyle(btnStyle);
        btnLoadMap1.setStyle(btnStyle);
        btnLoadMap2.setStyle(btnStyle);
        btnLoadSave.setStyle(btnStyle);

        menuLayout.getChildren().addAll(title, playerSelection, genSettings, btnGenerate, btnLoadMap1, btnLoadMap2, btnLoadSave);

        Scene menuScene = new Scene(menuLayout, 800, 600);
        stage.setTitle("IJA 2025 - Main Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    private Slider createConfiguredSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit((max - min) / 2);
        slider.setBlockIncrement(1);
        return slider;
    }

    private void launchGame(Stage stage, Game game, String p1, String p2) {
        Controller controller = new Controller(game);
        controller.setOnReturnToMenu(() -> showMenu(stage)); // add callback to return to the menu

        MapObserver mapView = new MapObserver(game.getSize());
        UIObserver UIView = new UIObserver(controller);

        game.addObserver(mapView);
        game.addObserver(UIView);

        mapView.initializeMap(controller, game.getMap(), game.getBuildingsMap());
        game.notifyInitialState();

        Scene scene = SceneFactory.initializeScene(stage, mapView.getGrid(), UIView.getPane(), UIView.getStatusBar());

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }
}
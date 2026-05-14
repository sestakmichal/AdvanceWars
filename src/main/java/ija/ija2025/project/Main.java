package ija.ija2025.project;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.view.Elements.MainMenuScreen;
import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.scene.control.Slider;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private final MainMenuScreen mainMenuView = new MainMenuScreen();

    @Override
    public void start(Stage stage) {
        showMenu(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showMenu(Stage stage) {
        Scene menuScene = mainMenuView.createScene(
                stage,
                () -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map1.json")),
                () -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map2.json")),
                (File file) -> launchGame(stage, GameFactory.createGameFromJson(file.getAbsolutePath())),
                () -> launchGame(stage, GameFactory.generateNewGame(10, 15))
        );

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
        controller.setOnReturnToMenu(() -> showMenu(stage));

        MapObserver mapView = new MapObserver(game.getSize());
        UIObserver uiView = new UIObserver(controller);

        game.addObserver(mapView);
        game.addObserver(uiView);

        mapView.initializeMap(controller, game.getMap());
        game.notifyInitialState();

        Scene scene = SceneFactory.initializeScene(stage,mapView.getGrid(),uiView.getPane(), uiView.getStatusBar());

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }
}
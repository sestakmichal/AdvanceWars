package ija.ija2025.homework2;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Controller;
import ija.ija2025.homework2.game.Game;
import ija.ija2025.homework2.game.GameFactory;
import ija.ija2025.homework2.tool.MapObserver;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.UI.SpriteFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    private Game game;
    Controller controller;

    private final String[] mapDefinition = {
            "P P P F F F F P P P P P",
            "P P F F F F F F P P P P",
            "P F F F M M F F F P P P",
            "P F F M M M M F F F P P",
            "W W W W W W W W P P P P",
            "P P P P P P W W W P P P",
            "P P P C P P P W W W P P",
            "P P P P P P P P W W W P",
            "M M P P P P P P P W W W",
            "M M M P P P P P P P W W",
    };

    @Override
    public void start(Stage stage) {

        game = GameFactory.createGame(mapDefinition);
        controller = new Controller(game);
        Position size = game.getSize();

        MapObserver mapView = new MapObserver(size);

        game.addObserver(mapView);

        mapView.initialize(controller, game.getMap());
        controller.initializeMockUnits(game);

        ScrollPane root = new ScrollPane(mapView.getNode());
        Scene scene = new Scene(root, 850, 650);

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
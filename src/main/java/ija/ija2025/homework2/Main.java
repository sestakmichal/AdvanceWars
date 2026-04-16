package ija.ija2025.homework2;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Controller;
import ija.ija2025.homework2.game.Events.Event;
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
    private int TileSize = 60;

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
        Controller gameController = new Controller(game);

        // UI tile generation
        Position size = game.getSize();
        Tile[][] uiTiles = new Tile[size.getRow()][size.getCol()];
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #333;");
        grid.setHgap(1);
        grid.setVgap(1);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {
                Tile tile = new Tile(r, c, game.getTerrainAt(new Position(r, c)), TileSize); // !TODO debate

                tile.setOnMouseClicked(e -> gameController.onTileClicked(tile));

                uiTiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }

        MapObserver mo = new MapObserver(uiTiles);
        game.addObserver(mo);

        // add mock units
        gameController.SpawnUnit("Tank", "Player1", new Position(0, 0));
        gameController.SpawnUnit("Infantry", "Player1", new Position(0, 1));
        gameController.SpawnUnit("Artillery", "Player1", new Position(0, 2));
        gameController.SpawnUnit("Tank", "Player2", new Position(1, 9));
        gameController.SpawnUnit("Infantry", "Player2", new Position(1, 10));
        gameController.SpawnUnit("Artillery", "Player2", new Position(1, 11));

        ScrollPane root = new ScrollPane(grid);
        Scene scene = new Scene(root, 850, 650);

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
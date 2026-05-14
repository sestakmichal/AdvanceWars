package ija.ija2025.project.view.Elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class MainMenuScreen {

    public Scene createScene(
            Stage stage,
            Runnable onLoadMap1,
            Runnable onLoadMap2,
            Consumer<File> onLoadSaveFile,
            Runnable onGenerateRandom
    ) {
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(30));
        menuLayout.setBackground(new Background(
                new BackgroundFill(Color.web("#2c3e50"), CornerRadii.EMPTY, Insets.EMPTY)
        ));

        Label title = new Label("IJA 2025 - ADVANCE WARS");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", 32));

        Button btnLoadMap1 = createMenuButton("Load Preset Map1", e -> onLoadMap1.run());
        Button btnLoadMap2 = createMenuButton("Load Preset Map2 - win Showcase", e -> onLoadMap2.run());
        Button btnLoadSave = createMenuButton("Load Save File", e -> openSaveFile(stage, onLoadSaveFile));
        Button btnGenerate = createMenuButton("Generate Random Map", e -> onGenerateRandom.run());

        menuLayout.getChildren().addAll(title, btnLoadMap1, btnLoadMap2, btnLoadSave, btnGenerate);

        return new Scene(menuLayout, 800, 600);
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setPrefWidth(240);
        button.setMinHeight(40);
        button.setFont(Font.font(14));
        button.setOnAction(action);
        return button;
    }

    private void openSaveFile(Stage stage, Consumer<File> onLoadSaveFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data/Maps"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            onLoadSaveFile.accept(file);
        }
    }
}

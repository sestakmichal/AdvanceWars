package ija.ija2025.project.view;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Units.Unit;
import javafx.scene.layout.Pane;

public class UIObserver implements Observer {
    private final Pane uiLayer; // The JavaFX Pane where the menu sits
    private final Controller controller;
    private ActionMenu currentMenu = null;

    public UIObserver(Controller controller) {
        this.uiLayer = new Pane();
        this.controller = controller;

        // click passthrough, so the grid can be selected
        this.uiLayer.setPickOnBounds(false);
    }

    public Pane getPane() { return uiLayer; }

    private void showMenu(Position pos, Unit unit) {
        hideMenu();

        currentMenu = new ActionMenu(controller, this::hideMenu);
        currentMenu.updateButtons(unit);

        double x = pos.getCol() * 60;
        double y = pos.getRow() * 60;

        // offset the menu
        currentMenu.setLayoutX(x + 50);
        currentMenu.setLayoutY(y + 10);

        uiLayer.getChildren().add(currentMenu);
        currentMenu.toFront();
    }

    private void hideMenu() {
        if (currentMenu != null) {
            uiLayer.getChildren().remove(currentMenu);
            currentMenu = null;
        }
    }



    @Override
    public void update(Event event) {
        if (event instanceof UnitPositionSelectedEvent(Position position, Unit unit)) {
            showMenu(position, unit);
        }
        if (event instanceof PositionsHiddenEvent) {
            hideMenu();
        }
        if (event instanceof MenuHiddenEvent) {
            hideMenu();
        }
    }
}
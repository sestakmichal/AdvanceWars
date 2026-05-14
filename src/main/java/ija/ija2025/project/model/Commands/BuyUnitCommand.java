/**
 * Authors: Matej Čapek
 * Description: Class describing the command for buying units.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Map.Position;

public class BuyUnitCommand implements Command {
    private String unitType;
    private Position position;
    private int price;

    @JsonIgnore
    private Game game;

    public BuyUnitCommand() {}

    public BuyUnitCommand(Game game, String unitType, Position position, int price) {
        this.game = game;
        this.unitType = unitType;
        this.position = position;
        this.price = price;
    }

    @Override
    public void execute() { if (game != null) game.buyUnit(unitType, position, price);}

    @Override
    public void undo() { if (game != null) game.reverseBuyUnit(unitType, position, price);}

    public String getUnitType() { return unitType; }
    public Position getPosition() { return position; }
    public int getPrice() { return price; }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "buy";
    }
}
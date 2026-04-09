package ija.ija2025.homework2.common.Commands;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Game;

public class MoveCommand implements Command {
    private Position from, to;
    private Unit unit;
    private Game game;

    public MoveCommand(Game game, Position from, Position to) {
        this.game = game;
        this.from = from;
        this.to = to;
        this.unit = game.getUnitAt(from);
    }

    @Override
    public void execute() {
        game.moveUnit(from, to);
        //game.updateObservers(new GameEvent(from, to, unit));
    }

    @Override
    public void undo() {
        game.moveUnit(to, from);
        //game.notifyObservers(new GameEvent(to, from, unit));
    }
}
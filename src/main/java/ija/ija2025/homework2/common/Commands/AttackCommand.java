package ija.ija2025.homework2.common.Commands;

import ija.ija2025.homework2.common.TerrainType;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Events.Event;
import ija.ija2025.homework2.game.Events.EventType;
import ija.ija2025.homework2.game.Game;

public class AttackCommand implements Command {
    private Game game;
    private Unit attacker, target;
    private int damageDealt;
    private boolean wasDestroyed;

    public AttackCommand(Game game, Unit attacker, Unit target) {
        this.game = game;
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {
        // defender terrain type for bonus defence
        TerrainType terrain = game.getTerrainAt(target.getPosition());

        this.damageDealt = target.takeDamage(attacker, terrain);

        if (target.isDestroyed()) {
            this.wasDestroyed = true;
            game.removeUnit(target);
            // game.notifyObservers(new Event(EventType.UNIT_DESTROYED);
        }
        else {
            // notify Unit attacked
            // game.notifyObservers(new Event(EventType.UNIT_ATTACKED);
        }
    }

    @Override
    public void undo() {
        // heal target
        // respawn unit if it was destroyed
        // notify
    }
}

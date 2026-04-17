package ija.ija2025.homework2.model.Commands;

import ija.ija2025.homework2.model.Map.TerrainType;
import ija.ija2025.homework2.model.Units.Unit;
import ija.ija2025.homework2.model.Game.Game;

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
        }
        else {

        }
    }

    @Override
    public void undo() {
        // heal target
        // respawn unit if it was destroyed
    }
}

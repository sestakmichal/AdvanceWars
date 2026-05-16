/**
 * Authors: Michal Šesták
 * Description: Command action .
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Commands.Results.AttackResult;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Map.Position;

public class AttackCommand implements Command {
    private Unit attacker;
    private Unit target;
    private Position attackerPos;
    private Position targetPos;

    private int previousTargetHP;
    private int previousAttackerHP;
    private boolean targetDestroyed;
    private boolean attackerDestroyed;

    @JsonIgnore
    private Game game;

    public AttackCommand() {}

    public AttackCommand(Game game, Unit attacker, Unit target) {
        this.game = game;
        this.attacker = attacker;
        this.target = target;
        this.attackerPos = attacker.getPosition();
        this.targetPos = target.getPosition();
    }

    @Override
    public void execute() {
        Unit attacker = game.getUnitAt(attackerPos);
        Unit target = game.getUnitAt(targetPos);

        AttackResult result = game.attackUnit(attacker, target);
        previousTargetHP = result.hpBefore();
        previousAttackerHP = result.attackerHpBefore();
        targetDestroyed = result.destroyed();
        attackerDestroyed = result.attackerDestroyed();
    }

    @Override
    public void undo() {
        game.reverseAttackUnit(attacker, target, previousTargetHP, previousAttackerHP, targetDestroyed, attackerDestroyed);
    }

    public Position getAttackerPos() { return attackerPos; }
    public Position getTargetPos() { return targetPos; }
    public int getPreviousTargetHP() { return previousTargetHP; }
    public int getPreviousAttackerHP() { return previousAttackerHP; }
    public boolean isTargetDestroyed() { return targetDestroyed; }
    public boolean isAttackerDestroyed() { return attackerDestroyed; }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "attack";
    }
}
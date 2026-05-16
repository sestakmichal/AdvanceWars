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
    private String attackerType;
    private String attackerOwner;
    private String targetType;
    private String targetOwner;

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

        this.attackerType = attacker.getType();
        this.attackerOwner = attacker.getOwner();
        this.targetType = target.getType();
        this.targetOwner = target.getOwner();
    }

    @Override
    public void execute() {
        AttackResult result = game.attackUnit(attacker, target);
        previousTargetHP = result.hpBefore();
        previousAttackerHP = result.attackerHpBefore();
        targetDestroyed = result.destroyed();
        attackerDestroyed = result.attackerDestroyed();
    }

    @Override
    public void undo() {
        game.reverseAttackUnit(
            attackerPos, targetPos,
            attackerType, attackerOwner,
            targetType, targetOwner,
            previousAttackerHP, previousTargetHP,
            attackerDestroyed, targetDestroyed
        );
    }

    public Position getAttackerPos() { return attackerPos; }
    public Position getTargetPos() { return targetPos; }
    public int getPreviousTargetHP() { return previousTargetHP; }
    public int getPreviousAttackerHP() { return previousAttackerHP; }
    public boolean isTargetDestroyed() { return targetDestroyed; }
    public boolean isAttackerDestroyed() { return attackerDestroyed; }

    public String getAttackerType() { return attackerType; }
    public void setAttackerType(String attackerType) { this.attackerType = attackerType; }
    public String getAttackerOwner() { return attackerOwner; }
    public void setAttackerOwner(String attackerOwner) { this.attackerOwner = attackerOwner; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getTargetOwner() { return targetOwner; }
    public void setTargetOwner(String targetOwner) { this.targetOwner = targetOwner; }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "attack";
    }
}
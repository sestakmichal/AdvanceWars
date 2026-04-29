package ija.ija2025.project.model.Units;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;

import java.util.Set;

public abstract class Unit {
    private String owner;
    private Position position;
    private int health;
    boolean destroyed;
    UnitActionState actionState;

    public Unit(String owner, int row, int col) {
        this.owner = owner;
        position = new Position(row, col);
        health = 100;
        destroyed = false;
        actionState = UnitActionState.READY;
    }

    public String getOwner() { return owner; }
    public Position getPosition() { return position; }
    public void setPosition(Position newPos) { position = newPos; }
    public int getHealth() { return health; }
    public boolean isDestroyed() { return destroyed; }

    public boolean canMove() { return actionState == UnitActionState.READY; }
    public boolean canAct() { return actionState != UnitActionState.DONE; }
    public void setMoved() { actionState = UnitActionState.MOVED; }
    public void setActed() { actionState = UnitActionState.DONE; }
    public void restoreState() { actionState = UnitActionState.READY; }


    public int getDefenseBonus(TerrainType type) {
        return switch (type) {
            case Plain -> 1;
            case Forest -> 2;
            case Mountain -> 4;
            case City -> 3;
            case Factory -> 3;
            case HQ -> 4;
            default -> 0;
        };
    }


    public int getFinalDamage(Unit attacker, TerrainType terrainType) {
        double damage = getDamageValue(attacker) * (attacker.getHealth()/100.0) * (1-terrainType.defenseBonus() *0.1);
        int damageRound = (int) Math.round(damage);
        int actualDamage = Math.min(health, damageRound);
        return actualDamage;
    }
    // int damage = basic_attack * hp_attacker/100 * (1-terrain_bonus * 0.1)
    public void takeDamage(Unit attacker, TerrainType terrainType) {
        int damage = getFinalDamage(attacker, terrainType);

        health -= damage;
        if (health <= 0) {
            health = 0;
            destroyed = true;
        }
    }

    public void receiveRecovery(int recoverHealth) {
        health = Math.min(100, health + recoverHealth);
    }

    public boolean isOwnedBy(String player) {return owner.equals(player);}

    public abstract int getMoveRange();
    public abstract int getMovementPrice(TerrainType terrain);
    public abstract Set<Integer> getAttackRange();
    public abstract int getDamageValue(Unit fromUnit);
    public abstract int getPrice();
    public abstract String getType();

    @Override
    public String toString() { /// add type
        return String.format("Unit: %s at position [%d, %d] with %d HP",
                getClass().getSimpleName(), position.getRow(), position.getCol(), health);
    }
}
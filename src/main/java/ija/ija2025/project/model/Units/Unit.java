package ija.ija2025.project.model.Units;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;

import java.util.Set;

public abstract class Unit {
    private String owner;
    private Position position;
    private int health;
    boolean destroyed;
    boolean moved;
    boolean acted;

    public Unit(String owner, int row, int col) {
        this.owner = owner;
        position = new Position(row, col);
        health = 100;
        destroyed = false;
        moved = false;
        acted = false;
    }

    public String getOwner() { return owner; }
    public Position getPosition() { return position; }
    public void setPosition(Position newPos) { position = newPos; }
    public int getHealth() { return health; }
    public void setHealth(int hpAmount) { this.health = Math.max(0, Math.min(100, hpAmount)); } // cap in range <0, 100>
    public boolean isDestroyed() { return destroyed; }

    public boolean canMove() { return !moved && !acted; }
    public boolean canAct() { return !acted; }
    public void setMoved() { moved = true; }
    public void setActed() { acted = true; }
    public void resetMoved() { moved = false; }
    public void resetActed() { acted = false; }
    public void resetMovedActed() { moved = false; acted = false; }

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
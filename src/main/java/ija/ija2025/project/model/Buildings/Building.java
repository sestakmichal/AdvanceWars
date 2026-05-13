package ija.ija2025.project.model.Buildings;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;

public class Building {
    private TerrainType type;
    private String owner;
    private Position position;

    private int health;
    private final int maxHealth;

    public Building(TerrainType type, String owner, int row, int col) {
        this.type = type;
        this.owner = (owner == null || owner.isBlank()) ? "Neutral" : owner;

        this.position = new Position(row, col);

        this.maxHealth = 20;
        this.health = maxHealth;
    }

    public TerrainType getType() { return type; }
    public String getOwner() { return owner; }
    public Position getPosition() { return position; }
    public int getHealth() { return health; }
    public void setHealth(int hpAmount) { this.health = Math.max(0, Math.min(maxHealth, hpAmount)); } // cap in range <0, 100>
    public int getMaxHp() { return maxHealth; }

    public boolean isOwnedBy(String player) { return owner.equals(player); }
    public boolean isNeutral() { return owner.equals("Neutral"); }

    public void changeOwner(String newOwner) {
        this.owner = (newOwner == null) ? "Neutral" : newOwner;
        this.health = maxHealth; // reset capture progress
    }

    public void takeCaptureDamage(int amount, String capturingPlayer) {
        if (isOwnedBy(capturingPlayer)) return;
        health -= amount;
        if (health <= 0) {
            changeOwner(capturingPlayer);
        }
    }

    public void repair(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public int getIncome() {
        return switch (type) {
            case City -> 1000;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return String.format(
                "Building: %s (%s) at [%d,%d] HP:%d/%d",
                type, owner,
                position.getRow(), position.getCol(),
                health, maxHealth
        );
    }
}

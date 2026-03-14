package ija.ija2025.homework2.common;

public abstract class Unit {
    private String type;
    private String unitOwner;
    private Position position;
    private int health;

    public Unit(String type, String unitOwner, int row, int col) {
        this.type = type;
        this.unitOwner = unitOwner;
        position = new Position(row, col);
        health = 100;
    }

    public Position getPosition() { return position; }
    public void setPosition(Position newPos) { position = newPos; }

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

    public abstract int getMoveRange();
    public abstract int getMovementPrice(TerrainType terrain);

    @Override
    public String toString() {
        return String.format("Unit: %s at position [%d, %d] with %d HP", type, position.getRow(), position.getCol(), health);
    }
}
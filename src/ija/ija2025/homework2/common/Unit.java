package ija.ija2025.homework2.common;

public class Unit {
    private String type;
    private String unitOwner;
    private Position position;
    private int health;

    public Unit(String type, String unitOwner, int x, int y) {
        this.type = type;
        this.unitOwner = unitOwner;
        position = new Position(x, y);
        health = 100;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return String.format("Unit: %s at position [%d, %d] with %d HP",
                                    type, position.getX(), position.getY(), health);
    }
}

package ija.ija2025.homework2.common.units;

public class Artillery extends Vehicle {

    public Artillery(String type, String unitOwner, int row, int col) {
        super(type, unitOwner, row, col);
    }

    @Override
    public int getMoveRange() { return 5; }

    @Override
    public int getDamageValue(String fromType) {
        return switch (fromType) {
            case "Artillery" -> 75;
            case "Infantry" -> 15;
            case "Tank" -> 70;
            default -> 0;
        };
    }
}
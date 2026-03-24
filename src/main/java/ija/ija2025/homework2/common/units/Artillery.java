package main.java.ija.ija2025.homework2.common.units;

public class Artillery extends Vehicle {

    public Artillery(String type, String unitOwner, int row, int col) {
        super(type, unitOwner, row, col);
    }

    @Override
    public int getMoveRange() { return 5; }
}
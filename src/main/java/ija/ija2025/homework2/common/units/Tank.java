package main.java.ija.ija2025.homework2.common.units;

public class Tank extends Vehicle {

    public Tank(String type, String unitOwner, int row, int col) {
        super(type, unitOwner, row, col);
    }

    @Override
    public int getMoveRange() { return 6; }

}
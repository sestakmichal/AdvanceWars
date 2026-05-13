package ija.ija2025.project.model.Map;

public enum TerrainType {
    Plain,
    Forest,
    Mountain,
    Water,
    City,
    Factory,
    HQ;

    public static TerrainType fromString(String terrain) {
        return switch (terrain.toUpperCase()) {
            case "P" -> Plain;
            case "F" -> Forest;
            case "M" -> Mountain;
            case "W" -> Water;
            case "C" -> City;
            case "FCT" -> Factory;
            case "HQ" -> HQ;
            default -> throw new IllegalStateException("Unexpected value: " + terrain);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case Plain -> "P";
            case Forest -> "F";
            case Mountain -> "M";
            case Water -> "W";
            case City -> "C";
            case Factory -> "FCT";
            case HQ -> "HQ";
        };
    }

    public int defenseBonus() {
        return switch (this) {
            case Plain -> 1;
            case Forest -> 2;
            case Mountain -> 4;
            case City, Factory -> 3;
            case HQ -> 4;
            case Water -> 0;
        };
    }

    public boolean isBuilding() {
        return switch (this) {
            case City, Factory, HQ -> true;
            default -> false;
        };
    }
}
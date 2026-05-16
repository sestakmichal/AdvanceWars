package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Map.Position;

public class UnitHPEntry {
    public Position position;
    public int hp;

    public UnitHPEntry() {}
    public UnitHPEntry(Position position, int hp) {
        this.position = position;
        this.hp = hp;
    }
}

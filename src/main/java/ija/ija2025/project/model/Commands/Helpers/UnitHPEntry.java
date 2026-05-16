/**
 * Authors: Michal Šesták
 * Description: Class describing entrz for a unit in the replay system.
 */

package ija.ija2025.project.model.Commands.Helpers;

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

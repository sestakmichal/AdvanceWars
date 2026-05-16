/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.Positions;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.UnitAbilities;

public record UnitPositionShownEvent(Position position, Unit unit, UnitAbilities abilities) implements Event {}
/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */
package ija.ija2025.project.model.Events.UnitActions;

import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Map.Position;

public record UnitHealthChangedEvent(Position pos, int newHealth) implements Event {}
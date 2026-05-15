/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.UnitActions;

import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

public record UnitMovedEvent(Unit unit, Position from, Position to) implements Event {}
///ActionObserver should react too and display options
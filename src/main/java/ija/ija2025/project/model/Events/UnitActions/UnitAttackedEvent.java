/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */
package ija.ija2025.project.model.Events.UnitActions;

import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Units.Unit;

public record UnitAttackedEvent(Unit attacker, Unit target, int damage) implements Event {}
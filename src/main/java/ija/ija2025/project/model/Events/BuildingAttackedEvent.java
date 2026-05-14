/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Units.Unit;

public record BuildingAttackedEvent(Unit unit, int damage) implements Event {}
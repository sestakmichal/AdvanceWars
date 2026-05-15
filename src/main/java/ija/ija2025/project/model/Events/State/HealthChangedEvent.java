/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.State;

import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Units.Unit;

public record HealthChangedEvent(Unit unit, int health) implements Event {}
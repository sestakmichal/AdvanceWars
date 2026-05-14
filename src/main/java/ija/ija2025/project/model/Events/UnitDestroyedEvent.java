/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Units.Unit;

public record UnitDestroyedEvent(Unit target) implements Event {}
///ActionObserver should react too and display options
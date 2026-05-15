/**
 * Authors: Michal Šesták
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.UnitActions;

import ija.ija2025.project.model.Events.Event;

public record HqCapturedEvent(String whichPlayerHQ) implements Event {}

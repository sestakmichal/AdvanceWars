/**
 * Authors: Michal Šesták
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events;

public record HqCapturedEvent(String whichPlayerHQ) implements Event {}

/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.State;

import ija.ija2025.project.model.Events.Event;

public record TurnChangedEvent(String player) implements Event {}
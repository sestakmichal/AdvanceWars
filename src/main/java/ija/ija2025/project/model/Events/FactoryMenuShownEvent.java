/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Map.Position;

public record FactoryMenuShownEvent(Position position) implements Event {}

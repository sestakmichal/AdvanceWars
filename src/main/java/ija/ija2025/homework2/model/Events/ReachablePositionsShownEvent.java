package ija.ija2025.homework2.model.Events;
import ija.ija2025.homework2.model.Map.Position;

import java.util.List;

public record ReachablePositionsShownEvent(List<Position> positions) implements Event {}
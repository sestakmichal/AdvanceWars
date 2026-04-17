package ija.ija2025.homework2.game.Events;
import ija.ija2025.homework2.common.Position;

import java.util.List;

public record ReachablePositionsShownEvent(List<Position> positions) implements Event {}
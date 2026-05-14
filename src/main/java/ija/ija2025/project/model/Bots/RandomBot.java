package ija.ija2025.project.model.Bots;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomBot implements GameBot {
    private String botPlayer; // např. "Player2"
    private Random random = new Random();

    public RandomBot(String botPlayer) {
        this.botPlayer = botPlayer;
    }

    @Override
    public void executeTurn(Game game) {
        // 1. Získání všech jednotek bota
        List<Unit> myUnits = game.getUnits().stream()
                .filter(u -> u.isOwnedBy(botPlayer))
                .toList();

        for (Unit unit : myUnits) {
            processUnitAction(game, unit);
        }

        // 2. Náhodný nákup v továrnách
        processBuying(game);

        // 3. Ukončení tahu
        game.nextTurn();
    }

    private void processUnitAction(Game game, Unit unit) {
        Position currentPos = unit.getPosition();

        // move
        List<Position> movable = game.getMovablePositions(currentPos);
        if (!movable.isEmpty()) {
            Position targetMove = movable.get(random.nextInt(movable.size()));
            game.moveUnit(currentPos, targetMove);
            currentPos = targetMove;
            System.out.println("AI moved to " + currentPos);
        }

        // capture
        if (game.canUnitCaptureAt(currentPos)) {
            game.captureBuilding(unit);
            System.out.println("AI caputed at " + currentPos);
        }
        // attack
        else {
            List<Position> attackable = game.getAttackablePositions(currentPos);
            if (!attackable.isEmpty()) {
                Position targetAttackPos = attackable.get(random.nextInt(attackable.size()));
                Unit targetUnit = game.getUnitAt(targetAttackPos);
                game.attackUnit(unit, targetUnit);
                System.out.println("AI attacked target at " + targetAttackPos);
            }
            // wait
            else {
                game.waitUnit(unit);
            }
        }
    }

    private void processBuying(Game game) {
        Map<String, Integer> prices = Map.of(
                "Infantry", 1000,
                "Artillery", 6000,
                "Tank", 7000
        );

        for (Position pos : game.getBuildingsMap().keySet()) {
            if (game.isOwnedFactory(pos) && game.getUnitAt(pos) == null) {
                // Najdeme všechny typy jednotek, na které má bot peníze
                int money = game.getPlayerMoney(botPlayer);
                List<String> affordableUnits = prices.entrySet().stream()
                        .filter(e -> e.getValue() <= money)
                        .map(Map.Entry::getKey)
                        .toList();

                if (!affordableUnits.isEmpty()) {
                    String selectedUnit = affordableUnits.get(random.nextInt(affordableUnits.size()));
                    int cost = prices.get(selectedUnit);

                    game.spawnUnit(selectedUnit, botPlayer, pos.getRow(), pos.getCol());
                    game.spendMoney(botPlayer, money - cost);

                    System.out.println("AI bought " + selectedUnit + " for " + cost);
                }
            }
        }
    }
}

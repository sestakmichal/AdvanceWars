/**
 * Authors: Michal Šesták
 * Description: Class for the auto bot functionality as the opponent.
 */

package ija.ija2025.project.controller.Bots;

import ija.ija2025.project.controller.CommandManager;
import ija.ija2025.project.model.Commands.*;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.UnitPrices;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomBot implements GameBot {
    private CommandManager commandManager;
    private String botPlayer;
    private Random random = new Random();

    public RandomBot(String botPlayer,  CommandManager commandManager) {
        this.botPlayer = botPlayer;
        this.commandManager =  commandManager;
    }

    public void initiateTurn(Game game, Runnable onTurnEnd) {
        new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                executeTurn(game);
                onTurnEnd.run(); // controller decides what happens next
            });
        }).start();
    }


    @Override
    public void executeTurn(Game game) {
        List<Unit> myUnits = game.getUnits().stream()
                .filter(u -> u.isOwnedBy(botPlayer))
                .toList();

        processBuying(game);

        for (Unit unit : myUnits) {
            processUnitAction(game, unit);
        }

        game.hidePositions();
    }

    private void processUnitAction(Game game, Unit unit) {
        Position currentPos = unit.getPosition();

        // move
        List<Position> movable = game.getMovablePositions(currentPos);
        if (!movable.isEmpty()) {
            Position targetMove = movable.get(random.nextInt(movable.size()));
            commandManager.execute(new MoveCommand(game, currentPos, targetMove));
            currentPos = targetMove;
            System.out.println("AI moved to " + currentPos);
        }

        // capture
        if (game.canUnitCaptureAt(currentPos)) {
            commandManager.execute(new CaptureCommand(game, unit));
            System.out.println("AI caputed at " + currentPos);
        }
        // attack
        else {
            List<Position> attackable = game.getAttackablePositions(currentPos);
            if (!attackable.isEmpty()) {
                Position targetAttackPos = attackable.get(random.nextInt(attackable.size()));
                Unit targetUnit = game.getUnitAt(targetAttackPos);
                commandManager.execute(new AttackCommand(game, unit, targetUnit));
                System.out.println("AI attacked target at " + targetAttackPos);
            }
            // wait
            else {
                commandManager.execute(new WaitCommand(game, unit));
            }
        }
    }

    private void processBuying(Game game) {
        Map<String, Integer> prices = Map.of(
                "Infantry", UnitPrices.Infantry,
                "Artillery", UnitPrices.Artillery,
                "Tank", UnitPrices.Tank
        );

        for (Position pos : game.getBuildingsMap().keySet()) {
            if (game.isOwnedFactory(pos) && game.getUnitAt(pos) == null) {
                int money = game.getPlayerMoney(botPlayer);
                List<String> affordableUnits = prices.entrySet().stream()
                        .filter(e -> e.getValue() <= money)
                        .map(Map.Entry::getKey)
                        .toList();

                if (!affordableUnits.isEmpty()) {
                    String selectedUnit = affordableUnits.get(random.nextInt(affordableUnits.size()));
                    int cost = prices.get(selectedUnit);

                    commandManager.execute(new BuyUnitCommand(game, selectedUnit, pos, cost));

                    System.out.println("AI bought " + selectedUnit + " for " + cost);
                }
            }
        }
    }
}
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RandomBot implements GameBot {
    private CommandManager commandManager;
    private String botPlayer;
    private Random random = new Random();
    private static final int DELAY_MS = 500;

    public RandomBot(String botPlayer,  CommandManager commandManager) {
        this.botPlayer = botPlayer;
        this.commandManager =  commandManager;
    }

    public void initiateTurn(Game game, Runnable onTurnEnd) {
        if (game.isFinished()) return;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        List<Unit> myUnits = game.getUnits().stream()
                .filter(u -> u.isOwnedBy(botPlayer))
                .toList();

        // limit units to around 20
        if (myUnits.size() < 20) {
            scheduler.schedule(()-> Platform.runLater(()-> processBuying(game)), DELAY_MS, TimeUnit.MILLISECONDS);
        }

        int delay = DELAY_MS * 2;
        for (Unit unit : myUnits) {
            final int d = delay;
            scheduler.schedule(() -> Platform.runLater(() -> {
                if (!game.isFinished() && !unit.isDestroyed() && unit.canAct()) {
                    processUnitAction(game, unit);
                }
            }), d, TimeUnit.MILLISECONDS);
            delay += DELAY_MS;
        }

        final int endDelay = delay;
        scheduler.schedule(() -> Platform.runLater(() -> {
            game.hidePositions();
            onTurnEnd.run();
            scheduler.shutdown();
        }), endDelay, TimeUnit.MILLISECONDS);
    }


    @Override
    public void executeTurn(Game game) {
        processBuying(game);
        List<Unit> myUnits = game.getUnits().stream()
                .filter(u -> u.isOwnedBy(botPlayer))
                .toList();

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
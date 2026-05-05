package ija.ija2025.project.model.Game.Services;

import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecoveryService {

    public static void applyMoneyRecovery(Game game) {
        String player = game.getCurrentPlayer();
        int income = calculateIncome(game, player);

        int newBalance = game.getPlayerMoney(player) + income;
        game.setPlayerMoney(player, newBalance);
    }

    public static void applyHealthRecovery(Game game) {
        String player = game.getCurrentPlayer();
        int currentMoney = game.getPlayerMoney(player);
        List<Unit> candidates = getRepairCandidates(game, player);

        for (Unit unit : candidates) {
            int hpToRecover = Math.min(20, 100 - unit.getHealth());
            int cost = calculateRepairCost(unit, hpToRecover);

            if (currentMoney >= cost) {
                currentMoney -= cost;
                unit.receiveRecovery(hpToRecover);
            }
        }
        game.setPlayerMoney(player, currentMoney);
    }


    private static int calculateIncome(Game game, String player) {
        int income = 0;

        for (Building building : game.getBuildingsMap().values()) {
            if (building.isOwnedBy(player)) {
                income += building.getIncome();
            }
        }

        return income;

    }

    private static int calculateRepairCost(Unit unit, int hpGained) {
        int steps = (int) Math.ceil(hpGained / 10.0);
        return (unit.getPrice() / 10) * steps;
    }

    private static List<Unit> getRepairCandidates(Game game, String player) {
        List<Unit> candidates = new ArrayList<>();
        for (Unit unit : game.getUnits()) {
            if (unit.isOwnedBy(player)) {
                if (game.isOwnedBuilding(unit.getPosition()) && unit.getHealth() < 100) {
                    candidates.add(unit);
                }
            }
        }
        candidates.sort(Comparator.comparingInt(Unit::getHealth));
        return candidates;
    }
}
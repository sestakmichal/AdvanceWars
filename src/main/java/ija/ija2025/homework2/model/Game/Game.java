package ija.ija2025.homework2.model.Game;
import ija.ija2025.homework2.model.Events.Event;

import ija.ija2025.homework2.model.Events.*;
import ija.ija2025.homework2.model.Map.Position;
import ija.ija2025.homework2.model.Map.TerrainType;
import ija.ija2025.homework2.model.Units.Artillery;
import ija.ija2025.homework2.model.Units.Infantry;
import ija.ija2025.homework2.model.Units.Tank;
import ija.ija2025.homework2.model.Units.Unit;
import ija.ija2025.homework2.model.Game.Services.PathFindingService;
import ija.ija2025.homework2.model.Game.Services.RecoveryService;
import ija.ija2025.homework2.model.Observation.Observer;
import ija.ija2025.homework2.model.Observation.Observable;

import java.util.*;

public class Game implements Observable {
    private List<Observer> gameObservers = new ArrayList<>();

    private Position mapSize;
    private TerrainType[][] map;
    private List<Unit> units = new ArrayList<>();
    private HashMap<Position, Unit> unitsMap = new HashMap<>();

    private String currentPlayer = "Player1";
    private Map<String, Integer> playerMoney = new HashMap<>();
    private Map<Position, String> buildingOwners = new HashMap<>();


    public Game() {
        playerMoney.put("Player1", 0);
        playerMoney.put("Player2", 0);
    }

    public Position getSize() { return mapSize; }
    public TerrainType[][] getMap() {return map;}

    public String getCurrentPlayer() {return currentPlayer;}
    public TerrainType getTerrainAt(Position pos) {return map[pos.getRow()][pos.getCol()];}
    public List<Unit> getUnits() {return units;}
    public Unit getUnitAt(Position pos) {return unitsMap.get(pos);}
    public String getTileOwner(Position pos) {return buildingOwners.getOrDefault(pos, "Neutral");}
    public int getPlayerMoney(String player) {return playerMoney.getOrDefault(player, 0);}
    public void setPlayerMoney(String player, int amount) {playerMoney.put(player, amount);}
    public Map<Position, String> getBuildingOwners() {return buildingOwners;}

    public void updateMap(TerrainType[][] newMap) {
        map = newMap;
        mapSize = new Position(map.length, map[0].length);
    }

    public Unit spawnUnit(String type, String owner, int row, int col) {
        Unit unit = switch (type) {
            case "Tank" -> new Tank(owner, row, col);
            case "Artillery" -> new Artillery(owner, row, col);
            case "Infantry" -> new Infantry(owner, row, col);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        Position pos = new Position(row, col);
        units.add(unit);
        unitsMap.put(pos, unit);
        notifyObservers(new UnitSpawnedEvent(unit, pos));
        return unit;
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
        unitsMap.remove(unit.getPosition());
        /// Notify unit removal here
    }


    public void moveUnit(Position startPos, Position endPos) {
        Unit movingUnit = getUnitAt(startPos);
        if (movingUnit == null) return;

        List<Position> reachable = PathFindingService.getReachablePositions(map, movingUnit, startPos);

        if (reachable.contains(endPos)) {
            unitsMap.remove(movingUnit.getPosition());
            movingUnit.setPosition(endPos);
            unitsMap.put(endPos, movingUnit);
            movingUnit.setMoved();
            notifyObservers(new UnitMovedEvent(movingUnit, startPos, endPos));
        }
    }

    public boolean canUnitReach(Position startPos, Position endPos) {
        Unit movingUnit = getUnitAt(startPos);
        if (movingUnit == null) return false;
        List<Position> reachable = PathFindingService.getReachablePositions(map, movingUnit, startPos);
        return reachable.contains(endPos);
    }

    public void nextTurn() {
        currentPlayer = currentPlayer.equals("Player1") ? "Player2" : "Player1";
        for (Unit unit : units) {
            if (unit.isOwnedBy(currentPlayer)) {
                unit.restoreState();
            }
        }
        applyRecoveryPhase();
    }


    private void applyRecoveryPhase() {
        RecoveryService.applyMoneyRecovery(this);
        /// Notify money applied
        RecoveryService.applyHealthRecovery(this);
        /// Notify  apply health
        /// NOTIFY Phase change
    }



    public boolean isOwnedBuilding(Position pos) {
        TerrainType type = getTerrainAt(pos);
        return type.isBuilding() && getTileOwner(pos).equals(currentPlayer);
    }


    /// UNIT states
    public boolean canUnitMove(Position pos) {
        Unit unit = getUnitAt(pos);
        return unit != null && unit.isOwnedBy(currentPlayer) && unit.canMove();
    }

    public boolean canUnitAct(Position pos) {
        Unit unit = getUnitAt(pos);
        return unit != null && unit.isOwnedBy(currentPlayer) && unit.canAct();
    }



    /// POSITION displays
    public void showReachablePositions(Position pos) {
        List<Position> reachable = PathFindingService.getReachablePositions(map, getUnitAt(pos), pos);
        notifyObservers(new ReachablePositionsShownEvent(reachable));
    }

    public void selectPosition(Position pos) {
        notifyObservers(new PositionSelectedEvent(pos));
    }

    public void hidePositions() {
        notifyObservers(new PositionsHiddenEvent());
    }


    public void addObserver(Observer gameObserver) {
        this.gameObservers.add(gameObserver);
    }
    public void removeObserver(Observer gameObserver) { this.gameObservers.remove(gameObserver); }
    private void notifyObservers(Event event) {
        for (Observer observer: gameObservers) {
            observer.update(event);
        }
    }
}
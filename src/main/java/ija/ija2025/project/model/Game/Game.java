package ija.ija2025.project.model.Game;
import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Events.Event;

import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;
import ija.ija2025.project.model.Units.Artillery;
import ija.ija2025.project.model.Units.Infantry;
import ija.ija2025.project.model.Units.Tank;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Services.PathFindingService;
import ija.ija2025.project.model.Game.Services.RecoveryService;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Observation.Observable;

import java.util.*;

public class Game implements Observable {
    private List<Observer> gameObservers = new ArrayList<>();

    private Position mapSize;
    private TerrainType[][] map;
    private HashMap<Position, Unit> unitsMap = new HashMap<>();

    private String currentPlayer = "Player1";
    private Map<String, Integer> playerMoney = new HashMap<>();

    private Map<Position, Building> buildings = new HashMap<>();


    public Game() {
        playerMoney.put("Player1", 0);
        playerMoney.put("Player2", 0);
    }

    public Position getSize() { return mapSize; }
    public TerrainType[][] getMap() {return map;}

    public String getCurrentPlayer() {return currentPlayer;}
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    public TerrainType getTerrainAt(Position pos) {return map[pos.getRow()][pos.getCol()];}
    public Collection<Unit> getUnits() {return unitsMap.values();}

    public Unit getUnitAt(Position pos) {return unitsMap.get(pos);}
    public String getTileOwner(Position pos) {return buildings.get(pos).getOwner();}
    public int getPlayerMoney(String player) {return playerMoney.getOrDefault(player, 0);}
    public void setPlayerMoney(String player, int amount) {playerMoney.put(player, amount);}

    public Map<Position, Building> getBuildings() { return buildings; }

    public void updateMap(TerrainType[][] newMap) {
        map = newMap;
        mapSize = new Position(map.length, map[0].length);
    }

    public void notifyInitialState() {
        for (Unit u : unitsMap.values()) {
            notifyObservers(new UnitSpawnedEvent(u, u.getPosition()));
        }
    }

    public Building spawnBuilding(String type, String owner, int row, int col) {

        Building building = switch (type) {
            case "HQ" -> new Building("HQ", owner, row, col);
            case "C" -> new Building("C", owner, row, col);
            case "FCT" -> new Building("FCT", owner, row, col);
            default -> throw new IllegalStateException("Unexpected building type: " + type);
        };

        Position pos = new Position(row, col);
        buildings.put(pos, building);
        // notify building spawned? can it happen even?
        return building;
    }

    public Unit spawnUnit(String type, String owner, int row, int col) {
        Unit unit = switch (type) {
            case "Tank" -> new Tank(owner, row, col);
            case "Artillery" -> new Artillery(owner, row, col);
            case "Infantry" -> new Infantry(owner, row, col);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        Position pos = new Position(row, col);
        unitsMap.put(pos, unit);
        notifyObservers(new UnitSpawnedEvent(unit, pos));
        return unit;
    }

    public void removeUnit(Unit unit) {
        unitsMap.remove(unit.getPosition());
        /// Notify unit removal here
    }


    public void moveUnit(Position startPos, Position endPos) {
        Unit unit = getUnitAt(startPos);
        if (unit == null) return;

        List<Position> movable = getMovablePositions(startPos);

        if (movable.contains(endPos)) {
            unitsMap.remove(unit.getPosition());
            unit.setPosition(endPos);
            unitsMap.put(endPos, unit);
            unit.setMoved();
            notifyObservers(new UnitMovedEvent(unit, startPos, endPos));
        }
    }

    public void waitUnit(Position pos) {
        Unit unit = getUnitAt(pos);
        if (unit == null) return;
        hidePositions();
        unit.setActed();
    }

    public boolean canUnitMoveTo(Position startPos, Position endPos) {
        Unit movingUnit = getUnitAt(startPos);
        if (movingUnit == null) return false;
        List<Position> reachable = getMovablePositions(startPos);
        return reachable.contains(endPos);
    }

    public void nextTurn() {
        currentPlayer = currentPlayer.equals("Player1") ? "Player2" : "Player1";
        for (Unit unit : unitsMap.values()) {
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
    public void showMovablePositions(Position pos) {
        List<Position> reachable = getMovablePositions(pos);
        notifyObservers(new MovablePositionsShownEvent(reachable));
    }

    public void selectUnitPosition(Position pos) {

        notifyObservers(new UnitPositionSelectedEvent(pos, getUnitAt(pos)));
    }

    public void hidePositions() {
        notifyObservers(new PositionsHiddenEvent());
    }

    public void hideMenu() {
        notifyObservers(new MenuHiddenEvent());
    }


    public List<Position> getMovablePositions(Position startPos) {
        Unit unit = getUnitAt(startPos);
        if (unit == null) return new ArrayList<>();

        List<Position> reachablePositions = PathFindingService.getReachablePositions(map, unit, startPos);
        List<Position> result = new ArrayList<>();

        for (Position pos : reachablePositions) {
            Unit occupant = getUnitAt(pos);

            if (occupant == null || pos.equals(startPos)) {
                result.add(pos);
            }
        }

        return result;
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
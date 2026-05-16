/**
 * Authors: Matej Čapek, Michal Šesták
 * Description: Class for executing main game logic and retrieving the games state.
 */

package ija.ija2025.project.model.Game;
import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Commands.Results.*;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Events.Positions.*;
import ija.ija2025.project.model.Events.State.*;
import ija.ija2025.project.model.Events.UnitActions.*;
import ija.ija2025.project.model.Units.*;
import ija.ija2025.project.model.Map.*;
import ija.ija2025.project.model.Game.Services.*;
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

    private Map<Position, Building> buildingsMap = new HashMap<>();

    public Position getSize() { return mapSize; }
    public TerrainType[][] getMap() {return map;}

    public String getCurrentPlayer() {return currentPlayer;}
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    public TerrainType getTerrainAt(Position pos) {return map[pos.getRow()][pos.getCol()];}
    public Collection<Unit> getUnits() {return unitsMap.values();}

    public Unit getUnitAt(Position pos) {return unitsMap.get(pos);}
    public Building getBuildingAt(Position pos) {return buildingsMap.get(pos);}
    public String getTileOwner(Position pos) {return buildingsMap.get(pos).getOwner();}
    public int getPlayerMoney(String player) {return playerMoney.getOrDefault(player, 0);}
    public void setPlayerMoney(String player, int amount) { playerMoney.put(player, amount); }
    public Map<Position, Building> getBuildingsMap() { return buildingsMap; }

    public void updateMap(TerrainType[][] newMap) {
        map = newMap;
        mapSize = new Position(map.length, map[0].length);
    }

    public void notifyInitialState() {
        for (Unit u : unitsMap.values()) {
            notifyObservers(new UnitSpawnedEvent(u, u.getPosition()));
        }
        notifyObservers(new MoneyChangedEvent("Player1", getPlayerMoney("Player1")));
        notifyObservers(new MoneyChangedEvent("Player2", getPlayerMoney("Player2")));
        notifyObservers(new TurnChangedEvent(currentPlayer));
    }

    public Building spawnBuilding(TerrainType type, String owner, int row, int col) {
        Building building = switch (type) {
            case HQ -> new Building(TerrainType.HQ, owner, row, col);
            case City -> new Building(TerrainType.City, owner, row, col);
            case Factory -> new Building(TerrainType.Factory, owner, row, col);
            default -> throw new IllegalStateException("Unexpected building type: " + type);
        };

        Position pos = new Position(row, col);
        buildingsMap.put(pos, building);
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

    public void destroyUnit(Unit unit) {
        unitsMap.remove(unit.getPosition());
        notifyObservers(new UnitDestroyedEvent(unit.getPosition()));
    }

    /// INTERNAL ACTIONS
    private void applyMoveInternal(Position start, Position end) {
        Unit unit = getUnitAt(start);
        unitsMap.remove(start);
        unit.setPosition(end);
        unitsMap.put(end, unit);
    }

    private void applyAttackInternal(Unit attacker, Unit target) {
        TerrainType terrain = getTerrainAt(target.getPosition());
        target.takeDamage(attacker, terrain);
        if (target.isDestroyed()) {
            destroyUnit(target);
        }
    }

    /// UNIT ACTIONS
    public void moveUnit(Position start, Position end) {
        Unit unit = getUnitAt(start);
        if (unit == null) return;
        if (!getMovablePositions(start).contains(end)) return;

        applyMoveInternal(start, end);
        unit.setMoved();
        notifyObservers(new UnitMovedEvent(unit, start, end));
    }

    public AttackResult attackUnit(Unit attacker, Unit target) {
        if (attacker == null || target == null) return null;
        if (!getAttackablePositions(attacker.getPosition()).contains(target.getPosition())) return null;

        int attackerHpBefore = attacker.getHealth();
        int targetHpBefore = target.getHealth();

        applyAttackInternal(attacker, target);
        attacker.setActed();

        boolean destroyed = target.isDestroyed();
        if (!destroyed) {
            notifyObservers(new UnitHealthChangedEvent(target.getPosition(), target.getHealth()));
        }
        boolean attackerDestroyed = false;
        if (!destroyed) {
            List<Position> defenderReach = PathFindingService.getAttackablePositions(map, target, target.getPosition());
            if (defenderReach.contains(attacker.getPosition())) {
                applyAttackInternal(target, attacker);
                attackerDestroyed = attacker.isDestroyed();
                if (!attackerDestroyed) {
                    notifyObservers(new UnitHealthChangedEvent(attacker.getPosition(),  attacker.getHealth()));
                }
            }
        }

        return new AttackResult(targetHpBefore, attackerHpBefore, destroyed, attackerDestroyed);
    }

    public CaptureResult captureBuilding(Unit unit) {
        if (!(unit instanceof Infantry)) return null;
        Building building = getBuildingAt(unit.getPosition());
        if (building == null) return null;

        String previousOwner = building.getOwner();
        int previousHP = building.getHealth();
        boolean wasCaptured = false;
        int captureDamage = (int) Math.floor(unit.getHealth()*0.1);
        building.takeCaptureDamage(captureDamage, currentPlayer);
        unit.setActed();

        notifyObservers(new BuildingHealthChangedEvent(building.getPosition(), building.getHealth()));
        if (building.getOwner().equals(currentPlayer)) {
            wasCaptured = true;
            if (building.getType() == TerrainType.HQ) {
                System.out.println("Successfully captured HQ");
                notifyObservers(new HqCapturedEvent(building.getOwner()));
            } else {
                System.out.println("Successfully captured a building");
                notifyObservers(new BuildingCapturedEvent(building.getPosition(), building.getOwner()));
            }
        }
        return new CaptureResult(previousOwner, previousHP, wasCaptured);
    }

    public void waitUnit(Unit unit) {
        if (unit == null) return;
        unit.setActed();
    }

    public void buyUnit(String type, Position pos, int cost) {
        int newMoney = getPlayerMoney(currentPlayer) - cost;
        spawnUnit(type, currentPlayer, pos.getRow(), pos.getCol());
        setPlayerMoney(currentPlayer, newMoney);
        notifyObservers(new MoneyChangedEvent(currentPlayer, newMoney));
    }

    /// REVERSE
    public void reverseMoveUnit(Position start, Position end) {
        Unit unit = getUnitAt(end);
        if (unit == null) return;
        applyMoveInternal(end, start);
        unit.resetMoved();
        notifyObservers(new UnitMovedEvent(unit, end, start));
    }

    public void reverseAttackUnit(Unit attacker, Unit target, int previousTargetHP, int previousAttackerHP, boolean destroyed, boolean attackerDestroyed) {
        Position targetPos = target.getPosition();
        Position attackerPos = attacker.getPosition();

        if (destroyed) {
            spawnUnit(target.getType(), target.getOwner(), targetPos.getRow(), targetPos.getCol());
        }
        if (attackerDestroyed) {
            spawnUnit(attacker.getType(), attacker.getOwner(), attackerPos.getRow(), attackerPos.getCol());
        }

        getUnitAt(targetPos).setHealth(previousTargetHP);
        getUnitAt(attackerPos).setHealth(previousAttackerHP);
        getUnitAt(attackerPos).resetActed();

        notifyObservers(new UnitHealthChangedEvent(targetPos, previousTargetHP));
        notifyObservers(new UnitHealthChangedEvent(attackerPos, previousAttackerHP));
    }

    public void reverseCaptureBuilding(Unit unit, String previousOwner, int previousCaptureHP, boolean wasCaptured) {
        if (unit == null) return;
        Building building = getBuildingAt(unit.getPosition());
        if (building == null) return;
        building.setHealth(previousCaptureHP);
        notifyObservers(new BuildingHealthChangedEvent(building.getPosition(), building.getHealth()));
        if (wasCaptured) {
            building.changeOwner(previousOwner);
            notifyObservers(new BuildingCapturedEvent(building.getPosition(), previousOwner));
        }
        unit.resetActed();
    }

    public void reverseWaitUnit(Unit unit) {
        if (unit == null) return;
        unit.resetActed();
    }

    public void reverseBuyUnit(Position pos, int cost) {
        String player = getCurrentPlayer();
        int newMoney = getPlayerMoney(player) + cost;
        Unit unit = getUnitAt(pos);
        if (unit == null) return;

        destroyUnit(unit);
        setPlayerMoney(player, newMoney);
        notifyObservers(new MoneyChangedEvent(player, newMoney));
    }

    /// UNIT POSSIBILITIES
    public boolean canUnitMoveTo(Position startPos, Position endPos) {
        Unit movingUnit = getUnitAt(startPos);
        if (movingUnit == null) return false;
        List<Position> reachable = getMovablePositions(startPos);
        return reachable.contains(endPos);
    }

    public boolean canUnitAttackAt(Position attackerPos, Position targetPos) {
        Unit attackingUnit = getUnitAt(attackerPos);
        if (attackingUnit == null) return false;
        List<Position> attackable = getAttackablePositions(attackerPos);
        return attackable.contains(targetPos);
    }

    public boolean canUnitCaptureAt(Position pos) {
        Unit capturingUnit = getUnitAt(pos);
        if (capturingUnit == null) return false;
        if (!(capturingUnit instanceof Infantry)) return false;
        Building building = getBuildingAt(pos);
        if (building == null) return false;
        return !building.getOwner().equals(currentPlayer);
    }


    /// PLAYER MANAGMENT
    public void nextTurn(String newPlayer) {
        currentPlayer = newPlayer;
        notifyObservers(new TurnChangedEvent(currentPlayer));

        for (Unit unit : unitsMap.values()) {
            if (unit.isOwnedBy(currentPlayer)) {
                unit.resetMovedActed();
            }
        }
        applyRecoveryPhase();
    }

    public void reverseTurn(String previousPlayer, int previousMoney, Map<Position, Integer> previousUnitHP) {
        setPlayerMoney(currentPlayer, previousMoney);

        for (Unit unit : unitsMap.values()) {
            if (unit.isOwnedBy(currentPlayer)) {
                Integer hp = previousUnitHP.get(unit.getPosition()); // fix here
                if (hp != null) {
                    unit.setHealth(hp);
                    notifyObservers(new UnitHealthChangedEvent(unit.getPosition(), hp));
                };
            }
        }
        notifyObservers(new MoneyChangedEvent(currentPlayer, getPlayerMoney(currentPlayer)));

        currentPlayer = previousPlayer;
        notifyObservers(new TurnChangedEvent(currentPlayer));
    }

    private void applyRecoveryPhase() {
        RecoveryService.applyMoneyRecovery(this);
        RecoveryService.applyHealthRecovery(this);
        notifyObservers(new MoneyChangedEvent(currentPlayer, getPlayerMoney(currentPlayer)));

        for (Unit unit : unitsMap.values()) {
            if (unit.isOwnedBy(currentPlayer)) {
                notifyObservers(new UnitHealthChangedEvent(unit.getPosition(), unit.getHealth()));
            }
        }
    }

    ///  MENU displays
    public void hideMenu() { notifyObservers(new MenuHiddenEvent()); }
    public void openFactoryMenu(Position pos) { notifyObservers(new FactoryMenuShownEvent(pos)); }


    ///  OWNERSHIP
    public boolean isOwnedFactory(Position pos) {
        Building building = getBuildingAt(pos);
        if (building == null) return false;
        String owner = building.getOwner();
        return building.getType().equals(TerrainType.Factory) && owner.equals(currentPlayer);
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

    public boolean unitsAreAvailableForCurrentPlayer() {
        for (Unit unit : getUnits()) {
            if (unit.isOwnedBy(currentPlayer) && unit.canAct()) return true;
        }
        return false;
    }


    /// POSITION displays
    public void showMovablePositions(Position pos) {
        List<Position> reachable = getMovablePositions(pos);
        notifyObservers(new MovablePositionsShownEvent(reachable));
    }

    public void showAttackablePositions(Position pos) {
        List<Position> attackable = getAttackablePositions(pos);
        notifyObservers(new AttackablePositionsShownEvent(attackable));
    }

    public void showUnitPosition(Position pos) { notifyObservers(new UnitPositionShownEvent(pos, getUnitAt(pos))); }

    public void hidePositions() {notifyObservers(new PositionsHiddenEvent());}


    ///  POSITIONS retrieval
    public List<Position> getMovablePositions(Position startPos) {
        Unit unit = getUnitAt(startPos);
        if (unit == null) return new ArrayList<>();

        List<Position> reachablePositions = PathFindingService.getReachablePositions(map, unitsMap, unit, startPos);
        List<Position> result = new ArrayList<>();

        for (Position pos : reachablePositions) {
            Unit occupant = getUnitAt(pos);

            if (occupant == null || pos.equals(startPos)) {
                result.add(pos);
            }
        }
        return result;
    }

    public List<Position> getAttackablePositions(Position startPos) {
        Unit unit = getUnitAt(startPos);
        if (unit == null) return new ArrayList<>();

        List<Position> attackablePositions = PathFindingService.getAttackablePositions(map, unit, startPos);
        List<Position> result = new ArrayList<>();

        for (Position pos : attackablePositions) {
            Unit occupant = getUnitAt(pos);

            if (occupant != null && !occupant.isOwnedBy(currentPlayer)) {
                result.add(pos);
            }
        }
        return result;
    }

    public void addObserver(Observer gameObserver) {this.gameObservers.add(gameObserver);}
    public void removeObserver(Observer gameObserver) { this.gameObservers.remove(gameObserver); }
    private void notifyObservers(Event event) {
        for (Observer observer: gameObservers) {
            observer.update(event);
        }
    }
}
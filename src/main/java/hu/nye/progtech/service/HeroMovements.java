package hu.nye.progtech.service;

import java.io.IOException;
import java.sql.SQLException;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.Object;
import hu.nye.progtech.model.ObjectType;

/**
 * Class that handles the movements and actions of the Hero in the game.
 */
public class HeroMovements {

    private int oldCoordinateX;
    private int oldCoordinateY;
    private Object object = new Object(ObjectType.WALL, 0, 0);
    private static int score = 0;

    /**
     * Retrieves the current score.
     *
     * @return The current score of the game.
     */
    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        HeroMovements.score = score;
    }

    public int getOldCoordinateX() {
        return oldCoordinateX;
    }

    public void setOldCoordinateX(int oldCoordinateX) {
        this.oldCoordinateX = oldCoordinateX;
    }

    public int getOldCoordinateY() {
        return oldCoordinateY;
    }

    public void setOldCoordinateY(int oldCoordinateY) {
        this.oldCoordinateY = oldCoordinateY;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Moves the hero in the specified direction and updates the game state accordingly.
     *
     * @param hero The Hero object representing the player.
     * @param map  The MapVO object representing the game map.
     * @return A new Hero object after the movement, or the original hero if the movement is invalid.
     */
    public Hero step(Hero hero, MapVO map) {
        int newCoordinateX = hero.getCoordinateX();
        int newCoordinateY = hero.getCoordinateY();

        oldCoordinateX = hero.getCoordinateX();
        oldCoordinateY = hero.getCoordinateY();

        if (hero.getViewingDirection() == Direction.North) {
            newCoordinateX = hero.getCoordinateX() - 1;
        } else if (hero.getViewingDirection() == Direction.South) {
            newCoordinateX = hero.getCoordinateX() + 1;

        } else if (hero.getViewingDirection() == Direction.West) {
            newCoordinateY = hero.getCoordinateY() - 1;
        } else {
            newCoordinateY = hero.getCoordinateY() + 1;
        }

        if (checkWalls(map, newCoordinateX, newCoordinateY)) {
            return new Hero(newCoordinateX, newCoordinateY, hero.getViewingDirection(), hero.getNumberOfArrows(), hero.isHaveGold());
        } else {
            System.out.println("\nErre a mezőre nem léphetsz!\n");
            return hero;
        }
    }

    /**
     * Updates the game map based on the hero's movement.
     *
     * @param map              The MapVO object representing the game map.
     * @param newCoordinateX The new x-coordinate of the hero.
     * @param newCoordinateY The new y-coordinate of the hero.
     * @param hero             The Hero object representing the player.
     * @return A new MapVO object after updating the map.
     */
    public MapVO drawStepOnMap(MapVO map, int newCoordinateX, int newCoordinateY, Hero hero) throws SQLException, IOException {
        char[][] newMap = map.getMap();

        checkWumpusGoldPit(map, newCoordinateX, newCoordinateY);
        newMap[oldCoordinateX - 1][oldCoordinateY - 1] = '_';

        if (object.getType() == ObjectType.GOLD && !hero.isHaveGold()) {
            newMap[object.getCoordinateX()][object.getCoordinateY()] = 'G';
        } else if (object.getType() == ObjectType.PIT) {
            newMap[object.getCoordinateX()][object.getCoordinateY()] = 'P';
            if (hero.getNumberOfArrows() > 0) {
                hero.setNumberOfArrows(hero.getNumberOfArrows() - 1);
            }
        }
        newMap[newCoordinateX - 1][newCoordinateY - 1] = 'H';

        return new MapVO(map.getRows(), map.getColumns(), newMap);
    }

    /**
     * Checks for Wumpus, Gold, or Pit at the hero's new position and updates the game state accordingly.
     *
     * @param map              The MapVO object representing the game map.
     * @param newCoordinateX The new x-coordinate of the hero.
     * @param newCoordinateY The new y-coordinate of the hero.
     */
    public void checkWumpusGoldPit(MapVO map, int newCoordinateX, int newCoordinateY) throws SQLException, IOException {
        if (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] == 'U') {
            System.out.println("\nWumpuszra léptél és meghaltál!\n");
            score = 0;
            new Menu();
        } else if (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] == 'P') {
            object = new Object(ObjectType.PIT, newCoordinateX - 1, newCoordinateY - 1);
            System.out.println("\nVeremre léptél! Elvesztettél egy nyilat!\n");
            score = score - 25;
        } else if (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] == 'G') {
            object = new Object(ObjectType.GOLD, newCoordinateX - 1, newCoordinateY - 1);
            System.out.println("\nArany mezőre léptél! Felszedheted az aranyat!\n");
        }
    }

    /**
     * Checks if the hero can move to the specified coordinates based on the game map.
     *
     * @param map              The MapVO object representing the game map.
     * @param newCoordinateX The new x-coordinate of the hero.
     * @param newCoordinateY The new y-coordinate of the hero.
     * @return True if the hero can move to the specified coordinates, false otherwise.
     */
    public boolean checkWalls(MapVO map, int newCoordinateX, int newCoordinateY) {
        return map.getMap()[newCoordinateX - 1][newCoordinateY - 1] != 'W';
    }

    /**
     * Turns the hero to the right based on the current viewing direction.
     *
     * @param hero The Hero object representing the player.
     */
    public void turnRight(Hero hero) {
        if (hero.getViewingDirection() == Direction.North) {
            hero.setViewingDirection(Direction.East);
        } else if (hero.getViewingDirection() == Direction.South) {
            hero.setViewingDirection(Direction.West);
        } else if (hero.getViewingDirection() == Direction.West) {
            hero.setViewingDirection(Direction.North);
        } else {
            hero.setViewingDirection(Direction.South);
        }
    }

    /**
     * Turns the hero to the left based on the current viewing direction.
     *
     * @param hero The Hero object representing the player.
     */
    public void turnLeft(Hero hero) {
        if (hero.getViewingDirection() == Direction.North) {
            hero.setViewingDirection(Direction.West);
        } else if (hero.getViewingDirection() == Direction.South) {
            hero.setViewingDirection(Direction.East);
        } else if (hero.getViewingDirection() == Direction.West) {
            hero.setViewingDirection(Direction.South);
        } else {
            hero.setViewingDirection(Direction.North);
        }
    }

    /**
     * Simulates shooting in the hero's current viewing direction and updates the game state accordingly.
     *
     * @param hero The Hero object representing the player.
     * @param map  The MapVO object representing the game map.
     * @return A new MapVO object after the shooting action.
     */
    public MapVO shoot(Hero hero, MapVO map) {
        int newCoordinateX = hero.getCoordinateX();
        int newCoordinateY = hero.getCoordinateY();


        while ((map.getMap()[newCoordinateX - 1][newCoordinateY - 1] != 'W') &&
                (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] != 'U')) {

            if (hero.getViewingDirection() == Direction.North) {
                newCoordinateX--;
            } else if (hero.getViewingDirection() == Direction.South) {
                newCoordinateX++;

            } else if (hero.getViewingDirection() == Direction.West) {
                newCoordinateY--;
            } else {
                newCoordinateY++;
            }
        }

        if (hero.getNumberOfArrows() > 0) {
            hero.setNumberOfArrows(hero.getNumberOfArrows() - 1);
        }

        if (checkShotField(map, newCoordinateX, newCoordinateY) == 'W') {
            return map;
        } else if (checkShotField(map, newCoordinateX, newCoordinateY) == 'U') {
            char[][] newMap = map.getMap();
            newMap[newCoordinateX - 1][newCoordinateY - 1] = '_';
            return new MapVO(map.getRows(), map.getColumns(), newMap);
        } else {
            return map;
        }
    }

    /**
     * Checks the field where the hero shot and updates the game state based on the result.
     *
     * @param map              The MapVO object representing the game map.
     * @param newCoordinateX The x-coordinate of the shot field.
     * @param newCoordinateY The y-coordinate of the shot field.
     * @return The character representing the content of the shot field ('W' for Wumpus, 'U' for empty).
     */
    public char checkShotField(MapVO map, int newCoordinateX, int newCoordinateY) {
        if (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] == 'U') {
            System.out.println("\nEltaláltad a wumpuszt! A wumpusz meghalt!\n");
            score = score + 25;
        } else if (map.getMap()[newCoordinateX - 1][newCoordinateY - 1] == 'W') {
            System.out.println("\nA lövés sikertelen! A nyíl nem talált!\n");
            score = score - 25;
        }

        return map.getMap()[newCoordinateX - 1][newCoordinateY - 1];
    }

    /**
     * Checks if the hero is on a gold field and picks up the gold if present.
     *
     * @param hero The Hero object representing the player.
     * @param map  The MapVO object representing the game map.
     */
    public void pickupGold(Hero hero, MapVO map) {
        if (map.getMap()[hero.getCoordinateX() - 1][hero.getCoordinateY() - 1] ==
                map.getMap()[object.getCoordinateX()][object.getCoordinateY()]) {
            System.out.println("\nArany felvéve!\n");
            hero.setHaveGold(true);
            score = score + 50;
        } else {
            System.out.println("\nNem arany mezőn vagy!\n");
        }
    }
}

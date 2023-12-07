package hu.nye.progtech.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.Object;
import hu.nye.progtech.model.ObjectType;

/**
 * The EditMap class provides functionality to edit and customize a game map.
 * <p>
 * It allows users to set the size of the map, add and remove various elements such as walls,
 * pits, gold, heroes, and wumpuses, and save the edited map to the database. The class ensures
 * that the edited map follows certain rules, such as having a single hero, a specified number
 * of wumpuses based on the map size, and only one gold element.
 * <p>
 * The class uses a menu system to interact with the user, where they can choose different
 * options like adding or removing elements and saving the map.
 */
public class EditMap {

    private static Hero editedHero;
    private static MapVO editedMap;

    public static Hero getEditedHero() {
        return editedHero;
    }

    public static void setEditedHero(Hero editedHero) {
        EditMap.editedHero = editedHero;
    }

    public EditMap() {}


    /**
     * Reads and validates the size of the game map from user input.
     * <p>
     * This method prompts the user to enter the size of the game map within the range of 6 to 20 (inclusive).
     * It continues to ask for input until a valid size is provided. Once a valid size is obtained,
     * it creates an empty map of the specified size, prints it, and proceeds to the map editing menu.
     *
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public void readSize() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        int size = 0;
        while (!sizeCheck(size)) {
            System.out.println("\nAdja meg a pálya méretét (6<=N<=20): ");

            try {
                size = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("\nAdja meg a pálya méretét (6<=N<=20): ");
            }

            if (sizeCheck(size)) {
                MapVO empty = emptyMap(size);
                empty.mapPrint();
                menu(empty);
            } else {
                System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
    }

    /**
     * Displays the menu for editing a game map and handles user input.
     * <p>
     * This method presents a menu with options to add objects, remove objects, exit without saving,
     * or exit and save the edited map. It continuously prompts the user for input until the user chooses to exit.
     * Depending on the user's choice, it invokes corresponding methods to modify the map and updates the edited
     * hero's arrow count. If the user chooses to save the map, it connects to the database and stores the edited
     * hero and map information. After successful completion, it prints a success message, displays the edited hero,
     * and returns to the main menu.
     *
     * @param map The map to be edited.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public void menu(MapVO map) throws IOException, SQLException {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        while (choice != 3) {
            printMenu();

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                printMenu();
            }

            switch (choice) {
                case 1:
                    try {
                        editedMap = addObject(map, objectRead());
                    } catch (Exception e) {
                        System.out.println("\nHiba! Próbáld újra!\n");
                        editedMap = addObject(map, objectRead());
                    }
                    map.mapPrint();

                    if (editedHero == null) {
                        System.out.println("\nNincs hős a pályán!\n");
                    } else {
                        System.out.println(editedHero);
                    }
                    break;
                case 2:
                    try {
                        editedMap = removeObject(map);
                    } catch (Exception e) {
                        System.out.println("\nHiba! Próbáld újra!\n");
                        editedMap = removeObject(map);
                    }
                    map.mapPrint();
                    break;
                case 3:
                    System.out.println("\nSikeresen kilépett!\n");
                    new Menu();
                    break;
                case 4:
                    if (checkMap(map)) {
                        editedHero.setNumberOfArrows(wumpusCount(map));

                        DatabaseService database = new DatabaseService();
                        database.databaseConnection();
                        database.sendEditedHeroToDatabase(editedHero);
                        int heroID = database.getHeroIDFromDatabase(editedHero);
                        database.sendEditedMapToDatabase(editedMap, heroID);
                        database.closeDatabaseConnection();
                        System.out.println("\nSikeresen kilépett! Pálya sikeresen elmentve!\n" + editedHero);
                        new Menu();
                        choice = 3;
                    } else {
                        System.out.println("\nHiba! Pálya mentése nem sikerült!\n");
                    }
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
    }

    /**
     * Checks if the provided game map is valid for saving.
     * <p>
     * This method ensures that the map contains exactly one gold object, one hero, and the correct number
     * of wumpus objects based on the map size. It prints error messages for specific issues, such as missing
     * gold or hero, and an incorrect number of wumpus objects. The method returns true if the map is valid
     * and false otherwise.
     *
     * @param map The game map to be checked.
     * @return true if the map is valid; false otherwise.
     */
    public boolean checkMap(MapVO map) {
        if (goldCount(map) != 1) {
            System.out.println("\nHiba! Nincs arany a pályán!\n");
            return false;
        }

        if (editedHero == null) {
            System.out.println("\nHiba! Nincs hős a pályán!\n");
            return false;
        }

        if (wumpusCount(map) != wumpusBySize(map.getRows())) {
            System.out.println("\nHiba! Nincs " + wumpusBySize(map.getRows()) + " db wumpusz a pályán!\n");
            return false;
        }

        return true;
    }

    /**
     * Prints the menu options for editing the game map.
     * <p>
     * This method displays a menu with numbered options for adding and removing elements from the game map,
     * as well as options for exiting the editing process with or without saving changes.
     */
    public void printMenu() {
        System.out.println("\nVálasszon az alábbiak közül: \n" +
                "1. Elem hozzáadása\n" +
                "2. Elem eltávolítása\n" +
                "3. Kilépés pálya mentése nélkül\n" +
                "4. Kilépés pálya mentésével\n" +
                "Ön választása: ");
    }

    /**
     * Checks if the provided size is within a valid range for the game map.
     *
     * @param size The size to be checked.
     * @return {@code true} if the size is within the valid range (6 to 20, inclusive), {@code false} otherwise.
     */
    public boolean sizeCheck(int size) {
        return size >= 6 && size <= 20;
    }

    /**
     * Determines the number of Wumpus creatures based on the provided size of the game map.
     *
     * @param size The size of the game map.
     * @return The number of Wumpus creatures, which depends on the size of the map:
     *         - If size is 8 or less, returns 1.
     *         - If size is greater than 8 and 14 or less, returns 2.
     *         - If size is greater than 14, returns 3.
     */
    public int wumpusBySize(int size) {
        if (size <= 8) {
            return 1;
        } else if (size <= 14) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * Counts the number of Wumpus creatures on the provided game map.
     *
     * @param map The game map represented by a MapVO object.
     * @return The count of Wumpus creatures present on the map.
     */
    public int wumpusCount(MapVO map) {
        int cnt = 0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                if (map.getMap()[i][j] == 'U') {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Counts the number of gold items on the provided game map.
     *
     * @param map The game map represented by a MapVO object.
     * @return The count of gold items present on the map.
     */
    public int goldCount(MapVO map) {
        int cnt = 0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                if (map.getMap()[i][j] == 'G') {
                    cnt++;
                }
            }
        }
        return cnt;

    }

    /**
     * Counts the number of hero items on the provided game map.
     *
     * @param map The game map represented by a MapVO object.
     * @return The count of hero items present on the map.
     */
    public int heroCount(MapVO map) {
        int cnt = 0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                if (map.getMap()[i][j] == 'H') {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Generates an empty game map with specified size.
     *
     * @param size The size of the game map (both rows and columns).
     * @return A MapVO object representing an empty game map with walls on the border and empty spaces inside.
     */
    public MapVO emptyMap(int size) {
        char[][] newMap = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    newMap[i][j] = 'W';
                } else {
                    newMap[i][j] = '_';
                }
            }
        }
        return new MapVO(size, size, newMap);
    }

    /**
     * Reads and returns the initial direction of the hero from user input.
     *
     * @return The Direction enum representing the initial direction of the hero (North, South, West, East).
     */
    public Direction heroDirectionRead() {
        Scanner scanner = new Scanner(System.in);

        Direction direction = null;

        System.out.println("Adja meg a hős kezdeti nézési irányát(North, South, West, East): ");
        while (direction == null) {
            switch (scanner.nextLine()) {
                case "North":
                    direction = Direction.North;
                    break;
                case "South":
                    direction = Direction.South;
                    break;
                case "West":
                    direction = Direction.West;
                    break;
                case "East":
                    direction = Direction.East;
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }

        }
        return direction;
    }

    /**
     * Reads and returns an Object based on user input, including the element type, row number, and column letter.
     *
     * @return The Object containing the specified element type, row number, and column letter.
     */
    public static Object objectRead() {
        Scanner scanner = new Scanner(System.in);

        ObjectType type = null;

        System.out.println("Adja meg az elem típusát(WALL, PIT, WUMPUS, HERO, GOLD): ");
        while (type == null) {
            switch (scanner.nextLine()) {
                case "WALL":
                    type = ObjectType.WALL;
                    break;
                case "PIT":
                    type = ObjectType.PIT;
                    break;
                case "WUMPUS":
                    type = ObjectType.WUMPUS;
                    break;
                case "GOLD":
                    type = ObjectType.GOLD;
                    break;
                case "HERO":
                    type = ObjectType.HERO;
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }

        System.out.println("Adja meg a sor számát: ");

        int rowNumber = 0;
        try {
            rowNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            objectRead();
        }

        System.out.println("Adja meg az oszlop betűjelét: ");
        int columnNumber = (scanner.next().charAt(0) - 64);

        return new Object(type, rowNumber, columnNumber);
    }

    /**
     * Checks whether it is valid to add the specified Object to the given MapVO based on various conditions.
     *
     * @param map The MapVO object representing the map information.
     * @param object The Object to be added to the map.
     * @return True if the addition is valid, false otherwise.
     */
    public boolean checkAddObject(MapVO map, Object object) {
        if (object.getCoordinateY() > map.getRows() || object.getCoordinateX() > map.getColumns()) {
            System.out.println("\nHiba! A pálya " + map.getRows() + "x" + map.getRows() + " méretű!\n");
            return false;
        }

        if (object.getType() == ObjectType.GOLD && goldCount(map) == 1) {
            System.out.println("\nHiba! Aranyból csak 1 lehet a pályán!\n");
            return false;
        }

        if (object.getType() == ObjectType.HERO && heroCount(map) == 1) {
            System.out.println("\nHiba! Hősből csak 1 lehet a pályán!\n");
            return false;
        }

        if (object.getType() == ObjectType.WUMPUS && wumpusCount(map) == wumpusBySize(map.getRows())) {
            System.out.println("\nHiba! Wumpuszból csak " + wumpusBySize(map.getRows()) + " lehet a pályán!\n");
            return false;
        }

        if (object.getType() != ObjectType.HERO && map.getMap()[object.getCoordinateX() - 1][object.getCoordinateY() - 1] != '_') {
            System.out.println("\nHiba! Az adott helyen van már elem!\n");
            return false;
        }

        if (object.getType() == ObjectType.HERO && (map.getMap()[object.getCoordinateX() - 1][object.getCoordinateY() - 1] != '_' &&
                map.getMap()[object.getCoordinateX() - 1][object.getCoordinateY() - 1] != 'G')) {
            System.out.println("\nHiba! Hőst csak üres vagy arany mezőre rakhatsz!\n");
            return false;
        }

        return true;
    }

    /**
     * Adds the specified Object to the given MapVO if the addition is valid, based on the conditions
     * checked by the {@code checkAddObject} method. Modifies the map accordingly and updates the editedHero
     * information if the added object is a HERO.
     *
     * @param map The MapVO object representing the map information.
     * @param object The Object to be added to the map.
     * @return The modified MapVO after adding the object, or the original map if the addition is not valid.
     */
    public MapVO addObject(MapVO map, Object object) {
        if (checkAddObject(map, object)) {
            char[][] newMap = map.getMap();
                switch (object.getType()) {
                    case GOLD:
                        newMap[object.getCoordinateX() - 1][object.getCoordinateY() - 1] = 'G';
                        break;
                    case HERO:
                        Direction direction = heroDirectionRead();
                        int numberOfArrows = wumpusCount(map);
                        editedHero = new Hero(object.getCoordinateX(), object.getCoordinateY(), direction, numberOfArrows, false);
                        break;
                    case WALL:
                        newMap[object.getCoordinateX() - 1][object.getCoordinateY() - 1] = 'W';
                        break;
                    case PIT:
                        newMap[object.getCoordinateX() - 1][object.getCoordinateY() - 1] = 'P';
                        break;
                    case WUMPUS:
                        newMap[object.getCoordinateX() - 1][object.getCoordinateY() - 1] = 'U';
                        break;
                    default:
                        System.out.println("\nHiba! Próbálja újra!\n");
                }
            return new MapVO(map.getRows(), map.getColumns(), newMap);
        } else {
            return map;
        }
    }

    /**
     * Checks if removing an element at the specified row and column coordinates is valid, based on the
     * conditions specified in the method. Ensures that the removal does not affect the boundaries of the map.
     *
     * @param map The MapVO object representing the map information.
     * @param rowNumber The row number of the element to be removed.
     * @param columnNumber The column number of the element to be removed.
     * @return {@code true} if the removal is valid, {@code false} otherwise.
     */
    public static boolean checkRemoveObject(MapVO map, int rowNumber, int columnNumber) {
        if (rowNumber > map.getRows() || columnNumber > map.getColumns()) {
            System.out.println("\nHiba! A pálya " + map.getRows() + "x" + map.getRows() + " méretű!\n");
            return false;
        }

        if (editedHero != null) {
            if (rowNumber == editedHero.getCoordinateY() - 1 && columnNumber == editedHero.getCoordinateX() - 1) {
                editedHero = null;
            }
        }

        if (rowNumber == 1 || columnNumber == 1 || rowNumber == map.getRows() || columnNumber == map.getColumns()) {
            System.out.println("\nHiba! A keret nem távolítható el!\n");
            return false;
        }
        return true;
    }

    /**
     * Removes an element from the specified coordinates in the map, if the removal is valid. Checks the
     * validity of the removal using the {@code checkRemoveObject} method.
     *
     * @param map The MapVO object representing the map information.
     * @return A new MapVO object after the removal, or the original map if the removal is not valid.
     */
    public static MapVO removeObject(MapVO map) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adja meg az eltávolítandó elem sorszámát: ");

        int rowNumber = 0;
        try {
            rowNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            removeObject(map);
        }

        System.out.println("Adja meg az eltávolítandó elem oszlopának betűjelét: ");
        int columnNumber = (scanner.next().charAt(0) - 64);

        if (checkRemoveObject(map, rowNumber, columnNumber)) {
            char[][] newMap = map.getMap();
            newMap[rowNumber - 1][columnNumber - 1] = '_';

            return new MapVO(map.getRows(), map.getColumns(), newMap);
        } else {
            return map;
        }
    }
}

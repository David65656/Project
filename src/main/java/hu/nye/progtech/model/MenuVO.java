package hu.nye.progtech.model;

import static hu.nye.progtech.service.EditMap.objectRead;
import static hu.nye.progtech.service.EditMap.removeObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import hu.nye.progtech.service.DatabaseService;
import hu.nye.progtech.service.EditMap;
import hu.nye.progtech.service.HeroMovements;
import hu.nye.progtech.service.Play;

/**
 * The Menu class represents the main menu of the game, providing options for starting a new game, loading a saved game,
 * adding a new map, and exiting the application.
 */

public class MenuVO {

    private static String userName;
    private static final Scanner menuScanner = new Scanner(System.in);
    private static final DatabaseService database;
    private static final EditMap editMap = new EditMap();
    private static Play play;

    static {
        try {
            database = new DatabaseService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int mapID;

    /**
     * Gets the username.
     *
     * @return The username.
     */
    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        MenuVO.userName = userName;
    }

    /**
     * Gets the map ID.
     *
     * @return The map ID.
     */
    public static int getMapID() {
        return mapID;
    }

    public static void setMapID(int mapID) {
        MenuVO.mapID = mapID;
    }

    /**
     * Constructs a Menu object, reads the username, prints the menu, and reads the user's choice.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public MenuVO() throws IOException, SQLException {
        readUserName();
        printMainMenu();
        readChoice();
    }

    /**
     * Prints the main menu options.
     */
    public static void printMainMenu() {
        System.out.println("\n1. Új játék\n" +
                "2. Játék betöltése\n" +
                "3. Új pálya hozzáadása\n" +
                "4. Kilépés\n" +
                "Ön választása: ");
    }

    /**
     * Reads the user's name.
     */
    public static void readUserName() {
        System.out.println("\nAdja meg a felhasználónevét: ");
        userName = menuScanner.nextLine();
    }

    /**
     * Reads the user's choice and performs the corresponding action.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public static void readChoice() throws IOException, SQLException {
        int choice = 0;

        while (choice != 4) {
            try {
                choice = menuScanner.nextInt();
            } catch (InputMismatchException e) {
                printMainMenu();
                menuScanner.next();
            }

            switch (choice) {
                case 1:
                    database.databaseConnection();
                    database.readAllMapFromDatabaseWithBuilder();
                    database.deleteUnfinishedGame();

                    System.out.println("Ön által választott pálya: ");
                    try {
                        mapID = menuScanner.nextInt();
                    } catch (InputMismatchException e) {
                        menuScanner.next();
                        database.readAllMapFromDatabaseWithBuilder();
                    }

                    MapVO map = database.readMapFromDatabase(mapID);
                    Hero hero = database.readHeroFromDatabase(mapID);
                    database.closeDatabaseConnection();

                    try {
                        play = new Play(map, hero);
                        playMenu();
                    } catch (NullPointerException e) {
                        System.out.println("\nHiba próbáld újra!\n");
                        MenuVO.readUserName();
                    }
                    break;
                case 2:
                    database.databaseConnection();

                    MapVO loadMap = null;
                    Hero loadHero = null;
                    int startCoordinateX = 0;
                    int startCoordinateY = 0;
                    int score = 0;

                    try {
                        loadMap = database.loadSavedMapFromDatabase(userName);
                        loadHero = database.loadSavedHeroFromDatabase(userName);
                        startCoordinateX = database.loadSavedHeroStartCoordinateXFromDatabase(loadHero);
                        startCoordinateY = database.loadSavedHeroStartCoordinateYFromDatabase(loadHero);
                        score = database.loadUserSavedScoreFromDatabase();
                    } catch (NullPointerException e) {
                        System.out.println("\nNincs mentett pályád!\n");
                        System.exit(0);
                    }

                    database.deleteUnfinishedGame();

                    database.closeDatabaseConnection();

                    System.out.println("\nMentett pálya sikeresen betöltve!\n");

                    new Play(loadMap, loadHero, startCoordinateX, startCoordinateY, score);
                    playMenu();
                    break;
                case 3:
                    new EditMap();
                    readSize();
                    break;
                case 4:
                    System.out.println("\nSikeresen kilépett!\n");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
    }

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
    public static void readSize() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        int size = 0;
        while (!editMap.sizeCheck(size)) {
            System.out.println("\nAdja meg a pálya méretét (6<=N<=20): ");

            try {
                size = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("\nAdja meg a pálya méretét (6<=N<=20): ");
            }

            if (editMap.sizeCheck(size)) {
                MapVO empty = editMap.emptyMap(size);
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
    public static void menu(MapVO map) throws IOException, SQLException {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        while (choice != 3) {
            printEditMenu();

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
                printEditMenu();
            }

            switch (choice) {
                case 1:
                    try {
                        EditMap.setEditedMap(editMap.addObject(map, objectRead()));
                    } catch (Exception e) {
                        System.out.println("\nHiba! Próbáld újra!\n");
                        EditMap.setEditedMap(editMap.addObject(map, objectRead()));
                    }
                    map.mapPrint();

                    if (EditMap.getEditedHero() == null) {
                        System.out.println("\nNincs hős a pályán!\n");
                    } else {
                        System.out.println(EditMap.getEditedHero());
                    }
                    break;
                case 2:
                    try {
                        EditMap.setEditedMap(removeObject(map));
                    } catch (Exception e) {
                        System.out.println("\nHiba! Próbáld újra!\n");
                        EditMap.setEditedMap(removeObject(map));
                    }
                    map.mapPrint();
                    break;
                case 3:
                    System.out.println("\nSikeresen kilépett!\n");
                    System.exit(0);
                    break;
                case 4:
                    if (editMap.checkMap(map)) {
                        EditMap.getEditedHero().setNumberOfArrows(editMap.wumpusCount(map));

                        DatabaseService database = new DatabaseService();
                        database.databaseConnection();
                        database.sendEditedHeroToDatabase(EditMap.getEditedHero());
                        int heroID = database.getHeroIDFromDatabase(EditMap.getEditedHero());
                        database.sendEditedMapToDatabase(EditMap.getEditedMap(), heroID);
                        database.closeDatabaseConnection();
                        System.out.println("\nSikeresen kilépett! Pálya sikeresen elmentve!\n" + EditMap.getEditedHero());
                        System.exit(0);
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
     * Manages the main gameplay loop, allowing the player to perform various actions.
     *
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     */
    public static void playMenu() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        HeroMovements move = new HeroMovements();
        int choice = 0;
        int cnt = 0;

        while (choice != 7) {
            if (play.checkHeroPosition() && play.getPlayedHero().isHaveGold()) {
                System.out.println("\nGratulálok! Megnyerted a játékot! A játék során megtett lépések száma: " + cnt +
                        "\nPontszámod: " + HeroMovements.getScore() + "\n");
                DatabaseService database = new DatabaseService();
                database.databaseConnection();
                database.isPlayerInScoreTable();
                database.printScoreTable();
                database.closeDatabaseConnection();
                choice = 7;
            } else {
                play.getPlayedMap().mapPrint();
                System.out.println(play.getPlayedHero());
                printPlayMenu();

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    scanner.next();
                    play.getPlayedMap().mapPrint();
                    System.out.println(play.getPlayedHero());
                    printPlayMenu();
                }

            }
            switch (choice) {
                case 1:
                    play.setPlayedHero(move.step(play.getPlayedHero(), play.getPlayedMap()));
                    play.setPlayedMap(move.drawStepOnMap(play.getPlayedMap(),
                            play.getPlayedHero().getCoordinateX(),
                            play.getPlayedHero().getCoordinateY(),
                            play.getPlayedHero()));
                    cnt++;
                    break;
                case 2:
                    move.turnLeft(play.getPlayedHero());
                    break;
                case 3:
                    move.turnRight(play.getPlayedHero());
                    break;
                case 4:
                    if (play.getPlayedHero().getNumberOfArrows() == 0) {
                        System.out.println("\nNincs nyilad, ezért nem tudsz lőni!\n");
                    } else {
                        play.setPlayedMap(move.shoot(play.getPlayedHero(), play.getPlayedMap()));
                    }
                    break;
                case 5:
                    move.pickupGold(play.getPlayedHero(), play.getPlayedMap());
                    break;
                case 6:
                    DatabaseService database = new DatabaseService();
                    database.databaseConnection();
                    database.sendSavedGameToDatabase(play.getPlayedMap(), play.getPlayedHero());
                    database.sendPlayerScoreToDatabase();
                    System.out.println("\nSikeresen kilépett! Pálya sikeresen elmentve!\n");
                    database.closeDatabaseConnection();
                    System.exit(0);
                    break;
                case 7:
                    System.out.println("\nSikeresen kilépett!\n");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
    }

    /**
     * Prints the menu of available actions for the player.
     */
    public static void printPlayMenu() {
        System.out.println("\nVálasszon az alábbiak közül: \n" +
                "1. Előre lépés\n" +
                "2. Balra fordulás\n" +
                "3. Jobbra fordulás\n" +
                "4. Lövés\n" +
                "5. Arany felszedése\n" +
                "6. Játék elhalasztás\n" +
                "7. Feladás\n" +
                "Ön választása: ");
    }

    /**
     * Prints the menu options for editing the game map.
     * <p>
     * This method displays a menu with numbered options for adding and removing elements from the game map,
     * as well as options for exiting the editing process with or without saving changes.
     */
    public static void printEditMenu() {
        System.out.println("\nVálasszon az alábbiak közül: \n" +
                "1. Elem hozzáadása\n" +
                "2. Elem eltávolítása\n" +
                "3. Kilépés pálya mentése nélkül\n" +
                "4. Kilépés pálya mentésével\n" +
                "Ön választása: ");
    }
}

package hu.nye.progtech.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;

/**
 * The Menu class represents the main menu of the game, providing options for starting a new game, loading a saved game,
 * adding a new map, and exiting the application.
 */

public class Menu {

    private static String userName;
    private final Scanner menuScanner = new Scanner(System.in);
    private final DatabaseService database = new DatabaseService();
    private static int mapID;

    /**
     * Gets the username.
     *
     * @return The username.
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Gets the map ID.
     *
     * @return The map ID.
     */
    public static int getMapID() {
        return mapID;
    }

    /**
     * Constructs a Menu object, reads the username, prints the menu, and reads the user's choice.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public Menu() throws IOException, SQLException {
        readUserName();
        printMenu();
        readChoice();
    }

    /**
     * Prints the main menu options.
     */
    public void printMenu() {
        System.out.println("\n1. Új játék\n" +
                "2. Játék betöltése\n" +
                "3. Új pálya hozzáadása\n" +
                "4. Kilépés\n" +
                "Ön választása: ");
    }

    /**
     * Reads the user's name.
     */
    public void readUserName() {
        System.out.println("\nAdja meg a felhasználónevét: ");
        userName = menuScanner.nextLine();
    }

    /**
     * Reads the user's choice and performs the corresponding action.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public void readChoice() throws IOException, SQLException {
        int choice = 0;

        while (choice != 4) {
            try {
                choice = menuScanner.nextInt();
            } catch (InputMismatchException e) {
                printMenu();
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
                        new Play(map, hero);
                    } catch (NullPointerException e) {
                        System.out.println("\nHiba próbáld újra!\n");
                        new Menu();
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
                        new Menu();
                    }

                    database.deleteUnfinishedGame();

                    database.closeDatabaseConnection();

                    System.out.println("\nMentett pálya sikeresen betöltve!\n");

                    new Play(loadMap, loadHero, startCoordinateX, startCoordinateY, score);
                    break;
                case 3:
                    new EditMap();
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
}

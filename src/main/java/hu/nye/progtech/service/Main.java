package hu.nye.progtech.service;

import java.sql.SQLException;

/**
 * The Main class serves as the entry point for the Wumpus Hunt game application.
 * It creates an instance of the Menu class to start the game and handle user interactions.
 */
public class Main {
    /**
     * The main method of the application.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {
            new Menu();
        } catch (SQLException e) {
            System.out.println("Nem indítottál el adatbázist!\n" + e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Hiba: " + e.getMessage());
            System.exit(0);
        }
    }
}
package hu.nye.progtech.service;

import java.io.IOException;
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
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    public static void main(String[] args) throws IOException, SQLException {
        new Menu();
    }
}
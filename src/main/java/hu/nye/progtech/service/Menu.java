package hu.nye.progtech.service;

import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {

    private static String userName;
    private final Scanner menuScanner=new Scanner(System.in);
    private final DatabaseService database = new DatabaseService();
    private static int mapID;

    public static String getUserName() {
        return userName;
    }

    public static int getMapID() {
        return mapID;
    }

    public Menu() throws IOException, SQLException {
        readUserName();
        printMenu();
        readChoice();
    }

    public void printMenu(){
        System.out.println("\n1. Új játék\n" +
                "2. Játék betöltése\n" +
                "3. Új pálya hozzáadása\n" +
                "4. Kilépés\n" +
                "Ön választása: ");
    }

    public void readUserName(){
        System.out.println("\nAdja meg a felhasználónevét: ");
        userName=menuScanner.nextLine();
    }

    public void readChoice() throws IOException, SQLException {
        int choice=0;
        int cnt;

        while (choice != 4) {
            choice=menuScanner.nextInt();

            switch (choice) {
                case 1:
                    database.databaseConnection();
                    database.readAllMapFromDatabaseWithBuilder();
                    database.deleteUnfinishedGame();

                    System.out.println("Ön által választott pálya: ");
                    mapID = menuScanner.nextInt();

                    MapVO map = database.readMapFromDatabase(mapID);
                    Hero hero = database.readHeroFromDatabase(mapID);
                    database.closeDatabaseConnection();

                    new Play(map,hero);
                    break;
                case 2:
                    //ADATBÁZIS MŰVELET
                    //new Play(map,hero);
                    break;
                case 3:
                    new EditMap();
                    break;
                case 4:
                    System.out.println("\nSikeresen kilépett!\n");
                    choice=4;
                    //menuScanner.close();
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
    }
}

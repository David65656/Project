package hu.nye.progtech.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;

/**
 * The Play class represents the gameplay logic, allowing the player to interact with the game.
 */
public class Play {
    private MapVO playedMap;
    private Hero playedHero;
    private final int heroStartCoordinateX;
    private final int heroStartCoordinateY;

    /**
     * Constructs a Play object with the given MapVO and Hero, starting the game.
     *
     * @param playedMap The MapVO object representing the game map.
     * @param playedHero The Hero object representing the player.
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     */
    public Play(MapVO playedMap, Hero playedHero) throws SQLException, IOException {
        this.playedMap = playedMap;
        this.playedHero = playedHero;

        heroStartCoordinateX = playedHero.getCoordinateX();
        heroStartCoordinateY = playedHero.getCoordinateY();
        setHeroOnMap();
        setHeroArrowNumber();
    }

    /**
     * Constructs a Play object with the given MapVO, Hero, and initial hero coordinates, starting the game.
     *
     * @param playedMap The MapVO object representing the game map.
     * @param playedHero The Hero object representing the player.
     * @param heroStartCoordinateX The initial x-coordinate of the hero.
     * @param heroStartCoordinateY The initial y-coordinate of the hero.
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     */
    public Play(MapVO playedMap, Hero playedHero,
                int heroStartCoordinateX, int heroStartCoordinateY, int score) throws SQLException, IOException {
        this.playedMap = playedMap;
        this.playedHero = playedHero;

        this.heroStartCoordinateX = heroStartCoordinateX;
        this.heroStartCoordinateY = heroStartCoordinateY;
        setHeroOnMap();

        HeroMovements.setScore(score);
    }

    /**
     * Sets the hero on the map based on the initial coordinates.
     */
    public void setHeroOnMap() {
        for (int i = 0; i < playedMap.getRows(); i++) {
            for (int j = 0; j < playedMap.getColumns(); j++) {
                if ((i == playedHero.getCoordinateX() - 1) && (j == playedHero.getCoordinateY() - 1)) {
                    playedMap.getMap()[i][j] = 'H';
                }
            }
        }
    }

    /**
     * Sets the initial number of arrows for the hero based on the number of wumpuses on the map.
     */
    public void setHeroArrowNumber() {
        int cnt = 0;
        for (int i = 0; i < playedMap.getRows(); i++) {
            for (int j = 0; j < playedMap.getColumns(); j++) {
                if (playedMap.getMap()[i][j] == 'U') {
                    cnt++;
                }
            }
        }
        playedHero.setNumberOfArrows(cnt);
    }

    /**
     * Prints the menu of available actions for the player.
     */
    public void printMenu() {
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
     * Checks if the hero is in the starting position.
     *
     * @return True if the hero is in the starting position, false otherwise.
     */
    public boolean checkHeroPosition() {
        return heroStartCoordinateX == playedHero.getCoordinateX() && heroStartCoordinateY == playedHero.getCoordinateY();
    }

    /**
     * Manages the main gameplay loop, allowing the player to perform various actions.
     *
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     */
    public void playMenu() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        HeroMovements move = new HeroMovements();
        int choice = 0;
        int cnt = 0;

        while (choice != 7) {
            if (checkHeroPosition() && playedHero.isHaveGold()) {
                System.out.println("\nGratulálok! Megnyerted a játékot! A játék során megtett lépések száma: " + cnt +
                        "\nPontszámod: " + HeroMovements.getScore() + "\n");
                DatabaseService database = new DatabaseService();
                database.databaseConnection();
                database.isPlayerInScoreTable();
                database.printScoreTable();
                database.closeDatabaseConnection();
                choice = 7;
            } else {
                playedMap.mapPrint();
                System.out.println(playedHero);
                printMenu();

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    scanner.next();
                    playedMap.mapPrint();
                    System.out.println(playedHero);
                    printMenu();
                }

            }
                switch (choice) {
                    case 1:
                        playedHero = move.step(playedHero, playedMap);
                        playedMap = move.drawStepOnMap(playedMap, playedHero.getCoordinateX(), playedHero.getCoordinateY(), playedHero);
                        cnt++;
                        break;
                    case 2:
                        move.turnLeft(playedHero);
                        break;
                    case 3:
                        move.turnRight(playedHero);
                        break;
                    case 4:
                        if (playedHero.getNumberOfArrows() == 0) {
                            System.out.println("\nNincs nyilad, ezért nem tudsz lőni!\n");
                        } else {
                            playedMap = move.shoot(playedHero, playedMap);
                        }
                        break;
                    case 5:
                        move.pickupGold(playedHero, playedMap);
                        break;
                    case 6:
                        DatabaseService database = new DatabaseService();
                        database.databaseConnection();
                        database.sendSavedGameToDatabase(playedMap, playedHero);
                        database.sendPlayerScoreToDatabase();
                        System.out.println("\nSikeresen kilépett! Pálya sikeresen elmentve!\n");
                        database.closeDatabaseConnection();
                        choice = 7;
                        new Menu().printMenu();
                        break;
                    case 7:
                        System.out.println("\nSikeresen kilépett!\n");
                        new Menu().printMenu();
                        break;
                    default:
                        System.out.println("\nHiba! Próbálja újra!\n");
                }
        }
    }
}

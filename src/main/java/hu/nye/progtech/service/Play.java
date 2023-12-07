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

    public MapVO getPlayedMap() {
        return playedMap;
    }

    public void setPlayedMap(MapVO playedMap) {
        this.playedMap = playedMap;
    }

    public Hero getPlayedHero() {
        return playedHero;
    }

    public void setPlayedHero(Hero playedHero) {
        this.playedHero = playedHero;
    }

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
     * Checks if the hero is in the starting position.
     *
     * @return True if the hero is in the starting position, false otherwise.
     */
    public boolean checkHeroPosition() {
        return heroStartCoordinateX == playedHero.getCoordinateX() && heroStartCoordinateY == playedHero.getCoordinateY();
    }
}

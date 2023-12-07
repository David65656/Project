package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PlayTest {

    private Play underTest;
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private Hero testHero = new Hero(3,3,Direction.North,1,false);
    private char[][] testMap  = {
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
            {'W', '_', '_', '_', '_', '_', 'G', '_', '_', 'W'},
            {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
            {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
            {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
            {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
            {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
            {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
    };
    private MapVO testMapVO = new MapVO(10,10, testMap);

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @BeforeEach
    void setUp() throws SQLException, IOException {
        underTest = new Play(testMapVO, testHero);
    }


    @Test
    void testSetHeroOnMapShouldDrawHeroOnMap() {
        underTest.setHeroOnMap();
        assertEquals('H',testMapVO.getMap()[testHero.getCoordinateX()-1][testHero.getCoordinateY()-1]);
    }

    @Test
    void testSetHeroArrowNumberShouldSetHeroArrowNumber() {
        underTest.setHeroArrowNumber();
        assertEquals(2,testHero.getNumberOfArrows());
    }

    @Test
    void testCheckHeroPositionShouldReturnTrueWhenHeroAtStartCoordinates() {
        assertTrue(underTest.checkHeroPosition());
    }

    @Test
    void testGetPlayedMapShouldGivePlayedMap() {
        underTest.getPlayedMap().mapPrint();
    }

    @Test
    void testSetPlayedMapShouldSetPlayedMap() {
        char[][] testMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO map = new MapVO(10,10,testMap);
        underTest.setPlayedMap(map);

        assertEquals(map, underTest.getPlayedMap());
    }

    @Test
    void testGetPlayedHeroShouldGivePlayedHero() {
        System.out.println(underTest.getPlayedHero());
    }

    @Test
    void testSetPlayedHeroShouldSetPlayedHero() {
        Hero hero = new Hero(2,3,Direction.North,0,false);
        underTest.setPlayedHero(hero);

        assertEquals(hero,underTest.getPlayedHero());
    }
}
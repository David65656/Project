package hu.nye.progtech.service;

import hu.nye.progtech.model.*;
import hu.nye.progtech.model.Object;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class HeroMovementsTest {

    private HeroMovements underTest;

    @BeforeEach
    void setUp() {
        underTest = new HeroMovements();
    }


    @Test
    void testStepShouldMoveHeroToDirectionNorthWhenHeroFacesNorth() {
        Hero testHero = new Hero(3,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        Hero resultHero = underTest.step(testHero,testMapVO);

        assertEquals(2,resultHero.getCoordinateX());
        assertEquals(3,resultHero.getCoordinateY());
    }

    @Test
    void testStepShouldMoveHeroToDirectionSouthWhenHeroFacesSouth() {
        Hero testHero = new Hero(3,3,Direction.South,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        Hero resultHero = underTest.step(testHero,testMapVO);

        assertEquals(4,resultHero.getCoordinateX());
        assertEquals(3,resultHero.getCoordinateY());
    }

    @Test
    void testStepShouldMoveHeroToDirectionEastWhenHeroFacesEast() {
        Hero testHero = new Hero(3,3,Direction.East,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        Hero resultHero = underTest.step(testHero,testMapVO);

        assertEquals(3,resultHero.getCoordinateX());
        assertEquals(4,resultHero.getCoordinateY());
    }

    @Test
    void testStepShouldMoveHeroToDirectionWestWhenHeroFacesWest() {
        Hero testHero = new Hero(3,3,Direction.West,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        Hero resultHero = underTest.step(testHero,testMapVO);

        assertEquals(3,resultHero.getCoordinateX());
        assertEquals(2,resultHero.getCoordinateY());
    }

    @Test
    void testStepShouldNotMoveHeroWhenNextFieldIsWall() {
        Hero testHero = new Hero(3,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        Hero resultHero = underTest.step(testHero,testMapVO);

        assertEquals(3,resultHero.getCoordinateX());
        assertEquals(3,resultHero.getCoordinateY());
    }

    @Test
    void testDrawStepOnMapShouldDrawHeroWhenNextFieldIsEmpty() throws Exception {
        Hero testHero = new Hero(2,3,Direction.North,1,false);
        underTest.setOldCoordinateX(3);
        underTest.setOldCoordinateY(3);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        MapVO resultMapVO = underTest.drawStepOnMap(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY(), testHero);

        assertEquals('H', resultMapVO.getMap()[testHero.getCoordinateX()-1][testHero.getCoordinateY()-1]);

    }

    @Test
    void testDrawStepOnMapShouldDrawHeroWhenNextFieldIsGold() throws Exception {
        Hero testHero = new Hero(2,3,Direction.North,1,false);
        underTest.setOldCoordinateX(3);
        underTest.setOldCoordinateY(3);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        MapVO resultMapVO = underTest.drawStepOnMap(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY(), testHero);

        assertEquals('H', resultMapVO.getMap()[testHero.getCoordinateX()-1][testHero.getCoordinateY()-1]);

    }

    @Test
    void testDrawStepOnMapShouldDrawHeroAndChangeNumberOfArrowsWhenNextFieldIsPit() throws Exception {
        Hero testHero = new Hero(2,3,Direction.North,1,false);
        underTest.setOldCoordinateX(3);
        underTest.setOldCoordinateY(3);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'P', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        MapVO resultMapVO = underTest.drawStepOnMap(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY(), testHero);

        assertEquals('H', resultMapVO.getMap()[testHero.getCoordinateX()-1][testHero.getCoordinateY()-1]);
        assertEquals(0, testHero.getNumberOfArrows());

    }

    @Test
    void testCheckWumpusGoldPitShouldPrintMessageWhenNextFieldIsPit() throws Exception {
        Hero testHero = new Hero(2,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'P', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        underTest.checkWumpusGoldPit(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY());

        System.setOut(System.out);

        String expectedMessage = "\nVeremre léptél! Elvesztettél egy nyilat!\n" + System.getProperty("line.separator");;

        assertEquals(expectedMessage, outputStream.toString());
    }

    @Test
    void testCheckWumpusGoldPitShouldPrintMessageWhenNextFieldIsGold() throws Exception {
        Hero testHero = new Hero(2,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        underTest.checkWumpusGoldPit(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY());

        System.setOut(System.out);

        String expectedMessage = "\nArany mezőre léptél! Felszedheted az aranyat!\n" + System.getProperty("line.separator");;

        assertEquals(expectedMessage, outputStream.toString());
    }

    @Test
    void testCheckWallsShouldReturnTrueIfNextFieldIsNotWall() {
        Hero testHero = new Hero(3,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        assertTrue(underTest.checkWalls(testMapVO,testHero.getCoordinateX(),testHero.getCoordinateY()));

    }

    @Test
    void testTurnRightShouldReturnEastWhenHeroDirectionIsNorth() {
        Hero testHero = new Hero(2, 3, Direction.North, 0, false);

        underTest.turnRight(testHero);

        assertEquals(Direction.East,testHero.getViewingDirection());

    }

    @Test
    void testTurnRightShouldReturnWestWhenHeroDirectionIsSouth() {
        Hero testHero = new Hero(2, 3, Direction.South, 0, false);

        underTest.turnRight(testHero);

        assertEquals(Direction.West,testHero.getViewingDirection());

    }

    @Test
    void testTurnRightShouldReturnSouthWhenHeroDirectionIsEast() {
        Hero testHero = new Hero(2, 3, Direction.East, 0, false);

        underTest.turnRight(testHero);

        assertEquals(Direction.South,testHero.getViewingDirection());

    }

    @Test
    void testTurnRightShouldReturnNorthWhenHeroDirectionIsWest() {
        Hero testHero = new Hero(2, 3, Direction.West, 0, false);

        underTest.turnRight(testHero);

        assertEquals(Direction.North,testHero.getViewingDirection());

    }

    @Test
    void testTurnLeftShouldReturnWestWhenHeroDirectionIsNorth() {
        Hero testHero = new Hero(2, 3, Direction.North, 0, false);

        underTest.turnLeft(testHero);

        assertEquals(Direction.West,testHero.getViewingDirection());
    }

    @Test
    void testTurnLeftShouldReturnEastWhenHeroDirectionIsSouth() {
        Hero testHero = new Hero(2, 3, Direction.South, 0, false);

        underTest.turnLeft(testHero);

        assertEquals(Direction.East,testHero.getViewingDirection());
    }

    @Test
    void testTurnLeftShouldReturnSouthWhenHeroDirectionIsWest() {
        Hero testHero = new Hero(2, 3, Direction.West, 0, false);

        underTest.turnLeft(testHero);

        assertEquals(Direction.South,testHero.getViewingDirection());
    }

    @Test
    void testTurnLeftShouldReturnNorthWhenHeroDirectionIsEast() {
        Hero testHero = new Hero(2, 3, Direction.East, 0, false);

        underTest.turnLeft(testHero);

        assertEquals(Direction.North,testHero.getViewingDirection());
    }

    @Test
    void testShootShouldReturnMapWhenShootFailed() {
        Hero testHero = new Hero(3,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        MapVO result = underTest.shoot(testHero,testMapVO);

        assertEquals(result,testMapVO);
    }

    @Test
    void testShootShouldReturnMapWithoutWumpusWhenShootIsSuccess() {
        Hero testHero = new Hero(3,3,Direction.North,1,false);
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'U', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        MapVO result = underTest.shoot(testHero,testMapVO);

        assertEquals('_', result.getMap()[1][2]);
    }

    @Test
    void testCheckShotFieldShouldReturnUWhenShotFieldIsWumpus() {
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'U', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        char result = underTest.checkShotField(testMapVO, 2, 3);

        assertEquals('U',result);
    }

    @Test
    void testCheckShotFieldShouldReturnWWhenShotFieldIsWall() {
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', 'G', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        char result = underTest.checkShotField(testMapVO, 2, 3);

        assertEquals('W',result);
    }

    @Test
    void testPickupGoldShouldChangeHeroHaveGoldToTrueWhenHeroIsOnGold() {
        Hero testHero = new Hero(4,3,Direction.South,1,false);
        underTest.setObject(new Object(ObjectType.GOLD,3,2));
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'U', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        underTest.pickupGold(testHero,testMapVO);

        assertTrue(testHero.isHaveGold());
    }

    @Test
    void testPickupGoldShouldNotChangeHeroHaveGoldToTrueWhenHeroIsNotOnGold() {
        Hero testHero = new Hero(4,3,Direction.South,1,false);
        underTest.setObject(new Object(ObjectType.GOLD,2,2));
        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', 'U', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', 'G', '_', '_', '_', 'G', '_', '_', 'W'},
                {'W', '_', 'H', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'P', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(10,10,testMap);

        underTest.pickupGold(testHero,testMapVO);

        assertFalse(testHero.isHaveGold());
    }
}

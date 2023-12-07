package hu.nye.progtech.service;

import hu.nye.progtech.model.*;
import hu.nye.progtech.model.Object;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditMapTest {

    EditMap underTest;
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

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
    void setUp() {
        underTest = new EditMap();
    }

    @Test
    void testSizeCheckValidSizeShouldReturnTrue() {
        assertTrue(underTest.sizeCheck(10));
    }

    @Test
    void testSizeCheckInvalidSizeShouldReturnFalse() {
        assertFalse(underTest.sizeCheck(5));
        assertFalse(underTest.sizeCheck(25));
    }

    @Test
    void testWumpusBySizeSmallSizeShouldReturnOne() {
        assertEquals(1, underTest.wumpusBySize(6));
    }

    @Test
    void testWumpusBySizeMediumSizeShouldReturnTwo() {
        assertEquals(2, underTest.wumpusBySize(10));
    }

    @Test
    void testWumpusBySizeLargeSizeShouldReturnThree() {
        assertEquals(3, underTest.wumpusBySize(15));
    }

    @Test
    void testWumpusCountNoWumpusShouldReturnZero() {
        MapVO map = underTest.emptyMap(10);
        assertEquals(0, underTest.wumpusCount(map));
    }

    @Test
    void testWumpusCountTwoWumpusShouldReturnTwo() {
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
        assertEquals(2, underTest.wumpusCount(map));
    }

    @Test
    void testGoldCountNoGoldShouldReturnZero() {
        MapVO map = underTest.emptyMap(10);
        assertEquals(0, underTest.goldCount(map));
    }

    @Test
    void testGoldCountOneGoldShouldReturnOne() {
        char[][] testMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', 'G', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO map = new MapVO(10,10,testMap);
        assertEquals(1, underTest.goldCount(map));
    }

    @Test
    void testHeroCountNoHeroShouldReturnZero() {
        MapVO map = underTest.emptyMap(10);
        assertEquals(0, underTest.heroCount(map));
    }

    @Test
    void testHeroCountOneHeroShouldReturnOne() {
        char[][] testMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', 'H', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO map = new MapVO(10,10,testMap);
        assertEquals(1, underTest.heroCount(map));
    }

    @Test
    void testEmptyMapValidSizeShouldReturnCorrectMap() {
        MapVO map = underTest.emptyMap(8);

        char[][] expectedMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };

        assertArrayEquals(expectedMap, map.getMap());
    }

    @Test
    void testHeroDirectionReadValidNorthDirectionShouldReturnNorthDirection() {
        provideInput("North\n");
        assertEquals(Direction.North, underTest.heroDirectionRead());
    }

    @Test
    void testHeroDirectionReadValidSouthDirectionShouldReturnSouthDirection() {
        provideInput("South\n");
        assertEquals(Direction.South, underTest.heroDirectionRead());
    }

    @Test
    void testHeroDirectionReadValidEastDirectionShouldReturnEastDirection() {
        provideInput("East\n");
        assertEquals(Direction.East, underTest.heroDirectionRead());
    }

    @Test
    void testHeroDirectionReadValidWestDirectionShouldReturnWestDirection() {
        provideInput("West\n");
        assertEquals(Direction.West, underTest.heroDirectionRead());
    }

    @Test
    void testHeroDirectionReadInvalidDirectionShouldRetry() {
        provideInput("Invalid\nNorth\n");
        assertEquals(Direction.North, underTest.heroDirectionRead());
    }

    @Test
    void testObjectReadValidHeroObjectTypeShouldReturnHeroObject() {
        provideInput("HERO\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.HERO, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testObjectReadValidWumpusObjectTypeShouldReturnWumpusObject() {
        provideInput("WUMPUS\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.WUMPUS, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testObjectReadValidGoldObjectTypeShouldReturnGoldObject() {
        provideInput("GOLD\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.GOLD, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testObjectReadValidPitObjectTypeShouldReturnPitObject() {
        provideInput("PIT\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.PIT, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testObjectReadValidWallObjectTypeShouldReturnWallObject() {
        provideInput("WALL\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.WALL, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testObjectReadInvalidObjectTypeShouldRetry() {
        provideInput("Invalid\nHERO\n1\nA\n");
        Object object = underTest.objectRead();
        assertEquals(ObjectType.HERO, object.getType());
        assertEquals(1, object.getCoordinateX());
        assertEquals(1, object.getCoordinateY());
    }

    @Test
    void testAddObjectAddHeroShouldAddToMapAndSetEditedHero() {

        MapVO initialMap = underTest.emptyMap(10);

        provideInput("North\n");

        Object heroObject = new Object(ObjectType.HERO, 5, 5);

        MapVO result = underTest.addObject(initialMap, heroObject);

        assertEquals('_', result.getMap()[4][4]);

        assertNotNull(underTest.getEditedHero());
        assertEquals(5, underTest.getEditedHero().getCoordinateX());
        assertEquals(5, underTest.getEditedHero().getCoordinateY());
    }

    @Test
    void testAddObjectAddGoldShouldAddToMap() {

        MapVO initialMap = underTest.emptyMap(10);

        Object goldObject = new Object(ObjectType.GOLD, 3, 3);

        MapVO result = underTest.addObject(initialMap, goldObject);

        assertEquals('G', result.getMap()[2][2]);
    }

    @Test
    void testAddObjectAddWumpusShouldAddToMap() {

        MapVO initialMap = underTest.emptyMap(10);

        Object wumpusObject = new Object(ObjectType.WUMPUS, 3, 3);

        MapVO result = underTest.addObject(initialMap, wumpusObject);

        assertEquals('U', result.getMap()[2][2]);
    }

    @Test
    void testAddObjectAddPitShouldAddToMap() {

        MapVO initialMap = underTest.emptyMap(10);

        Object pitObject = new Object(ObjectType.PIT, 3, 3);

        MapVO result = underTest.addObject(initialMap, pitObject);

        assertEquals('P', result.getMap()[2][2]);
    }

    @Test
    void testAddObjectAddWallShouldAddToMap() {

        MapVO initialMap = underTest.emptyMap(10);

        Object wallObject = new Object(ObjectType.WALL, 3, 3);

        MapVO result = underTest.addObject(initialMap, wallObject);

        assertEquals('W', result.getMap()[2][2]);
    }

    @Test
    void testRemoveObjectRemoveValidElementShouldRemoveFromMap() {

        MapVO initialMap = underTest.emptyMap(10);

        String input = "5\nE\n";
        provideInput(input);

        MapVO result = underTest.removeObject(initialMap);

        assertEquals('_', result.getMap()[4][4]);
    }

    @Test
    void testCheckRemoveObjectValidCoordinatesShouldReturnTrue() {

        char[][] testMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'W', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO map = new MapVO(10,10,testMap);

        assertTrue(EditMap.checkRemoveObject(map, 6, 4));
    }

    @Test
    void testCheckRemoveObjectInvalidCoordinatesShouldReturnFalse() {

        char[][] testMap = {
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'U', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'U', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', 'W', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', '_', '_', '_', '_', '_', '_', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO map = new MapVO(10,10,testMap);


        assertFalse(EditMap.checkRemoveObject(map, 1, 3));
        assertFalse(EditMap.checkRemoveObject(map, 2, 10));
        assertFalse(EditMap.checkRemoveObject(map, 11, 11));
    }

    @Test
    void testCheckAddObjectValidAdditionShouldReturnTrue() {

        MapVO map = underTest.emptyMap(10);

        Object validObject = new Object(ObjectType.HERO, 2, 3);

        assertTrue(underTest.checkAddObject(map, validObject));
    }

    @Test
    void testCheckAddObjectInvalidCoordinatesShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);

        Object invalidObject = new Object(ObjectType.HERO, 12, 8);

        assertFalse(underTest.checkAddObject(map, invalidObject));
    }

    @Test
    void testCheckAddObjectDuplicateGoldShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);
        map.getMap()[2][3] = 'G';

        Object duplicateGoldObject = new Object(ObjectType.GOLD, 4, 5);

        assertFalse(underTest.checkAddObject(map, duplicateGoldObject));
    }

    @Test
    void testCheckAddObjectDuplicateHeroShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);
        map.getMap()[2][3] = 'H';

        Object duplicateHeroObject = new Object(ObjectType.HERO, 4, 5);

        assertFalse(underTest.checkAddObject(map, duplicateHeroObject));
    }

    @Test
    void testCheckAddObjectTooMuchWumpusShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);
        map.getMap()[2][3] = 'U';
        map.getMap()[5][3] = 'U';

        Object wumpusObject = new Object(ObjectType.WUMPUS, 4, 5);

        assertFalse(underTest.checkAddObject(map, wumpusObject));
    }

    @Test
    void testCheckAddObjectInvalidPositionShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);
        map.getMap()[2][3] = 'U';

        Object goldObject = new Object(ObjectType.GOLD, 1, 2);

        assertFalse(underTest.checkAddObject(map, goldObject));
    }

    @Test
    void testCheckAddObjectInvalidHeroPositionShouldReturnFalse() {

        MapVO map = underTest.emptyMap(10);
        map.getMap()[2][3] = 'U';

        Object goldObject = new Object(ObjectType.HERO, 1, 2);

        assertFalse(underTest.checkAddObject(map, goldObject));
    }

    @Test
    void testCheckMapValidMapShouldReturnTrue() {

        MapVO validMap = underTest.emptyMap(10);
        validMap.getMap()[2][3] = 'H';
        validMap.getMap()[4][5] = 'G';
        validMap.getMap()[6][7] = 'U';
        validMap.getMap()[8][9] = 'U';

        Hero testHero = new Hero(1,2, Direction.North, 0,false);
        underTest.setEditedHero(testHero);

        assertTrue(underTest.checkMap(validMap));
    }

    @Test
    void testCheckMapMissingGoldShouldReturnFalse() {

        MapVO mapMissingGold = underTest.emptyMap(10);
        mapMissingGold.getMap()[2][3] = 'H';


        assertFalse(underTest.checkMap(mapMissingGold));
    }

    @Test
    void testCheckMapMissingHeroShouldReturnFalse() {

        MapVO mapMissingHero = underTest.emptyMap(10);
        mapMissingHero.getMap()[4][5] = 'G';


        assertFalse(underTest.checkMap(mapMissingHero));
    }

    @Test
    void testCheckMapIncorrectWumpusCountShouldReturnFalse() {

        MapVO mapIncorrectWumpusCount = underTest.emptyMap(10);
        mapIncorrectWumpusCount.getMap()[2][3] = 'H';
        mapIncorrectWumpusCount.getMap()[4][5] = 'G';
        mapIncorrectWumpusCount.getMap()[6][7] = 'U';

        Hero testHero = new Hero(1,2, Direction.North, 0,false);
        underTest.setEditedHero(testHero);

        assertFalse(underTest.checkMap(mapIncorrectWumpusCount));
    }
    @Test
    void testPrintMenuShouldPrintCorrectMenu() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        underTest.printMenu();

        System.setOut(System.out);

        String expectedMenu = "\nVálasszon az alábbiak közül: \n" +
                "1. Elem hozzáadása\n" +
                "2. Elem eltávolítása\n" +
                "3. Kilépés pálya mentése nélkül\n" +
                "4. Kilépés pálya mentésével\n" +
                "Ön választása: " + System.getProperty("line.separator");;

        assertEquals(expectedMenu, outputStream.toString());
    }
}

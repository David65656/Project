package hu.nye.progtech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuTest {

    private Menu underTest;

    @BeforeEach
    void setUp() throws Exception {
        underTest = new Menu(0);
        Menu.setUserName("testUser");
        Menu.setMapID(100000);
    }

    @Test
    void testGetUserName() {
        assertEquals("testUser", Menu.getUserName());
    }

    @Test
    void testSetUserName() {
        Menu.setUserName("testUser");
        assertEquals("testUser", Menu.getUserName());
    }

    @Test
    void testGetMapID() {
        assertEquals(100000, Menu.getMapID());
    }

    @Test
    void testPrintMenu() {
        underTest.printMenu();
    }
}

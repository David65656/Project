package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest {

    private DatabaseService underTest;

    private final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/wumpusdatabase";
    private final Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");

    DatabaseServiceTest() throws SQLException {
    }

    @BeforeEach
    void setUp() throws Exception {
        underTest = new DatabaseService();
    }

    @Test
    void testDatabaseConnectionShouldConnectDatabase() {
        underTest.databaseConnection();
    }

    @Test
    void testCloseDatabaseConnectionShouldCloseDatabase() {
        underTest.closeDatabaseConnection();
    }

    @Test
    void testReadAllMapFromDatabaseWithBuilderShouldReadAllMaps() throws Exception {
        underTest.readAllMapFromDatabaseWithBuilder();
    }

    @Test
    void testDeleteUnfinishedGameShouldDeleteUnfinishedGames() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();
        Menu.setUserName("testUser");
        Menu.setMapID(1);
        underTest.deleteUnfinishedGame();
        underTest.closeDatabaseConnection();
    }

    @Test
    void testConvertMapShouldConvertMapToString() {
        char[][] testMap  = {
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
        MapVO testMapVO = new MapVO(10,10, testMap);

        String result = underTest.convertMap(testMapVO);

        assertEquals("WWWWWWWWWW#W________W#W_____G__W#W________W#W___U____W#W__U__P__W#W________W#W________W#W________W#WWWWWWWWWW", result);
    }

    @Test
    void testConvertCharToStringShouldConvertGivenHeroColumnAsCharToString() {
        Hero hero = new Hero(5, 3, Direction.North, 0, false);

        String result = underTest.convertCharToString(hero);

        assertEquals("C", result);
    }

    @Test
    void testConvertDirectionToStringShouldConvertGivenDirectionToString() {
        assertEquals("North", underTest.convertDirectionToString(Direction.North));
        assertEquals("South", underTest.convertDirectionToString(Direction.South));
        assertEquals("West", underTest.convertDirectionToString(Direction.West));
        assertEquals("East", underTest.convertDirectionToString(Direction.East));
    }

    @Test
    void testConvertBooleanToIntShouldConvertGivenIntToBoolean() {
        assertEquals(0, underTest.convertBooleanToInt(false));
        assertEquals(1, underTest.convertBooleanToInt(true));
    }

    @Test
    void testPrintScoreTableShouldPrintScoreTable() throws Exception {
        Menu.setMapID(1);
        underTest.printScoreTable();
    }

    @Test
    void testSendPlayerScoreToDatabaseShouldSavePlayerScore() throws Exception {
        Menu.setUserName("testUser");
        Menu.setMapID(1);
        HeroMovements.setScore(10);

        underTest.databaseConnection();

        underTest.sendPlayerScoreToDatabase();
        underTest.printScoreTable();
        Statement statement = connection.createStatement();
        String query = "DELETE FROM scoretable WHERE username='testUser';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testUpdatePlayerScoreInDatabaseShouldUpdateExistingScore() throws Exception {
        Menu.setUserName("testUser");
        Menu.setMapID(1);
        HeroMovements.setScore(10);

        underTest.databaseConnection();

        underTest.sendPlayerScoreToDatabase();
        underTest.printScoreTable();

        HeroMovements.setScore(100);
        underTest.updatePlayerScoreInDatabase();
        underTest.printScoreTable();

        Statement statement = connection.createStatement();
        String query = "DELETE FROM scoretable WHERE username='testUser';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testIsPlayerInScoreTableShouldSaveNewScoreOrUpdateExistingScore() throws Exception {
        Menu.setUserName("testUser");
        Menu.setMapID(1);
        HeroMovements.setScore(10);

        underTest.databaseConnection();

        underTest.isPlayerInScoreTable();
        underTest.printScoreTable();

        HeroMovements.setScore(100);
        underTest.isPlayerInScoreTable();
        underTest.printScoreTable();

        Statement statement = connection.createStatement();
        String query = "DELETE FROM scoretable WHERE username='testUser';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();

    }

    @Test
    void testConvertIntToBooleanShouldConvertIntToBoolean() {
        assertFalse(underTest.convertIntToBoolean(0));
        assertTrue(underTest.convertIntToBoolean(1));
    }

    private void printSavedGame() {
        underTest.databaseConnection();

        String query = "SELECT * FROM savedgametable";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String mapID = resultSet.getString("mapID");
                String mapCurrentState = resultSet.getString("mapCurrentState");
                String heroCurrentRow = resultSet.getString("heroCurrentRow");
                String heroCurrentColumn = resultSet.getString("heroCurrentColumn");
                String heroCurrentView = resultSet.getString("heroCurrentView");
                String heroCurrentHaveGold = resultSet.getString("heroCurrentHaveGold");
                String heroCurrentNumberOfArrows = resultSet.getString("heroCurrentNumberOfArrows");
                String userCurrentScore = resultSet.getString("userCurrentScore");
                System.out.println("\n" + userName + ", " + mapID + ", " + mapCurrentState + ", " + heroCurrentRow + ", " + heroCurrentColumn + ", " + heroCurrentView + ", " + heroCurrentHaveGold + ", " + heroCurrentNumberOfArrows + ", " + userCurrentScore);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        underTest.closeDatabaseConnection();
    }

    @Test
    void testSendSavedGameToDatabaseShouldSaveGame() throws Exception {
        char[][] testMap  = {
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
        MapVO testMapVO = new MapVO(10,10, testMap);

        Hero testHero = new Hero(4, 5, Direction.North, 0, false);

        Menu.setUserName("testUser");
        Menu.setMapID(1);
        HeroMovements.setScore(10);

        underTest.databaseConnection();

        underTest.sendSavedGameToDatabase(testMapVO, testHero);

        printSavedGame();

        Statement statement = connection.createStatement();
        String query = "DELETE FROM savedgametable WHERE username='testUser';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();

    }

    @Test
    void testLoadSavedMapFromDatabaseShouldLoadSavedMapFromDatabase() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Menu.setUserName("testUser");
        Menu.setMapID(1);
        MapVO result = underTest.loadSavedMapFromDatabase("testUser");

        assertEquals("WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW", underTest.convertMap(result));

        underTest.deleteUnfinishedGame();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testLoadSavedHeroFromDatabaseShouldLoadSavedHeroFromDatabase() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Menu.setUserName("testUser");
        Menu.setMapID(1);
        Hero result = underTest.loadSavedHeroFromDatabase("testUser");

        assertEquals(5, result.getCoordinateX());
        assertEquals(5, result.getCoordinateY());
        assertEquals(Direction.North, result.getViewingDirection());
        assertEquals(1, result.getNumberOfArrows());
        assertFalse(result.isHaveGold());

        underTest.deleteUnfinishedGame();

        underTest.closeDatabaseConnection();

    }

    @Test
    void testLoadSavedHeroStartCoordinateXFromDatabaseShouldLoadSavedHeroStartCoordinateXFromDatabase() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Menu.setUserName("testUser");
        Menu.setMapID(1);
        Hero hero = underTest.loadSavedHeroFromDatabase("testUser");
        int result = underTest.loadSavedHeroStartCoordinateXFromDatabase(hero);

        assertEquals(5, result);

        underTest.deleteUnfinishedGame();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testLoadSavedHeroStartCoordinateYFromDatabaseShouldLoadSavedHeroStartCoordinateYFromDatabase() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Menu.setUserName("testUser");
        Menu.setMapID(1);
        Hero hero = underTest.loadSavedHeroFromDatabase("testUser");
        int result = underTest.loadSavedHeroStartCoordinateYFromDatabase(hero);

        assertEquals(5, result);

        underTest.deleteUnfinishedGame();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testLoadUserSavedScoreFromDatabaseShouldLoadUserSavedScore() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES ('testUser',1,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW',5,'E','North',0,1,0);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Menu.setUserName("testUser");
        Menu.setMapID(1);

        int result = underTest.loadUserSavedScoreFromDatabase();

        assertEquals(0, result);

        underTest.deleteUnfinishedGame();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testReadMapFromDatabaseShouldReadMapFromDatabase() throws Exception {
        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query1 = "INSERT INTO maptable VALUES (100000,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW','testUser',100000);";
        String query2 = "INSERT INTO herotable VALUES (100000,5,'E','North');";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query2)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query1)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        MapVO result = underTest.readMapFromDatabase(100000);

        assertEquals("WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW", underTest.convertMap(result));

        Statement statementDelete = connection.createStatement();
        String queryDelete1 = "DELETE FROM maptable WHERE mapID=100000;";
        String queryDelete2 = "DELETE FROM herotable WHERE heroID=100000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDelete1)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDelete2)) {
            preparedStatement.executeUpdate();
        }

        statementDelete.close();


        underTest.closeDatabaseConnection();
    }

    @Test
    void testMapAsStringToMapVOShouldConvertStringToMapvO() {
        underTest.mapAsStringToMapVO("WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW");

        char[][] testMap  = {
                {'W', 'W', 'W', 'W', 'W', 'W'},
                {'W', '_', '_', '_', '_', 'W'},
                {'W', '_', '_', 'G', '_', 'W'},
                {'W', 'P', '_', '_', '_', 'W'},
                {'W', 'U', '_', '_', 'H', 'W'},
                {'W', 'W', 'W', 'W', 'W', 'W'}
        };
        MapVO testMapVO = new MapVO(6,6, testMap);

        assertEquals(6, testMapVO.getRows());
        assertEquals(6, testMapVO.getColumns());

        for(int i = 0; i < 6; i++) {
            for(int j = 0; j<6; j++){
                assertEquals(testMapVO.getMap()[i][j],underTest.getReadMap().getMap()[i][j]);
            }
        }
    }

    @Test
    void testReadHeroFromDatabaseShouldReadHeroFromDatabase() throws Exception {

        underTest.databaseConnection();
        Statement statement = connection.createStatement();
        String query1 = "INSERT INTO maptable VALUES (100000,'WWWWWW#W____W#W__G_W#WP___W#WU__HW#WWWWWW','testUser',100000);";
        String query2 = "INSERT INTO herotable VALUES (100000,5,'E','North');";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query2)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query1)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        Hero result = underTest.readHeroFromDatabase(100000);

        assertEquals(5, result.getCoordinateX());
        assertEquals(5, result.getCoordinateY());
        assertEquals(Direction.North, result.getViewingDirection());
        assertEquals(false, result.isHaveGold());
        assertEquals(0, result.getNumberOfArrows());

        Statement statementDelete = connection.createStatement();
        String queryDelete1 = "DELETE FROM maptable WHERE mapID=100000;";
        String queryDelete2 = "DELETE FROM herotable WHERE heroID=100000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDelete1)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDelete2)) {
            preparedStatement.executeUpdate();
        }

        statementDelete.close();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testHeroDirectionReadShouldConvertDirectionAsStringToDirectionType() {
        assertEquals(Direction.North, underTest.heroDirectionRead("North"));
        assertEquals(Direction.South, underTest.heroDirectionRead("South"));
        assertEquals(Direction.West, underTest.heroDirectionRead("West"));
        assertEquals(Direction.East, underTest.heroDirectionRead("East"));
    }

    @Test
    void testSendEditedMapToDatabaseShouldSendEditedMapToDatabase() throws Exception {
        underTest.databaseConnection();

        Menu.setUserName("testUser");
        char[][] testMap  = {
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
        MapVO testMapVO = new MapVO(10,10, testMap);

        Statement insertStatement = connection.createStatement();
        String insertQuery = "INSERT INTO herotable VALUES ('100000',5,'E','North')";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.executeUpdate();
        }

        insertStatement.close();

        underTest.sendEditedMapToDatabase(testMapVO,100000);

        underTest.readAllMapFromDatabaseWithBuilder();

        Statement statement = connection.createStatement();
        String deleteQuery1 = "DELETE FROM maptable WHERE heroID=100000";
        String deleteQuery2 = "DELETE FROM herotable WHERE heroID=100000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery1)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery2)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testSendEditedMapToDatabaseThrowsSQLException() {
        final MapVO map = new MapVO(0, 0, new char[][]{{'a'}});

        assertThrows(SQLException.class, () -> underTest.sendEditedMapToDatabase(map, 0));
    }

    private void readAllHeroFromDatabase() {
        underTest.databaseConnection();

        String query = "SELECT * FROM herotable";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String heroID = resultSet.getString("heroID");
                String heroColumn = resultSet.getString("heroColumn");
                String heroRow = resultSet.getString("heroRow");
                String heroView = resultSet.getString("heroView");
                System.out.println("\n" + heroID + ", " + heroColumn + ", " + heroRow + ", " + heroView);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        underTest.closeDatabaseConnection();
    }

    @Test
    void testSendEditedHeroToDatabase() throws Exception {
        underTest.databaseConnection();

        Menu.setUserName("testUser");

        Hero testHero = new Hero(5, 5, Direction.North, 0, false);

        underTest.sendEditedHeroToDatabase(testHero);

        readAllHeroFromDatabase();

        Statement statement = connection.createStatement();
        String deleteQuery = "DELETE FROM herotable WHERE heroID=100000";
        String updateQuery = "UPDATE herotable\n" +
                "SET heroID = 100000\n" +
                "WHERE heroID = (SELECT MAX(heroID) FROM herotable);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();
    }

    @Test
    void testGetHeroIDFromDatabase() throws Exception {

        Hero testHero = new Hero(100, 1, Direction.North, 0, false);

        underTest.databaseConnection();
        Statement insertStatement = connection.createStatement();
        String insertQuery = "INSERT INTO herotable VALUES (100000,100,'A','North')";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.executeUpdate();
        }

        insertStatement.close();

        int result = underTest.getHeroIDFromDatabase(testHero);

        assertEquals(100000, result);

        Statement statement = connection.createStatement();
        String deleteQuery = "DELETE FROM herotable WHERE heroID=100000";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        }

        statement.close();

        underTest.closeDatabaseConnection();
    }
}

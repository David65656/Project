package hu.nye.progtech.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.MenuVO;

/**
 * The `DatabaseService` class provides methods to interact with a MySQL database for a Wumpus game.
 * It handles database connection, reading and writing game data, and managing user scores.
 */
public class DatabaseService {
    private final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/wumpusdatabase";
    private final Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
    private MapVO readMap;
    private Hero readHero;

    public MapVO getReadMap() {
        return readMap;
    }

    public DatabaseService() throws SQLException {}

    /**
     * Establishes a connection to the MySQL database.
     */
    public void databaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Hol van a MySQL JDBC Driver?");
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(
                jdbcUrl, "root", "")) {
            if (connection != null) {
                System.out.println("Adatbázishoz való csatlakozás sikeres!");
            } else {
                System.out.println("Adatbázishoz való csatlakozás sikertelen!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the MySQL database.
     */
    public void closeDatabaseConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Adatbázis sikeresen bezárva!");
        }
    }

    /**
     * Reads all maps from the database using a builder pattern and prints them to the console.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void readAllMapFromDatabaseWithBuilder() throws SQLException {
        int cnt = 1;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT map, builderName FROM maptable;");

        while (resultSet.next()) {
            final String mapAsString = resultSet.getString("map");
            final String builderName = resultSet.getString("builderName");

            String[] splitMap = mapAsString.split("#");
            System.out.println("\n" + cnt + ". pálya: ");
            for (int i = 0; i < splitMap.length; i++) {
                System.out.println(splitMap[i]);
            }
            System.out.print("A készítő: ");
            System.out.println(builderName);
            cnt++;
        }
        statement.close();
    }

    /**
     * Deletes the unfinished game data for the current user from the database.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void deleteUnfinishedGame() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM savedgametable WHERE userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, MenuVO.getUserName());
            if (preparedStatement.executeUpdate() != 0) {
                System.out.println("Mentett játék törölve!");
            }
        }

        statement.close();
    }

    /**
     * Converts a MapVO object to a string representation of the map.
     * <p>
     * This method takes a MapVO object and constructs a string representation of the map
     * by concatenating the characters in each row, separating rows with the '#' character.
     *
     * @param map The MapVO object to be converted to a string.
     * @return A string representation of the map.
     */
    public String convertMap(MapVO map) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < map.getRows(); i++) {
            if (i != 0) {
                sb.append("#");
            }
            for (int j = 0; j < map.getColumns(); j++) {
                sb.append(map.getMap()[i][j]);
            }
        }
        return  sb.toString();
    }

    /**
     * Converts the column coordinate of a Hero object to its corresponding string representation.
     * <p>
     * This method takes a Hero object and retrieves its column coordinate. It then converts
     * the coordinate value to its corresponding uppercase letter representation (A for 1, B for 2, etc.)
     * and returns the result as a string.
     *
     * @param hero The Hero object from which to extract the column coordinate.
     * @return The string representation of the column coordinate.
     */
    public String convertCharToString(Hero hero) {
        char[] charArray = { (char) (hero.getCoordinateY() + 64) };
        return String.valueOf(charArray);
    }

    /**
     * Converts a Direction enum value to its corresponding string representation.
     * <p>
     * This method takes a Direction enum value and returns a string representation based on the
     * cardinal direction (North, South, West, East).
     *
     * @param direction The Direction enum value to be converted to a string.
     * @return The string representation of the given Direction.
     */
    public String convertDirectionToString(Direction direction) {
        if (direction == Direction.North) {
            return "North";
        } else if (direction == Direction.South) {
            return "South";
        } else if (direction == Direction.West) {
            return "West";
        } else {
            return "East";
        }
    }

    /**
     * Converts a boolean value to its corresponding integer representation.
     * <p>
     * This method takes a boolean value as input and returns an integer representation,
     * where `true` is mapped to 1 and `false` is mapped to 0.
     *
     * @param bool The boolean value to be converted.
     * @return The integer representation of the boolean value (1 for true, 0 for false).
     */
    public int convertBooleanToInt(Boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Prints the score table for a specific map from the database.
     * <p>
     * This method retrieves and prints the score table information for a particular map
     * by executing a SQL query. It displays the usernames and corresponding scores in the
     * standard output. The mapID parameter is used to filter scores for a specific map.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void printScoreTable() throws SQLException {
        int cnt = 1;
        String query = "SELECT userName, score FROM scoretable WHERE mapID=? ORDER BY score DESC;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, MenuVO.getMapID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String score = resultSet.getString("score");
                System.out.println("\n" + cnt + ". " + userName + ": " + score);
                cnt++;
            }
        }
    }

    /**
     * Sends the player's score to the database.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void sendPlayerScoreToDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO scoretable VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, MenuVO.getUserName());
            preparedStatement.setInt(2, HeroMovements.getScore());
            preparedStatement.setInt(3, MenuVO.getMapID());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }


    /**
     * Updates or sends the player's score to the database based on whether the player exists in the score table.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void updatePlayerScoreInDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "UPDATE scoretable SET score=? WHERE mapID=? AND userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, HeroMovements.getScore());
            preparedStatement.setInt(2, MenuVO.getMapID());
            preparedStatement.setString(3, MenuVO.getUserName());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }

    /**
     * Checks if a player is present in the score table for the current user.
     * <p>
     * This method queries the score table in the database to determine if the current
     * user's username exists in the table.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void isPlayerInScoreTable() throws SQLException {
        String query = "SELECT userName FROM scoretable WHERE mapID=? AND userName=?;";
        Statement statement = connection.createStatement();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, MenuVO.getMapID());
            preparedStatement.setString(2, MenuVO.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                updatePlayerScoreInDatabase();
            } else {
                sendPlayerScoreToDatabase();
            }
        }
        statement.close();
    }

    /**
     * Converts an integer value to its corresponding boolean representation.
     * <p>
     * This method takes an integer value as input and returns a boolean representation,
     * where the result is 'true' if the input is non-zero and 'false' if the input is zero.
     *
     * @param n The integer value to be converted.
     * @return The boolean representation of the integer value (true if non-zero, false if zero).
     */
    public Boolean convertIntToBoolean(int n) {
        return n != 0;
    }

    /**
     * Sends the saved game data (map and hero information) to the database.
     *
     * @param map  The map data to be saved.
     * @param hero The hero data to be saved.
     * @throws SQLException If a database access error occurs.
     */
    public void sendSavedGameToDatabase(MapVO map, Hero hero) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES (?,?,?,?,?,?,?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, MenuVO.getUserName());
            preparedStatement.setInt(2, MenuVO.getMapID());
            preparedStatement.setString(3, convertMap(map));
            preparedStatement.setInt(4, hero.getCoordinateX());
            preparedStatement.setString(5, convertCharToString(hero));
            preparedStatement.setString(6, convertDirectionToString(hero.getViewingDirection()));
            preparedStatement.setInt(7, convertBooleanToInt(hero.isHaveGold()));
            preparedStatement.setInt(8, hero.getNumberOfArrows());
            preparedStatement.setInt(9, HeroMovements.getScore());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }

    /**
     * Loads the saved map from the database for a specific user.
     * <p>
     * This method retrieves the mapCurrentState from the 'savedgametable' for the given username.
     * It then calls the 'mapAsStringToMapVO' method to convert the retrieved map string to a MapVO object.
     *
     * @param user The username of the player whose saved map is to be loaded.
     * @return The MapVO object representing the saved map.
     * @throws SQLException If a database access error occurs.
     */
    public MapVO loadSavedMapFromDatabase(String user) throws SQLException {
        String query = "SELECT mapCurrentState FROM savedgametable WHERE userName=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String mapAsString = resultSet.getString("mapCurrentState");
                mapAsStringToMapVO(mapAsString);
            }
        }
        return readMap;
    }

    /**
     * Loads a Hero object from the database based on the provided user's username.
     * <p>
     * This method retrieves information about a saved Hero from the database, including its
     * current row, column, viewing direction, possession of gold, and the number of arrows.
     * It then creates and returns a new Hero object with the loaded information.
     *
     * @param user The username associated with the saved game in the database.
     * @return The Hero object loaded from the database.
     * @throws SQLException If a database access error occurs.
     */
    public Hero loadSavedHeroFromDatabase(String user) throws SQLException {
        String query = "SELECT heroCurrentRow, heroCurrentColumn, heroCurrentView, heroCurrentHaveGold, heroCurrentNumberOfArrows " +
                "FROM savedgametable WHERE userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int heroRow = resultSet.getInt("heroCurrentRow");
                int heroColumn = (resultSet.getString("heroCurrentColumn").charAt(0)) - 64;
                String heroView = resultSet.getString("heroCurrentView");
                boolean haveGold =  convertIntToBoolean(resultSet.getInt("heroCurrentHaveGold"));
                int numberOfArrows = resultSet.getInt("heroCurrentNumberOfArrows");

                Direction heroDirection = heroDirectionRead(heroView);

                readHero = new Hero(heroRow, heroColumn, heroDirection, numberOfArrows, haveGold);
            }
        }
        return  readHero;
    }

    private int heroStartRow;

    /**
     * Loads the starting row coordinate of a Hero object from the database.
     *
     * This method retrieves the starting row coordinate of a Hero from the database,
     * based on the provided Hero object's current row coordinate. The query involves
     * joining tables to obtain the necessary information.
     *
     * @param hero The Hero object for which to load the starting row coordinate.
     * @return The starting row coordinate of the Hero loaded from the database.
     * @throws SQLException If a database access error occurs.
     */
    public int loadSavedHeroStartCoordinateXFromDatabase(Hero hero) throws SQLException {
        String query = "SELECT heroRow " +
                "FROM herotable,maptable,savedgametable " +
                "WHERE herotable.heroID=maptable.heroID " +
                "AND maptable.mapID=savedgametable.mapID " +
                "AND savedgametable.heroCurrentRow=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, hero.getCoordinateX());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                heroStartRow = resultSet.getInt("heroRow");
            }
        }
        return heroStartRow;
    }

    private int heroStartColumn;

    /**
     * Loads the saved starting column coordinate of a hero from the database.
     * <p>
     * This method retrieves the heroColumn from the 'herotable', 'maptable', and 'savedgametable'
     * by matching the heroID and mapID based on the given Hero object.
     *
     * @param hero The Hero object for which the saved starting column coordinate is to be loaded.
     * @return The saved starting column coordinate of the hero.
     * @throws SQLException If a database access error occurs.
     */
    public int loadSavedHeroStartCoordinateYFromDatabase(Hero hero) throws SQLException {
        String query = "SELECT heroColumn " +
                "FROM herotable,maptable,savedgametable " +
                "WHERE herotable.heroID=maptable.heroID " +
                "AND maptable.mapID=savedgametable.mapID " +
                "AND savedgametable.heroCurrentColumn=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,  convertCharToString(hero));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String heroStartColumnAsString = resultSet.getString("heroColumn");
                char heroStartColumnAsChar = heroStartColumnAsString.charAt(0);
                heroStartColumn = heroStartColumnAsChar - 64;
            }
        }
        return heroStartColumn;
    }

    private int userSavedScore;
    /**
     * Retrieves the saved score of a user from the database based on their username.
     * <p>
     * This method executes a SQL query to retrieve the saved score associated with a specific user from the "savedgametable".
     * The query is parameterized with the username to ensure security and prevent SQL injection.
     *
     * @return The saved score of the user, as an integer. If no record is found for the user, the default value is returned.
     * @throws SQLException If a database access error occurs or the SQL execution fails.
     */

    public int loadUserSavedScoreFromDatabase() throws SQLException {
        String query = "SELECT userCurrentScore FROM savedgametable WHERE userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,  MenuVO.getUserName());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userSavedScore = resultSet.getInt("userCurrentScore");
            }
        }
        return userSavedScore;
    }

    /**
     * Reads the map data from the database for a specified map ID.
     *
     * @param mapID The ID of the map to be read from the database.
     * @return The map data as a MapVO object.
     * @throws SQLException If a database access error occurs.
     */
    public MapVO readMapFromDatabase(int mapID) throws SQLException {
        String query = "SELECT map FROM maptable WHERE mapID=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mapID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String mapAsString = resultSet.getString("map");
                mapAsStringToMapVO(mapAsString);
            }
        }
        return readMap;
    }

    /**
     * Converts a string representation of a map to a MapVO object and assigns it to the readMap field.
     *
     * @param mapAsString The string representation of the map.
     */
    public void mapAsStringToMapVO(String mapAsString) {
        String[] splitMap = mapAsString.split("#");

        char[][] newMap = new char[splitMap.length][splitMap.length];

        for (int i = 0; i < splitMap.length; i++) {
            for (int j = 0; j < splitMap.length; j++) {
                newMap[i][j] = splitMap[i].charAt(j);
            }
        }
        readMap = new MapVO(splitMap.length, splitMap.length, newMap);
    }

    /**
     * Reads hero data from the database for a specified map ID.
     *
     * @param mapID The ID of the map for which hero data is to be read from the database.
     * @return The hero data as a Hero object.
     * @throws SQLException If a database access error occurs.
     */
    public Hero readHeroFromDatabase(int mapID) throws SQLException {
        String query = "SELECT heroRow, heroColumn, heroView " +
                "FROM herotable, maptable " +
                "WHERE herotable.heroID=maptable.heroID " +
                "AND maptable.mapID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mapID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int heroRow = resultSet.getInt("heroRow");
                int heroColumn = (resultSet.getString("heroColumn").charAt(0)) - 64;
                String heroView = resultSet.getString("heroView");

                Direction heroDirection = heroDirectionRead(heroView);

                readHero = new Hero(heroRow, heroColumn, heroDirection, 0, false);
            }
        }
        return  readHero;
    }

    /**
     * Converts a string representation of a direction to a Direction enum.
     *
     * @param directionInput The string representation of the direction.
     * @return The corresponding Direction enum value.
     */
    public Direction heroDirectionRead(String directionInput) {
        Direction direction = null;

        while (direction == null) {
            switch (directionInput) {
                case "North":
                    direction = Direction.North;
                    break;
                case "South":
                    direction = Direction.South;
                    break;
                case "West":
                    direction = Direction.West;
                    break;
                case "East":
                    direction = Direction.East;
                    break;
                default:
                    System.out.println("\nHiba! Próbálja újra!\n");
            }
        }
        return direction;
    }

    /**
     * Sends the edited map data to the database.
     *
     * @param map    The edited map data to be saved.
     * @param heroID The ID of the hero associated with the map.
     * @throws SQLException If a database access error occurs.
     */
    public void sendEditedMapToDatabase(MapVO map, int heroID) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO maptable(map, builderName, heroID) VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, convertMap(map));
            preparedStatement.setString(2, MenuVO.getUserName());
            preparedStatement.setInt(3, heroID);

            preparedStatement.executeUpdate();
        }

        statement.close();

    }

    /**
     * Sends the edited hero data to the database.
     *
     * @param hero The edited hero data to be saved.
     * @throws SQLException If a database access error occurs.
     */
    public void sendEditedHeroToDatabase(Hero hero) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO herotable(heroRow, heroColumn, heroView) VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, hero.getCoordinateX());
            preparedStatement.setString(2, convertCharToString(hero));
            preparedStatement.setString(3, convertDirectionToString(hero.getViewingDirection()));

            preparedStatement.executeUpdate();
        }

        statement.close();

    }

    private int heroID;

    /**
     * Retrieves the hero ID from the database based on the hero's data.
     *
     * @param hero The hero for which the ID is to be retrieved.
     * @return The hero ID from the database.
     * @throws SQLException If a database access error occurs.
     */
    public int getHeroIDFromDatabase(Hero hero) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT heroID FROM herotable WHERE heroRow=? AND heroColumn=? AND heroView=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, hero.getCoordinateX());
            preparedStatement.setString(2, convertCharToString(hero));
            preparedStatement.setString(3, convertDirectionToString(hero.getViewingDirection()));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                heroID = resultSet.getInt("heroID");
            }
        }
        statement.close();
        return heroID;
    }
}

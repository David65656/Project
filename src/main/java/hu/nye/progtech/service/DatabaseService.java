package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import org.w3c.dom.ls.LSOutput;

import java.sql.*;
import java.util.Scanner;

public class DatabaseService {
    private final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/wumpusdatabase";
    private final Connection connection = DriverManager.getConnection(jdbcUrl,"root","");
    private MapVO readMap;
    private Hero readHero;
    public DatabaseService() throws SQLException {}
    public void databaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Hol van a MySQL JDBC Driver?");
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(
                jdbcUrl,"root","")) {
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

    public void closeDatabaseConnection() throws SQLException {
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

    public void readAllMapFromDatabaseWithBuilder() throws SQLException {
        int cnt=1;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT map, builderName FROM maptable;");

        while (resultSet.next()) {
            String mapAsString = resultSet.getString("map");
            String builderName = resultSet.getString("builderName");

            String[] splitMap = mapAsString.split("#");
            System.out.println("\n"+cnt+". pálya: ");
            for(int i=0;i<splitMap.length;i++){
                System.out.println(splitMap[i]);
            }
            System.out.print("A készítő: ");
            System.out.println(builderName);
            cnt++;
        }
        statement.close();
    }

    public void deleteUnfinishedGame() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM savedgametable WHERE userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Menu.getUserName());
            if(preparedStatement.executeUpdate()!=0){
                System.out.println("Mentett játék törölve!");
            }
        }

        statement.close();
    }

    public String convertMap(MapVO map){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < map.getRows(); i++) {
            if(i!=0){
                sb.append("#");
            }
            for (int j = 0; j < map.getColumns(); j++) {
                sb.append(map.getMap()[i][j]);
            }
        }
        return  sb.toString();
    }

    public String convertCharToString(Hero hero){
        char[] charArray = {(char)(hero.getCoordinate_y() + 64)};
        return String.valueOf(charArray);
    }

    public String convertDirectionToString(Direction direction){
        if(direction==Direction.North){
            return "North";
        }
        else if (direction==Direction.South) {
            return "South";

        }
        else if (direction==Direction.West) {
            return "West";
        }
        else {
            return "East";
        }
    }

    public int convertBooleanToInt(Boolean bool){
        if(bool){
            return 1;
        }
        else{
            return 0;
        }
    }

    public void printScoreTable() throws SQLException {
        int cnt=1;
        String query = "SELECT userName, score FROM scoretable WHERE mapID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Menu.getMapID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String score = resultSet.getString("score");
                System.out.println("\n"+cnt+". "+userName+": "+score);
                cnt++;
            }
        }
    }

    public void sendPlayerScoreToDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO scoretable VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Menu.getUserName());
            preparedStatement.setInt(2, HeroMovements.getScore());
            preparedStatement.setInt(3, Menu.getMapID());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }

    public void updatePlayerScoreInDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "UPDATE scoretable SET score=? WHERE mapID=? AND userName=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, HeroMovements.getScore());
            preparedStatement.setInt(2, Menu.getMapID());
            preparedStatement.setString(3, Menu.getUserName());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }

    private boolean isUserExists;
    public void isPlayerInScoreTable() throws SQLException {
        String query = "SELECT COUNT(*) > 0 AS user_exists FROM scoretable WHERE userName=?;";
        Statement statement = connection.createStatement();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Menu.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                isUserExists = convertIntToBoolean(resultSet.getInt("user_exists"));
                System.out.println(isUserExists);
            }
        }
        statement.close();
    }

    public Boolean convertIntToBoolean(int n) {
        return n != 0;
    }

    public void updateOrSendPlayerScore() throws SQLException {
        if(isUserExists){
            updatePlayerScoreInDatabase();
        }
        else {
            sendPlayerScoreToDatabase();
        }
    }

    public void sendSavedGameToDatabase(MapVO map, Hero hero) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO savedgametable VALUES (?,?,?,?,?,?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Menu.getUserName());
            preparedStatement.setInt(2, Menu.getMapID());
            preparedStatement.setString(3, convertMap(map));
            preparedStatement.setInt(4, hero.getCoordinate_x());
            preparedStatement.setString(5, convertCharToString(hero));
            preparedStatement.setString(6, convertDirectionToString(hero.getViewingDirection()));
            preparedStatement.setInt(7, convertBooleanToInt(hero.isHaveGold()));
            preparedStatement.setInt(8, hero.getNumberOfArrows());

            preparedStatement.executeUpdate();
        }

        statement.close();
    }

    public MapVO readMapFromDatabase(int ID) throws SQLException {
        String query = "SELECT map FROM maptable WHERE mapID=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String mapAsString = resultSet.getString("map");
                mapAsStringToMapVO(mapAsString);
            }
        }
        return readMap;
    }

    public void mapAsStringToMapVO(String mapAsString){
        String[] splitMap = mapAsString.split("#");

        char[][] newMap = new char[splitMap.length][splitMap.length];

        for(int i=0;i<splitMap.length;i++){
            for (int j = 0; j < splitMap.length; j++) {
                newMap[i][j] = splitMap[i].charAt(j);
            }
        }
        readMap=new MapVO(splitMap.length,splitMap.length,newMap);
    }

    public Hero readHeroFromDatabase(int ID) throws SQLException{
        String query = "SELECT heroRow, heroColumn, heroView FROM herotable, maptable WHERE herotable.heroID=maptable.heroID AND maptable.mapID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int heroRow = resultSet.getInt("heroRow");
                int heroColumn = (resultSet.getString("heroColumn").charAt(0))-64;
                String heroView = resultSet.getString("heroView");

                Direction heroDirection = heroDirectionRead(heroView);

                readHero=new Hero(heroRow,heroColumn,heroDirection,0,false);
            }
        }
        return  readHero;
    }

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
}

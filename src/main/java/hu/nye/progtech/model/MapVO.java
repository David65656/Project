package hu.nye.progtech.model;

/**
 * The `MapVO` class represents a two-dimensional map in a game with information about its dimensions and contents.
 * <p>
 * It provides methods to retrieve information about the map, such as the number of rows and columns, and to print the map.
 */

public class MapVO {
    private final int rows;
    private final int columns;
    private final char[][] map;

    public MapVO(int rows, int columns, char[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.map = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.map[i][j] = map[i][j];
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public char[][] getMap() {
        return map;
    }

    /**
     * Prints the map to the console, displaying row and column indices along with the map content.
     */
    public void mapPrint() {
        System.out.println("A pálya: ");
        System.out.print("        ");
        for (int j = 0; j < columns; j++) {
            System.out.print((char) (j + 65));
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            if (i + 1 > 9) {
                System.out.print(i + 1 + ".sor: ");
            } else {
                System.out.print(i + 1 + ".sor:  ");
            }
            for (int j = 0; j < columns; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}

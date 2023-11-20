package hu.nye.progtech.model;

import java.sql.Array;
import java.util.Arrays;

public class Map {
    final private int rows;
    final private int columns;
    final char[][] map;

    public Map(int rows, int columns, char[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.map = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.map[i][j]=map[i][j];
            }
        }
    }

    public void mapPrint(){
        System.out.println("A pálya: ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}

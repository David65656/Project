package hu.nye.progtech.model;

public class MapVO {
    final private int rows;
    final private int columns;
    char[][] map;

    public MapVO(int rows, int columns, char[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.map = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.map[i][j]=map[i][j];
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

    public void mapPrint(){
        System.out.println("A pálya: ");
        System.out.print("        ");
        for (int j = 0; j < columns; j++) {
            System.out.print((char)(j+65));
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            if(i+1>9){
                System.out.print(i+1+".sor: ");
            }
            else{
                System.out.print(i+1+".sor:  ");
            }
            for (int j = 0; j < columns; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}

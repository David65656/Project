package hu.nye.progtech.service;

import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.Object;
import hu.nye.progtech.model.ObjectType;

import java.util.Scanner;

public class EditMap {

    public EditMap() {
        readSize();
    }

    public void readSize(){
        Scanner scanner = new Scanner(System.in);
        int size=0;
        while(!sizeCheck(size)){
            System.out.println("Adja meg a pálya méretét (6<=N<=20): ");
            size = scanner.nextInt();
            if(sizeCheck(size)){
                MapVO empty = emptyMap(size);
                empty.mapPrint();
                menu(empty);
                scanner.close();
            }
            else{
                System.out.println("Hiba! Próbálja újra!");
            }
        }
    }

    public void menu(MapVO map){
        int choice=0;
        Scanner scanner = new Scanner(System.in);

        while(choice!=3){
            printMenu();
            choice=scanner.nextInt();
            switch (choice){
                case 1:
                    addObject(map,objectRead());
                    map.mapPrint();
                    break;
                case 2:
                    removeObject(map);
                    map.mapPrint();
                    break;
                case 3:
                    System.out.println("Sikeresen kilépett!");
                    break;
                default:
                    System.out.println("Hiba! Próbálja újra!");
            }
        }
    }

    public void printMenu(){
        System.out.println("Válasszon az alábbiak közül: \n" +
                "1. Elem hozzáadása\n" +
                "2. Elem eltávolítása\n" +
                "3. Kilépés\n" +
                "Ön választása: ");
    }

    public boolean sizeCheck(int N){
        return N >= 6 && N <= 20;
    }

    public int wumpusBySize(int N){
        if(N<=8){
            return 1;
        } else if (N<=14) {
            return 2;
        } else {
            return 3;
        }
    }

    public int wumpusCount(MapVO map){
        int cnt=0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++){
                if(map.getMap()[i][j]=='U'){
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public int goldCount(MapVO map){
        int cnt=0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++){
                if(map.getMap()[i][j]=='G'){
                    cnt++;
                }
            }
        }
        return cnt;

    }

    public int heroCount(MapVO map){
        int cnt=0;
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++){
                if(map.getMap()[i][j]=='H'){
                    cnt++;
                }
            }
        }
        return cnt;
    }
    public MapVO emptyMap(int N){
        char[][] newMap = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(i==0 || i==N-1 || j==0 || j==N-1){
                    newMap[i][j] = 'W';
                }
                else {
                    newMap[i][j] = '_';
                }
            }
        }
        return new MapVO(N,N,newMap);
    }

    public Object objectRead(){
        Scanner scanner = new Scanner(System.in);

        ObjectType type = null;

        System.out.println("Adja meg az elem típusát(WALL, PIT, WUMPUS, HERO, GOLD): ");
        while (type==null){
            switch(scanner.nextLine()){
                case "WALL":
                    type = ObjectType.WALL;
                    break;
                case "PIT":
                    type = ObjectType.PIT;
                    break;
                case "WUMPUS":
                    type = ObjectType.WUMPUS;
                    break;
                case "GOLD":
                    type = ObjectType.GOLD;
                    break;
                case "HERO":
                    type = ObjectType.HERO;
                    break;
                default:
                    System.out.println("Hiba! Próbálja újra!");
            }
        }

        System.out.println("Adja meg a sor számát: ");
        int rowNumber = scanner.nextInt();

        System.out.println("Adja meg az oszlop betűjelét: ");
        int columnNumber= (scanner.next().charAt(0)-64);

        return new Object(type,rowNumber,columnNumber);
    }

    public MapVO addObject(MapVO map, Object object){
        char[][] newMap= map.getMap();
            switch (object.getType()){
                case GOLD:
                    newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='G';
                    break;
                case HERO:
                    newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='H';
                    break;
                case WALL:
                    newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='W';
                    break;
                case PIT:
                    newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='P';
                    break;
                case WUMPUS:
                    newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='U';
                    break;
            }
        return new MapVO(map.getRows(), map.getColumns(), newMap);
    }

    public MapVO removeObject(MapVO map){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Adja meg az eltávolítandó elem sorszámát: ");
        int rowNumber = scanner.nextInt();
        System.out.println("Adja meg az eltávolítandó elem oszlopszámát: ");
        int columnNumber= (scanner.next().charAt(0)-64);

        char[][] newMap= map.getMap();
        newMap[rowNumber-1][columnNumber-1]='_';

        return new MapVO(map.getRows(), map.getColumns(), newMap);
    }
}

package hu.nye.progtech.service;

import hu.nye.progtech.model.*;
import hu.nye.progtech.model.Object;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EditMap {

    Hero editedHero;

    public EditMap() throws IOException {
        readSize();
    }

    public void readSize() throws IOException{
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

    public void menu(MapVO map) throws IOException{
        int choice=0;
        Scanner scanner = new Scanner(System.in);

        while(choice!=3){
            printMenu();
            choice=scanner.nextInt();
            switch (choice){
                case 1:
                    addObject(map,objectRead(map));
                    map.mapPrint();
                    break;
                case 2:
                    removeObject(map);
                    map.mapPrint();
                    break;
                case 3:
                    System.out.println("Sikeresen kilépett!");
                    break;
                case 4:
                    if(checkMap(map)){
                        saveMap(map);
                        editedHero.setNumberOfArrows(wumpusCount(map));
                        System.out.println("Sikeresen kilépett! Pálya sikeresen elmentve!\n" +editedHero);
                        choice = 3;
                    }
                    else {
                        System.out.println("Hiba! Pálya mentése nem sikerült!");
                    }
                    break;
                default:
                    System.out.println("Hiba! Próbálja újra!");
            }
        }
    }

    public boolean checkMap(MapVO map){
        if(goldCount(map)!=1){
            System.out.println("\nHiba! Nincs arany a pályán!\n");
            return false;
        }

        if(heroCount(map)!=1){
            System.out.println("\nHiba! Nincs hős a pályán!\n");
            return false;
        }

        if(wumpusCount(map)!=wumpusBySize(map.getRows())){
            System.out.println("\nHiba! Nincs " + wumpusBySize(map.getRows()) + " db wumpusz a pályán!\n");
            return false;
        }

        return true;
    }

    public void saveMap(MapVO map) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedMap.txt"));
            char[][] savedMap = map.getMap();

            for (int i = 0; i < map.getRows(); i++) {
                for (int j = 0; j < map.getColumns(); j++) {
                    writer.write(savedMap[i][j]);
                }
                writer.newLine();
            }
            writer.close();
    }

    public void printMenu(){
        System.out.println("Válasszon az alábbiak közül: \n" +
                "1. Elem hozzáadása\n" +
                "2. Elem eltávolítása\n" +
                "3. Kilépés pálya mentése nélkül\n" +
                "4. Kilépés pálya mentésével\n" +
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

    public Direction heroDirectionRead() {
        Scanner scanner = new Scanner(System.in);

        Direction direction = null;

        System.out.println("Adja meg a hős kezdeti nézési irányát(North, South, West, East): ");
        while (direction == null) {
            switch (scanner.nextLine()) {
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
                    System.out.println("Hiba! Próbálja újra!");
            }

        }
        return direction;
    }


    public Object objectRead(MapVO map){
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

    public boolean checkAddObject(MapVO map, Object object){
        if(object.getCoordinate_x()>map.getRows() || object.getCoordinate_y()> map.getColumns()){
            System.out.println("\nHiba! A pálya "+map.getRows()+"x"+map.getRows()+" méretű!");
            return false;
        }

        if(object.getType()==ObjectType.GOLD && goldCount(map)==1){
            System.out.println("\nHiba! Aranyból csak 1 lehet a pályán!\n");
            return false;
        }

        if(object.getType()==ObjectType.HERO && heroCount(map)==1){
            System.out.println("\nHiba! Hősből csak 1 lehet a pályán!\n");
            return false;
        }

        if(object.getType()==ObjectType.WUMPUS && wumpusCount(map)==wumpusBySize(map.getRows())){
            System.out.println("\nHiba! Wumpuszból csak " + wumpusBySize(map.getRows()) + " lehet a pályán!\n");
            return false;
        }

        if(map.getMap()[object.getCoordinate_y()-1][object.getCoordinate_x()-1]!='_'){
            System.out.println("\nHiba! Az adott helyen van már elem!\n");
            return false;
        }

        return true;
    }

    public MapVO addObject(MapVO map, Object object){
        if(checkAddObject(map,object)){
            char[][] newMap= map.getMap();
                switch (object.getType()){
                    case GOLD:
                        newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='G';
                        break;
                    case HERO:
                        newMap[object.getCoordinate_y()-1][object.getCoordinate_x()-1]='H';

                        Direction direction = heroDirectionRead();
                        int numberOfArrows = wumpusCount(map);
                        editedHero=new Hero(object.getType(), object.getCoordinate_x(), object.getCoordinate_y(), direction, numberOfArrows);
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
        else {
            return map;
        }
    }

    public boolean checkRemoveObject(MapVO map, int rowNumber, int columnNumber){
        if(rowNumber>map.getRows() || columnNumber> map.getColumns()){
            System.out.println("\nHiba! A pálya "+map.getRows()+"x"+map.getRows()+" méretű!");
            return false;
        }

        if(rowNumber==1 || columnNumber == 1 || rowNumber == map.getRows() || columnNumber == map.getColumns()){
            System.out.println("\nHiba! A keret nem távolítható el!\n");
            return false;
        }
        return true;
    }

    public MapVO removeObject(MapVO map){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Adja meg az eltávolítandó elem sorszámát: ");
        int rowNumber = scanner.nextInt();
        System.out.println("Adja meg az eltávolítandó elem oszlopának betűjelét: ");
        int columnNumber= (scanner.next().charAt(0)-64);

        if(checkRemoveObject(map, rowNumber, columnNumber)){
            char[][] newMap= map.getMap();
            newMap[rowNumber-1][columnNumber-1]='_';

            return new MapVO(map.getRows(), map.getColumns(), newMap);
        }
        else {
            return map;
        }
    }
}

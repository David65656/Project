package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Play {
    MapVO playedMap;
    Hero playedHero;
    int heroStartCoordinate_x;
    int heroStartCoordinate_y;
    public Play() throws IOException {
        //EditMap editMap = new EditMap();
        playedHero=new Hero(5,2, Direction.North,1,false);
        int size = 6;
        playedMap=readSavedMap(size);

        heroStartCoordinate_x= playedHero.getCoordinate_x();
        heroStartCoordinate_y= playedHero.getCoordinate_y();

        playMenu();

    }

    public MapVO readSavedMap(int size) throws FileNotFoundException {
        File file = new File("savedMap.txt");
        Scanner scanner = new Scanner(file);

        char[][] scannedMap = new char[size][size];
        for (int i = 0; i < size; i++) {
            String line = scanner.next();
            for (int j = 0; j < size; j++) {
                scannedMap[i][j] = line.charAt(j);
            }
        }
        playedMap = new MapVO(size,size,scannedMap);
        return playedMap;
    }

    public void printMenu(){
        System.out.println("Válasszon az alábbiak közül: \n" +
                "1. Előre lépés\n" +
                "2. Balra fordulás\n" +
                "3. Jobbra fordulás\n" +
                "4. Lövés\n" +
                "5. Arany felszedése\n" +
                "6. Játék elhalasztás\n" +
                "7. Feladás\n" +
                "Ön választása: ");
    }

    public boolean checkHeroPosition(){
        return heroStartCoordinate_x == playedHero.getCoordinate_x() && heroStartCoordinate_y == playedHero.getCoordinate_y();
    }

    public void playMenu() {
        Scanner scanner = new Scanner(System.in);
        HeroMovements move = new HeroMovements();
        int choice = 0;

        while (choice != 7) {
            if(checkHeroPosition() && playedHero.isHaveGold()){
                System.out.println("\nGratulálok! Megnyerted a játékot!\n");
                choice=7;
            }
            else{
                playedMap.mapPrint();
                System.out.println(playedHero);
                printMenu();
                choice = scanner.nextInt();
            }
                switch (choice) {
                    case 1:
                        playedHero=move.step(playedHero,playedMap);
                        playedMap=move.drawStepOnMap(playedMap, playedHero.getCoordinate_x(), playedHero.getCoordinate_y(),playedHero);
                        break;
                    case 2:
                        move.turnLeft(playedHero);
                        break;
                    case 3:
                        move.turnRight(playedHero);
                        break;
                    case 4:
                        playedMap=move.shoot(playedHero, playedMap);
                        break;
                    case 5:
                        move.pickupGold(playedHero, playedMap);
                        break;
                    case 6:
                        System.out.println("Sikeresen kilépett! Pálya sikeresen elmentve!\n");
                        break;
                    case 7:
                        System.out.println("Sikeresen kilépett!");
                        break;
                    default:
                        System.out.println("Hiba! Próbálja újra!");
                }
        }
    }



}

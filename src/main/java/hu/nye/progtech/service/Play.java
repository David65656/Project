package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Play {
    private MapVO playedMap;
    private Hero playedHero;
    private final int heroStartCoordinate_x;
    private final int heroStartCoordinate_y;
    public Play(MapVO playedMap, Hero playedHero) throws SQLException, IOException {
        this.playedMap = playedMap;
        this.playedHero = playedHero;

        heroStartCoordinate_x = playedHero.getCoordinate_x();
        heroStartCoordinate_y = playedHero.getCoordinate_y();
        setHeroOnMap();
        setHeroArrowNumber();

        playMenu();
    }

    public void setHeroOnMap(){
        for (int i = 0; i < playedMap.getRows(); i++) {
            for (int j = 0; j < playedMap.getColumns(); j++) {
                if((i == playedHero.getCoordinate_x()-1) && (j == playedHero.getCoordinate_y()-1)){
                    playedMap.getMap()[i][j]='H';
                }
            }
        }
    }

    public void setHeroArrowNumber(){
        int cnt=0;
        for (int i = 0; i < playedMap.getRows(); i++) {
            for (int j = 0; j < playedMap.getColumns(); j++) {
                if(playedMap.getMap()[i][j]=='U'){
                    cnt++;
                }
            }
        }
        playedHero.setNumberOfArrows(cnt);
    }

    public void printMenu(){
        System.out.println("\nVálasszon az alábbiak közül: \n" +
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

    public void playMenu() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        HeroMovements move = new HeroMovements();
        int choice = 0;
        int cnt = 0;

        while (choice != 7) {
            if(checkHeroPosition() && playedHero.isHaveGold()){
                System.out.println("\nGratulálok! Megnyerted a játékot! A játék során megtett lépések száma: "+cnt+"\nPontszámod: "+move.getScore()+"\n");
                DatabaseService database = new DatabaseService();
                database.databaseConnection();
                database.isPlayerInScoreTable();
                database.updateOrSendPlayerScore();
                database.printScoreTable();
                database.closeDatabaseConnection();
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
                        cnt++;
                        break;
                    case 2:
                        move.turnLeft(playedHero);
                        break;
                    case 3:
                        move.turnRight(playedHero);
                        break;
                    case 4:
                        if(playedHero.getNumberOfArrows()==0){
                            System.out.println("\nNincs nyilad, ezért nem tudsz lőni!\n");
                        }
                        else{
                            playedMap=move.shoot(playedHero, playedMap);
                        }
                        break;
                    case 5:
                        move.pickupGold(playedHero, playedMap);
                        break;
                    case 6:
                        DatabaseService database = new DatabaseService();
                        database.databaseConnection();
                        database.sendSavedGameToDatabase(playedMap,playedHero);
                        System.out.println("\nSikeresen kilépett! Pálya sikeresen elmentve!\n");
                        database.closeDatabaseConnection();
                        choice=7;
                        new Menu().printMenu();
                        break;
                    case 7:
                        System.out.println("\nSikeresen kilépett!\n");
                        new Menu().printMenu();
                        break;
                    default:
                        System.out.println("\nHiba! Próbálja újra!\n");
                }
        }
    }



}

package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.ObjectType;

import java.io.*;
import java.util.Scanner;

public class ReadMap {

    private final MapVO inputMapVO;
    public ReadMap() throws IOException {
        File file = new File("wumpluszinput.txt");
        Scanner scanner = new Scanner(file);

        int cnt=0;
        Direction heroDirection = null;

        int N = scanner.nextInt();
        char column = scanner.next().charAt(0);
        int heroColumn = column-64;
        int heroRow = scanner.nextInt();

        char direction = scanner.next().charAt(0);
        if(direction == 'N'){
            heroDirection = Direction.North;
        } else if (direction == 'S') {
            heroDirection = Direction.South;
        } else if (direction == 'E'){
            heroDirection = Direction.East;
        } else{
            heroDirection = Direction.West;
        }

        char[][] scannedMap = new char[N][N];
        for (int i = 0; i < N; i++) {
            String line = scanner.next();
            for (int j = 0; j < N; j++) {
                scannedMap[i][j] = line.charAt(j);
                if(line.charAt(j)=='U'){
                    cnt++;
                }
            }
        }

        Hero hero = new Hero(ObjectType.HERO, heroRow, heroColumn, heroDirection, cnt);
        System.out.println(hero);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if((i == hero.getCoordinate_y()-1) && (j == hero.getCoordinate_x()-1)){
                    if(scannedMap[i][j]!='_'){
                        System.out.println("Hibás koordináta!");
                    }else{
                        scannedMap[i][j]='H';
                    }
                }
            }
        }
        inputMapVO = new MapVO(N,N,scannedMap);
        scanner.close();
    }

    public void printReadMap() {
        inputMapVO.mapPrint();
    }
}

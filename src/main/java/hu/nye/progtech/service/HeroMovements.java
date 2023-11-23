package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.MapVO;
import hu.nye.progtech.model.Object;
import hu.nye.progtech.model.ObjectType;

public class HeroMovements {

    int oldCoordinate_x;
    int oldCoordinate_y;
    Object object;
    public Hero step(Hero hero, MapVO map) {
        int newCoordinate_x=hero.getCoordinate_x();
        int newCoordinate_y=hero.getCoordinate_y();

        oldCoordinate_x=hero.getCoordinate_x();
        oldCoordinate_y=hero.getCoordinate_y();

        if(hero.getViewingDirection()==Direction.North){
            newCoordinate_x=hero.getCoordinate_x()-1;
        }

        else if (hero.getViewingDirection()==Direction.South) {
            newCoordinate_x=hero.getCoordinate_x()+1;

        }

        else if (hero.getViewingDirection()==Direction.West) {
            newCoordinate_y=hero.getCoordinate_y()-1;
        }

        else {
            newCoordinate_y=hero.getCoordinate_y()+1;
        }

        if(checkWalls(map,newCoordinate_x,newCoordinate_y)){
            return new Hero(newCoordinate_x,newCoordinate_y, hero.getViewingDirection(),hero.getNumberOfArrows(),hero.isHaveGold());
        }
        else {
            System.out.println("\nErre a mezőre nem léphetsz!\n");
            return hero;
        }
    }

    public MapVO drawStepOnMap(MapVO map, int newCoordinate_x, int newCoordinate_y, Hero hero){
        char[][] newMap=map.getMap();

        checkWumpusGoldPit(map,newCoordinate_x, newCoordinate_y);
        newMap[oldCoordinate_x-1][oldCoordinate_y-1] = '_';


        if(object.getType()==ObjectType.GOLD && !hero.isHaveGold()){
            newMap[object.getCoordinate_x()][object.getCoordinate_y()]='G';
        }
        else if(object.getType()==ObjectType.PIT){
            newMap[object.getCoordinate_x()][object.getCoordinate_y()]='P';
            if(hero.getNumberOfArrows()>0){
                hero.setNumberOfArrows(hero.getNumberOfArrows()-1);
            }
        }

        newMap[newCoordinate_x-1][newCoordinate_y-1]='H';

        return new MapVO(map.getRows(), map.getColumns(), newMap);
    }

    public void checkWumpusGoldPit(MapVO map, int newCoordinate_x, int newCoordinate_y) {
        if (map.getMap()[newCoordinate_x - 1][newCoordinate_y - 1] == 'U') {
            System.out.println("\nWumpuszra léptél és meghaltál!\n");
        } else if (map.getMap()[newCoordinate_x - 1][newCoordinate_y - 1] == 'P') {
            object=new Object(ObjectType.PIT,newCoordinate_x - 1,newCoordinate_y - 1);
            System.out.println("\nVeremre léptél! Elvesztettél egy nyilat!\n");
        } else if (map.getMap()[newCoordinate_x - 1][newCoordinate_y - 1] == 'G') {
            object=new Object(ObjectType.GOLD,newCoordinate_x - 1,newCoordinate_y - 1);
            System.out.println("\nArany mezőre léptél! Felszedheted az aranyat!\n");
        }
    }


    public boolean checkWalls(MapVO map, int newCoordinate_x, int newCoordinate_y){
        return map.getMap()[newCoordinate_x - 1][newCoordinate_y - 1] != 'W';
    }

    public void turnRight(Hero hero){
        if(hero.getViewingDirection()==Direction.North){
            hero.setViewingDirection(Direction.East);
        }

        else if (hero.getViewingDirection()==Direction.South) {
            hero.setViewingDirection(Direction.West);
        }

        else if (hero.getViewingDirection()==Direction.West) {
            hero.setViewingDirection(Direction.North);
        }

        else {
            hero.setViewingDirection(Direction.South);
        }
    }
    public void turnLeft(Hero hero){
        if(hero.getViewingDirection()==Direction.North){
            hero.setViewingDirection(Direction.West);
        }

        else if (hero.getViewingDirection()==Direction.South) {
            hero.setViewingDirection(Direction.East);
        }

        else if (hero.getViewingDirection()==Direction.West) {
            hero.setViewingDirection(Direction.South);
        }

        else {
            hero.setViewingDirection(Direction.North);
        }
    }

    public void shoot(Hero hero, MapVO map){

    }

    public void pickupGold(Hero hero, MapVO map){
        if(map.getMap()[hero.getCoordinate_x()-1][hero.getCoordinate_y()-1]==
                map.getMap()[object.getCoordinate_x()][object.getCoordinate_y()]){
            System.out.println("\nArany felvéve!\n");
            hero.setHaveGold(true);
        }
        else{
            System.out.println("\nNem arany mezőn vagy!\n");
        }
    }
}

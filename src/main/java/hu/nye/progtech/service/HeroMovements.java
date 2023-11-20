package hu.nye.progtech.service;

import hu.nye.progtech.model.Direction;
import hu.nye.progtech.model.Hero;
import hu.nye.progtech.model.ObjectType;

public class HeroMovements extends Hero{

    private HeroMovements(ObjectType type, int coordinate_x, int coordinate_y, Direction viewingDirection, int numberOfArrows) {
        super(type, coordinate_x, coordinate_y, viewingDirection, numberOfArrows);
    }
    public void step(){}
    public void turnRight(){}
    public void turnLeft(){}
    public void shoot(){}
    public void pickupGold(){}
}

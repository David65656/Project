package hu.nye.progtech.model;

public class Hero extends Object{

    private Direction viewingDirection;
    private int numberOfArrows;

    public Hero(ObjectType type, int coordinate_x, int coordinate_y, Direction viewingDirection, int numberOfArrows) {
        super(coordinate_x, coordinate_y);
        this.viewingDirection = viewingDirection;
        this.numberOfArrows = numberOfArrows;
    }

    public Direction getViewingDirection() {
        return viewingDirection;
    }

    public void setViewingDirection(Direction viewingDirection) {
        this.viewingDirection = viewingDirection;
    }

    public int getNumberOfArrows() {
        return numberOfArrows;
    }

    public void setNumberOfArrows(int numberOfArrows) {
        this.numberOfArrows = numberOfArrows;
    }

    @Override
    public String toString() {
        return "A hős a(z) " + viewingDirection + " irányba néz"+
                ", nyilainak száma: " + numberOfArrows + ", "+
                coordinate_x + " számú sorban és " +coordinate_y+" számú oszlopban tartózkodik!";
    }
}

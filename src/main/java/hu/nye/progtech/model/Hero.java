package hu.nye.progtech.model;

public class Hero extends Object{

    private Direction viewingDirection;
    private int numberOfArrows;

    private boolean haveGold;

    public Hero(ObjectType type, int coordinate_x, int coordinate_y, Direction viewingDirection, int numberOfArrows) {
        super(coordinate_x, coordinate_y);
        this.viewingDirection = viewingDirection;
        this.numberOfArrows = numberOfArrows;
        this.haveGold=false;
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

    public boolean isHaveGold() {
        return haveGold;
    }

    public void setHaveGold(boolean haveGold) {
        this.haveGold = haveGold;
    }

    @Override
    public String toString() {
        if(isHaveGold()==false){
            return "A hős a(z) " + viewingDirection + " irányba néz"+
                    ", nyilainak száma: " + numberOfArrows + ", "+
                    coordinate_y + " számú sorban és " +(char)(coordinate_x +64) +" jelű oszlopban tartózkodik!"+
                    " Nincs nála arany!";
        }
        else {
            return "A hős a(z) " + viewingDirection + " irányba néz"+
                    ", nyilainak száma: " + numberOfArrows + ", "+
                    coordinate_y + " számú sorban és " +(char)(coordinate_x +64) +" jelű oszlopban tartózkodik!"+
                    " Van nála arany!";
        }

    }
}

package hu.nye.progtech.model;

/**
 * The `Hero` class represents a character in the game with specific attributes such as
 * viewing direction, number of arrows, and possession of gold.
 * <p>
 * It extends the base `Object` class and is designed for use in a game environment.
 */

public class Hero extends Object {
    private Direction viewingDirection;
    private int numberOfArrows;
    private boolean haveGold;

    public Hero(int coordinateX, int coordinateY, Direction viewingDirection, int numberOfArrows, boolean haveGold) {
        super(ObjectType.HERO, coordinateX, coordinateY);
        this.viewingDirection = viewingDirection;
        this.numberOfArrows = numberOfArrows;
        this.haveGold = haveGold;
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
        if (!isHaveGold()) {
            return "A hős a(z) " + viewingDirection + " irányba néz" +
                    ", nyilainak száma: " + numberOfArrows + ", " +
                    coordinateX + " számú sorban és " + (char) (coordinateY + 64) + " jelű oszlopban tartózkodik!" +
                    " Nincs nála arany!";
        } else {
            return "A hős a(z) " + viewingDirection + " irányba néz" +
                    ", nyilainak száma: " + numberOfArrows + ", " +
                    coordinateX + " számú sorban és " + (char) (coordinateY + 64) + " jelű oszlopban tartózkodik!" +
                    " Van nála arany!";
        }

    }
}

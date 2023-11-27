package hu.nye.progtech.model;

/**
 * The `Object` class represents a generic object in a game with information about its type and coordinates.
 * <p>
 * It serves as a base class for more specialized objects in the game environment.
 */

public class Object {
    private final ObjectType type;
    protected int coordinateX;
    protected int coordinateY;

    public Object(ObjectType type, int coordinateX, int coordinateY) {
        this.type = type;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public ObjectType getType() {
        return type;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    @Override
    public String toString() {
        return "A létrehozott objektum típusa: " + type + "!" +
                " Az objektum a(z) " + coordinateY +
                " számú sorban és a(z) " + (char) (coordinateX + 64) +
                " jelű oszlopban található!";
    }
}

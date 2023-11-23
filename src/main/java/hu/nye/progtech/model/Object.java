package hu.nye.progtech.model;

public class Object {
    private final ObjectType type;
    protected int coordinate_y;
    protected int coordinate_x;

    public Object(ObjectType type, int coordinate_x, int coordinate_y) {
        this.type = type;
        this.coordinate_y = coordinate_y;
        this.coordinate_x = coordinate_x;
    }

    public ObjectType getType() {
        return type;
    }

    public int getCoordinate_x() {
        return coordinate_x;
    }

    public void setCoordinate_y(int coordinate_y) {
        this.coordinate_y = coordinate_y;
    }

    public int getCoordinate_y() {
        return coordinate_y;
    }

    public void setCoordinate_x(int coordinate_x) {
        this.coordinate_x = coordinate_x;
    }

    @Override
    public String toString() {
        return "A létrehozott objektum típusa: "+type+"!"+
                " Az objektum a(z) "+coordinate_y+
                " számú sorban és a(z) "+(char)(coordinate_x +64)+
                " jelű oszlopban található!";
    }
}

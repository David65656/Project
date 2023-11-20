package hu.nye.progtech.model;

public class Object {
    private final ObjectType type;
    protected int coordinate_x;
    protected int coordinate_y;

    public Object(ObjectType type, int coordinate_x, int coordinate_y) {
        this.type = type;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
    }

    protected Object(int coordinate_x, int coordinate_y) {
        this.type = ObjectType.HERO;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
    }

    public ObjectType getType() {
        return type;
    }

    public int getCoordinate_x() {
        return coordinate_x;
    }

    public void setCoordinate_x(int coordinate_x) {
        this.coordinate_x = coordinate_x;
    }

    public int getCoordinate_y() {
        return coordinate_y;
    }

    public void setCoordinate_y(int coordinate_y) {
        this.coordinate_y = coordinate_y;
    }
}

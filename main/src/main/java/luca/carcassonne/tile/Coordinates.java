package luca.carcassonne.tile;

// The tile's coordinate system
public class Coordinates implements Cloneable {
    private Integer x;
    private Integer y;

    public Coordinates(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Coordinates)) {
            return false;
        }
        Coordinates c = (Coordinates) o;
        return c.getX() == x && c.getY() == y;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Coordinates newCoordinates = (Coordinates) super.clone();
        newCoordinates.setX(x);
        newCoordinates.setY(y);

        return newCoordinates;
    }
}

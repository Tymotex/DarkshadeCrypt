package unsw.dungeon;

public class Coordinate {
    protected int x;
    protected int y;

    /**
     * Constructor for coordinate data class
     * @param x
     * @param y
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object otherObj) {
        if (otherObj.getClass() == Coordinate.class) {
            Coordinate otherCoord = (Coordinate) otherObj;
            if (otherCoord.x == this.x && otherCoord.y == this.y) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
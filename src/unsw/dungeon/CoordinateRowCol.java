package unsw.dungeon;

public class CoordinateRowCol {
    int row;
    int col;

    /**
     * Convenience data class alternative to Coordinate
     * @param row
     * @param col
     */
    public CoordinateRowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
    
}
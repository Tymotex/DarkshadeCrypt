package unsw.dungeon;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class FleePathFinder implements PathFinding {
    private int[][] obstructionMatrix;
    private int height;
    private int width;
    private Stack<Coordinate> pathToDest = new Stack<Coordinate>();

    /**
     * Constructor for FleePathFinder
     */
    public FleePathFinder() {}

    /**
     * Constructor for the FleePathFinder class.
     * @param obstructionMatrix - the obstructions in the dungeon grid
     */
    public FleePathFinder(int obstructionMatrix[][]) {
        this.obstructionMatrix = obstructionMatrix;
        this.height = obstructionMatrix.length;
        this.width = obstructionMatrix[0].length;
        this.pathToDest = new Stack<Coordinate>();
    }

    /**
     * Check whether given cell (row, col)  is a valid cell or not. Return true if row number and column number is in range 
     * @param row
     * @param col
     * @return true if the cell at the row and column is valid
     */
    private boolean isValid(int row, int col) {
        return (row >= 0) && (row < height) && (col >= 0) && (col < width); 
    } 
    
    /**
     * Function to find the shortest path between a given source cell to a destination cell. 
     * This fills out the pathToDest stack of coordinates to get to
     * @param src
     * @param dest
     * @return whether a path exists
     */
    private boolean findPath(CoordinateRowCol src, CoordinateRowCol dest) {     
        boolean visited[][] = new boolean[height][width]; 
        CoordinateRowCol pred[][] = new CoordinateRowCol[height][width];
        
        visited[src.row][src.col] = true; 
        // These arrays are used to get row and column numbers of 4 neighbours of a given cell 
        int rowNum[] = {-1, 0, 0, 1}; 
        int colNum[] = {0, -1, 1, 0}; 
    
        Queue<CoordinateRowCol> q = new LinkedList<CoordinateRowCol>(); 
        q.add(src);
    
        while (!q.isEmpty()) { 
            CoordinateRowCol curr = q.peek(); 
            // Reached dest
            if (curr.row == dest.row && curr.col == dest.col) {
                tracePath(src, dest, pred);
                return true; 
            }
            q.remove(); 
            for (int i = 0; i < 4; i++) { 
                int adjRow = curr.row + rowNum[i]; 
                int adjCol = curr.col + colNum[i]; 
                CoordinateRowCol adjCell = new CoordinateRowCol(adjRow, adjCol); 
                // If adjacent cell is valid, has path and not visited yet, enqueue it. 
                if (isValid(adjRow, adjCol) && obstructionMatrix[adjRow][adjCol] == 1 && !visited[adjRow][adjCol]) { 
                    visited[adjRow][adjCol] = true; 
                    q.add(adjCell);
                    pred[adjRow][adjCol] = curr;
                } 
            } 
        } 
        return false; 
    } 

    /**
     * Given a matrix of predecessor coordinates, traces back the path from
     * dest to source to give a full path from src to dest. Fills out the 
     * pathToDest stack
     * @param src
     * @param dest
     * @param pred
     */
    private void tracePath(CoordinateRowCol src, CoordinateRowCol dest, CoordinateRowCol pred[][]) {
        // System.out.println("Tracing path from (" + src.row + ", " + src.col + ") and (" + dest.row + ", " + dest.col + ")");
        CoordinateRowCol curr = new CoordinateRowCol(dest.row, dest.col);
        while (!(curr.row == src.row && curr.col == src.col)) {
            // System.out.print(curr + " <-");
            pathToDest.push(new Coordinate(curr.col, curr.row));
            curr = pred[curr.row][curr.col];
        }
        // System.out.println(src);
    }

    /**
     * Determins a suitable unobstructed path from src to dest and stack of coordinates
     * that will lead from src to dest, step by step 
     * @param src
     * @param dest
     * @return stack of coordinates that will lead from src to dest, step by step
     */
    @Override
    public Stack<Coordinate> getPathToDest(Coordinate src, Coordinate avoidCoord) {
        CoordinateRowCol newSrc = new CoordinateRowCol(src.y, src.x);
        CoordinateRowCol newAvoidCoord = new CoordinateRowCol(avoidCoord.y, avoidCoord.x);
        CoordinateRowCol newDest = getFleeDest(newSrc, newAvoidCoord);
        findPath(newSrc, newDest);
        return pathToDest;
    }

    /**
     * Given the current coordinate and the coordinate to avoid, returns the next
     * best coordinate to move to in order to avoid the target
     * @param src
     * @param avoidCoord
     * @return
     */
    private CoordinateRowCol getFleeDest(CoordinateRowCol src, CoordinateRowCol avoidCoord) {
        int upRow = src.row - 1, downrow = src.row + 1, leftRow = src.col - 1, rightRow = src.col + 1;
        if (avoidCoord.row >= src.row) {
            // Go up
            if (isValid(upRow, src.col) && obstructionMatrix[upRow][src.col] != 0) {
                // No obstruction! Proceeding to flee 1 tile up
                return new CoordinateRowCol(upRow, src.col);
            }
        } 
        if (avoidCoord.row < src.row) {
            // Go down
            if (isValid(downrow, src.col) && obstructionMatrix[downrow][src.col] != 0) {
                // No obstruction! Proceeding to flee 1 tile down
                return new CoordinateRowCol(downrow, src.col);
            }
        } 
        if (avoidCoord.col >= src.col) {
            // Go left
            if (isValid(src.row, leftRow) && obstructionMatrix[src.row][leftRow] != 0) {
                // No obstruction! Proceeding to flee 1 tile up
                return new CoordinateRowCol(src.row, leftRow);
            }
        } 
        if (avoidCoord.col < src.col) {
            // Go right
            if (isValid(src.row, rightRow) && obstructionMatrix[src.row][rightRow] != 0) {
                // No obstruction! Proceeding to flee 1 tile up
                return new CoordinateRowCol(src.row, rightRow);
            }
        }
        // No way to flee. Staying put
        return src;
    }
    
    /**
     * When the dungeon map updates a new src, dest and obstructions, the obstruction matrix
     * needs to be refreshed so that the pathfinding always chases a moving object and changing map
     * @param obstructionMatrix - the matrix of 0s and 1s, with 1 indicating unobstructed
     * paths and 0 indicating obstructions at that cell
     */
    @Override
    public void refreshObstructionMatrix(int[][] obstructionMatrix) {
        this.obstructionMatrix = obstructionMatrix;
        this.height = obstructionMatrix.length;
        this.width = obstructionMatrix[0].length; 
    }
} 

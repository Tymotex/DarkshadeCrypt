package unsw.dungeon;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class ChasingPathFinder implements PathFinding {
    private int[][] obstructionMatrix;
    private int height;
    private int width;
    private Stack<Coordinate> pathToDest;
    private boolean ignoreObstructions = false;
    private Dungeon dungeon;

    /**
     * Constructor for the ChasingPathFinder class.
     * @param obstructionMatrix - the obstructions in the dungeon grid
     */
    public ChasingPathFinder(int obstructionMatrix[][], Dungeon dungeon) {
        this.obstructionMatrix = obstructionMatrix;
        this.height = obstructionMatrix.length;
        this.width = obstructionMatrix[0].length;
        this.pathToDest = new Stack<Coordinate>();
        this.dungeon = dungeon;
    }

    public ChasingPathFinder(int obstructionMatrix[][], Dungeon dungeon, boolean ignoreObstructions) {
        this(obstructionMatrix, dungeon);
        this.ignoreObstructions = ignoreObstructions;
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
        
        // Debug.printC("Received src: " + src + " dest: " + dest);

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
                if (isValid(adjRow, adjCol) && (obstructionMatrix[adjRow][adjCol] == 1 || (obstructionMatrix[adjRow][adjCol] == 0 && ignoreObstructions)) && !visited[adjRow][adjCol]) { 
                    // Debug.printC("PATH: Investingating path from (" + curr.col + ", " + curr.row + ") to dest (" + adjCol + ", " + adjRow + ")", Debug.YELLOW);
                    visited[adjRow][adjCol] = true; 
                    q.add(adjCell);
                    pred[adjRow][adjCol] = curr;
                } else if (isValid(adjRow, adjCol) && obstructionMatrix[adjRow][adjCol] == 2 && !visited[adjRow][adjCol]) { 
                    // If adjacent cell is valid, IS A PORTAL and not visited yet, enqueue it. 
                    visited[adjRow][adjCol] = true;  // Entry portal is visited
                    pred[adjRow][adjCol] = curr;     // Entry portal pred is this tile
                    Portal portal = dungeon.getEntity(adjCol, adjRow, Portal.class);
                    Portal exitPortal = portal.getPortalDestination();      
                    Coordinate exitPortalCoordinate = new Coordinate(exitPortal.getX(), exitPortal.getY()); // The tile the exit portal is at
                    Coordinate portalDestination = portal.getDestinationCoordinates(); // The tile the exit portal spits you out at
                    visited[exitPortalCoordinate.y][exitPortalCoordinate.x] = true;   // Marking the exit portal as visited
                    
                    if (portalDestination == null)
                        continue;

                    visited[portalDestination.y][portalDestination.x] = true;         // Marking the tile we land on upon entering the portal as visited
                    pred[portalDestination.y][portalDestination.x] = new CoordinateRowCol(adjRow, adjCol); 
                    q.add(new CoordinateRowCol(portalDestination.y, portalDestination.x));
                    // Debug.printC("PORTAL: Investigating path from (" + curr.col + ", " + curr.row + ") to dest " + portalDestination, Debug.YELLOW);

                } 
            }
        } 
        // When there's no path, just travel to an unobstructed random adjacent coordinate
        randomWander(new Coordinate(src.col, src.row));
        return false; 
    } 

    private void randomWander(Coordinate src) {
        Random random = new Random();
        Coordinate randomDest;
        if (random.nextInt(2) == 0) {
            randomDest = new Coordinate(src.y, src.x + random.nextInt(3) - 1);
        } else {
            randomDest = new Coordinate(src.y + random.nextInt(3) - 1, src.x);
        }
        pathToDest.push(randomDest);
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
        CoordinateRowCol curr = new CoordinateRowCol(dest.row, dest.col);
        while (!(curr.row == src.row && curr.col == src.col)) {
            Coordinate next = new Coordinate(curr.col, curr.row);
            // Debug.printC("Pushing to path: " + next);
            pathToDest.push(next);
            curr = pred[curr.row][curr.col];
        }
    }

    /**
     * Determins a suitable unobstructed path from src to dest and stack of coordinates
     * that will lead from src to dest, step by step 
     * @param src
     * @param dest
     * @return stack of coordinates that will lead from src to dest, step by step
     */
    @Override
    public Stack<Coordinate> getPathToDest(Coordinate src, Coordinate dest) {
        CoordinateRowCol newSrc = new CoordinateRowCol(src.y, src.x);
        CoordinateRowCol newDest = new CoordinateRowCol(dest.y, dest.x);
        findPath(newSrc, newDest);
        return pathToDest;
    }

    /**
     * When the dungeon map updates a new src, dest and obstructions, the obstruction matrix
     * needs to be refreshed so that the pathfinding always chases a moving object and changing map
     * @param obstructionMatrix - the matrix of 0s and 1s, with 1 indicating unobstructed
     * paths and 0 indicating obstructions at that cell
     */
    @Override
    public void refreshObstructionMatrix(int obstructionMatrix[][]) {
        this.obstructionMatrix = obstructionMatrix;
        this.height = obstructionMatrix.length;
        this.width = obstructionMatrix[0].length;
    }
} 

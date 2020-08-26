package unsw.dungeon;

import java.util.Stack;

public interface PathFinding {
    /**
     * Determines a suitable unobstructed path from src to dest and stack of coordinates
     * that will lead from src to dest, step by step 
     * @param src
     * @param dest
     * @return stack of coordinates that will lead from src to dest, step by step
     */
    public Stack<Coordinate> getPathToDest(Coordinate src, Coordinate dest);

    /**
     * When the dungeon map updates a new src, dest and obstructions, the obstruction matrix
     * needs to be refreshed so that the pathfinding always chases a moving object and changing map
     * @param obstructionMatrix - the matrix of 0s and 1s, with 1 indicating unobstructed
     * paths and 0 indicating obstructions at that cell
     */
    public void refreshObstructionMatrix(int obstructionMatrix[][]);
}

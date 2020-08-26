package unsw.dungeon;

/**
 * Used to check for obstructions by interactionHandler
 */
public interface Obstruction {
    /**
     * 
     * @return false if entity cannot move ontop of this object instance 
     */
    public boolean canPass();
}

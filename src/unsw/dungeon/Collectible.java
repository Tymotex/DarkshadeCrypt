package unsw.dungeon;

public interface Collectible {
    /**
     * Handles the picking up of entities from the dungeon map
     * @param p
     */
    public void pickup(Player p);
}
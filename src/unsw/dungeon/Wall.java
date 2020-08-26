package unsw.dungeon;

public class Wall extends Entity implements Obstruction {

    /**
     * Constructor for wall
     * @param x
     * @param y
     * @param dungeon
     */
    public Wall(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    /**
     * Can never walk pass a wall
     * @return false
     */
    @Override
    public boolean canPass() {
        
        return false;
    }
    
}

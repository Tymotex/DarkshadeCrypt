package unsw.dungeon;

public class Wisp extends Enemy {
    /**
     * Constructor for Skeleton
     * 
     * @param x
     * @param y
     * @param dungeon
     */
    public Wisp(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        setinteractionHandler(new GhostInteractionHandler());
        setSpeed(800);
    }

    @Override
    public void refreshObstructionMatrix() {
        int obstruct[][] = new int[dungeon.getHeight()][dungeon.getWidth()];
        for (int row = 0; row < dungeon.getHeight(); row++) {
            for (int col = 0; col < dungeon.getWidth(); col++) {
                obstruct[row][col] = 1;
            }
        }

        for (Entity currEntity : dungeon.getEntities()) {
            // Unblocked portal:
            if (currEntity instanceof Portal) {
                obstruct[currEntity.getY()][currEntity.getX()] = 0;
            }
        }

        getPathfinding().refreshObstructionMatrix(obstruct);
        // dungeon.showMatrix();
    }

    @Override
    public void playFootstep() {}
}
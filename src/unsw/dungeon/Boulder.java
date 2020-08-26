package unsw.dungeon;

public class Boulder extends Entity implements Obstruction {

    private InteractionHandler interactionHandler = new BoulderInteractionHandler();

    public Boulder(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }
    
    /**
     * Helper for canPush method which does the actual checking
     * @param x
     * @param y
     * @param stepDirection
     * @return
     */
    private boolean checkCanPush(int x, int y, String stepDirection) {
        if (dungeon.hasObstruction(x, y)) {
            Obstruction obs = dungeon.getObstruction(x, y);
            return obs.canPass();
        } else {
            return true;
        }
    }
    
    /**
     * This method checks whether this instance of the class can be pushed to that specific direction
     * 
     * @param stepDirection
     * @return
     */
    public boolean canPush(String stepDirection) {
        switch (stepDirection) {
            case InteractionHandler.UP:
                return checkCanPush(getX(), getY() - 1, stepDirection);
            case InteractionHandler.DOWN:
                return checkCanPush(getX(), getY() + 1, stepDirection);
            case InteractionHandler.LEFT:
                return checkCanPush(getX() - 1, getY(), stepDirection);
            case InteractionHandler.RIGHT:
                return checkCanPush(getX() + 1, getY(), stepDirection);
            default:
                Debug.printC("Unknown step direction", Debug.RED);
                return true;
        }
    }

    /**
     * Method checks for whether there is a floor switch below the instance of this class (boulder) and toggles the switch
     */
    public void toggleFloorSwitchIfBelow() {
        if (dungeon.hasEntityOfType(getX(), getY(), FloorSwitch.class)) {
            FloorSwitch floorSwitch = (FloorSwitch) dungeon.getEntity(getX(), getY(), FloorSwitch.class);
            floorSwitch.toggle();
        }
    }

    /**
     * Wrapper method for Interactionhandler
     * 
     * @param targetCoord
     * @param stepDirection
     */
    private void handleMove(Coordinate targetCoord, String stepDirection) {
        targetCoord = interactionHandler.handlePortal(targetCoord, dungeon);
        if (targetCoord == null) return;
        if (canPush(stepDirection)) {
            SoundPlayer.playSFX("boulder_push.wav");
            toggleFloorSwitchIfBelow();
        }
        if (interactionHandler.handleInteraction(this, targetCoord, dungeon, stepDirection)) {
            this.setCoordinate(targetCoord);
        }
        // showDetails();
    }

    /**
     * Method tries to moves this instance of the class boulder up from its current position
     */
    public void moveUp() {
        Coordinate upCoord = new Coordinate(getX(), getY() - 1);
        handleMove(upCoord, InteractionHandler.UP);
    }

    /**
     * Method tries to moves this instance of the class boulder down from its current position
     * 
     */
    public void moveDown() {
        Coordinate downCoord = new Coordinate(getX(), getY() + 1);
        handleMove(downCoord, InteractionHandler.DOWN);
    }

    /**
     * Method tries to moves this instance of the class boulder left from its current position
     */
    public void moveLeft() {
        Coordinate leftCoord = new Coordinate(getX() - 1, getY());
        handleMove(leftCoord, InteractionHandler.LEFT);
    }

    /**
     * Method tries to moves this instance of the class boulder right from its current position
     */
    public void moveRight() {
        Coordinate rightCoord = new Coordinate(getX() + 1, getY());
        handleMove(rightCoord, InteractionHandler.RIGHT);
    }

    /**
     * Boulder behaves like a wall
     */
    @Override
    public boolean canPass() {
        return false;
    }
}
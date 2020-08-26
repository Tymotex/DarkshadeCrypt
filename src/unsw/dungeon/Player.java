package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The player entity
 */
public class Player extends Entity {
    private InteractionHandler interactionHandler;
    private Usable currItem;
    private BooleanProperty isAlive = new SimpleBooleanProperty(true);
    private boolean isInvincible = false;

    private StringProperty durabilityText;
    private Inventory inventory = new Inventory();

    public void addToInventory(Usable newItem) {
        inventory.addToInventory(newItem);
    }

    /**
     * Selected the next item on the inventory hotbar
     * as the currently selected item
     */
    public void selectNextItem() {
        Usable nextItem = inventory.getNextItem();
        if (nextItem != null) {
            nextItem.setPlayerImage(this);
            setCurrItem(nextItem);
        }
    }

    /**
     * Selected the previous item on the inventory hotbar
     * as the currently selected item
     */
    public void selectPrevItem() {
        Usable prevItem = inventory.getPreviousItem();
        if (prevItem != null) {
            prevItem.setPlayerImage(this);
            setCurrItem(prevItem);
        }
    }

    /**
     * Removes the currently selected item from the hotbar
     */
    public void removeCurrItem() {
        if (currItem != null) {
            currItem.remove();
            currItem = inventory.removeCurrItemFromInventory();
            setCurrItem(inventory.getCurrentItem());
        }
    }

    /**
     * After the current item is used, this method updates
     * the durability string text
     */
    private void updateDurabilityString() {
        if (currItem != null) {
            this.durabilityText.set(this.currItem.getDescription());
        } else {
            this.durabilityText.set("");
        }
    }
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        this.interactionHandler = new PlayerInteractionHandler();
        this.durabilityText = new SimpleStringProperty();
    }

    /**
     * Uses the current item if the target is appropriate
     * @param target
     */
    public void useItem(Entity target) {
        if (currItem != null) {
            if (target instanceof Enemy) {
                if (currItem instanceof Weapon) {
                    // Use the weapon and remove it if it breaks
                    if (!currItem.useItem(target)) {
                        removeCurrItem();
                    }
                } else {
                    die();
                }
            } else if (target instanceof Door) {
                if (!currItem.useItem(target)) {
                    removeCurrItem();
                }
            }
        } else {
            if (target instanceof Enemy){
                die();
            }
        }
        updateDurabilityString();
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
        if (interactionHandler.handleInteraction(this, targetCoord, dungeon, stepDirection)) {
            this.setCoordinate(targetCoord);
        }
        // showDetails();
    }

    /**
     * Method tries to moves this instance of the class player up from its current position
     */
    public void moveUp() {
        Coordinate upCoord = new Coordinate(getX(), getY() - 1);
        handleMove(upCoord, InteractionHandler.UP);
    }

    /**
     * Method tries to moves this instance of the class player down from its current position
     */
    public void moveDown() {
        Coordinate downCoord = new Coordinate(getX(), getY() + 1);
        handleMove(downCoord, InteractionHandler.DOWN);
    }

    /**
     * Method tries to moves this instance of the class player left from its current position
     */
    public void moveLeft() {
        Coordinate leftCoord = new Coordinate(getX() - 1, getY());
        handleMove(leftCoord, InteractionHandler.LEFT);
    }

    /**
     * Method tries to moves this instance of the class player right from its current position
     */
    public void moveRight() {
        Coordinate rightCoord = new Coordinate(getX() + 1, getY());
        handleMove(rightCoord, InteractionHandler.RIGHT);
    }

    @Override
    public void showDetails() {
        Debug.printC(this.getClass().getSimpleName() + " at (" + getX() + ", " + getY() + ")", Debug.BLUE);
    }

    @Override
    public void die() {
        SoundPlayer.playSFX("hurt.wav");
        isAlive.set(false);
        super.die();
    }

    /**
     * Returns whether this player is still alive
     * @return
     */
    public boolean isAlive() {
        return isAlive.get();
    }

    /**
     * Method for binding BooleanProperty isAlive
     */
    public BooleanProperty getIsAlive() {
        return isAlive;
    }

    public StringProperty getDurabilityString() {
        return this.durabilityText;
    }

    public Usable getCurrItem() {
        return currItem;
    }

    /**
     * Setter for the current item. 
     * @param currItem
     */
    public void setCurrItem(Usable currItem) {
        // Stop indicating the door for the current key
        if (this.getCurrItem() instanceof Key) {
            Key key = (Key) this.getCurrItem();
            key.isBeingHeld.set(false);
        }
        if (currItem != null) {
            currItem.getImageViewIcon().setEffect(null);
            currItem.setPlayerImage(this);
            this.currItem = currItem;
            // Holding the key should indicate the corresponding door it unlocks
            if (currItem instanceof Key) {
                Key key = (Key) currItem;
                key.isBeingHeld.set(true);
            }
        } else {
            this.updateImage("player/player.png");
        }
        updateDurabilityString();
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public StringProperty getDurabilityText() {
        return durabilityText;
    }

    public void setDurabilityText(StringProperty durabilityText) {
        this.durabilityText = durabilityText;
    }

	public Inventory getInventory() {
		return inventory;
	}
}

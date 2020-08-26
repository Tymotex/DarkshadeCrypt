package unsw.dungeon;

import java.io.File;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Inventory {
    public static final int INVENTORY_SIZE = 8;
    public static final int NO_SELECTION = -1;
    private int currentSize;
    private Usable inventory[] = new Usable[INVENTORY_SIZE];
    protected ImageView inventoryIcons[];
    protected IntegerProperty currPosition = new SimpleIntegerProperty(NO_SELECTION);  

    public Inventory() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory[i] = null;
        }
    }

    /**
     * Add the given item to the inventory
     * @param newItem
     */
    public void addToInventory(Usable newItem) {
        // Debug.printC("Adding new item to inventory", Debug.YELLOW);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (inventory[i] == null) {
                // Debug.printC("Adding new item to inventory at column " + i + " :" + newItem, Debug.YELLOW);
                inventory[i] = newItem;

                ImageView icon = newItem.getImageViewIcon();
                inventoryIcons[i].setImage(icon.getImage());

                currPosition.set(i);

                break;
            }
        }
        currentSize++;
    }

    /**
     * Remove the current item from the inventory
     * @return the current item that was removed
     */
    public Usable removeCurrItemFromInventory() {
        // Shift all items after the current position back one index
        Debug.printC("(size = " + currentSize + ") Removing item at index " + currPosition.get(), Debug.YELLOW);
        if (currPosition.get() != NO_SELECTION) {
            for (int i = currPosition.get() + 1; i <= currentSize; i++) {
                inventory[i - 1] = inventory[i];
                if (inventory[i] != null) {
                    inventoryIcons[i - 1].setImage(inventory[i].getImageIcon());
                } else {
                    setEmptyImage(i - 1);
                }
                Debug.printC("Setting " + i + " to be empty image");
                setEmptyImage(i);
                inventory[i] = null;
            }
            currentSize--;    
            if (currentSize <= 0) {
                currentSize = 0;
                currPosition.set(NO_SELECTION);
            } else {
                if (currPosition.get() - 1 < 0) {
                    currPosition.set(0);
                } else {
                    currPosition.set(currPosition.get() - 1);
                }
            }
        }
        return getCurrentItem();
    }

    /**
     * Sets the item in the inventory at index i to be an empty image
     * @param i
     */
    private void setEmptyImage(int i) {
        ImageView iv = new ImageView(new Image((new File("assets/sprites/entities/empty_item.png")).toURI().toString()));
        iv.setFitHeight(DungeonControllerLoader.DIM * DungeonControllerLoader.SCALE);
        iv.setFitWidth(DungeonControllerLoader.DIM * DungeonControllerLoader.SCALE);
        inventoryIcons[i].setImage(iv.getImage());
    }

    public Usable getCurrentItem() {
        return (currPosition.get() != NO_SELECTION) ? inventory[currPosition.get()] : null;
    }

    /**
     * Gets the next item available in the inventory
     * @return
     */
    public Usable getNextItem() {
        if (currentSize <= 0) return null;
        int nextPosition = (currPosition.get() + 1 >= currentSize) ? 0 : currPosition.get() + 1;
        currPosition.set(nextPosition);
        return inventory[nextPosition];
    }

    /**
     * Gets the previous available item in the inventory
     * @return
     */
    public Usable getPreviousItem() {
        if (currentSize <= 0) return null;
        int prevPosition = (currPosition.get() - 1 < 0) ? currentSize - 1 : currPosition.get() - 1;
        currPosition.set(prevPosition);
        return inventory[prevPosition];
    }
}
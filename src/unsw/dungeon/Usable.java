package unsw.dungeon;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface Usable {
    /**
     * Returns the number of uses this instance of the object has
     * @return
     */
    public int getUses();

    /**
     * Uses the item on the target
     * @param Entity target
     * @return whether or not the item is usable after completing this function
     */
    public boolean useItem(Entity target);


    /**
     * Method called when the usuable item runs out of uses
     */
    public void remove();

    /**
     * Returns the number of uses left over initial amount
     * @return
     */
    public String getDescription();

    /**
     * Changes the player's sprite to hold this item
     * @param p
     */
    public void setPlayerImage(Player p);


    /**
     *  
     * @return image of the class this instance of the object is 
     */
    public Image getImageIcon();

    /**
     *  
     * @return ImageView of the class this instance of the object is 
     */
    public ImageView getImageViewIcon();
}

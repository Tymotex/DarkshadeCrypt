package unsw.dungeon;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Key extends Entity implements Collectible, Usable {
    private int id;
    IntegerProperty durability = new SimpleIntegerProperty(1);
    BooleanProperty isBeingHeld = new SimpleBooleanProperty(false);

    public Key(int x, int y, Dungeon dungeon, int id) {
        super(x, y, dungeon);
        this.id = id;
    }

    /**
     * Replaces the player's currItem with this instance of the key and remove the
     * key from the map
     * 
     * @param Player p
     */
    public void pickup(Player p) {
        SoundPlayer.playSFX("key_pickup.wav");
        p.setCurrItem(this);
        setPlayerImage(p);

        // Add to inventory
        p.addToInventory(this);

        // Make this object disappear
        die();
    }

    @Override
    public void setPlayerImage(Player p) {
        p.updateImage("player/player_key.png");
    }

    /**
     * Checks whether the target is a door with corresponding id and open it if it
     * has the same id
     * 
     * @param Entity target
     */
    @Override
    public boolean useItem(Entity target) {
        // check if it is a door
        if (target instanceof Door) {
            // if it is door with same id, kill door, then delete key (set player inventory
            // to null)
            Door door = (Door) target;
            if (door.getId() == id) {
                Debug.printC("Door is getting killed");
                SoundPlayer.playSFX("door_unlock.wav");
                door.openDoor();
                // door.die();
                durability.set(durability.get() - 1);
            }
        }
        return (getUses() > 0) ? true : false;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getUses() {
        return durability.get();
    }

    @Override
    public Image getImageIcon() {
        return new Image((new File("assets/sprites/entities/key.png").toURI().toString()));
    }

    @Override
    public ImageView getImageViewIcon() {
        return new ImageView(new Image((new File("assets/sprites/entities/key.png").toURI().toString())));
    }

    @Override
    public String getDescription() {
        return durability.get() + " use left";
    }

    @Override
    public void remove() {

    }

}
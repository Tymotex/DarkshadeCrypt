package unsw.dungeon;

import java.io.File;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sword extends Entity implements Weapon {

    int maxDurability = 1;
    IntegerProperty durability = new SimpleIntegerProperty(1);

    /**
     * Constructor
     * @param x
     * @param y
     * @param dungeon
     */
    public Sword(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    /**
     * Constructor with custom durability
     * @param x
     * @param y
     * @param dungeon
     * @param durability
     */
    public Sword(int x, int y, Dungeon dungeon, int durability) {
        this(x, y, dungeon);
        this.durability.set(durability);
        this.maxDurability = durability;
    }

    /**
     * Replaces the player's currItem with this instance of the sword and remove the
     * sword from the map
     * 
     * @param Player p
     */
    @Override
    public void pickup(Player p) {
        SoundPlayer.playSFX("sword_pickup.wav");
        setPlayerImage(p);
        p.addToInventory(this);
        p.setCurrItem(this);
        die();
    }

    @Override
    public void setPlayerImage(Player p) {
        p.updateImage("player/player_sword.png");
    }

    @Override
    public boolean useItem(Entity target) {
        // if target is enemy, kill enemy (call die), lose one durability
        if (target instanceof Enemy && durability.get() > 0) {
            SoundPlayer.playSFX("sword_attack.wav");
            Enemy enemy = (Enemy) target;
            enemy.die();
            durability.set(durability.get() - 1);
        }
        return (getUses() > 0) ? true : false;
    }

    @Override
    public int getUses() {
        return durability.get();
    }

    @Override
    public Image getImageIcon() {
        return new Image((new File("assets/sprites/entities/sword.png").toURI().toString()));
    }

    @Override
    public ImageView getImageViewIcon() {
        return new ImageView(getImageIcon());
    }

    @Override
    public String getDescription() {
        return durability.get() + "/" + maxDurability + " uses left";
    }

    @Override
    public void remove() {
        SoundPlayer.playSFX("weapon_break.wav");
    }   

}
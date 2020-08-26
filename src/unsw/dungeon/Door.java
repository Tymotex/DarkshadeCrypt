package unsw.dungeon;

import java.io.File;

import javafx.animation.Animation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Door extends Entity implements Obstruction {
    private int id;
    private BooleanProperty isOpen = new SimpleBooleanProperty();
    protected BooleanProperty shouldIndicate = new SimpleBooleanProperty(false);

    /**
     * Constructor for Door
     * @param x
     * @param y
     * @param dungeon
     * @param id
     */
    public Door(int x, int y, Dungeon dungeon, int id) {
        super(x, y, dungeon);
        this.id = id;
    }

    /**
     * 
     */
    public void openDoor() {
        ImageView newDoor = new ImageView(new Image((new File("assets/sprites/entities/doors.png").toURI().toString())));
        newDoor.setViewport(new Rectangle2D(0, 0, DungeonControllerLoader.DIM, DungeonControllerLoader.DIM));
        newDoor.setFitHeight(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        newDoor.setFitWidth(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        this.getImageView().setImage(newDoor.getImage());
        
        this.getIsOpen().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Animation animation;
                animation = new SpriteAnimation(
                    this.getImageView(),
                    Duration.millis(200),
                    4, 1,
                    0, 0,
                    DungeonControllerLoader.DIM, DungeonControllerLoader.DIM
                );
                animation.setCycleCount(1);
                animation.play();
            }
        });   

        isOpen.set(true);;
    }

    public void indicate() {
        Debug.printC("======================================> !!! DOOR with ID " + id, Debug.YELLOW);
        ImageView newDoor = new ImageView(new Image((new File("assets/sprites/entities/door_indicated.png").toURI().toString())));
        newDoor.setViewport(new Rectangle2D(0, 0, DungeonControllerLoader.DIM, DungeonControllerLoader.DIM));
        newDoor.setFitHeight(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        newDoor.setFitWidth(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        this.getImageView().setImage(newDoor.getImage());
        
    }

    public void unindicate() {
        Debug.printC("======================================> !!! DOOR with ID UNINDICATE " + id, Debug.YELLOW);
        ImageView newDoor = new ImageView(new Image((new File("assets/sprites/entities/doors.png").toURI().toString())));
        newDoor.setViewport(new Rectangle2D(0, 0, DungeonControllerLoader.DIM, DungeonControllerLoader.DIM));
        newDoor.setFitHeight(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        newDoor.setFitWidth(DungeonControllerLoader.SCALE * DungeonControllerLoader.DIM);
        this.getImageView().setImage(newDoor.getImage());
    }


    /**
     * Getter for id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * If door is open, player can walk pass door
     */
    @Override
    public boolean canPass() {
        return isOpen.get();
    }

    public BooleanProperty getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(BooleanProperty isOpen) {
        this.isOpen = isOpen;
    }
    
    public boolean isOpen() {
        return isOpen.get();
    }

}
package unsw.dungeon;

import java.io.File;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    
    private IntegerProperty x, y;
    private Coordinate coordinate;
    private ImageView imageView;
    protected Dungeon dungeon;
    protected int relativeGridOffsetX = 0;
    protected int relativeGridOffsetY = 0;
    
    /**
     * Creates an entity in the associated dungeon at the given coordinates
     * @param x
     * @param y
     * @param dungeon
     */
    public Entity(int x, int y, Dungeon dungeon) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.coordinate = new Coordinate(x, y);
        this.dungeon = dungeon;
    }

    /**
     * Removes this entity from the game
     */
    public void die () {
        dungeon.removeEntity(this);
        updateImage("entities/null.png");
    }
    
    /**
     * Given the path to an image, relative to the sprites directory, sets
     * the entity's image to that sprite
     * @param pathToImage
     */
    public void updateImage(String pathToImage) {
        String fullPathToImage = new File("assets/sprites/" + pathToImage).toURI().toString();
        this.getImageView().setImage(new Image(fullPathToImage));
    }

    /**
     * Debugging convenience method for showing the entity's coordinate
     */
    public void showDetails() {
        Debug.printC(this.getClass().getSimpleName() + " at " + coordinate);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }

    /**
     * Changes the current entity's y coordinate to the new input y coordinate
     * @param x
     */
    public void setY(int y) {
        y().set(y);
        this.coordinate.setY(y);
    }

    /**
     * Changes the current entity's x coordinate to the new input x coordinate
     * @param x
     */
    public void setX(int x) {
        x().set(x);
        this.coordinate.setX(x);
    }

    /**
     * Get the coordinates of this entity
     * @return coordinate data object
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    /**
     * Changes coordinate of this entity to the new input coordinate
     * @param newCoords
     */
    public void setCoordinate(Coordinate newCoords) {
        this.coordinate = newCoords;
        setX(newCoords.x);
        setY(newCoords.y);
    }

    /**
     * Get the ImageView object of this entity
     * @return
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Set the ImageView object for this entity
     * @param imageView
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}

package unsw.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities = new ArrayList<>();
    private List<ImageView> middleEntities = new ArrayList<>();
    private List<ImageView> upperEntities = new ArrayList<>();

    public static final int DIM = 32; 

    public static final double SCALE = 1.5;

    // Images
    private Image playerImage;
    private Image wallImages[] = new Image[4];
    private Image boulderImages[] = new Image[4];
    private Image treasureImage;
    private Image potionInvicibilityImage;
    private Image portalImage;
    private Image swordImage;
    private Image axeImage;
    private Image maceImage;
    private Image floorSwitchImage;
    private Image ghostImage;
    private Image skeletonImage;
    private Image draugrImage;
    private Image ghoulImage;
    private Image wispImage;
    private Image skeletonLadyImage;
    private Image exitImage;
    private Image doorImage;
    private Image keyImage;

    public DungeonControllerLoader(String filename) throws FileNotFoundException {
        super(filename);
        // Animated:
        treasureImage = getAsset("sprites/entities/coin.png");
        potionInvicibilityImage = getAsset("sprites/entities/potion_1.png");
        portalImage = getAsset("sprites/entities/portal.png");
        playerImage = getAsset("sprites/player/player.png");
        skeletonImage = getAsset("sprites/enemy/skeleton.png");
        ghostImage = getAsset("sprites/enemy/ghost.png");
        draugrImage = getAsset("sprites/enemy/draugr.png");
        ghoulImage = getAsset("sprites/enemy/ghoul.png");
        wispImage = getAsset("sprites/enemy/wisp.png");
        skeletonLadyImage = getAsset("sprites/enemy/skeletonlady.png");
        exitImage = getAsset("sprites/entities/exit.png");
        doorImage = getAsset("sprites/entities/doors.png");

        // Static images:
        keyImage = getAsset("sprites/entities/key.png");
        swordImage = getAsset("sprites/entities/sword.png");  
        axeImage = getAsset("sprites/entities/axe.png");    
        maceImage = getAsset("sprites/entities/mace.png");    
        floorSwitchImage = getAsset("sprites/entities/floor_switch.png");
        wallImages[0] = getAsset("textures/stone_1.png");
        wallImages[1] = getAsset("textures/stone_2.png");
        wallImages[2] = getAsset("textures/stone_3.png");
        wallImages[3] = getAsset("textures/stone_4.png");
        boulderImages[0] = getAsset("sprites/entities/boulder_1.png");   
        boulderImages[1] = getAsset("sprites/entities/boulder_2.png");   
        boulderImages[2] = getAsset("sprites/entities/boulder_3.png");   
        boulderImages[3] = getAsset("sprites/entities/boulder_4.png");   
    }

    /**
     * Fetches the asset at the given path relative to the assets directory and
     * returns an Image object
     * @return Image of specified file
     */
    private Image getAsset(String pathToSprite) {
        return new Image((new File("assets/" + pathToSprite)).toURI().toString());
    }

    /**
     * Adds the given entity and the image to the list of entities to be loaded
     * @param entity
     * @param view
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        if (entity instanceof Door) {
            Door door = (Door) entity;
            trackDoor(door, view);
            upperEntities.add(view);
        } else if (entity instanceof Player || 
                   entity instanceof Enemy || 
                   entity instanceof Boulder) {
            middleEntities.add(view);
        } else {
            entities.add(view);
        }
    }

    /**
     * 
     * @param node
     * @param relativeOffsetX
     * @param relativeOffsetY
     * @param millisecondDuration
     */
    private void translate(Node node, int relativeOffsetX, int relativeOffsetY, int millisecondDuration) {
        TranslateTransition translate = new TranslateTransition();  
        translate.setByX(relativeOffsetX);
        translate.setByY(relativeOffsetY);
        translate.setDuration(Duration.millis(millisecondDuration));
        translate.setNode(node);  
        translate.play();  
    }

    /**
     * Resets the relative position of the entity after sequences of translation
     * @param entity
     * @param node
     */
    private void resetRelativePosition(Entity entity, Node node) {
        translate(node, -entity.relativeGridOffsetX, -entity.relativeGridOffsetY, 1);
        entity.relativeGridOffsetX = 0;
        entity.relativeGridOffsetY = 0;
    }

    /**
     * Resets the relative position of the entity after sequences of translation
     * @param entity
     * @param node
     */
    private void resetRelativePositionX(Entity entity, Node node) {
        translate(node, -entity.relativeGridOffsetX, 0, 1);
        entity.relativeGridOffsetX = 0;
    }

    /**
     * Resets the relative position of the entity after sequences of translation
     * @param entity
     * @param node
     */
    private void resetRelativePositionY(Entity entity, Node node) {
        translate(node, 0, -entity.relativeGridOffsetY, 1);
        entity.relativeGridOffsetY = 0;
    }

    /**
     * Attaches a sprite animation to the door when the door is opened
     * @param door
     * @param node
     */
    private void trackDoor(Door door, Node node) {
        door.getIsOpen().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Animation animation;
                animation = new SpriteAnimation(
                    door.getImageView(),
                    Duration.millis(200),
                    4, 1,
                    0, 0,
                    DIM, DIM
                );
                animation.setCycleCount(1);
                animation.play();
            }
        });
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (Math.abs(newValue.intValue() - oldValue.intValue()) > 1) {
                    // Entity must have teleported. Don't play handle animations
                    // SoundPlayer.playSFX("teleport.wav");
                    GridPane.setColumnIndex(node, newValue.intValue());
                    resetRelativePositionX(entity, node);
                } else {
                    handleMovementAnimation(entity, node, oldValue, newValue, "HORIZONTAL");
                }
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (Math.abs(newValue.intValue() - oldValue.intValue()) > 1) {
                    // Entity must have teleported. Don't play handle animations
                    // SoundPlayer.playSFX("teleport.wav");
                    GridPane.setRowIndex(node, newValue.intValue());
                    resetRelativePositionY(entity, node);
                } else {
                    handleMovementAnimation(entity, node, oldValue, newValue, "VERTICAL");
                }
            }
        });
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities, middleEntities, upperEntities);
    }

    // OnLoad Overrides:

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        player.setImageView(view);
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        Random random = new Random();
        ImageView view = new ImageView(wallImages[random.nextInt(4)]);
        wall.setImageView(view);
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        Random random = new Random();
        ImageView view = new ImageView(boulderImages[random.nextInt(4)]);
        boulder.setImageView(view);
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        treasure.setImageView(view);
        Animation animation;
        animation = new SpriteAnimation(
            treasure.getImageView(),
            Duration.millis(1000),
            16, 4,
            0, 0,
            DIM, DIM
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(InvincibilityPotion potion) {
        ImageView view = new ImageView(potionInvicibilityImage);
        potion.setImageView(view);
        Animation animation;
        animation = new SpriteAnimation(
            potion.getImageView(),
            Duration.millis(500),
            6, 6,
            0, 0,
            DIM, DIM
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        addEntity(potion, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        sword.setImageView(view);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(Axe axe) {
        ImageView view = new ImageView(axeImage);
        axe.setImageView(view);
        addEntity(axe, view);
    }

    @Override
    public void onLoad(Mace mace) {
        ImageView view = new ImageView(maceImage);
        mace.setImageView(view);
        addEntity(mace, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        portal.setImageView(view);
        Animation animation;
        animation = new SpriteAnimation(
            portal.getImageView(),
            Duration.millis(500),
            20, 5,
            0, 0,
            DIM, DIM
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        addEntity(portal, view);
    }
    
    @Override
    public void onLoad(FloorSwitch floorSwitch) {
        ImageView view = new ImageView(floorSwitchImage);
        floorSwitch.setImageView(view);
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Skeleton skeleton) {
        ImageView view = new ImageView(skeletonImage);
        skeleton.setImageView(view);
        addEntity(skeleton, view);
    }

    @Override
    public void onLoad(Ghost ghost) {
        ImageView view = new ImageView(ghostImage);
        ghost.setImageView(view);
        addEntity(ghost, view);
    }

    @Override
    public void onLoad(Draugr draugr) {
        ImageView view = new ImageView(draugrImage);
        draugr.setImageView(view);
        addEntity(draugr, view);
    }

    @Override
    public void onLoad(Ghoul ghoul) {
        ImageView view = new ImageView(ghoulImage);
        ghoul.setImageView(view);
        addEntity(ghoul, view);
    }

    @Override
    public void onLoad(Wisp wisp) {
        ImageView view = new ImageView(wispImage);
        wisp.setImageView(view);
        addEntity(wisp, view);
    }

    @Override
    public void onLoad(SkeletonLady skeletonLady) {
        ImageView view = new ImageView(skeletonLadyImage);
        skeletonLady.setImageView(view);
        addEntity(skeletonLady, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        exit.setImageView(view);
        Animation animation;
        animation = new SpriteAnimation(
            exit.getImageView(),
            Duration.millis(500),
            20, 5,
            0, 0,
            DIM, DIM
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(doorImage);
        door.setImageView(view);
        addEntity(door, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        key.setImageView(view);
        addEntity(key, view);
    }

    // Sprite sheet animation helper functions
    
    /**
     * Delegates the animation in a specific direction for an entity to the appropriate
     * animation handler function
     */
    private void handleMovementAnimation(Entity entity, Node node, Number oldValue, Number newValue, String direction) {
        if (entity instanceof Player) {
            switch(direction) {
                case "VERTICAL":
                    handlePlayerAnimationVertical((Player) entity, node, oldValue, newValue);
                    break;
                case "HORIZONTAL":
                    handlePlayerAnimationHorizontal((Player) entity, node, oldValue, newValue);
                    break;
                default:
                    break;
            }
        } else if (entity instanceof Enemy) {
            switch(direction) {
                case "VERTICAL":
                    handleEnemyAnimationVertical((Enemy) entity, node, oldValue, newValue);
                    break;
                case "HORIZONTAL":
                    handleEnemyAnimationHorizontal((Enemy) entity, node, oldValue, newValue);
                    break;
                default:
                    break;
            }
        } else if (entity instanceof Boulder) {
            switch(direction) {
                case "VERTICAL":
                    handleBoulderAnimationVertical(entity, node, oldValue, newValue);
                    break;
                case "HORIZONTAL":
                    handleBoulderAnimationHorizontal(entity, node, oldValue, newValue);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handles player horizontal animation
     */
    private void handlePlayerAnimationHorizontal(Player player, Node node, Number oldValue, Number newValue) {
        Animation animation;
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, (int) Math.round(-DIM * SCALE), 0, 100);
            player.relativeGridOffsetX -= DIM * SCALE;
            animation = new SpriteAnimation(
                player.getImageView(),
                Duration.millis(200),
                3, 3,
                0, DIM,
                DIM, DIM
            );
            animation.setCycleCount(1);
        } else {
            translate(node, (int) Math.round(DIM  * SCALE), 0, 100);
            player.relativeGridOffsetX += DIM * SCALE;
            animation = new SpriteAnimation(
                player.getImageView(),
                Duration.millis(200),
                3, 3,
                0, DIM * 2,
                DIM, DIM
            );
            animation.setCycleCount(1);
        }
        SoundPlayer.playSFX("footstep.wav");
        animation.play();
    }

    /**
     * Handles player vertical animation
     */
    private void handlePlayerAnimationVertical(Player player, Node node, Number oldValue, Number newValue) {
        Animation animation;
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, 0, (int) Math.round(-DIM  * SCALE), 100);
            player.relativeGridOffsetY -= (int) Math.round(DIM * SCALE);
            animation = new SpriteAnimation(
                player.getImageView(),
                Duration.millis(200),
                3, 3,
                0, DIM * 3,
                DIM, DIM
            );
            animation.setCycleCount(1);
        } else {
            translate(node, 0, (int) Math.round(DIM  * SCALE), 100);
            player.relativeGridOffsetY += (int) Math.round(DIM * SCALE);
            animation = new SpriteAnimation(
                player.getImageView(),
                Duration.millis(200),
                3, 3,
                0, 0,
                DIM, DIM
            );
            animation.setCycleCount(1);
        }
        SoundPlayer.playSFX("footstep.wav");
        animation.play();
    }

    /**
     * Handles enemy horizontal animation
     */
    private void handleEnemyAnimationHorizontal(Enemy enemy, Node node, Number oldValue, Number newValue) {
        Animation animation;
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, (int) Math.round(-DIM * SCALE), 0, 175);
            enemy.relativeGridOffsetX -= (int) Math.round(DIM * SCALE);
            animation = new SpriteAnimation(
                enemy.getImageView(),
                Duration.millis(175),
                3, 3,
                0, DIM,
                DIM, DIM
            );
            animation.setCycleCount(1);
        } else {
            translate(node, (int) Math.round(DIM * SCALE), 0, 175);
            enemy.relativeGridOffsetX += DIM * SCALE;
            animation = new SpriteAnimation(
                enemy.getImageView(),
                Duration.millis(175),
                3, 3,
                0, DIM * 2,
                DIM, DIM
            );
            animation.setCycleCount(1);
        }
        enemy.playFootstep();
        animation.play();
    }

    /**
     * Handles enemy vertical animation
     */
    private void handleEnemyAnimationVertical(Enemy enemy, Node node, Number oldValue, Number newValue) {
        Animation animation;
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, 0, (int) Math.round(-DIM * SCALE), 175);
            enemy.relativeGridOffsetY -= (int) Math.round(DIM * SCALE);
            animation = new SpriteAnimation(
                enemy.getImageView(),
                Duration.millis(175),
                3, 3,
                0, DIM * 3,
                DIM, DIM
            );
            animation.setCycleCount(1);
        } else {
            translate(node, 0, (int) Math.round(DIM * SCALE), 175);
            enemy.relativeGridOffsetY += (int) Math.round(DIM * SCALE);
            animation = new SpriteAnimation(
                enemy.getImageView(),
                Duration.millis(175),
                3, 3,
                0, 0,
                DIM, DIM
            );
            animation.setCycleCount(1);
        }
        enemy.playFootstep();
        animation.play();
    }

    /**
     * Boulder animation horizontal movement handler
     */
    private void handleBoulderAnimationHorizontal(Entity boulder, Node node, Number oldValue, Number newValue) {
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, (int) Math.round(-DIM * SCALE), 0, 75);
            boulder.relativeGridOffsetX -= DIM * SCALE;
        } else {
            translate(node, (int) Math.round(DIM * SCALE), 0, 75);
            boulder.relativeGridOffsetX += (int) Math.round(DIM * SCALE);
        }
    }

    /**
     * Boulder animation vertical movement handler
     */
    private void handleBoulderAnimationVertical(Entity boulder, Node node, Number oldValue, Number newValue) {
        if (newValue.intValue() < oldValue.intValue()) {
            translate(node, 0, (int) Math.round(-DIM * SCALE), 75);
            boulder.relativeGridOffsetY -= (int) Math.round(DIM * SCALE);
        } else {
            translate(node, 0, (int) Math.round(DIM * SCALE), 75);
            boulder.relativeGridOffsetY += (int) Math.round(DIM * SCALE);
        }
    }

}

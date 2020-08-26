package unsw.dungeon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DungeonController {

    @FXML
    private GridPane squares;

    @FXML
    private GridPane inventoryGrid;

    @FXML
    private Text potionEffectText;

    @FXML
    private Text durabilityText;

    @FXML
    private Text goalText;

    @FXML
    private Text treasureGoalProgress;

    @FXML
    private Text enemyGoalProgress;

    @FXML
    private Text switchGoalProgress;

    @FXML
    private Text potionProgress;

    static final int inventorySize = 8;
    ImageView inventoryItems[] = new ImageView[inventorySize];
    int currInventoryItem = 0;

    public static final int DIM = 32;

    private List<ImageView> initialEntities;
    private List<ImageView> middleEntities;
    private List<ImageView> upperEntities;
    private Player player;
    private Dungeon dungeon;
    private BooleanProperty playerIsAlive = new SimpleBooleanProperty(true);
    private BooleanProperty levelWon = new SimpleBooleanProperty(true);

    private DungeonScreen dungeonScreen;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, List<ImageView> upperEntities) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();

        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            this.inventoryItems[i] = new ImageView();
        }

        // If defeat every becomes true, transition to 
        playerIsAlive.bindBidirectional(player.getIsAlive());
        levelWon.bindBidirectional(dungeon.getGameStatus().levelIsCompleted);

        this.playerIsAlive.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dungeon.pause();
                dungeonScreen.getOutcomeScreen("YOU DIED", Color.FIREBRICK);
            }
        });   
        this.levelWon.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                dungeon.pause();
                SoundPlayer.playSFX("level_win.wav");
                dungeonScreen.getOutcomeScreen("VICTORY ACHIEVED", Color.GREEN);
            }
        });   
        this.initialEntities = new ArrayList<>(initialEntities);
    }
    
    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, List<ImageView> middleEntities, List<ImageView> upperEntities) {
        this(dungeon, initialEntities, upperEntities);
        this.middleEntities = middleEntities;
        this.upperEntities = upperEntities;
    }

    public void removeEntity(ImageView image) {
        squares.getChildren().remove(image);
    }

    // getters and setters

    @FXML
    public void initialize() {
        Image ground = new Image((new File("assets/textures/dirt.png")).toURI().toString());
        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                ImageView groundImg = new ImageView(ground);
                groundImg.setFitHeight(DungeonControllerLoader.SCALE * DIM);
                groundImg.setFitWidth(DungeonControllerLoader.SCALE * DIM);
                squares.add(groundImg, x, y);
            }
        }

        // Adding entities from low to high z level
        for (ImageView entity : initialEntities) {
            entity.setViewport(new Rectangle2D(0, 0, DIM, DIM));
            entity.setFitHeight(DungeonControllerLoader.SCALE * DIM);
            entity.setFitWidth(DungeonControllerLoader.SCALE * DIM);
            squares.getChildren().add(entity);
        }

        for (ImageView entity : middleEntities) {
            entity.setViewport(new Rectangle2D(0, 0, DIM, DIM));
            entity.setFitHeight(DungeonControllerLoader.SCALE * DIM);
            entity.setFitWidth(DungeonControllerLoader.SCALE * DIM);
            squares.getChildren().add(entity);
        }

        for (ImageView entity : upperEntities) {
            entity.setViewport(new Rectangle2D(0, 0, DIM, DIM));
            entity.setFitHeight(DungeonControllerLoader.SCALE * DIM);
            entity.setFitWidth(DungeonControllerLoader.SCALE * DIM);
            squares.getChildren().add(entity);
        }

        // Adding inventory image
        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            Debug.printC("Adding image to inventory at col index " + i, Debug.YELLOW);
            // Node pane = getNodeByRowColumnIndex(i, 0, inventoryGrid);
            // pane.setStyle("-fx-background-color: #FFFFFF;");
            inventoryGrid.add(inventoryItems[i], i, 0);
            inventoryItems[i].setImage(new Image((new File("assets/sprites/entities/empty_item.png")).toURI().toString()));
            inventoryItems[i].setFitHeight(DungeonControllerLoader.SCALE * DIM);
            inventoryItems[i].setFitWidth(DungeonControllerLoader.SCALE * DIM);
        }
        
        this.player.getInventory().inventoryIcons = inventoryItems;
        this.player.getInventory().currPosition.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() != Inventory.NO_SELECTION) {
                    inventoryItems[newValue.intValue()].setFitHeight(DungeonControllerLoader.SCALE * DIM + 10);
                    inventoryItems[newValue.intValue()].setFitWidth(DungeonControllerLoader.SCALE * DIM);
                    inventoryItems[newValue.intValue()].setEffect(null);
                }
                if (oldValue.intValue() != Inventory.NO_SELECTION) {
                    inventoryItems[oldValue.intValue()].setFitHeight(DungeonControllerLoader.SCALE * DIM);
                    inventoryItems[oldValue.intValue()].setFitWidth(DungeonControllerLoader.SCALE * DIM);
                    ColorAdjust blackout = new ColorAdjust();
                    blackout.setBrightness(-0.75);
                    inventoryItems[oldValue.intValue()].setEffect(blackout);
                    inventoryItems[oldValue.intValue()].setCache(true);
                    inventoryItems[oldValue.intValue()].setCacheHint(CacheHint.SPEED);
                }
                Debug.printC("INVENTORY CURRENT ITEM HAS CHANGED", Debug.YELLOW);
            }
        });

        // Binding text elements
        goalText.setText(dungeon.getGameStatus().getObjectiveString());
        Bindings.bindBidirectional(durabilityText.textProperty(), player.getDurabilityString());
        Bindings.bindBidirectional(treasureGoalProgress.textProperty(), dungeon.getTreasureGoalProgress());
        Bindings.bindBidirectional(enemyGoalProgress.textProperty(), dungeon.getEnemyGoalProgress());
        Bindings.bindBidirectional(switchGoalProgress.textProperty(), dungeon.getSwitchGoalProgress());
        for (InvincibilityPotion potion : dungeon.getPotions()) {
            potionProgress.textProperty().bindBidirectional(potion.potionProgress);
        }
    }

    long lastMoved = System.currentTimeMillis();
    
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastMoved > 150) {
            lastMoved = now;
            switch (event.getCode()) {
                case W:
                case UP:
                    player.moveUp();
                    break;
                case S:
                case DOWN:
                    player.moveDown();
                    break;
                case A:
                case LEFT:
                    player.moveLeft();
                    break;
                case D:
                case RIGHT:
                    player.moveRight();
                    break;
                case E:
                    Debug.printC("E was pressed going to right item");
                    player.selectNextItem();
                    break;
                case Q:
                    Debug.printC("Q was pressed going to left item");
                    player.selectPrevItem();
                    break;
                case R:
                    Debug.printC("R was pressed. Removing item");
                    player.removeCurrItem();
                    break;
                default:
                    break;
            }
        }
        switch (event.getCode()) {
            case ESCAPE:
                dungeon.pause();
                dungeonScreen.pause();
                break;
            default:
                break;
        }
    }

    /**
     * Method for unpausing the game when game is paused
     */
    public void resume() {
        dungeon.resume();
    }

    public BooleanProperty getPlayerIsAlive() {
        return playerIsAlive;
    }

    public DungeonScreen getDungeonScreen() {
        return dungeonScreen;
    }

    public void setDungeonScreen(DungeonScreen dungeonScreen) {
        this.dungeonScreen = dungeonScreen;
    }

}
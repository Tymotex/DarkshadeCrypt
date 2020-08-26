package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;


/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class Dungeon {

    private int width, height;
    private List<Entity> entities = new ArrayList<Entity>();
    private Player player;
    private Gamestatus gameStatus; 
    private List<Timeline> timers = new ArrayList<Timeline>();

    /**
     * Constructs a new dungeon with the given width and height.
     * @param width
     * @param height
     */
    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<Entity>();
        this.player = null;
    }

    /**
     * Constructs a new dungeon with the given width and height and game status
     * @param width
     * @param height
     * @param gameStatus
     */
    public Dungeon(int width, int height, Gamestatus gameStatus) {
        this(width, height);
        this.gameStatus = gameStatus;
    }

    /**
     * Method pauses all timers in the dungeon so game doesn't continue when in pause screen
     */
    public void pause() {
        Debug.printC("PAUSING TIMER", Debug.PURPLE);
        for (Timeline timer: timers) {
            timer.stop();
        }
    }

    /**
     * Method plays all timers in the dungeon to unpause game
     */
    public void resume() {
        for (Timeline timer: timers) {
            timer.play();
        }
    }


    /**
     * Given a new pathfinding strategy, sets every currently active enemy's
     * pathfinding strategy to the new one
     * @param newPathfindingStrat
     */
    public void applyEnemyPathfindingStrat(PathFinding newPathfindingStrat) {
        for (Entity eachEntity : entities) {
            if (eachEntity instanceof Enemy) {
                Enemy enemy = (Enemy) eachEntity;
                enemy.setPathfinding(newPathfindingStrat);
            }
        }
    }

    /**
     * Gets the list of entities on a specified coordinate.
     * @param x
     * @param y
     * @return List of entities at the specified coordinate point
     */
    public List<Entity> getEntities(int x, int y) {
        // Loop through every entity and check if it has that position. If it does, add it to the list
        // of entities found at that coordinate
        List<Entity> entitiesOnCell = new ArrayList<Entity>();
        for (Entity entity: entities) {
            if (entity != null) {
                if (entity.getX() == x && entity.getY() == y) {
                    entitiesOnCell.add(entity);
                }
            }
        }
        return entitiesOnCell;
    }
    
    /**
     * Gets an entity of a specific class at the supplied coordinates
     * @param x
     * @param y
     * @param targetClass
     * @return First entity of the targetClass type found on the cell coordinates (x, y).
     *         Returns null if an entity of the target type is not present at (x, y)
     */
    @SuppressWarnings("unchecked")
    public <T> T getEntity(int x, int y, Class<T> targetClass) {
        List<Entity> foundEntities = getEntities(x, y);
        for (Entity eachEntity : foundEntities) {
            if (eachEntity.getClass() == targetClass) {
                return (T) eachEntity;
            }
        }
        return null;
    }

    /**
     * Convenience method for fetching the obstruction object found at
     * the given grid coordinates
     * @param x
     * @param y
     * @return
     */
    public Obstruction getObstruction(int x, int y) {
        List<Entity> foundEntities = getEntities(x, y);
        for (Entity eachEntity : foundEntities) {
            if (eachEntity instanceof Obstruction) {
                return (Obstruction) eachEntity;
            }
        }
        return null;
    }

    /**
     * Given a coordinate, returns true/false if an obstruction exists
     * @param x
     * @param y
     * @return true if an obstruction exists at that coordinate. False otherwise
     */
    public boolean hasObstruction(int x, int y) {
        List<Entity> foundEntities = getEntities(x, y);
        for (Entity eachEntity : foundEntities) {
            if (eachEntity instanceof Obstruction) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Given a coordinate, returns true/false if an entity with the target type exists
     * @param x
     * @param y
     * @return true if an entity of specified type exists. False otherwise
     */
    public boolean hasEntityOfType(int x, int y, Class<?> targetClass) {
        List<Entity> foundEntities = getEntities(x, y);
        for (Entity eachEntity : foundEntities) {
            if (eachEntity.getClass() == targetClass) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the entity with the specific ID in this dungeon
     * @param id
     * @return
     */
    public Entity getEntityByID(int id) {
        for (Entity entity : entities) {
            if (entity instanceof Portal) {
                Portal portal = (Portal) entity;
                if ( portal.getId() == id ) {
                    return portal;
                }
            }
        }
        return null;
    }

    /**
     * Get's the key with the given ID, if it exists
     * @param id
     * @return
     */
    public Key getKeyByID(int id) {
        Debug.printC("!!! Looking for key of id : " + id);
        for (Entity entity : entities) {
            if (entity instanceof Key) {
                Key key = (Key) entity;
                if (key.getId() == id) {
                    return key;
                }
            }
        }
        return null;
    }

    /**
     * Binding the door with the key so that when the key is held, the corresponding door
     * will be indicated
     * @param entity
     */
    public void linkDoorsWithKeys() {
        for (Entity entity : entities) {
            if (entity instanceof Door) {
                Door door = (Door) entity;
                Key correspondingKey = getKeyByID(door.getId());
                Bindings.bindBidirectional(door.shouldIndicate, correspondingKey.isBeingHeld);
                door.shouldIndicate.addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        door.indicate();
                    } else {
                        door.unindicate();
                    }
                });
            }
        }
    }
    
    /**
     * Gets all the potions in the dungeon
     * @return
     */
    public List<InvincibilityPotion> getPotions() {
        List<InvincibilityPotion> potions = new ArrayList<>();
        for (Entity eachEntity : entities) {
            if (eachEntity.getClass() == InvincibilityPotion.class) {
                potions.add((InvincibilityPotion) eachEntity);
            }
        }
        return potions;
    }

    /**
     * Adds an entity to the dungeon's entity collection
     * @param entity
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Removes the specified entity from the dungeon's collection of entities
     * @param entity
     */
    public void removeEntity(Entity entity) {
        // Temporarily setting coordinate for visual consistency in frontend
        // entity.setX(100); 
        // entity.setY(100);
        entity.getImageView().setImage(null);
        entities.remove(entity);
    }

    public void showMatrix() {
        int M[][] = getObstructionMatrix();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(M[row][col] + " ");
            }
            Debug.printC("");
        }
    }

    /**
     * Gets a width * height matrix of 0s and 1s. 0s indicate obstructions and 
     * 1s indicate no obstruction. Useful for pathfinding algorithms
     * @return obstruction matrix
     */
    public int[][] getObstructionMatrix() {
        int M[][] = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                M[row][col] = 1;
            }
        }
        for (Entity currEntity : entities) {
            if (currEntity instanceof Obstruction) {
                Obstruction obs = (Obstruction) currEntity;
                if (!obs.canPass()) {
                    M[currEntity.getY()][currEntity.getX()] = 0;
                } else {
                    // Unblocked portal:
                    if (obs instanceof Portal) {
                        M[currEntity.getY()][currEntity.getX()] = 2;
                    }
                }
            }
        }
        return M;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Gamestatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Gamestatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Method should be used whenever a timer is used in the dungeon so that the dugeon can pause all timer on command
     * @param timer
     */
    public void addTimer(Timeline timer) {
        timers.add(timer);
    }

    /**
     * Removes timer from list of timers
     * @param timer
     */
    public void removeTimer(Timeline timer) {
        timers.remove(timer);
    }

    public StringProperty getTreasureGoalProgress() {
        return getGameStatus().getTreasureGoalProgress();
    }
    
    public StringProperty getEnemyGoalProgress() {
        return getGameStatus().getEnemyGoalProgress();
    }

    public StringProperty getSwitchGoalProgress() {
        return getGameStatus().getSwitchGoalProgress();
    }

    public List<Entity> getEntities() {
        return entities;
    }

}

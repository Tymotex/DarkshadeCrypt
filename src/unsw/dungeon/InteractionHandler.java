package unsw.dungeon;

import java.util.List;

public abstract class InteractionHandler {
    // Step directions
    public final static String UP = "UP";
    public final static String DOWN = "DOWN";
    public final static String LEFT = "LEFT";
    public final static String RIGHT = "RIGHT";
    public final static String NO_DIR = "NO_DIR";

    /**
     * Hook for interaction with portal, set true on default since 
     * all major entities interact with portals
     * @return
     */
    boolean handlesPortal() {
        return true;
    }

    /**
     * Hook for interaction with boulders
     */
    boolean handlesBoulder(){
        return false;
    }

    /**
     * Hook for interaction with exit
     */
    boolean handlesExit(){
        return false;
    }

    /**
     * Hook for interaction with door
     */
    boolean handlesDoor(){
        return false;
    }

    /**
     * Hook for interaction with enemy
     */
    boolean handlesEnemy(){
        return false;
    }

    /**
     * Hook for interaction with collectible
     */
    boolean handlesCollectible(){
        return false;
    }

    /**
     * Hook for interaction with Obstruction, set true on default since all entities are affected by obstructions
     * 
     */
    boolean handlesObstruction(){
        return true;
    }

    /**
     * Hook for interaction with floorSwitch
     */
    boolean handlesFloorSwitch(){
        return false;
    }

    /**
     * Hook for interaction with player
     */
    boolean handlesPlayer(){
        return false;
    }


     /**
     * Handles interaction between the player and different entities and returns
     * whether or not the player is allowed to move after handling the interaction
     * @param e - the entity being interacted with
     * @param d - reference to the dungeon
     * @param currEntity - the entity attempting to do the interaction
     * @param stepDirection - which direction the entity is attempting to step in
     * @return true if the boulder can move, false if the boulder cannot move after interaction
     */
    private boolean handleInteractionHelper(Entity e, Dungeon d, Entity currEntity, String stepDirection) {
        // Portal
        if (e.getClass() == Portal.class && handlesPortal()) {
            Debug.printC("Player is being teleported", Debug.YELLOW);
            Portal portal = (Portal) e;
            portal.teleport(currEntity);
        } 
        // FloorSwitch
        if (e.getClass() == FloorSwitch.class && handlesFloorSwitch()) {
            FloorSwitch floorSwitch = (FloorSwitch) e;
            floorSwitch.toggle();
        }
        // Player
        if (e.getClass() == Player.class && handlesPlayer()) {
            Player player = (Player) e;
            if (player.isInvincible()) {
                // If player is invincible, kill the enemy
                SoundPlayer.playSFX("hurt.wav");
                currEntity.die();
            } else {
                player.useItem(currEntity);
            }
            return false;
        } 
        // Boulder
        if (e.getClass() == Boulder.class && handlesBoulder()) {
            handleBoulder(e, stepDirection);
            return true;
        } 
        // Exit
        if (e.getClass() == Exit.class && handlesExit()) {
            handleExit(e);
        }
        // Door
        if (e.getClass() == Door.class && handlesDoor()) {
            Debug.printC("Player is attempting to open door", Debug.PURPLE);
            Door door = (Door) e;
            if (!door.isOpen()) {
                ((Player) currEntity).useItem(door);
                return false;
            }
        }

        // Enemy
        if (e instanceof Enemy && handlesEnemy()) {
            handleEnemy(e, (Player) currEntity);   
        }
        // if item is collectible
        if (e instanceof Collectible && handlesCollectible()) {
            handlePickup(e, (Player) currEntity);
        }

        if (e instanceof Obstruction && handlesObstruction()) {
            return ((Obstruction) e).canPass();
        }
        return true;
    }

    /**
     * Method moves the boulder entity e in the direction of the stepDirection string
     * @param e
     * @param stepDirection
     */
    private void handleBoulder(Entity e, String stepDirection) {
        Debug.printC("Player is attempting to push boulder", Debug.PURPLE);
        Boulder boulder = (Boulder) e;
        switch (stepDirection) {
            case InteractionHandler.UP:
                boulder.moveUp();
                break;
            case InteractionHandler.DOWN:
                boulder.moveDown();
                break;
            case InteractionHandler.LEFT:
                boulder.moveLeft();
                break;
            case InteractionHandler.RIGHT:
                boulder.moveRight();
                break;
            default:
                Debug.printC("Unknown step direction", Debug.RED);
                break;
        }
    }

    /**
     * Calls exit method from exit when player reaches exit
     */
    private void handleExit(Entity e) {
        Debug.printC("Player reached an exit", Debug.PURPLE);
        Exit exit = (Exit) e;
        exit.exit();
    }

    /**
     * Involves the interaction between enemy and player, including one of the entities dying
     */
    private void handleEnemy(Entity e, Player p) {
        Debug.printC("Player is attempting fight enemy", Debug.PURPLE);
        Enemy enemy = (Enemy) e;
        if (p.isInvincible()) {
            // If player is invincible, kill the enemy
            // SoundPlayer.playSFX("hurt.wav");
            enemy.die();
        } else {
            p.useItem(enemy);
        }
    }

    /**
     * Player picks up a collectible item 
     */
    private void handlePickup(Entity e, Player p) {
        Collectible c = (Collectible) e;
        c.pickup(p);
    }

    /**
     * Handles any teleportation if the portal is in the target coordinate
     * @param target coordinate
     * @param dungeon reference
     * @return
     */
    public Coordinate handlePortal(Coordinate coord, Dungeon dungeon) {
        if (dungeon.hasEntityOfType(coord.x, coord.y, Portal.class)) {
            Portal entryPortal = dungeon.getEntity(coord.x, coord.y, Portal.class);
            return entryPortal.getDestinationCoordinates();
        } else {
            return coord;
        }
    } 

    /**
     * Handles the interaction between the current entity and the entity at the next coordinate
     * @param current entity
     * @param next coordinate
     * @param dungeon
     * @param stepping direction
     * @return
     */
    public boolean handleInteraction(Entity currEntity, Coordinate nextCoordinate, Dungeon dungeon, String stepDirection) {
        boolean playerCanMove = true;
        if (dungeon.hasEntityOfType(nextCoordinate.x, nextCoordinate.y, Boulder.class) && handlesBoulder()) {
            Boulder boulder = dungeon.getEntity(nextCoordinate.x, nextCoordinate.y, Boulder.class);
            if (boulder.canPush(stepDirection)) {
                // Normal
                List<Entity> adjacentEntities = dungeon.getEntities(nextCoordinate.x, nextCoordinate.y);
                for (Entity eachEntity : adjacentEntities) {
                    playerCanMove = playerCanMove && handleInteractionHelper(eachEntity, dungeon, currEntity, stepDirection);
                }
            } else {
                return false;
            }
        } else {
            List<Entity> adjacentEntities = dungeon.getEntities(nextCoordinate.x, nextCoordinate.y);
            for (Entity eachEntity : adjacentEntities) {
                playerCanMove = playerCanMove && handleInteractionHelper(eachEntity, dungeon, currEntity, stepDirection);
            }
        }
        return playerCanMove;
    }
}

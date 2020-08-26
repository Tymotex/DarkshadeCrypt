package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Entity;
import unsw.dungeon.Player;

public class DoorTest {
    private int initPlayerX = 4;
    private int initPlayerY = 4;

    /**
     * Test door is an obstruction when locked
     */
    @Test
    public void testLockedDoor(){
        // setting up environment
        // setting up keys to be far away
        int key1Id = 1;
        int[] key1 = {initPlayerX + 5,initPlayerY + 5, key1Id};
        int[][] keys = {key1};

        // setting up to be right of player

        int[] door1 = {initPlayerX + 1,initPlayerY, key1Id};
        int[][] doors = {door1};

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, keys, JSONMapBuilder.key, doors, JSONMapBuilder.door);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();
        int initX = player.getX();
        int initY = player.getY();
        // run into the door to see if player position has changed
        for (int i = 0; i < 5; i++) {
            // check 5 times 
            player.moveRight();
            assertEquals(initX, player.getX());
            assertEquals(initY, player.getY());
        }
    }

    /**
     * Exists in conjunction with a single key that can open it. If the player holds the key, they can open the door by moving through it. 
     * Once door is open it remains so.
     */
    @Test
    public void testOpenDoor(){
        // setting up environment
        // setting up a keys coord to right of player
        int key1Id = 1;
        int[] key1 = {initPlayerX + 1,initPlayerY, key1Id};
        int[][] keys = {key1};

        // setting up a door right below the keys, with same id as the key above 

        int[] door1 = {initPlayerX + 1,initPlayerY + 1, key1Id};
        int[][] doors = {door1};

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, keys, JSONMapBuilder.key, doors, JSONMapBuilder.door);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();

        // player picks up key
        player.moveRight();

        // player opens the door
        player.moveDown();
        player.moveDown();
        player.moveDown();

        // check that door does not exist at its initial position
        List<Entity> entities = dungeon.getEntities(key1[0] + 1, key1[1]);
        for (Entity entity : entities) {
            // check that the entity is not a door
            System.out.println(entity.getClass().getSimpleName());
            assertNotEquals(Door.class, entity.getClass());
        }

        // check that player can move through door multiple times after it is open

        for (int i = 0; i < 10; i++) {
            System.out.println(player.getCoordinate());

            player.moveUp();

            // assert player is on top of door
            assertEquals(door1[0], player.getX());
            assertEquals(door1[1], player.getY());

            player.moveUp();

            System.out.println(player.getCoordinate());

            player.moveDown();
            // assert player is on top of door
            assertEquals(door1[0], player.getX());
            assertEquals(door1[1], player.getY());
        
            player.moveDown();
        }

    }
}
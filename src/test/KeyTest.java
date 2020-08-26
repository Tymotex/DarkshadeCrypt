package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Entity;
import unsw.dungeon.Key;
import unsw.dungeon.Player;

public class KeyTest {
    private int initPlayerX = 4;
    private int initPlayerY = 4;

    /**
     * Testing the key can be picked up by the player when they move into the square containing it
     */ 
    @Test
    public void testKeyPickUp()  {
        // setting up environment
        // setting up a key coord to right of player
        int key1Id = 1;
        int[] key1 = {initPlayerX + 1,initPlayerY, key1Id};
        int[][] keys = {key1};
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, keys, JSONMapBuilder.key);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();
        player.moveRight();

        // look for entites at the player's new location 
        // key shouldn't exist

        List<Entity> entities = dungeon.getEntities(key1[0] + 1, key1[1]);
        for (Entity entity : entities) {
            // check that the entity is not a key
            System.out.println(entity.getClass().getSimpleName());
            assertNotEquals(Key.class, entity.getClass());
        }
    }
    
    /**
     * Testing if only one door has a lock that fits the key and the 
     * player can carry only one key at a time, 
     */
    @Test
    public void testCarryMultipleKeys(){
        // setting up environment
        // setting up 3 keys coord to right of player
        int key1Id = 1;
        int key2Id = 2;
        int key3Id = 3;
        int[] key1 = {initPlayerX + 1,initPlayerY, key1Id};
        int[] key2 = {initPlayerX + 2,initPlayerY, key2Id};
        int[] key3 = {initPlayerX + 3,initPlayerY, key3Id};

        int[][] keys = {key1, key2, key3};

        // setting up doors right below the keys, with same id as the keys above them

        int[] door1 = {initPlayerX + 1,initPlayerY + 1, key1Id};
        int[] door2 = {initPlayerX + 2,initPlayerY + 1, key2Id};
        int[] door3 = {initPlayerX + 3,initPlayerY + 1, key3Id};

        int[][] doors = {door1, door2, door3};

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, keys, JSONMapBuilder.key, doors, JSONMapBuilder.door);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();

        // attempt to collect all three keys
        player.moveRight();
        player.moveRight();
        player.moveRight();


        // tries to unlock first door, (should fail)
        player.moveLeft();
        player.moveLeft();

        int initX = player.getX();
        int initY = player.getY();

        player.moveDown();
        
        assertEquals(initX, player.getX());
        assertEquals(initY, player.getY());

        // tries to unlock second door, (should fail)
        player.moveRight();

        initX = player.getX();
        initY = player.getY();
        player.moveDown();
        player.moveDown();

        assertEquals(initX, player.getX());
        assertEquals(initY, player.getY());

        // tries to unlock 3rd door, (should succeed)
        player.moveRight();

        initX = player.getX();
        initY = player.getY();
        System.out.println(initX);
        System.out.println(initY);

        player.moveDown();
        player.moveDown();
    
        assertEquals(initX, player.getX());
        assertEquals(initY + 1, player.getY()); // has successfully moved down
    }
}
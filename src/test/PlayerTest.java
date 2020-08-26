package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class PlayerTest {
    private int initPlayerX = 3;
    private int initPlayerY = 3;

    private JSONObject buildMap() {
        JSONObject jsonString = new JSONObject();

        // build app
        jsonString.put("width", "8");
        jsonString.put("height", "8");


        // build entities
        JSONArray entities = new JSONArray();

        // add player
        JSONObject player = new JSONObject();
        player.put("x", initPlayerX);
        player.put("y", initPlayerY);
        player.put("type", "player");
        entities.put(player);

        jsonString.put("entities", entities);

        // build goals
        JSONObject goalCondition = new JSONObject();
        goalCondition.put("goal", "boulders");
        jsonString.put("goal-condition", goalCondition);

        return jsonString;
    }

    /**
     * Tests if player can be moved up, down, left, and right into 
     * adjacent squares, provided another entity doesn't stop them (e.g. a wall).
     */
    @Test
    public void testMovement() {
        JSONObject map = buildMap();
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();

        player.moveUp();
        assertEquals(initPlayerX, player.getX());
        assertEquals(initPlayerY - 1, player.getY());

        
        player.moveDown();
        assertEquals(initPlayerX, player.getX());
        assertEquals(initPlayerY, player.getY());
        
        player.moveRight();
        assertEquals(initPlayerX + 1, player.getX());
        assertEquals(initPlayerY, player.getY());
        
        player.moveLeft();
        assertEquals(initPlayerX, player.getX());
        assertEquals(initPlayerY, player.getY());
    }   

    /**
     * Basic test just checks what happens when player moves into a wall.
     * Tests by moving the player into another entity which stops them (e.g. a wall).
     */
    @Test
    public void testObstructedMovement() {

        // setting up boulders which are adjacent to portal
        int[] w1 = { initPlayerX + 1, initPlayerY };

        int[][] walls = { w1 };
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, walls, JSONMapBuilder.wall);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();

        player.moveRight();
        assertEquals(initPlayerX, player.getX());
        assertEquals(initPlayerY, player.getY());
        
    }   

}


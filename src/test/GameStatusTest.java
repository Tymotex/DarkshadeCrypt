package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Gamestatus;
import unsw.dungeon.Player;

public class GameStatusTest {
    private int initPlayerX = 3;
    private int initPlayerY = 3;
    private int width = 10;
    private int height = 10;

    /**
     * Tests 1 goal: whether getting to the exit results in level complete
     */
    @Test
    public void testExitGoalSimple(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.exit, initPlayerX + 1, initPlayerY));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.EXIT);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertTrue(gs.levelCompleted());
    }

    /**
     * Tests 2 goals: whether getting collecting all treasures and pushing all boulders
     * completes the level
     */
    @Test
    public void testGoalComplex1(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX + 2, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.boulder, initPlayerX + 2, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.floorSwitch, initPlayerX + 2, initPlayerY - 2));
        JSONArray goalSet = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.TREASURE))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.BOULDERS));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveUp();
        assertTrue(gs.levelCompleted());
    }

    /**
     * Tests whether getting to an exit before all treasures are collected 
     * prevents the player from completing the level
     */
    @Test
    public void testGoalComplex2(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.exit, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX + 1, initPlayerY - 1));
        JSONArray goalSet = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.TREASURE))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.EXIT));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveUp();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveDown();
        assertTrue(gs.levelCompleted());
    }

    /**
     * Test 4 part 1 - getting to an exit after killing all enemies
     * Tests nested subgoals. Eg. (clear enemies AND collect treasure) OR
     * (clear enemies AND get to exit)
     */
    @Test
    public void testGoalComplex3_1(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.sword, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX + 3, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX + 3, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.exit, initPlayerX + 4, initPlayerY));
        
        JSONArray goalSet1 = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.ENEMIES))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.TREASURE));
        JSONArray goalSet2 = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.ENEMIES))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.EXIT));
        
        JSONObject g1 = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet1);
        JSONObject g2 = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet2);
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.OR, new JSONArray().put(g1).put(g2));
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();
        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertTrue(gs.levelCompleted());
    }

    /**
     * Test 4 part 2 - getting all treasures after killing all enemies
     * Tests nested subgoals. Eg. (clear enemies AND collect treasure) OR
     * (clear enemies AND get to exit)
     */
    @Test
    public void testGoalComplex3_2(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.sword, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX + 3, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.treasure, initPlayerX + 3, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.exit, initPlayerX + 4, initPlayerY));
        JSONArray goalSet1 = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.ENEMIES))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.TREASURE));
        JSONArray goalSet2 = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.ENEMIES))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.EXIT));
        
        JSONObject g1 = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet1);
        JSONObject g2 = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet2);
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.OR, new JSONArray().put(g1).put(g2));
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();
        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveRight();
        assertFalse(gs.levelCompleted());
        player.moveUp();
        assertTrue(gs.levelCompleted());
    }

    /**
     * Test 5
     * Floor switch detoggling + exit test
     */
    @Test
    public void testGoalComplex4(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.boulder, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.floorSwitch, initPlayerX + 2, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.exit, initPlayerX + 2, initPlayerY - 1));
        JSONArray goalSet1 = new JSONArray()
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.BOULDERS))
            .put(JSONMapBuilder.createGoal(JSONMapBuilder.EXIT));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.AND, goalSet1);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();
        Player player = dungeon.getPlayer();
        Gamestatus gs = dungeon.getGameStatus();
        assertFalse(gs.levelCompleted());
        player.moveRight(); // Pushes the boulder onto the floor switch
        assertFalse(gs.levelCompleted());
        player.moveRight(); // Pushes the boulder off the floor switch
        assertFalse(gs.levelCompleted());
        player.moveUp();
        assertFalse(gs.levelCompleted());
        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        player.moveLeft();
        player.moveUp();
        player.moveLeft();
        assertTrue(gs.levelCompleted());
    }

}
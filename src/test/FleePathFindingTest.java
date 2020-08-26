package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Skeleton;
import unsw.dungeon.Player;

public class FleePathFindingTest {
    private int width = 10;
    private int height = 10;

    /**
     * Tests if enemy below and towards the right of the player flees
     * away from the player by following the direct path to the corner
     * lower right corner of the map
     */
    @Test(timeout = 3000)
    public void testFleeDirectPath1(){
        int initPlayerX = 2;
        int initPlayerY = 3;
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.invincibility, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX + 5, initPlayerY + 5));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.EXIT);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        // Picking up the invincibility potion
        player.moveRight(); 
        while (!dungeon.hasEntityOfType(9, 9, Skeleton.class)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

    /**
     * Tests if enemy above and towards the left of the player flees
     * away from the player by following the direct path to the corner
     * upper left corner of the map. The left upper corner is walled, so the
     * enemy's best position is (1, 1) 
     */
    @Test(timeout = 3000)
    public void testFleeDirectPath2(){
        int initPlayerX = 7;
        int initPlayerY = 6;
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.invincibility, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX - 1, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 2))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 3))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 4))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 5))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 6))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 7))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 8))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 0, 9))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 1, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 2, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 3, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 4, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 5, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 6, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 7, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 8, 0))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, 9, 0));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.EXIT);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        // Picking up the invincibility potion
        player.moveRight(); 
        // Enemy should flee to the top left walled corner
        while (!dungeon.hasEntityOfType(1, 1, Skeleton.class)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

}
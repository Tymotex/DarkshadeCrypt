package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class ChasePathFindingTest {
    private int initPlayerX = 3;
    private int initPlayerY = 3;
    private int width = 10;
    private int height = 10;

    /**
     * Tests if enemy can find a direct path to the player under a time constraint
     * in a map with no obstructions
     */
    @Test(timeout = 5000)
    public void testDirectPath(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX + 5, initPlayerY + 5));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.EXIT);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        while (player.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

    /**
     * Tests if enemy can find a complicated path to the player (with layers of obstructions)
     */
    @Test(timeout = 10000)
    public void testObstructedPath(){
        JSONArray entities = new JSONArray()
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.player, initPlayerX, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.enemy, initPlayerX + 5, initPlayerY + 5))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY - 2))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 2))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 3))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 4))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 5))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 1, initPlayerY + 6))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY - 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 1))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 2))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 3))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 4))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 5))
            .put(JSONMapBuilder.makeEnt(JSONMapBuilder.wall, initPlayerX + 3, initPlayerY + 6));
        JSONObject goal = JSONMapBuilder.createGoal(JSONMapBuilder.EXIT);
        JSONObject map = JSONMapBuilder.buildMap(width, height, entities, goal);
        DungeonMockLoader loader = new DungeonMockLoader(map);
        Dungeon dungeon = loader.load();

        Player player = dungeon.getPlayer();
        while (player.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

}
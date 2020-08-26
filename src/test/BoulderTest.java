package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import unsw.dungeon.Boulder;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Entity;
import unsw.dungeon.Player;

public class BoulderTest {
    private int initPlayerX = 2;
    private int initPlayerY = 2;

    /**
     * Test player can move 1 boulder
     */
    @Test
    public void test1Boulder(){
        // setting up environment
        // setting up boulders coord to right of player
        int[] b1 = {initPlayerX + 1,initPlayerY};
        int[][] boulders = {b1};
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, boulders, JSONMapBuilder.boulder);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();
        Boulder boulder = (Boulder) dungeon.getEntity(b1[0], b1[1], Boulder.class);
        player.moveRight();

        Entity entity = dungeon.getEntities(b1[0] + 1, b1[1]).get(0);

        // check boulder is at right position

        assertEquals(boulder.getX(), b1[0] + 1);
        assertEquals(boulder.getY(), b1[1]);

        assertEquals(boulder, entity);
    }

    /**
     * Test player pushing more than 1 boulder, boulder shouldn't move
     */
    @Test
    public void test2Boulder(){
        // setting up environment
        // setting up boulders coord to right of player
        int[] b1 = {initPlayerX + 1,initPlayerY};
        int[] b2 = {initPlayerX + 2,initPlayerY};
        int[][] boulders = {b1, b2};

        // add boulders to map
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, boulders, JSONMapBuilder.boulder);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();
        Boulder boulder1 = (Boulder) dungeon.getEntity(b1[0], b1[1], Boulder.class);
        Boulder boulder2 = (Boulder) dungeon.getEntity(b2[0], b2[1], Boulder.class);

        // move boulder 
        player.moveRight();

        // boulder shoudn't have moved, 
        Entity entity1 = dungeon.getEntities(b1[0], b1[1]).get(0);
        Entity entity2 = dungeon.getEntities(b2[0], b2[1]).get(0);

        // check both boulders are in right position, i.e. they haven't moved
        // check boulder 1
        assertEquals(boulder1.getX(), b1[0]);
        assertEquals(boulder1.getY(), b1[1]);

        assertEquals(boulder1, entity1);


        // check boulder2
        assertEquals(boulder2.getX(), b2[0]);
        assertEquals(boulder2.getY(), b2[1]);

        assertEquals(boulder2, entity2);
    }

    

}

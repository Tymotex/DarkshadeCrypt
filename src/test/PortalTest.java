package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

import unsw.dungeon.Boulder;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class PortalTest {
    private int initPlayerX = 3;
    private int initPlayerY = 3;

    /**
     * Test checks that teleportation works with player
     */
    @Test
    public void testBasicPortal() {
        // setting up environment

        // creating a pair of portals with same id
        // portal 1 to the left of the player
        int[] p1 = { initPlayerX - 1, initPlayerY, 1 };

        // position of portal 2
        int portal2X = 7;
        int portal2Y = 7;
        int[] p2 = { portal2X, portal2Y, 1 };
        int[][] portals = { p1, p2 };

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, portals, JSONMapBuilder.portal);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();

        // get key entities
        Player player = dungeon.getPlayer();

        // Portal portal1 = (Portal) dungeon.getEntities(initPlayerX - 1,
        // initPlayerY).get(0);
        // System.out.println(portal1.getX());
        // System.out.println(portal1.getY());

        // move player into portal
        player.moveLeft();

        // check player spawned adjacent to the portal
        int currPlayerX = player.getX();
        int currPlayerY = player.getY();
        int diffFromPortal2 = Math.abs(currPlayerX - portal2X) + Math.abs(currPlayerY - portal2Y);

        int expectedDiff = 1;
        assertEquals(expectedDiff, diffFromPortal2);
    }

    /**
     * Test involves pushing just 1 boulder into a portal
     */
    @Test
    public void test1BoulderPortal() {
        // setting up environment

        // creating a pair of portals with same id
        // portal 1 to the left of the player
        int[] p1 = { initPlayerX + 2, initPlayerY, 1 };

        // position of portal 2
        int portal2X = 7;
        int portal2Y = 7;
        int[] p2 = { portal2X, portal2Y, 1 };
        int[][] portals = { p1, p2 };

        // setting up boulder
        int[] b1 = { initPlayerX + 1, initPlayerY };
        int[][] boulders = { b1 };

        // JSONObject map = buildMap(portals, boulders);
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, portals, JSONMapBuilder.portal, boulders, JSONMapBuilder.boulder);

        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();

        // get key entities
        Player player = dungeon.getPlayer();
        Boulder boulder = (Boulder) dungeon.getEntity(b1[0], b1[1], Boulder.class);

        // Portal portal1 = (Portal) dungeon.getEntities(initPlayerX - 1,
        // initPlayerY).get(0);
        // System.out.println(portal1.getX());
        // System.out.println(portal1.getY());

        // make player push boulder into portal
        player.moveRight();

        // check boulder spawned adjacent to the portal
        int currBoulderX = boulder.getX();
        int currBoulderY = boulder.getY();
        int diffFromPortal2 = Math.abs(currBoulderX - portal2X) + Math.abs(currBoulderY - portal2Y);

        int expectedDiff = 1;
        assertEquals(expectedDiff, diffFromPortal2);
    }

    /**
     * Test involves pushing 4 boulders into a portal and then make player teleport
     * into portal
     */
    @Test
    public void test4BoulderPortal() {
        // setting up environment

        // creating a pair of portals with same id
        // portal 1 to the left of the player
        int[] p1 = { initPlayerX + 2, initPlayerY, 1 };

        // position of portal 2
        int portal2X = 7;
        int portal2Y = 7;
        int[] p2 = { portal2X, portal2Y, 1 };
        int[][] portals = { p1, p2 };

        // setting up boulders which are adjacent to portal
        int[] b1 = { p1[0] + 1, p1[1] };
        int[] b2 = { p1[0] - 1, p1[1] };
        int[] b3 = { p1[0], p1[1] + 1 };
        int[] b4 = { p1[0], p1[1] - 1 };
        int[][] boulders = { b1, b2, b3, b4 };

        // JSONObject map = buildMap(portals, boulders);
        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, portals, JSONMapBuilder.portal, boulders, JSONMapBuilder.boulder);

        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();

        // get key entities
        Player player = dungeon.getPlayer();
        ArrayList<Boulder> boulderArray = new ArrayList<>();
        for (int[] bCoord : boulders) {
            boulderArray.add((Boulder) dungeon.getEntity(bCoord[0], bCoord[1], Boulder.class));
        }

        // Portal portal1 = (Portal) dungeon.getEntities(initPlayerX - 1,
        // initPlayerY).get(0);
        // System.out.println(portal1.getX());
        // System.out.println(portal1.getY());

        // make player push first boulder into portal
        player.moveRight();

        // make player push second boulder into portal
        player.moveUp();
        player.moveUp();
        player.moveRight();
        player.moveDown();

        // make player push 3rd boulder into portal
        player.moveRight();
        player.moveRight();
        player.moveDown();
        player.moveLeft();

        // make player push 4th boulder into portal
        player.moveDown();
        player.moveDown();
        player.moveLeft();
        player.moveUp();

        // Player also tries to teleport
        int expectedPlayerX = player.getX();
        int expectedPlayerY = player.getY();
        player.moveUp();


        // check boulders spawned adjacent to the portal (every direction, there is a
        // boulder)
        System.out.println(boulders.length);

        for (Boulder boulder : boulderArray) {

            int currBoulderX = boulder.getX();
            int currBoulderY = boulder.getY();

            int diffFromPortal2 = Math.abs(currBoulderX - portal2X) + Math.abs(currBoulderY - portal2Y);
            int expectedDiff = 1;
            assertEquals(expectedDiff, diffFromPortal2);
        }

        // check player has not teleported
        assertEquals(expectedPlayerX, player.getX());
        assertEquals(expectedPlayerY, player.getY());
    }

    /**
     * Test involves pushing 5 boulders into a portal and then making sure the fifth
     * boulder could not be teleported.
     */
    @Test
    public void test5BoulderPortal() {
        // setting up environment

        // creating a pair of portals with same id
        // portal 1 to the left of the player
        int[] p1 = { initPlayerX + 2, initPlayerY, 1 };

        // position of portal 2
        int portal2X = 7;
        int portal2Y = 7;
        int[] p2 = { portal2X, portal2Y, 1 };
        int[][] portals = { p1, p2 };

        // setting up boulders which are adjacent to portal
        int[] b1 = { p1[0] + 1, p1[1] };
        int[] b2 = { p1[0] - 1, p1[1] };
        int[] b3 = { p1[0], p1[1] + 1 };
        int[] b4 = { p1[0], p1[1] - 1 };
        int[][] boulders = { b1, b2, b3, b4 };

        // JSONObject map = buildMap(portals, boulders);
        JSONObject map = JSONMapBuilder.buildMap(7, 6, portals, JSONMapBuilder.portal, boulders, JSONMapBuilder.boulder);

        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();

        // get key entities
        Player player = dungeon.getPlayer();
        ArrayList<Boulder> boulderArray = new ArrayList<>();
        for (int[] bCoord : boulders) {
            boulderArray.add((Boulder) dungeon.getEntity(bCoord[0], bCoord[1], Boulder.class));
        }

        // Portal portal1 = (Portal) dungeon.getEntities(initPlayerX - 1,
        // initPlayerY).get(0);
        // System.out.println(portal1.getX());
        // System.out.println(portal1.getY());

        // make player attempt to push a boulder through the portal
        player.moveDown();

        int expectedPlayerX = 7;
        int expectedPlayerY = 6; 

        // check player has not teleported
        assertEquals(expectedPlayerX, player.getX());
        assertEquals(expectedPlayerY, player.getY());
    }

}
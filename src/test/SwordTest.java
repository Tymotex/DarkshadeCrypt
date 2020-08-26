package test;

import org.json.JSONObject;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Enemy;
import unsw.dungeon.Skeleton;

public class SwordTest {
    private int initPlayerX = 1;
    private int initPlayerY = 1;

    /**
     * Testing that the sword can be picked up and that 5 limited uses
     * of the sword get used up.
     */
    @Test(timeout = 5000)
    public void testSwordKillsEnemy(){
        // setting up environment
        // setting up 6 enemys coord to right of player
        int[] enemy1 = {initPlayerX + 5,initPlayerY};

        int[][] enemys = {enemy1};

        // spawning sword next to player
        int[] sword = {initPlayerX + 1,initPlayerY };
        int[][] swords = { sword };

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, enemys, JSONMapBuilder.enemy, swords, JSONMapBuilder.sword);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Enemy enemy = (Enemy) dungeon.getEntity(initPlayerX + 5, initPlayerY, Skeleton.class);
        Player player = dungeon.getPlayer();
        player.moveRight();

        // Allow the enemy to approach the player and be killed
        while (enemy.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Testing that the sword can be picked up and that 5 limited uses
     * of the sword get used up.
     */
    @Test(timeout = 5000)
    public void test6Enemies1Sword(){
        // setting up environment
        // setting up 6 enemys coord to right of player
        int[] enemy1 = {initPlayerX + 5,initPlayerY + 5};
        int[] enemy2 = {initPlayerX + 5,initPlayerY + 4};
        int[] enemy3 = {initPlayerX + 5,initPlayerY + 3};
        int[] enemy4 = {initPlayerX + 5,initPlayerY + 2};
        int[] enemy5 = {initPlayerX + 5,initPlayerY + 1};
        int[] enemy6 = {initPlayerX + 5,initPlayerY + 6};

        int[][] enemys = {enemy1, enemy2, enemy3, enemy4, enemy5, enemy6};

        // spawning sword next to player
        int[] sword = {initPlayerX + 1,initPlayerY };
        int[][] swords = { sword };

        JSONObject map = JSONMapBuilder.buildMap(initPlayerX, initPlayerY, enemys, JSONMapBuilder.enemy, swords, JSONMapBuilder.sword);
        DungeonMockLoader loader = new DungeonMockLoader(map);

        Dungeon dungeon = loader.load();
        
        // get key entities
        Player player = dungeon.getPlayer();
        player.moveRight();

        while (player.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
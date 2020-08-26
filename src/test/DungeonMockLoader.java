package test;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.dungeon.Axe;
import unsw.dungeon.Boulder;
import unsw.dungeon.Door;
import unsw.dungeon.Draugr;
import unsw.dungeon.DungeonLoader;
import unsw.dungeon.Entity;
import unsw.dungeon.Exit;
import unsw.dungeon.FloorSwitch;
import unsw.dungeon.Ghost;
import unsw.dungeon.Ghoul;
import unsw.dungeon.InvincibilityPotion;
import unsw.dungeon.Key;
import unsw.dungeon.Mace;
import unsw.dungeon.Portal;
import unsw.dungeon.Skeleton;
import unsw.dungeon.Wisp;
import unsw.dungeon.SkeletonLady;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;
import unsw.dungeon.Wall;

public class DungeonMockLoader extends DungeonLoader {

    public DungeonMockLoader(JSONObject json) {
        super(json);
    }

    public DungeonMockLoader() {
        this(buildMap());
    }

    private static JSONObject buildMap() {
        JSONObject jsonString = new JSONObject();

        // build app
        jsonString.put("width", "30");
        jsonString.put("height", "30");

        // build entities
        JSONArray entities = new JSONArray();

        jsonString.put("entities", entities);

        // build goals
        JSONObject goalCondition = new JSONObject();
        goalCondition.put("goal", "boulders");
        jsonString.put("goal-condition", goalCondition);

        return jsonString;
    }

    @Override
    public void onLoad(Entity player) {
    }

    @Override
    public void onLoad(Wall wall) {
    }

    @Override
    public void onLoad(Boulder wall) {
    }

    @Override
    public void onLoad(Treasure treasure) {
    }

    @Override
    public void onLoad(InvincibilityPotion potion) {
    }

    @Override
    public void onLoad(Sword sword) {
    }

    @Override
    public void onLoad(Axe axe) {
    }

    @Override
    public void onLoad(Mace mace) {
    }

    @Override
    public void onLoad(Portal portal) {
    }

    @Override
    public void onLoad(FloorSwitch floorSwitch) {
    }

    @Override
    public void onLoad(Exit exit) {
    }

    @Override
    public void onLoad(Door door) {
    }

    @Override
    public void onLoad(Key key) {
    }

    @Override
    public void onLoad(Skeleton skeleton) {
    }

    @Override
    public void onLoad(Ghost ghost) {
    }

    @Override
    public void onLoad(Ghoul ghoul) {
    }

    @Override
    public void onLoad(Draugr draugr) {
    }

    @Override
    public void onLoad(Wisp wisp) {
    }

    @Override
    public void onLoad(SkeletonLady skeletonLady) {
    }

}
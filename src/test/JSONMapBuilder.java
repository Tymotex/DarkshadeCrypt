package test;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONMapBuilder {
    final public static String boulder = "boulder";
    final public static String portal = "portal";
    final public static String wall = "wall";
    final public static String exit = "exit";
    final public static String player = "player";
    final public static String treasure = "treasure";
    final public static String enemy = "enemy";
    final public static String invincibility = "invincibility";
    final public static String floorSwitch = "switch";
    final public static String key = "key";
    final public static String door = "door";
    final public static String sword = "sword";

    /**
     * Used to build json map with 1 type of non-player entity
     * 
     * @param initPlayerX
     * @param initPlayerY
     * @param entitys1 <p> - an array containing the x,y (optional id), coord of entity1Type</p>
     * @param entity1Type
     * @return
     */
    public static JSONObject buildMap(int initPlayerX, int initPlayerY, int[][] entitys1, String entity1Type) {
        JSONObject jsonString = new JSONObject();

        // build app
        jsonString.put("width", "8");
        jsonString.put("height", "8");

        // build entities
        JSONArray entities = new JSONArray();

        JSONObject player = new JSONObject();
        player.put("x", initPlayerX);
        player.put("y", initPlayerY);
        player.put("type", "player");
        entities.put(player);

        for (int[] entity1XY : entitys1) {
            JSONObject entity1 = new JSONObject();
            entity1.put("x", entity1XY[0]);
            entity1.put("y", entity1XY[1]);
            if (entity1XY.length == 3)
                entity1.put("id", entity1XY[2]);

            entity1.put("type", entity1Type);
            entities.put(entity1);
        }

        jsonString.put("entities", entities);

        // build generic goals
        JSONObject goalCondition = new JSONObject();
        goalCondition.put("goal", "exit");
        jsonString.put("goal-condition", goalCondition);

        return jsonString;
    }

    /**
     * Used for building maps with 2 types of non-player entities
     * @param initPlayerX
     * @param initPlayerY
     * @param entitys1
     * @param entity1Type
     * @param entitys2
     * @param entity2Type
     * @return
     */ 
    public static JSONObject buildMap(int initPlayerX, int initPlayerY, int[][] entitys1, String entity1Type, int[][] entitys2, String entity2Type) {
        JSONObject jsonString = new JSONObject();

        // build app
        jsonString.put("width", "8");
        jsonString.put("height", "8");


        // build entities
        JSONArray entities = new JSONArray();

        JSONObject player = new JSONObject();
        player.put("x", initPlayerX);
        player.put("y", initPlayerY);
        player.put("type", "player");
        entities.put(player);

        for (int[] entity1XY : entitys1) {
            JSONObject entity1 = new JSONObject();
            entity1.put("x", entity1XY[0]);
            entity1.put("y", entity1XY[1]);
            if (entity1XY.length == 3)
                entity1.put("id", entity1XY[2]);

            entity1.put("type", entity1Type);
            entities.put(entity1);
        }

        for (int[] entity2XY : entitys2) {
            JSONObject entity2 = new JSONObject();
            entity2.put("x", entity2XY[0]);
            entity2.put("y", entity2XY[1]);
            if (entity2XY.length == 3)
                entity2.put("id", entity2XY[2]);

            entity2.put("type", entity2Type);
            entities.put(entity2);
        }

        jsonString.put("entities", entities);

        // build generic goals
        JSONObject goalCondition = new JSONObject();
        goalCondition.put("goal", "exit");
        jsonString.put("goal-condition", goalCondition);

        return jsonString;
    }

    public static JSONObject makeEnt(String type, int x, int y) {
        JSONObject ent = new JSONObject();
        ent.put("type", type);
        ent.put("x", Integer.toString(x));
        ent.put("y", Integer.toString(y));
        return ent;
    } 

    public static JSONObject makeEnt(String type, int x, int y, int id) {
        JSONObject ent = new JSONObject();
        ent.put("type", type);
        ent.put("x", Integer.toString(x));
        ent.put("y", Integer.toString(y));
        ent.put("id", Integer.toString(id));
        return ent;
    } 

    public static JSONObject buildMap(int width, int height, JSONArray ents, JSONObject goalCondition) {
        JSONObject jsonString = new JSONObject();

        jsonString.put("width", Integer.toString(width));
        jsonString.put("height", Integer.toString(height));

        // build entities
        JSONArray entities = ents;

        jsonString.put("entities", entities);

        // build generic goals
        jsonString.put("goal-condition", goalCondition);

        return jsonString;
    }

    public static final String EXIT = "exit";
    public static final String ENEMIES = "enemies";
    public static final String TREASURE = "treasure";
    public static final String BOULDERS = "boulders";

    public static final String AND = "AND";
    public static final String OR = "OR";
    

    static JSONObject createGoal(String goalType) {
        JSONObject goal = new JSONObject();
        goal.put("goal", goalType);
        return goal;
    }

    static JSONObject createGoal(String operand, JSONArray subgoal) {
        JSONObject goal = new JSONObject();
        goal.put("goal", operand);
        goal.put("subgoals", subgoal);
        return goal;
    }

    public static void main(String args[]) {
        System.out.println("Testing");
        JSONArray goalSet1 = new JSONArray();
        goalSet1.put(JSONMapBuilder.createGoal(EXIT));
        goalSet1.put(JSONMapBuilder.createGoal(TREASURE));
        JSONObject goalTree = JSONMapBuilder.createGoal(AND, goalSet1);
        System.out.println(goalTree);
    }
}


package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
// Timer
// Timer
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
// Printing queue
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 */
public abstract class DungeonLoader {
    private JSONObject json;

    // Temporary class variables
    int enemyCount = 0;
    int treasureCount = 0;
    int floorSwitchCount = 0;
    List<Entity> chasers = new ArrayList<>();

    private List<FloorSwitch> switches = new ArrayList<>();

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    // used for building our own json maps for junit testing
    public DungeonLoader(JSONObject json) {
        this.json = json;
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        JSONObject goalConditions = json.getJSONObject("goal-condition");

        Gamestatus gamestatus = new Gamestatus();
        Dungeon dungeon = new Dungeon(width, height, gamestatus);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }



        Debug.printC("===== Constructing the goal tree =====");
        GoalComponent resultTree = getGoals(goalConditions);
        Debug.printC("===== Done constructing goal tree =====");
        showTree(resultTree);

        
        dungeon.getGameStatus().setGoalTree(resultTree);;

        for (FloorSwitch floorSwitch : switches) {
            if (floorSwitch.updateState()) {
                System.out.println("Gay");
                floorSwitch.toggle();
            }
        }

        // System.out.println(chasers);
        for (Entity chaser : chasers) {
            int clockCycle = 250;
            if (chaser instanceof Enemy) {
                Enemy enemy = (Enemy) chaser;
                clockCycle = enemy.getSpeed();
                enemy.setPathfinding(new ChasingPathFinder(dungeon.getObstructionMatrix(), dungeon));
            }

            KeyFrame chaseTick = new KeyFrame(Duration.millis(clockCycle), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (chaser instanceof Enemy) {
                        Enemy enemy = (Enemy) chaser;
                        enemy.chasePlayer();
                    }
                }
            });
            Timeline chaseTimeline = new Timeline(chaseTick);
            chaseTimeline.setCycleCount(Timeline.INDEFINITE);

            dungeon.addTimer(chaseTimeline);
            if (chaser instanceof Enemy) {
                Enemy eachEnemy = (Enemy) chaser;
                eachEnemy.refreshObstructionMatrix();
                eachEnemy.setEnemyTimeline(chaseTimeline);
                eachEnemy.attach(dungeon.getGameStatus());
            }


            KeyFrame startChase = new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    chaseTimeline.play();
                }
            });

            Timeline initialDelay = new Timeline(new KeyFrame(Duration.seconds(5)), startChase);
            initialDelay.play();
            dungeon.addTimer(initialDelay);
        }
        System.out.println(resultTree.getGoalString(0));
        dungeon.linkDoorsWithKeys();
        return dungeon;
    }

// =================================================================================

    /**
     * Parses the JSON goals and constructs the goal tree
     */
    private GoalComponent getGoals(JSONObject goalObj) {
        GoalComponent currTree = null;
        String goalStr = goalObj.get("goal").toString();
        if (goalStr.equals("AND") || goalStr.equals("OR")) {
            // Composite
            currTree = new GoalComposite(goalStr);
            JSONArray subgoals = goalObj.getJSONArray("subgoals");
            // For each goal in the subgoals, add them as children
            for (Object eachGoal : subgoals) {
                JSONObject currGoal = (JSONObject) eachGoal;
                System.out.println(" ===> Adding child: " + currGoal.get("goal"));
                currTree.addGoal(getGoals(currGoal));
            }
        } else {
            // Leaf
            Goal goal;
            switch (goalStr) {
                case "exit":
                    goal = new ExitGoal();
                    break;
                case "treasure":
                    goal = new TreasureGoal(treasureCount);
                    break;
                case "enemies":
                    goal = new EnemyGoal(enemyCount);
                    break;
                case "boulders":
                    goal = new FloorSwitchGoal(floorSwitchCount);
                    break;
                default:
                    goal = new ExitGoal();
                    break;
            }
            currTree = new GoalLeaf(goal);
        }
        return currTree;
    }

    public void showTree(GoalComponent goalTree) {
        Queue<GoalComponent> printQ = new LinkedList<GoalComponent>();
        printQ.add(goalTree);
        Debug.printC("Goal components in level order: ", Debug.BLUE);
        while(printQ.peek() != null) {
            GoalComponent curr = printQ.remove(); 
            
            Debug.printC(curr.toString());
            if (curr.getChildren() != null) {
                for (GoalComponent eachGoal : curr.getChildren()) {
                    printQ.add(eachGoal);
                }
            }
        }
        Debug.printC("Done showing the goal component tree", Debug.BLUE);
    }   

// =================================================================================

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");
        int id;

        Entity entity = null;
        switch (type) {
            case "player":
                Player player = new Player(x, y, dungeon);
                dungeon.setPlayer(player);
                onLoad(player);
                entity = player;
                break;
            case "wall":
                Wall wall = new Wall(x, y, dungeon);
                onLoad(wall);
                entity = wall;
                break;
            case "boulder":
                Boulder boulder = new Boulder(x, y, dungeon);
                onLoad(boulder);
                entity = boulder;
                break;
            case "treasure":
                Treasure treasure = new Treasure(x, y, dungeon);
                treasureCount++;
                onLoad(treasure);
                entity = treasure;
                treasure.attach(dungeon.getGameStatus());
                break;
            case "invincibility":
                InvincibilityPotion potion = new InvincibilityPotion(x, y, dungeon);
                onLoad(potion);
                entity = potion;
                break;
            case "sword":
                Sword sword = new Sword(x, y, dungeon);
                onLoad(sword);
                entity = sword;
                break;
            case "axe":
                Axe axe = new Axe(x, y, dungeon);
                onLoad(axe);
                entity = axe;
                break;
            case "mace":
                Mace mace = new Mace(x, y, dungeon);
                onLoad(mace);
                entity = mace;
                break;
            case "portal":
                Debug.printC("LOADING PORTAL", Debug.PURPLE);
                id = json.getInt("id");
                Portal portal = new Portal(x, y, dungeon, id);
                // pair portals
                Portal pair = (Portal) dungeon.getEntityByID(id);
                portal.pairPortal(pair);
                onLoad(portal);
                entity = portal;
                break;
            case "exit":
                Exit exit = new Exit(x, y, dungeon);
                onLoad(exit);
                entity = exit;
                exit.attach(dungeon.getGameStatus());
                break;
            case "switch":
                FloorSwitch floorSwitch = new FloorSwitch(x, y, dungeon);
                onLoad(floorSwitch);
                entity = floorSwitch;
                switches.add(floorSwitch);
                floorSwitchCount++;
                floorSwitch.attach(dungeon.getGameStatus());
                break;
            case "door":
                id = json.getInt("id");
                Door door = new Door(x, y, dungeon, id);
                onLoad(door);
                entity = door;
                break;
            case "key":
                id = json.getInt("id");
                Key key = new Key(x, y, dungeon, id);
                onLoad(key);
                entity = key;
                break;
            case "enemy":
                enemyCount++;
                Skeleton enemy = new Skeleton(x, y, dungeon);
                onLoad(enemy);
                entity = enemy;
                this.chasers.add(enemy);
                break;
            case "skeleton":
                enemyCount++;
                Skeleton skeleton = new Skeleton(x, y, dungeon);
                onLoad(skeleton);
                entity = skeleton;
                this.chasers.add(skeleton);
                break;
            case "ghost":
                enemyCount++;
                Ghost ghost = new Ghost(x, y, dungeon);
                onLoad(ghost);
                entity = ghost;
                this.chasers.add(ghost);
                break;
            case "ghoul":
                enemyCount++;
                Ghoul ghoul = new Ghoul(x, y, dungeon);
                onLoad(ghoul);
                entity = ghoul;
                this.chasers.add(ghoul);
                break;
            case "draugr":
                enemyCount++;
                Draugr draugr = new Draugr(x, y, dungeon);
                onLoad(draugr);
                entity = draugr;
                this.chasers.add(draugr);
                break;
            case "wisp":
                enemyCount++;
                Wisp wisp = new Wisp(x, y, dungeon);
                onLoad(wisp);
                entity = wisp;
                this.chasers.add(wisp);
                break;
            case "skeletonlady":
                enemyCount++;
                SkeletonLady skeletonLady = new SkeletonLady(x, y, dungeon);
                onLoad(skeletonLady);
                entity = skeletonLady;
                this.chasers.add(skeletonLady);
                break;
        }
        dungeon.addEntity(entity);
    }

    public abstract void onLoad(Entity player);

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Boulder wall);

    public abstract void onLoad(Treasure treasure);

    public abstract void onLoad(InvincibilityPotion potion);

    public abstract void onLoad(Sword sword);

    public abstract void onLoad(Axe axe);

    public abstract void onLoad(Mace mace);

    public abstract void onLoad(Portal portal);

    public abstract void onLoad(FloorSwitch floorSwitch);

    public abstract void onLoad(Skeleton skeleton);

    public abstract void onLoad(Ghost ghost);

    public abstract void onLoad(Draugr draugr);

    public abstract void onLoad(Ghoul ghoul);

    public abstract void onLoad(Wisp wisp);

    public abstract void onLoad(SkeletonLady skeletonLady);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(Door door); 

    public abstract void onLoad(Key key);
    
}

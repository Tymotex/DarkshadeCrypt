package unsw.dungeon;

import java.util.List;

public interface GoalComponent {

    /**
     * 
     * @return returns whether the goals in the goal lists are completed
     */
    public boolean isGoalComplete();

    /**
     * Adds goal to goal composite, does nothing if this is a goal leaf
     * @param newGoal
     */
    public void addGoal(GoalComponent newGoal);

    /**
     * @return a list of goal components which this goal composite has as children 
     */
    public List<GoalComponent> getChildren();

    /**
     * Updates all leafs of type goalEntity with either 
     * @param Entity goalEntity
     * @param boolean isSuccess
     */
    public void updateGoalTree(Entity goalEntity, boolean isSuccess);

    /**
     * @return whether player can exit / end the game now
     */
    public boolean playerCanExit();

    // getters and setters
    public Goal getGoal();

    public String getTreasureCount();
    public String getEnemyCount();
    public String getSwitchCount();

    public String getGoalString(int indentLevel);
}

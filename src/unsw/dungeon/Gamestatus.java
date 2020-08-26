package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gamestatus implements Observer {
    private GoalComponent goalTree;
    BooleanProperty levelIsCompleted = new SimpleBooleanProperty(false);
    private StringProperty treasureGoalProgress = new SimpleStringProperty("None"); 
    private StringProperty enemyGoalProgress = new SimpleStringProperty("None"); 
    private StringProperty switchGoalProgress = new SimpleStringProperty("None"); 

    /**
     * Constructor for Gamestatus
     */
    public Gamestatus() {}
    
    /**
     * Updates the goal tree 
     * @param Subject subject
     */
    @Override
    public void update(Subject subject) {
        Entity entitySubj = (Entity) subject;
        if (subject instanceof FloorSwitch) {
            FloorSwitch floorSwitch = (FloorSwitch) subject;
            goalTree.updateGoalTree(entitySubj, floorSwitch.getState());
        } else if (subject instanceof Enemy) {
            goalTree.updateGoalTree(entitySubj, true);
        } else if (subject instanceof Exit) {
            goalTree.updateGoalTree(entitySubj, true);
            if (!goalTree.playerCanExit()) {
                goalTree.updateGoalTree(entitySubj, false);
            }
        } else if (subject instanceof Treasure) {
            goalTree.updateGoalTree(entitySubj, true);
        }
        updateGoalProgress();
        levelIsCompleted.set(levelCompleted());
        // goalString.set(goalTree.getGoalString(0));
    }
    
    /**
     * Returns whether this level is completed
     * @return
     */
    public boolean levelCompleted() {
        return goalTree.isGoalComplete();
    }

    public GoalComponent getGoalTree() {
        return goalTree;
    }

    public void setGoalTree(GoalComponent goalTree) {
        this.goalTree = goalTree;
        updateGoalProgress();
    }

    /**
     * Gets the formatted string of win conditions for the current level
     * @return
     */
    public String getObjectiveString() {
        return goalTree.getGoalString(0);
    }

    /**
     * Updates each of the StringProperty's goal tracker contents
     */
    private void updateGoalProgress() {
        treasureGoalProgress.set(goalTree.getTreasureCount());
        switchGoalProgress.set(goalTree.getSwitchCount());
        enemyGoalProgress.set(goalTree.getEnemyCount());
    }
    
    public StringProperty getTreasureGoalProgress() {
        return treasureGoalProgress;
    }
    
    public StringProperty getEnemyGoalProgress() {
        return enemyGoalProgress;
    }

    public StringProperty getSwitchGoalProgress() {
        return switchGoalProgress;
    }
}
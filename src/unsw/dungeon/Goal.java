package unsw.dungeon;

public interface Goal {
    /**
     * Decreases number of this perticular goal which is left by 1,
     * e.g. 1 enemy dies, updateSuccess on enemy
     */
    public void updateSuccess();

    /**
     * Increases number of this perticular goal which is left by 1,
     * e.g. 1 switch is untoggled, updateSuccess on floorSwitchGoal
     */
    public void updateFailure();

    /**
     * @return whether the goal is completed , i.e 0 left of enemies, switch, treasure
     */
    public boolean isComplete();

    /**
     * @return percentage 
     */
    public String showProgress();

    /**
     * @return formatted string summary of the goal
     */
    public String getGoalString();
}


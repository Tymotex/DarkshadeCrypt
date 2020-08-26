package unsw.dungeon;

public class FloorSwitchGoal implements Goal {
    private int floorSwitchesRemaining;
    private int floorSwitchCount;

    /**
     * Constructor for this Goal object
     * @param floorSwitchCount
     */
    public FloorSwitchGoal(int floorSwitchCount) {
        this.floorSwitchesRemaining = floorSwitchCount;
        this.floorSwitchCount = floorSwitchCount;
    }

    @Override
    public void updateSuccess() {
        floorSwitchesRemaining--;
    }

    @Override
    public void updateFailure() {
        floorSwitchesRemaining++;
    }

    @Override
    public String toString() {
        return "FloorSwitchGoal (remaining: " + floorSwitchesRemaining + ")";
    }

    @Override
    public boolean isComplete() {
        return (floorSwitchesRemaining == 0);
    }

    @Override
    public String showProgress() {
        return (floorSwitchCount == 0) ? "None" : (floorSwitchCount - floorSwitchesRemaining) + "/" + floorSwitchCount;
    }

    @Override
    public String getGoalString() {
        return (this.floorSwitchesRemaining == 1) ? "Activate 1 switch" : "Activate " + this.floorSwitchesRemaining + " switches";
    }

}
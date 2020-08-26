package unsw.dungeon;

public class TreasureGoal implements Goal {
    private int treasureRemaining;
    private int treasureCount;

    public TreasureGoal(int treasureCount) {
        this.treasureRemaining = treasureCount;
        this.treasureCount = treasureCount;
    }

    @Override
    public void updateSuccess() {
        treasureRemaining--;
        Debug.printC("Treasures remaining: " + treasureRemaining);
    }

    @Override
    public void updateFailure() {
        treasureRemaining++;
    }

    @Override
    public String toString() {
        return "TreasureGoal (remaining: " + treasureRemaining + ")";
    }

    @Override
    public boolean isComplete() {
        return (treasureRemaining == 0);
    }

    @Override
    public String showProgress() {
        return (treasureCount == 0) ? "None" : (treasureCount - treasureRemaining) + "/" + treasureCount; 
    }

    @Override
    public String getGoalString() {
        return (this.treasureRemaining == 1) ? "Collect 1 coin" : "Collect " + this.treasureRemaining + " coins";
    }
}

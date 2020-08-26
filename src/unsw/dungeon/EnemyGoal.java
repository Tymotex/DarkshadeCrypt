package unsw.dungeon;

public class EnemyGoal implements Goal {
    private int enemiesRemaining;
    private int enemyCount;

    /**
     * Constructor for EnemyGoal. Takes in how many enemies to track
     * 
     * @param enemyCount
     */
    public EnemyGoal(int enemyCount) {
        this.enemiesRemaining = enemyCount;
        this.enemyCount = enemyCount;
    }

    @Override
    public void updateSuccess() {
        enemiesRemaining--;
        Debug.printC("Reducing enemies remaining. Now there's " + enemiesRemaining, Debug.YELLOW);
    }

    @Override
    public void updateFailure() {
        enemiesRemaining++;
    }

    @Override
    public String toString() {
        return "EnemyGoal (remaining: " + enemiesRemaining + ")";
    }

    @Override
    public boolean isComplete() {
        return (enemiesRemaining == 0);
    }

    @Override
    public String showProgress() {
        return (enemyCount == 0) ? "None" : (enemyCount - enemiesRemaining) + "/" + enemyCount; 
    }

    @Override
    public String getGoalString() {
        return (this.enemiesRemaining == 1) ? "Kill 1 enemy" : "Kill " + this.enemiesRemaining + " enemies";
    }

}
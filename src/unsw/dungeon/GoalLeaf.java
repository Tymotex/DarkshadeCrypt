package unsw.dungeon;

import java.util.List;

public class GoalLeaf implements GoalComponent {
    private Goal goal;

    /**
     * Constructor for goals containing a single simple objective
     * 
     * @param goal
     */
    public GoalLeaf(Goal goal) {
        System.out.println(" ---> Constructing goal leaf: " + goal.toString());
        this.goal = goal;
    }

    @Override
    public boolean isGoalComplete() {
        // Debug.printC(this + " is complete? " + goal.isComplete(), Debug.YELLOW);
        return goal.isComplete();
    }
    
    @Override
    public void addGoal(GoalComponent newGoal) {
        return;
    }

    @Override
    public String toString() {
        return "Leaf goal: " + goal.toString();
    }

    @Override
    public List<GoalComponent> getChildren() {
        return null;
    }

    private void handleUpdating(boolean isSuccess) {
        if (isSuccess) {
            goal.updateSuccess();
        } else {
            goal.updateFailure();
        }
    }

    @Override
    public void updateGoalTree(Entity goalEntity, boolean isSuccess) {
        if (goal.getClass() == EnemyGoal.class && goalEntity instanceof Enemy) {
            handleUpdating(isSuccess);
        } else if (goal.getClass() == TreasureGoal.class && goalEntity.getClass() == Treasure.class) {
            handleUpdating(isSuccess);
        } else if (goal.getClass() == FloorSwitchGoal.class && goalEntity.getClass() == FloorSwitch.class) {
            handleUpdating(isSuccess);
        } else if (goal.getClass() == ExitGoal.class && goalEntity.getClass() == Exit.class) {
            handleUpdating(isSuccess);
        }
    }

    @Override
    public boolean playerCanExit() {
        if (goal instanceof ExitGoal) {
            return true;
        } else {
            if (goal.isComplete()) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Goal getGoal() {
        return goal;
    }

    @Override
    public String getGoalString(int indentLevel) {
        String goalStr = "";
        for (int i = 0; i < indentLevel; i++) {
            goalStr += "  ";
        }
        return goalStr + "• " + goal.getGoalString() + "\n";
    }


    @Override
    public String getTreasureCount() {
        return (goal.getClass() == TreasureGoal.class) ? (goal.showProgress()) : ("None");
    }

    @Override
    public String getEnemyCount() {
        return (goal.getClass() == EnemyGoal.class) ? (goal.showProgress()) : ("None");
    }

    @Override
    public String getSwitchCount() {
        return (goal.getClass() == FloorSwitchGoal.class) ? (goal.showProgress()) : ("None");
    }

}
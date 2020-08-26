package unsw.dungeon;

import java.util.List;
import java.util.ArrayList;

public class GoalComposite implements GoalComponent {

    private String operand;
    public List<GoalComponent> children = new ArrayList<GoalComponent>();

    /**
     * Constructor for composite goal objects
     * @param operand
     */
    public GoalComposite(String operand) {
        this.operand = operand;
    }

    @Override
    public boolean isGoalComplete() {
        if (operand.equals("AND")) {
            for (GoalComponent eachGoal : children) {
                if (!eachGoal.isGoalComplete()) {
                    return false;
                }
            }
            return true;
        } else if (operand.equals("OR")) {
            for (GoalComponent eachGoal : children) {
                if (eachGoal.isGoalComplete()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void addGoal(GoalComponent newGoal) {
        children.add(newGoal);
        System.out.println(" ---> Added new goal");
    }

    @Override
    public String toString() {
        return "Composite goal: " + operand;
    }

    @Override
    public List<GoalComponent> getChildren() {
        return children;
    }

    @Override
    public void updateGoalTree(Entity goalEntity, boolean isSuccess) {
        for (GoalComponent eachGoal : children) {
            eachGoal.updateGoalTree(goalEntity, isSuccess);
        }
    }

    @Override
    public boolean playerCanExit() {
        if (operand.equals("AND")) {
            for (GoalComponent eachGoal : children) {
                if (!eachGoal.playerCanExit()) {
                    return false;
                }
            }
            return true;
        } else if (operand.equals("OR")) {
            for (GoalComponent eachGoal : children) {
                if (eachGoal.getGoal() instanceof ExitGoal) {
                    return true;
                } else if (eachGoal.playerCanExit()) {
                    return true;
                } 
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public Goal getGoal() {
        return null;
    }

    @Override
    public String getGoalString(int indentLevel) {
        String goalString = "";
        for (int i = 0; i < indentLevel; i++) goalString += "  ";
        
        if (operand.equals("AND")) {
            // "goal 1" AND "goal 2" AND ...
            goalString += "Do all of:\n";
            for (int i = 0; i < children.size(); i++) {
                goalString += children.get(i).getGoalString(indentLevel + 1);
            }
        } else if (operand.equals("OR")) {
            // Either "goal 1" or "goal 2" or ...
            goalString += "Either:\n";
            for (int i = 0; i < children.size(); i++) {
                goalString += children.get(i).getGoalString(indentLevel + 1);
                for (int j = 0; j < indentLevel; j++) goalString += "  ";
                if (i < children.size() - 1) goalString += "Or:\n";
            }
        }
        return goalString;
    }

    @Override
    public String getTreasureCount() {
        for (GoalComponent child : children) {
            String result = child.getTreasureCount();
            if (!result.equals("None")) {
                return result;
            }
        }
        return "None";
    }

    @Override
    public String getEnemyCount() {
        for (GoalComponent child : children) {
            String result = child.getEnemyCount();
            if (!result.equals("None")) {
                return result;
            }
        }
        return "None";
    }

    @Override
    public String getSwitchCount() {
        for (GoalComponent child : children) {
            String result = child.getSwitchCount();
            if (!result.equals("None")) {
                return result;
            }
        }
        return "None";
    }

}


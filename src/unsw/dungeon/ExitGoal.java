package unsw.dungeon;

public class ExitGoal implements Goal {
    private boolean exited = false;

    @Override
    public void updateSuccess() {
        exited = true;
        System.out.println("Exit. Level complete!");
    }

    @Override
    public void updateFailure() {
        exited = false;
    }

    @Override
    public String toString() {
        return "ExitGoal";
    }

    @Override
    public boolean isComplete() {
        return exited;
    }

    @Override
    public String showProgress() {
        return exited ? "Exited" : "Haven't exited yet";
    }

    @Override
    public String getGoalString() {
        return "Get to the exit";
    }

}
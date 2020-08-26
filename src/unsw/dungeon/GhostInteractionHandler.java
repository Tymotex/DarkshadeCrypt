package unsw.dungeon;

public class GhostInteractionHandler extends EnemyInteractionHandler {
    @Override
    boolean handlesObstruction(){
        return false;
    }
}
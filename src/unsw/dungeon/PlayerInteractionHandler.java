package unsw.dungeon;

public class PlayerInteractionHandler extends InteractionHandler {
    
    @Override
    boolean handlesBoulder(){
        return true;
    }

    @Override
    boolean handlesExit(){
        return true;
    }

    @Override
    boolean handlesDoor(){
        return true;
    }

    @Override
    boolean handlesEnemy(){
        return true;
    }

    @Override
    boolean handlesCollectible(){
        return true;
    }
}
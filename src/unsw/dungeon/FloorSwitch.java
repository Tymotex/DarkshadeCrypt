package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class FloorSwitch extends Entity implements Subject {

    private List<Observer> observers = new ArrayList<>();
    private boolean state = false;

    /**
     * Constructor for FloorSwitch
     * @param x
     * @param y
     * @param dungeon
     */
    public FloorSwitch(int x, int y, Dungeon dungeon){
        super(x, y, dungeon);
    }

    /**
     * Toggles the state of this switch
     * @return state
     */
    public boolean toggle() {
        if (state) {
            SoundPlayer.playSFX("switch_off.wav");
        } else {
            SoundPlayer.playSFX("switch_on.wav");
        }
        state = ! state;
        notifyObservers();
        return state;
    }
    
    /**
     * Subscribes an observer to this current floorswitch subject instance
     * @param Observer o
     */
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    /**
     * Unsubscribes an observer to this current floorswitch subject instance
     * @param Observer o
     */
    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    /**
     * Updates all the currently subscribed observers with the status of this subject
     */
    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(this);
        }
    }
    
    /**
     * Used to update initial state of the boudler 
     * @return true if 
     */
    public boolean updateState() {
        boolean boulderIsAbove = dungeon.hasEntityOfType(getX(), getY(), Boulder.class);
        return boulderIsAbove;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}
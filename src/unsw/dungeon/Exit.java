package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Exit extends Entity implements Subject, Obstruction {
    
    private List<Observer> observers = new ArrayList<>();
    
    /**
     * Constructor for Exit
     * @param x
     * @param y
     * @param dungeon
     */
    public Exit(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    /**
     * Subscribes an observer to this current exit subject instance
     * @param Observer o
     */
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    /**
     * Unsubscribes an observer to this current exit subject instance
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
     * Updates the game 
     */
    public void exit() {
        // showDetails();
        notifyObservers();
    }

    /**
     * Behaves like a wall
     */
    @Override
    public boolean canPass() {
        return false;
    }
    
}
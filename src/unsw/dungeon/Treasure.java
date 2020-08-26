package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Treasure extends Entity implements Subject, Collectible {

    private List<Observer> observers = new ArrayList<>();

    public Treasure(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(this);
        }
    }

    @Override
    public void pickup(Player p) {
        SoundPlayer.playSFX("treasure_collect.wav");

        // update observers
        notifyObservers();
        // Make this object disappear
        die();
    }

}
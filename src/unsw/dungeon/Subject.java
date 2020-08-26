package unsw.dungeon;

public interface Subject {
    
    /**
     * Loop through all subscribed observers and updates them
     */
    public void notifyObservers();
    
    /**
     * Subscribes observer to this subject instance
     * @param Observer o
     */
    public void attach (Observer o);

    /**
     * Unsubscribes observer to this subject instance
     * @param Observer o
     */
    public void detach (Observer o);
}
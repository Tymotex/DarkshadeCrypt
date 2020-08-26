package unsw.dungeon;

public interface Observer {
    /**
     * Given the subject, updates necessary fields and takes necessary action
     * after changes to the subject
     * @param s
     */
    public void update(Subject s);
}
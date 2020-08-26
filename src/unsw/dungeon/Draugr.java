package unsw.dungeon;

public class Draugr extends Enemy {
    /**
     * Constructor for Draugr
     * @param x
     * @param y
     * @param dungeon
     */
    public Draugr(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        setSpeed(300);
    }

    @Override
    public void playFootstep() {
        SoundPlayer.playSFX("draugr_footstep.wav");
    }
}

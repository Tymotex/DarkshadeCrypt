package unsw.dungeon;

public class Ghoul extends Enemy {
    /**
     * Constructor for Ghoul
     * @param x
     * @param y
     * @param dungeon
     */
    public Ghoul(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        setSpeed(500);
    }

    @Override
    public void playFootstep() {
        SoundPlayer.playSFX("ghoul_footstep.wav");
    }
}
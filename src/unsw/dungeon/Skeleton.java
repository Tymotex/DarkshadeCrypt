package unsw.dungeon;

public class Skeleton extends Enemy {
    /**
     * Constructor for Skeleton
     * @param x
     * @param y
     * @param dungeon
     */
    public Skeleton(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        setSpeed(1000);
    }

    @Override
    public void playFootstep() {
        SoundPlayer.playSFX("skeleton_step.wav");
    }
}

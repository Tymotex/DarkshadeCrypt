package unsw.dungeon;

public class SkeletonLady extends Enemy {
    /**
     * Constructor for Skeleton
     * @param x
     * @param y
     * @param dungeon
     */
    public SkeletonLady(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        setSpeed(600);
    }

    @Override
    public void playFootstep() {
        SoundPlayer.playSFX("skeletonlady_footstep.wav");
    }
}
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.animation.Timeline;

public abstract class Enemy extends Entity implements Subject {
    private InteractionHandler interactionHandler;
    private PathFinding pathFinder;
    private List<Observer> observers = new ArrayList<>();
    private Stack<Coordinate> pathToDest;
    private Timeline enemyTimeline;
    private boolean isAlive = true;
    private int speed = 1000;

    /**
     * Constructor for Enemy
     * @param x
     * @param y
     * @param dungeon
     */
    public Enemy(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        this.dungeon = dungeon;
        this.interactionHandler = new EnemyInteractionHandler();
        this.pathToDest = new Stack<Coordinate>();
    }

    @Override
    public void die() {
        isAlive = false;
        Debug.printC(this + " is dying.", Debug.PURPLE);
        notifyObservers();
        enemyTimeline.stop();
        dungeon.removeTimer(enemyTimeline);
        super.die();
    }

    /**
     * Stops enemy from moving
     */
    public void stop() {
        enemyTimeline.stop();
    }

    /**
     * Makes enemy start moving
     */
    public void start() {
        enemyTimeline.play();
    }

    /**
     * Refreshes the obstruction matrix for the pathfinding algorithm to be
     * responsive to the changing environment and target coordinate
     */
    public void refreshObstructionMatrix() {
        int obstruct[][] = dungeon.getObstructionMatrix();
        if (pathFinder != null) {
            this.pathFinder.refreshObstructionMatrix(obstruct);
        }
        // dungeon.showMatrix();
    }

    /**
     * Makes one step towards the player's current position
     */
    public void chasePlayer() {
        refreshObstructionMatrix();
        Coordinate currPosition = new Coordinate(getX(), getY());
        Player player = this.dungeon.getPlayer();
        Coordinate playerPosition = new Coordinate(player.getX(), player.getY());

        if (currPosition.equals(playerPosition)) {
            // Debug.printC("Enemy has reached the player!", Debug.RED);
            player.useItem(this);
        } else {
            this.pathToDest = pathFinder.getPathToDest(currPosition, playerPosition);
            if (!this.pathToDest.empty()) {
                Coordinate nextPosition = this.pathToDest.pop();
                // this.setCoordinate(nextPosition);
                if (getX() + 1 == nextPosition.x) {
                    moveRight();
                } else if (getX() - 1 == nextPosition.x) {
                    moveLeft();
                } else if (getY() + 1 == nextPosition.y) {
                    moveDown();
                } else if (getY() - 1 == nextPosition.y) {
                    moveUp();
                }
            }
        }
    }

    public abstract void playFootstep();

    /**
     * Handles any interactions with the entity in the target coordinate and moves
     * the current entity to that coordinate, if possible
     * 
     * @param targetCoord
     * @param stepDirection
     */
    private void handleMove(Coordinate targetCoord, String stepDirection) {
        targetCoord = interactionHandler.handlePortal(targetCoord, dungeon);
        if (targetCoord == null)
            return;
        if (interactionHandler.handleInteraction(this, targetCoord, dungeon, stepDirection)) {
            this.setCoordinate(targetCoord);
        }
        // showDetails();
    }

    /**
     * Method tries to moves this instance of the class enemy up from its current
     * position
     */
    public void moveUp() {
        Coordinate upCoord = new Coordinate(getX(), getY() - 1);
        handleMove(upCoord, InteractionHandler.UP);
    }

    /**
     * Method tries to moves this instance of the class enemy down from its current
     * position
     */
    public void moveDown() {
        Coordinate downCoord = new Coordinate(getX(), getY() + 1);
        handleMove(downCoord, InteractionHandler.DOWN);
    }

    /**
     * Method tries to moves this instance of the class enemy left from its current
     * position
     */
    public void moveLeft() {
        Coordinate leftCoord = new Coordinate(getX() - 1, getY());
        handleMove(leftCoord, InteractionHandler.LEFT);
    }

    /**
     * Method tries to moves this instance of the class enemy right from its current
     * position
     */
    public void moveRight() {
        Coordinate rightCoord = new Coordinate(getX() + 1, getY());
        handleMove(rightCoord, InteractionHandler.RIGHT);
    }

    public InteractionHandler getinteractionHandler() {
        return interactionHandler;
    }

    public void setinteractionHandler(InteractionHandler interactionHandler) {
        this.interactionHandler = interactionHandler;
    }

    public PathFinding getPathfinding() {
        return pathFinder;
    }

    public void setPathfinding(PathFinding pathFinder) {
        if (pathFinder.getClass() == FleePathFinder.class) {
            Debug.printC("Setting fleeing pathfinding algo");
            FleePathFinder f = (FleePathFinder) pathFinder;
            this.pathFinder = f;
        }
        if (pathFinder.getClass() == ChasingPathFinder.class) {
            Debug.printC("Setting default chase pathfinding algo");
            ChasingPathFinder p = (ChasingPathFinder) pathFinder;
            this.pathFinder = p;
        }
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public boolean isAlive() {
        return isAlive;
    }

    // Observer pattern overrides

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
        for (Observer o : observers) {
            o.update(this);
        }

    }

    public Timeline getEnemyTimeline() {
        return enemyTimeline;
    }

    public void setEnemyTimeline(Timeline enemyTimeline) {
        this.enemyTimeline = enemyTimeline;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}  

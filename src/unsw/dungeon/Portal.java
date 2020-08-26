package unsw.dungeon;

public class Portal extends Entity implements Obstruction {
    private Portal portalDestination;
    private int id;

    public Portal(int x, int y, Dungeon dungeon, int id) {
        super(x, y, dungeon);
        this.id = id;
        this.portalDestination = null;
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public Portal getPortalDestination() {
        return portalDestination;
    }

    public void setPortalDestination(Portal portalDestination) {
        this.portalDestination = portalDestination;
        Debug.printC("Linked portal to coordinate: (" + getX() + ", " + getY() + ")");
    }

    // end
    /**
     * Checks whether the destination portal (current portal's pair) has open
     * adjacent tiles next to it
     * 
     * @return
     */
    public boolean hasOpenAdjTiles() {
        int portalX = portalDestination.getX();
        int portalY = portalDestination.getY();
        if (dungeon.hasObstruction(portalX, portalY - 1) && dungeon.hasObstruction(portalX + 1, portalY)
                && dungeon.hasObstruction(portalX, portalY + 1) && dungeon.hasObstruction(portalX - 1, portalY)) {
            // Debug.printC("ALL 4 TILES BLOCKED IN DEST " + portalDestination.getCoordinate());
            return false;
        }
        // Debug.printC("Portal at " + getCoordinate() + " is open");
        return true;
    }

    /**
     * Returns the coordiate for the destination portal (current portal's pair)
     * @return
     */
    public Coordinate getDestinationCoordinates() {
        int portalX = portalDestination.getX();
        int portalY = portalDestination.getY();
        int destX = 0;
        int destY = 0;
        if (!dungeon.hasObstruction(portalX, portalY - 1)) {
            // check whether there is obstruction north of portalDestination
            destX = portalX;
            destY = portalY - 1;
        } else if (!dungeon.hasObstruction(portalX + 1, portalY)) {
            // check whether there is obstruction east of portalDestination
            destX = portalX + 1;
            destY = portalY;
        } else if (!dungeon.hasObstruction(portalX, portalY + 1)) {
            // check whether there is obstruction south of portalDestination
            destX = portalX;
            destY = portalY + 1;
        } else if (!dungeon.hasObstruction(portalX - 1, portalY)) {
            // check whether there is obstruction west of portalDestination
            destX = portalX - 1;
            destY = portalY;
        } else {
            Debug.printC("All four tiles around the destination portal are blocked", Debug.RED);
            return null;
        }
        return new Coordinate(destX, destY);
    }

    /**
     * Teleports the entity to the destination portal
     * @param entity
     */
    public void teleport(Entity entity) {
        Coordinate destination = getDestinationCoordinates();
        if (destination != null) {
            Debug.printC("Teleported " + entity.toString() + " to " + destination.toString());
            entity.setCoordinate(destination);
        }
    }

    /**
     * Sets the current portal's pair with the input portal
     * 
     * @param portal2
     */
    public void pairPortal(Portal portal2) {
        if (portal2 != null && portal2 != this) {
            // only portal2 them up if they are not the same thing
            this.setPortalDestination(portal2);
            portal2.setPortalDestination(this);
        }
    }

    @Override
    public boolean canPass() {
        return hasOpenAdjTiles();
    }
}

package unsw.dungeon;

public interface Weapon extends Collectible, Usable {

    @Override
    void pickup(Player p);

    @Override
    int getUses();
    
}
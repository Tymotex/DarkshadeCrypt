package unsw.dungeon;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class InvincibilityPotion extends Entity implements Collectible {
    int duration = 6;

    DoubleProperty percentRemaining = new SimpleDoubleProperty(0);
    StringProperty potionProgress = new SimpleStringProperty();

    Timeline timer = null;

    public InvincibilityPotion(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void showDetails() {
        Debug.printC(this.getClass().getSimpleName() + " at (" + getX() + ", " + getY() + ")");
    }

    @Override
    public void pickup(Player p) {
        if (!p.isInvincible()) {
            SoundPlayer.playSFX("potion_drink.wav");
            p.setInvincible(true);
            percentRemaining.set(1.0);
            InvincibilityPotion potion = this;
            KeyFrame tickProgressBar = new KeyFrame(Duration.seconds(1), e -> {
                duration--;
                if (duration < 0) return;
                potionProgress.set(this.duration + "");
            });
            timer = new Timeline(tickProgressBar);
            timer.setCycleCount(this.duration);
            timer.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    System.out.println("\nhere\n");
                    potion.cancelInvincibility();
                    dungeon.removeTimer(timer);
                }
            });
            dungeon.addTimer(timer);
            timer.play();
            dungeon.applyEnemyPathfindingStrat(new FleePathFinder(dungeon.getObstructionMatrix()));
        }        
        die();
    }

    @Override
    public void die() {
        setX(0); 
        setY(0);
        this.getImageView().setImage(new Image((new File("assets/sprites/entities/null.png")).toURI().toString()));
    }

    /**
     * Removes the invicibility potion affects on the player and stops tiemr from ticking
     */
    public void cancelInvincibility() {
        ChasingPathFinder chasingStrat = new ChasingPathFinder(dungeon.getObstructionMatrix(), dungeon);
        dungeon.applyEnemyPathfindingStrat(chasingStrat);
        Player player = dungeon.getPlayer();
        player.setInvincible(false);
    }

    /**
     * Returns the percantage remaining of the potion
     */
    public DoubleProperty getPercentRemaining() {
        return percentRemaining;
    }

    public void setPercentRemaining(DoubleProperty percentRemaining) {
        this.percentRemaining = percentRemaining;
    }

}
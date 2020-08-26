package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TutorialController {

    private TutorialScreen screen;

    @FXML
    private Pane mainMenuBackground;

    @FXML
    private GridPane backgroundGrid;

    @FXML
    private Pane mainMenuPane;

    @FXML
    private Button nextButton;

    @FXML
    private Text tutorialText;

    @FXML
    private Button startButton;

    @FXML
    public void initialize () {
        tutorialText.setText(
            "Move with WASD and walk into collectibles to pick them up\n" +
            "Equip weapons to protect yourself against enemies\n" +
            "Meet the objectives to complete the level\n" + 
            "Use E and Q to select the current item\n" +
            "Drink potions to become temporarily invincible\n" +
            "Use ESC to bring up the game menu"
        );
    }

    /**
     * Setter
     */
    public TutorialController (TutorialScreen screen) {
        this.screen = screen;
    }

    @FXML
    void handleNext(ActionEvent event) {
        screen.startTutorial();
    }

    @FXML
    void handleStart(ActionEvent event) {
        screen.goStart();
    }

}

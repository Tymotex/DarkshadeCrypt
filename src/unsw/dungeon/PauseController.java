package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class PauseController {

    private StartScreen startScreen;
    private PauseScreen pauseScreen;

    @FXML
    private Text goalText;

    @FXML
    private Text treasureGoalProgress;

    @FXML
    private Text enemyGoalProgress;

    @FXML
    private Text switchGoalProgress;

    @FXML
    private Button resumeButton;

    @FXML
    private Button retryButton;

    @FXML
    private Button goStartButton;

    public PauseController (PauseScreen pauseScreen) {
        super();
        this.pauseScreen = pauseScreen;
    }

    /**
     * Moves to start screen
     */
    @FXML
    void handleGoStart(ActionEvent event) {
        if (startScreen == null) {
            return;
        }
        startScreen.start();
    }

    /**
     * Restarts the current dungeon screen's dungeon
     * @param event
     */
    @FXML
    void handleRetry(ActionEvent event) {
        pauseScreen.restartDungeon();
    }

    /**
     * Unpauses the game and transitions into dungeon screen
     */
    @FXML
    void handleResume(ActionEvent event) {
        pauseScreen.resumeDungeon();
    }

    /**
     * Allows escape key to unpause game
     * @param event
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case ESCAPE:
            pauseScreen.resumeDungeon();
            System.out.println("In pause screen");
            break;
        default:
            break;
        }
    }

    /**
     * Transitions game into start screen
     */
    public void setStartScreen(StartScreen startScreen) {
        System.out.println(startScreen.toString());
        this.startScreen = startScreen;
    }

}

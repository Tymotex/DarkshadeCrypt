package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class OutcomeController {

    @FXML
    private Button restartButton;

    @FXML
    private Button startButton;

    @FXML
    private Text outComeText;

    private OutcomeScreen outcomeScreen;

    public OutcomeController(OutcomeScreen outcomeScreen) {
        super();
        this.outcomeScreen = outcomeScreen;
    }

    public void setOutComeText(String outComeText, Color colour) {
        this.outComeText.setText(outComeText);
        this.outComeText.setFill(colour);
    }

    /**
     * Restarts map
     */
    @FXML
    public void handleRestart(ActionEvent event) {
        outcomeScreen.restartDungeon();
    }

    /**
     * Transitions game back to start screen
     */
    @FXML
    public void handleStart(ActionEvent event) {
        outcomeScreen.goToStart();
    }

}

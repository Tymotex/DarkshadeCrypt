package unsw.dungeon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class StartController {

    private MapSelectorScreen mapSelectorScreen;

    @FXML
    private Button quickStartButton;

    @FXML
    private Button startButton;

    @FXML
    private Button tutorialButton;

    @FXML
    private Button endButton;

    @FXML
    private Pane mainMenuPane;

    @FXML
    private GridPane backgroundGrid;

    private StartScreen screen;

    public StartController (StartScreen screen) {
        this.screen = screen;
    }

    /**
     * handles exitting the game
     */
    @FXML
    public void handleEnd(ActionEvent event) {
        System.out.println("Ending");
        System.exit(0);
    }

    /**
     * Transitions the game into map selector screen
     */
    @FXML
    public void handleStart(ActionEvent event) {
        mapSelectorScreen.start();
    }

    /**
     * Quickly loads the game into advanced.json map
     */
    @FXML
    public void handleQuickStart(ActionEvent event) {
        mapSelectorScreen.startDungeon("Advanced2.json");
    }

    /**
     * setter
     * @param mapSelectorScreen
     */
    public void setMapSelectorScreen(MapSelectorScreen mapSelectorScreen) {
        this.mapSelectorScreen = mapSelectorScreen;
    }

    @FXML
    public void initialize() {
        for (Node child : backgroundGrid.getChildren()) {
            if (child.getId() == null) {
                GaussianBlur gaussianBlur = new GaussianBlur();       
                gaussianBlur.setRadius(7); 
                child.setEffect(gaussianBlur);
            } else if (child.getId().equals("mainMenuPane")) {
                Glow glow = new Glow();
                glow.setLevel(0.25);
                child.setEffect(glow);
            }
        }
    }

    /**
     * Transitions game to tutorial screen
     * @param event
     */
    @FXML
    public void handleTutorialStart(ActionEvent event) {
        screen.startTutorialScreen();
        // mapSelectorScreen.startDungeon("tutorial.json");
    }

}
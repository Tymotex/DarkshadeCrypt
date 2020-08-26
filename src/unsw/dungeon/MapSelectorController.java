package unsw.dungeon;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class MapSelectorController {
    private StartScreen startScreen;
    private MapSelectorScreen mapSelectorScreen;
    private String selectedLevel = null;

    @FXML
    private Button startButton;

    @FXML
    private Button toStartButton;

    @FXML
    private ComboBox<String> mapSelector;

    public MapSelectorController(MapSelectorScreen mapSelectorScreen) {
        super();
        this.mapSelectorScreen = mapSelectorScreen;
    }

    @FXML
    public void initialize() {
        // get all maps from dungeons folder
        // They must be in json format
        
        File dir = new File("dungeons");
        String[] json_files = dir.list();
        String[] removed_json_files = new String[json_files.length];
        for (int i = 0; i < json_files.length ; i++) {
            if (!json_files[i].equals("tutorial.json")) {
                removed_json_files[i] = json_files[i].split("\\.")[0];
            }
        }
        ObservableList<String> files = FXCollections.observableArrayList(removed_json_files);

        mapSelector.setItems(files);
    }

    /**
     * transitions to start screen
     */
    @FXML
    public void handleToStart(ActionEvent event) {
        startScreen.start();
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    /**
     * Allows selection of map which changes current selected level
     */
    @FXML
    public void handleMapSelector(ActionEvent event) {
        // build dungeon
        String map = mapSelector.getValue();
        selectedLevel = map;
    }   

    /**
     * Starts the level which the currently selected level is 
     */
    @FXML
    public void handleStartLevel(ActionEvent event) {
        if (selectedLevel != null) {
            mapSelectorScreen.startDungeon(selectedLevel + ".json");
        }
    }

}
package unsw.dungeon;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DungeonApplication extends Application {
    private StartScreen startScreen;
    private PauseScreen pauseScreen;
    private MapSelectorScreen mapSelectorScreen;
    private DungeonScreen dungeonScreen;
    private OutcomeScreen outcomeScreen;
    private TutorialScreen tutorialScreen;
    static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        mediaPlayer = SoundPlayer.playBGM("warped.wav");
        // ending the game
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent arg0) {
                System.out.println("Ending Game");
                System.exit(0);
            }
        });
        
        startScreen = new StartScreen(primaryStage, this);
        pauseScreen = new PauseScreen(primaryStage, this);
        mapSelectorScreen = new MapSelectorScreen(primaryStage, this);

        mapSelectorScreen.getController().setStartScreen(startScreen);
        pauseScreen.getController().setStartScreen(startScreen);
        startScreen.getController().setMapSelectorScreen(mapSelectorScreen);
        startScreen.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Getters and setters
    /**
     * Transitions to game with map
     * @param map
     */
    public void startLevel (String map) {
        mapSelectorScreen.startDungeon(map);
    }

    /**
     * Transitions to start screen
     */
    public void startStart () {
        startScreen.start();
    }

    /**
     * Resumes the currently stored game after being paused
     */
    public void resumeDungeon () {
        dungeonScreen.resume();
        dungeonScreen.start();
    }

    public String getCurrentMap () {
        return dungeonScreen.map;
    }


    public StartScreen getStartScreen() {
        return startScreen;
    }

    public void setScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    public PauseScreen getPauseScreen() {
        return pauseScreen;
    }

    public void start() {
        pauseScreen.start();
    }

    public void setScreen(PauseScreen pauseScreen) {
        this.pauseScreen = pauseScreen;
    }

    public MapSelectorScreen getMapSelectorScreen() {
        return mapSelectorScreen;
    }

    public void setScreen(MapSelectorScreen mapSelectorScreen) {
        this.mapSelectorScreen = mapSelectorScreen;
    }

    public DungeonScreen getDungeonScreen() {
        return dungeonScreen;
    }

    public void setScreen(DungeonScreen dungeonScreen) {
        this.dungeonScreen = dungeonScreen;
    }

    public OutcomeScreen getOutcomeScreen() {
        return outcomeScreen;
    }

    public void setScreen(OutcomeScreen outcomeScreen) {
        this.outcomeScreen = outcomeScreen;
    }

    public TutorialScreen getTutorialScreen() {
        return tutorialScreen;
    }

    public void setScreen(TutorialScreen tutorialScreen) {
        this.tutorialScreen = tutorialScreen;
    }
    
}

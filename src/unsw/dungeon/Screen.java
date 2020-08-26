package unsw.dungeon;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Screen {
    private Stage stage;
    private String title;
    private Scene scene;
    private DungeonApplication dungeonApplication;

    public Screen (Stage stage, DungeonApplication dungeonApplication) throws IOException {
        this.dungeonApplication = dungeonApplication;
        this.stage = stage;
    }

    /**
     * Method for transitioning the game into this instance of the object
     */
    public void start () {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    // Getters and setters
    
    /**
     * Returns the title of this current screen 
     * @return
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public DungeonApplication getDungeonApplication() {
        return dungeonApplication;
    }

    public void setDungeonApplication(DungeonApplication dungeonApplication) {
        this.dungeonApplication = dungeonApplication;
    }

}
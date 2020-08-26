package unsw.dungeon;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PauseScreen extends Screen {
    private PauseController controller;

    public PauseScreen (Stage stage, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);

        setTitle("Pause Screen");
        controller = new PauseController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pause.fxml"));
        loader.setController(controller);
        
        // load into a Parent node called root
        Parent root = loader.load();
        Image image = new Image(new File("cursor.png").toURI().toString());  //pass in the image path
        root.setCursor(new ImageCursor(image));
        setScene(new Scene(root, 1920, 1080));
    }

    public PauseController getController () {
        return controller;
    }

    /**
     * Restarts the current level 
     */
    public void restartDungeon() {
        String map = getDungeonApplication().getCurrentMap();
        getDungeonApplication().startLevel(map);
    }

    /**
     * Unpauses the dungeon and transitions back into dungeon screen
     */
    public void resumeDungeon (){
        getDungeonApplication().resumeDungeon();
    }
}
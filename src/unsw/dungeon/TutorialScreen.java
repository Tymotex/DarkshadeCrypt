package unsw.dungeon;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TutorialScreen extends Screen {
    private TutorialController controller;

    /**
     * Constructor
     * @param stage
     * @param dungeonApplication
     * @throws IOException
     */
    public TutorialScreen(Stage stage, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);

        setTitle("Dungeon");
        controller = new TutorialController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tutorial.fxml"));
        loader.setController(controller);
        
        // load into a Parent node called root
        Parent root = loader.load();
        Image image = new Image(new File("cursor.png").toURI().toString());  //pass in the image path
        root.setCursor(new ImageCursor(image));
        setScene(new Scene(root, 1920, 1080));
    }
    
    /**
     * Transitions the game's state to the tutorial dungeon level  
     */
    public void startTutorial () {
        getDungeonApplication().startLevel("tutorial.json");
    }

    /**
     * Transitions the game's state to the start screen
     * 
     */
    public void goStart () {
        getDungeonApplication().startStart();
    }
}
package unsw.dungeon;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartScreen extends Screen{

    private StartController controller;

    public StartScreen (Stage stage, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);
        setTitle("Dungeon");
        controller = new StartController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        loader.setController(controller);

        // load into a Parent node called root
        Parent root = loader.load();
        Image image = new Image(new File("cursor.png").toURI().toString());  //pass in the image path
        root.setCursor(new ImageCursor(image));
        setScene(new Scene(root, 1920, 1080));
    }

    /**
     * Transitions game to tutorial screen
     */
    public void startTutorialScreen () {
        try {
            TutorialScreen tutorialScreen = new TutorialScreen(getStage(), getDungeonApplication());
            getDungeonApplication().setScreen(tutorialScreen);
            tutorialScreen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for start controller
     * @return
     */
    public StartController getController () {
        return controller;
    }
}
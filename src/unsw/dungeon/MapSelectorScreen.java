package unsw.dungeon;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MapSelectorScreen extends Screen {
    private MapSelectorController controller;

    public MapSelectorScreen (Stage stage, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);

        setTitle("Map Selector");
        controller = new MapSelectorController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mapSelector.fxml"));
        loader.setController(controller);

        
        // load into a Parent node called root
        Parent root = loader.load();
        Image image = new Image(new File("cursor.png").toURI().toString());  //pass in the image path
        root.setCursor(new ImageCursor(image));
        setScene(new Scene(root, 1920, 1080));
    }

    public MapSelectorController getController () {
        return controller;
    }

    /**
     * Creates a new dungeonScreen instance and dungeon game, and transitions to the screen
     * @param map
     */
    public void startDungeon(String map) {
        try {
            DungeonScreen dungeonScreen = new DungeonScreen(getStage(), map, getDungeonApplication());
            dungeonScreen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
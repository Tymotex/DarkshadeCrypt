package unsw.dungeon;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OutcomeScreen extends Screen {

    private OutcomeController controller;

    public OutcomeScreen(Stage stage, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);
        setTitle("Map Selector");
        controller = new OutcomeController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("levelOutcome.fxml"));
        loader.setController(controller);

        dungeonApplication.setScreen(this);
        
        // load into a Parent node called root
        Parent root = loader.load();
        Image image = new Image(new File("cursor.png").toURI().toString());  //pass in the image path
        root.setCursor(new ImageCursor(image));
        setScene(new Scene(root, 1920, 1080));
        root.setStyle("-fx-background-color: black;");
    }
    
    /**
     * Method for allowing the modification of the text printed in the outcome text
     */
    public void setOutComeText(String text, Color colour) {
        controller.setOutComeText(text, colour);
    }

    /**
     * Restarts the current level 
     */
    public void restartDungeon() {
        String map = getDungeonApplication().getCurrentMap();
        getDungeonApplication().startLevel(map);
    } 

    /**
     * Transitions game screen to startScreen
     */
    public void goToStart (){
        getDungeonApplication().startStart();;
    }

    public OutcomeController getController () {
        return controller;
    }
}
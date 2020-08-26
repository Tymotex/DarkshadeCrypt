package unsw.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DungeonScreen extends unsw.dungeon.Screen {
    private DungeonController controller;
    String map;
    private Parent root;

    public DungeonScreen(Stage stage, String map, DungeonApplication dungeonApplication) throws IOException {
        super(stage, dungeonApplication);
        this.map = map;

        setTitle("Dungeon");
        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(map);
        controller = dungeonLoader.loadController();
        controller.setDungeonScreen(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        loader.setController(controller);

        root = loader.load();
        setScene(new Scene(root, 1920, 1080));
        root.requestFocus();

        Image image = new Image(new File("cursor.png").toURI().toString());  // pass in the image path
        root.setCursor(new ImageCursor(image));

        dungeonApplication.setScreen(this);
        System.out.println(stage.getWidth());
        System.out.println(stage.getHeight()); 
    }

    @Override
    public void start(){
        super.start();
    }

    /**
     * Transitions from dungeon to outcome screen
     */
    public void getOutcomeScreen(String outComeText, Color colour){
        OutcomeScreen outcomeScreen;
        try {
            outcomeScreen = new OutcomeScreen(getStage(), getDungeonApplication());
            outcomeScreen.setOutComeText(outComeText, colour);
            outcomeScreen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recreates current object 
     */
    public void restart() {
        DungeonControllerLoader dungeonLoader;
        try {
            dungeonLoader = new DungeonControllerLoader(map);
            controller = dungeonLoader.loadController();
            controller.setDungeonScreen(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
            loader.setController(controller);
            
            Parent root = loader.load();
            setScene(new Scene(root));
            root.requestFocus();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void pause() {
        getDungeonApplication().start();
    }

    public DungeonScreen (Stage stage, DungeonApplication dungeonApplication)  throws IOException{
        this(stage, "boulders.json", dungeonApplication);
    }

    /**
     * Resumes the game after being paused
     */
    public void resume () {
        controller.resume();
    }

    public DungeonController getController () {
        return controller;
    }
}
package unsw.dungeon;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundPlayer {
    public static void playSFX(String pathToSFX) {
        pathToSFX = "assets/sfx/" + pathToSFX;
        Media sound = new Media(new File(pathToSFX).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(1);
        mediaPlayer.play();
    }

    public static MediaPlayer playBGM(String pathToBGM) {
        pathToBGM = "assets/bgm/" + pathToBGM;
        Media sound = new Media(new File(pathToBGM).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        }); 
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        return mediaPlayer;
        // return null;
    }
}
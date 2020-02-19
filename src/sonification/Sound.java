package sonification;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//import org.pmw.tinylog.Logger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Sound {
	
	public static void playSound(URL pRessourceLocation) throws Exception {
	   
//		//enable javaFx elements by starting up the platform
//    	try {
//			Platform.startup(() ->			{
//			    // This block will be executed on JavaFX Thread
//			});
//		} catch (Exception e) {
//			Platform.runLater(null);
//		}
		
		boolean doesURLpointToWave = pRessourceLocation.toString().contains(".wav");  //TODO if this is ever kept in that manner, do it right an check ending of string
		boolean doesURLpointToMP3  = pRessourceLocation.toString().contains(".mp3");
		
		if(pRessourceLocation != null && doesURLpointToWave) {
		    Clip clip = AudioSystem.getClip();
		    AudioInputStream ais = AudioSystem.getAudioInputStream(pRessourceLocation);
		    clip.open(ais);
		    clip.start();
		}
		else if(pRessourceLocation != null && doesURLpointToMP3){

			Media media = new Media(pRessourceLocation.toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
		}
		else {
//			Logger.info("No sound found!");
//			// TODO Log Error
		}
		
	}
}

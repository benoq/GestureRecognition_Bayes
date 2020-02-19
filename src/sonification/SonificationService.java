package sonification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import thingsToPackIntoConfiguration.Constants_Config;

public class SonificationService {
	
	public URL mapGestureToSound(String pGestureName) throws MalformedURLException {
	    URL soundURL = null;
	    switch (pGestureName) {
		case "aSound_YES":
			soundURL = new File(Constants_Config.pathForSoundFile_yes).toURI().toURL();
			break;

		default:
			break;
		}
	    return soundURL;
	}
	
	public void playSoundForGesture(String pGestureName) throws Exception {

		URL url_sound = null;
		
	    switch (pGestureName) {
		case "aSound_YES":
			url_sound = new File(Constants_Config.pathForSoundFile_yes).toURI().toURL();
			break;
		case "aSound_NO":
			url_sound = new File(Constants_Config.pathForSoundFile_no).toURI().toURL();
			break;
		default:
			url_sound = new File(Constants_Config.pathForSoundFile_unbekannteGeste).toURI().toURL();
			break;
		}
	    
	    if (url_sound != null) {
	    	 Sound.playSound(url_sound);	
	    }
	}
}

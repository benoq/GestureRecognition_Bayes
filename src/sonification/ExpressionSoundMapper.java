package sonification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import thingsToPackIntoConfiguration.Constants_Config;

public class ExpressionSoundMapper {

	public URL mapRequestToSound (Expression pExpression) throws MalformedURLException {
	    URL soundURL = null;
		
		switch(pExpression) {
		case YES:
			soundURL = new File(Constants_Config.pathForSoundFile_yes).toURI().toURL();
			break;
		case UNDEFINED_EXPRESSION:
			soundURL = new File(Constants_Config.pathForSoundFile_unbekannteGeste).toURI().toURL();
			break;
		}
		return soundURL;
	}
	
}

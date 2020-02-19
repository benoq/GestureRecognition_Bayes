package run;

import com.gt.UI.MainApp;

//import javafx.application.Platform;

//import javafx.application.Platform;

public class run {
	
	public static void main(String[] args) {
		
//		//enable javaFx elements by starting up the platform
//    	try {
//			Platform.startup(() ->			{
//			    // This block will be executed on JavaFX Thread
//			});
//		} catch (Exception e) {
//			Platform.runLater(null);
//		}
		
		MainApp m = new MainApp();
		m.main(null);
	}

}

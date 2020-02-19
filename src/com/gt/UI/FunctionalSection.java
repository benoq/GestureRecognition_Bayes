package com.gt.UI;

import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gt.gesture.mouseCapture.DataCapturePanel;
import com.gt.gesture.proxy.OperationMediator;

public class FunctionalSection {
	
	private JPanel jPanel;

	public JPanel getjPanel() {
		return jPanel;
	}

	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	public JPanel bringVerifyPanel(JPanel pPanel, ActionListener pActionListner, DataCapturePanel pDCP_verify, OperationMediator pOM) {
		if (jPanel == null && pPanel == null) {
			JPanel verifyPanel = new JPanel();
			verifyPanel.setLayout(null);
			
			// TODO: add a save button, helps to increase train data   ...@beng: scrutinize the train function first
			JButton btn_ReplayCaptured = new JButton("Replay Captured");
			btn_ReplayCaptured.setBounds(455, 83, 136, 23);
			btn_ReplayCaptured.addActionListener(pActionListner);

			JButton btn_Verify = new JButton("Verify");
			btn_Verify.setBounds(502, 199, 89, 23);
			btn_Verify.addActionListener(pActionListner);

			JButton btn_RecognizeGesture = new JButton("Recognize");
			btn_RecognizeGesture.setBounds(455, 30, 89, 23);
			btn_RecognizeGesture.addActionListener(pActionListner);

			JComboBox registeredModelCMB = new JComboBox();
			registeredModelCMB.setBounds(455, 168, 136, 20);
			verifyPanel.add(registeredModelCMB);
			// load data into combo
			registeredModelCMB.setModel(new DefaultComboBoxModel(pOM.readRegGestureModels()));
			JLabel lblTrainedModels = new JLabel("Trained Models");
			lblTrainedModels.setBounds(455, 143, 108, 14);
			
			//adds to panel
			verifyPanel.add(pDCP_verify, null);
			verifyPanel.add(btn_ReplayCaptured);
			verifyPanel.add(btn_Verify);
			verifyPanel.add(btn_RecognizeGesture);
			verifyPanel.add(lblTrainedModels);
			jPanel = verifyPanel;
		}
		
		pPanel = jPanel;  // TODO check if this double safety is necessary or if the check for existing Panels can be skipped
		return jPanel;
	}
	
	public JPanel bringPanel_permanentRecognition(JPanel pPanel, ActionListener pActionListner, DataCapturePanel pDCP, OperationMediator pOM) {
		if (jPanel == null && pPanel == null) {

			JPanel panel_permRec = new JPanel();
			
			JButton btn_test = new JButton("test");
			btn_test.setBounds(700, 200, 120, 23);
			btn_test.addActionListener(pActionListner);
			
			//add elements 
			panel_permRec.add(pDCP);
			panel_permRec.add(btn_test);
			
			jPanel = panel_permRec;
		}
     
		return jPanel;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */ //copied from https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
	public ImageIcon createImageIcon(String path,
	                                           String description) {
	    
	    if (path != null) {
	        return new ImageIcon(path, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

}


package com.gt.UI;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gt.gesture.features.RawFeature;
import com.gt.gesture.mouseCapture.DataCapturePanel;
import com.gt.gesture.proxy.OperationMediator;

import sonification.SonificationService;
import thingsToPackIntoConfiguration.Constants_Config;

public class MainApp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	JLabel status;
	JLabel about;
	private JPanel verifyPanel;
	private JPanel capturePanel;
	private JPanel trainPanel;
	private JPanel permanentRecognitionPanel;
	JPanel trainTestDataEditorPanel;
	DataCapturePanel dcpVerify;
	DataCapturePanel dcpCapture;
	DataCapturePanel dcpDataEditor;
	DataCapturePanel dcp_permanentRecognition;
	OperationMediator oprMed = new OperationMediator();

	// Bedienelemente
	JButton btnReplay_Verify;
	JButton verify_Btn;
	JButton recognizeBtn;
	JComboBox registeredModelCMB;

	// gui elements
	FunctionalSection member_fs = new FunctionalSection();
	FunctionalSection fs_permanent = new FunctionalSection();
	DesignElement member_design = new DesignElement();
	

	// ********** Functions, listeners...//

	ChangeListener changeListener_tabs = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			System.out.println("tab switched");
			clearGuiFields();
		}
	};

	ActionListener capture_alBtns = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			clearGuiFields();

			if (e.getSource() == btnAddNewGesture) {
				// check for existence
				String newGest = newGestureTXTFLD.getText();
				boolean contains = false;
				for (int i = 0; i < captureDataCMB.getItemCount(); i++) {
					if (captureDataCMB.getItemAt(i).toString().equalsIgnoreCase(newGest)) {
						contains = true;
						break;
					}
				}
				if (!contains)
					captureDataCMB.addItem(newGestureTXTFLD.getText());
				newGestureTXTFLD.setText("");
			}
			if (e.getSource() == btnSaveGestureRawTrainData) {
				oprMed.saveCaptured(dcpCapture.getCapturedRawFeature(), captureDataCMB.getSelectedItem().toString());
			}
			if (e.getSource() == btnReplay_Capture) {
				replayCaptured(getDCPCapture());
			}
		}
	};

	ActionListener verify_alBtns = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source_button = new JButton(); 
			JComponent test = new JComponent() {
			};
			if (e.getSource() instanceof JButton) {
				source_button = (JButton) e.getSource();
				test = (JComponent) e.getSource();
			}
			
			System.out.println(test);
			System.out.println("source: " + source_button);
			
			if (e.getSource() == btnSaveGestureRawTrainData) {
				oprMed.saveCaptured(dcpVerify.getCapturedRawFeature(), captureDataCMB.getSelectedItem().toString());
			}
			if (e.getSource() == btnReplay_Verify) {
				replayCaptured(getDCPVerify());
			}
			if (e.getSource() == btnReplay_Verify) {// fs_verify.getjPanel().getComponent(2)) { // getComponent is very
													// unreliable... search for a smarter way
				boolean res = oprMed.verify(registeredModelCMB.getSelectedItem().toString(),
						dcpVerify.getCapturedRawFeature());
				if (res) {
					// verified
					status.setText("Verified   " + registeredModelCMB.getSelectedItem().toString());
				} else {
					// not verified
					status.setText("Not Verified   " + registeredModelCMB.getSelectedItem().toString());
				}
			}
			if (source_button.getText().equals("Recognize")) {  //here: buttonMapping between JPanels via String checking of ButtonText <- Improve
				String recGest = oprMed.recognizeGesture(dcpVerify.getCapturedRawFeature());
				status.setText("Gesture matches best the following: " + recGest);
				
				//TODO @beng: maybe place the sonification call on a more adequate place; this is just "get it somehow done" solution 
				SonificationService sonaService = new SonificationService();
			
				try {
					sonaService.playSoundForGesture(recGest);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	};

	ActionListener train_alBtns = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnGenerateCodebook) {
				oprMed.generateCodeBook();
			}
			if (e.getSource() == btnTrainHmmModel) {
				oprMed.trainHMM();
			}
		}
	};

	private void replayCaptured(DataCapturePanel dcp) {
		RawFeature rfCln = null;
		try {
			rfCln = (RawFeature) dcp.getCapturedRawFeature().clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		dcp.animateCaptured(rfCln);
	}

	private void loadComboWithRegistredModels(JComboBox jcmb) {
		jcmb.setModel(new DefaultComboBoxModel(oprMed.readRegGestureModels()));
	}

	// ******** GUI ********/
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
//		Icon icon_hand = member_fs.createImageIcon(Constants_Config.pathForVisualFile_Hand, "icon description"); 
		
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);

			JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			mainTabbedPane.setBounds(10, 29, 606, 331);
			jContentPane.add(mainTabbedPane);

			mainTabbedPane.addTab("Add/Capture", null, getCapturePanel(), null);
//			mainTabbedPane.addTab("Verify", null, getVerifyPanel());
			mainTabbedPane.addTab("Verify", null, member_fs.bringVerifyPanel(verifyPanel, verify_alBtns, getDCPVerify(), oprMed), null);
			mainTabbedPane.addTab("Train", null, getTrainPanel(), null);
			mainTabbedPane.addTab("Permanent Recognition", null, fs_permanent.bringPanel_permanentRecognition(permanentRecognitionPanel, verify_alBtns, member_design.bringDCP(), oprMed), null);
//			mainTabbedPane.addTab("Permanent Recognition", icon_hand, member_fs.bringPanel_permanentRecognition(verifyPanel, verify_alBtns, getDCPVerify(), oprMed), null);

			mainTabbedPane.addChangeListener(changeListener_tabs);
			mainTabbedPane.setSelectedIndex(1);

			// getTrainTestDataEditorPanel(), null);
			jContentPane.add(getStatusLabel());
			jContentPane.add(getAboutLabel());

			JLabel lblMouseGestureRecognition = new JLabel("Mouse Gesture Recognition");
			lblMouseGestureRecognition.setFont(new Font("Arial", Font.BOLD, 15));
			lblMouseGestureRecognition.setBounds(410, 11, 206, 27);
			jContentPane.add(lblMouseGestureRecognition);
		}
		return jContentPane;
	}

	private JLabel getStatusLabel() {
		if (status == null) {
			status = new JLabel("");
			status.setFont(new Font("Tahoma", Font.PLAIN, 16));
			status.setBounds(10, 367, 578, 27);
		}
		return status;
	}

	private JLabel getAboutLabel() {
		if (about == null) {
			about = new JLabel("BetaVersion");
			about.setBounds(10, 392, 606, 14);
		}
		return about;
	}

	private JPanel getVerifyPanel() {
		if (verifyPanel == null) {
			verifyPanel = new JPanel();
			verifyPanel.setLayout(null);
			verifyPanel.add(getDCPVerify(), null);
			// TODO: add a save button, helps to increase train data
			btnReplay_Verify = new JButton("Replay Captured");
			btnReplay_Verify.setBounds(455, 83, 136, 23);
			btnReplay_Verify.addActionListener(verify_alBtns);
			verifyPanel.add(btnReplay_Verify);

			verify_Btn = new JButton("Verify");
			verify_Btn.setBounds(502, 199, 89, 23);
			verify_Btn.addActionListener(verify_alBtns);
			verifyPanel.add(verify_Btn);

			recognizeBtn = new JButton("Recognize");
			recognizeBtn.setBounds(455, 30, 89, 23);
			recognizeBtn.addActionListener(verify_alBtns);
			verifyPanel.add(recognizeBtn);

			registeredModelCMB = new JComboBox();
			registeredModelCMB.setBounds(455, 168, 136, 20);
			verifyPanel.add(registeredModelCMB);
			// load data into combo
			registeredModelCMB.setModel(new DefaultComboBoxModel(oprMed.readRegGestureModels()));
			JLabel lblTrainedModels = new JLabel("Trained Models");
			lblTrainedModels.setBounds(455, 143, 108, 14);
			verifyPanel.add(lblTrainedModels);
			verifyPanel.add(getDCPVerify(), null);

		}
		return verifyPanel;
	}

	private JTextField newGestureTXTFLD;
	JButton btnAddNewGesture;
	JComboBox captureDataCMB;
	JButton btnSaveGestureRawTrainData;
	JButton btnReplay_Capture;

	private JPanel getCapturePanel() {
		if (capturePanel == null) {
			capturePanel = new JPanel();
			capturePanel.setLayout(null);
			capturePanel.add(getDCPCapture());

			JLabel lblNewGesture = new JLabel("Name for new Gesture");
			lblNewGesture.setBounds(455, 33, 86, 14);
			lblNewGesture.setFont(Constants_Config.FONT_NORMAL);
			capturePanel.add(lblNewGesture);

			newGestureTXTFLD = new JTextField();
			newGestureTXTFLD.setBounds(455, 58, 136, 20);
			newGestureTXTFLD.setColumns(10);

			capturePanel.add(newGestureTXTFLD);

			btnAddNewGesture = new JButton("Add");
			btnAddNewGesture.setBounds(502, 89, 89, 23);
			btnAddNewGesture.addActionListener(capture_alBtns);
			capturePanel.add(btnAddNewGesture);

			JLabel lblCurrentGesture = new JLabel("Currently Captured");
			lblCurrentGesture.setBounds(455, 142, 113, 14);
			capturePanel.add(lblCurrentGesture);

			captureDataCMB = new JComboBox();
			captureDataCMB.setBounds(455, 167, 136, 20);
			capturePanel.add(captureDataCMB);
			// load combo
			captureDataCMB.setModel(new DefaultComboBoxModel(oprMed.readTrainData1D()));
			btnSaveGestureRawTrainData = new JButton("Save");
			btnSaveGestureRawTrainData.setBounds(502, 198, 89, 23);
			btnSaveGestureRawTrainData.addActionListener(capture_alBtns);
			capturePanel.add(btnSaveGestureRawTrainData);

			btnReplay_Capture = new JButton("Replay");
			btnReplay_Capture.setBounds(502, 232, 89, 23);
			btnReplay_Capture.addActionListener(capture_alBtns);
			capturePanel.add(btnReplay_Capture);

		}
		return capturePanel;
	}

	JButton btnGenerateCodebook;
	JButton btnTrainHmmModel;

	private JPanel getTrainPanel() {
		if (trainPanel == null) {
			trainPanel = new JPanel();
			trainPanel.setLayout(null);

			btnGenerateCodebook = new JButton("Generate CodeBook");
			btnGenerateCodebook.setBounds(10, 26, 142, 23);
			btnGenerateCodebook.addActionListener(train_alBtns);
			btnGenerateCodebook.setMultiClickThreshhold(2000);
			trainPanel.add(btnGenerateCodebook);

			btnTrainHmmModel = new JButton("Train HMM Model");
			btnTrainHmmModel.setBounds(10, 69, 142, 23);
			btnTrainHmmModel.addActionListener(train_alBtns);
			btnTrainHmmModel.setMultiClickThreshhold(2000);
			trainPanel.add(btnTrainHmmModel);
		}
		return trainPanel;
	}

	JButton nextBtn_dataEditor;
	JButton prevBtn_dataEditor;
	JCheckBox chckbxAutoPlay_dataEditor;
	JButton playBtn_dataEditor;
	JButton deleteDataBtn_dataEditor;
	JComboBox trainTestDataCombo_dataEditor;

	private JPanel getTrainTestDataEditorPanel() {
		if (trainTestDataEditorPanel == null) {
			trainTestDataEditorPanel = new JPanel();
			trainTestDataEditorPanel.setLayout(null);
			trainTestDataEditorPanel.add(getDCPDataEditor(), null);

			nextBtn_dataEditor = new JButton(">");
			nextBtn_dataEditor.setBounds(538, 96, 41, 23);
			trainTestDataEditorPanel.add(nextBtn_dataEditor);

			prevBtn_dataEditor = new JButton("<");
			prevBtn_dataEditor.setBounds(482, 96, 41, 23);
			trainTestDataEditorPanel.add(prevBtn_dataEditor);

			chckbxAutoPlay_dataEditor = new JCheckBox("Auto Play");
			chckbxAutoPlay_dataEditor.setBounds(482, 66, 97, 23);
			trainTestDataEditorPanel.add(chckbxAutoPlay_dataEditor);

			playBtn_dataEditor = new JButton("Play");
			playBtn_dataEditor.setBounds(482, 184, 97, 23);
			trainTestDataEditorPanel.add(playBtn_dataEditor);

			deleteDataBtn_dataEditor = new JButton("Remove This");
			deleteDataBtn_dataEditor.setBounds(482, 234, 97, 23);
			trainTestDataEditorPanel.add(deleteDataBtn_dataEditor);

			trainTestDataCombo_dataEditor = new JComboBox();
			trainTestDataCombo_dataEditor.setBounds(482, 130, 97, 20);
			trainTestDataEditorPanel.add(trainTestDataCombo_dataEditor);
		}
		return trainTestDataEditorPanel;
	}

	private DataCapturePanel getDCPVerify() {
		if (dcpVerify == null) {
			dcpVerify = new DataCapturePanel();
			dcpVerify.setBounds(new Rectangle(15, 15, 430, 275));
		}
		return dcpVerify;
	}

	private DataCapturePanel getDCPCapture() {
		if (dcpCapture == null) {
			dcpCapture = new DataCapturePanel();
			dcpCapture.setBounds(new Rectangle(15, 15, 430, 275));
		}
		return dcpCapture;
	}

	private DataCapturePanel getDCPDataEditor() {
		if (dcpDataEditor == null) {
			dcpDataEditor = new DataCapturePanel();
			dcpDataEditor.setBounds(new Rectangle(15, 15, 430, 275));
		}
		return dcpDataEditor;
	}
	
	


	/**
	 * This is the default constructor
	 */
	public MainApp() {
		super();
		this.setSize(642, 455);
		this.setContentPane(getJContentPane());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		} catch (Exception e1) {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(e1.toString());
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainApp thisClass = new MainApp();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setTitle("Bayes");
				thisClass.setVisible(true);
				thisClass.setResizable(false);
			}
		});
	}

	private void clearGuiFields() {
		// clear status message
		if (this.status != null) {
			this.status.setText("");
		}
	}
}

package com.gt.UI;

import java.awt.Rectangle;

import javax.swing.JButton;

import com.gt.gesture.mouseCapture.DataCapturePanel;

public class DesignElement {

	// outsource Capture panels from MainApp to this
	public DataCapturePanel bringDCP() {
		DataCapturePanel dcp = new DataCapturePanel();
		dcp.setBounds(new Rectangle(15, 15, 430, 275));

		return dcp;
	}

}

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import application.Registry;

public class SearchListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JTextField ipField = Registry.getInstance().getWindow().getInputPanel().getIpInput().getField();
		JTextField fileField = Registry.getInstance().getWindow().getInputPanel().getMusicInput().getField();
		
		String initialIp = ipField.getText();
		String fileName = fileField.getText();
		
		Registry.getInstance().getP2pApplication().initTransfer(fileName,initialIp);
	}

}

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import application.Registry;

public class ConnectListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JTextField ipField = Registry.getInstance().getWindow().getInputPanel().getIpInput().getField();
		
		String initialIp = ipField.getText();
		
		Registry.getInstance().getP2pApplication().connect(initialIp);
	}

}

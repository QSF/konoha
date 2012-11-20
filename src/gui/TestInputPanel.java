package gui;

import javax.swing.JFrame;

public class TestInputPanel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		InputPanel panel = new InputPanel();
		
		frame.add(panel);
		frame.setSize(700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}

}

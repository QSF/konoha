package gui;

import javax.swing.JFrame;

public class TestPeersListPanel {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		PeersPanel peersPanel = new PeersPanel();
		PeersListPanel panel = peersPanel.getPeersListPanel();
		
		//adiciona os peers.
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		panel.addPeer(new JPeer("192.168.0.1", "50,04"));
		
		
		frame.add(peersPanel);
		frame.setSize(700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}

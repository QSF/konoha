package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PeersPanel extends JPanel {

	private static final long serialVersionUID = -5324997827881521594L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 400;
	
	/*Label que contém a msg do que é listado.*/
	private JLabel peersLabel;
	/*Contém a lista de peers*/
	private PeersListPanel peersListPanel;
	
	public PeersPanel(){
		this.init();
	}
	
	protected void init(){
//		this.setBackground(Color.BLACK);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(PeersPanel.WIDTH, 
							PeersPanel.HEIGHT));
		
		//adicionar a descrição da música.
		this.setPeersLabel(new JLabel("Lista de peers"));
		this.setPeersListPanel(new PeersListPanel());
		
		this.getPeersLabel().setAlignmentX(Component.LEFT_ALIGNMENT);
		this.getPeersListPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.add(this.getPeersLabel());
		this.add(this.getPeersListPanel());
	}

	public JLabel getPeersLabel() {
		return peersLabel;
	}

	public void setPeersLabel(JLabel peersLabel) {
		this.peersLabel = peersLabel;
	}

	public PeersListPanel getPeersListPanel() {
		return peersListPanel;
	}

	public void setPeersListPanel(PeersListPanel peersListPanel) {
		this.peersListPanel = peersListPanel;
	}
	
}

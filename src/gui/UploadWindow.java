package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class UploadWindow extends JFrame {

	private static final long serialVersionUID = 8729771275923554676L;
	
	public final static int WIDTH = 300;
	public final static int HEIGHT = 200;
	
	/**Contém a lista de peers*/
	private PeersListPanel peersListPanel;
	
	public UploadWindow(){
		this.init();
	}
	
	protected void init(){
		this.setTitle("Upload");
		this.setLayout(new BoxLayout(this.getContentPane(), 
				BoxLayout.PAGE_AXIS));
		this.setSize(new Dimension(NeighborsWindow.WIDTH, 
				NeighborsWindow.HEIGHT));
		
		this.setPeersListPanel(new PeersListPanel("IP                 Arquivo  Offset Length"));
		
		this.add(this.getPeersListPanel());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public PeersListPanel getPeersListPanel() {
		return peersListPanel;
	}

	public void setPeersListPanel(PeersListPanel peersListPanel) {
		this.peersListPanel = peersListPanel;
	}
}

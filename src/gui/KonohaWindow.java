package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * Janela principal do konaha.
 */
public class KonohaWindow extends JFrame {

	private static final long serialVersionUID = -1007887547250584131L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 700;
	
	private HeaderPanel headerPanel;
	private InputPanel inputPanel;
	private PeersPanel peersPanel;
	
	public KonohaWindow(){
		this.init();
	}
	
	protected void init(){
		this.setLayout(new BoxLayout(this.getContentPane(), 
							BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(KonohaWindow.WIDTH, 
				KonohaWindow.HEIGHT));
		
		this.setHeaderPanel(new HeaderPanel());
		this.setInputPanel(new InputPanel());
		this.setPeersPanel(new PeersPanel());
		
		this.add(this.getHeaderPanel());
		this.add(this.getInputPanel());
		this.add(this.getPeersPanel());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public HeaderPanel getHeaderPanel() {
		return headerPanel;
	}

	public void setHeaderPanel(HeaderPanel headerPanel) {
		this.headerPanel = headerPanel;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public void setInputPanel(InputPanel inputPanel) {
		this.inputPanel = inputPanel;
	}

	public PeersPanel getPeersPanel() {
		return peersPanel;
	}

	public void setPeersPanel(PeersPanel peersPanel) {
		this.peersPanel = peersPanel;
	}
	
}

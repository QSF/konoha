package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Janela principal do konaha.
 */
public class KonohaWindow extends JFrame {

	private static final long serialVersionUID = -1007887547250584131L;
	
	public final static int WIDTH = 500;
	public final static int HEIGHT = 450;
	
	private HeaderPanel headerPanel;
	private InputPanel inputPanel;
	private PeersPanel peersPanel;
	private JLabel messages;
	
	public KonohaWindow(){
		this.init();
	}
	
	protected void init(){
		this.setTitle("###Konoha###");
		this.setLayout(new BoxLayout(this.getContentPane(), 
							BoxLayout.PAGE_AXIS));
		this.setSize(new Dimension(KonohaWindow.WIDTH, 
				KonohaWindow.HEIGHT));
		
		this.setHeaderPanel(new HeaderPanel());
		this.setInputPanel(new InputPanel());
		this.setPeersPanel(new PeersPanel());
		this.setMessages("Informe um arquivo.");
		
		//adiciona o listener do evento.
		this.getInputPanel().getSearchButton().addActionListener(new SearchListener());
		this.getInputPanel().getConnectButton().addActionListener(new ConnectListener());
		
		this.getHeaderPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		this.getInputPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		this.getPeersPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		this.getMessages().setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.add(this.getHeaderPanel());
		this.add(this.getInputPanel());
		this.add(this.getPeersPanel());
		this.add(this.getMessages());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	/**Getters e setters*/
	
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

	public JLabel getMessages() {
		return messages;
	}

	public void setMessages(JLabel messages) {
		this.messages = messages;
	}
	
	public void setMessages(String messages) {
		if (this.messages == null)
			this.messages = new JLabel();
		this.messages.setText(messages);
	}
	
}

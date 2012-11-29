package gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Classe que representa o painel que contém entrada de dados.
 * Aqui irá conter o campo que digita o ip inicial, o campo que digita 
 * o nome da música e o botão de pesquisar(a música).
 * */
public class InputPanel extends JPanel {

	private static final long serialVersionUID = -4020583899809831294L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 200;
	
	/**Input referente ao ip inicial*/
	private JInput ipInput;
	
	private JButton connectButton;
	
	/**Input referente ao nome da música*/
	private JInput musicInput;
	
	private JButton searchButton;
	
	public InputPanel(){
		this.init();
	}
	
	
	protected void init(){		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.setIpInput(new JInput("Digite o IP inicial"));
		this.setConnectButton(new JButton("conectar nessa porra!"));
		
		this.setMusicInput(new JInput("Digite o nome da música:"));
		this.setSearchButton(new JButton("buscar"));
		//adicionar handler
		
		this.add(this.getIpInput());
		this.add(this.getConnectButton());
		this.add(this.getMusicInput());
		this.add(this.getSearchButton());
	}
	
	/**Getters e setters*/

	public JInput getIpInput() {
		return ipInput;
	}

	public void setIpInput(JInput ipInput) {
		this.ipInput = ipInput;
	}

	public JInput getMusicInput() {
		return musicInput;
	}

	public void setMusicInput(JInput musicInput) {
		this.musicInput = musicInput;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}


	public JButton getConnectButton() {
		return connectButton;
	}


	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}
}

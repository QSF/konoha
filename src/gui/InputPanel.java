package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Classe que representa o painel que contém entrada de dados.
 * Aqui irá conter o campo que digita o ip inicial, o campo que digita 
 * o nome da música e o botão de pesquisar(a música).
 * @TODO que tal fazer um esquema de ler as msgs de um proprierties?
 * */
public class InputPanel extends JPanel {

	private static final long serialVersionUID = -4020583899809831294L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 200;
	
	private JInput ipInput;
	private JInput musicInput;
	
	private JButton searchButton;
	
	public InputPanel(){
		this.init();
	}
	
	protected void init(){
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(InputPanel.WIDTH, 
											InputPanel.HEIGHT));
		
		this.setIpInput(new JInput("Digite o IP inicial"));
		this.setMusicInput(new JInput("Digite o nome da música:"));
		
		this.setSearchButton(new JButton("buscar"));
		//adicionar handler
		
		this.add(this.getIpInput());
		this.add(this.getMusicInput());
		this.add(this.getSearchButton());
	}

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
}

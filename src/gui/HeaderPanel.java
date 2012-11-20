package gui;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Essa classe é o painel superior, contendo a logo e o slogan.
 * */
public class HeaderPanel extends JPanel {

	private static final long serialVersionUID = 6983583458654838378L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 100;
	
	public final static int LOGO_WIDTH = 100;
	public final static int LOGO_HEIGHT = 100;
	
	/*Ícone que possui a imagem do logo*/
	private JLabel logo;
	/*Ícone que possui o slogan*/
	private JLabel slogan;
	
	public HeaderPanel(){
		this.init("Use Kohana, don't be an aho!");
	}
	/*Função que inicializa o layout*/
	protected void init(String slogan){
//		this.setBackground(Color.BLUE);
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setMaximumSize(new Dimension(HeaderPanel.WIDTH, 
											HeaderPanel.HEIGHT));
		
		//o logo sempre vai ser a imagem em img/logo
		String path = "img/logo.png";
		
		ImageIcon icon = new ImageIcon(path);
		Image image = icon.getImage().getScaledInstance(HeaderPanel.LOGO_WIDTH, 
					HeaderPanel.LOGO_HEIGHT, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(image);
		//seta o logo
		this.setLogo(new JLabel(icon));
		
		//seta o slogan
		this.setSlogan(new JLabel(slogan));
		
		this.add(this.getLogo());
		this.add(this.getSlogan());
	}

	public JLabel getLogo() {
		return logo;
	}

	public void setLogo(JLabel logo) {
		this.logo = logo;
	}
	public JLabel getSlogan() {
		return slogan;
	}
	public void setSlogan(JLabel slogan) {
		this.slogan = slogan;
	}

}

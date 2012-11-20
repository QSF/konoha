package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe que funciona como um modelo de entrada, com Jtext.
 * */
public class JInput extends JPanel {

	private static final long serialVersionUID = -8771274616706473014L;
	
	public final static int WIDTH = 400;
	public final static int HEIGHT = 35;
	
	private JLabel inputName;
	private JTextField field;
	
	public JInput(String inputName){
		this.init(inputName);
	}
	
	protected void init(String inputName){
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMaximumSize(new Dimension(JInput.WIDTH, 
												JInput.HEIGHT));
			
		this.setInputName(new JLabel(inputName));
		this.add(this.getInputName());
		
		//setar o tamanho do JTextField?
		this.setField(new JTextField());
		this.add(this.getField());
	}
	
	public JLabel getInputName() {
		return inputName;
	}
	public void setInputName(JLabel inputName) {
		this.inputName = inputName;
	}
	public JTextField getField() {
		return field;
	}
	public void setField(JTextField field) {
		this.field = field;
	}
	
}

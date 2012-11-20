package gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

/**
 * Classe que contém a lista de peers.
 * Deve oferecer métodos para adicionar e remover elementos da lista.
 * */
public class PeersPanel extends JPanel {

	private static final long serialVersionUID = -5324997827881521594L;
	
	private ArrayList<JPeer> jPeers = new ArrayList<>();
	
	private JList<String> list;
	
	public PeersPanel(){
		this.init();
	}
	
	protected void init(){
		this.list = new JList<String>();
		
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setLayoutOrientation(JList.VERTICAL);
		this.list.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		this.list.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		
		//this.setBackground(Color.WHITE);
		
		this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		
		this.add(list);
		
		JScrollPane scroll = new JScrollPane(this.list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		scroll.setBorder(emptyBorder);
		
		this.add(scroll);
	}
}

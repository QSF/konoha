package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Classe que contém a lista de peers.
 * Deve oferecer métodos para adicionar e remover elementos da lista.
 * */
public class PeersListPanel extends JPanel {

	private static final long serialVersionUID = -5324997827881521594L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 200;
	
	private ArrayList<JPeer> jPeers = new ArrayList<>();
	private Vector<String> peers = new Vector<>();
	
	private JList<String> list;
	
	public PeersListPanel(){
		this.init();
	}
	
	protected void init(){
		this.list = new JList<String>();
		
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setLayoutOrientation(JList.VERTICAL);		
		//this.setBackground(Color.WHITE);
		
		this.setMaximumSize(new Dimension(PeersListPanel.WIDTH,PeersListPanel.HEIGHT));
		this.setMinimumSize(new Dimension(PeersListPanel.WIDTH,PeersListPanel.HEIGHT));
		
		this.add(list);
		
		JScrollPane scroll = new JScrollPane(this.list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
//		Border emptyBorder = BorderFactory.createEmptyBorder();
//		scroll.setBorder(emptyBorder);
		
		this.add(scroll);
		
		this.initHeader();
	}
	
	protected void initHeader(){
		this.addPeer(new JPeer("         IP        ", "total(%)"));
	}
	
	/**
	 * Adiciona um peer na lista de peers.
	 * */
	public void addPeer(JPeer peer){
		this.jPeers.add(peer);
		this.peers.add(peer.toString());
		
		this.list.setListData(this.peers);
	}
	
	/**
	 * Remove um peer na lista de peers.
	 * */
	public void removePeer(JPeer peer){
		int index = this.peers.indexOf(peer.toString());
		if (index == -1){
			return;
		}
		this.peers.remove(index);
		this.jPeers.remove(index);
		
		this.list.setListData(this.peers);
	}
	
	/**
	 * Limpa a lista de peers.
	 * */
	public void clear(){
		this.peers.clear();
		this.jPeers.clear();
		
		this.list.setListData(this.peers);
		this.initHeader();
	}
}

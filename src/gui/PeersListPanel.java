package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import router.Peer;

/**
 * Classe que contém a lista de peers.
 * Deve oferecer métodos para adicionar e remover elementos da lista.
 * */
public class PeersListPanel extends JPanel {

	private static final long serialVersionUID = -5324997827881521594L;
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 200;
	
	private ArrayList<Peer> jPeers = new ArrayList<>();
	private Vector<String> peers = new Vector<>();
	
	private JList<String> list;
	private String header;
	
	public PeersListPanel(String header){
		this.init(header);
	}
	
	public PeersListPanel(){
		this.init("IP                        total(%)");
	}
	
	protected void init(String header){		
		this.list = new JList<String>();
		
		this.list.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane scroll = new JScrollPane(this.list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(scroll, Component.LEFT_ALIGNMENT);
		
		this.initHeader(header);
	}
	
	protected void initHeader(String header){
		this.setHeader(header);
		this.peers.add(header);
		this.list.setListData(this.peers);
	}
	
	/**
	 * Adiciona um peer na lista de peers.
	 * */
	public void addPeer(Peer peer){
		this.jPeers.add(peer);
		this.peers.add(peer.toString());
		
		this.list.setListData(this.peers);
	}
	
	/**
	 * Adiciona um peer na lista de peers, com arquivo, offset e length.
	 * */
	public void addPeer(Peer peer, String fileName, long off, long length){
		this.jPeers.add(peer);
		this.peers.add(peer.getIp().getHostAddress() + " " + fileName + " " + 
					off + " " + length );
		
		this.list.setListData(this.peers);
	}
	
	/**
	 * Remove um peer na lista de peers.
	 * */
	public void removePeer(Peer peer){
		int index = -1;
		int i = 0;
		for (Peer p : this.jPeers){
			if (p.getIp().equals(peer.getIp())){
				index = i;
				break;
			}
			i++;
		}
		
		if (index == -1){//não contém este peer.
			return;
		}
		
		this.peers.remove(index  + 1);//por causa do header
		this.jPeers.remove(index);
		
		this.list.setListData(this.peers);
	}
	
	/**
	 * Limpa a lista de peers.
	 * */
	public void clear(){
		ArrayList<Peer> aux = (ArrayList<Peer>) this.jPeers.clone();
		for (Peer p : aux){
			this.removePeer(p);
		}
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}

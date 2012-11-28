package router;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import file.transfer.DataType;
import file.transfer.OperationCode;

import application.Registry;
import application.RouterConfig;

/**
 * Classe responsável pelo roteamento.
 * */
public class Router {
	
	/**Lista de peers vizinhos*/
	private ArrayList<Peer> peers = new ArrayList<>();
	
	/**Nome do arquivo*/
	private String fileName;
	
	private Peer myPeer;
	
	/**Fica ouvindo os pedidos de roteamento*/
	private RouterListener routerListener;
	private RouterConfig config;
	
	public Router(){
		//pega as configurações.
		this.config = new RouterConfig();
		RouterListener routerListener = new RouterListener(
				this.config.getPort(),this.config.getConnections());
		
		this.setRouterListener(routerListener);
		//inicializa o listener.
		Thread thread = new Thread(routerListener);
		thread.start();
		
		this.myPeer = new Peer();
		try {
			this.myPeer.setIp(Inet4Address.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que encontra os peers vizinhos,
	 * dado um determinado peer.
	 * */
	public void askNeighbors(){
		for (Peer peer : this.peers)
			this.askNeighbor(peer);		
	}
	
	/**
	 * Atualiza a lista de vizinhos de acordo com um peer.
	 * */
	public void askNeighbor(Peer peer){
		DataType data = new DataType();
		data.getOperations().add(OperationCode.ASKNEIGHBORS);
		RouterSender sender = new RouterSender(peer, this.config.getPort(),data);
		Thread thread = new Thread(sender);
		thread.start();
	}
	
	/**
	 * Método que pergunta para os peers,
	 * sobre um determinado arquivo.
	 * Os peers que possuem vão responder diretamente para
	 * o transfer, adicionando em sua lista de peers.
	 * */
	public void searchFile(String fileName,Peer peer){
		DataSearch dataSearch = new DataSearch();
		dataSearch.getOperations().add(OperationCode.SEARCH);
		dataSearch.setFileName(fileName);
		dataSearch.setTTL((byte) this.config.getTtl());
		dataSearch.setPeer(peer);
		
		for (Peer p: this.peers){
			if (!p.getIp().equals(peer.getIp()))
				this.search(p,dataSearch);
		}
	}
	
	public RouterSender search(Peer peer, DataType data) {
		RouterSender sender = new RouterSender(peer, this.config.getPort(),data);
		Thread thread = new Thread(sender);
		thread.start();
		return sender;
	}
	
	synchronized public void addPeers(ArrayList<Peer> peers){
		for (Peer peer : peers){
			this.addPeer(peer);
		}
	}
	
	synchronized public void addPeer(Peer peer){
		boolean contains = false;
		
		if (peer.getIp().equals(this.myPeer.getIp()) )//não sou meu vizinho.
			return;
		
		for (Peer oldPeer : this.peers){
			if ( oldPeer.getIp().equals(peer.getIp()) ){
				contains = true;
				break;
			}
		}
		
		if (!contains){
			this.peers.add(peer);
			Registry.getInstance().getNeighborsWindow().getPeersListPanel().addPeer(peer);
		}
			
	}
	
	synchronized public void removePeer(Peer peer){
		boolean contains = false;
		int i = 0;
		for (Peer oldPeer : this.peers){
			if ( oldPeer.getIp().equals(peer.getIp()) ){
				contains = true;
				break;
			}
			i++;
		}
		
		if (contains){
			this.peers.remove(i);
			Registry.getInstance().getNeighborsWindow().getPeersListPanel().removePeer(peer);
		}
	}
	
	/**Getters e setters*/
	
	public ArrayList<Peer> getPeers() {
		return peers;
	}
	public void setPeers(ArrayList<Peer> peers) {
		this.peers = peers;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public RouterListener getRouterListener() {
		return routerListener;
	}

	public void setRouterListener(RouterListener routerListener) {
		this.routerListener = routerListener;
	}	
}

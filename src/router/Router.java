package router;

import java.util.ArrayList;

import application.RouterConfig;

/**
 * Classe responsável pelo roteamento.
 * */
public class Router {
	
	/**Lista de peers vizinhos*/
	private ArrayList<Peer> peers = new ArrayList<>();
	
	/**Nome do arquivo*/
	private String fileName;
	
	/**Fica ouvindo os pedidos de roteamento*/
	private RouterListener routerListener;
	
	public Router(){
		//pega as configurações.
		RouterConfig config = new RouterConfig();
		RouterListener routerListener = new RouterListener(
				config.getPort(),config.getConnections());
		
		this.setRouterListener(routerListener);
		//inicializa o listener.
		Thread thread = new Thread(routerListener);
		thread.start();
	}
	
	/**
	 * Método que encontra os peers vizinhos,
	 * dado um determinado peer.
	 * */
	public void findNeighbors(Peer peer){
		//abri um socket.
		
	}
	
	/**
	 * Método que pergunta para os peers,
	 * sobre um determinado arquivo.
	 * Os peers que possuem vão responder diretamente para
	 * o transfer, adicionando em sua lista de peers.
	 * */
	public void searchFile(String fileName){
		//para cada peer, cria um Sender e faz o pedido
		//de um arquivo.
	}
	
	synchronized public void addPeers(ArrayList<Peer> peers){
		for (Peer peer : peers){
			this.addPees(peer);
		}
	}
	
	synchronized public void addPees(Peer peer){
		boolean contains = false;
		
		for (Peer oldPeer : this.peers){
			if (oldPeer.getIp().getHostAddress() == 
					peer.getIp().getHostAddress())
				contains = true;
		}
		
		if (!contains)
			this.peers.add(peer);
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

package router;

import java.util.ArrayList;

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
	
	/**
	 * Método que encontra os peers vizinhos.
	 * */
	public void findNeighbor(){
		
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

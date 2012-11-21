package router;

import java.util.ArrayList;

/**
 * Classe responsável pelo roteamento.
 * */
public class Router {
	
	/**Lista de peers vizinhos*/
	private ArrayList<Peer> peers;
	/**Nome do arquivo*/
	private String fileName;
	
	/**
	 * Método que encontra os peers vizinhos.
	 * */
	public void findNeighbor(){
		
	}
	
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
}

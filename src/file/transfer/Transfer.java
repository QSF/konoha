package file.transfer;

import java.util.ArrayList;
import router.Peer;
import application.DataFile;

/**
 *	Classe responsável pela parte de transferência de arquivos.
 *	Ela é quem toma a decisão baseado na lista de peers. 
 */
public class Transfer implements Runnable{
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Receiver> receivers;
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Peer> peers;
	
	/**Lista de peers que possuem o arquivos*/
	private DataFile file;
	
	public Transfer(ArrayList<Peer> peers, DataFile file){
		this.setPeers(peers);
		this.setFile(file);
	}
	
	@Override
	public void run(){
		
	}
	
	/**Getters e setters*/
	public ArrayList<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(ArrayList<Receiver> receivers) {
		this.receivers = receivers;
	}

	public ArrayList<Peer> getPeers() {
		return peers;
	}

	public void setPeers(ArrayList<Peer> peers) {
		this.peers = peers;
	}

	public DataFile getFile() {
		return file;
	}

	public void setFile(DataFile file) {
		this.file = file;
	}
	
}

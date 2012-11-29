package router;

import java.util.ArrayList;

import file.transfer.DataType;
import file.transfer.OperationCode;

/**
 * Classe que representa um pacote contendo uma lista de peers vizinhos.
 * */
public class DataNeighbors extends DataType {
	
	private ArrayList<Peer> peers = new ArrayList<>();
	
	public DataNeighbors(){
		this.getOperations().add(OperationCode.NEIGHBORS);
	}

	public ArrayList<Peer> getPeers() {
		return peers;
	}

	public void setPeers(ArrayList<Peer> peers) {
		this.peers = peers;
	}
}

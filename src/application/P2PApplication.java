package application;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import router.Peer;
import router.Router;
import file.transfer.Transfer;

/**
 * Classe que faz a regra de negócio de nossa aplicação.
 * Ela recebe a entrada dos usuários via GUI.
 * Pede os peers para a nossa classe de roteamento.
 * E passa para decidir a divisão dos arquivos pelos peers.
 * Além disso, esta classe possui as informações sobre a lista de
 * arquivos que um peer possui.
 * 
 * @see Router
 * @see Transfer
 * */
public class P2PApplication {
	
	/**Lista de arquivos do peer*/
	private ArrayList<DataFile> files;
	
	private Transfer transfer;
	
	private Peer myPeer;
	
	public P2PApplication() {
		this.myPeer = new Peer();
		try {
			this.myPeer.setIp(Inet4Address.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void initTransfer(String fileName,String initialIp){
		this.transfer = new Transfer(fileName);
		Thread thread = new Thread(this.transfer);
		thread.start();
		Router router = Registry.getInstance().getRouter();
		
		Peer peer = new Peer();
		try {
			peer.setIp(Inet4Address.getByName(initialIp));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("Meus vizinhos");
		for (Peer p: router.getPeers())
			System.out.println(p.getIp());
		
		router.askNeighbors(peer);
//		router.searchFile(fileName,this.myPeer);
	}

	public ArrayList<DataFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<DataFile> files) {
		this.files = files;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}
}

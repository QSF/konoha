package application;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import router.Peer;
import router.Router;
import file.transfer.Sender;
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
	private ArrayList<DataFile> files = new ArrayList<>();
	
	private Transfer transfer;
	/**Objeto que cria conexões que enviam os arquivos*/
	private Sender sender;
	
	private Peer myPeer;
	/**Arquivo que contém as informações de tranferência*/
	private TransferConfig transferConfig = new TransferConfig();
	
	public P2PApplication() {
		int port = this.transferConfig.getPort();
		int numberConnections = this.transferConfig.getConnections();
		//este sender recebe pedido de transferência de arquivos.
		this.setSender(new Sender(port, numberConnections));
		
		this.myPeer = new Peer();
		try {
			this.myPeer.setIp(Inet4Address.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(String initialIp) {
		Router router = Registry.getInstance().getRouter();
		
		Peer peer = new Peer();
		try {
			peer.setIp(Inet4Address.getByName(initialIp));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//adiciona o peer inicial na lista de vizinhos.
		router.addPeer(peer);
		router.askNeighbors();
	}
	
	public void initTransfer(String fileName){
		Router router = Registry.getInstance().getRouter();
		fileName = fileName + ".mp3";
		this.transfer = new Transfer(fileName, this.transferConfig);
		Thread thread = new Thread(this.transfer);
		thread.start();
		this.updateFileList();
		
		router.searchFile(fileName,this.myPeer);
	}
	
	public void updateFileList(){
		File dir = new File("arquivos");
		
		File[] fList = dir.listFiles();
		if (fList == null)
			return;
		this.files.clear();
		for ( int i = 0; i < fList.length; i++ ){
			DataFile dataFile = new DataFile();
			dataFile.setName(fList[i].getName());
			dataFile.setSize(fList[i].length());
			
			this.files.add(dataFile);
		}
	}
	
	/**
	 * Método que pesquisa se no diretório de arquivos
	 * há algúm arquivo com este nome. 
	 * */
	public synchronized boolean hasFile(String fileName) {
		this.updateFileList();
		boolean contains = false;
		
		for (DataFile file : this.files)
			if (file.getName().equals(fileName)){
				contains = true;
				break;
			}
		
		return contains;
	}

	public DataFile getFile(String fileName) {
		for (DataFile file : this.files)
			if (file.getName().equals(fileName))
				return file;
		
		return null;
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

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public TransferConfig getTransferConfig() {
		return transferConfig;
	}

	public void setTransferConfig(TransferConfig transferConfig) {
		this.transferConfig = transferConfig;
	}
}

package application;

import java.io.File;
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
	private ArrayList<DataFile> files = new ArrayList<>();
	
	private Transfer transfer;
	
	private Peer myPeer;
	
	public P2PApplication() {
		this.updateFileList();
		this.myPeer = new Peer();
		try {
			this.myPeer.setIp(Inet4Address.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void initTransfer(String fileName,String initialIp){
//		Router router = Registry.getInstance().getRouter();
		if (this.hasFile(fileName + ".mp3"))
			System.out.println("Tem " + fileName);
		else
			System.out.println("N Tem " + fileName);
		Peer peer = new Peer();
		try {
			peer.setIp(Inet4Address.getByName(initialIp));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		ArrayList<Peer> peers = new ArrayList<>();
		peers.add(peer);
		
		this.transfer = new Transfer(peers,fileName);
		Thread thread = new Thread(this.transfer);
		thread.start();
		this.updateFileList();
		//router.searchFile(fileName + ".mp3",this.myPeer);
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
	public boolean hasFile(String fileName) {
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
}

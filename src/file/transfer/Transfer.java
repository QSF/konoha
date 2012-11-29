package file.transfer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import router.Peer;
import application.DataFile;
import application.Registry;
import application.TransferConfig;

/**
 *	Classe responsável pela parte de transferência de arquivos.
 *	Ela é quem toma a decisão baseado na lista de peers. 
 */
public class Transfer implements Runnable {
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Receiver> receivers = new ArrayList<>();
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Peer> peers = new ArrayList<>();
	
	/**Lista de peers que possuem o arquivos*/
	private DataFile file;
	
	private int senderNumber;
	
	private TransferConfig transferConfig;

	public Transfer(String fileName, TransferConfig transferConfig){
		this.setTransferConfig(transferConfig);
		this.file = new DataFile();
		this.file.setName(fileName);
	}	

	public Transfer(ArrayList<Peer> peers, String fileName) {
		this.setPeers(peers);
		this.file = new DataFile();
		this.file.setName(fileName);
	}
	
	protected void calculatePing(){
		DataType data = new DataType();
		data.getOperations().add(OperationCode.ISALIVE);
		//para cada peer.
		//crie um thread para conectar com o peer e calcular o ping.
		this.senderNumber = this.peers.size();
		for (Peer peer: this.peers){
			Registry.getInstance().getRouter().search(peer, data);
		}
		boolean finishedPing = false;
		while (!finishedPing){
			for (Peer peer: this.peers){
				if (peer.getPing() != -1.0)
					finishedPing = true;
			}
		}
	}
	
	@Override
	public void run() {
		//esperar um tempo
		long initial = System.currentTimeMillis()/1000;
		//espera 5 sec, tempo máximo para esperar um peer.
		while (System.currentTimeMillis()/1000 - initial < 5){}
		
		this.calculatePing();
		this.decision();

		this.file.setContent(new byte[(int) this.file.getSize()]);
		int port = this.transferConfig.getPort();
		int offset = 0;
		int length = 0;
		for (Peer peer: peers){
			length = (int) ((peer.getPercent() * this.file.getSize()) / 100);
			
			System.out.println("O peer " + peer.getIp() + " tem o %: " + peer.getPercent());
			System.out.println("Que são " + length + " bytes de " + this.file.getSize());
			Receiver receiver = new Receiver(this, port, peer, offset, length);
			this.receivers.add(receiver);
			Thread thread = new Thread(receiver);
			thread.start();
			
			offset = offset + length;
		}
		
		while (!this.receivers.isEmpty()){};
//		initial = System.currentTimeMillis()/1000;
//		while (System.currentTimeMillis()/1000 - initial < 10){}
//		//depois que transferiu todas as partes, salva.
		try {
			this.saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** Método que grava efetivamente a música na Pasta
	 * @throws IOException 
	 */
	public void saveFile() throws IOException {    
		FileOutputStream fos = null;
	    try {
			fos = new FileOutputStream("arquivos/" + this.file.getName());	
			fos.write(this.file.getContent());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}
	}
	
	/**
	 * Método que faz a tomada de decisão sobre a divisão do arquivo, tendo
	 * como critério o menor ping entre os peers que possuem o arquivo. Quanto
	 * menor o ping do peer, maior o tamanho do arquivo que ele irá enviar.
	 * @return ArrayList<Peers>
	 */
	public ArrayList<Peer> decision() {
		int sum = 0;
		//Obtendo a soma dos pings dos peers que possuem o arquivo
		for (Peer p : this.peers) {
			sum += p.getPing();
		}
		//Calculando a porcentagem que cada ping de cada peer representa
		//Falta arrumar quando a porcentagem sai "quebrada"
		for (Peer p : this.peers) {
			p.setPercent((int) ((p.getPing() * 100) / sum));
		}
		
		//Bubble sort
		int i, j;
	    for (i = 0; i < this.peers.size() - 1; i++) {
	        for (j = 0; j < this.peers.size() - i - 1; j++) {
	            if (this.peers.get(j).getPercent() > this.peers.get(j + 1).getPercent()){
	            	Collections.swap(this.peers, j, j + 1);
	            }
	        }
	    }
	    
	    //Reorganizando as porcentagens
	    i = 0; j = this.peers.size() - 1; int temp = 0;
	    while (i != j) {
	    	temp = this.peers.get(i).getPercent();
	    	this.peers.get(i).setPercent(this.peers.get(j).getPercent());
	    	this.peers.get(j).setPercent(temp);
	    	i++;
	    	j--;
	    }
	    
	    //Soma das porcentagens para "arrendondar" o resultado
	    sum = 0;
		for (Peer p : this.peers) {
			sum += p.getPercent();
		}
		
		this.peers.get(0).setPercent((this.peers.get(0).getPercent() + (100 - sum)));
		return this.peers;
	}

	/**
	 * Método que encerra uma tranferência.
	 * */
	public void close(){

	}
	
	synchronized public void addPeer(Peer peer){
		boolean contains = false;
		
		for (Peer oldPeer : this.peers){
			if ( oldPeer.getIp().equals(peer.getIp()) ){
				contains = true;
				break;
			}
		}
		
		if (!contains)
			this.peers.add(peer);
	}
	
	synchronized public void setPing(Peer peer){
		for (int i = 0; i < this.peers.size(); i++){
			Peer oldPeer = this.peers.get(i);
			if ( oldPeer.getIp().equals(peer.getIp()) ){
				oldPeer.setPing(peer.getPing());
				break;
			}
		}
	}
	
	public synchronized void removeReceiver(Receiver receiver){
		this.receivers.remove(receiver);
	}
	
	public synchronized void removeSender(){
		this.senderNumber--;
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
	
	public int getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(int senderNumber) {
		this.senderNumber = senderNumber;
	}

	public TransferConfig getTransferConfig() {
		return transferConfig;
	}

	public void setTransferConfig(TransferConfig transferConfig) {
		this.transferConfig = transferConfig;
	}

}

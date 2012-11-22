package file.transfer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import router.Peer;
import application.DataFile;
import application.Registry;

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

	public Transfer(String fileName){
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
		for (Peer peer: this.peers){
			Registry.getInstance().getRouter().search(peer, data);
			System.out.println(peer.getPing());
		}
		//para cada peer.
		//crie um thread para conectar com o peer e calcular o ping.
	}
	
	@Override
	public void run() {
		//esperar um tempo
		this.calculatePing();
		this.Decision();
		
		int offset = 0;
		int length = 0;
		for (Peer peer: peers){
			length = (int) ((peer.getPercent() * this.file.getSize()) / 100);
			
			Thread thread = new Thread(new Receiver(this, 12346, peer, offset, length));
			thread.start();
			
			offset = offset + length;
		}
	}


	/** Método que grava efetivamente a música na Pasta
	 * @throws IOException 
	 */
	public void saveFile() throws IOException {    
		FileOutputStream fos = null;
	    try {
			fos = new FileOutputStream(this.file.getName() + ".mp3");	
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
	public ArrayList<Peer> Decision() {
		int sum = 0;
		//Obtendo a soma dos pings dos peers que possuem o arquivo
		for (Peer p : this.peers) {
			sum += p.getPing();
		}
		//Calculando a porcentagem que cada ping de cada peer representa
		//Falta arrumar quando a porcentagem sai "quebrada"
		for (Peer p : this.peers) {
			p.setPercent(((p.getPing() * 100) / sum));
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
	
	synchronized public void setPeer(Peer peer){
		System.out.println("Me veio o ip: " + peer.getIp());
		System.out.println("Com ping: " + peer.getPing());
		for (int i = 0; i < this.peers.size(); i++){
			Peer oldPeer = this.peers.get(i);
			if ( oldPeer.getIp().equals(peer.getIp()) ){
				oldPeer.setPing(peer.getPing());
				break;
			}
		}
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

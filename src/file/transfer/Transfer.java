package file.transfer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import router.Peer;
import application.DataFile;

/**
 *	Classe responsável pela parte de transferência de arquivos.
 *	Ela é quem toma a decisão baseado na lista de peers. 
 */
public class Transfer implements Runnable {
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Receiver> receivers;
	
	/**Lista de peers que possuem o arquivos*/
	private ArrayList<Peer> peers;
	
	/**Lista de peers que possuem o arquivos*/
	private DataFile file;
	
	public Transfer() {
		
	}
	
	public Transfer(ArrayList<Peer> peers, DataFile file) {
		this.setPeers(peers);
		this.setFile(file);
	}
	
	@Override
	public void run(){
		
	}
	
	/** M�todo que grava efetivamente a m�sica na Pasta 
	 * @throws IOException 
	 */
	public void saveFile() throws IOException {    
		FileOutputStream fos = null;
	    try {
			fos = new FileOutputStream(this.file.getName() + ".mp3");	
			fos.write(this.file.getContent());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fos.close();
		}
	}
	
	/**
	 * M�todo que faz a tomada de decis�o sobre a divis�o do arquivo, tendo
	 * como crit�rio o menor ping entre os peers que possuem o arquivo. Quanto
	 * menor o ping do peer, maior o tamanho do arquivo que ele ir� enviar.
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

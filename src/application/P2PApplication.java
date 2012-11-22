package application;

import java.util.ArrayList;

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
	
	public void initTransfer(String fileName){
		this.transfer = new Transfer(fileName);
		Thread thread = new Thread(this.transfer);
		thread.start();
		
		//liga o roteamento.
		Registry.getInstance().getRouter().searchFile(fileName);
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

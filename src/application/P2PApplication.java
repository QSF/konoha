package application;

import java.util.ArrayList;

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

	public ArrayList<DataFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<DataFile> files) {
		this.files = files;
	}
}

package file.transfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread que escuta os pedidos de transferência de partes de 
 * um arquivo.
 * Cria threads que enviam os arquivos.
 * */
public class Sender implements Runnable {
	
	private ServerSocket serverSocket;

	@Override
	public void run() {
		this.serverSocket = null;
		try {
			this.serverSocket = new ServerSocket(13267); 
	    
			while (true) {  
				  Socket sock;
				  sock = this.serverSocket.accept();
				  System.out.println("Conexão aceita: " + sock);  
				  //criar nova thread para enviar arquivos.
		    }  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

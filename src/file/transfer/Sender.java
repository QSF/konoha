package file.transfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import router.RouterReceiver;

/**
 * Thread que escuta os pedidos de transferência de partes de 
 * um arquivo.
 * Cria threads que enviam os arquivos.
 * */
public class Sender implements Runnable {

	private ServerSocket serverConnection;
	
	private int port;
	private int numberConnections;
	private Socket connection;
	private boolean runCondition;
	
	//array de threads
	private ArrayList<TransferSender> senders = new ArrayList<>();

	public Sender(int port, int numberConnections){
		this.port = port;
		this.numberConnections = numberConnections;
	}
	
	/**
	 * Inicializa o socket server.
	 * */
	private void startServer(){
		try {
			this.serverConnection = new ServerSocket(this.port, this.numberConnections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Espera por uma nova coneção.
	 * */	
	public void connect() {
		try {
			this.connection = this.serverConnection.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cria novas threads que cuidam de cada conexão.
	 * */
	@Override
	public void run() {
		this.runCondition = true;
		this.startServer();
		
		while(this.runCondition) {
			this.connect();
			TransferSender sender = new TransferSender(this.connection);
			this.senders.add(sender);
			Thread thread = new Thread(sender);
			thread.start();
		}	
	}
}

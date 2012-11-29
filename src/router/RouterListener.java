package router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe que escuta na porta de roteamento.
 * Esta classe espera novas conexões e cria threads do tipo
 * Receiver para cada nova requisição.
 * */
public class RouterListener implements Runnable {

	private ServerSocket serverConnection;
	
	private int port;
	private int numberConnections;
	private Socket connection;
	private boolean runCondition;
	
	//array de threads
	private ArrayList<RouterReceiver> receivers = new ArrayList<>();

	public RouterListener(int port, int numberConnections){
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
			RouterReceiver receiver = new RouterReceiver(this.connection);
			this.receivers.add(receiver);
			Thread thread = new Thread(receiver);
			thread.start();
		}	
	}
	
	public synchronized void removeReceiver(RouterReceiver receiver) {
		this.receivers.remove(receiver);
	}
	
}

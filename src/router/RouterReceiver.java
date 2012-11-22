package router;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.spi.RegisterableService;

import application.Registry;

import file.transfer.DataType;
import file.transfer.IOData;

/**
 * Classe que representa uma conexão da router.
 * É responsável por tratar todos os protocolos de roteamento.
 * */
public class RouterReceiver implements Runnable {
	
	private Peer peer;
	private IOData stream;
	private Socket connection;
	
	public RouterReceiver(Socket connection){
		this.connection = connection;
		this.createStreams();
		
		this.peer = new Peer();
		this.peer.setIp(this.connection.getInetAddress());
	}

	public void createStreams() {
		DataOutputStream output;
		try {
			output = new DataOutputStream(this.connection.getOutputStream());
			output.flush();
			this.stream = new IOData(new DataInputStream (this.connection.getInputStream()), output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(ArrayList<DataType> data){
		this.stream.send(data);
	}
	
	public void send(DataType data){
		this.stream.send(this.dataTypeToArray(data));
	}
	
	private ArrayList<DataType> dataTypeToArray(DataType data){
		ArrayList<DataType> dataList = new ArrayList<>();
		dataList.add(data);
		return dataList;
	}

	public ArrayList<DataType> receive() {
		ArrayList<DataType> dataTypeList = this.stream.receive();
		
		return dataTypeList;
	}	

	public void closeConnection() {
		try {
			this.stream.close();
			this.connection.close();
//			 Registry.getInstance().getServerListerner()
//			 	.removeReceiverConnection(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		boolean runCondition = true;
		while(runCondition){
			ArrayList<DataType> dataList = this.receive();
			
			//chama um método que trata o protocolo
			for (DataType data: dataList){
				String methodName = data.getOperations().get(0) + "Action";
				try {
					/*Chama um método de forma dinâmica
					 * Assim, haverá um método para cada operation code*/
					Method method = this.getClass().getMethod(methodName, data.getClass());
					method.invoke(this, data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}	
		this.closeConnection();
	}
	
	/**
	 * Método que trata quando um peer vizinho
	 * pede as informações sobre os vizinhos.
	 * */
	public void ASKNEIGHBORSAction(DataType data){		
		
		//pega a lista de vizinhos deste peer.
		ArrayList<Peer> peers = Registry.getInstance().getRouter()
			.getPeers();
		
		//empacoto a requisição.
		DataNeighbors dataNeighbor = new DataNeighbors();
		dataNeighbor.setPeers((ArrayList<Peer>) peers.clone());
		
		//envia a lista de peers.
		this.send(data);
	}
}

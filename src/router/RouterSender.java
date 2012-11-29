package router;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import application.Registry;
import file.transfer.DataType;
import file.transfer.IOData;

public class RouterSender implements Runnable{
	
	private Peer peer;
	private IOData stream;
	private Socket connection;
	
	private DataType data;
	
	private int port;
	private boolean runCondition;
	
	public RouterSender(){
		
	}
	
	public RouterSender(Peer peer, int port, DataType data){
		this.peer = peer;
		this.port = port;
		this.data = data;
	}
	
	public void connect() {
		try {
			this.connection = new Socket(this.peer.getIp(), this.port);
		} catch (IOException e){
			System.out.println("O peer " +  this.peer.getIp() + " está fora.");
			Registry.getInstance().getRouter().removePeer(this.peer);
			this.runCondition = false;
		}
		
	}

	public void createStreams() {
		if (!this.runCondition)
			return;
		try {
			DataOutputStream output = new DataOutputStream(this.connection.getOutputStream());
			output.flush();
			this.stream = new IOData(new DataInputStream(this.connection.getInputStream()), output);
			
		} catch (IOException e) {
			System.out.println("O peer " +  this.peer.getIp() + " está fora.");
			Registry.getInstance().getRouter().removePeer(this.peer);
			this.runCondition = false;
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
	
	@Override
	public void run() {
		this.runCondition = true;
		this.connect();
		this.createStreams();
		while (this.runCondition){
			String methodName = data.getOperations().get(0) + "Action";
			try {
				/*Chama um método de forma dinâmica
				 * Assim, haverá um método para cada operation code*/
				Method method = this.getClass().getMethod(methodName, data.getClass());
				method.invoke(this, data);
			} catch (Exception ex) {
				ex.printStackTrace();
				this.runCondition = false;
			}
		}
		this.closeConnection();
	}
	
	/**
	 * Método que pede uma lista de vizinhos
	 * */
	public void ASKNEIGHBORSAction(DataType data){		
		//pede os peers.
		this.send(this.data);
		
		//vai receber a lista de peers.
		ArrayList<DataType> dataList = this.stream.receive();
		DataNeighbors dataNeighbor = (DataNeighbors) dataList.get(0);
		
		//adiciona para minha lista de vizinhos.
		Registry.getInstance().getRouter().addPeers(
				dataNeighbor.getPeers());
		
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Método que pergunta se o determinado peer possui um arquivo.
	 * */
	public void SEARCHAction(DataSearch data){		
		//pede os peers.
		this.send(this.data);		
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Método que informa um peer que ele possui um arquivo.
	 * */
	public void ANSWERAction(DataAnswer data){		
		//informa que possui o arquivo..
		this.send(this.data);		
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Pergunta se um peer está vivo, calculando seu ping.
	 * O set ping armazena o resultado.
	 * */
	public void ISALIVEAction(DataType data){		
		long initial = new Date().getTime();
		
		this.send(data);
		this.stream.receive();
		
		long finalTime = new Date().getTime();
		
		float ping = (float) (finalTime - initial);		
		
		this.peer.setPing(ping);		
		System.out.println(peer.getIp() + " tem o ping: " + peer.getPing());
		Registry.getInstance().getP2pApplication().getTransfer().setPing(this.peer);
		Registry.getInstance().getP2pApplication().getTransfer().removeSender();
		
		//fecha o thread.
		this.runCondition = false;
	}
	
	public void ENDAction(DataType data){		
		//apenas envia o end
		this.send(data);		
		//fecha o thread.
		this.runCondition = false;
	}
	
	public void closeConnection() {
		try {
			this.stream.close();
			this.connection.close();
		} catch (IOException e) {
			System.out.println("O peer " +  this.peer.getIp() + " está fora.");
			Registry.getInstance().getRouter().removePeer(this.peer);
			this.runCondition = false;
		}
	}
}

package router;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import application.DataFile;
import application.Registry;
import file.transfer.DataType;
import file.transfer.IOData;
import file.transfer.OperationCode;

/**
 * Classe que representa uma conexão da router.
 * É responsável por tratar todos os protocolos de roteamento.
 * */
public class RouterReceiver implements Runnable {
	
	private Peer peer;
	private IOData stream;
	private Socket connection;
	private boolean runCondition;
	
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
		this.runCondition = true;
		
		while(this.runCondition){
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
					this.runCondition = false;
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
		this.send(dataNeighbor);
		//adiciona o cara na lista de peers(se ele não estiver).
		Registry.getInstance().getRouter().addPeer(this.peer);
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Responde que está vivo, e fecha a conexão.
	 * A resposta é feita apenas enviando um END.
	 * */
	public void ISALIVEAction(DataType data){
		DataType dataType = new DataType();
		dataType.getOperations().add(OperationCode.END);
		
		//envia a lista de peers.
		this.send(dataType);
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Método que responde a uma pergunta de arquivo.
	 * Este método checa se possui o arquivo, se sim, responde com um answer.
	 * Depois, caso o TTL esteja ok, envia para seus ips
	 * */
	public void SEARCHAction(DataSearch data){
		System.out.println("Recebi um pedido de arquivo, do " + this.peer.getIp());
		System.out.println("Em nome do " + data.getPeer().getIp());
		System.out.println("Arquivo com nome: " + data.getFileName());
		System.out.println("TTL: " + data.getTTL());
		
		
		if (Registry.getInstance().getP2pApplication().hasFile(data.getFileName())){
			DataAnswer dataType = new DataAnswer();
			dataType.getOperations().add(OperationCode.ANSWER);
			
			DataFile file = Registry.getInstance().getP2pApplication().getFile(data.getFileName());
			dataType.setFileName(file.getName());
			dataType.setSize(file.getSize());
			//envia para o cliente.
			//pois ele cria um RouterSender.
			Registry.getInstance().getRouter().search(data.getPeer(), dataType);
		}
		
		//diminui o TTL
		data.setTTL((byte) (data.getTTL() - 1));
		if (data.getTTL() > 0){//TTL não pode ser 0
			//repassa para os vizinhos.
			for (Peer peer : Registry.getInstance().getRouter().getPeers()){
				if (!peer.getIp().equals(data.getPeer().getIp()) &&//não envia para quem pede o arquivo
					!peer.getIp().equals(this.peer.getIp())){//e nem para quem me passou.
					Registry.getInstance().getRouter().search(peer, data);
				}
			}
		}
		
		//fecha o thread.
		this.runCondition = false;
	}
	
	/**
	 * Método que adiciona um peer a uma lista de peers que contém um determinado arquivo.
	 * Esta lista de peers se encontra no Transfer.
	 * É preciso checar o nome do arquivo.
	 * */
	public void ANSWERAction(DataAnswer data){
		System.out.println("O peer " + this.peer.getIp());
		System.out.println("Possui o arquivo " + data.getFileName());
		System.out.println("Com o tamanho " + data.getSize());
		
		DataFile file = Registry.getInstance().getP2pApplication().getTransfer()
			.getFile();
		//checa o arquivo.
		if (! file.getName().equals(data.getFileName()) ){
			//o transfer atual deverá ser igual ao arquivo que este peer irá receber.
			return;			
		}
		//seta o tamanho do arquivo.
		if (file.getSize() == -1)//ainda não setaram o tamanho.
			file.setSize(data.getSize());
		
		//adiciona o peer na lista do Transfer.
		Registry.getInstance().getP2pApplication().getTransfer().addPeer(this.peer);
		//tenta adicionar na lista de vizinhos.
		Registry.getInstance().getRouter().addPeer(this.peer);
		
		//fecha o thread.
		this.runCondition = false;
	}
}

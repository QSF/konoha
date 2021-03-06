package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import router.Peer;

/**
 * Thread que ficara responsível por receber um arquivo(ou pedaço dele),
 * recebendo de um determinado peer.
 * 
 * Irá realizar uma conexão com um peer, pedindo o arquivo.
 * Após a tranferência, confere a integridade do mesmo, com um hash. 
 * */
public class Receiver implements Runnable {
	
	/** Peer que fornecerá o arquivo(ou parte dele) */
	private Peer peer;
	
	/** Socket que representa a conexão */
	private Socket socket;
	
	/** Objeto que envia e recebe dados */
	private IOData stream;
	
	/**Byte inicial que será pego do arquivo*/
	private int offset;
	
	/** Quantidade de bytes que será lido do arquivo */
	private int length;
	
	/** Transfer que será usado no método run, que juntar as partes do arquivo **/
	private Transfer transfer;
	
	/** Porta usanda para conexão*/
	private int port;
	
	private boolean abort = false;
	
	public Receiver(Transfer transfer, int port, Peer peer, int offset, int length) {
		this.transfer = transfer;
		this.setOffset(offset);
		this.setLength(length);
		this.setPeer(peer);
		this.setPort(port);
	}
	
	/**
	 * Cria os streams de entrada e saída
	 * */
	public void createStreams() {
		DataOutputStream output;
		try {
			output = new DataOutputStream(this.getSocket().getOutputStream());
			output.flush();
			this.stream = new IOData(new DataInputStream (this.getSocket().getInputStream()), output);
		} catch (IOException e) {
			this.transfer.setAbort(true);
			System.out.println("Problema ao receber dados do peer" + this.peer.getIp());
			this.abort = true;
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

	/**
	 * Método que recebe os dados.
	 * */
	public ArrayList<DataType> receive() {
		ArrayList<DataType> dataTypeList = this.stream.receive();
		return dataTypeList;
	}	

	/**
	 * Método que encerra uma conexão.
	 * */
	public void closeConnection() {
		try {
			this.stream.close();
			this.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Método que concatena as partes recebidas do arquivo 
	 **/
	@Override
	public void run() {
		this.connect();
		this.createStreams();
		if (abort)
			return;
		//envia os dados sobre o pedaço solicitado.
		DataMusicTransfer askData = new DataMusicTransfer(
				this.transfer.getFile().getName(), this.offset,this.length);
		this.send(askData);
		
		//esperar que envie a música.
		byte[] bytes = new byte[askData.getLength()];
      
		InputStream is = null;
		try {
			is = this.socket.getInputStream();
			int count;
	        int position = 0;
	        byte[] bytes2 = new byte[1024];
	        while ((count = is.read(bytes2)) >= 0) {
	        	System.arraycopy(bytes2, 0, bytes, position, count);
	            position += count;
	        }//le todos os bytes
		} catch (IOException e) {
			this.transfer.setAbort(true);
			System.out.println("Problema ao receber dados do peer" + this.peer.getIp());
			e.printStackTrace();
		}
		System.out.println(this.transfer.getFile().getName() + ":Leu de " + this.getOffset() + 
				" a " + (this.getOffset() + this.getLength()) + " do peer " + this.peer.getIp());
		//concatena
		System.arraycopy(bytes, 0, this.transfer.getFile().getContent(), 
				this.offset, this.length);
		System.out.println("Concatenou o pedaço do peer " + this.peer.getIp());
		
		try {
			is.close();
		} catch (IOException e) {
			this.transfer.setAbort(true);
			System.out.println("Problema ao receber dados do peer" + this.peer.getIp());
			e.printStackTrace();
			return;
		}
		this.closeConnection();
		this.getTransfer().removeReceiver(this);
	}

	public void connect() {
		try {
			this.socket = new Socket(this.peer.getIp(), this.port);
		} catch (IOException e) {
			this.transfer.setAbort(true);
			System.out.println("Problema ao receber dados do peer" + this.peer.getIp());
			this.abort = true;
			e.printStackTrace();
		}
		
	}
	
	/**Getters e Setters*/
	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public IOData getStream() {
		return stream;
	}

	public void setStream(IOData stream) {
		this.stream = stream;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}

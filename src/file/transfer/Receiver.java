package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import router.Peer;

/**
 * Thread que ficara respons�vel por receber um arquivo(ou peda�o dele),
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
	
	/** Transfer que ser� usado no m�todo run, que juntar� as partes do arquivo **/
	private Transfer transfer;
	
	public Receiver(Transfer transfer, Socket socket, int offset, int length) {
		this.transfer = transfer;
		this.setSocket(socket);
		this.setOffset(offset);
		this.setLength(length);
		
		this.createStreams();
		this.sendInfo();
	}
	
	/**
	 * Enviar as informações sobre o arquivo que está sendo requisitado.
	 * */
	protected void sendInfo(){
		
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
			e.printStackTrace();
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * Checa a integridade dos bytes recebidos.
	 * */
	private boolean checkFile(ArrayList<DataType> dataList) {
		
		return false;
	}
	
	/**
	 * M�todo que concatena as partes recebidas do arquivo 
	 **/
	@Override
	public void run() {
		ArrayList<DataType> dataList = this.receive();
		
		/* if (this.checkFile(dataList)){
			checa a integridade
			adiciona os bytes para o arquivo.
			Registry.getInstance().getTransfer().setBytesToFile(this.getOffset(), this.getLength(),dataList);
		} */
		
		DataMusicTransfer data = (DataMusicTransfer) dataList.get(0);
		System.arraycopy(data.getContent(), 0, this.transfer.getFile().getContent(), 
						 data.getOffset(), data.getLength());
		this.closeConnection();
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
}

package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import router.Peer;

public class TransferSender implements Runnable {

	private Peer peer;
	private IOData stream;
	private Socket connection;
	private Sender sender;
	
	public TransferSender(Socket connection, Sender sender){
		this.sender = sender;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		ArrayList<DataType> dataList = this.receive();
		DataMusicTransfer data = (DataMusicTransfer) dataList.get(0);
		
		File file = new File("arquivos/" + data.getFileName());
		FileInputStream fis;
		OutputStream os = null;
            
		try {
			os = this.connection.getOutputStream();
			byte[] mybytearray = new byte[data.getLength()];
	        fis = new FileInputStream(file);
	        fis.skip(data.getOffset());
	        fis.read(mybytearray, 0, data.getLength());
            os.write(mybytearray, 0, data.getLength());
            os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Enviou para o peer " + this.peer.getIp() + 
				", de " + data.getOffset() + " a " +(data.getOffset()+ data.getLength()) + 
				" do aquivo " + data.getFileName());
		this.closeConnection();
		this.sender.remove(this);
	}
}

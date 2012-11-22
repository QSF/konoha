package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import router.Peer;

public class TransferSender implements Runnable {

	private Peer peer;
	private IOData stream;
	private Socket connection;
	private boolean runCondition;
	
	public TransferSender(Socket connection){
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
			DataMusicTransfer data = (DataMusicTransfer) dataList.get(0);
			
			File file = new File(data.getFileName() + ".mp3");
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				byte[] bytes = new byte[data.getLength()];
				fis.read(bytes, data.getOffset(), data.getLength());
				data.setContent(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.send(data);
		}	
		this.closeConnection();
	}
}

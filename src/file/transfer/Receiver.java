package file.transfer;

import java.net.Socket;

import router.Peer;

public class Receiver implements Runnable {

	private Peer peer;
	private Socket socket;
	private IOData stream;
	private int offset;
	private int length;
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}

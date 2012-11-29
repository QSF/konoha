package router;

import java.util.ArrayList;

import application.Registry;
import file.transfer.DataType;
import file.transfer.OperationCode;

public class TimerRouter implements Runnable {

	@Override
	public void run() {
		
		while (true){
			try {
				//espera 10 segundos
				Thread.currentThread().sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//envia um end para cada peer, pedindo seus vizinhos.
			Registry.getInstance().getRouter().askNeighbors();
//			ArrayList<Peer> peers = (ArrayList<Peer>) Registry.getInstance().getRouter().getPeers().clone();
//			for (Peer peer : peers){
//				DataType data = new DataType();
//				data.getOperations().add(OperationCode.END);
//				Registry.getInstance().getRouter().search(peer, data);
//			}
		}
	}

}

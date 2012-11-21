package file.transfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread que escuta os pedidos de transferência de partes de 
 * um arquivo.
 * Cria threads que enviam os arquivos.
 * */
public class Sender implements Runnable {
	
	private ServerSocket serverSocket;

	@Override
	public void run() {
		this.serverSocket = null;
		
		try {
			
			this.serverSocket = new ServerSocket(13267); 
	    
			while (true) {  
		  
				  Socket sock;
				  sock = this.serverSocket.accept();
				  System.out.println("Conexão aceita: " + sock);  
				  //criar nova thread para enviar arquivos.
				  
				  // envia o arquivo (transforma em byte array)  
				  File myFile = new File ("meuArquivo.pdf");  
				  byte [] mybytearray  = new byte [(int)myFile.length()];  
				  FileInputStream fis = new FileInputStream(myFile);  
				  BufferedInputStream bis = new BufferedInputStream(fis);  
				  bis.read(mybytearray,0,mybytearray.length);  
				  OutputStream os = sock.getOutputStream();  
				  System.out.println("Enviando...");  
				  os.write(mybytearray,0,mybytearray.length);  
				  os.flush();  
				  sock.close();  
		      }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

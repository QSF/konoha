package file.transfer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import router.DataAnswer;
import router.DataNeighbors;
import router.DataSearch;
import router.Peer;

/**
 * Classe responsável por fazer toda a comunicação de rede.
 * Sempre que um pacote for enviado ou recebido, será por essa classe.
 * */
public class IOData {
		/**Stream de saida*/
		private DataOutputStream output;
		
		/**Stream de entrada*/
		private DataInputStream input;
		
		public IOData(DataInputStream input,DataOutputStream output) {
			this.input = input;
			this.output = output;
		}
		
		/**
		 * Método que recebe um pacote.
		 * Para transformar os bytes em data type, a função
		 * IOData.byteToDataType() é utilizada.
		 * 
		 * @return dataTypeList Lista de DataTypes.
		 * @see IOData.byteToDataType()
		 *   
		 * */
		public ArrayList<DataType> receive() {
			try {
				//instanciando e lendo a quantia de DataType
				ArrayList<DataType> dataTypeList = new ArrayList<DataType>();
				int size = this.input.readInt();
				int[] length = new int[size];
				int i=0;
				
				//lendo o tamanho de cada DataType
				for(i=0; i<size; i++){
					length[i] = this.input.readInt();
					i++;		
				}
				
				//
				for (i=0 ; i < size ; i++){
					byte[] bytes = new byte[length[i]];
					this.input.read(bytes,0,length[i]);
					dataTypeList.add(this.byteToDataType(bytes));
				}
				
				
				return dataTypeList;
			} catch (IOException e){
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Método que envia um pacote.
		 * Para transformar os data types em bytes, a função
		 * IOData.dataTypeToBytes()
		 * 
		 * @return data Lista de DataTypes.
		 * @see IOData.dataTypeToBytes()
		 *   
		 * */
		public void send(ArrayList<DataType> data) {
			try {
				int size = 0;
				ArrayList<byte[]> bytesList = new ArrayList<byte[]>();
				//Convertendo cada DataType em bytes
				for(DataType dataType: data){
					bytesList.add(this.dataTypeToByte(dataType));
					size = size + bytesList.get(bytesList.size()-1).length;
				}
				//grava a quantidade de DataTypes
				this.output.writeInt(bytesList.size());
				
				byte[] bytes = new byte[size];
				int destPos = 0;
				//grava o tamanho de cada DataType
				for(byte[] tmp: bytesList){
					System.arraycopy(tmp, 0, bytes, destPos, tmp.length);
					destPos = destPos + tmp.length;
					this.output.writeInt(tmp.length);
				}
				
						
				this.output.write(bytes);
				this.output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Transforma um DataType em um array de bytes.
		 * É utilizado a chama de métodos dinâmicos, de acordo com o
		 * nome da operação que será convertida.
		 * Desta forma, este métodos são "especializados" em converter
		 * para bytes, um determinado DataType.
		 * 
		 * @param data DataType que será convertido em bytes.
		 * @return bytes bytes[], array de bytes equivalente ao data passado.
		 * 
		 * @see OperationCode
		 * */
		private byte[] dataTypeToByte(DataType data) throws UnsupportedEncodingException {		
			for (int i = 0; i < data.getOperations().size(); i++){//loopa por todos os "protocolos"
				
				String methodName = data.getOperations().get(i) + "ToBytes";
				try {
					/*Chama um método de forma dinâmica
					 * Assim, haverá um método para cada tipo de header*/
					Method method = this.getClass().getMethod(methodName, data.getClass());
					return (byte[]) method.invoke(this, data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}			
			}
			return null;
		}
		
		/**
		 * Método específico que transforma um DataNeighbors
		 * em um array de bytes.
		 * */
		public byte[] NEIGHBORSToBytes(DataNeighbors data) {
			/*
			 * OPERATION CODE(1) + QTD_PEERS(1) + LISTA DE PEERS
			 * Onde cada peer contém o seu ip, e o InetAddres
			 * tem tamanho 4(bytes).
			 * */

			byte[] bytes = new byte[1 + 1 + (4 * data.getPeers().size())];
			
			//header
			bytes[0] = OperationCode.enumToByte(data.getOperations().get(0));
			//qtd de peers
			bytes[1] = (byte) data.getPeers().size();
			
			int i = 1;
			
			for (Peer peer : data.getPeers()){
				byte[] addr = peer.getIp().getAddress();
				
				bytes[++i] = addr[0];
				bytes[++i] = addr[1];
				bytes[++i] = addr[2];
				bytes[++i] = addr[3];				
			}
			
			return bytes;
		}
		
		/**
		 * Método específico que transforma um DataSearch
		 * em um array de bytes.
		 * */
		public byte[] SEARCHToBytes(DataSearch data){
			/*
			 * OPERATION CODE(1) + TTL(1) + IP + FILE NAME
			 * */
			byte[] bytes = new byte[1 + 1 + 4 +
			                        ( data.getFileName().length())];
			
			//header
			bytes[0] = OperationCode.enumToByte(data.getOperations().get(0));
			//time to live
			bytes[1] = data.getTTL();
			
			ByteArrayOutputStream decoding = new ByteArrayOutputStream();
			decoding.write(bytes[0]);
			decoding.write(bytes[1]);
			try {
				byte[] addr = data.getPeer().getIp().getAddress();
				decoding.write(addr);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				decoding.write(data.getFileName().getBytes("UTF-8"));
				return decoding.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		/**
		 * Método que atribui que passa o operation code END do dataType para bytes 
		 * */
		public byte[] ENDToBytes(DataType data){
			return OperationCodeToByte(data);
		}
		
		/**
		 * Método que atribui que passa o operation code ASKNEIGHBORS do dataType para bytes 
		 * */
		public byte[] ASKNEIGHBORSToBytes(DataType data){
			return OperationCodeToByte(data);
		}
		
		
		/**
		 * Método específico que transforma um DataAnswer
		 * em um array de bytes.
		 * */
		public byte[] ANSWERToBytes(DataAnswer data){
			/*
			 * OPERATION CODE(1) + FILE SIZE(8) + FILE NAME
			 * */
			byte[] bytes = new byte[1 + 8 + data.getFileName().length()];
			
			//header
			bytes[0] = OperationCode.enumToByte(data.getOperations().get(0));
						
			ByteArrayOutputStream decoding = new ByteArrayOutputStream();
			decoding.write(bytes[0]);

			//FILE SIZE
			Long size = data.getSize();
			decoding.write(size.byteValue());
			try {
				//FILE NAME
				decoding.write(data.getFileName().getBytes("UTF-8"));
				return decoding.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
				
		/**
		 * Método que atribui que passa o operation code ISALIVE do dataType para bytes 
		 * */
		public byte[] ISALIVEToBytes(DataType data){
			return OperationCodeToByte(data);
		}
		
		
		/**
		 * Método que pega um DataType simples, só com operation code
		 * e passa para bytes.
		 */
		private byte[] OperationCodeToByte(DataType data){
			byte[] bytes = new byte[1];
			bytes[0] = OperationCode.enumToByte(data.getOperations().get(0));
			return bytes;
		}
		
		/**
		 * Método que converte um array de bytes em um DataType só com operation code
		 * */
		public DataType byteToOperationCode(byte[] bytes){
			DataType data = new DataType();
			data.getOperations().add(OperationCode.byteToEnum(bytes[0]));	
			return data;
		}
		
		
		
		/**
		 * Transforma um array de bytes em um DataType.
		 * É utilizado a chamada de métodos dinâmicos, de acordo com o
		 * nome da operação que será convertida.
		 * Desta forma, este métodos são "especializados" em converter
		 * um determinado DataType para bytes.
		 * 
		 * @param  bytes bytes[], array de bytes.
		 * @return data DataType que é equivalente ao array de bytes passado.
		 * 
		 * @see OperationCode
		 * */
		private DataType byteToDataType(byte[] bytes) throws UnsupportedEncodingException {
			/*Chama os métodos de maneira dinâmica
			 * Tem que fazer um esquema caso tenha vários DataTypes em um mesmo protocolo*/
			String methodName = "bytesTo" + OperationCode.byteToEnum(bytes[0]);
			 
			try {
				Method method = this.getClass().getMethod(methodName, bytes.getClass());
				return (DataType) method.invoke(this, bytes);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException 
									| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Método que converte um array de bytes em um DataNeighbors
		 * */
		public DataNeighbors bytesToNEIGHBORS(byte[] bytes){
			DataNeighbors data = new DataNeighbors();
			
			data.getOperations().add(OperationCode.byteToEnum(bytes[0]));
			
			byte neighborsSize = bytes[1];
			
			int i = 1;
			for (int j = 0; j < neighborsSize; j++){
				Peer peer = new Peer();
				byte[] addr = new byte[4];
				addr[0] = bytes[++i];
				addr[1] = bytes[++i];
				addr[2] = bytes[++i];
				addr[3] = bytes[++i];
				
				try {
					peer.setIp((Inet4Address) Inet4Address.getByAddress(addr));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
				data.getPeers().add(peer);
			}

			return data;		
		}
		
		/**
		 * Método que converte um array de bytes em um DataSearch
		 * */
		public DataSearch bytesToSEARCH(byte[] bytes){
			DataSearch data = new DataSearch();
			//Operation code
			data.getOperations().add(OperationCode.byteToEnum(bytes[0]));
			
			//TTL
			data.setTTL(bytes[1]);

			//IP
			int i = 1;
			Peer peer = new Peer();
			byte[] addr = new byte[4];
			addr[0] = bytes[++i];
			addr[1] = bytes[++i];
			addr[2] = bytes[++i];
			addr[3] = bytes[++i];
			try {
				peer.setIp((Inet4Address) Inet4Address.getByAddress(addr));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			data.setPeer(peer);
			
			//FileName
			String fileName = null;
			try {
				fileName = new String(bytes,6,bytes.length-6,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setFileName(fileName);
			
			return data;		
		}
		
		/**
		 * Método que atribui que passa um array de bytes
		 * para um dataType com o  operation code ASKNEIGHBORS   
		 * */
		public DataType bytesToASKNEIGHBORS(byte[] bytes){
			return byteToOperationCode(bytes);		
		}
		
		/**
		 * Método que atribui que passa um array de bytes
		 * para um dataType com o  operation code END
		 * */
		public DataType bytesToEND(byte[] bytes){
			return byteToOperationCode(bytes);		
		}
		
		/**
		 * Método que converte um array de bytes em um DataAnswer
		 * */
		public DataAnswer bytesToANSWER(byte[] bytes){
			DataAnswer data = new DataAnswer();
			//Operation Code
			data.getOperations().add(OperationCode.byteToEnum(bytes[0]));
			
			byte[] size = new byte[8];
			for (int i=0; i<8; i++)
				size[i] = bytes[i+1];
			
			
			String fileName = null;
			try {
				fileName = new String(bytes,9,bytes.length-9,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setFileName(fileName);
			
			return data;			
		}
		
		/**
		 * Método que atribui que passa um array de bytes
		 * para um dataType com o  operation code ISALIVE   
		 * */
		public DataType bytesToISALIVE(byte[] bytes){
			return byteToOperationCode(bytes);		
		}
		
		
		/** M�todo espec�fico que converte um DataMusicTransfer
		 * em um array de bytes
		 **/
		public byte[] MUSICTRANSFERtoBytes(DataMusicTransfer data) {
			/*
			 * OPERATION CODE(1) + OFFSET(4) + LENGTH(4) + CONTENT
			 * Onde offset representa qual o byte inicial,
			 * length a quantidade a ser lida e content o conteudo
			 * */
			//Aloca quantidade de bytes necess�rios
			byte[] bytes = new byte[1 + (4 * 1) + (4 * 1) + data.content.length];
			//Converte os int em um array de bytes		
			byte[] off = ByteBuffer.allocate(4).putInt(data.getOffset()).array();
			byte[] len = ByteBuffer.allocate(4).putInt(data.getLength()).array();
			//Atribui os valores no array
			bytes[0] = OperationCode.enumToByte(data.getOperations().get(0));
			//Copia array de bytes para array de bytes - (src, posSrc, dest, posDest, qtd)
			System.arraycopy(off, 0, bytes, 1, 4);
			System.arraycopy(len, 0, bytes, 5, 4);
			System.arraycopy(data.content, 0, bytes, 9, data.content.length);
			
			return bytes;	
		}
		
		/**
		 * M�todo que converte um array de bytes em um DataMusicTransfer
		 **/
		public DataMusicTransfer bytesToMUSICTRANSFER(byte[] bytes) {
			DataMusicTransfer data = new DataMusicTransfer();
			
			byte[] off = new byte[4];
			byte[] len = new byte[4];
			
			System.arraycopy(bytes, 1, off, 0, 4);
			System.arraycopy(bytes, 5, len, 0, 4);
			
			ByteBuffer offWrapper = ByteBuffer.wrap(off);
			ByteBuffer lenWrapper = ByteBuffer.wrap(len);
			
			byte[] cont = new byte[lenWrapper.getInt()];
			System.arraycopy(bytes, 9, cont, 0, (bytes.length - 9));
			
			data.getOperations().add(OperationCode.byteToEnum(bytes[0])); 
			data.setOffset(offWrapper.getInt());
			data.setLength(lenWrapper.getInt());
			data.setContent(cont);
			return data;
		}
		
		/**
		 * Método que fecha a conexão com os streams.
		 * */
		public void close() throws IOException {
			this.output.close();
			this.input.close();
		}
}

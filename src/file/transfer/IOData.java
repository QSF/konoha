package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
		 * Método que fecha a conexão com os streams.
		 * */
		public void close() throws IOException {
			this.output.close();
			this.input.close();
		}
}

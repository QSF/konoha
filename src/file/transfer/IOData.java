package file.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class IOData {
		private DataOutputStream output;
		private DataInputStream input;
		
		public IOData(DataInputStream input,DataOutputStream output) {
			this.input = input;
			this.output = output;
		}
		
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
		
		/*Devemos fazer um send com Arraylist de DataType*/
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
		
		private byte[] dataTypeToByte(DataType data) throws UnsupportedEncodingException {		
			for (int i = 0; i < data.getOperations().size(); i++){//loopa por todos os "protocolos"
				
				String methodName = data.getOperations().get(i) + "ToBytes";
				try {
					/*Chama um m�todo de forma din�mica
					 * Assim, haver� um m�todo para cada tipo de header*/
					Method method = this.getClass().getMethod(methodName, data.getClass());
					return (byte[]) method.invoke(this, data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}			
			}
			return null;
		}
		
		private DataType byteToDataType(byte[] bytes) throws UnsupportedEncodingException {
			/*Chama os m�todos de maneira din�mica
			 * Tem que fazer um esquema caso tenha v�rios DataTypes em um mesmo protocolo*/
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
}

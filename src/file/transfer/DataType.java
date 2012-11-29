package file.transfer;

import java.util.ArrayList;

/**
 * Classe que representa o protocolo de rede, em forma de objeto.
 * */
public class DataType {
	/**Header para saber quais os tipos de opeção deste pacote.*/
	protected ArrayList<OperationCode> operations = new ArrayList<OperationCode>();
		
	public ArrayList<OperationCode> getOperations() {
		return operations;
	}

	public void setOperations(ArrayList<OperationCode> operations) {
		this.operations = operations;
	}
}

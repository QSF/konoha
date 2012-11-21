package file.transfer;

import java.util.ArrayList;

public class DataType {
	protected ArrayList<OperationCode> operations = new ArrayList<OperationCode>();
		
	public ArrayList<OperationCode> getOperations() {
		return operations;
	}

	public void setOperations(ArrayList<OperationCode> operations) {
		this.operations = operations;
	}
}

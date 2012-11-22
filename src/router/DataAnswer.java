package router;

import file.transfer.DataType;

/**
 * Classe que representa um pacote de busca à um arquivo na rede.
 * */
public class DataAnswer extends DataType{
	
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

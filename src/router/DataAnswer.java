package router;

import file.transfer.DataType;

/**
 * Classe que representa um pacote de busca à um arquivo na rede.
 * */
public class DataAnswer extends DataType{
	
	private String fileName;
	
	private long size;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}

package router;

import file.transfer.DataType;

/**
 * Classe que representa um pacote de busca à um arquivo na rede.
 * */
public class DataSearch extends DataType{
	
	private String fileName;
	/* Time to live, número de saltos entre máquinas que os pacotes podem demorar 
	 *  numa rede de computadores antes de serem descartados.
	 */
	private byte TTL;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte getTTL() {
		return TTL;
	}

	public void setTTL(byte tTL) {
		TTL = tTL;
	}
	
	

}

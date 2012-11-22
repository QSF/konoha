package application;

/**
 * Esta classe armazenará as informações de um determinado arquivo.
 * */
public class DataFile {
	
	/**Nome do arquivo*/
	private String name;
	
	/**Tamanho do arquivo*/
	private long size;
	
	/** Conteudo do arquivo **/
	private byte[] content;
	
	/** Hash MD5 do conteúdo do arquivo, 
	 * utilizado para verificar a integridade do mesmo.
	 **/
	private String hash;
	
	/**Getters and Setters*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}

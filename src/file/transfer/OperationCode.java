package file.transfer;

/**
 * Enum que representa qual operações o pacote representa.
 * */
public enum OperationCode {
	//ASKNEIGHBORS -> 0 - pede a lista de vizinhos de um peer
	//NEIGHBORS -> 1 - manda a lista de vizinhos para quem pediu
	//MUSICTRANSFER -> 2 - transferir a musica para quem pediu
	ASKNEIGHBORS, NEIGHBORS, SEARCH, ANSWER, ISALIVE, END, MUSICTRANSFER;

	/**
	 * Converte um byte para Operation code.
	 */
	public static OperationCode byteToEnum(byte value){
		OperationCode list[] = values();
		for (int i = 0; i < list.length; i++)
			if (i == value) return list[i];
		return null;
	}
	
	/**
	 * Converte um OperationCode para byte.
	 */
	public static byte enumToByte(OperationCode value){
		byte i = 0;
		
		for (Enum<?> e : values()){
			if (e == value) return i;
			i++;
		}
		return -1;
	}
}

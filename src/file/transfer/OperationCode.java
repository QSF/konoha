package file.transfer;

/**
 * Enum que representa qual operações o pacote representa.
 * */
public enum OperationCode {
	ASKNEIGHBORS,NEIGHBORS,END;
	
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

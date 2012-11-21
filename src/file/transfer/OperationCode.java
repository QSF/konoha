package file.transfer;

public enum OperationCode {
	HEADER, CHAT, MELD, DISCARD, BUY, GOOUT, SCORE, TIME, MOUNT, DEAD, HAND, PLAYER;
	/*Adicionar headers:
	 * � a sua vez(ou n�o)
	 * O Usu�rio conectou(nome do user e dupla)
	 * 
	 * */
	public static OperationCode byteToEnum(byte value){
		OperationCode list[] = values();
		for (int i = 0; i < list.length; i++)
			if (i == value) return list[i];
		return null;
	}
	
	public static byte enumToByte(OperationCode value){
		byte i = 0;
		
		for (Enum<?> e : values()){
			if (e == value) return i;
			i++;
		}
		//legal seria exception
		return -1;
	}
}

package file.transfer;

public class DataMusicTransfer extends DataType {
	private int offset;
	private int length;
	private byte[] content = null; 
	private String fileName;
	
	public DataMusicTransfer(String fileName, int offset, int length){
		OperationCode op = OperationCode.MUSICTRANSFER;
		this.operations.add(op);
		this.offset = offset;
		this.length = length;
		this.fileName = fileName;
	}
	
	public DataMusicTransfer(){
		
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public byte[] getContent() {
		return this.content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}

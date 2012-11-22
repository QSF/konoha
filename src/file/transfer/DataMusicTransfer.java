package file.transfer;

public class DataMusicTransfer extends DataType {
	int offset;
	int length;
	byte[] content; 
	
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
}

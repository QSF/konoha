package gui;

/**
 * Representa um peer na GUI.
 * Contém IP e % que baixa deste peer.
 */
public class JPeer {
	
	private String ip;
	private String percentage;
	private String downloaded;
	
	public JPeer(){
		
	}
	
	public JPeer(String ip, String percentage, String downloaded){
		this.setIp(ip);
		this.setPercentage(percentage);
		this.setDownloaded(downloaded);
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		//formatar.
		this.ip = ip;
	}
	public String getPercentage() {
		//formatar.
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	@Override
	public String toString() {
		return this.getIp() + "       " + this.getPercentage()
				+ "           " + this.getDownloaded();
	}

	public String getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(String downloaded) {
		this.downloaded = downloaded;
	}

}

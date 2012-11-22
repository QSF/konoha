package router;

import java.net.Inet4Address;

/**
 * Objeto que representa um peer.
 * */
public class Peer {
	private Inet4Address ip;
	private int ping;
	private int percent;
	
	public Inet4Address getIp() {
		return ip;
	}
	
	public void setIp(Inet4Address ip) {
		//formatar.
		this.ip = ip;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
}

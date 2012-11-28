package router;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Objeto que representa um peer.
 * */
public class Peer {
	private Inet4Address ip;
	
	private float ping = -1;
	
	private int percent;
	
	public Inet4Address getIp() {
		return ip;
	}
	
	public void setIp(InetAddress inetAddress) {
		//formatar.
		this.ip = (Inet4Address) inetAddress;
	}

	public float getPing() {
		return ping;
	}

	public void setPing(float ping) {
		this.ping = ping;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
}

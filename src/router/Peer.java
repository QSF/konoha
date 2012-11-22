package router;

import java.net.Inet4Address;

/**
 * Objeto que representa um peer.
 * */
public class Peer {
	private Inet4Address ip;
	
	public Inet4Address getIp() {
		return ip;
	}
	
	public void setIp(Inet4Address ip) {
		//formatar.
		this.ip = ip;
	}
}

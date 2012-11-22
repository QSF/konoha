package router;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Objeto que representa um peer.
 * */
public class Peer {
	private Inet4Address ip;
	
	public Inet4Address getIp() {
		return ip;
	}
	
	public void setIp(InetAddress inetAddress) {
		//formatar.
		this.ip = (Inet4Address) inetAddress;
	}
}

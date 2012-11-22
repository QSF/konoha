package router;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IPTest {

	public static void main(String[] args) {
		try {
			Inet4Address ip = (Inet4Address) Inet4Address.getByName("192.168.1.1");
			byte[] addr = ip.getAddress();
			System.out.println(addr);
			System.out.println(addr.length);
			
			
			System.out.println(Inet4Address.getByAddress(addr)
					.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}

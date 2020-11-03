package me.pikod.main;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class SendSite {
	public static void runSender() {
		URL url;
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			url = new URL("https://www.shark-mc.com/send?server="+localhost.getHostAddress()+"&hostname="+localhost.getHostName()+"&port="+VirtualShop.plugin.getServer().getPort());
			URLConnection c = url.openConnection();
			c.getInputStream();
			VirtualShop.log.info("Successfully sended stats.");
		} catch (Exception e) {
			VirtualShop.log.warning("Stats could not be sent.");
		}
	}
}

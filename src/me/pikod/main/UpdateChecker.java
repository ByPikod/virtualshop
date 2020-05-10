package me.pikod.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;

public class UpdateChecker {
	private int projectId;
	private URL checkURL;
	private String newVersion;
	public boolean hasUpdate;
	public UpdateChecker(VirtualShop plugin) {
		this.projectId = 74496;
		this.newVersion = plugin.getDescription().getVersion();
		try {
			checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
			URLConnection con = checkURL.openConnection();
	        newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
	        if(plugin.getDescription().getVersion().equals(newVersion)) {
	        	hasUpdate = false;
	        }else {
	        	hasUpdate = true;
	        }
		}catch(Exception er) {
			Bukkit.getLogger().warning("&cCould not check to VirtualShop updates!");
		}
	}
}

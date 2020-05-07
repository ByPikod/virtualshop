package me.pikod.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PManager {
	public File shopConfig;
	public File langConfig;
	
	public PManager(VirtualShop plugin) {
		shopConfig = new File(plugin.getDataFolder(), "shops.yml");
		langConfig = new File(plugin.getDataFolder(), "lang.yml");
		try {
			firstRun(plugin);
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	private void firstRun(VirtualShop plugin) throws Exception {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		if(!shopConfig.exists()) {
			copy(plugin.getResource("shops.yml"), shopConfig);
		}
		if(!langConfig.exists()) {
			copy(plugin.getResource("lang.yml"), langConfig);
		}
	}
	
	public void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

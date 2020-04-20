package me.pikod.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.pikod.commands.cmdMain;
import me.pikod.commands.cmdMarket;
import me.pikod.listener.ActionHandler;
import net.milkbowl.vault.economy.Economy;

public class VirtualShop extends JavaPlugin {
	
	public static Logger log;
	public static VirtualShop plugin;
	public static PManager pmanager;
	public static FileConfiguration shops;
	public static FileConfiguration lang;
	public static Economy vault;
	public static UpdateChecker uc;
	public static boolean debugMode = false;
	
	public void vaultGet() {
		RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
		if(eco != null) {
			VirtualShop.vault = eco.getProvider();
			log.info("Success! Vault plugin are finded.");
		}else {
			log.warning("Failed get Vault! Disabling plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onEnable() {
		uc = new UpdateChecker(this);
		pmanager = new PManager(this);
		plugin = this;
		log = getLogger();
		shops = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));
		
		log.info("Settings setted!");
		
		new ActionHandler(this);
		new cmdMain(this);
		new cmdMarket(this);
		
		log.info("Commands executed!");
		
		log.info("Checking Vault plugin!");
		
		if(!getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
			log.warning(Color.chat("Vault are not installed! Disabling plugin!"));
			Bukkit.getPluginManager().disablePlugin(this);
		}
		vaultGet();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static void reloadShops() {
		shops = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "lang.yml"));
		if(!shops.isSet("categoryMenuSize")) {
			shops.set("categoryMenuSize", 4);
			try {
				shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
			} catch (IOException e) {
				
			}
			reloadShops();
		}
	}
	
	public static String strSade(String str) {
		char trueChars[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's' , 't', 'y', 'u', 'v', 'y', 'z', 'x' , 'q'};
		String ret = "";
		
		for(char key : str.toCharArray()){
			boolean isCorrect = false;
			for(char trueKey : trueChars) {
				if(key == trueKey) {
					isCorrect = true;
				}
			}
			if(isCorrect == false) {
				if(key == 'Ý') {
					ret = ret+"i";
				}else if(key == 'ý') {
					ret = ret+"i";
				}else if(key == 'ö') {
					ret = ret+"o";
				}else if(key == 'ü') {
					ret = ret+"ü";
				}else if(key == 'þ') {
					ret = ret+"s";
				}
			}else {
				ret = ret+key;
			}
			
			
		}
		return ret;
	}
	
	public static boolean isSade(String str) {
		char trueChars[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's' , 't', 'y', 'u', 'v', 'y', 'z', 'x' , 'q'};
		boolean isSade = true;
		
		for(char key : str.toCharArray()){
			boolean isCorrect = false;
			for(char trueKey : trueChars) {
				if(key == trueKey) {
					isCorrect = true;
				}
			}
			if(isCorrect == false) {
				isSade = false;
			}
			
		}
		return isSade;
	}
	
	public static String numberToStr(long sayi) {
		String ret = ""+sayi;
		if(sayi > 999) {
			ret = ret.substring(0, ret.length()-3) + "," + ret.substring(ret.length()-3);
		}
		if(sayi > 999999) {
			ret = ret.substring(0, ret.length()-7) + "," + ret.substring(ret.length()-7);
		}
		if(sayi > 999999999) {
			ret = ret.substring(0, ret.length()-11) + "," + ret.substring(ret.length()-11);
		}
		
		return ret;
	}

}

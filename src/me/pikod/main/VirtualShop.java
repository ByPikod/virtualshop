package me.pikod.main;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.pikod.commands.cmdMain;
import me.pikod.commands.cmdMarket;
import me.pikod.functions.Color;
import me.pikod.functions.f;
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
	public static boolean isUpperVersion;
	
	
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
		
		try {
			if(Material.matchMaterial("BLACK_STAINED_GLASS_PANE") == null) {
				isUpperVersion = false;
			}else isUpperVersion = true;
		}catch(Exception e) {
			isUpperVersion = false;
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static void reloadShops() {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		if(!pmanager.shopConfig.exists()) {
			pmanager.copy(plugin.getResource("shops.yml"), pmanager.shopConfig);
		}
		if(!pmanager.langConfig.exists()) {
			pmanager.copy(plugin.getResource("lang.yml"), pmanager.langConfig);
		}
		shops = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "lang.yml"));
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
		if(sayi > 999_999) {
			ret = ret.substring(0, ret.length()-7) + "," + ret.substring(ret.length()-7);
		}
		if(sayi > 999_999_999) {
			ret = ret.substring(0, ret.length()-11) + "," + ret.substring(ret.length()-11);
		}
		if(sayi > 999_999_999_999l) {
			ret = ret.substring(0, ret.length()-16) + "," + ret.substring(ret.length()-16);
		}
		if(sayi > 999_999_999_999_999l) {
			ret = ret.substring(0, ret.length()-23) + "," + ret.substring(ret.length()-23);
		}
		if(sayi > 999_999_999_999_999_999l) {
			ret = ret.substring(0, ret.length()-31) + "," + ret.substring(ret.length()-31);
		}
		return ret;
	}
	
	public static void setStainedGlassItem(ItemStack item, String color, int id) {
		if(isUpperVersion) {
			Material m = Material.matchMaterial(color+"_STAINED_GLASS_PANE");
			item.setType(m);
		}else {
			item.setDurability((short) id);
		}
	}
	
	public static ItemStack getStainedGlassItem(String color, int id) {
		ItemStack item;
		if(isUpperVersion) {
			if(debugMode) {
				f.bm("IS UPPER VERSION: TRUE\nCOLOR: "+color);
			}
			
			item = new ItemStack(Material.matchMaterial(color+"_STAINED_GLASS_PANE"));
		}else {
			if(debugMode) {
				f.bm("IS UPPER VERSION: FALSE\nCOLOR: "+color);
			}
			
			item = new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"));
			item.setDurability((short) id);
		}
		return item;
	}
}

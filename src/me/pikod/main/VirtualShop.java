package me.pikod.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.pikod.commands.cmdMain;
import me.pikod.commands.cmdMarket;
import me.pikod.commands.cmdSell;
import me.pikod.listener.ActionHandler;
import me.pikod.utils.Color;
import me.pikod.utils.f;
import net.milkbowl.vault.economy.Economy;

public class VirtualShop extends JavaPlugin {

	
	public static Logger log;
	public static VirtualShop plugin;
	public static PManager pmanager;
	public static YamlConfiguration shops;
	public static YamlConfiguration lang;
	public static YamlConfiguration config;
	public static Economy vault;
	public static UpdateChecker uc;
	public static boolean debugMode = false;
	public static boolean isUpperVersion;
	public static String country = System.getProperty("user.country");
	
	
	public void vaultGet() {
		RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
		if(eco != null) {
			VirtualShop.vault = eco.getProvider();
		}else {
			if(country.equals("TR")) {
				log.warning("Vault bulunamadý, eklenti devredýþý býrakýlýyor.");
			}else {
				log.warning("Failed get Vault! Disabling plugin.");
			}
			Bukkit.getPluginManager().disablePlugin(this);
		}
		log.info("Country: "+country);
	}
	
	@Override
	public void onEnable() {
		
		uc = new UpdateChecker(this);
		pmanager = new PManager(this);
		plugin = this;
		log = getLogger();
		shops = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));
		config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		
		log.info("Settings setted!");
		
		new ActionHandler(this);
		new cmdMain(this);
		new cmdMarket(this);
		new cmdSell(this);
		
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
		Seller.loadClass();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static void saveShops() {
		try {
			shops.save(pmanager.shopConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadShops() {
		reloadShopsPriv(false);
	}
	public static void reloadShops(boolean Seller) {
		reloadShopsPriv(Seller);
	}
	
	private static void reloadShopsPriv(boolean seller) {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		if(!pmanager.shopConfig.exists()) {
			pmanager.copy(plugin.getResource("shops.yml"), pmanager.shopConfig);
		}
		if(!pmanager.langConfig.exists()) {
			if(country.equals("TR")) {
				pmanager.copy(plugin.getResource("lang_tr.yml"), pmanager.langConfig);
			}else {
				pmanager.copy(plugin.getResource("lang.yml"), pmanager.langConfig);
			}
		}
		if(!pmanager.config.exists()) {
			pmanager.copy(plugin.getResource("config.yml"), pmanager.config);
		}
		config = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "config.yml"));
		shops = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "lang.yml"));
		if(seller) Seller.loadClass();
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
	
	public static String numberToStr(double sayi) {
		String strNumber = String.valueOf(sayi);
		String ret = ""+(long) sayi;
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
		String kalan = strNumber.substring(strNumber.indexOf('.'));
		if(!kalan.equals(".0")) {
			ret += kalan;
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

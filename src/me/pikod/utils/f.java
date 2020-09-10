package me.pikod.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.pikod.main.VirtualShop;

public class f {
	/**
	 * 
	 * Functions Class
	 * 
	 **/
	
	//ChatColor
	static public YamlConfiguration getLang() {return VirtualShop.lang;}
    static public String c(String message){ return ChatColor.translateAlternateColorCodes('&', message); }
    static public String autoConfig(String field){
    	if(VirtualShop.config.isSet(field)) {
    		return f.c(VirtualShop.config.getString(field));
    	}
		return "Undefined Field (config.yml): "+field;
    }
    static public String autoLang(String field){
    	if(VirtualShop.lang.isSet(field)) {
    		return f.c(VirtualShop.lang.getString(field));
    	}
		return "Undefined Field (lang.yml): "+field;
    }
    static public void pm(Player p, String message){ p.sendMessage(c(message)); }
    static public void pm(Player p, Integer message){ p.sendMessage(String.valueOf(message)); }
    static public void pm(Player p, boolean message){ p.sendMessage(String.valueOf(message)); }
   // static public void msa(Player p,String message){ p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(m(message))); }
    static public void bm(String message){ Bukkit.broadcastMessage(message+""); }
    static public void bm(int message){ Bukkit.broadcastMessage(String.valueOf(message)+""); }
    static public void bm(boolean message){ Bukkit.broadcastMessage(String.valueOf(message)); }
    static public void bm(Object message){ Bukkit.broadcastMessage(String.valueOf(message)); }
}

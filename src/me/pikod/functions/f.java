package me.pikod.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class f {
	/**
	 * 
	 * Functions Class
	 * 
	 **/
	
	//ChatColor
    static public String m(String message){ return message.replaceAll("§","&"); }
    static public void pm(Player p, String message){ p.sendMessage(m(message)); }
    static public void pm(Player p, Integer message){ p.sendMessage(String.valueOf(message)); }
    static public void pm(Player p, boolean message){ p.sendMessage(String.valueOf(message)); }
   // static public void msa(Player p,String message){ p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(m(message))); }
    static public void bm(String message){ Bukkit.broadcastMessage(message+""); }
    static public void bm(int message){ Bukkit.broadcastMessage(String.valueOf(message)+""); }
    static public void bm(boolean message){ Bukkit.broadcastMessage(String.valueOf(message)); }
    static public void bm(Object message){ Bukkit.broadcastMessage(String.valueOf(message)); }
}

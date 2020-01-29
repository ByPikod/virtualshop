package me.pikod.gui;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.pikod.main.VirtualShop;
import net.milkbowl.vault.economy.Economy;

public class VaultUsing {
	public static Economy vault;
	
	public void vaultGet() {
		RegisteredServiceProvider<Economy> eco = VirtualShop.plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if(eco != null) {
			vault = eco.getProvider();
		}
	}
	
	public void using(Player player) {
		vault.getBalance((OfflinePlayer) player);
	}
}

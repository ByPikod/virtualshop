package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.gui.guiCategories;
import me.pikod.gui.guiErisim;
import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class cmdMarket implements CommandExecutor {
	public cmdMarket(VirtualShop plugin) {
		plugin.getCommand("market").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("virtualshop.shop")) {
				new guiCategories((Player) sender);
			}else {
				sender.sendMessage(Color.chat(guiErisim.getStr("noPermission")));
			}
			
		}else {
			sender.sendMessage("Bu komut sadece oyun içinden kullanýlabilir!");
		}
		return true;
	}
}

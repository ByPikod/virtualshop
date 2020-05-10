package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.functions.Color;
import me.pikod.gui.GuiCategories;
import me.pikod.gui.GuiLanguage;
import me.pikod.main.VirtualShop;

public class cmdMarket implements CommandExecutor {
	public cmdMarket(VirtualShop plugin) {
		plugin.getCommand("shop").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("virtualshop.shop")) {
				new GuiCategories((Player) sender);
			}else {
				sender.sendMessage(Color.chat(GuiLanguage.getStr("noPermission")));
			}
			
		}else {
			sender.sendMessage("Bu komut sadece oyun içinden kullanýlabilir!");
		}
		return true;
	}
}

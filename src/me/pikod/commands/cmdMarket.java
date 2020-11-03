package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.gui.GuiCategories;
import me.pikod.main.VirtualShop;

public class cmdMarket implements CommandExecutor {
	public cmdMarket(VirtualShop plugin) {
		plugin.getCommand("shop").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			new GuiCategories((Player) sender);
		}else {
			sender.sendMessage("Bu komut sadece oyun içinden kullanýlabilir!");
		}
		return true;
	}
}

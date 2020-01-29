package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.gui.guiAdminMain;
import me.pikod.gui.guiErisim;
import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class cmdMain implements CommandExecutor {

	VirtualShop plugin;
	
	public cmdMain(VirtualShop plugin) {
		this.plugin = plugin;
		plugin.getCommand("virtualshop").setExecutor(this);
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("virtualshop.manage")) {
			if(sender instanceof Player) {
				new guiAdminMain((Player) sender);
			}else {
				sender.sendMessage(Color.chat(guiErisim.prefix+guiErisim.noPermission));
			}
		}
		return true;
	}
	
}

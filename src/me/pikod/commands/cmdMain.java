package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.functions.Color;
import me.pikod.gui.GuiAdminMain;
import me.pikod.gui.GuiLanguage;
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
				new GuiAdminMain((Player) sender);
			}else {
				sender.sendMessage(Color.chat(GuiLanguage.prefix+GuiLanguage.getStr("noPermission")));
			}
		}
		return true;
	}
	
}

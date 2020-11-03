package me.pikod.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pikod.gui.GuiAdminMain;
import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class cmdMain implements CommandExecutor {

	VirtualShop plugin;
	
	public cmdMain(VirtualShop plugin) {
		this.plugin = plugin;
		plugin.getCommand("virtualshop").setExecutor(this);
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("virtualshop.manage")) {
			if(args.length != 0) {
				if(args[0].toLowerCase().equals("gui")) {
					if(sender instanceof Player) {
						new GuiAdminMain((Player) sender);
						return true;
					}else {
						sender.sendMessage(Color.chat(f.autoLang("noConsole")));
					}
				}else if(args[0].toLowerCase().equals("reload")) {
					VirtualShop.reloadShops();
					sender.sendMessage(Color.chat(f.autoLang("successReloaded")));
					return true;
				}else if(args[0].toLowerCase().equals("help")) {
					if(VirtualShop.lang.isSet("helpMenu")) {
						String send = "";
						boolean first = true;
						for(String str : VirtualShop.lang.getStringList("helpMenu")) {
							if(!first) {
								send += "\n";
							}
							first = false;
							send += f.c(str);
						}
						sender.sendMessage(send);
					}else {
						sender.sendMessage(f.autoLang("helpMenu"));
					}
				}else {
					if(VirtualShop.country.equals("TR")) {
						sender.sendMessage(f.c("&6VirtualShop &ev"+VirtualShop.plugin.getDescription().getVersion()+"&6 - 'Pikod' tarafýndan yapýldý. Yardým almak için &e'/vs help' &6yazýnýz."));
					}else {
						sender.sendMessage(f.c("&6VirtualShop &ev"+VirtualShop.plugin.getDescription().getVersion()+"&6 - created by 'Pikod'. Use &e'/vs help' &6to get commands."));
					}
					return true;
				}
			}else {
				if(VirtualShop.country.equals("TR")) {
					sender.sendMessage(f.c("&6VirtualShop &ev"+VirtualShop.plugin.getDescription().getVersion()+"&6 - 'Pikod' tarafýndan yapýldý. Yardým almak için &e'/vs help' &6yazýnýz."));
				}else {
					sender.sendMessage(f.c("&6VirtualShop &ev"+VirtualShop.plugin.getDescription().getVersion()+"&6 - created by 'Pikod'. Use &e'/vs help' &6to get commands."));
				}
				return true;
			}
			
		}else {
			sender.sendMessage(f.autoLang("noPermission"));
		}
		return true;
	}
	
}

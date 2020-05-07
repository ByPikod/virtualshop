package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.functions.Color;

public class GuiAdminMain extends GuiManager {
	public GuiAdminMain(Player player) {
		this.create(1, GuiLanguage.admin_menu, "adminMenu");
		
		ItemStack reloadPlugin = new ItemStack(Material.BEACON);
		ItemStack kagit1 = new ItemStack(Material.PAPER, 1);
		//ItemStack kagit2 = new ItemStack(Material.PAPER, 1);
		ItemStack categories = new ItemStack(Material.ANVIL, 1);
		
		ItemMeta meta = kagit1.getItemMeta();
		meta.setDisplayName(Color.chat("&a&lPlugin Creators"));
		List<String> lore = new ArrayList<>();
		lore.add(Color.chat("&2Created by Pikod!"));
		lore.add(Color.chat("&2Click for more..."));
		meta.setLore(lore);
		kagit1.setItemMeta(meta);
		
		/*meta = kagit2.getItemMeta();
		meta.setDisplayName(renkli.chat("&a&lKullaným Rehberi"));
		lore.clear();
		lore.add(renkli.chat("&2Týklayarak spigot'taki eðitim"));
		lore.add(renkli.chat("&2rehberine göz atabilirsiniz!"));
		meta.setLore(lore);
		kagit2.setItemMeta(meta);*/
		
		meta = categories.getItemMeta();
		meta.setDisplayName(Color.chat("&a&l[Add / Edit] Category"));
		lore.clear();
		lore.add(Color.chat("&2Opens the add and"));
		lore.add(Color.chat("&2edit categories menu!"));
		meta.setLore(lore);
		categories.setItemMeta(meta);
		
		meta = reloadPlugin.getItemMeta();
		meta.setDisplayName(Color.chat("&c&lReload"));
		lore.clear();
		lore.add(Color.chat("&4Reloads config"));
		meta.setLore(lore);
		reloadPlugin.setItemMeta(meta);
		
		this.setItem(8, kagit1);
		//this.setItem(6, kagit2);
		this.setItem(4, categories);
		this.setItem(0, reloadPlugin);
		
		player.openInventory(this.getInventory());
	}
}

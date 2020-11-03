package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiAdminMain extends GuiManager {
	public GuiAdminMain(Player player) {
		super(player);
		this.create(1, f.autoLang("adminMainMenuTitle"));
		
		ItemStack reloadPlugin = new ItemStack(Material.BEACON);
		ItemStack kagit1 = new ItemStack(Material.PAPER, 1);
		//ItemStack kagit2 = new ItemStack(Material.PAPER, 1);
		ItemStack categories = new ItemStack(Material.ANVIL, 1);
		
		ItemMeta meta = kagit1.getItemMeta();
		meta.setDisplayName(f.autoLang("mainMenu_Creators"));
		List<String> lore = new ArrayList<>();
		if(VirtualShop.lang.isSet("creatorsLore")) {
			for(String str : VirtualShop.lang.getStringList("creatorsLore")) {
				lore.add(f.c(str));
			}
		}
		
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
		meta.setDisplayName(Color.chat(f.autoLang("mainMenu_Categories")));
		lore.clear();
		if(VirtualShop.lang.isSet("categoriesLore")) {
			for(String str : VirtualShop.lang.getStringList("categoriesLore")) {
				lore.add(f.c(str));
			}
		}
		meta.setLore(lore);
		categories.setItemMeta(meta);
		
		meta = reloadPlugin.getItemMeta();
		meta.setDisplayName(Color.chat(f.autoLang("mainMenu_Reload")));
		lore.clear();
		if(VirtualShop.lang.isSet("reloadLore")) {
			for(String str : VirtualShop.lang.getStringList("reloadLore")) {
				lore.add(f.c(str));
			}
		}
		meta.setLore(lore);
		reloadPlugin.setItemMeta(meta);
		
		this.setItem(8, kagit1);
		//this.setItem(6, kagit2);
		this.setItem(4, categories);
		this.setItem(0, reloadPlugin);
		
		player.openInventory(this.gui);
	}
}

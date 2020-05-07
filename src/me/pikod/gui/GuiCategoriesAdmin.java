package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.functions.Color;
import me.pikod.main.VirtualShop;

public class GuiCategoriesAdmin extends GuiManager {
	public GuiCategoriesAdmin(Player player) {
		this.create(VirtualShop.shops.getInt("categoryMenuSize"), Color.chat(GuiLanguage.categories_admin_menu), "categoriesAdmin");
		for(int i = 0; i < this.getInventory().getSize(); i++) {
			if(VirtualShop.shops.getConfigurationSection("categories."+i) != null) {
				
				ConfigurationSection s = VirtualShop.shops.getConfigurationSection("categories."+i);
				Material m = Material.matchMaterial(s.getString("item"));
				ItemStack item = new ItemStack(m, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Color.chat(s.getString("displayName")));
				List<String> lore = new ArrayList<String>();
				lore.add(Color.chat("&2Right Click:&a Delete/Edit category"));
				lore.add(Color.chat("&2Left Click:&a Add items to category"));
				meta.setLore(lore);
				item.setItemMeta(meta);
				item.setDurability((short) s.getInt("subID"));
				this.getInventory().setItem(i, item);
			}
		}
		
		player.openInventory(this.getInventory());
	}
}

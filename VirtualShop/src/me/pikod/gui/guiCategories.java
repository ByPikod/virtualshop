package me.pikod.gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.gui.guiErisim;
import me.pikod.gui.guiManager;
import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class guiCategories extends guiManager {
	public guiCategories(Player player) {
		this.create(VirtualShop.shops.getInt("categoryMenuSize"), Color.chat(guiErisim.getStr("categories_title")), "categories");
		for(int i = 0; i < this.getInventory().getSize(); i++) {
			if(VirtualShop.shops.getConfigurationSection("categories."+i) != null) {
				@SuppressWarnings("deprecation")
				Material m = Material.getMaterial(VirtualShop.shops.getConfigurationSection("categories."+i).getInt("item"));
				ItemStack item = new ItemStack(m, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Color.chat(VirtualShop.shops.getConfigurationSection("categories."+i).getString("displayName")));
				item.setItemMeta(meta);
				item.setDurability((short) VirtualShop.shops.getConfigurationSection("categories."+i).getInt("subID"));
				this.getInventory().setItem(i, item);
			}
		}
		
		player.openInventory(this.getInventory());
	}
}

package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiCategoriesAdmin extends GuiManager {
	public GuiCategoriesAdmin(Player player) {
		this.create(VirtualShop.config.getInt("categoryMenuSize"), Color.chat(f.autoLang("editCategoriesTitle")));
		for(int i = 0; i < this.getInventory().getSize(); i++) {
			if(VirtualShop.shops.getConfigurationSection("categories."+i) != null) {
				ConfigurationSection s = VirtualShop.shops.getConfigurationSection("categories."+i);
				Material m = Material.matchMaterial(s.getString("item"));
				ItemStack item = new ItemStack(m, 1);
				ItemMeta meta = item.getItemMeta();
				if(s.isSet("displayName"))
				meta.setDisplayName(f.c(s.getString("displayName")));
				List<String> lore = new ArrayList<String>();
				if(VirtualShop.lang.isSet("panelCategoryLore")) {
					for(String str : VirtualShop.lang.getStringList("panelCategoryLore")) {
						lore.add(f.c(str));
					}
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				item.setDurability((short) s.getInt("subID"));
				this.getInventory().setItem(i, item);
			}
		}
		
		player.openInventory(this.getInventory());
	}
}

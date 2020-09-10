package me.pikod.gui;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.gui.GuiLanguage;
import me.pikod.gui.GuiManager;
import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;

public class GuiCategories extends GuiManager {
	public GuiCategories(Player player) {
		this.create(VirtualShop.config.getInt("categoryMenuSize"), Color.chat(GuiLanguage.getStr("categoriesTitle")));
		for(int i = 0; i < this.getInventory().getSize(); i++) {
			if(VirtualShop.shops.getConfigurationSection("categories."+i) != null) {
				ConfigurationSection s = VirtualShop.shops.getConfigurationSection("categories."+i);
				Material m = Material.matchMaterial(s.getString("item"));
				ItemStack item = new ItemStack(m, 1);
				ItemMeta meta = item.getItemMeta();
				if(s.isSet("displayName"))
				meta.setDisplayName(Color.chat(s.getString("displayName")));
				item.setItemMeta(meta);
				item.setDurability((short) s.getInt("subID"));
				this.getInventory().setItem(i, item);
			}
		}
		
		player.openInventory(this.getInventory());
	}
}

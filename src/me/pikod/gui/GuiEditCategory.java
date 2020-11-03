package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.VirtualShop;
import me.pikod.main.data.DataCategory;
import me.pikod.main.data.DataSaver;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiEditCategory extends GuiManager {
	public GuiEditCategory(Player player, short categorySlot) {
		super(player);
		this.create(1, f.autoLang("editCategoryTitle"));
		YamlConfiguration lang = VirtualShop.lang;
		ConfigurationSection s = VirtualShop.shops.getConfigurationSection("categories."+categorySlot);
		DataCategory c = DataSaver.getCategory(categorySlot);
		ItemStack item = new ItemStack(Material.matchMaterial(s.getString("item")), 1);
		item.setDurability((short) VirtualShop.shops.getInt("categories."+categorySlot+".subID"));
		ItemMeta meta = item.getItemMeta();
		if(VirtualShop.shops.isSet("categories."+categorySlot+".displayName"))
		meta.setDisplayName(f.c(VirtualShop.shops.getString("categories."+categorySlot+".displayName")));
		
		List<String> lore = new ArrayList<String>();
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.setItem(4, item);
		
		ItemStack kaldir = new ItemStack(Material.LAVA_BUCKET, 1);
		meta = kaldir.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_Delete"));
		lore.clear();
		if(lang.isSet("editCategory_DeleteLore")) {
			for(String key : lang.getStringList("editCategory_DeleteLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		kaldir.setItemMeta(meta);
		
		this.setItem(8, kaldir);
		
		ItemStack slot = new ItemStack(Material.COMPASS, 1);
		meta = slot.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_CategoryId"));
		lore.clear();
		lore.add(""+categorySlot);
		meta.setLore(lore);
		slot.setItemMeta(meta);
		
		this.setItem(3, slot);
		
		ItemStack perm = new ItemStack(Material.ENDER_PEARL, 1);
		meta = perm.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_Permission"));
		lore.clear();
		String perm1;
		if(c.isPermission()) {
			perm1 = c.getPermission();
		}else perm1 = f.autoLang("nullKeyword");
		if(lang.isSet("editCategory_BackLore")) {
			for(String key : lang.getStringList("editCategory_PermissionLore")) {
				lore.add(f.c(key.replace("{PERMISSION}", perm1)));
			}
		}
		meta.setLore(lore);
		perm.setItemMeta(meta);
		
		this.setItem(1, perm);
		
		ItemStack esyaSayisi = new ItemStack(Material.CHEST, 1);
		meta = esyaSayisi.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_ItemCount"));
		lore.clear();
		if(VirtualShop.shops.getConfigurationSection("categories."+categorySlot+".shop") != null) {
			lore.add(""+VirtualShop.shops.getConfigurationSection("categories."+categorySlot+".shop").getKeys(false).size());
		}else {
			lore.add("0");
		}
		
		meta.setLore(lore);
		esyaSayisi.setItemMeta(meta);
		
		this.setItem(5, esyaSayisi);

		ItemStack decoration;
		
		if(VirtualShop.shops.getBoolean("categories."+categorySlot+".decoration")) {
			decoration = VirtualShop.getStainedGlassItem("LIME", 5);
		}else {
			decoration = VirtualShop.getStainedGlassItem("RED", 14);
		}
		
		meta = decoration.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_IsDecoration"));
		lore.clear();
		if(VirtualShop.shops.getBoolean("categories."+categorySlot+".decoration")) {
			lore.add(Color.chat(f.autoLang("decorationEnabled")));
		}else {
			lore.add(Color.chat(f.autoLang("decorationDisabled")));
		}
		
		meta.setLore(lore);
		decoration.setItemMeta(meta);	
		
		this.setItem(7, decoration);
		
		ItemStack geri = new ItemStack(Material.BARRIER);
		meta = geri.getItemMeta();
		meta.setDisplayName(f.autoLang("editCategory_Back"));
		lore.clear();
		if(lang.isSet("editCategory_BackLore")) {
			for(String key : lang.getStringList("editCategory_BackLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		geri.setItemMeta(meta);	
		
		this.setItem(0, geri);
		
		player.openInventory(gui);
	}
}

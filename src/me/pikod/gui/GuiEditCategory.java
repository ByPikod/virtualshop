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

public class GuiEditCategory extends GuiManager {
	public GuiEditCategory(Player player, short categorySlot) {
		this.create(1, GuiLanguage.edit_category, "editCategory");
		ConfigurationSection s = VirtualShop.shops.getConfigurationSection("categories."+categorySlot);
		ItemStack item = new ItemStack(Material.matchMaterial(s.getString("item")), 1);
		item.setDurability((short) VirtualShop.shops.getInt("categories."+categorySlot+".subID"));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.chat(VirtualShop.shops.getString("categories."+categorySlot+".displayName")));
		
		List<String> lore = new ArrayList<String>();
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.setItem(4, item);
		
		ItemStack kaldir = new ItemStack(Material.LAVA_BUCKET, 1);
		meta = kaldir.getItemMeta();
		meta.setDisplayName(Color.chat("&4Delete"));
		lore.clear();
		lore.add(Color.chat("&cClick for delete"));
		lore.add(Color.chat("&cthe category!"));
		meta.setLore(lore);
		kaldir.setItemMeta(meta);
		
		this.setItem(8, kaldir);
		
		ItemStack slot = new ItemStack(Material.COMPASS, 1);
		meta = slot.getItemMeta();
		meta.setDisplayName(Color.chat("&aCategory Location (Slot)"));
		lore.clear();
		lore.add(""+categorySlot);
		meta.setLore(lore);
		slot.setItemMeta(meta);
		
		this.setItem(3, slot);
		
		ItemStack esyaSayisi = new ItemStack(Material.CHEST, 1);
		meta = esyaSayisi.getItemMeta();
		meta.setDisplayName(Color.chat("&aItem Count"));
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
		meta.setDisplayName(Color.chat("&7Is Decoration"));
		lore.clear();
		if(VirtualShop.shops.getBoolean("categories."+categorySlot+".decoration")) {
			lore.add(Color.chat("&aEnabled"));
		}else {
			lore.add(Color.chat("&cDisabled"));
		}
		
		meta.setLore(lore);
		decoration.setItemMeta(meta);	
		
		this.setItem(7, decoration);
		
		ItemStack geri = new ItemStack(Material.BARRIER);
		meta = geri.getItemMeta();
		meta.setDisplayName(Color.chat("&4Back"));
		lore.clear();
		lore.add(Color.chat("&cClick for back"));
		lore.add(Color.chat("&cprevious menu!"));
		meta.setLore(lore);
		geri.setItemMeta(meta);	
		
		this.setItem(0, geri);
		
		player.openInventory(this.getInventory());
	}
}

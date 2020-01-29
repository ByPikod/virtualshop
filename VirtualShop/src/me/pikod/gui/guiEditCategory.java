package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class guiEditCategory extends guiManager {
	public guiEditCategory(Player player, short categorySlot) {
		this.create(1, guiErisim.edit_category, "editCategory");
		
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material.getMaterial(VirtualShop.shops.getConfigurationSection("categories."+categorySlot).getInt("item")), 1);
		item.setDurability((short) VirtualShop.shops.getConfigurationSection("categories."+categorySlot).getInt("subId"));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.chat(VirtualShop.shops.getString("categories."+categorySlot+".displayName")));
		
		List<String> lore = new ArrayList<String>();
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.setItem(4, item);
		
		ItemStack kaldir = new ItemStack(Material.LAVA_BUCKET, 1);
		meta = kaldir.getItemMeta();
		meta.setDisplayName(Color.chat("&4Kategoriyi Sil"));
		lore.clear();
		lore.add(Color.chat("&cKategoriyi sonsuza"));
		lore.add(Color.chat("&ckadar kaldýrýr!"));
		meta.setLore(lore);
		kaldir.setItemMeta(meta);
		
		this.setItem(8, kaldir);
		
		ItemStack slot = new ItemStack(Material.COMPASS, 1);
		meta = slot.getItemMeta();
		meta.setDisplayName(Color.chat("&aKategorinin Yeri (Slot)"));
		lore.clear();
		lore.add(""+categorySlot);
		meta.setLore(lore);
		slot.setItemMeta(meta);
		
		this.setItem(3, slot);
		
		ItemStack esyaSayisi = new ItemStack(Material.CHEST, 1);
		meta = esyaSayisi.getItemMeta();
		meta.setDisplayName(Color.chat("&aKategorideki Eþya Sayýsý"));
		lore.clear();
		if(VirtualShop.shops.getConfigurationSection("categories."+categorySlot+".shop") != null) {
			lore.add(""+VirtualShop.shops.getConfigurationSection("categories."+categorySlot+".shop").getKeys(false).size());
		}else {
			lore.add("0");
		}
		
		meta.setLore(lore);
		esyaSayisi.setItemMeta(meta);
		
		this.setItem(5, esyaSayisi);
		
		ItemStack geri = new ItemStack(Material.BARRIER);
		meta = geri.getItemMeta();
		meta.setDisplayName(Color.chat("&4Geri dön"));
		lore.clear();
		lore.add(Color.chat("&cBir önceki menüye"));
		lore.add(Color.chat("&cdönmenizi saðlar!"));
		meta.setLore(lore);
		geri.setItemMeta(meta);	
		
		this.setItem(0, geri);
		
		player.openInventory(this.getInventory());
	}
}

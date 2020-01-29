package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class guiAddItem extends guiManager {
	public guiAddItem(Player player, int page, int category) {
		this.create(6, Color.chat(guiErisim.add_item), "addItem");
		
		List<String> lore = new ArrayList<String>();
		ItemMeta meta;
		if(page != 1) {
			ItemStack geri = new ItemStack(Material.ARROW);
			meta = geri.getItemMeta();
			meta.setDisplayName(Color.chat("&9Önceki sayfa!"));
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}else {
			ItemStack geri = new ItemStack(Material.BARRIER);
			meta = geri.getItemMeta();
			meta.setDisplayName(Color.chat("&9Ana Sayfa"));
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}
		
		
		ItemStack categoryId = new ItemStack(Material.COMPASS, 1);
		meta = categoryId.getItemMeta();
		meta.setDisplayName(Color.chat("&9Kategori ID"));
		lore.clear();
		lore.add(""+category);
		meta.setLore(lore);
		categoryId.setItemMeta(meta);
		this.setItem(48, categoryId);
		
		ItemStack sayfa = new ItemStack(Material.PAPER, page);
		meta = sayfa.getItemMeta();
		meta.setDisplayName(Color.chat("&9Sayfa"));
		sayfa.setItemMeta(meta);
		this.setItem(49, sayfa);
		
		ItemStack esyaSayisi = new ItemStack(Material.CHEST, 1);
		meta = esyaSayisi.getItemMeta();
		meta.setDisplayName(Color.chat("&9Eþya Sayýsý"));
		lore.clear();
		if(VirtualShop.shops.getConfigurationSection("categories."+category+".shop") != null) {
			lore.add(""+VirtualShop.shops.getConfigurationSection("categories."+category+".shop").getKeys(false).size());
		}else {
			lore.add("0");
		}
		meta.setLore(lore);
		esyaSayisi.setItemMeta(meta);
		this.setItem(50, esyaSayisi);
		
		ItemStack ileri = new ItemStack(Material.ARROW);
		meta = ileri.getItemMeta();
		meta.setDisplayName(Color.chat("&9Sonraki sayfa!"));
		ileri.setItemMeta(meta);
		this.setItem(53, ileri);
		
		player.openInventory(this.getInventory());
		
		for(int i = (44*page)-44; i < 44*page; i++) {
			ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+i);
			if(shop != null) {
				@SuppressWarnings("deprecation")
				ItemStack item = new ItemStack(Material.getMaterial(shop.getInt("item")), shop.getInt("count"));
				item.setDurability((short) shop.getInt("subId"));
				meta = item.getItemMeta();
				lore.clear();
				String buyCost = shop.getString("buyCost");
				String sellCost = shop.getString("sellCost");
				
				String strBuy = VirtualShop.numberToStr(Long.parseLong(buyCost));
				String strSell = VirtualShop.numberToStr(Long.parseLong(sellCost));
				
				if(shop.isSet("lore")) {
					for(String key : shop.getConfigurationSection("lore").getKeys(false)) {
						lore.add(shop.getString("lore."+key));
					}
					lore.add(Color.chat("&r"));
					meta.setLore(lore);
				}
				
				lore.add(Color.chat("&2Düzenlemek için týkla!"));
				if(Long.parseLong(buyCost) == 0) {
					lore.add(Color.chat("&aAlým: &cKapalý"));
				}else {
					lore.add(Color.chat("&aAlým: &e"+strBuy));
				}
				
				if(Long.parseLong(sellCost) == 0) {
					lore.add(Color.chat("&aSatým: &cKapalý"));
				}else {
					lore.add(Color.chat("&aSatým: &e"+strSell));
				}
				
				if(shop.isSet("displayName")) {
					meta.setDisplayName(shop.getString("displayName"));
				}
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				if(shop.isSet("ench")) {
					for(String ench : shop.getConfigurationSection("ench").getKeys(false)) {
						@SuppressWarnings("deprecation")
						Enchantment enchObj = Enchantment.getById(Integer.parseInt(ench));
						item.addUnsafeEnchantment(enchObj, shop.getInt("ench."+ench));
					}
				}
				
				this.setItem(i-((44*page)-44), item);
			}
		}
		
	}
}

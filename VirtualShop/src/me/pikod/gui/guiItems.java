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

public class guiItems extends guiManager {
	public guiItems(Player player, int page, int category) {
		this.create(6, guiErisim.getStr("items_title"), "items");
		
		List<String> lore = new ArrayList<String>();
		ItemMeta meta;
		int maxPage = getMaxPage(category);
		
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
		
		
		ItemStack sayfa = new ItemStack(Material.PAPER, page);
		meta = sayfa.getItemMeta();
		meta.setDisplayName(Color.chat("&9Sayfa"));
		lore.clear();
		lore.add(Color.chat("&aKategori ID: &2"+category));
		meta.setLore(lore);
		sayfa.setItemMeta(meta);
		this.setItem(49, sayfa);
		if(page != maxPage) {
			ItemStack ileri = new ItemStack(Material.ARROW);
			meta = ileri.getItemMeta();
			meta.setDisplayName(Color.chat("&9Sonraki sayfa!"));
			ileri.setItemMeta(meta);
			this.setItem(53, ileri);
		}	
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
				
				if(Long.parseLong(buyCost) == 0) {
					lore.add(Color.chat(guiErisim.getStr("buyCost").replace("{COST}", guiErisim.getStr("buyClosed"))));
				}else {
					lore.add(Color.chat(guiErisim.getStr("buyCost").replace("{COST}", strBuy)));
				}
				
				if(Long.parseLong(sellCost) == 0) {
					lore.add(Color.chat(guiErisim.getStr("sellCost").replace("{COST}", guiErisim.getStr("sellClosed"))));
				}else {
					lore.add(Color.chat(guiErisim.getStr("sellCost").replace("{COST}", strSell)));
				}
				
				meta.setLore(lore);
				
				if(shop.isSet("displayName")) {
					meta.setDisplayName(shop.getString("displayName"));
				}
				
				
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
	
	public static int getMaxPage(int category) {
		ConfigurationSection categorySec = VirtualShop.shops.getConfigurationSection("categories."+category);
		int max = 1;
		if(!categorySec.isSet("shop")) return max;
		for(String shop : categorySec.getConfigurationSection("shop").getKeys(false)) {
			if(Integer.parseInt(shop) > max) {
				max = Integer.parseInt(shop);
			}
		}
		
		return (max/44)+1;
	}
	
	
}

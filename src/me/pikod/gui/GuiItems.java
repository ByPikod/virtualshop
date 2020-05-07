package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.functions.Color;
import me.pikod.main.VirtualShop;

public class GuiItems extends GuiManager {
	public GuiItems(Player player, int page, int category) {
		this.create(6, GuiLanguage.getStr("items_title"), "items");
		
		List<String> lore = new ArrayList<String>();
		ItemMeta meta;
		int maxPage = getMaxPage(category);
		
		if(page != 1) {
			ItemStack geri = new ItemStack(Material.ARROW);
			meta = geri.getItemMeta();
			meta.setDisplayName(Color.chat(GuiLanguage.getStr("previousPage")));
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}else {
			ItemStack geri = new ItemStack(Material.BARRIER);
			meta = geri.getItemMeta();
			meta.setDisplayName(Color.chat(GuiLanguage.getStr("mainMenu")));
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}
		
		
		ItemStack sayfa = new ItemStack(Material.PAPER, page);
		meta = sayfa.getItemMeta();
		meta.setDisplayName(Color.chat(GuiLanguage.getStr("page")));
		lore.clear();
		lore.add(Color.chat("&aCategory ID: &2"+category));
		meta.setLore(lore);
		sayfa.setItemMeta(meta);
		this.setItem(49, sayfa);
		if(page != maxPage) {
			ItemStack ileri = new ItemStack(Material.ARROW);
			meta = ileri.getItemMeta();
			meta.setDisplayName(Color.chat(GuiLanguage.getStr("nextPages")));
			ileri.setItemMeta(meta);
			this.setItem(53, ileri);
		}	
		player.openInventory(this.getInventory());
		
		for(int i = (44*page)-44; i < 44*page; i++) {
			ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+i);
			if(shop != null) {
				ItemStack item = new ItemStack(Material.matchMaterial(shop.getString("item")), shop.getInt("count"));
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
					lore.add(Color.chat(GuiLanguage.getStr("buyCost").replace("{COST}", GuiLanguage.getStr("buyClosed"))));
				}else {
					lore.add(Color.chat(GuiLanguage.getStr("buyCost").replace("{COST}", strBuy)));
				}
				
				if(Long.parseLong(sellCost) == 0) {
					lore.add(Color.chat(GuiLanguage.getStr("sellCost").replace("{COST}", GuiLanguage.getStr("sellClosed"))));
				}else {
					lore.add(Color.chat(GuiLanguage.getStr("sellCost").replace("{COST}", strSell)));
				}
				
				meta.setLore(lore);
				
				if(shop.isSet("displayName")) {
					meta.setDisplayName(shop.getString("displayName"));
				}
				
				
				item.setItemMeta(meta);
				
				if(shop.isSet("ench")) {
					for(String ench : shop.getConfigurationSection("ench").getKeys(false)) {
						Enchantment enchObj = Enchantment.getByName(ench);
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

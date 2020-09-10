package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiAddItem extends GuiManager {
	public GuiAddItem(Player player, int page, int category) {
		this.create(6, Color.chat(f.autoLang("addItemToCategoryTitle")));

		YamlConfiguration lang = VirtualShop.lang;
		List<String> lore = new ArrayList<String>();
		ItemMeta meta;
		if(page != 1) {
			ItemStack geri = new ItemStack(Material.ARROW);
			meta = geri.getItemMeta();
			meta.setDisplayName(f.autoLang("addItem_PreviousPage"));
			lore.clear();
			if(lang.isSet("panelPreviousPageLore")) {
				for(String key : lang.getStringList("panelPreviousPageLore")) {
					lore.add(f.c(key));
				}
			}
			meta.setLore(lore);
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}else {
			ItemStack geri = new ItemStack(Material.BARRIER);
			meta = geri.getItemMeta();
			meta.setDisplayName(f.autoLang("addItem_MainMenu"));
			lore.clear();
			if(lang.isSet("panelMainMenuLore")) {
				for(String key : lang.getStringList("panelMainMenuLore")) {
					lore.add(f.c(key));
				}
			}
			meta.setLore(lore);
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}
		
		
		ItemStack categoryId = new ItemStack(Material.COMPASS, 1);
		meta = categoryId.getItemMeta();
		meta.setDisplayName(f.autoLang("addItem_CategoryId"));
		lore.clear();
		lore.add(""+category);
		meta.setLore(lore);
		categoryId.setItemMeta(meta);
		this.setItem(48, categoryId);
		
		ItemStack sayfa = new ItemStack(Material.PAPER, page);
		meta = sayfa.getItemMeta();
		meta.setDisplayName(f.autoLang("addItem_Page"));
		lore.clear();
		if(lang.isSet("panelPageLore")) {
			for(String key : lang.getStringList("panelPageLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		sayfa.setItemMeta(meta);
		this.setItem(49, sayfa);
		
		ItemStack esyaSayisi = new ItemStack(Material.CHEST, 1);
		meta = esyaSayisi.getItemMeta();
		meta.setDisplayName(f.autoLang("addItem_ItemCount"));
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
		meta.setDisplayName(f.autoLang("addItem_NextPage"));
		lore.clear();
		if(lang.isSet("panelNextPageLore")) {
			for(String key : lang.getStringList("panelNextPageLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		ileri.setItemMeta(meta);
		this.setItem(53, ileri);
		
		player.openInventory(this.getInventory());
		
		int baslangic, bitis;
		int sizeOfPreviousPages = ((page-1) * 45);
		baslangic = sizeOfPreviousPages+1;
		bitis = sizeOfPreviousPages+45;
		
		for(int i = baslangic; i <= bitis; i++) {
			ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+i);
			if(shop != null) {
				
				ItemStack item = new ItemStack(Material.matchMaterial(shop.getString("item")), shop.getInt("count"));
				item.setDurability((short) shop.getInt("subId"));
				meta = item.getItemMeta();
				lore.clear();
				String buyCost = shop.getString("buyCost");
				String sellCost = shop.getString("sellCost");
				
				String strBuy = VirtualShop.numberToStr(Double.valueOf(buyCost));
				String strSell = VirtualShop.numberToStr(Double.valueOf(sellCost));
				
				if(shop.isSet("lore")) {
					for(String key : shop.getConfigurationSection("lore").getKeys(false)) {
						lore.add(shop.getString("lore."+key));
					}
					lore.add(Color.chat("&r"));
					meta.setLore(lore);
				}
				if(strBuy.equals("0")) {
					strBuy = f.autoLang("panelBuyClosed");
				}
				if(strSell.equals("0")) {
					strSell = f.autoLang("panelSellClosed");
				}
				if(lang.isSet("panelItemLore")) {
					for(String key : lang.getStringList("panelItemLore")) {
						lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
					}
				}
				
				
				if(shop.isSet("displayName")) {
					meta.setDisplayName(shop.getString("displayName"));
				}
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				if(shop.isSet("ench")) {
					for(String ench : shop.getConfigurationSection("ench").getKeys(false)) {
						Enchantment enchObj = Enchantment.getByName(ench);
						item.addUnsafeEnchantment(enchObj, shop.getInt("ench."+ench));
					}
				}
				
				this.setItem((i-sizeOfPreviousPages)-1, item);
			}
		}
		
	}
}

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

public class GuiItems extends GuiManager {
	public GuiItems(Player player, int page, int category) {
		this.create(6, f.autoLang("itemsTitle"));
		List<String> lore = new ArrayList<String>();
		ItemMeta meta;
		int maxPage = getMaxPage(category);

		YamlConfiguration lang = VirtualShop.lang;
		if(page != 1) {
			ItemStack geri = new ItemStack(Material.ARROW);
			meta = geri.getItemMeta();
			meta.setDisplayName(f.autoLang("previousPage"));
			lore.clear();
			if(lang.isSet("previousPageLore")) {
				for(String key : lang.getStringList("previousPageLore")) {
					lore.add(f.c(key));
				}
			}
			meta.setLore(lore);
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}else {
			ItemStack geri = new ItemStack(Material.BARRIER);
			meta = geri.getItemMeta();
			meta.setDisplayName(f.autoLang("mainMenu"));
			lore.clear();
			if(lang.isSet("mainMenuLore")) {
				for(String key : lang.getStringList("mainMenuLore")) {
					lore.add(f.c(key));
				}
			}
			meta.setLore(lore);
			geri.setItemMeta(meta);
			this.setItem(45, geri);
		}
		
		
		ItemStack sayfa = new ItemStack(Material.PAPER, page);
		meta = sayfa.getItemMeta();
		meta.setDisplayName(f.autoLang("page"));
		lore.clear();
		lore.add(Color.chat("&aCategory ID: &2"+category));
		meta.setLore(lore);
		sayfa.setItemMeta(meta);
		this.setItem(49, sayfa);
		if(page != maxPage) {
			ItemStack ileri = new ItemStack(Material.ARROW);
			meta = ileri.getItemMeta();
			meta.setDisplayName(f.autoLang("nextPage"));
			lore.clear();
			if(lang.isSet("nextPageLore")) {
				for(String key : lang.getStringList("nextPageLore")) {
					lore.add(f.c(key));
				}
			}
			meta.setLore(lore);
			ileri.setItemMeta(meta);
			this.setItem(53, ileri);
		}	
		
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
					strBuy = f.autoLang("buyClosed");
				}
				if(strSell.equals("0")) {
					strSell = f.autoLang("sellClosed");
				}
				if(lang.isSet("itemLore")) {
					for(String key : lang.getStringList("itemLore")) {
						lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
					}
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
				
				this.setItem((i-sizeOfPreviousPages)-1, item);
			}
			player.openInventory(this.getInventory());
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

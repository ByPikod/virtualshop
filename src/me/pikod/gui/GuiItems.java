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
import me.pikod.main.data.DataItem;
import me.pikod.main.data.DataPage;
import me.pikod.main.data.DataSaver;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiItems extends GuiManager {
	public GuiItems(Player player, int page, int category) {
		super(player);
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
		
		DataCategory c = DataSaver.getCategory(category);
		DataPage p = c.getPages().get(page);
		int sizeOfPreviousPages = ((page-1) * 45);
		if(p != null) {
			for(DataItem item : p.getItems()) {
				this.setItem((item.getSlot()-sizeOfPreviousPages)-1, item.getUserView());
			}
		}	
		player.openInventory(gui);
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

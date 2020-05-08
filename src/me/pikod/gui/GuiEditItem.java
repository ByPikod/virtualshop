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

public class GuiEditItem extends GuiManager {
	public GuiEditItem(Player player, int item, int category) {
		this.create(1, GuiLanguage.edit_item, "editItem");
		
		ItemStack geri = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = geri.getItemMeta();
		meta.setDisplayName(Color.chat("&4Back"));
		List<String> lore = new ArrayList<String>();
		lore.add(Color.chat("&cClick for back"));
		lore.add(Color.chat("&cto previous menu!"));
		meta.setLore(lore);
		geri.setItemMeta(meta);
		this.setItem(0, geri);
		
		ItemStack itemS = new ItemStack(Material.matchMaterial(VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("item")), VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getInt("count"));
		meta = itemS.getItemMeta();
		itemS.setDurability((short) VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getInt("subId"));
		meta.setDisplayName("");
		lore.clear();
		lore.add(Color.chat("&aID: &2"+item));
		lore.add(Color.chat("&aCategory ID: &2"+category));
		meta.setLore(lore);
		
		ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item);
		
		if(shop.isSet("displayName")) {
			meta.setDisplayName(shop.getString("displayName"));
		}
		
		if(shop.isSet("lore")) {
			for(String key : shop.getConfigurationSection("lore").getKeys(false)) {
				lore.add(shop.getString("lore."+key));
			}
			lore.add(Color.chat("&r"));
			meta.setLore(lore);
		}
		
		itemS.setItemMeta(meta);
		
		if(shop.isSet("ench")) {
			for(String ench : shop.getConfigurationSection("ench").getKeys(false)) {
				Enchantment enchObj = Enchantment.getByName(ench);
				itemS.addUnsafeEnchantment(enchObj, shop.getInt("ench."+ench));
			}
		}
		
		this.setItem(4, itemS);
		
		ItemStack kaldir = new ItemStack(Material.LAVA_BUCKET);
		meta = kaldir.getItemMeta();
		meta.setDisplayName(Color.chat("&4Delete"));
		lore.clear();
		lore.add(Color.chat("&cClick for delete"));
		lore.add(Color.chat("&citem in category!"));
		meta.setLore(lore);
		kaldir.setItemMeta(meta);
		this.setItem(8, kaldir);
		
		ItemStack alis = new ItemStack(Material.GOLD_INGOT);
		meta = alis.getItemMeta();
		
		String buyCost = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("buyCost");
		String sellCost = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("sellCost");
		
		String strBuy = VirtualShop.numberToStr(Long.parseLong(buyCost));
		String strSell = VirtualShop.numberToStr(Long.parseLong(sellCost));
		
		if(Long.parseLong(buyCost) == 0) {
			meta.setDisplayName(Color.chat("&aSet Buy Cost &2(Disabled)"));
		}else {
			meta.setDisplayName(Color.chat("&aSet Buy Cost &2("+strBuy+")"));
		}
		lore.clear();
		lore.add(Color.chat("&2Click for"));
		lore.add(Color.chat("&2set buy cost!"));
		meta.setLore(lore);
		alis.setItemMeta(meta);
		this.setItem(3, alis);
		
		ItemStack satis = new ItemStack(Material.IRON_INGOT);
		meta = satis.getItemMeta();
		if(Long.parseLong(sellCost) == 0) {
			meta.setDisplayName(Color.chat("&aSet Sell Cost &2(Disabled)"));
		}else {
			meta.setDisplayName(Color.chat("&aSet Sell Cost &2("+strSell+")"));
		}
		
		lore.clear();
		lore.add(Color.chat("&2Click for"));
		lore.add(Color.chat("&2set sell cost!"));
		meta.setLore(lore);
		satis.setItemMeta(meta);
		this.setItem(5, satis);
		
		player.openInventory(this.getInventory());
	}
}

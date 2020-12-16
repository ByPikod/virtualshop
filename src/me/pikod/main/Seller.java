package me.pikod.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Seller {
	private static List<SellableItem> sellableItems = new ArrayList<SellableItem>();
	
	public static void loadClass() {
		YamlConfiguration shops = VirtualShop.shops;
		sellableItems.clear();
		if(shops.isSet("categories")) {
			ConfigurationSection categories = shops.getConfigurationSection("categories");
			for(String key : categories.getKeys(false)) {
				ConfigurationSection category = categories.getConfigurationSection(key);
				if(category.isSet("shop")) {
					ConfigurationSection shop = category.getConfigurationSection("shop");
					for(String key1 : shop.getKeys(false)) {
						ConfigurationSection item = shop.getConfigurationSection(key1);
						String strCost = item.getString("sellCost");
						double longCost = Double.valueOf(strCost);
						if(longCost != 0) {
							ItemStack i = new ItemStack(Material.matchMaterial(item.getString("item")));
							i.setAmount(item.getInt("count"));
							i.setDurability((short) item.getInt("subId"));
							if(item.isSet("lore")) {
								i.getItemMeta().setLore(item.getStringList("lore"));
							}
							if(item.isSet("ench")) {
								for(String key2 : item.getConfigurationSection("ench").getKeys(false)) {
									i.addUnsafeEnchantment(Enchantment.getByName(key2), item.getInt("ench."+key2));
								}
							}
							if(item.isSet("displayName")) {
								i.getItemMeta().setDisplayName(item.getString("displayName"));
							}
							new SellableItem(i, longCost);
						}
					}
				} else continue;
			}
		}
	}
	
	public static boolean equalsItem(ItemStack item, ItemStack i) {
		if(!i.getType().equals(item.getType())) return false;
		if(i.getDurability() != item.getDurability()) return false;
		if(!i.getData().equals(item.getData())) return false;
		if(item.hasItemMeta()) {
			if(item.getItemMeta().hasDisplayName()) {
				if(!item.getItemMeta().getDisplayName().equals(i.getItemMeta().getDisplayName())) return false;
			}else if(i.getItemMeta().hasDisplayName()) return false;
			if(item.getItemMeta().hasLore()) {
				if(!item.getItemMeta().getLore().equals(i.getItemMeta().getLore())) return false;
			}else if(i.getItemMeta().hasLore()) return false;
			if(item.getItemMeta().hasEnchants()) {
				if(!item.getItemMeta().getEnchants().equals(i.getEnchantments())) return false;
			} else if(i.getItemMeta().hasEnchants()) return false;
		}else if(i.hasItemMeta()) return false;
		return true;
	}
	
	public static List<SellableItem> getSellableItems(){
		return sellableItems;
	}
	
	public static class SellableItem{
		private final ItemStack item;
		private final double piece;
		private final int id;
		
		public SellableItem(ItemStack item, double cost) {
			this.item = item;
			this.piece = cost / item.getAmount();
			id = sellableItems.size();
			sellableItems.add(this);
		}
		
		public ItemStack getItem() {
			return item;
		}
		
		public double getPiece(){
			return piece;
		}
		
		public int getId() {
			return id;
		}
		
		public boolean itemEquals(ItemStack i) {
			if(!i.getType().equals(item.getType())) return false;
			if(i.getDurability() != item.getDurability()) return false;
			if(!i.getData().equals(item.getData())) return false;
			if(item.hasItemMeta()) {
				if(item.getItemMeta().hasDisplayName()) {
					if(!item.getItemMeta().getDisplayName().equals(i.getItemMeta().getDisplayName())) return false;
				}else if(i.getItemMeta().hasDisplayName()) return false;
				if(item.getItemMeta().hasLore()) {
					if(!item.getItemMeta().getLore().equals(i.getItemMeta().getLore())) return false;
				}else if(i.getItemMeta().hasLore()) return false;
				if(item.getItemMeta().hasEnchants()) {
					if(!item.getItemMeta().getEnchants().equals(i.getEnchantments())) return false;
				} else if(i.getItemMeta().hasEnchants()) return false;
			}else if(i.hasItemMeta()) return false;
			return true;
		}
	}
}

package me.pikod.main.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.pikod.main.VirtualShop;

public class DataSaver {
	private static List<DataCategory> categories = new ArrayList<DataCategory>();
	
	public static void LoadData() {
		categories.clear();
		YamlConfiguration data = VirtualShop.shops;
		
		// Fill Categories
		for(String str : data.getConfigurationSection("categories").getKeys(false)) {
			ConfigurationSection category = data.getConfigurationSection("categories."+str);
			categories.add(new DataCategory(category));
		}
	}
	
	protected static void removeCategoryFromList(DataCategory c) {
		categories.remove(c);
	}
	
	public static DataCategory getCategory(int slot) {
		for(DataCategory c : categories) {
			if(c.getSlot() == slot) return c;
		}
		return null;
	}
	
	public static void createCategory(Material material, String displayName, short durability, int slot) {
		VirtualShop.shops.createSection("categories."+slot);
		VirtualShop.shops.set("categories."+slot+".item", material.toString());
		VirtualShop.shops.set("categories."+slot+".displayName", displayName);
		VirtualShop.shops.set("categories."+slot+".subID", durability);
		VirtualShop.saveShops();
		categories.add(new DataCategory(VirtualShop.shops.getConfigurationSection("categories."+slot)));
	}
	
	public static void createItem(DataCategory category, ItemStack item, int slot, double buyCost, double sellCost) {
		ConfigurationSection conf = category.getSection().createSection("shop."+slot);
		conf.set("item", item.getType().toString());
		conf.set("subId", item.getDurability());
		conf.set("count", item.getAmount());
		conf.set("buyCost", String.valueOf(buyCost));
		conf.set("sellCost", String.valueOf("0"));
		if(item.getItemMeta().hasLore()) {
			conf.set("lore", item.getItemMeta().getLore());
		}
		for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
			conf.set("ench."+entry.getKey().getName(), item.getEnchantmentLevel(entry.getKey()));
		}
		if(item.getItemMeta().hasDisplayName()) conf.set("displayName", item.getItemMeta().getDisplayName());
		VirtualShop.saveShops();
		new DataItem(category, conf);
	}
	
	public static List<DataCategory> getCategories(){
		return categories;
	}
}

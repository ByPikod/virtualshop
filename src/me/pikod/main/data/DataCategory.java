package me.pikod.main.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.VirtualShop;
import me.pikod.utils.f;

public class DataCategory {
	private final ConfigurationSection section;
	private final int slot;
	
	private final ItemStack adminItem;
	private final ItemStack userItem;
	
	private String permission = null;
	
	protected final HashMap<Integer, DataPage> pages = new HashMap<Integer, DataPage>();
	
	@SuppressWarnings("deprecation")
	protected DataCategory(ConfigurationSection section) {
		this.section = section;
		this.slot = Integer.parseInt(section.getName());
		if(getSection().isSet("permission"))
			permission = getSection().getString("permission");
		ItemMeta meta;
		
		// Set Item for Admin View
		ItemStack adminItem = new ItemStack(Material.matchMaterial(section.getString("item")));
		ItemStack userItem = new ItemStack(Material.matchMaterial(section.getString("item")));
		meta = adminItem.getItemMeta();
		if(section.isSet("displayName")) meta.setDisplayName(f.c(section.getString("displayName")));
		if(section.isSet("subID")) adminItem.setDurability((short) section.getInt("subID"));
		if(section.isSet("meta")) adminItem.getData().setData((byte) section.getInt("meta"));
		List<String> lore = new ArrayList<String>();
		if(VirtualShop.lang.isSet("panelCategoryLore")) {
			for(String str : VirtualShop.lang.getStringList("panelCategoryLore")) {
				lore.add(f.c(str));
			}
		}
		meta.setLore(lore);
		adminItem.setItemMeta(meta);
		meta = userItem.getItemMeta();
		if(section.isSet("displayName")) meta.setDisplayName(f.c(section.getString("displayName")));
		if(section.isSet("subId")) userItem.setDurability((short) section.getInt("subId"));
		userItem.setItemMeta(meta);
		
		this.adminItem = adminItem;
		this.userItem = userItem;
		
		// Fill Pages
		if(getSection().isSet("shop")) {
			for(String str : getSection().getConfigurationSection("shop").getKeys(false)) {
				ConfigurationSection item = getSection().getConfigurationSection("shop."+str);
				new DataItem(this, item);
			}
		}
		
	}
	
	public ConfigurationSection getSection() {
		return section;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public ItemStack getAdminView() {
		return adminItem;
	}
	
	public ItemStack getUserView() {
		return userItem;
	}
	
	public void deleteCategory() {
		DataSaver.removeCategoryFromList(this);
		VirtualShop.shops.set("categories."+slot, null);
		VirtualShop.saveShops();
	}
	
	public void setDecoration(boolean isDecoration) {
		getSection().set("decoration", isDecoration);
		VirtualShop.saveShops();
	}
	
	public void setPermission(String permission) {
		getSection().set("permission", permission);
		this.permission = permission;
		VirtualShop.saveShops();
	}
	
	public boolean isPermission() {
		if(permission == null) {
			return false;
		}
		return true;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public HashMap<Integer, DataPage> getPages() {
		return pages;
	}
	
	public DataPage getPage(int page) {
		return pages.get(page);
	}
	
	public DataItem getItem(int slot) {
		int pageid = ((slot-1)/45)+1;
		return pages.get(pageid).getItemFromId(slot);
	}
}

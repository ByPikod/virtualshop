package me.pikod.main.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.Seller;
import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class DataItem {
	private final ConfigurationSection section;
	
	private final DataCategory category;
	private final DataPage page;
	private final int slot;
	private final int pageid;
	private int stackSize = 64;
	
	double buyCost;
	double sellCost;
	
	private ItemStack adminView;
	private ItemStack userView;
	
	protected DataItem(DataCategory category, ConfigurationSection section) {
		this.section = section;
		this.category = category;
		slot = Integer.parseInt(section.getName());
		
		pageid = ((Integer.parseInt(section.getName())-1)/45)+1;
		DataPage p = category.pages.get(pageid);
		if(p == null) {
			DataPage page = new DataPage(category, pageid);
			this.page = page;
			page.items.add(this);
		}else {
			this.page = p;
			p.items.add(this);
		}
		stackSize = getSection().getInt("stackSize");
		buyCost = Double.parseDouble(section.getString("buyCost"));
		sellCost = Double.parseDouble(section.getString("sellCost"));
		
		/** ADMIN VIEW **/
		adminView = new ItemStack(Material.matchMaterial(section.getString("item")));
		ItemMeta meta;
		meta = adminView.getItemMeta();
		
		if(section.isSet("displayName")) meta.setDisplayName(f.c(section.getString("displayName")));
		if(section.isSet("subId")) adminView.setDurability((short) section.getInt("subId"));
		if(section.isSet("count")) adminView.setAmount(section.getInt("count"));
		List<String> lore = new ArrayList<String>();
		if(section.isSet("lore")) {
			for(String key : section.getStringList("lore")) {
				lore.add(f.c(key));
			}
			lore.add(Color.chat("&r"));
		}
		String strBuy, strSell;
		if(buyCost == 0) strBuy = f.autoLang("panelBuyClosed"); else strBuy = VirtualShop.numberToStr(buyCost);
		if(sellCost == 0) strSell = f.autoLang("panelSellClosed"); else strSell = VirtualShop.numberToStr(sellCost);
		if(VirtualShop.lang.isSet("panelItemLore")) {
			for(String key : VirtualShop.lang.getStringList("panelItemLore")) {
				lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
			}
		}
		meta.setLore(lore);
		adminView.setItemMeta(meta);
		/** USER VIEW **/
		userView = new ItemStack(Material.matchMaterial(section.getString("item")));
		meta = userView.getItemMeta();
		
		if(section.isSet("displayName")) meta.setDisplayName(f.c(section.getString("displayName")));
		if(section.isSet("subId")) userView.setDurability((short) section.getInt("subId"));
		if(section.isSet("count")) userView.setAmount(section.getInt("count"));
		lore.clear();
		if(section.isSet("lore")) {
			for(String key : section.getStringList("lore")) {
				lore.add(f.c(key));
			}
			lore.add(Color.chat("&r"));
		}
		if(buyCost == 0) strBuy = f.autoLang("panelBuyClosed"); else strBuy = VirtualShop.numberToStr(buyCost);
		if(sellCost == 0) strSell = f.autoLang("panelSellClosed"); else strSell = VirtualShop.numberToStr(sellCost);
		if(VirtualShop.lang.isSet("itemLore")) {
			for(String key : VirtualShop.lang.getStringList("itemLore")) {
				lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
			}
		}
		meta.setLore(lore);
		userView.setItemMeta(meta);
		if(section.isSet("ench")) {
			for(String key : section.getConfigurationSection("ench").getKeys(true)) {
				adminView.addUnsafeEnchantment(Enchantment.getByName(key), section.getInt("ench."+key));
				userView.addUnsafeEnchantment(Enchantment.getByName(key), section.getInt("ench."+key));
			}
		}
	}
	
	public ConfigurationSection getSection() {
		return section;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int getPageId() {
		return pageid;
	}
	
	public DataPage getPage() {
		return page;
	}
	
	public DataCategory getCategory() {
		return category;
	}
	
	public void deleteItem() {
		getPage().items.remove(this);
		getCategory().getSection().set("shop."+section.getName(), null);
		VirtualShop.saveShops();
	}
	
	public ItemStack getAdminView() {
		return adminView;
	}
	
	public ItemStack getUserView() {
		return userView;
	}
	
	public void setStackSize(int newStackSize) {
		if(newStackSize != 1 && newStackSize != 16 && newStackSize != 32 && newStackSize != 64) {
			getSection().set("stackSize", 64);
		}else {
			stackSize = newStackSize;
			getSection().set("stackSize", newStackSize);
		}
		VirtualShop.saveShops();
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public double getBuyCost() {
		return buyCost;
	}
	
	public double getSellCost() {
		return sellCost;
	}
	
	public void refreshViews() {
		ItemMeta meta = adminView.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(section.isSet("lore")) {
			for(String key : section.getStringList("lore")) {
				lore.add(f.c(key));
			}
			lore.add(Color.chat("&r"));
		}
		String strBuy, strSell;
		if(buyCost == 0) strBuy = f.autoLang("panelBuyClosed"); else strBuy = VirtualShop.numberToStr(buyCost);
		if(sellCost == 0) strSell = f.autoLang("panelSellClosed"); else strSell = VirtualShop.numberToStr(sellCost);
		if(VirtualShop.lang.isSet("panelItemLore")) {
			for(String key : VirtualShop.lang.getStringList("panelItemLore")) {
				lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
			}
		}
		meta.setLore(lore);
		adminView.setItemMeta(meta);
		meta = userView.getItemMeta();
		lore.clear();
		if(section.isSet("lore")) {
			for(String key : section.getStringList("lore")) {
				lore.add(f.c(key));
			}
			lore.add(Color.chat("&r"));
		}
		if(VirtualShop.lang.isSet("itemLore")) {
			for(String key : VirtualShop.lang.getStringList("itemLore")) {
				lore.add(f.c(key.replace("{BUY_COST}", strBuy).replace("{SELL_COST}", strSell)));
			}
		}
		meta.setLore(lore);
		userView.setItemMeta(meta);
	}
	
	public void setBuyCost(double cost) {
		buyCost = cost;
		if(cost == 0) getSection().set("buyCost", "0"); else getSection().set("buyCost", String.valueOf(cost));
		VirtualShop.saveShops();
		refreshViews();
	}
	
	public void setSellCost(double cost) {
		sellCost = cost;
		if(cost == 0) getSection().set("sellCost", "0"); else getSection().set("sellCost", String.valueOf(cost));
		VirtualShop.saveShops();
		Seller.loadClass();
		refreshViews();
	}
}

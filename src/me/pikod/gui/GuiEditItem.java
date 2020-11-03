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

public class GuiEditItem extends GuiManager {
	public GuiEditItem(Player player, int item, int category) {
		super(player);
		this.create(1, f.autoLang("editItemTitle"));

		YamlConfiguration lang = VirtualShop.lang;
		
		ItemStack geri = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = geri.getItemMeta();
		meta.setDisplayName(f.autoLang("editItem_Back"));
		List<String> lore = new ArrayList<String>();
		lore.clear();
		if(lang.isSet("editItem_BackLore")) {
			for(String key : lang.getStringList("editItem_BackLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		geri.setItemMeta(meta);
		this.setItem(0, geri);
		
		ItemStack itemS = new ItemStack(Material.matchMaterial(VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("item")), VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getInt("count"));
		meta = itemS.getItemMeta();
		itemS.setDurability((short) VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getInt("subId"));
		meta.setDisplayName("");
		lore.clear();
		ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item);
		
		if(shop.isSet("lore")) {
			for(String key : shop.getStringList("lore")) {
				lore.add(shop.getString("lore."+key));
			}
			lore.add(Color.chat("&r"));
			meta.setLore(lore);
		}
		
		lore.add(Color.chat("&aID: &2"+item));
		lore.add(Color.chat("&aCategory ID: &2"+category));
		meta.setLore(lore);
		
		if(shop.isSet("displayName")) {
			meta.setDisplayName(shop.getString("displayName"));
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
		meta.setDisplayName(f.autoLang("editItem_Delete"));
		lore.clear();
		if(lang.isSet("editItem_DeleteLore")) {
			for(String key : lang.getStringList("editItem_DeleteLore")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		kaldir.setItemMeta(meta);
		this.setItem(8, kaldir);
		
		ItemStack alis = new ItemStack(Material.GOLD_INGOT);
		meta = alis.getItemMeta();
		
		String buyCost = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("buyCost");
		String sellCost = VirtualShop.shops.getConfigurationSection("categories."+category+".shop."+item).getString("sellCost");
		
		String strBuy = VirtualShop.numberToStr(Double.valueOf(buyCost));
		String strSell = VirtualShop.numberToStr(Double.valueOf(sellCost));
		
		String str = strBuy;
		if(str.equals("0")) {
			str = f.autoLang("editItem_BuyDisabled");
		}
		meta.setDisplayName(f.autoLang("editItem_BuyCost").replace("{COST}", str));
		lore.clear();
		if(lang.isSet("editItem_SetBuyCost")) {
			for(String key : lang.getStringList("editItem_SetBuyCost")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		alis.setItemMeta(meta);
		this.setItem(3, alis);
		
		ItemStack satis = new ItemStack(Material.IRON_INGOT);
		meta = satis.getItemMeta();
		str = strSell;
		if(str.equals("0")) {
			str = f.autoLang("editItem_SellDisabled");
		}
		meta.setDisplayName(f.autoLang("editItem_SellCost").replace("{COST}", str));
		
		lore.clear();
		if(lang.isSet("editItem_SetSellCost")) {
			for(String key : lang.getStringList("editItem_SetSellCost")) {
				lore.add(f.c(key));
			}
		}
		meta.setLore(lore);
		satis.setItemMeta(meta);
		this.setItem(5, satis);
		int stackS = 64;
		if(shop.isSet("stackSize")) stackS = shop.getInt("stackSize");
		ItemStack stackSize = new ItemStack(Material.SHEARS);
		stackSize.setAmount(stackS);
		lore.clear();
		meta = stackSize.getItemMeta();
		meta.setDisplayName(f.autoLang("editItem_stackSize"));
		if(lang.isSet("editItem_StackSizeLore")) {
			for(String key : lang.getStringList("editItem_StackSizeLore")) {
				lore.add(f.c(key).replace("{STACK_SIZE}", String.valueOf(stackS)));
			}
		}
		meta.setLore(lore);
		
		stackSize.setItemMeta(meta);
		setItem(7, stackSize);
		
		player.openInventory(gui);
	}
}

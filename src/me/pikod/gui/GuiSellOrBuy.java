package me.pikod.gui;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.functions.Color;
import me.pikod.main.VirtualShop;

public class GuiSellOrBuy extends GuiManager {
	public GuiSellOrBuy(Player player, boolean isBuy, ItemStack item, long cost, int categoryId, int categoryPage, ConfigurationSection itemSec) {
		if(isBuy) {
			this.create(3, GuiLanguage.getStr("buy_item_title"), "buyItem");
		}else {
			this.create(3, GuiLanguage.getStr("sell_item_title"), "sellItem");
		}
		ItemStack anaItem = item.clone();
		int itemAdet = item.getAmount();
		long itemCost = cost / itemAdet;
		item.setAmount(1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(Color.chat(GuiLanguage.getStr("piecePrefix")+itemCost));
		lore.add(Color.chat(GuiLanguage.getStr("totalPrefix")+VirtualShop.numberToStr(itemCost)));
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.setItem(4, item);
		
		
		item = VirtualShop.getStainedGlassItem(5);
		item.setAmount(1);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat("&a+"));
		item.setItemMeta(meta);	
		this.setItem(5, item);
		
		item.setAmount(16);	
		this.setItem(6, item.clone());
		item.setAmount(32);
		this.setItem(7, item.clone());
		item.setAmount(64);
		this.setItem(8, item.clone());
		
		item = VirtualShop.getStainedGlassItem(14);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat("&c-"));
		item.setItemMeta(meta);	
		this.setItem(3, item);
		
		item.setAmount(16);
		this.setItem(2, item.clone());
		item.setAmount(32);
		this.setItem(1, item.clone());
		item.setAmount(64);
		this.setItem(0, item.clone());
		item = VirtualShop.getStainedGlassItem(15);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat("&r"));
		item.setItemMeta(meta);
		
		for(int i = 9; i < 18; i++) {
			this.setItem(i, item);
		}
		item = new ItemStack(Material.BARRIER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat(GuiLanguage.getStr("back")));
		meta.setLore(Arrays.asList(Color.chat(GuiLanguage.getStr("backToPrefix")+categoryId+":"+categoryPage)));
		item.setItemMeta(meta);
		this.setItem(26, item);
		
		item = new ItemStack(Material.EMERALD_BLOCK, 1);
		meta = item.getItemMeta();
		
		meta.setDisplayName(Color.chat(GuiLanguage.getStr("accept")));
		item.setItemMeta(meta);
		this.setItem(25, item);
		
		anaItem.setAmount(1);
		meta = anaItem.getItemMeta();
		if(itemSec.isSet("lore")) {
			List<String> lore1 = new ArrayList<String>();
			for(String key : itemSec.getConfigurationSection("lore").getKeys(false)) {
				lore1.add(Color.chat(itemSec.getString("lore."+key)));
			}
			meta.setLore(lore1);
		}else {
			meta.setLore(null);
		}
		
		anaItem.setItemMeta(meta);
		this.setItem(18, anaItem);
		
		player.openInventory(this.getInventory());
	}
	
	public static void Add(Inventory inv, int howMany) {
		int x = 0;
		int totalAmount = 0;
		for(int i = 18; i <= 24; i++) {
			if(inv.getItem(i) != null) x++; else break;
		}
		x -= 1;
		x += 18;
		if(inv.getItem(x).getAmount()+howMany > 64) {
			ItemStack item = inv.getItem(x);
			int itemAmount = item.getAmount();
			item.setAmount(64);
			ItemStack newStack = item.clone();
			newStack.setAmount((itemAmount+howMany)-64);
			if(x+1 != 25) inv.setItem(x+1, newStack);
		}else {
			ItemStack item = inv.getItem(x);
			item.setAmount(item.getAmount()+howMany);
		}
		for(int i = 18; i <= 24; i++) {
			if(inv.getItem(i) != null) totalAmount += inv.getItem(i).getAmount(); else break;
		}
		ItemStack getMoney = inv.getItem(4);
		List<String> lore = getMoney.getItemMeta().getLore();
		String pieceCost = lore.get(0);
		int piece = Integer.parseInt(pieceCost.substring(GuiLanguage.getStr("piecePrefix").length()));
		long total = piece*totalAmount;
		lore.set(1, Color.chat(GuiLanguage.getStr("totalPrefix")+VirtualShop.numberToStr(total)));
		ItemStack loreChange = inv.getItem(4);
		ItemMeta meta = loreChange.getItemMeta();
		meta.setLore(lore);
		loreChange.setItemMeta(meta);
	}
	public static void Remove(Inventory inv, int howMany) {
		int x = 0;
		int totalAmount = 0;
		for(int i = 18; i <= 24; i++) {
			if(inv.getItem(i) != null) x++; else break;
		}
		x -= 1;
		x += 18;
		if(inv.getItem(x).getAmount()-howMany < 1) {
			if(x == 18) {
				inv.getItem(x).setAmount(1);
			}else {
				int itemAmount = inv.getItem(x).getAmount();
				inv.setItem(x, null);
				ItemStack newStack = inv.getItem(x-1);
				newStack.setAmount((itemAmount-howMany)+64);
			}
		}else {
			ItemStack item = inv.getItem(x);
			item.setAmount(item.getAmount()-howMany);
		}
		for(int i = 18; i <= 24; i++) {
			if(inv.getItem(i) != null) totalAmount += inv.getItem(i).getAmount(); else break;
		}
		ItemStack getMoney = inv.getItem(4);
		List<String> lore = getMoney.getItemMeta().getLore();
		String pieceCost = lore.get(0);
		int piece = Integer.parseInt(pieceCost.substring(GuiLanguage.getStr("piecePrefix").length()));
		long total = piece*totalAmount;
		lore.set(1, Color.chat(GuiLanguage.getStr("totalPrefix")+VirtualShop.numberToStr(total)));
		ItemStack loreChange = inv.getItem(4);
		ItemMeta meta = loreChange.getItemMeta();
		meta.setLore(lore);
		loreChange.setItemMeta(meta);
	}
}

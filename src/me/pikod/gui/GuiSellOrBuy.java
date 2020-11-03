package me.pikod.gui;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.ChatEvent;
import me.pikod.main.ChatInterface;
import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiSellOrBuy extends GuiManager {
	
	private final int categoryPage;
	private final int categoryId;
	private final Player player;
	
	public GuiSellOrBuy(Player player, boolean isBuy, ItemStack item, double cost, int categoryId, int categoryPage, ConfigurationSection itemSec) {
		super(player);
		this.categoryId = categoryId;
		this.categoryPage = categoryPage;
		this.player = player;

		if(f.autoConfig("shoppingType").toUpperCase().equals("CHAT")) {
			player.closeInventory();
			ItemStack verilecekItem = item.clone();
			if(verilecekItem.hasItemMeta()) {
				ItemMeta newMeta = verilecekItem.getItemMeta();
				if(itemSec.isSet("lore")) {
					List<String> lore = new ArrayList<String>();
					for(String value : itemSec.getConfigurationSection("lore").getKeys(false)) {
						String key = itemSec.getString("lore."+value);
						lore.add(key);
					}
					newMeta.setLore(lore);
				}else {
					newMeta.setLore(null);
				}
				verilecekItem.setItemMeta(newMeta);
			}
			if(isBuy) {
				new ChatEvent(player, new ChatInterface() {
					
					
					@Override
					public boolean chatAction(AsyncPlayerChatEvent event) {
						if(event.getMessage().toLowerCase().equals(f.autoLang("cancelKeyword").toLowerCase())) {
							back();
							return true;
						}
						Player player = event.getPlayer();
						Inventory playerInventory = player.getInventory();
						double piece = (cost/verilecekItem.getAmount());
						int totalAmount;
						double money = VirtualShop.vault.getBalance(event.getPlayer());
						try {
							totalAmount = Integer.parseInt(event.getMessage());
						}catch(Exception e) {
							event.getPlayer().sendMessage(f.autoLang("incorrectNumber"));
							back();
							return true;
						}
						int amountBackup = totalAmount;
						if(!(money >= totalAmount*piece)) {
							event.getPlayer().sendMessage(f.autoLang("notEnoughMoney"));
							back();
							return true;
						}
						VirtualShop.vault.withdrawPlayer(event.getPlayer(), totalAmount*piece);
						int stackSize = 64;
						if(itemSec.isSet("stackSize")) stackSize = itemSec.getInt("stackSize");
						while(totalAmount > stackSize) {
							totalAmount -= stackSize;
							verilecekItem.setAmount(stackSize);
							playerInventory.addItem(verilecekItem);
						}
						if(totalAmount != 0) {
							verilecekItem.setAmount(totalAmount);
							playerInventory.addItem(verilecekItem);
						}
						String msg = f.autoLang("successBuy");
						msg = msg.replace("{ITEM}", verilecekItem.getType().toString());
						msg = msg.replace("{MONEY}", String.valueOf(VirtualShop.numberToStr((long) (totalAmount*piece))));
						msg = msg.replace("{STACK}", String.valueOf(amountBackup));
						event.getPlayer().sendMessage(msg);
						back();
						return true;
					}
				});
				player.sendMessage(f.c(f.autoLang("writeBuyAmount")));
			}else {
				new ChatEvent(player, new ChatInterface() {
					
					
					@Override
					public boolean chatAction(AsyncPlayerChatEvent event) {
						if(event.getMessage().toLowerCase().equals(f.autoLang("cancelKeyword").toLowerCase())) {
							back();
							return true;
						}
						ItemMeta correctMeta = verilecekItem.getItemMeta();
						Player player = event.getPlayer();
						Inventory playerInventory = player.getInventory();
						int selled = 0;
						double piece = (cost/verilecekItem.getAmount());
						int totalAmount;
						try {
							totalAmount = Integer.parseInt(event.getMessage());
						}catch(Exception e) {
							if(event.getMessage().toUpperCase().equals("ALL")) {
								totalAmount = 1000000;
							}else {
								event.getPlayer().sendMessage(f.autoLang("incorrectNumber"));
								back();
								return true;
							}
						}
						for(int i = 0; i < playerInventory.getSize(); i++) {
							if(playerInventory.getItem(i) == null) continue;
							ItemStack itemTry = playerInventory.getItem(i);
							ItemMeta tryMeta = itemTry.getItemMeta();
							boolean uyusuyor = true;
							
							if(!itemTry.getType().equals(verilecekItem.getType())) continue;
							if(itemTry.getDurability() != verilecekItem.getDurability()) continue;
							if(!itemTry.getData().equals(verilecekItem.getData())) continue;
							if(correctMeta.hasDisplayName()) {
								if(tryMeta.hasDisplayName())
									uyusuyor = (correctMeta.getDisplayName().equals(tryMeta.getDisplayName())); else uyusuyor = false;
							} else if(tryMeta.hasDisplayName()) continue;
							if(!uyusuyor) continue;
							if(correctMeta.hasLore()) {
								if(tryMeta.hasLore()){
									int x = 0;
									for(String str : correctMeta.getLore()) {
										String str2 = tryMeta.getLore().get(x);
										if(!str.equals(str2)) {
											continue;
										}
										x++;
									}
								}else continue;
							} else if(tryMeta.hasLore()) continue;
								
							if(correctMeta.hasEnchants()) {
								if(tryMeta.hasEnchants()) 
									uyusuyor = (tryMeta.getEnchants().equals(correctMeta.getEnchants())); else uyusuyor = false;
							}else if(tryMeta.hasEnchants()) continue;
							if(!uyusuyor) continue;
							if(totalAmount-itemTry.getAmount() >= 0) {
								totalAmount -= itemTry.getAmount();
								selled += itemTry.getAmount();
								playerInventory.setItem(i, null);
							}else {
								int var1 = itemTry.getAmount()-totalAmount;
								itemTry.setAmount(var1);
								selled += totalAmount;
								totalAmount = 0;
							}
						}
						if(selled == 0) {
							event.getPlayer().sendMessage(f.autoLang("notEnoughItem"));
						}else {
							double m = selled*piece;
							VirtualShop.vault.depositPlayer(event.getPlayer(), m);
							String msg = f.autoLang("successSell");
							msg = msg.replace("{ITEM}", verilecekItem.getType().toString());
							msg = msg.replace("{MONEY}", VirtualShop.numberToStr((long) m));
							msg = msg.replace("{STACK}", String.valueOf(selled));
							event.getPlayer().sendMessage(msg);
						}
						back();
						return true;
					}
				});
				player.sendMessage(f.c(f.autoLang("writeSellAmount")));
			}
			return;
		}
		
		if(isBuy) {
			this.create(3, f.autoLang("buyItemTitle"));
		}else {
			this.create(3, f.autoLang("sellItemTitle"));
		}
		ItemStack anaItem = item.clone();
		int itemAdet = item.getAmount();
		double itemCost = cost / itemAdet;
		item.setAmount(1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(itemSec.isSet("lore")) {
			for(String keys : itemSec.getConfigurationSection("lore").getKeys(false)) {
				lore.add(f.c(itemSec.getString("lore."+keys)));
			}
			lore.add(f.c("&r"));
		}
		lore.add(f.autoLang("piecePrefix")+VirtualShop.numberToStr(itemCost));
		lore.add(f.autoLang("totalPrefix")+VirtualShop.numberToStr(itemCost));
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.setItem(4, item);
		
		int stackSize = 64;
		if(itemSec.isSet("stackSize")) stackSize = itemSec.getInt("stackSize");
		
		item = VirtualShop.getStainedGlassItem("LIME", 5);
		item.setAmount(1);
		meta = item.getItemMeta();
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(" ");
		meta.setDisplayName(Color.chat("&a+"));
		item.setItemMeta(meta);	
		this.setItem(5, item);
		Material demir_parmaklik = Material.matchMaterial("IRON_FENCE");
		if(demir_parmaklik == null) {
			demir_parmaklik = Material.matchMaterial("IRON_BARS");
		}
		if(stackSize < 16) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(16);	
		this.setItem(6, item.clone());
		if(stackSize < 32) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(32);
		this.setItem(7, item.clone());
		if(stackSize < 64) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(64);
		this.setItem(8, item.clone());
		
		
		item = VirtualShop.getStainedGlassItem("RED", 14);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat("&c-"));
		item.setItemMeta(meta);	
		this.setItem(3, item);
		
		if(stackSize < 16) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(16);
		this.setItem(2, item.clone());
		if(stackSize < 32) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(32);
		this.setItem(1, item.clone());
		if(stackSize < 64) {
			item.setAmount(1);
			item.setItemMeta(m);
			item.setType(demir_parmaklik);
		}else
		item.setAmount(64);
		this.setItem(0, item.clone());
		item = VirtualShop.getStainedGlassItem("BLACK", 15);
		meta = item.getItemMeta();
		meta.setDisplayName(Color.chat("&r"));
		item.setItemMeta(meta);
		
		for(int i = 9; i < 18; i++) {
			this.setItem(i, item);
		}
		item = new ItemStack(Material.BARRIER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(f.autoLang("shopMenuBack"));
		setData("category", categoryId);
		setData("page", categoryPage);
		item.setItemMeta(meta);
		this.setItem(26, item);
		
		item = new ItemStack(Material.EMERALD_BLOCK, 1);
		meta = item.getItemMeta();
		
		meta.setDisplayName(f.autoLang("accept"));
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
		
		player.openInventory(gui);
	}
	
	public static int getStackSize(Inventory inv) {
		Material demir_parmaklik = Material.matchMaterial("IRON_FENCE");
		if(demir_parmaklik == null) {
			demir_parmaklik = Material.matchMaterial("IRON_BARS");
		}
		if(inv.getItem(2).getType() == demir_parmaklik) {
			return 1;
		}
		if(inv.getItem(1).getType() == demir_parmaklik) {
			return 16;
		}
		if(inv.getItem(0).getType() == demir_parmaklik) {
			return 32;
		}
		return 64;
	}
	
	public static void Add(Inventory inv, int howMany) {
		int stackSize = getStackSize(inv);
		int x = 0;
		int totalAmount = 0;
		for(int i = 18; i <= 24; i++) {
			if(inv.getItem(i) != null) x++; else break;
		}
		x += 17;
		if(inv.getItem(x).getAmount()+howMany > stackSize) {
			ItemStack item = inv.getItem(x);
			int itemAmount = item.getAmount();
			item.setAmount(stackSize);
			ItemStack newStack = item.clone();
			newStack.setAmount((itemAmount+howMany)-stackSize);
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
		int h = lore.size();
		String pieceCost = lore.get(h-2);
		double piece = Double.parseDouble(pieceCost.substring(f.autoLang("piecePrefix").length()).replace(",", ""));
		double total = piece*totalAmount;
		lore.set(h-1, f.autoLang("totalPrefix")+VirtualShop.numberToStr(total));
		ItemStack loreChange = inv.getItem(4);
		ItemMeta meta = loreChange.getItemMeta();
		meta.setLore(lore);
		loreChange.setItemMeta(meta);
	}
	public static void Remove(Inventory inv, int howMany) {
		int stackSize = getStackSize(inv);
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
				newStack.setAmount((itemAmount-howMany)+stackSize);
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
		int h = lore.size();
		String pieceCost = lore.get(h-2);
		double piece = Double.parseDouble(pieceCost.substring(f.autoLang("piecePrefix").length()).replace(",", ""));
		double total = piece*totalAmount;
		lore.set(h-1, f.autoLang("totalPrefix")+VirtualShop.numberToStr(total));
		ItemStack loreChange = inv.getItem(4);
		ItemMeta meta = loreChange.getItemMeta();
		meta.setLore(lore);
		loreChange.setItemMeta(meta);
	}
	
	private void back() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(VirtualShop.plugin, new Runnable() {
			
			@Override
			public void run() {
				new GuiItems(player, categoryPage, categoryId);
			}
		});
	}
}

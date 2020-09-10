package me.pikod.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.gui.GuiAddItem;
import me.pikod.gui.GuiCategories;
import me.pikod.gui.GuiCategoriesAdmin;
import me.pikod.gui.GuiEditCategory;
import me.pikod.gui.GuiEditItem;
import me.pikod.gui.GuiItems;
import me.pikod.gui.GuiLanguage;
import me.pikod.gui.GuiSellOrBuy;
import me.pikod.main.ChatEvent;
import me.pikod.main.ChatInterface;
import me.pikod.main.VirtualShop;
import me.pikod.utils.Color;
import me.pikod.utils.f;
import net.milkbowl.vault.economy.Economy;

public class ActionHandler implements Listener {
	
	public static List<ChatEvent> chatListenerList = new ArrayList<ChatEvent>();
	
	
	public ActionHandler(VirtualShop plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(event.getPlayer().hasPermission("virtualshop.manage")) {
			if(VirtualShop.uc.hasUpdate) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(VirtualShop.plugin, new Runnable() {
					
					@Override
					public void run() {
						event.getPlayer().sendMessage(f.autoLang("newUpdates"));	
					}
				}, 10L);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void handler(InventoryClickEvent event) {
		if(VirtualShop.debugMode) {
			event.getWhoClicked().sendMessage(event.getAction().toString()+":"+event.getRawSlot()+" - Normal slot: "+event.getSlot()+" Slot type: "+event.getSlotType().toString());
		}
		Player player = (Player) event.getWhoClicked();
		if(event.getView().getTitle().equals(Color.chat(f.autoLang("adminMainMenuTitle")))) { //ANA ADMIN MENUSU
			event.setCancelled(true);
			if(event.getSlot() == 4) {
				player.closeInventory();
				new GuiCategoriesAdmin(player);
			}else if(event.getSlot() == 0) {
				player.closeInventory();
				VirtualShop.reloadShops(true);
				player.sendMessage(Color.chat(f.autoLang("successReloaded")));
			}else if(event.getSlot() == 8) {
				player.sendMessage(Color.chat("&9"+GuiLanguage.bar));
				player.sendMessage(Color.chat("&9"));
				player.sendMessage(Color.chat("&9-> &2Created by, &aPikod"));
				player.sendMessage(Color.chat("&9-> &2Discord: &ahttps://discord.gg/dkbNmFJ"));
				player.sendMessage(Color.chat("&9-> &2Youtube: &ahttps://www.youtube.com/c/pikod"));
				player.sendMessage(Color.chat("&9"));
				player.sendMessage(Color.chat("&9"+GuiLanguage.bar));
				player.closeInventory();
			}
		}else if(event.getView().getTitle().equals(Color.chat(f.autoLang("editCategoriesTitle")))) { //KATEGORI DUZENLEME MENUSU
			if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			
			if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME) {
				event.setCancelled(true);
				ItemStack item = event.getCursor();
				item.setAmount(1);
				
				ItemMeta meta;
				meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if(meta.hasDisplayName())
				meta.setDisplayName(f.c(meta.getDisplayName()));
				if(VirtualShop.lang.isSet("panelCategoryLore")) {
					for(String str : VirtualShop.lang.getStringList("panelCategoryLore")) {
						lore.add(f.c(str));
					}
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				event.getInventory().setItem(event.getRawSlot(), item);
				event.setCursor(null);
				
				
				
				VirtualShop.shops.createSection("categories."+event.getSlot());
				VirtualShop.shops.set("categories."+event.getSlot()+".item", item.getType().toString());
				VirtualShop.shops.set("categories."+event.getSlot()+".displayName", meta.getDisplayName());
				VirtualShop.shops.set("categories."+event.getSlot()+".subID", item.getDurability());
				VirtualShop.shops.set("categories."+event.getSlot()+".meta", item.getData().getData());
				VirtualShop.saveShops();
				VirtualShop.reloadShops();
				
			}else if(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.PICKUP_HALF || event.getAction() == InventoryAction.PICKUP_ONE || event.getAction() == InventoryAction.PICKUP_SOME){
				if(event.getSlotType() == SlotType.QUICKBAR) return;
				event.setCancelled(true);
				if(event.isRightClick()) {
					new GuiEditCategory(player, (short) event.getSlot());
				}else if(event.isLeftClick()) {
					new GuiAddItem(player, 1,event.getSlot());
				}
				
			}else {
				event.setCancelled(true);
			}
		}else if(event.getView().getTitle().equals(Color.chat(f.autoLang("editCategoryTitle")))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			String slot;
			ItemStack item = event.getInventory().getItem(3);
			slot = item.getItemMeta().getLore().get(0);
			
			
			event.setCancelled(true);
			if(!event.isLeftClick() && !event.isRightClick()) return;
			if(event.getSlot() == 0) {
				new GuiCategoriesAdmin(player);
			}
			
			if(event.getSlot() == 8) {
				VirtualShop.shops.set("categories."+slot, null);
				VirtualShop.saveShops();
				VirtualShop.reloadShops();
				player.sendMessage(f.autoLang("successRemoved"));
				new GuiCategoriesAdmin(player);
			}
			
			if(event.getSlot() == 7) {
				if(event.getCurrentItem().getItemMeta().getLore().get(0).equals(f.autoLang("decorationEnabled"))) {
					VirtualShop.shops.set("categories."+slot+".decoration", false);
					item = VirtualShop.getStainedGlassItem("RED", 14);
					ItemMeta meta =  item.getItemMeta();
					meta.setDisplayName(f.autoLang("editCategory_IsDecoration"));
					meta.setLore(Arrays.asList(f.autoLang("decorationDisabled")));
					item.setItemMeta(meta);
					event.getInventory().setItem(event.getSlot(), item);
				}else {
					VirtualShop.shops.set("categories."+slot+".decoration", true);
					item = VirtualShop.getStainedGlassItem("LIME",5);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(f.autoLang("isDecoration"));
					meta.setLore(Arrays.asList(f.autoLang("decorationEnabled")));
					item.setItemMeta(meta);
					event.getInventory().setItem(event.getSlot(), item);
				}
				VirtualShop.saveShops();
			}
		}else if(event.getView().getTitle().equals(Color.chat(f.autoLang("addItemToCategoryTitle")))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			event.setCancelled(true);
			
			int page, sizeOfPreviousPages, location;
			page = event.getInventory().getItem(49).getAmount();
			sizeOfPreviousPages = ((page-1) * 45); 
			location = event.getSlot()+sizeOfPreviousPages+1;
			
			if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME) {
				if(event.getSlot() > 44) {
					player.sendMessage(f.autoLang("cannotBeAddItem"));
					return;
				}
				ItemStack item = event.getCursor();
				
				if(VirtualShop.shops.getConfigurationSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location) == null) {
					VirtualShop.shops.createSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location);
				}
				
				ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location);
				
				shop.set("item", item.getType().toString());
				shop.set("subId", item.getDurability());
				shop.set("count", item.getAmount());
				shop.set("buyCost", "1000");
				shop.set("sellCost", "0");
				
				int i = 0;
				
				if(item.getItemMeta().hasLore()) {
					Iterator<String> iteratorLore = item.getItemMeta().getLore().iterator();
					while(iteratorLore.hasNext()) {
						i++;
						shop.set("lore."+i, iteratorLore.next());
					}
				}
				
				for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
					shop.set("ench."+entry.getKey().getName(), item.getEnchantmentLevel(entry.getKey()));
				}
				
				if(item.getItemMeta().hasDisplayName()) {
					shop.set("displayName", item.getItemMeta().getDisplayName());
				}
				
				VirtualShop.saveShops();
				
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if(item.getItemMeta().getLore() != null) {
					Iterator<String> loreIterator = item.getItemMeta().getLore().iterator();
					while(loreIterator.hasNext()) {
						String next = loreIterator.next();
						lore.add(next);
					}
					lore.add(Color.chat("&r"));
				}
				
				
				if(VirtualShop.lang.isSet("panelItemLore")) {
					for(String key : VirtualShop.lang.getStringList("panelItemLore")) {
						lore.add(f.c(key.replace("{BUY_COST}", "1000").replace("{SELL_COST}", f.autoLang("sellClosed"))));
					}
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				event.getInventory().setItem(event.getSlot(), item);
				
				event.setCursor(null);
				
				VirtualShop.reloadShops();
			}else if(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.PICKUP_HALF || event.getAction() == InventoryAction.PICKUP_ONE || event.getAction() == InventoryAction.PICKUP_SOME) {
				if(event.getSlotType() == SlotType.QUICKBAR) return;
				if(event.isRightClick() || event.isLeftClick() || event.isShiftClick()) {
					if(event.getSlot() == 45) {
						if(event.getInventory().getItem(49).getAmount() == 1) {
							new GuiCategoriesAdmin(player);
						}else {
							new GuiAddItem(player, page-1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
						}
					}else if(event.getSlot() == 53) {
						new GuiAddItem(player, page+1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}else {
						if(event.getSlot() > 44) return;
						new GuiEditItem(player, location, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}
				}
				
			}
		}else if(event.getView().getTitle().equals(f.autoLang("editItemTitle"))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			event.setCancelled(true);
			if(event.isRightClick() || event.isLeftClick() || event.isShiftClick()) {
				int lanes = event.getInventory().getItem(4).getItemMeta().getLore().size();
				int lane1 = lanes-1;
				int lane2 = lanes-2;
				int baslangic = event.getInventory().getItem(4).getItemMeta().getLore().get(lane1).indexOf(Color.chat("&2"));
				int baslangic2 = event.getInventory().getItem(4).getItemMeta().getLore().get(lane2).indexOf(Color.chat("&2"));
				int categoryId = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(lane1).substring(baslangic+2));
				int itemId = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(lane2).substring(baslangic2+2));

				int page = 1;
				page = ((itemId-1)/45)+1;
				
				if(event.getSlot() == 0) {
					new GuiAddItem(player, page ,(short) categoryId);
				}
				if(event.getSlot() == 8) {
					VirtualShop.shops.set("categories."+categoryId+".shop."+itemId, null);
					VirtualShop.saveShops();
					VirtualShop.reloadShops();
					player.sendMessage(f.autoLang("successRemoved"));
					new GuiAddItem(player, page, categoryId);
				}
				if(event.getSlot() == 7) {
					ItemStack current = event.getCurrentItem();
					List<String> lore = new ArrayList<String>();
					if(current.getAmount() == 1) {
						current.setAmount(16);
						ItemMeta meta = current.getItemMeta();
						if(VirtualShop.lang.isSet("editItem_StackSizeLore")) {
							for(String key : VirtualShop.lang.getStringList("editItem_StackSizeLore")) {
								lore.add(f.c(key).replace("{STACK_SIZE}", "16"));
							}
						}
						meta.setLore(lore);
						current.setItemMeta(meta);
						VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".stackSize", 16);
					}else
					if(current.getAmount() == 16) {
						current.setAmount(32);
						ItemMeta meta = current.getItemMeta();
						if(VirtualShop.lang.isSet("editItem_StackSizeLore")) {
							for(String key : VirtualShop.lang.getStringList("editItem_StackSizeLore")) {
								lore.add(f.c(key).replace("{STACK_SIZE}", "32"));
							}
						}
						meta.setLore(lore);
						current.setItemMeta(meta);
						VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".stackSize", 32);
					}else
					if(current.getAmount() == 32) {
						current.setAmount(64);
						ItemMeta meta = current.getItemMeta();
						if(VirtualShop.lang.isSet("editItem_StackSizeLore")) {
							for(String key : VirtualShop.lang.getStringList("editItem_StackSizeLore")) {
								lore.add(f.c(key).replace("{STACK_SIZE}", "64"));
							}
						}
						meta.setLore(lore);
						current.setItemMeta(meta);
						VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".stackSize", 64);
					}else
					if(current.getAmount() == 64) {
						current.setAmount(1);
						ItemMeta meta = current.getItemMeta();
						if(VirtualShop.lang.isSet("editItem_StackSizeLore")) {
							for(String key : VirtualShop.lang.getStringList("editItem_StackSizeLore")) {
								lore.add(f.c(key).replace("{STACK_SIZE}", "1"));
							}
						}
						meta.setLore(lore);
						current.setItemMeta(meta);
						VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".stackSize", 1);
					}
					VirtualShop.saveShops();
					VirtualShop.reloadShops();
				}
				
				if(event.getSlot() == 3) {
					player.closeInventory();
					player.sendMessage(f.autoLang("writeNewBuyCost"));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Double.valueOf(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().toLowerCase().equals(f.autoLang("cancelKeywordAdmin").toLowerCase())) {
									event.getPlayer().sendMessage(f.autoLang("cancelled"));
									new GuiEditItem(player, itemId, categoryId);
									return true;
								}
								event.getPlayer().sendMessage(f.autoLang("invalidNumber"));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".buyCost", ""+Double.valueOf(event.getMessage()));
							VirtualShop.saveShops();
							VirtualShop.reloadShops();
							event.getPlayer().sendMessage(f.autoLang("assigned"));
							Bukkit.getScheduler().scheduleSyncDelayedTask(VirtualShop.plugin, new Runnable() {
								
								@Override
								public void run() {
									new GuiEditItem(player, itemId, categoryId);
								}
							});
							return true;
						}
					});
				}
				
				if(event.getSlot() == 5) {
					player.closeInventory();
					player.sendMessage(f.autoLang("writeNewSellCost"));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Double.valueOf(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().equals(f.autoLang("cancelKeywordAdmin").toLowerCase())) {
									event.getPlayer().sendMessage(f.autoLang("cancelled"));
									new GuiEditItem(player, itemId, categoryId);
									return true;
								}
								event.getPlayer().sendMessage(f.autoLang("invalidNumber"));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".sellCost", ""+Double.valueOf(event.getMessage()));
							VirtualShop.saveShops();
							VirtualShop.reloadShops(true);
							event.getPlayer().sendMessage(f.autoLang("assigned"));
							Bukkit.getScheduler().scheduleSyncDelayedTask(VirtualShop.plugin, new Runnable() {
								
								@Override
								public void run() {
									new GuiEditItem(player, itemId, categoryId);
								}
							});
							return true;
						}
					});
				}
			}
		}else if(event.getView().getTitle().equals(f.autoLang("categoriesTitle"))) { /** CATEGORIES **/
			event.setCancelled(true);
			if(event.getSlotType() == SlotType.QUICKBAR) return; //TOP Envanterden deðilse action döndür
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			if(VirtualShop.shops.getConfigurationSection("categories."+event.getSlot()) != null && VirtualShop.shops.getBoolean("categories."+event.getSlot()+".decoration") != true) { //Eðer basýlan item'e ait veri varsa.
				new GuiItems(player, 1, event.getSlot());
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("sellItemTitle"))) || event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("buyItemTitle")))) {
			event.setCancelled(true);
			if(event.getAction() == InventoryAction.NOTHING) return;
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			
			if(event.getSlot() == 26) {
				int page, category;
				ItemStack item = event.getInventory().getItem(26);
				String info = item.getItemMeta().getLore().get(0);
				info = info.substring(GuiLanguage.getStr("backToPrefix").length());
				String[] splitted = info.split("-");
				category = Integer.parseInt(splitted[0]);
				page = Integer.parseInt(splitted[1]);
				new GuiItems(player, page, category); //Ana sayfaya döndür.
				return;
			}
			if(event.getSlot() == 25) {
				int totalAmount = 0;
				for(int i = 18; i <= 24; i++) {
					if(event.getInventory().getItem(i) != null) totalAmount += event.getInventory().getItem(i).getAmount(); else break;
				}
				ItemStack getMoney = event.getInventory().getItem(4);
				List<String> lore = getMoney.getItemMeta().getLore();
				int h = lore.size();
				String pieceCost = lore.get(h-2);
				int piece = Integer.parseInt(pieceCost.substring(f.autoLang("piecePrefix").length()).replace(",", ""));
				long total = piece*totalAmount;
				int amountBackup = totalAmount;
				
				Inventory playerInventory = event.getWhoClicked().getInventory();
				Inventory guiInventory = event.getInventory();
				ItemStack verilecekItem = guiInventory.getItem(18); //Satýlacak veya Alýnacak
				Economy eco = VirtualShop.vault;
				OfflinePlayer p = Bukkit.getOfflinePlayer(event.getWhoClicked().getUniqueId());
				HumanEntity he = event.getWhoClicked();
				double money = eco.getBalance(p);
				
				if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("sellItemTitle")))) {
					int selled = 0;
					for(int i = 0; i < playerInventory.getSize(); i++) {
						if(totalAmount == 0) continue;
						ItemStack itemTry = playerInventory.getItem(i);
						if(itemTry == null) continue;
						ItemMeta correctMeta, tryMeta;
						correctMeta = verilecekItem.getItemMeta();
						tryMeta = itemTry.getItemMeta();
						
						boolean uyusuyor = true;
						
						if(itemTry.getType() != verilecekItem.getType()) continue;
						if(itemTry.getDurability() != verilecekItem.getDurability()) continue;
						if(!itemTry.getData().equals(verilecekItem.getData())) continue;
						if(correctMeta.hasDisplayName()) {
							if(tryMeta.hasDisplayName())
								uyusuyor = (correctMeta.getDisplayName().equals(tryMeta.getDisplayName())); else uyusuyor = false;
						}else if(tryMeta.hasDisplayName()) uyusuyor = false;
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
						}else if(tryMeta.hasLore()) continue;
							
						if(correctMeta.hasEnchants()) {
							if(tryMeta.hasEnchants()) 
								uyusuyor = (tryMeta.getEnchants().equals(correctMeta.getEnchants())); else uyusuyor = false;
						}else if(tryMeta.hasLore()) continue;
						
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
						he.sendMessage(Color.chat(GuiLanguage.getStr("notEnoughItem")));
					}else {
						long m = selled*piece;
						eco.depositPlayer(p, m);
						String msg = Color.chat(GuiLanguage.getStr("successSell"));
						msg = msg.replace("{ITEM}", verilecekItem.getType().toString());
						msg = msg.replace("{MONEY}", VirtualShop.numberToStr(m));
						msg = msg.replace("{STACK}", String.valueOf(selled));
						he.sendMessage(msg);
					}
				}else {
					if(!(money >= total)) {
						he.sendMessage(Color.chat(GuiLanguage.getStr("notEnoughMoney")));
						return;
					}
					eco.withdrawPlayer(p, total);

					int stackSize = GuiSellOrBuy.getStackSize(event.getInventory());
					while(totalAmount > stackSize) {
						totalAmount -= stackSize;
						ItemStack ver = verilecekItem.clone();
						ver.setAmount(stackSize);
						playerInventory.addItem(ver);
					}
					if(totalAmount != 0) {
						ItemStack ver = verilecekItem.clone();
						ver.setAmount(totalAmount);
						playerInventory.addItem(ver);
					}
					String msg = Color.chat(GuiLanguage.getStr("successBuy"));
					msg = msg.replace("{ITEM}", verilecekItem.getType().toString());
					msg = msg.replace("{MONEY}", String.valueOf(VirtualShop.numberToStr(total)));
					msg = msg.replace("{STACK}", String.valueOf(amountBackup));
					he.sendMessage(msg);
				}
				
				return;
			}
			
			if(event.getSlot() == 3) {
				GuiSellOrBuy.Remove(event.getInventory(), 1);
			}
			if(event.getSlot() == 2) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Remove(event.getInventory(), 16);
			}
			if(event.getSlot() == 1) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Remove(event.getInventory(), 32);
			}
			if(event.getSlot() == 0) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Remove(event.getInventory(), 64);
			}
			
			if(event.getSlot() == 5) {
				GuiSellOrBuy.Add(event.getInventory(), 1);
			}
			if(event.getSlot() == 6) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Add(event.getInventory(), 16);
			}
			if(event.getSlot() == 7) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Add(event.getInventory(), 32);
			}
			if(event.getSlot() == 8) {
				if(event.getCurrentItem().getType() != Material.IRON_FENCE)
				GuiSellOrBuy.Add(event.getInventory(), 64);
			}
		}else if(event.getView().getTitle().equals(f.autoLang("itemsTitle"))) { /** IN CATEGORY **/
			event.setCancelled(true);
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			/* BAKILAN KATEGORI'NIN ÖZELLIKLERINI ÇEKELIM */
			
			int page, sizeOfPreviousPages, location;
			page = event.getInventory().getItem(49).getAmount();
			sizeOfPreviousPages = ((page-1) * 45); 
			location = event.getSlot()+sizeOfPreviousPages+1;
			
			int categoryId = 0;
			String categoryIdStr = "";
			Inventory envanter = event.getInventory();
			
			List<String> categoryIdLore = envanter.getItem(49).getItemMeta().getLore(); //Categorinin Açýklamasýnýndan gelen ID deðeri
			categoryIdStr = categoryIdLore.get(0).substring(categoryIdLore.get(0).indexOf(Color.chat("&2"))+Color.chat("&2").length());
			categoryId = Integer.parseInt(categoryIdStr);
			
			page = event.getInventory().getItem(49).getAmount(); //49. slottaki page elementinden kaçýncý sayfa olduðunu çekiyoruz.
			
			/* SPECIAL BUTTONS */
			
			if(event.getSlot() == 45) { //ÖNCEKI SAYFA
				if(event.getInventory().getItem(49).getAmount() == 1) { //Sayfa 1 ise
					new GuiCategories(player); //Ana sayfaya döndür.
					return;
				}
				new GuiItems(player, page-1, categoryId); //Eðer buraya gelmiþse bir önceki menüye at.
				return;
			}
			if(event.getSlot() == 53) { //SONRAKI SAYFA
				if(page == GuiItems.getMaxPage(categoryId)) return;
				new GuiItems(player, page+1, categoryId);
				return;
			}
			
			if(event.getSlot() > 45) return;
			ConfigurationSection category = VirtualShop.shops.getConfigurationSection("categories."+categoryId);
			ConfigurationSection shop = category.getConfigurationSection("shop."+location);
			page = event.getInventory().getItem(49).getAmount();
			if(shop != null) {

				double buyCost = Double.valueOf(shop.getString("buyCost"));
				double sellCost = Double.valueOf(shop.getString("sellCost"));
				if(event.isLeftClick()) {
					if(buyCost == 0) {
						player.sendMessage(Color.chat(GuiLanguage.getStr("closedBuy")));
						return;
					}
					new GuiSellOrBuy(player, true, event.getCurrentItem(), buyCost, categoryId, page, shop);
				}
				if(event.isRightClick()) {
					if(sellCost == 0) {
						player.sendMessage(Color.chat(GuiLanguage.getStr("closedSell")));
						return;
					}
					new GuiSellOrBuy(player, false, event.getCurrentItem(), sellCost, categoryId, page, shop);
				}
			}
		}
	}
	
	@EventHandler
	public void handler(AsyncPlayerChatEvent event) {
		List<ChatEvent> newListe = null;
		Player player = event.getPlayer();
		for(ChatEvent selected : chatListenerList) {
			if(selected.player.equals(player)) {
				event.setCancelled(true);
				if(selected.listener.chatAction(event)) {
					newListe = chatListenerList;
				}
			}
		}
		if(newListe != null) {
			newListe.removeIf(chat -> chat.player.equals(player));
		}
		
	}
}

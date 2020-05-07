package me.pikod.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.functions.Color;
import me.pikod.functions.f;
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
				event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &cVirtualShop finded new updates! Link for update: \n&7https://www.spigotmc.org/resources/74496\n"));
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
		
		if(event.getView().getTitle().equals(Color.chat(GuiLanguage.admin_menu))) { //ANA ADMIN MENUSU
			event.setCancelled(true);
			if(event.getSlot() == 4) {
				player.closeInventory();
				new GuiCategoriesAdmin(player);
			}else if(event.getSlot() == 0) {
				player.closeInventory();
				VirtualShop.reloadShops();
				player.sendMessage(Color.chat(GuiLanguage.prefix+" &aYML files reloaded!"));
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
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.categories_admin_menu))) { //KATEGORI DUZENLEME MENUSU
			if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			
			if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME) {
				ItemStack item = event.getCursor();
				String type = item.getType().toString();
				event.setCancelled(true);
				item.setAmount(1);
				
				ItemMeta meta;
				meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				meta.setDisplayName(Color.chat(meta.getDisplayName()));
				lore.add(Color.chat("&2Right Click:&a Delete/Edit category"));
				lore.add(Color.chat("&2Left Click:&a Add items to category "));
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				event.getInventory().setItem(event.getRawSlot(), item);
				event.setCursor(null);
				
				
				
				VirtualShop.shops.createSection("categories."+event.getSlot());
				VirtualShop.shops.set("categories."+event.getSlot()+".item", type);
				VirtualShop.shops.set("categories."+event.getSlot()+".displayName", meta.getDisplayName());
				VirtualShop.shops.set("categories."+event.getSlot()+".subID", item.getDurability());
				VirtualShop.shops.set("categories."+event.getSlot()+".meta", item.getData().getData());
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					
				}
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
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.edit_category))) {
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
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				VirtualShop.reloadShops();
				player.sendMessage(Color.chat(GuiLanguage.prefix+" &aSuccessfully removed!"));
				new GuiCategoriesAdmin(player);
			}
			
			if(event.getSlot() == 7) {
				if(event.getCurrentItem().getItemMeta().getLore().get(0).equals(Color.chat("&aEnabled"))) {
					VirtualShop.shops.set("categories."+slot+".decoration", false);
					item = VirtualShop.getStainedGlassItem(14);
					ItemMeta meta =  item.getItemMeta();
					meta.setDisplayName(Color.chat("&7Is Decoration"));
					meta.setLore(Arrays.asList(Color.chat("&cDisabled")));
					item.setItemMeta(meta);
					event.getInventory().setItem(event.getSlot(), item);
				}else {
					VirtualShop.shops.set("categories."+slot+".decoration", true);
					item = VirtualShop.getStainedGlassItem(5);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(Color.chat("&7Is Decoration"));
					meta.setLore(Arrays.asList(Color.chat("&aEnabled")));
					item.setItemMeta(meta);
					event.getInventory().setItem(event.getSlot(), item);
				}
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.add_item))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			event.setCancelled(true);
			
			if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME) {
				if(event.getSlot() > 44) {
					player.sendMessage(Color.chat(GuiLanguage.prefix+" &cCannot be add item to here!"));
					return;
				}
				ItemStack item = event.getCursor();

				int location = event.getSlot();
				int pageSize = 44;
				location = ((pageSize * event.getInventory().getItem(49).getAmount())+location)-44;
				
				if(VirtualShop.shops.getConfigurationSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location) == null) {
					VirtualShop.shops.createSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location);
				}
				
				ConfigurationSection shop = VirtualShop.shops.getConfigurationSection("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+location);
				
				shop.set("item", item.getType());
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
				
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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
				
				
				lore.add(Color.chat("&2Click for edit!"));
				lore.add(Color.chat("&aBuy: &e1000"));
				lore.add(Color.chat("&aSell: &cDisabled"));
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
							new GuiAddItem(player, event.getInventory().getItem(49).getAmount()-1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
						}
					}else if(event.getSlot() == 53) {
						new GuiAddItem(player, event.getInventory().getItem(49).getAmount()+1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}else {
						if(event.getSlot() > 44) return;
						if(!VirtualShop.shops.isSet("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+event.getSlot())) return;
						new GuiEditItem(player, event.getSlot()+(event.getInventory().getItem(49).getAmount()*44)-44, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}
				}
				
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.edit_item))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			event.setCancelled(true);
			if(event.isRightClick() || event.isLeftClick() || event.isShiftClick()) {
				int baslangic = event.getInventory().getItem(4).getItemMeta().getLore().get(1).indexOf(Color.chat("&2"));
				int baslangic2 = event.getInventory().getItem(4).getItemMeta().getLore().get(0).indexOf(Color.chat("&2"));
				int categoryId = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(1).substring(baslangic+2));
				int itemId = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getLore().get(0).substring(baslangic2+2));
				
				int page = 1;
				page = (itemId/44)+1;
				
				if(event.getSlot() == 0) {
					new GuiAddItem(player, page ,(short) categoryId);
				}
				if(event.getSlot() == 8) {
					VirtualShop.shops.set("categories."+categoryId+".shop."+itemId, null);
					try {
						VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					VirtualShop.reloadShops();
					player.sendMessage(Color.chat(GuiLanguage.prefix+" &aSuccessfully removed!"));
					new GuiAddItem(player, page, categoryId);
				}
				
				if(event.getSlot() == 3) {
					player.closeInventory();
					player.sendMessage(Color.chat(GuiLanguage.prefix+" &aPlease write a new buy cost! If you want to disable buying, write '0'. If you want to cancel write 'cancel'."));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Long.parseLong(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().equals("cancel")) {
									event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &cOperation canceled!"));
									new GuiEditItem(player, itemId, categoryId);
									ActionHandler.listedenKaldir(player);
									return true;
								}
								event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &cThe input you entered is not a valid number. Please try again or cancel operation."));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".buyCost", ""+Long.parseLong(event.getMessage()));
							try {
								VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							VirtualShop.reloadShops();
							event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &aSuccessfully assigned new variable!"));
							new GuiEditItem(player, itemId, categoryId);
							return true;
						}
					});
				}
				
				if(event.getSlot() == 5) {
					player.closeInventory();
					player.sendMessage(Color.chat(GuiLanguage.prefix+" &aPlease write a new sell cost! If you want to disable selling, write '0'. If you want to cancel write 'cancel'."));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Long.parseLong(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().equals("cancel")) {
									event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &cCanceled operation!"));
									new GuiEditItem(player, itemId, categoryId);
									ActionHandler.listedenKaldir(player);
									return true;
								}
								event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &cThe input you entered is not a valid number. Please try again or cancel operation."));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".sellCost", ""+Long.parseLong(event.getMessage()));
							try {
								VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							VirtualShop.reloadShops();
							event.getPlayer().sendMessage(Color.chat(GuiLanguage.prefix+" &aSuccessfully assigned new variable!"));
							new GuiEditItem(player, itemId, categoryId);
							return true;
						}
					});
				}
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("categories_title")))) { /** CATEGORIES **/
			event.setCancelled(true);
			if(event.getSlotType() == SlotType.QUICKBAR) return; //TOP Envanterden deðilse action döndür
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			if(VirtualShop.shops.getConfigurationSection("categories."+event.getSlot()) != null && VirtualShop.shops.getBoolean("categories."+event.getSlot()+".decoration") != true) { //Eðer basýlan item'e ait veri varsa.
				new GuiItems(player, 1, event.getSlot());
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("sell_item_title"))) || event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("buy_item_title")))) {
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
				String[] splitted = info.split(":");
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
				String pieceCost = lore.get(0);
				int piece = Integer.parseInt(pieceCost.substring(GuiLanguage.getStr("piecePrefix").length()));
				long total = piece*totalAmount;
				int amountBackup = totalAmount;
				
				Inventory playerInventory = event.getWhoClicked().getInventory();
				Inventory guiInventory = event.getInventory();
				ItemStack verilecekItem = guiInventory.getItem(18); //Satýlacak veya Alýnacak
				Economy eco = VirtualShop.vault;
				OfflinePlayer p = Bukkit.getOfflinePlayer(event.getWhoClicked().getUniqueId());
				HumanEntity he = event.getWhoClicked();
				double money = eco.getBalance(p);
				
				if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("sell_item_title")))) {
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
						if(correctMeta.hasDisplayName())
							if(tryMeta.hasDisplayName())
									uyusuyor = (correctMeta.getDisplayName().equals(tryMeta.getDisplayName())); else uyusuyor = false;
						if(!uyusuyor) continue;
						if(correctMeta.hasLore())
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
						if(correctMeta.hasEnchants())
							if(tryMeta.hasEnchants()) 
								uyusuyor = (tryMeta.getEnchants().equals(correctMeta.getEnchants())); else uyusuyor = false;
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
					
					while(totalAmount > 64) {
						totalAmount -= 64;
						verilecekItem.setAmount(64);
						playerInventory.addItem(verilecekItem);
					}
					if(totalAmount != 0) {
						verilecekItem.setAmount(totalAmount);
						playerInventory.addItem(verilecekItem);
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
				GuiSellOrBuy.Remove(event.getInventory(), 16);
			}
			if(event.getSlot() == 1) {
				GuiSellOrBuy.Remove(event.getInventory(), 32);
			}
			if(event.getSlot() == 0) {
				GuiSellOrBuy.Remove(event.getInventory(), 64);
			}
			
			if(event.getSlot() == 5) {
				GuiSellOrBuy.Add(event.getInventory(), 1);
			}
			if(event.getSlot() == 6) {
				GuiSellOrBuy.Add(event.getInventory(), 16);
			}
			if(event.getSlot() == 7) {
				GuiSellOrBuy.Add(event.getInventory(), 32);
			}
			if(event.getSlot() == 8) {
				GuiSellOrBuy.Add(event.getInventory(), 64);
			}
		}else if(event.getView().getTitle().equals(Color.chat(GuiLanguage.getStr("items_title")))) { /** IN CATEGORY **/
			event.setCancelled(true);
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			/* BAKILAN KATEGORI'NIN ÖZELLIKLERINI ÇEKELIM */
			
			int categoryId = 0;
			int page = 0;
			String categoryIdStr = "";
			Inventory envanter = event.getInventory();
			
			List<String> categoryIdLore = envanter.getItem(49).getItemMeta().getLore(); //Categorinin Açýklamasýnýndan gelen ID deðeri
			categoryIdStr = categoryIdLore.get(0).substring(categoryIdLore.get(0).indexOf(Color.chat("&2"))+Color.chat("&2").length());
			categoryId = Integer.parseInt(categoryIdStr);
			
			page = event.getInventory().getItem(49).getAmount(); //49. slottaki page elementinden kaçýncý sayfa olduðunu çekiyoruz.
			
			/* SPECIAL BUTTONS (+44) */
			
			if(event.getSlot() == 45) { //ÖNCEKI SAYFA
				if(event.getInventory().getItem(49).getAmount() == 1) { //Sayfa 1 ise
					new GuiCategories(player); //Ana sayfaya döndür.
					return;
				}
				new GuiItems(player, page-1, categoryId); //Eðer buraya gelmiþse bir önceki menüye at.
			}
			if(event.getSlot() == 53) { //SONRAKI SAYFA
				if(page == GuiItems.getMaxPage(categoryId)) return;
				new GuiItems(player, page+1, categoryId);
			}
			
			int clickedSlot = 0;
			if(event.getSlot() <= 44) {
				clickedSlot = ((page-1)*44)+event.getSlot();
			}
			if(event.getSlot() > 44) return;
			ConfigurationSection category = VirtualShop.shops.getConfigurationSection("categories."+categoryId);
			ConfigurationSection shop = category.getConfigurationSection("shop."+clickedSlot);
			page = event.getInventory().getItem(49).getAmount();
			if(shop != null) {

				long buyCost = Long.parseLong(shop.getString("buyCost"));
				long sellCost = Long.parseLong(shop.getString("sellCost"));
				
				if(event.isLeftClick()) {
					if(buyCost == 0) {
						player.sendMessage(Color.chat(GuiLanguage.getStr("closedBuy")));
						return;
					}
					new GuiSellOrBuy(player, true, event.getCurrentItem(), buyCost, categoryId, page, category.getConfigurationSection("shop."+event.getSlot()));
				}
				if(event.isRightClick()) {
					if(sellCost == 0) {
						player.sendMessage(Color.chat(GuiLanguage.getStr("closedSell")));
						return;
					}
					new GuiSellOrBuy(player, false, event.getCurrentItem(), sellCost, categoryId, page, category.getConfigurationSection("shop."+event.getSlot()));
				}
			}
		}
	}
	
	
	public static void listedenKaldir(Player player) {
		
	}
	
	@EventHandler
	public void handler(AsyncPlayerChatEvent event) {
		List<ChatEvent> newListe = null;
		Player player = event.getPlayer();
		for(ChatEvent selected : chatListenerList) {
			if(selected.player.equals(player)) {
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

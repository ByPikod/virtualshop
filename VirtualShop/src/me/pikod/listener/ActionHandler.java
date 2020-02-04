package me.pikod.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
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

import me.pikod.gui.ChatEvent;
import me.pikod.gui.ChatInterface;
import me.pikod.gui.guiAddItem;
import me.pikod.gui.guiCategories;
import me.pikod.gui.guiCategoriesAdmin;
import me.pikod.gui.guiEditCategory;
import me.pikod.gui.guiEditItem;
import me.pikod.gui.guiErisim;
import me.pikod.gui.guiItems;
import me.pikod.main.Color;
import me.pikod.main.VirtualShop;

public class ActionHandler implements Listener {
	
	public static List<ChatEvent> chatListenerList = new ArrayList<ChatEvent>();
	
	
	public ActionHandler(VirtualShop plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(event.getPlayer().hasPermission("virtualshop.manage")) {
			if(VirtualShop.uc.hasUpdate) {
				event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &9VirtualShop güncellemeler buldu!\nhttps://www.spigotmc.org/resources/74496 Adresinden yeni versiyonu indirebilirsiniz!"));
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
		
		if(event.getInventory().getName().equals(Color.chat(guiErisim.admin_menu))) { //ANA ADMIN MENUSU
			event.setCancelled(true);
			if(event.getSlot() == 4) {
				player.closeInventory();
				new guiCategoriesAdmin(player);
			}else if(event.getSlot() == 0) {
				player.closeInventory();
				VirtualShop.reloadShops();
				player.sendMessage(Color.chat(guiErisim.prefix+" &aYML Dosyalarý yenilendi!"));
			}else if(event.getSlot() == 8) {
				player.sendMessage(Color.chat("&9"+guiErisim.bar));
				player.sendMessage(Color.chat("&9"));
				player.sendMessage(Color.chat("&9-> &2Pikod &atarafýndan yapýldý!"));
				player.sendMessage(Color.chat("&9-> &2Discord: &ahttps://discord.gg/dkbNmFJ"));
				player.sendMessage(Color.chat("&9-> &2Youtube: &ahttps://www.youtube.com/c/pikod"));
				player.sendMessage(Color.chat("&9"));
				player.sendMessage(Color.chat("&9"+guiErisim.bar));
				player.closeInventory();
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.categories_admin_menu))) { //KATEGORI DUZENLEME MENUSU
			if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			
			if(event.getAction().toString().substring(0, 5).equals("PLACE")) {				
				if(!event.getCursor().getItemMeta().hasDisplayName()) {
					event.setCancelled(true);
					player.closeInventory();
					player.sendMessage(Color.chat(guiErisim.prefix+" &cEþyaya bir isim belirlemelisiniz!"));
					return;
				}
				event.setCancelled(true);
				ItemStack item = event.getCursor();
				
				item.setAmount(1);
				
				ItemMeta meta;
				meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				meta.setDisplayName(Color.chat(meta.getDisplayName()));
				lore.add(Color.chat("&2Sað týk:&a Kategori kaldýrma/düzenleme"));
				lore.add(Color.chat("&2Sol týk:&a Kategori içeriði düzenleme"));
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				event.getInventory().setItem(event.getRawSlot(), item);
				event.setCursor(null);
				
				
				
				VirtualShop.shops.createSection("categories."+event.getSlot());
				VirtualShop.shops.set("categories."+event.getSlot()+".item", item.getTypeId());
				VirtualShop.shops.set("categories."+event.getSlot()+".displayName", meta.getDisplayName());
				VirtualShop.shops.set("categories."+event.getSlot()+".subID", item.getDurability());
				
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					
				}
				VirtualShop.reloadShops();
				
			}else if(event.getAction().toString().substring(0, 6).equals("PICKUP")){
				if(event.getSlotType() == SlotType.QUICKBAR) return;
				event.setCancelled(true);
				if(event.isRightClick()) {
					new guiEditCategory(player, (short) event.getSlot());
				}else if(event.isLeftClick()) {
					new guiAddItem(player, 1,event.getSlot());
				}
				
			}else {
				event.setCancelled(true);
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.edit_category))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			String slot;
			ItemStack item = event.getInventory().getItem(3);
			slot = item.getItemMeta().getLore().get(0);
			
			
			event.setCancelled(true);
			if(!event.isLeftClick() && !event.isRightClick()) return;
			if(event.getSlot() == 0) {
				new guiCategoriesAdmin(player);
			}
			
			if(event.getSlot() == 8) {
				VirtualShop.shops.set("categories."+slot, null);
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				VirtualShop.reloadShops();
				player.sendMessage(Color.chat(guiErisim.prefix+" &aBaþarýyla kaldýrýldý!"));
				new guiCategoriesAdmin(player);
			}
			
			if(event.getSlot() == 7) {
				if(event.getCurrentItem().getItemMeta().getLore().get(0).equals(Color.chat("&aAçýk"))) {
					VirtualShop.shops.set("categories."+slot+".decoration", false);
					item = event.getCurrentItem();
					ItemMeta meta =  item.getItemMeta();
					meta.setLore(Arrays.asList(Color.chat("&cKapalý")));
					item.setDurability((short) 14);
					item.setItemMeta(meta);
					event.setCurrentItem(item);
				}else {
					VirtualShop.shops.set("categories."+slot+".decoration", true);
					item = event.getCurrentItem();
					ItemMeta meta =  item.getItemMeta();
					item.setDurability((short) 5);
					meta.setLore(Arrays.asList(Color.chat("&aAçýk")));
					item.setItemMeta(meta);
					event.setCurrentItem(item);
				}
				try {
					VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.add_item))) {
			if(event.getSlotType() == SlotType.QUICKBAR) return;
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			event.setCancelled(true);
			
			if(event.getAction().toString().substring(0, 5).equals("PLACE")) {
				if(event.getSlot() > 44) {
					player.sendMessage(Color.chat(guiErisim.prefix+" &cBu kýsma eþya eklenemez!"));
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
				
				shop.set("item", item.getTypeId());
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
					shop.set("ench."+entry.getKey().getId(), item.getEnchantmentLevel(entry.getKey()));
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
				Iterator<String> loreIterator = item.getItemMeta().getLore().iterator();
				while(loreIterator.hasNext()) {
					String next = loreIterator.next();
					lore.add(next);
				}
				lore.add(Color.chat("&r"));
				lore.add(Color.chat("&2Düzenlemek için týkla!"));
				lore.add(Color.chat("&aAlým: &e1000"));
				lore.add(Color.chat("&aSatým: &cKapalý"));
				meta.setLore(lore);
				item.setItemMeta(meta);
				event.getInventory().setItem(event.getSlot(), item);
				
				event.setCursor(null);
				
				
				
				
				VirtualShop.reloadShops();
				
				
			}else if(event.getAction().toString().substring(0, 6).equals("PICKUP")) {
				if(event.getSlotType() == SlotType.QUICKBAR) return;
				if(event.isRightClick() || event.isLeftClick() || event.isShiftClick()) {
					if(event.getSlot() == 45) {
						if(event.getInventory().getItem(49).getAmount() == 1) {
							new guiCategoriesAdmin(player);
						}else {
							new guiAddItem(player, event.getInventory().getItem(49).getAmount()-1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
						}
					}else if(event.getSlot() == 53) {
						new guiAddItem(player, event.getInventory().getItem(49).getAmount()+1, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}else {
						if(event.getSlot() > 44) return;
						if(!VirtualShop.shops.isSet("categories."+Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0))+".shop."+event.getSlot())) return;
						new guiEditItem(player, event.getSlot()+(event.getInventory().getItem(49).getAmount()*44)-44, Integer.parseInt(event.getInventory().getItem(48).getItemMeta().getLore().get(0)));
					}
				}
				
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.edit_item))) {
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
					new guiAddItem(player, page ,(short) categoryId);
				}
				if(event.getSlot() == 8) {
					VirtualShop.shops.set("categories."+categoryId+".shop."+itemId, null);
					try {
						VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					VirtualShop.reloadShops();
					player.sendMessage(Color.chat(guiErisim.prefix+" &aBaþarýyla kaldýrýldý!"));
					new guiAddItem(player, page, categoryId);
				}
				
				if(event.getSlot() == 3) {
					player.closeInventory();
					player.sendMessage(Color.chat(guiErisim.prefix+" &aLütfen yeni alýþ deðerini yazýnýz! Eðer almayý kapatmak istiyorsanýz '0' iptal etmek istiyorsanýz 'iptal' deðerini giriniz."));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Long.parseLong(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().equals("iptal")) {
									event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &cDeðer alma iþlemi iptal edildi!"));
									new guiEditItem(player, itemId, categoryId);
									ActionHandler.listedenKaldir(player);
									return true;
								}
								event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &cGirdiðiniz deðer geçerli bir sayý deðildir, lütfen tekrar deneyin."));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".buyCost", ""+Long.parseLong(event.getMessage()));
							try {
								VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							VirtualShop.reloadShops();
							event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &aBaþarýyla yeni deðer girildi!"));
							new guiEditItem(player, itemId, categoryId);
							return true;
						}
					});
				}
				
				if(event.getSlot() == 5) {
					player.closeInventory();
					player.sendMessage(Color.chat(guiErisim.prefix+" &aLütfen yeni satýþ deðerini yazýnýz! Eðer satýþý kapatmak istiyorsanýz '0' iptal etmek istiyorsanýz 'iptal' deðerini giriniz."));
					new ChatEvent(player, new ChatInterface() {
						
						@Override
						public boolean chatAction(AsyncPlayerChatEvent event) {
							event.setCancelled(true);
							try {
								Long.parseLong(event.getMessage());
							}catch(Exception e) {
								if(event.getMessage().equals("iptal")) {
									event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &cDeðer alma iþlemi iptal edildi!"));
									new guiEditItem(player, itemId, categoryId);
									ActionHandler.listedenKaldir(player);
									return true;
								}
								event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &cGirdiðiniz deðer geçerli bir sayý deðildir, lütfen tekrar deneyin."));
								return false;
							}
							VirtualShop.shops.set("categories."+categoryId+".shop."+itemId+".sellCost", ""+Long.parseLong(event.getMessage()));
							try {
								VirtualShop.shops.save(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							VirtualShop.reloadShops();
							event.getPlayer().sendMessage(Color.chat(guiErisim.prefix+" &aBaþarýyla yeni deðer girildi!"));
							new guiEditItem(player, itemId, categoryId);
							return true;
						}
					});
				}
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.getStr("categories_title")))) { /** CATEGORIES **/
			event.setCancelled(true);
			if(event.getSlotType() == SlotType.QUICKBAR) return; //TOP Envanterden deðilse action döndür
			if(event.getRawSlot()+1 > event.getInventory().getSize() || event.getRawSlot()+1 < 0) {
				return;
			}
			if(VirtualShop.shops.getConfigurationSection("categories."+event.getSlot()) != null && VirtualShop.shops.getBoolean("categories."+event.getSlot()+".decoration") != true) { //Eðer basýlan item'e ait veri varsa.
				new guiItems(player, 1, event.getSlot());
			}
		}else if(event.getInventory().getName().equals(Color.chat(guiErisim.getStr("items_title")))) { /** IN CATEGORY **/
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
					new guiCategories(player); //Ana sayfaya döndür.
					return;
				}
				new guiItems(player, page-1, categoryId); //Eðer buraya gelmiþse bir önceki menüye at.
			}
			if(event.getSlot() == 53) { //SONRAKI SAYFA
				if(page == guiItems.getMaxPage(categoryId)) return;
				new guiItems(player, page+1, categoryId);
			}
			
			int clickedSlot = 0;
			if(event.getSlot() <= 44) {
				clickedSlot = ((page-1)*44)+event.getSlot();
			}
			if(event.getSlot() > 44) return;
			ConfigurationSection category = VirtualShop.shops.getConfigurationSection("categories."+categoryId);
			ConfigurationSection shop = category.getConfigurationSection("shop."+clickedSlot);
			
			if(shop != null) {
				long buyCost = Long.parseLong(shop.getString("buyCost"));
				long sellCost = Long.parseLong(shop.getString("sellCost"));
				
				if(event.isLeftClick()) {
					if(buyCost == 0) {
						player.sendMessage(Color.chat(guiErisim.getStr("closedBuy")));
						return;
					}
					
					double money = VirtualShop.vault.getBalance(player);
					
					if(money >= (double) buyCost) {
						String message = guiErisim.getStr("successBuy");
						ItemStack item = new ItemStack(Material.getMaterial(shop.getInt("item")));
						item.setAmount(shop.getInt("count"));
						item.setDurability((short) shop.getInt("subId"));
						message = message.replace("{ITEM}", item.getData().getItemType().toString());
						message = message.replace("{STACK}", ""+shop.getInt("count"));
						message = message.replace("{MONEY}", VirtualShop.numberToStr(Long.parseLong(shop.getString("buyCost"))));
						player.sendMessage(Color.chat(message));
						
						ItemMeta meta = item.getItemMeta();
						if(shop.isSet("displayName")) {
							meta.setDisplayName(shop.getString("displayName"));
						}
						
						if(shop.isSet("lore")) {
							List<String> lore = new ArrayList<String>();
							for(String key : shop.getConfigurationSection("lore").getKeys(false)) {
								lore.add(shop.getString("lore."+key));
							}
							meta.setLore(lore);
						}
						item.setItemMeta(meta);
						
						if(shop.isSet("ench")) {
							for(String ench : shop.getConfigurationSection("ench").getKeys(true)) {
								Enchantment enchObj = Enchantment.getById(Integer.parseInt(ench));
								item.addUnsafeEnchantment(enchObj, shop.getInt("ench."+ench));
							}
						}
						
						player.getInventory().addItem(item);
						VirtualShop.vault.withdrawPlayer(player, (double) buyCost);
					}else {
						player.sendMessage(Color.chat(guiErisim.getStr("notEnoughMoney")));
					}
					
					
				}
				if(event.isRightClick()) {
					if(sellCost == 0) {
						player.sendMessage(Color.chat(guiErisim.getStr("closedSell")));
						return;
					}
					for(int i = 0; i < player.getInventory().getSize(); i++) {
						if(player.getInventory().getItem(i) == null) continue;
						if(player.getInventory().getItem(i).getTypeId() == shop.getInt("item")) {
							if(player.getInventory().getItem(i).getDurability() == shop.getInt("subId")) {
								if(player.getInventory().getItem(i).getAmount() >= shop.getInt("count")) {
									List<String> lore = new ArrayList<String>();
									
									if(shop.isSet("lore")) {
										for(String key : shop.getConfigurationSection("lore").getKeys(false)) {
											lore.add(shop.getString("lore."+key));
										}
										if(!player.getInventory().getItem(i).getItemMeta().hasLore()) continue;
										if(!player.getInventory().getItem(i).getItemMeta().getLore().equals(lore)) continue;
									}
									
									if(shop.isSet("ench")) {
										ItemStack enchItem = new ItemStack(Material.getMaterial(shop.getInt("item")));
										for(String ench : shop.getConfigurationSection("ench").getKeys(false)) {
											Enchantment enchObj = Enchantment.getById(Integer.parseInt(ench));
											enchItem.addUnsafeEnchantment(enchObj, shop.getInt("ench."+ench));
										}
										if(!player.getInventory().getItem(i).getEnchantments().equals(enchItem.getEnchantments())) continue;
									}
									
									if(shop.isSet("displayName")){
										if(!player.getInventory().getItem(i).getItemMeta().hasDisplayName()) continue;
										if(!player.getInventory().getItem(i).getItemMeta().getDisplayName().equals(shop.getString("displayName"))) continue;
									}
									
									ItemStack iItem = player.getInventory().getItem(i);
									iItem.setAmount(iItem.getAmount()-shop.getInt("count"));
									
									
									if(iItem.getAmount() == 0) {
										player.getInventory().setItem(i, null);
									}else {
										player.getInventory().setItem(i, iItem);
									}
									
									String message = guiErisim.getStr("successSell");
									ItemStack item = new ItemStack(Material.getMaterial(shop.getInt("item")), 1);
									item.setDurability((short) shop.getInt("subId"));
									message = message.replace("{ITEM}", item.getData().getItemType().toString());
									message = message.replace("{STACK}", ""+shop.getInt("count"));
									message = message.replace("{MONEY}", VirtualShop.numberToStr(Long.parseLong(shop.getString("sellCost"))));
									player.sendMessage(Color.chat(message));
									
									VirtualShop.vault.depositPlayer(player, sellCost);
									return;
								}
							}
						}
					}
					player.sendMessage(Color.chat(guiErisim.getStr("notEnoughtItem")));
					
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

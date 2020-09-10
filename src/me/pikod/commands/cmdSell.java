package me.pikod.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.pikod.main.Seller;
import me.pikod.main.Seller.SellableItem;
import me.pikod.main.VirtualShop;
import me.pikod.utils.f;

public class cmdSell implements CommandExecutor{

	public cmdSell(VirtualShop plugin) {
		plugin.getCommand("sell").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("virtualshop.sell")) {
			if(sender instanceof Player) {
				if(args.length == 0) {
					sender.sendMessage(f.autoLang("correctUsage").replace("{USAGE}", "/sell <all/hand>"));
					return true;
				}else {
					if(args[0].equals("all")) {
						long verilecekPara = 0;	
						HashMap<Seller.SellableItem, Double> selledList = new HashMap<Seller.SellableItem, Double>();
						Player player = (Player) sender;
						Inventory inv = player.getInventory();
						for(int i = 0; i < inv.getSize(); i++) {
							ItemStack item = inv.getItem(i);
							if(item != null) {
								for(SellableItem s : Seller.getSellableItems()) {
									if(s.itemEquals(item)) {
										double x = item.getAmount() * s.getPiece();
										verilecekPara += x;
										Double d = selledList.get(s);
										if(d==null) d=0d;
										selledList.put(s, d+x);
										
										inv.setItem(i, null);
									}
								}
							}
						}
						if(verilecekPara != 0) {
							for (Entry<SellableItem, Double> entry : selledList.entrySet()) {
								String send = f.autoLang("successSell");
								ItemStack i = entry.getKey().getItem();
								if(i.hasItemMeta()) {
									if(i.getItemMeta().hasDisplayName()) {
										send = send.replace("{ITEM}", i.getItemMeta().getDisplayName());
									}else {send = send.replace("{ITEM}", i.getType().toString());}
								}else send = send.replace("{ITEM}", i.getType().toString());
									

								send = send.replace("{MONEY}", String.valueOf(VirtualShop.numberToStr(entry.getValue())));
								send = send.replace("{STACK}", String.valueOf((long) (entry.getValue() / entry.getKey().getPiece())));
								sender.sendMessage(send);
							}
							VirtualShop.vault.depositPlayer(player, verilecekPara);
						}else player.sendMessage(f.autoLang("noSellableItem"));
					}else if(args[0].equals("hand")) {
						Player player = (Player) sender;
						ItemStack hand = player.getItemInHand();
						if(hand == null) {
							player.sendMessage(f.autoLang("emptyHand"));
							return true;
						}
						Inventory inv = player.getInventory();
						SellableItem s = null;
						for(SellableItem z : Seller.getSellableItems()) {
							if(z.itemEquals(hand)) {
								s = z;
							}
						} if(s == null) {
							player.sendMessage(f.autoLang("dontSellable"));
							return true;
						}
						double verilecekPara = 0;
						int satilanItem = 0;
						for(int i = 0; i < inv.getSize(); i++) {
							ItemStack item = inv.getItem(i);
							if(item != null) {
								if(!Seller.equalsItem(hand, item)) continue;
								if(Seller.equalsItem(item, hand) && s.itemEquals(item)) {
									verilecekPara += item.getAmount() * s.getPiece();
									satilanItem += item.getAmount();
									inv.setItem(i, null);
								}
							}
						}
						if(satilanItem != 0) {
							String send = f.autoLang("successSell");
							if(hand.hasItemMeta()) {
								if(hand.getItemMeta().hasDisplayName()) {
									send = send.replace("{ITEM}", hand.getItemMeta().getDisplayName());
								}else {send = send.replace("{ITEM}", hand.getType().toString());}
							}else send = send.replace("{ITEM}", hand.getType().toString());
								

							send = send.replace("{MONEY}", String.valueOf(VirtualShop.numberToStr(verilecekPara)));
							send = send.replace("{STACK}", String.valueOf(satilanItem));
							sender.sendMessage(send);
						}
					}else {
						sender.sendMessage(f.autoLang("correctUsage").replace("{USAGE}", "/sell <all/hand>"));
					}
				}
			}else {
				sender.sendMessage(f.autoLang("noConsole"));
			}
		}else sender.sendMessage(f.autoLang("noPermission"));
		return true;
	}

}

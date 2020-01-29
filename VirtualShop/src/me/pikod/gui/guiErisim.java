package me.pikod.gui;

import me.pikod.main.VirtualShop;

public class guiErisim {
	public static String prefix = "&8[&9VirtualShop&8]";
	public static String server_prefix = "&8[&fExample&8]";
	
	public static String admin_menu = "&9&lAdmin Menu";
	public static String categories_admin_menu = "&9Kategorileri düzenle";
	public static String edit_category = "&9Kategori düzenle";
	public static String add_item = "&9Kategoriye Eşya Ekle";
	public static String edit_item = "&9Eşya düzenle";
	
	public static String bar = "▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉";
	
	/* PLAYER EDIT */
	public static String categories_title = "&9Kategoriler";
	public static String items_title = "&9Market";
	
	public static String closedSell = " &cBu eşyayadan satma kapatıldı!";
	public static String closedBuy = " &cBu eşyayadan alma kapatıldı!";
	public static String successBuy = " &2{ITEM} &aadlı eşyadan &2{MONEY} &akarşılığında &2{STACK} &atane aldınız!";
	public static String successSell = " &2{ITEM} &aadlı eşyadan &2{MONEY} &akarşılığında &2{STACK} &atane sattınız!";
	public static String notEnoughMoney = " &cBunu alabilmek için yeterli paraya sahip değilsin!";
	public static String notEnoughItem = " &cSatabilmek için gerekli miktarda eşyaya sahip değilsin!";
	public static String noPermission = " &cBu komudu kullanmak için gerekli yetkiye sahip değilsin!";
	
	public static void reload() {
		if(VirtualShop.shops.isSet("prefix")) {
			server_prefix = VirtualShop.shops.getString("prefix");
		}
		
		
		if(VirtualShop.shops.isSet("categories_title")) {
			server_prefix = VirtualShop.shops.getString("categories_title");
		}
		if(VirtualShop.shops.isSet("items_title")) {
			items_title = VirtualShop.shops.getString("items_title");
		}
		
		if(VirtualShop.shops.isSet("closedSell")) {
			closedSell = VirtualShop.shops.getString("closedSell");
		}
		if(VirtualShop.shops.isSet("closedBuy")) {
			closedBuy = VirtualShop.shops.getString("closedBuy");
		}
		
		if(VirtualShop.shops.isSet("successBuy")) {
			successBuy = VirtualShop.shops.getString("successBuy");
		}
		if(VirtualShop.shops.isSet("successSell")) {
			successSell = VirtualShop.shops.getString("successSell");
		}
		
		if(VirtualShop.shops.isSet("notEnoughMoney")) {
			notEnoughMoney = VirtualShop.shops.getString("notEnoughMoney");
		}
		if(VirtualShop.shops.isSet("notEnoughItem")) {
			notEnoughItem = VirtualShop.shops.getString("notEnoughItem");
		}
		if(VirtualShop.shops.isSet("noPermission")) {
			notEnoughItem = VirtualShop.shops.getString("nopermission");
		}
	}
}

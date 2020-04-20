package me.pikod.gui;

import me.pikod.main.VirtualShop;

public class guiErisim {
	public static String prefix = "&8[&9VirtualShop&8]";
	
	public static String admin_menu = "&9&lAdmin Menu";
	public static String categories_admin_menu = "&9Kategorileri düzenle";
	public static String edit_category = "&9Kategori düzenle";
	public static String add_item = "&9Kategoriye Eşya Ekle";
	public static String edit_item = "&9Eşya düzenle";
	
	public static String bar = "▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉";
	
	/* PLAYER EDIT */
	public static String getStr(String field) {
		if(VirtualShop.lang.isSet(field)) {
			return VirtualShop.lang.getString(field);
		}else {
			return "Bilinmeyen metin!";
		}
		
	}
}

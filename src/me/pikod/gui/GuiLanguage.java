package me.pikod.gui;

import me.pikod.main.VirtualShop;

public class GuiLanguage {
	public static String prefix = "&8[&9VirtualShop&8]";
	
	public static String admin_menu = "&9&lAdmin Menu";
	public static String categories_admin_menu = "&9Edit Categories";
	public static String edit_category = "&9Edit Category";
	public static String add_item = "&9Add Item";
	public static String edit_item = "&9Edit Item";
	
	public static String bar = "█████████████████████████████████";
	
	/* PLAYER EDIT */
	public static String getStr(String field) {
		if(VirtualShop.lang.isSet(field)) {
			return VirtualShop.lang.getString(field);
		}else {
			return "Undefined text: "+field;
		}
		
	}
}

package me.pikod.gui;

import me.pikod.main.VirtualShop;

public class GuiLanguage {
	public static String prefix = "&8[&9VirtualShop&8]";
	
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

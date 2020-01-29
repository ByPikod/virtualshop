package me.pikod.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import me.pikod.main.Color;

public class guiManager {
	private Inventory gui = null;
	private String idName;
	
	public void create(int rows, String name, String idName) {
		int slot = 9;
		switch(rows) {
			case 1:
				slot = 9;
				break;
			case 2:
				slot = 18;
				break;
			case 3:
				slot = 27;
				break;
			case 4:
				slot = 36;
				break;
			case 5:
				slot = 45;
				break;
			case 6:
				slot = 54;
				break;
			default:
				slot = 9;
				break;
		}
		
		this.gui = Bukkit.createInventory(null, slot, Color.chat(name));
		this.idName = idName;
	}
	
	//ITEM SETTINGS
	
	public void setItem(int slot, org.bukkit.inventory.ItemStack item) {
		if(gui != null) {
			gui.setItem(slot, item);
		}
	}
	
	
	public String getIdName() {
		return idName;
	}
	
	public Inventory getInventory() {
		return this.gui;
	}
}



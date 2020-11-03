package me.pikod.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.pikod.utils.Color;

public class GuiManager {
	public Inventory gui = null;

	private static HashMap<Player, GuiManager> lastInventories = new HashMap<Player, GuiManager>();
	private HashMap<String, Integer> inventoryData = new HashMap<String, Integer>();
	
	protected GuiManager(Player player) {
		lastInventories.put(player, this);
	}
	
	public void setData(String key, Integer data) {
		inventoryData.put(key, data);
	}
	
	public int getData(String key) {
		return inventoryData.get(key);
	}
	
	public static GuiManager getLastInventory(Player player) {
		return lastInventories.get(player);
	}
	
	public void create(int rows, String name) {
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
	}
	
	//ITEM SETTINGS
	
	public void setItem(int slot, org.bukkit.inventory.ItemStack item) {
		if(gui != null) {
			gui.setItem(slot, item);
		}
	}
}



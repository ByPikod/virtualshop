package me.pikod.gui;
import org.bukkit.entity.Player;

import me.pikod.gui.GuiManager;
import me.pikod.main.VirtualShop;
import me.pikod.main.data.DataCategory;
import me.pikod.main.data.DataSaver;
import me.pikod.utils.Color;
import me.pikod.utils.f;

public class GuiCategories extends GuiManager {
	public GuiCategories(Player player) {
		super(player);
		this.create(VirtualShop.config.getInt("categoryMenuSize"), Color.chat(f.autoLang("categoriesTitle")));
		for(DataCategory category : DataSaver.getCategories()){
			gui.setItem(category.getSlot(), category.getUserView());
		}
		
		player.openInventory(gui);
	}
}

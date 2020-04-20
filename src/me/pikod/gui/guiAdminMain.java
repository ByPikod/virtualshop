package me.pikod.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pikod.main.Color;

public class guiAdminMain extends guiManager {
	public guiAdminMain(Player player) {
		this.create(1, guiErisim.admin_menu, "adminMenu");
		
		ItemStack reloadPlugin = new ItemStack(Material.DIODE);
		ItemStack kagit1 = new ItemStack(Material.PAPER, 1);
		//ItemStack kagit2 = new ItemStack(Material.PAPER, 1);
		ItemStack categories = new ItemStack(Material.ANVIL, 1);
		
		ItemMeta meta = kagit1.getItemMeta();
		meta.setDisplayName(Color.chat("&a&lPlugin Yapým Ekibi"));
		List<String> lore = new ArrayList<>();
		lore.add(Color.chat("&2Pikod tarafýnca yapýldý!"));
		lore.add(Color.chat("&2Týklayarak ulaþabilirsiniz!"));
		meta.setLore(lore);
		kagit1.setItemMeta(meta);
		
		/*meta = kagit2.getItemMeta();
		meta.setDisplayName(renkli.chat("&a&lKullaným Rehberi"));
		lore.clear();
		lore.add(renkli.chat("&2Týklayarak spigot'taki eðitim"));
		lore.add(renkli.chat("&2rehberine göz atabilirsiniz!"));
		meta.setLore(lore);
		kagit2.setItemMeta(meta);*/
		
		meta = categories.getItemMeta();
		meta.setDisplayName(Color.chat("&a&lKategori Ekle/Düzenle"));
		lore.clear();
		lore.add(Color.chat("&2Kategori eklmeye, düzenlemeye"));
		lore.add(Color.chat("&2yarayan menüyü açmaktadýr!"));
		meta.setLore(lore);
		categories.setItemMeta(meta);
		
		meta = reloadPlugin.getItemMeta();
		meta.setDisplayName(Color.chat("&c&lConfig yenile"));
		lore.clear();
		lore.add(Color.chat("&4Config dosyalarýndaki bilgileri"));
		lore.add(Color.chat("&4çekerek marketi günceller."));
		meta.setLore(lore);
		reloadPlugin.setItemMeta(meta);
		
		this.setItem(8, kagit1);
		//this.setItem(6, kagit2);
		this.setItem(4, categories);
		this.setItem(0, reloadPlugin);
		
		player.openInventory(this.getInventory());
	}
}

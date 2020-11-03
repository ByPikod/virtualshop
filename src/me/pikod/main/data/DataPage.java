package me.pikod.main.data;

import java.util.ArrayList;
import java.util.List;

public class DataPage {
	private final DataCategory category;
	
	protected List<DataItem> items = new ArrayList<DataItem>();
	
	private final int page;
	
	public DataPage(DataCategory category, int page) {
		this.category = category;
		this.page = page;
		category.pages.put(page, this);
	}
	
	public DataCategory getCategory() {
		return category;
	}
	
	public List<DataItem> getItems() {
		return items;
	}
	
	public int getID() {
		return page;
	}
	
	public DataItem getItemFromId(int id) {
		for(DataItem item : items) {
			if(item.getSlot() == id) return item;
		}
		return null;
	}
}

package me.pikod.main;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatInterface {
	public boolean chatAction(AsyncPlayerChatEvent event);
}

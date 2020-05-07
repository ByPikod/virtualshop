package me.pikod.main;

import org.bukkit.entity.Player;

import me.pikod.listener.ActionHandler;

public class ChatEvent {
	public Player player;
	public ChatInterface listener;
	public int id;
	
	public ChatEvent(Player player, ChatInterface listener) {
		this.player = player;
		this.listener = listener;
		ActionHandler.chatListenerList.add(this);
	}
	
	public void setListener(ChatInterface listener) {
		this.listener = listener;
	}
}

package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ActionMessageSent extends Action{

	public ActionMessageSent() {
		super(ActionType.MESSAGE_SENT);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		checkConditions(event.getPlayer());
	}
	
	

}

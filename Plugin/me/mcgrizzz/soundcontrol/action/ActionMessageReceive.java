package me.mcgrizzz.soundcontrol.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ActionMessageReceive extends Action{

	public ActionMessageReceive() {
		super(ActionType.MESSAGE_RECEIVE);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		for(Player p : event.getRecipients()){
			checkConditions(p);
		}
	}

}

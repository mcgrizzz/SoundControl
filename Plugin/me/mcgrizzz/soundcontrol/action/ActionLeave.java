package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActionLeave extends Action {

	public ActionLeave() {
		super(ActionType.LEAVE);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		checkConditions(e.getPlayer());
	}
	
	

}
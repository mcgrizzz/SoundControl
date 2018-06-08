package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ActionCmd extends Action{

	public ActionCmd() {
		super(ActionType.CMD);
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		checkConditions(event.getPlayer());
	}

}

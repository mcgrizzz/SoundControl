package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ActionDeath extends Action{

	public ActionDeath() {
		super(ActionType.DEATH);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		checkConditions(e.getEntity());
	}
	
	

}

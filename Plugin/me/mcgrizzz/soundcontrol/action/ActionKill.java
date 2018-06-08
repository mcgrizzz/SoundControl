package me.mcgrizzz.soundcontrol.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ActionKill extends Action {
	
	
	public ActionKill() {
		super(ActionType.KILL);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent e){
		
			Player p = e.getEntity();
			if(p.getKiller() != null){
				checkConditions(p.getKiller());
			}
			
	}
	
	
}

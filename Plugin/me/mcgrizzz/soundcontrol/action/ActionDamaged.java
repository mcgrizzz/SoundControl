package me.mcgrizzz.soundcontrol.action;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class ActionDamaged extends Action{
	
	
	public ActionDamaged() {
		super(ActionType.DAMAGED);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			checkConditions(p);
		}
	}
	
}

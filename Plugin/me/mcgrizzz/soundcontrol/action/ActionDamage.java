package me.mcgrizzz.soundcontrol.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ActionDamage extends Action{
	
	
	public ActionDamage() {
		super(ActionType.DAMAGE);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player)e.getDamager();
			checkConditions(p);
		}
	}
	
}

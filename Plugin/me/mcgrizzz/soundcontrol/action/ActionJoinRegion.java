package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import me.mcgrizzz.soundcontrol.SoundControl;

public class ActionJoinRegion extends Action {

	public ActionJoinRegion() {
		super(ActionType.JOIN_REGION);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		if(!SoundControl.instance().regionEnabled){
			return;
		}
		
		if ((e.getFrom().getX() == e.getTo().getX()) && (e.getFrom().getZ() == e.getTo().getZ())) {
		      return;
		}
		if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())return;
		if(SoundControl.instance().getRegion().getRegion(e.getTo()).equalsIgnoreCase(SoundControl.instance().getRegion().getRegion(e.getFrom())))return;
		
		checkConditions(e.getPlayer(), e.getTo());
	}
	
	

}

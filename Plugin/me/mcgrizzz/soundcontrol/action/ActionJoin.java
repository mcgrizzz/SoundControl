package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import me.mcgrizzz.soundcontrol.SoundControl;

public class ActionJoin extends Action {

	public ActionJoin() {
		super(ActionType.JOIN);
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		SoundControl.instance().getServer().getScheduler().runTaskLater(SoundControl.instance(), new Runnable(){

			@Override
			public void run() {
				checkConditions(e.getPlayer());
			}
			
		}, 5);
		
	}

}
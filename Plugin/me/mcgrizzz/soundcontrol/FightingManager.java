package me.mcgrizzz.soundcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FightingManager implements Runnable, Listener{
	
	HashMap<UUID, Integer> hit = new HashMap<UUID, Integer>();
	
	public FightingManager(){
		SoundControl.instance().getServer().getPluginManager().registerEvents(this, SoundControl.instance());
		SoundControl.instance().getServer().getScheduler().runTaskTimer(SoundControl.instance(), this, 0, 45);
	}
	
	public void onStop(){
		hit.clear();
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player damager = (Player)event.getDamager();
			Player entity = (Player)event.getEntity();
			fight(damager, entity);
		}
	}
	
	
	public void fight(Player a, Player b){
			hit.put(a.getUniqueId(), 1);
			hit.put(b.getUniqueId(), 1);
	}
	
	public boolean isFighting(Player p){
		return hit.containsKey(p.getUniqueId());
	}
	
	
	@Override
	public void run() {
		ArrayList<UUID> remove = new ArrayList<UUID>();
		for(UUID id : hit.keySet()){
			if(hit.containsKey(id)){
				int i = hit.get(id);
				i--;
				hit.put(id, i);
				
				if(hit.get(id) == -1){
					remove.add(id);
				}
			}
		}
		
		for(UUID id : remove){
			hit.remove(id);
		}
		
	}
	
	

}

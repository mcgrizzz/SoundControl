package me.mcgrizzz.soundcontrol.action;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.mcgrizzz.soundcontrol.SoundControl;
import me.mcgrizzz.soundcontrol.condition.ConditionType;


public abstract class Action implements Listener{
	
	ActionType type;
	
	HashMap<ConditionType, Integer> plays = new HashMap<ConditionType, Integer>();
	HashMap<String, Integer> regions = new HashMap<String, Integer>();
	
	public Action(ActionType type){
		this.type = type;
		SoundControl.instance().getServer().getPluginManager().registerEvents(this, SoundControl.instance());
	}
	
	public void addCondition(ConditionType c, int id){
		this.plays.put(c, id);
	}
	
	public void addRegion(String s, int id){
		this.regions.put(s, id);
	}
	
	public ActionType getType(){
		return this.type;
	}
	
	public void checkConditions(Player p){
		for(ConditionType t : plays.keySet()){
			if(t == ConditionType.FIGHT){
				if(SoundControl.instance().getFight().isFighting(p)){
					SoundControl.instance().playEffect(p, plays.get(t));
				}
			}else if(t == ConditionType.RAIN){
				if(p.getWorld().isThundering()){
					SoundControl.instance().playEffect(p, plays.get(t));
				}
			}else{
				SoundControl.instance().playEffect(p, plays.get(t));
			}
		}
		
		if(!SoundControl.instance().regionEnabled){
			return;
		}
		
		if(type == ActionType.LEAVE_REGION || type == ActionType.JOIN_REGION)return;
		for(String s : regions.keySet()){
			if(SoundControl.instance().getRegion().getRegion(p.getLocation()).equals(s)){
				SoundControl.instance().playEffect(p, regions.get(s));
			}
		}
	}
	
	//This is specifically for Leave and Join region
	public void checkConditions(Player p, Location loc){
		
		if(!SoundControl.instance().regionEnabled){
			return;
		}
		
		String reg = SoundControl.instance().getRegion().getRegion(loc);
		
		
		
		boolean played = false;
		for(String s : regions.keySet()){
			if(reg.equals(s)){
				SoundControl.instance().playEffect(p, regions.get(s));
				played = true;
			}
		}
		
		if(!played && !reg.equalsIgnoreCase("default")){
			checkConditions(p);
		}
		
		
		
	}
	
	
	

}

package me.mcgrizzz.soundcontrol;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.mcgrizzz.soundcontrol.condition.ConditionType;
import me.mcgrizzz.soundcontrol.region.Region;

public class RegionManager {
	
	HashMap<UUID, Integer> playerRegions = new HashMap<UUID, Integer>();
	HashMap<Integer, Region>  regions = new HashMap<Integer, Region>();
 	
	public void onStop(){
		playerRegions.clear();
		regions.clear();
	}
	
	public int getCurrentRegion(Player p){
		if(playerRegions.containsKey(p.getUniqueId())){
			return playerRegions.get(p.getUniqueId());
		}
		return -2;
	}
	
	public void removePlayer(Player p){
		if(playerRegions.containsKey(p.getUniqueId())){
			playerRegions.remove(p.getUniqueId());
			SoundControl.instance().playingMusic.remove(p.getUniqueId());
		}
	}
	
	public boolean checkRegions(Region r, boolean fighting, boolean raining, String regLoc){
		
		boolean def = r.getRegion().equalsIgnoreCase("default");
		//System.out.println("\n");
		////System.out.println(def);
		//System.out.println("region: " + r.getRegion());
		//System.out.println("region player is in: " + regLoc);
		ConditionType type = r.getType();
		//System.out.println("type of condition: " + type);
		if(def){
			if(type == ConditionType.RAIN){
				if(raining){
					if(fighting){
						//check if there is a default fighting or regLoc fighting, return false if there is 
						if(exists("default", ConditionType.FIGHT)){
							//System.out.println("RETURN FALSE 1");
							return false;
						}
						if(exists(regLoc, ConditionType.FIGHT)){
							//System.out.println("RETURN FALSE 2");
							return false;
						}
					}else{
						//check if there is a regLoc raining, if there is return false
						if(!regLoc.equalsIgnoreCase("default") && exists(regLoc, ConditionType.RAIN)){
							//System.out.println("RETURN FALSE 3");
							return false;
						}
						//System.out.println("RETURN TRUE 1");
						return true;
					}
				}else{
					//System.out.println("RETURN FALSE 4");
					return false;
				}
			}else if(type == ConditionType.FIGHT){
				if(fighting){
					//fighting
					//check regLoc fighting, if there is return false
					if(!regLoc.equalsIgnoreCase("default") && exists(regLoc, ConditionType.FIGHT)){
						//System.out.println("RETURN FALSE 5");
						return false;
					}
					//System.out.println("RETURN TRUE 2");
					return true;
				}else{
					//System.out.println("RETURN FALSE 6");
					return false;
				}
			}else{
				if(fighting){
					//check if there is a fighting for regLoc or default, if there is return false
					if(exists("default", ConditionType.FIGHT) || exists(regLoc, ConditionType.FIGHT)){
						//System.out.println("RETURN FALSE 7");
						return false;
					}
				}
				if(raining){
					//check if there is a raining for regLoc or default, if there is reutrn false
					if(exists("default", ConditionType.RAIN) || exists(regLoc, ConditionType.RAIN)){
						//System.out.println("RETURN FALSE 8");
						return false;
					}
				}
				if(!regLoc.equalsIgnoreCase("default") && exists(regLoc, ConditionType.DEFAULT)){
					//System.out.println("RETURN FALSE 9");
					return false;
				}
				
				//System.out.println("RETURN TRUE 3");
				return true;
			}
		}else{
			//region is not default
			if(!r.getRegion().equalsIgnoreCase(regLoc)){
				//System.out.println("RETURN FALSE 4");
				return false;
			
			}
			if(type == ConditionType.RAIN){
				if(raining){
					if(fighting){
						//check if there is a default fighting or regLoc fighting, return false if there is 
						if(exists("default", ConditionType.FIGHT)){
							//System.out.println("RETURN FALSE 10");
							return false;
						}
						if(exists(regLoc, ConditionType.FIGHT)){
							//System.out.println("RETURN FALSE 11");
							return false;
						}
					}else{
						//System.out.println("RETURN TRUE 4");
						return true;
					}
				}else{
					///System.out.println("RETURN FALSE 12");
					return false;
				}
			}else if(type == ConditionType.FIGHT){
				if(fighting){
					//fighting
					//check regLoc fighting, if there is return false
					//System.out.println("RETURN TRUE 5");
					return true;
				}else{
					//System.out.println("RETURN FALSE 13");
					return false;
				}
			}else{
				if(fighting){
					//check if there is a fighting for regLoc or default, if there is return false
					if(exists("default", ConditionType.FIGHT) || exists(regLoc, ConditionType.FIGHT)){
						//System.out.println("RETURN FALSE 14");
						return false;
					}
				}
				if(raining){
					//check if there is a raining for regLoc or default, if there is reutrn false
					if(exists("default", ConditionType.RAIN) || exists(regLoc, ConditionType.RAIN)){
						//System.out.println("RETURN FALSE 15");
						return false;
					}
				}
				//System.out.println("RETURN TRUE 6");
				return true;
			}
		}
		//System.out.println("RETURN FALSE 16");
		return false;
	}
	
	
	//only call of regLoc is default
	public void checkForRemove(Player p, boolean raining, boolean fighting){
		boolean defAcceptable = false;
		for(Region r : regions.values()){
			if(r.getRegion().equals("default")){
				if(raining){
					if(r.getType() == ConditionType.RAIN){
						defAcceptable = true;
						break;
					}
				}
				if(fighting){
					if(r.getType() == ConditionType.FIGHT){
						defAcceptable = true;
						break;
					}
				}
				if(r.getType() != ConditionType.FIGHT && r.getType() != ConditionType.RAIN){
					defAcceptable = true;
					break;
				}
			}
		}
		
		if(!defAcceptable){
			SoundControl.instance().stopMusic(p);
			removePlayer(p);
		}
	}
	
	//public boolean checkAlternative(String reg, ConditionType t, String locReg, int id){
			//return false;
	//}
	
	public boolean exists(String s, ConditionType t){
		for(int i : regions.keySet()){
			Region r = regions.get(i);
			if(r.getRegion().equalsIgnoreCase(s)){
				if(t == r.getType()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void setRegion(Player p, int i){
		this.playerRegions.put(p.getUniqueId(), i);
	}
	
	public void addRegion(String s, Region r){
		int i = regions.size();
		r.setId(i);
		regions.put(i, r);
	}
	
	public boolean isRegion(String s){
		return regions.containsKey(s);
	}
	
	public Region getFromId(int i){
		if(regions.containsKey(new Integer(i))){
			return regions.get(new Integer(i));
		}
		return null;
	}
	
	
	public boolean inActiveRegion(Location l){
		 ApplicableRegionSet regionss = WGBukkit.getRegionManager(l.getWorld()).getApplicableRegions(l);
		    
		    for (ProtectedRegion region : regionss.getRegions()) {
		    	if(regions.containsKey(region.getId())){
		    		return true;
		    	}
		    }
		    
		    return false;
	}
	
	public String getRegion(Location l){
	    
		ApplicableRegionSet regions = WGBukkit.getRegionManager(l.getWorld()).getApplicableRegions(l);
	    
	    int highestPriority = -1;
	    ProtectedRegion highestRegion = null;
	    for (ProtectedRegion region : regions.getRegions()) {
	      if ((region.getPriority() > highestPriority))
	      {
	        highestPriority = region.getPriority();
	        highestRegion = region;
	      }
	    }
	    
	    if(highestRegion != null){
	    	return highestRegion.getId();
	    }
	    
	    return "default";
	}

}

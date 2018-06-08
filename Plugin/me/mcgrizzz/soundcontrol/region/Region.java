package me.mcgrizzz.soundcontrol.region;

import me.mcgrizzz.soundcontrol.SoundControl;
import me.mcgrizzz.soundcontrol.condition.ConditionType;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Region implements Listener{
	
	ConditionType c;
	int music;
	String region;
	int id;
	
	public Region(ConditionType c, int music, String region){
		this.c = c;
		this.music = music;
		this.region = region;
		SoundControl.instance().getServer().getPluginManager().registerEvents(this, SoundControl.instance());
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public ConditionType getType(){
		return this.c;
	}
	
	
	public String getRegion(){
		return this.region;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		////System.out.println("Moving around");
		if ((e.getFrom().getX() == e.getTo().getX()) && (e.getFrom().getZ() == e.getTo().getZ())) {
		      return;
		}
		if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())return;
		String locReg = SoundControl.instance().getRegion().getRegion(e.getTo());
		
		
		if(SoundControl.instance().getRegion().checkRegions(this, SoundControl.instance().getFight().isFighting(e.getPlayer()), 
				e.getPlayer().getWorld().hasStorm(), locReg)){
			SoundControl.instance().playMusic(e.getPlayer(), music);
			SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
			
		}else if(locReg.equals("default")){
			SoundControl.instance().getRegion().checkForRemove(e.getPlayer(), 
					e.getPlayer().getWorld().hasStorm(), SoundControl.instance().getFight().isFighting(e.getPlayer()));
		}
		
		////System.out.println("CHECKING REGION CONDITION: " + c);
		/*if(c == ConditionType.DEFAULT){
			//If this is default
			//if(SoundControl.instance().getRegion().getCurrentRegion(e.getPlayer()) == -2){
				//Check if player is fighting, if they are see if there are any 
				boolean fighting = SoundControl.instance().getFight().isFighting(e.getPlayer());
				boolean raining = e.getPlayer().getWorld().isThundering();
				boolean hasAnotherRegion = false;
				
				if(fighting){
					//System.out.println("is fighting");
					hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.FIGHT, locReg, id);
				}
				
				if(raining){
					//System.out.println("is raining");
					if(!fighting){
						hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.RAIN, locReg, id);
					}else if(!hasAnotherRegion){
						hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.RAIN, locReg, id);
					}
				}
				if(!hasAnotherRegion && (raining || fighting)){
					//System.out.println("no other check, raining or fighting");
					hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, fighting ? ConditionType.FIGHT : ConditionType.RAIN, locReg, id);
				}else if(!hasAnotherRegion){
					//System.out.println("no other check");
					hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.DEFAULT, locReg, id);
				}
				
				if(!hasAnotherRegion){
					//System.out.println("there isn't another");
					SoundControl.instance().playMusic(e.getPlayer(), music);
					SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
				}
			//}
		}else if(c == ConditionType.RAIN){
			//if this is checking for rain
			boolean fighting = SoundControl.instance().getFight().isFighting(e.getPlayer());
			boolean raining = e.getPlayer().getWorld().isThundering();
			boolean hasAnotherRegion = false;
			
			if(fighting){
				hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.FIGHT, locReg, id);
			}
			
			if(!hasAnotherRegion){
				hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.DEFAULT, locReg, id);
			}
			
			if((!fighting || !hasAnotherRegion) && raining){
				SoundControl.instance().playMusic(e.getPlayer(), music);
				SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
			}
		}else /*if(c == ConditionType.FIGHT){
			//System.out.println("This condition is fight");
			boolean fighting = SoundControl.instance().getFight().isFighting(e.getPlayer());
			//System.out.println("fighting: " + fighting);
			boolean hasAnotherRegion = SoundControl.instance().getRegion().checkAlternative(region, ConditionType.FIGHT, locReg, id);
		
			//System.out.println("has another region: " + hasAnotherRegion);
			if(fighting && !hasAnotherRegion){
				//System.out.println("is fighting and no other region");
				SoundControl.instance().playMusic(e.getPlayer(), music);
				SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
			}
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		////System.out.println("MOVING");
		////System.out.println(region  + " region per location: " + SoundControl.instance().getRegion().getRegion(e.getTo()));
		/*if(!SoundControl.instance().getRegion().getRegion(e.getTo()).equals(region))return;
		////System.out.println("Not going to region");
		//if(SoundControl.instance().getRegion().getRegion(e.getFrom()) != SoundControl.instance().getRegion().getRegion(e.getTo())){
			if(SoundControl.instance().getRegion().getCurrentRegion(e.getPlayer()) != -2){
			//	//System.out.println("is set");
			int reg = SoundControl.instance().getRegion().getCurrentRegion(e.getPlayer());
			//System.out.println("Current Region playing: " + reg);
			ConditionType type = SoundControl.instance().getRegion().getFromId(reg).c;
			//System.out.println("Current region playing type: " +type);
			////System.out.println(type);
			if(type == ConditionType.DEFAULT){
				//System.out.println("Current region playing is default");
				  if(c != ConditionType.DEFAULT){
					  //System.out.println("Region checking is not default");
					  if(c == ConditionType.FIGHT){
						  //System.out.println("Region checking is fighting");
						  if(SoundControl.instance().getFight().isFighting(e.getPlayer())){
							  //System.out.println("Player is fighting");
							  	SoundControl.instance().playMusic(e.getPlayer(), music);
								SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
						  }
					  }
				  }
				}else if(type == ConditionType.RAIN){
					 //System.out.println("Current region playing is rain");
					if(!e.getPlayer().getWorld().isThundering()){
						//System.out.println("it's not raining");
						if(c != ConditionType.RAIN){
							//System.out.println("checking condition checking is not rain");
							if(c == ConditionType.FIGHT){
								//System.out.println("checking region is fighting");
								 if(SoundControl.instance().getFight().isFighting(e.getPlayer())){
									 //System.out.println("Is fighting");
									  	SoundControl.instance().playMusic(e.getPlayer(), music);
										SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
								  }
							}else{
								//System.out.println("Region checking is not fighting");
								SoundControl.instance().playMusic(e.getPlayer(), music);
								SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
							}
						}
					}
				}else{
					//System.out.println("Current region is fighting");
					if(!SoundControl.instance().getFight().isFighting(e.getPlayer())){
						//System.out.println("player is not fighting");
						if(c != ConditionType.FIGHT){
							//System.out.println("Checked region is not fighting");
							if(c == ConditionType.RAIN){
								if(e.getPlayer().getWorld().isThundering()){
									SoundControl.instance().playMusic(e.getPlayer(), music);
									SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
								}
							}else{
								SoundControl.instance().playMusic(e.getPlayer(), music);
								SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
							}
						}
					}else{
						if(c == ConditionType.FIGHT){
							if(SoundControl.instance().getFight().isFighting(e.getPlayer())){
								SoundControl.instance().playMusic(e.getPlayer(), music);
								SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
							}
						}
					}
				}
			}else{
				////System.out.println("not set");
				//System.out.println(c);
				if(c == ConditionType.DEFAULT){
					SoundControl.instance().playMusic(e.getPlayer(), music);
					SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
				}else if(c == ConditionType.FIGHT){
					if(SoundControl.instance().getFight().isFighting(e.getPlayer())){
						SoundControl.instance().playMusic(e.getPlayer(), music);
						SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
					}
				}else{
					if(e.getPlayer().getWorld().isThundering()){
						SoundControl.instance().playMusic(e.getPlayer(), music);
						SoundControl.instance().getRegion().setRegion(e.getPlayer(), id);
					}
				}
			}
		//}else{
			
		//}
		
		/*if(SoundControl.instance().getRegion().getRegion(e.getFrom()) == SoundControl.instance().getRegion().getRegion(e.getTo()) 
				&& !SoundControl.instance().getRegion().getCurrentRegion(e.getPlayer()).equals("not_set")){
			String reg = SoundControl.instance().getRegion().getCurrentRegion(e.getPlayer());
			ConditionType type = SoundControl.instance().getRegion().getRegionFromName(reg).c;
			if(c == ConditionType.DEFAULT){
				if(type == ConditionType.DEFAULT){
					SoundControl.instance().playMusic(e.getPlayer(), music);
					SoundControl.instance().getRegion().setRegion(e.getPlayer(), region);
					return;
				}
				//Least priority
			}else if(c == ConditionType.FIGHT){
				if(SoundControl.instance().getFight().isFighting(e.getPlayer())){
					SoundControl.instance().playMusic(e.getPlayer(), music);
					SoundControl.instance().getRegion().setRegion(e.getPlayer(), region);
				}
				//Most priority
			}else if(c == ConditionType.RAIN){
				//Second priority
				if(type != ConditionType.RAIN){
					
				}
			}
			
		}else{
			
		}*/
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		if(SoundControl.instance().getRegion().getCurrentRegion(event.getPlayer()) != -2){
			SoundControl.instance().getRegion().removePlayer(event.getPlayer());
		}
	}
	
	/*@EventHandler
	public void onWeather(WeatherChangeEvent e){
		if(c == ConditionType.RAIN){
			if(e.toWeatherState()){
				
			}
		}
	}*/

}

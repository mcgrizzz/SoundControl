package me.mcgrizzz.soundcontrol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi;

import me.mcgrizzz.soundcontrol.action.Action;
import me.mcgrizzz.soundcontrol.action.ActionType;
import me.mcgrizzz.soundcontrol.condition.ConditionType;
import me.mcgrizzz.soundcontrol.region.Region;
import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;

public class SoundControl extends JavaPlugin implements Runnable{
	
	static SoundControl me;
	FightingManager fm;
	RegionManager rm;
	
	public boolean regionEnabled;
	
	HashMap<Integer, Media> music = new HashMap<Integer, Media>();
	HashMap<UUID, Integer> playingMusic = new HashMap<UUID, Integer>();
	
	HashMap<UUID, Integer> rateLimit = new HashMap<UUID, Integer>();
	
	HashMap<Integer, Media> effects = new HashMap<Integer, Media>();
	
	HashMap<ActionType, Action> actions = new HashMap<ActionType, Action>();
	
	String id = "SoundControl";
	String version;
	
	boolean updateNeeded = false;
	
	public void onEnable(){
		me = this;
		
		regionEnabled = getServer().getPluginManager().getPlugin("WorldGuard") != null; 
		
		
		if(!regionEnabled){
			System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString() + "[SoundControl] WorldGuard not detected, no region support!"+ Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		}
		fm = new FightingManager();
		
		if(regionEnabled){
			rm = new RegionManager();
		}
		
		loadMedia();
		loadActions();
		
		updateNeeded = updateNeeded(true);
		if(!updateNeeded){
			System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString() + "[SoundControl] Up to date!"+ Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		}
		
		
		getServer().getScheduler().runTaskTimer(this, this, 0, 20);
	}
	
	public void onDisable(){
		music.clear();
		playingMusic.clear();
		effects.clear();
		actions.clear();
		fm.onStop();
		if(regionEnabled){
			rm.onStop();
		}
		
		
	}
	
	public static SoundControl instance(){
		return me;
	}
	
	public void playEffect(Player p, int i){
		//System.out.println("TRYING TO PLAY EFFECT!");
		if(effects.containsKey(i)){
			//System.out.println("PLAYING EFFECT!");
			if(rateLimit.containsKey(p.getUniqueId())){
				if(rateLimit.get(p.getUniqueId()) < 1){
					JukeboxAPI.play(p, effects.get(new Integer(i)));
					rateLimit.put(p.getUniqueId(), 1);
				}
			}else{
				JukeboxAPI.play(p, effects.get(new Integer(i)));
				rateLimit.put(p.getUniqueId(), 0);
			}
			
		}
	}
	
	public void playMusic(Player p, int i){
		//System.out.println("TRYING TO PLAYING MUSIC!");
		if(music.containsKey(i)){
			if(playingMusic.containsKey(p.getUniqueId())){
				if(playingMusic.get(p.getUniqueId()) != i){
					//System.out.println("PLAYING MUSIC!");
					JukeboxAPI.play(p, music.get(new Integer(i)));
					playingMusic.put(p.getUniqueId(), i);
				}
			}else{
				JukeboxAPI.play(p, music.get(new Integer(i)));
				playingMusic.put(p.getUniqueId(), i);
			}
			
		}
	}
	
	public void loadActions(){
		
		File act = new File(getDataFolder(), "commands.txt");
		if(act.exists()){
			try (BufferedReader br = new BufferedReader(new FileReader(act))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    		readAction(line);
		    	}
			}catch(Exception e){
				
			}
		}else{
			loadResource("commands.txt");
		}
		
	}
	
	public void readAction(String s){
		String[] args = s.split(" ");
		if(args.length < 4){
			System.out.println("ERROR PARSING COMMANDS: You must define an id, specifier, condition, and media id'");
			return;
		}
		if(args[0].equalsIgnoreCase("action")){
			if(args[1].equalsIgnoreCase("default")){
				System.out.println("ERROR PARSING COMMANDS: Actions must have a defined type and cannot be 'default'");
				return;
			}
			boolean region = false;
			ActionType type = ActionType.fromString(args[1]);
			if(type == null){
				System.out.println("ERROR PARSING COMMANDS: '"+ args[1] + "' is not a valid action.");
				return;
			}
			
			ConditionType t = ConditionType.fromString(args[2]);
			String reg = "";
			if(t == null && args[2].contains("region")){
				region = true;
				reg = args[2].replace("region:", "");
				if(!regionEnabled){
					return;
				}
			}else if(t == null){
				System.out.println("ERROR PARSING COMMANDS: '"+ args[2] + "' is not a valid condition, defaulting to 'default'");
				t = ConditionType.DEFAULT;
			}
			Action c;
			if(actions.containsKey(type)){
				c = actions.get(type);
			}else{
				c = type.getAction();
			}
			
			if(type.toString().toLowerCase().contains("region")){
				if(!regionEnabled){
					return;
				}
			}
			int i = getInt(args[3]);
			if(i == -2){
				System.out.println("ERROR PARSING COMMANDS: '" + args[3] + "' is not a valid integer");
			}else{
				if(region){
					c.addRegion(reg, i);
				}else{
					c.addCondition(t, i);
				}
				
				actions.put(type, c);
			}
			
		}else{
			if(!regionEnabled){
				return;
			}
			String reg = args[1];
			ConditionType t = ConditionType.fromString(args[2]);
			if(t == null && args[2].contains("region")){
				
				System.out.println("ERROR PARSING COMMANDS: You cannot have a region condition on a region command");
				t = ConditionType.DEFAULT;
			}else if(t == null){
				System.out.println("ERROR PARSING COMMANDS: '"+ args[2] + "' is not a valid condition, defaulting to 'default'");
				t = ConditionType.DEFAULT;
			}
			
			int i = getInt(args[3]);
			if(i == -2){
				System.out.println("ERROR PARSING COMMANDS: '" + args[3] + "' is not a valid integer");
			}else{
				Region r = new Region(t, i, reg);
				getRegion().addRegion(reg, r);
			}
		}
	}
	
	public void loadMedia(){
		File eff = new File(getDataFolder(), "effects.txt");
		if(eff.exists()){
			try (BufferedReader br = new BufferedReader(new FileReader(eff))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] args = line.split(" ");
		    	if(args.length < 2){
		    		System.out.println("ERROR PARSING EFFECTS: You must give only a url and an id");
		    		}else{
		    			int i = getInt(args[0]);
		    			if(i == -2){
		    				System.out.println("ERROR PARSING EFFECTS: '" + args[0] + "' is not a valid integer");
		    			}else{
		    				//System.out.println(args[1]);
		    				Media m = new Media(ResourceType.SOUND_EFFECT, args[1]);
		    				effects.put(i, m);
		    			}
		    		}
		    	}
			}catch(Exception e){
			}
		}else{
			loadResource("effects.txt");
		}
		if(!regionEnabled)return;
		File mu = new File(getDataFolder(), "music.txt");
		if(mu.exists()){
			try (BufferedReader br = new BufferedReader(new FileReader(mu))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] args = line.split(" ");
		    	if(args.length < 2){
		    		System.out.println("ERROR PARSING MUSIC: You must give only a url and an id");
		    		}else{
		    			int i = getInt(args[0]);
		    			if(i == -2){
		    				System.out.println("ERROR PARSING MUSIC: '" + args[0] + "' is not a valid integer");
		    			}else{
		    				//System.out.println(args[1]);
		    				Media m = new Media(ResourceType.MUSIC, args[1]);
		    				music.put(i, m);
		    			}
		    		}
		    	}
			}catch(Exception e){
			}
		}else{
			loadResource("music.txt");
		}
	}
	
	public FightingManager getFight(){
		return fm;
	}
	
	public Integer getInt(String s){
		int i;
		try{
			i = Integer.parseInt(s);
		}catch(Exception e){
			i = -2;
		}
		
		return i;
	}
	
	public RegionManager getRegion(){
		return rm;
	}
	
	public void loadResource(String s){
		getDataFolder().mkdir();
		File f = new File(getDataFolder(), s);
		
		
		 InputStream link = (getClass().getResourceAsStream("/" + s));
 		// write the inputStream to a FileOutputStream
 		 try{
 			 	f.createNewFile();
 			 	OutputStream outputStream = new FileOutputStream(f);
 			 		
	    			int read = 0;
	    			byte[] bytes = new byte[1024];
	    	 
	    			while ((read = link.read(bytes)) != -1) {
	    				outputStream.write(bytes, 0, read);
	    			}
 		 }catch(Exception e){
 			 
 		 }
	}
	
	public void stopMusic(Player p){
		if(playingMusic.containsKey(p.getUniqueId())){
			JukeboxAPI.stopMusic(p);
		}
		
	}

	@Override
	public void run() {
		rateLimit.clear();
	}
	
	public boolean updateNeeded(boolean startup){
		
		System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString() + "[" +id+ "] Checking for updates..." + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		try {
			
			
			String link = "https://raw.githubusercontent.com/mcgrizzz/PluginVersions/master/versions.txt";
			
			ArrayList<String> input = new ArrayList<String>();
			try {
				URL url = new URL(link);
				Scanner s = new Scanner(url.openStream());
				while(s.hasNext()){
					input.add(s.next());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//for(String s : input){
				//System.out.println(s);
			//}
			int start = 0;
			for(String s : input){
				if(s.contains(id)){
					start = input.indexOf(s);
					break;
				}
			}
			
			version = input.get(start + 1);
	
			
			
			if(compareVersion(getDescription().getVersion())){
				if(startup)System.out.println(Ansi.ansi().fg(Ansi.Color.RED).bold().toString() + "[" + id + "] Plugin outdated. New version: " + this.version + Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	private boolean compareVersion(String s){
		 String localVersion = s.replace(" ", "").replaceAll("\\.", " ");
		 String externalVersion = this.version.replace(" ", "").replaceAll("[A-Za-z]", "").replaceAll("\\.", " ");
		 String[] externalDigits = externalVersion.split(" ");
		 String[] localDigits = localVersion.split(" ");
		 return (arrayToInt(localDigits) < arrayToInt(externalDigits));
			
	 }
	 
	 private int arrayToInt(String[] string){
		 int result = 0;
		 double multiplier = 100;
		 for(int i = 0; i < string.length; i++){
			 result += Integer.parseInt(string[i])*multiplier;
			 multiplier /= 10;
		 }
		 return result;
	 }


}

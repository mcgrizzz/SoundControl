package me.mcgrizzz.soundcontrolgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.FXCollections;

public class Storage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4719208471166819782L;

	ArrayList<Command> commands;
	
	HashMap<Integer, String> effects;
	HashMap<String, Integer> effectNames;
	
	HashMap<Integer, String> music;
	HashMap<String, Integer> musicNames;
	
	
	
	
	public Storage(){
		commands = new ArrayList<Command>();
		
		effects = new HashMap<Integer, String>();
		effectNames = new HashMap<String, Integer>();
		
		music = new HashMap<Integer, String>();
		musicNames = new HashMap<String, Integer>();
		
		SoundControlGui.me.mNames = FXCollections.observableArrayList();
		SoundControlGui.me.mNames.add("Add new music");
		SoundControlGui.me.mNames.addAll(musicNames.keySet());
		
		SoundControlGui.me.eNames = FXCollections.observableArrayList();
		SoundControlGui.me.eNames.add("Add new sound effect");
		SoundControlGui.me.eNames.addAll(effectNames.keySet());
		
	}
	
	public void addEffect(String s, String name){
		if(!effects.containsValue(s)){
			effectNames.put(name, effects.size());
			effects.put(effects.size(), s);
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			    	SoundControlGui.me.eNames.add(name);
			}});
		}
	}
	
	public String getNameMusic(int i){
		for(String s : musicNames.keySet()){
			if(musicNames.get(s) == i){
				return s;
			}
		}
		return null;
	}
	
	public String getNameEffect(int i){
		for(String s : effectNames.keySet()){
			if(effectNames.get(s) == i){
				return s;
			}
		}
		return null;
	}
	
	public void transfer(){
		SoundControlGui.me.mNames = FXCollections.observableArrayList();
		SoundControlGui.me.mNames.add("Add new music");
		SoundControlGui.me.mNames.addAll(musicNames.keySet());
		
		SoundControlGui.me.eNames = FXCollections.observableArrayList();
		SoundControlGui.me.eNames.addAll(effectNames.keySet());
		SoundControlGui.me.eNames.add("Add new sound effect");
	}
	
	public void addMusic(String s, String name){
		if(!music.containsValue(s)){
			musicNames.put(name, music.size());
			music.put(music.size(), s);
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			    	SoundControlGui.me.mNames.add(name);
			}});
		}
	}
	
	public void removeMusic(String name){
		if(musicNames.containsKey(name)){
			removeMusic(musicNames.get(name));
			musicNames.remove(name);
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			    	SoundControlGui.me.mNames.remove(name);
			}});
		}
	}
	
	public void removeMusic(int id){
		music.remove(new Integer(id));
	}
	
	public void removeEffect(String name){
		if(effectNames.containsKey(name)){
			removeEffect(effectNames.get(name));
			effectNames.remove(name);
			Platform.runLater(new Runnable() {
			    @Override public void run() {
			    	SoundControlGui.me.eNames.remove(name);
			}});
			
		}
	}
	
	public void removeEffect(int id){
		effects.remove(new Integer(id));
	}
	
	public void addCommand(Command cmd){
		boolean exists = false;
		for(Command cmmd : commands){
				System.out.println("EXISTS ALREADY: " + cmmd.getId());
				exists = true;
			
		}
		//if(!exists){
			commands.add(cmd);
		//}
	}
	
	public int getEffect(String s){
		if(effectNames.containsKey(s)){
			System.out.println("NAMED: " + s + " NUMBER: " + effectNames.get(s));
			return effectNames.get(s);
		}else{
			return -1;
		}
	}
	
	public int getMusic(String s){
		if(musicNames.containsKey(s)){
			System.out.println("NAMED: " + s + " NUMBER: " + musicNames.get(s));
			return musicNames.get(s);
		}else{
			return -1;
		}
	}
	
	public void removeCommand(Command c){
		Command remove = null;
		for(Command cmmd : commands){
			if(cmmd.getId().equals(c.getId())){
				remove = cmmd;
			}
		}
		commands.remove(remove);
	}
	
	public void removeCommand(String id){
		Command remove = null;
		for(Command cmmd : commands){
			if(cmmd.getId().equals(id)){
				remove = cmmd;
			}
		}
		commands.remove(remove);
	}
	
	public HashMap<Integer, String> getEffects(){
		return effects;
	}
	
	public HashMap<Integer, String> getMusic(){
		return music;
	}
	
	public String getLineMusic(int i){
		return  i + " " + music.get(new Integer(i)) +  " " + getNameMusic(i);
	}
	
	public String getLineEffect(int i){
		return  i + " " + effects.get(new Integer(i)) +  " " + getNameEffect(i);
	}
	
	
	
	
	
}

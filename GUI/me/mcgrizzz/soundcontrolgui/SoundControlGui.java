package me.mcgrizzz.soundcontrolgui;

import java.io.File;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SoundControlGui extends Application{
	
	File storage;
	
	static Gui g;
	static SoundControlGui me;
	
	ObservableList<String> mNames;
	ObservableList<String> eNames;
	
	public SoundControlGui(){
		me = this;
		g = new Gui();
		
	}
	
	public static void main(String[] args){
		new SoundControlGui();
		launch(args);
	}
	
	
	
	public enum ActionType{
		DAMAGED("when a player is damaged", 0),
		DEATH("when a player dies", 1),
		KILL("when a player kills another", 2),
		ENCHANT("when a player enchants an item", 3),
		DAMAGE("when a player damages another", 4),
		JOIN("when a player joins the server", 5),
		LEAVE("when a player leaves the server", 6),
		LEAVE_REGION("when a player leaves a region", 7),
		JOIN_REGION("when a player joins a region", 8),
		MESSAGE_SENT("when a player sends a message via chat", 9),
		MESSAGE_RECEIVE("when a player receives a message via chat", 10),
		CMD("when a player executes a command", 11);
		
		private String description;
		private int id;
		
		ActionType(String description, int id){
			this.description = description;
			this.id = id;
		}
		
		public String getDescription(){
			return this.description;
		}
		
		public int getId(){
			return this.id;
		}
		
		public static ActionType fromId(int id){
			for(ActionType type : values()){
				if(type.getId() == id){
					return type;
				}
			}
			return null;
		}
		
		public static ActionType fromDescription(String s){
			for(ActionType type : values()){
				if(type.getDescription().equalsIgnoreCase(s)){
					return type;
				}
			}
			return null;
		}
	}
	
	public enum ConditionType{
		DEFAULT("No extra conditions", 0),
		RAIN("Only play when it's raining", 1),
		FIGHT("Only play when the player is fighting", 2),
		REGION("Only play when the player is within a region -->", 3);
		
		private String description;
		private int id;
		
		ConditionType(String description, int id){
			this.description = description;
			this.id = id;
		}
		
		public String getDescription(){
			return this.description;
		}
		
		public int getId(){
			return this.id;
		}
		
		public static ConditionType fromId(int id){
			for(ConditionType type : values()){
				if(type.getId() == id){
					return type;
				}
			}
			return null;
		}
		
		public static ConditionType fromDescription(String s){
			for(ConditionType type : values()){
				if(type.getDescription().equalsIgnoreCase(s)){
					return type;
				}
			}
			return null;
		}
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.getIcons().add(new Image(getClass().getResource( "icon.png" ).toExternalForm()));
		g.promptDirectoryChoice(primaryStage);
	}

}

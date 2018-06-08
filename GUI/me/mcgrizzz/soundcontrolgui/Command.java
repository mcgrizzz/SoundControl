package me.mcgrizzz.soundcontrolgui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import me.mcgrizzz.soundcontrolgui.SoundControlGui.ActionType;
import me.mcgrizzz.soundcontrolgui.SoundControlGui.ConditionType;

public class Command implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3226745824686526302L;
	
	
	int[] args = new int[4];
	//0 = region/action 1 = actionType / regionName 2 = ConditionType 3 = id;
	String regionCondition = "default";
	String regionDefined = "default"; //This is if it's a region command
	
	public Command(){
		
	}
	
	public void changeValue(int index, int value){
		args[index] = value;
	}
	
	public void changeRegionCondition(String s){
		this.regionCondition = s;
	}
	
	public void changeRegionId(String s){
		this.regionDefined = s;
	}
	
	public int getValue(int index){
		return args[index];
	}
	
	public String getId(){
		return args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + regionCondition + " " + regionDefined; 
	}
	
	
	public String getString(){
		String s = "";
		if(args[0] == 0){
			//region
			s += "region ";
			s += regionDefined + " ";
		}else{
			s += "action ";
			s += ActionType.fromId(args[1]).toString().toLowerCase() + " ";
		}
		ConditionType t = ConditionType.fromId(args[2]);
		if(t == ConditionType.REGION){
			s += t.toString().toLowerCase() + ":" + regionCondition;
		}else{
			s += t.toString().toLowerCase();
		}
		
		return s += " " +  args[3];
		
	}
	
	public Node getNode(){
		
		
		//delete.setStyle();
		//delete.setText("");
		
		Button playy = new Button();
		playy.setText("Play preview");
		
	    playy.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				try{
					Storage s = SoundControlGui.g.storage;
					String url = (args[0]==0) ? s.music.get(args[3]) : s.effects.get(args[3]);
					if(url != null){
						if(Desktop.isDesktopSupported())
						{
						  Desktop.getDesktop().browse(new URI(url));
						}else{
							Runtime runtime = Runtime.getRuntime();
				            try {
				                runtime.exec("xdg-open " + url);
				            } catch (IOException e) {
				                // TODO Auto-generated catch block
				                e.printStackTrace();
				            }
						}
					}
					
				}catch(Exception e){
					AlertBox.display("Error playing preview!", e.getMessage());
				}
				
			}
			
		});
		Button delete = new Button();
		
		TextField region = new TextField();
		region.setText(regionDefined);
		region.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldVal, String newVal) {
	              changeRegionId(newVal);
	              System.out.println(getString());
	             }
	         });
		
		TextField regionCon = new TextField();
		regionCon.setText(regionCondition);
		regionCon.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldVal, String newVal) {
	              changeRegionCondition(newVal);
	              System.out.println(getString());
	             }
	         });
		
		
		HBox box = new HBox(8);
		
		Label define = new Label("Play " + (args[0] == 0 ? "music " : "a sound "));
		box.getChildren().add(define);
		
		ConditionType t = ConditionType.fromId(args[2]);
		List<String> lists = new ArrayList<String>();
		if(args[0] == 0){
			for(ConditionType te : ConditionType.values()){
				if(te == ConditionType.REGION)continue;
				lists.add(te.getDescription());
			}
		}else{
			for(ConditionType te : ConditionType.values()){
				lists.add(te.getDescription());
			}
		}
		
		ObservableList<String> optionss = FXCollections.observableArrayList(lists);
		ComboBox<String> comboBoxs = new ComboBox<String>(optionss);
		comboBoxs.setValue(t.getDescription());
		
		comboBoxs.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue observable, String oldVal, String newVal) {
	               ConditionType old = ConditionType.fromDescription(oldVal);
	               ConditionType newV = ConditionType.fromDescription(newVal);
	               
	               if(old != ConditionType.REGION){
	            	   if(newV == ConditionType.REGION){
	            		   //add the textbox
	            		   box.getChildren().add(box.getChildren().indexOf(delete)-2, regionCon);
	            		   SoundControlGui.g.window.sizeToScene();
	            	   }
	               }else{
	            	   if(newV != ConditionType.REGION){
	            		   //remove textbox and label
	            		   box.getChildren().remove(regionCon);
	            		   SoundControlGui.g.window.sizeToScene();
	            	   }
	               }
	               if(newV != null){
	            	   System.out.println("CHANING CONDITION TYPE TO : " + newV); 
	            	   changeValue(2, newV.getId());
	            	   System.out.println(getString());
	               }
	               
	             }
	         });
		
		if(args[0] == 0){
			Label inRegion = new Label("in region ");
			box.getChildren().add(inRegion);
			box.getChildren().add(region);
			
		}else{
			List<String> list = new ArrayList<String>();
			for(ActionType type : ActionType.values()){
				list.add(type.getDescription());
			}
				ObservableList<String> options = FXCollections.observableArrayList(list);
				ComboBox<String> comboBox = new ComboBox<String>(options);
				comboBox.setValue(ActionType.fromId(args[1]).getDescription());
				
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					
			        @Override public void changed(ObservableValue ov, String from, String to) {
			        	ActionType f = ActionType.fromDescription(from);
			        	ActionType too = ActionType.fromDescription(to);
			        	
			        	if(f != ActionType.LEAVE_REGION && f != ActionType.JOIN_REGION){
			        		if(too == ActionType.LEAVE_REGION || too == ActionType.JOIN_REGION){
			        			//need to add the label and textbox
			        			comboBoxs.setValue(ConditionType.REGION.getDescription());
			        			//comboBoxs.setEditable(false);
			        			comboBoxs.setDisable(true);
			        		}
			        	}else{
			        		if(too != ActionType.LEAVE_REGION && too != ActionType.JOIN_REGION){
			        			//need to remove text box and label
			        			comboBoxs.setValue(ConditionType.DEFAULT.getDescription());
			        			comboBoxs.setDisable(false);
			        		}
			        	}
			        	System.out.println("CHANING ACTION TYPE TO : " + too); 
			        	changeValue(1, too.getId());
			        	System.out.println(getString());
			        }    
			    });
				box.getChildren().add(comboBox);
				
				
		}
		
		
		
		
		box.getChildren().add(comboBoxs);
		
		
		delete.setShape(null);
		delete.setPadding(new Insets(-1, -1, -1, -1));
		delete.setAlignment(Pos.BASELINE_RIGHT);
		delete.getStylesheets().add(getClass().getResource("delete.css").toExternalForm());
		delete.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				TilePane pane = (TilePane) box.getParent();
				pane.getChildren().remove(box);
				SoundControlGui.g.storage.removeCommand(getId());
			}
			
		});
		
		if(t == ConditionType.REGION){
			box.getChildren().add(regionCon);
		}
		
		ComboBox<String> play;
		Label playWhat = new Label(" play ");
		
		if(args[0] == 0){
			play = new ComboBox<String>(SoundControlGui.me.mNames);
			String current = SoundControlGui.g.storage.getNameMusic(args[3]);
			if(current != null){
				play.setValue(current);
			}
			
			play.valueProperty().addListener(new ChangeListener<String>() {
	            public void changed(ObservableValue observable, String oldVal, String newVal) {
	            	System.out.println("old " + oldVal);
	            	System.out.println("new " + newVal);
		            if(newVal != null && newVal.equals("Add new music")){
		            	MediaRequest req = new MediaRequest(){

							@Override
							public void complete(String label, String url) {
								System.out.println("LABEL : " + label);
								//play.setValue(label);
								SoundControlGui.g.storage.addMusic(url, label);
							}};
							req.display("Enter music", false);
		            	}else if(newVal != null){
		            		changeValue(3, SoundControlGui.g.storage.getMusic(newVal));
		            	}
	            	
		             }
		         });
		}else{
			play = new ComboBox<String>(SoundControlGui.me.eNames);
			String current = SoundControlGui.g.storage.getNameEffect(args[3]);
			if(current != null){
				play.setValue(current);
			}
			play.valueProperty().addListener(new ChangeListener<String>() {
	            public void changed(ObservableValue observable, String oldVal, String newVal) {
	            	System.out.println("old " + oldVal);
	            	System.out.println("new " + newVal);
		            if(newVal != null && newVal.equals("Add new sound effect")){
		            		MediaRequest req = new MediaRequest(){

								@Override
								public void complete(String label, String url) {
									System.out.println("LABEL : " + label);
									//play.setValue(label);
									SoundControlGui.g.storage.addEffect(url, label);
									
								}};
								
		            		req.display("Enter sound effect", true);
		            		
		            	}else if(newVal != null){
		            		changeValue(3, SoundControlGui.g.storage.getEffect(newVal));
		            	}
	            	
		             }
		         });
			
		}
		
		
		box.getChildren().add(playWhat);
		box.getChildren().add(play);
		box.getChildren().add(playy);
		box.getChildren().add(delete);
		
		
		box.setBackground(Background.EMPTY);
		
		return box;
	}
	
	

}

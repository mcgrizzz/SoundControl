package me.mcgrizzz.soundcontrolgui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Gui implements EventHandler<ActionEvent>{
	
	File directory;
	
	Storage storage;
	
	Stage window;
	
	Scene prompt;
	Scene editCommands;
	
	Button exportLines;
	
	Button commandEdit;
	Button export;
	Button export2;
	Button save;
	
	TilePane commands;
	
	public Gui(){
		
		
		setUpVariables();
	}
	
	public void setUpVariables(){
		
		/*
		 * Initial Prompt
		 * 
		 */
		
		HBox box = new HBox();
		
		commandEdit = new Button();
		commandEdit.setText("Edit your commands");
		commandEdit.setPadding(new Insets(20, 20, 20, 20));
		commandEdit.setOnAction(this);
		commandEdit.setLineSpacing(10);
		
		export = new Button();
		export.setText("Export your configuration");
		export.setPadding(new Insets(20, 20, 20, 20));
		export.setOnAction(this);
		export.setLineSpacing(10);
		
		box.getChildren().addAll(commandEdit, export);
		box.setAlignment(Pos.CENTER);
		
		
		prompt = new Scene(box , 350, 250);
		prompt.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
		prompt.setFill(Color.DARKGREY);
		
		
	}
	
	
	public void promptDirectoryChoice(Stage stage){
		window = stage;
		
		window.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                       save();
                    }
                });
            }
        });
		
		window.setResizable(false);
		
		AlertBox.display("Choose a directory", "Browse to the SoundControl plugin directory!");
		
		window.setTitle("Browse to Directory");
		
		Preferences prefs = Preferences.userNodeForPackage(Gui.class);
		String path = prefs.get("directoryBase", "");
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose the SoundControl Directory");
		if(!path.isEmpty()){
			File f = new File(path);
			chooser.setInitialDirectory(f);
		}
		
		directory = chooser.showDialog(window);
		prefs.put("directoryBase", directory.getAbsolutePath());
		
		if(directory != null){
			prompt();
		}else if(directory == null){
			AlertBox.display("Closing Program", " No directory choosen, closing program! \n Thanks for using SoundControl \n Written by: Drepic");
		}
		
	}
	
	public void exportToPlugin(){
		if(window.getScene() == prompt){
			window.close();
		}
		
		/*Command c = new Command();
		c.changeRegionId("test");
		c.changeValue(0, 0);
		c.changeValue(1, 0);
		c.changeValue(2, 1);
		c.changeValue(3, 0);
		
		Command c1 = new Command();
		c1.changeRegionCondition("test");
		c1.changeValue(0, 1);
		c1.changeValue(1, 2);
		c1.changeValue(2, 3);
		c1.changeValue(3, 0);
		
		storage.addCommand(c);
		storage.addCommand(c1);
		
		storage.addMusic("url", "testname");
		storage.addEffect("url", "effectname");*/
		
		String commands = "";
		String music = "";
		String effects = "";
		
		for(Command b : storage.commands){
			commands += b.getString() + "\n";
		}
		
		for(int i : storage.getEffects().keySet()){
			effects += storage.getLineEffect(i) + "\n";
		}
		
		for(int i : storage.getMusic().keySet()){
			music += storage.getLineMusic(i) + "\n";
		}
		
		writeToFile("commands.txt", commands);
		writeToFile("effects.txt", effects);
		writeToFile("music.txt", music);
		
		AlertBox.display("Saved", " All commands exported");
		
	}
	
	public void writeToFile(String s, String writing){
		try {

			String content = writing;

			File file = new File(directory, s);
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				directory.mkdir();
				file.createNewFile();
			}
			
			System.out.println(file);
			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void save(){
		for(Command c : storage.commands){
			System.out.println(c.getString());
			System.out.println(c.getId());
		}
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
 
        File f = new File(directory, "soundcontrol.storage");
        File dir = directory;
 
        try {
                dir.mkdirs();
                f.createNewFile();
        } catch (IOException e1) {
                e1.printStackTrace();
            }
 
        try{
               fout = new FileOutputStream(f);
               oos = new ObjectOutputStream(fout);
               oos.writeObject(storage);
        } catch (Exception e) {
               e.printStackTrace();
        }finally {
               if(oos  != null){
                   try {
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
 
    public boolean load(){
 
        File f = new File(directory, "soundcontrol.storage");
       
        storage = new Storage();
        
        if(!f.exists()){
        	return false;
        }
        
        try {
            FileInputStream streamIn = new FileInputStream(f);
           ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
           storage = (Storage)objectinputstream.readObject();
 
           objectinputstream.close();
 
       } catch (Exception e) {
    	   
           e.printStackTrace();
    }
        
        storage.transfer();
 
        return true;
    }
    
	
	public void prompt(){
		if(load()){
			AlertBox.display("Loaded commands", " Commands have been loaded from previous session");
		}else{
			AlertBox.display("New commands", " No previous session has been found");
		}
		openCommands();
	}
	
	public void openCommands(){
		
		/*
		 * Command Editor
		 * 
		 */
		
		BorderPane pp = new BorderPane();
		pp.setPadding(new Insets(10, 10, 10, 10));
		//pp.setVgap(20);
		
		
		
		ScrollPane pane = new ScrollPane();
		pane.setFitToWidth(true);
		pane.setStyle("-fx-background-color: #383838;");
		//pane.setMaxHeight(350);
		commands = new TilePane();	
		//commands.setPadding(new Insets(10, 0, 10, 10));
		commands.setVgap(20);
		commands.setPrefColumns(1);
		

		HBox buttons = new HBox(5);
		//buttons.setSpacing(5);
		
		for(Command c : storage.commands){
			 commands.getChildren().add(c.getNode());
		}
		
		export2 = new Button();
		export2.setText("Export your configuration");
		export2.setOnAction(this);
		export2.setLineSpacing(10);
		buttons.getChildren().add(export2);
		
		
		
		save = new Button();
		save.setText("Save");
		save.setOnAction(this);
		save.setLineSpacing(10);
		buttons.getChildren().add(save);
		
		Button newCommand = new Button();
		newCommand.setText("New command");
		newCommand.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				
				alert.setTitle("Add Command");
				alert.setHeaderText("Add a custom command");
				alert.setContentText("What kind of command do you want to add ?");

				ButtonType buttonTypeOne = new ButtonType("A region command");
				ButtonType buttonTypeTwo = new ButtonType("An Action Command");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne){
				    // ... user chose "One"
					Command c = new Command();
					c.changeValue(0, 0);
					c.changeValue(1, 0);
					c.changeValue(2, 0);
					c.changeValue(3, 0);
					storage.addCommand(c);
					commands.getChildren().add(c.getNode());
					window.sizeToScene();
					//pane.setContent(commands);
				} else if (result.get() == buttonTypeTwo) {
				    // ... user chose "Two"
					Command c = new Command();
					c.changeValue(0, 1);
					c.changeValue(1, 0);
					c.changeValue(2, 0);
					c.changeValue(3, 0);
					storage.addCommand(c);
					commands.getChildren().add(c.getNode());
					window.sizeToScene();
					//pane.setContent(commands);
				} else {
				    // ... user chose CANCEL or closed the dialog
				}
			}
			
		});
		
		
		buttons.getChildren().add(newCommand);
		buttons.setAlignment(Pos.BOTTOM_RIGHT);
		
		
		
		pane.setContent(commands);
		pp.setMaxHeight(600);
		pp.setPrefWidth(1150);
		
		pp.setCenter(pane);
		pp.setBottom(buttons);
		
		editCommands = new Scene(pp);
		editCommands.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
		
		
		
		window.close();
		window.setScene(editCommands);
		window.setTitle("Edit commands");
		window.show();
	}
	
	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == commandEdit){
			/*if(load()){
				AlertBox.display("Loaded commands", " Commands have been loaded from previous session");
			}else{
				AlertBox.display("New commands", " No previous session has been found");
			}
			openCommands();
			
		}else if(e.getSource() == export){
			load();
			exportToPlugin();
		*/}else if(e.getSource() == export2){
			save();
			exportToPlugin();
		}else if(e.getSource() == save){
			save();
			AlertBox.display("Saved", " The Session has been saved!");
		}
	}

}

package me.mcgrizzz.soundcontrolgui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class MediaRequest {
	
	public void display(String title, boolean effect) {
        final Stage window = new Stage();
        window.getIcons().add(new Image(AlertBox.class.getResource( "icon.png" ).toExternalForm()));
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(450);
        window.setMinHeight(250);
        
        TextField name = new TextField();
        name.setPromptText("Enter " + (effect ? "sound effect " : "music ") + "name");
        TextField url = new TextField();
        url.setPromptText("Enter " + (effect ? "sound effect " : "music ") + "url");
       
        
        
        Button closeButton = new Button("Cancel");
        closeButton.setOnAction(e -> window.close());
        
        Button saveButton = new Button("Save");
        final MediaRequest req = this;
        saveButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(!(name.getText().isEmpty() || name.getText().equals(url.getPromptText())) && !(url.getText().isEmpty() || url.getText().equals(url.getPromptText()))){
					req.complete(name.getText(), url.getText());
					window.close();
				}else{
					AlertBox.display("Empty", "The name and url of the media may not be empty!");
				}
			}
        	
        });
        
        if(effect){
        	 name.textProperty().addListener(new ChangeListener<String>(){

     			@Override
     			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
     				if(SoundControlGui.g.storage.effectNames.containsKey(newValue)){
     					saveButton.setDisable(true);
     				}else{
     					if(saveButton.isDisabled()){
     						saveButton.setDisable(false);
     					}
     				}
     			}
             	
             });
        }else{
        	 name.textProperty().addListener(new ChangeListener<String>(){

     			@Override
     			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
     				if(SoundControlGui.g.storage.musicNames.containsKey(newValue)){
     					saveButton.setDisable(true);
     				}else{
     					if(saveButton.isDisabled()){
     						saveButton.setDisable(false);
     					}
     				}
     			}
             	
             });
        }
       
        

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(0, 0, 10, 0));
        layout.getChildren().addAll(name, url, saveButton, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(AlertBox.class.getResource("Styles.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
    }
	
	public abstract void complete(String label, String url);
	
		
}



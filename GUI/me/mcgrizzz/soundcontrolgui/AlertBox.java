package me.mcgrizzz.soundcontrolgui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String title, String message) {
        final Stage window = new Stage();
        window.getIcons().add(new Image(AlertBox.class.getResource( "icon.png" ).toExternalForm()));
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(150);

        Label label = new Label();
        label.setFont(Font.font(14));
        label.setText("\n" +  message);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> window.close());
        

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(0, 0, 10, 0));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(AlertBox.class.getResource("Styles.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
    }

}

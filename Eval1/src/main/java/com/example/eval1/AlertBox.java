package com.example.eval1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public void AlertBox () {}
    public void display(String title, String body) {
        Stage popup = new Stage();

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(title);

        Label label1= new Label(body);

        Button button1= new Button("X Close");

        button1.setOnAction(e -> popup.close());

        VBox layout= new VBox(10);

        layout.getChildren().addAll(label1, button1);

        layout.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(layout, 800, 800));

        popup.showAndWait();
    }
}


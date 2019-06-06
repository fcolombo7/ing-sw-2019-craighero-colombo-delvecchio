package it.polimi.ingsw.GUI;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

public class WaitingRoomWindow {
    private static TextArea area;

    public static void create(Stage stage){
        stage.setMinHeight(500);
        stage.setMinWidth(600);
        stage.setHeight(500);
        stage.setWidth(600);
        AnchorPane waitingRoom = new AnchorPane();
        Label text = new Label("Waiting for other players...");
        Button disconnect= new Button("Disconnect");
        Label welcome = new Label();
        welcome.setText("WELCOME TO ADRENALINE");
        area = new TextArea();
        area.setDisable(true);

        welcome.getStyleClass().add("welcome");

        welcome.setLayoutX(85);
        welcome.setLayoutY(10);
        area.setPrefHeight(300);
        area.setMaxHeight(300);
        area.setPrefWidth(540);
        area.setLayoutX(30);
        area.setLayoutY(100);
        text.setLayoutX(145);
        text.setLayoutY(60);
        disconnect.setLayoutX(30);
        disconnect.setLayoutY(430);

        waitingRoom.getChildren().addAll(welcome, text, disconnect, area);
        Scene scene = new Scene(waitingRoom, 600, 500);
        stage.setTitle("Waiting room");
        stage.setScene(scene);

        disconnect.setOnAction(actionEvent -> {
            MainWindow.getConnection().logout();
            stage.close();
            LoginWindow.log(stage);});
        waitingRoom.getStylesheets().addAll(WaitingRoomWindow.class.getResource("/gui/waitingroom.css").toExternalForm());
        stage.show();
    }

    public static TextArea getArea() {
        return area;
    }
}

package it.polimi.ingsw.GUIexample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameWindow {


    public static void open(Image image){
        Stage s= new Stage();
        s.setTitle("ADRENALINE");
        ImageView map=new ImageView(image);
        map.setFitWidth(600);
        map.setFitHeight(454);
        Image playerBoard= new Image("/gui/playerBoard.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600);
        plB.setPreserveRatio(true);
        Button shoot= new Button("Shoot");
        Button grab= new Button("Grab");
        Button move= new Button("Move");
        Button loadWeapon= new Button("Load Weapon");
        Button showPlB= new Button("Show PlayerBoards");
        showPlB.setPrefWidth(150);
        showPlB.setStyle("-fx-background-color: green");
        shoot.setPrefWidth(150);
        grab.setPrefWidth(150);
        move.setPrefWidth(150);
        loadWeapon.setPrefWidth(150);
        Button b1=new Button();
        b1.setPrefWidth(80);
        b1.setPrefHeight(83);
        b1.setStyle("-fx-background-color: transparent");
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });
        Button b2=new Button();
        b2.setPrefWidth(97);
        b2.setPrefHeight(83);
        b2.setStyle("-fx-background-color: transparent");
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b3=new Button();
        b3.setPrefWidth(95);
        b3.setPrefHeight(80);
        b3.setStyle("-fx-background-color: transparent");
        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        showPlB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PlBs.showPlBs();
            }
        });
        Button b4=new Button();
        b4.setPrefWidth(68);
        b4.setPrefHeight(65);
        b4.setStyle("-fx-background-color: transparent");
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b5=new Button();
        b5.setPrefWidth(82);
        b5.setPrefHeight(84);
        b5.setStyle("-fx-background-color: transparent");
        b5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b6=new Button();
        b6.setPrefWidth(94);
        b6.setPrefHeight(86);
        b6.setStyle("-fx-background-color: transparent");
        b6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });
        Button b7=new Button();
        b7.setPrefWidth(77);
        b7.setPrefHeight(99);
        b7.setStyle("-fx-background-color: transparent");
        b7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b8=new Button();
        b8.setPrefWidth(78);
        b8.setPrefHeight(91);
        b8.setStyle("-fx-background-color: transparent");
        b8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b9=new Button();
        b9.setPrefWidth(98);
        b9.setPrefHeight(86);
        b9.setStyle("-fx-background-color: transparent");
        b9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b10=new Button();
        b10.setPrefWidth(82);
        b10.setPrefHeight(83);
        b10.setStyle("-fx-background-color: transparent");
        b10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });
        Button b11=new Button();
        b11.setPrefWidth(84);
        b11.setPrefHeight(90);
        b11.setStyle("-fx-background-color: transparent");
        b11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });



        AnchorPane gp= new AnchorPane();
        gp.getChildren().addAll(map, plB, shoot, grab, move, loadWeapon, showPlB, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11);

        map.setLayoutX(0);
        map.setLayoutY(0);
        plB.setLayoutY(454);
        plB.setLayoutX(0);
        shoot.setLayoutX(620);
        shoot.setLayoutY(50);
        grab.setLayoutY(100);
        grab.setLayoutX(620);
        move.setLayoutX(620);
        move.setLayoutY(150);
        loadWeapon.setLayoutX(620);
        loadWeapon.setLayoutY(200);
        showPlB.setLayoutX(620);
        showPlB.setLayoutY(300);
        b1.setLayoutX(111);
        b1.setLayoutY(105);
        b2.setLayoutX(201);
        b2.setLayoutY(106);
        b3.setLayoutX(303);
        b3.setLayoutY(108);
        b4.setLayoutX(419);
        b4.setLayoutY(125);
        b5.setLayoutX(113);
        b5.setLayoutY(201);
        b6.setLayoutX(202);
        b6.setLayoutY(211);
        b7.setLayoutX(322);
        b7.setLayoutY(201);
        b8.setLayoutX(408);
        b8.setLayoutY(204);
        b9.setLayoutX(197);
        b9.setLayoutY(314);
        b10.setLayoutX(314);
        b10.setLayoutY(311);
        b11.setLayoutX(405);
        b11.setLayoutY(301);

        //GridPane.setColumnIndex(map, 0);
        //GridPane.setRowIndex(map, 0);
        //GridPane.setColumnIndex(plB, 0);
        //GridPane.setRowIndex(plB, 1);
        //GridPane.setColumnIndex(butt, 0);
        //GridPane.setRowIndex(butt, 0);
        //GridPane.setColumnIndex(shoot, 1);
        //GridPane.setRowIndex(shoot, 0);
        //GridPane.setColumnIndex(grab, 2);
        //GridPane.setRowIndex(grab, 0);
        //GridPane.setColumnIndex(move, 3);
        //GridPane.setRowIndex(move,0 );

        Scene scene=new Scene(gp, 800, 600);
        scene.getStylesheets().addAll(GameWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Bye.byebye();
                s.close();
            }
        });
        s.show();

    }
}

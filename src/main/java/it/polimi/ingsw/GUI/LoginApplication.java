package it.polimi.ingsw.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApplication extends Application {

    int players=3;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Login");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(700);
        primaryStage.setHeight(500);
        primaryStage.setWidth(700);
        RadioButton rmi= new RadioButton("RMI");
        RadioButton sock= new RadioButton("Socket");
        ToggleGroup connectionGroup= new ToggleGroup();
        rmi.setToggleGroup(connectionGroup);
        sock.setToggleGroup(connectionGroup);
        sock.setSelected(true);
        Button loginButton= new Button("Login");

        Label nameText=new Label("Player name");
        //nameText.setPadding(new Insets(50,0,0,0));
        TextField playerNameField= new TextField();
        TextField mottoField= new TextField();
        Label mottoText= new Label("Motto");
        Label lb0= new Label("");
        Label lb1= new Label("");
        playerNameField.setPrefWidth(250);
        mottoField.setPrefWidth(250);
        lb0.setPrefWidth(300);
        lb1.setPrefWidth(300);
        lb1.setStyle("-fx-text-fill: red");
        Label title= new Label("");
        title.setPrefWidth(700);
        title.setPrefHeight(122);
        title.getStyleClass().add("title");
        AnchorPane grid= new AnchorPane();
        grid.getChildren().add(nameText);
        grid.getChildren().add(mottoText);
        grid.getChildren().add(playerNameField);
        grid.getChildren().add(mottoField);
        grid.getChildren().add(rmi);
        grid.getChildren().add(sock);
        grid.getChildren().add(loginButton);
        grid.getChildren().add(lb0);
        grid.getChildren().add(lb1);
        grid.getChildren().add(title);

        nameText.setLayoutX(20);
        nameText.setLayoutY(170);
        mottoText.setLayoutX(20);
        mottoText.setLayoutY(210);
        playerNameField.setLayoutX(200);
        playerNameField.setLayoutY(170);
        mottoField.setLayoutX(200);
        mottoField.setLayoutY(210);
        lb1.setLayoutX(200);
        lb1.setLayoutY(250);
        loginButton.setLayoutX(500);
        loginButton.setLayoutY(240);
        rmi.setLayoutX(20);
        rmi.setLayoutY(370);
        sock.setLayoutX(150);
        sock.setLayoutY(370);
        lb0.setLayoutX(20);
        lb0.setLayoutY(410);
        title.setLayoutX(0);
        title.setLayoutY(0);


        //GridPane.setColumnIndex(title,1);
        //GridPane.setRowIndex(title, 0);
      //  GridPane.setColumnIndex(nameText,0);
        //GridPane.setRowIndex(nameText, 1);
        //GridPane.setColumnIndex(mottoText, 0);
        //GridPane.setRowIndex(mottoText, 2);
        //GridPane.setColumnIndex(playerNameField, 1);
        //GridPane.setColumnIndex(mottoField, 1);
        //GridPane.setRowIndex(playerNameField, 1);
        //GridPane.setRowIndex(mottoField, 2);
        //GridPane.setColumnIndex(loginButton, 2);
        //GridPane.setRowIndex(loginButton, 3);
        //GridPane.setColumnIndex(lb0, 1);
        //GridPane.setRowIndex(lb0, 5);
        //GridPane.setColumnIndex(lb1,1);
        //GridPane.setRowIndex(lb1, 3);
        //GridPane.setColumnIndex(rmi, 0);
        //GridPane.setRowIndex(rmi, 4);
        //GridPane.setColumnIndex(sock, 1);
        //GridPane.setRowIndex(sock, 4);
        //GridPane.setHgrow(rmi, Priority.ALWAYS);
        //GridPane.setHgrow(sock, Priority.ALWAYS);
        //GridPane.setHgrow(loginButton, Priority.ALWAYS);
        //GridPane.setHgrow(nameText, Priority.ALWAYS);
        //GridPane.setHgrow(playerNameField, Priority.ALWAYS);
        //GridPane.setHgrow(mottoText, Priority.ALWAYS);
        //GridPane.setHgrow(mottoField, Priority.ALWAYS);
        //GridPane.setHgrow(lb0, Priority.ALWAYS);
        //GridPane.setHgrow(lb1, Priority.ALWAYS);
        //GridPane.setVgrow(rmi, Priority.ALWAYS);
        //GridPane.setVgrow(sock, Priority.ALWAYS);
        //GridPane.setVgrow(loginButton, Priority.ALWAYS);
        //GridPane.setVgrow(nameText, Priority.ALWAYS);
        //GridPane.setVgrow(playerNameField, Priority.ALWAYS);
        //GridPane.setVgrow(mottoText, Priority.ALWAYS);
        //GridPane.setVgrow(mottoField, Priority.ALWAYS);
        //GridPane.setVgrow(lb0, Priority.ALWAYS);
        //GridPane.setVgrow(lb1, Priority.ALWAYS);



        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(playerNameField.getText().isEmpty()){
                    String msg="Please enter your name";
                    lb1.setText(msg);
                }
                else if(mottoField.getText().isEmpty()){
                    String msg="Please enter your motto";
                    lb1.setText(msg);
                }
                else{
                    if(!playerNameField.getText().equalsIgnoreCase("gino")&&players<4){
                        players=players+1;
                        String msg="";
                        lb1.setText(msg);
                        AnchorPane waitingRoom = new AnchorPane();
                        Label text = new Label("Waiting for other players...");
                        Button disconnect= new Button("Disconnect");
                        Button rules=new Button("Show rules");
                        Label welcome = new Label();
                        welcome.setText("WELCOME TO ADRENALINE");
                        Label plOnline= new Label("Logged Players:");
                        plOnline.setPrefHeight(100);
                        plOnline.setPrefWidth(150);
                        Button refresh=new Button("Refresh");
                        refresh.setPrefWidth(100);
                        refresh.setPrefHeight(25);

                        refresh.getStyleClass().add("refr");


                        welcome.setLayoutX(20);
                        welcome.setLayoutY(10);
                        text.setLayoutX(25);
                        text.setLayoutY(260);
                        disconnect.setLayoutX(20);
                        disconnect.setLayoutY(120);
                        rules.setLayoutX(210);
                        rules.setLayoutY(120);
                        plOnline.setLayoutX(20);
                        plOnline.setLayoutY(300);
                        refresh.setLayoutX(170);
                        refresh.setLayoutY(400);



                        waitingRoom.getChildren().addAll(welcome, text, disconnect, rules, plOnline, refresh);
                        Scene secondScene = new Scene(waitingRoom, 300, 500);

                        Stage newWindow = new Stage();
                        newWindow.setTitle("Waiting room");
                        newWindow.setScene(secondScene);

                        rules.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                Rules.showRules();
                            }
                        });

                        disconnect.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                newWindow.close();
                                primaryStage.show();
                                players=players-1;
                            }
                        });

                        newWindow.initStyle(StageStyle.UNDECORATED);
                        newWindow.initModality((Modality.WINDOW_MODAL));
                        newWindow.initOwner(primaryStage);
                        primaryStage.hide();
                        newWindow.setX(primaryStage.getX() + 100);
                        newWindow.setY(primaryStage.getY() + 50);
                        newWindow.setResizable(false);
                        waitingRoom.getStylesheets().addAll(this.getClass().getResource("/gui/waitingroom.css").toExternalForm());
                        newWindow.show();
                    } else if(players<4){
                        String msg="Name already used";
                        lb1.setText(msg);
                    } else {
                        if(players==4) {
                            primaryStage.close();
                            MapChoice.display();
                        } else {
                            StackPane tooPlayers= new StackPane();
                            Label text= new Label("Sorry, too many players in game");
                            tooPlayers.getChildren().add(text);
                            Scene tooScene= new Scene(tooPlayers, 300, 300);
                            Stage tooWindow= new Stage();
                            tooWindow.setScene(tooScene);
                            tooWindow.initModality(Modality.WINDOW_MODAL);
                            tooWindow.initOwner(primaryStage);
                            tooWindow.show();
                        }
                    }

                }
            }
        });

        rmi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String msg="";
                if(rmi.isSelected()){
                    msg= "RMI connection selected";
                }
                lb0.setText(msg);
            }
        });

        sock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String msg="";
                if(sock.isSelected()){
                    msg= "Socket connection selected";
                }
                lb0.setText(msg);
            }
        });
        primaryStage.setResizable(false);
        Scene scene=new Scene(grid, 700, 500);
        scene.getStylesheets().addAll(this.getClass().getResource("/gui/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
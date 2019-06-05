package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

//login
public class LoginWindow {

    private  static int players=4;

    public static Label plOnline;

    public static ArrayList<String> pl= new ArrayList<String>(){

        public String printList(int c){
            int i=0;
            String playerss= new String();
            while(i<c+1){
                playerss=("\n" + pl.get(i));
            }
            return playerss;
        }
    };

    public static void log(Stage s){
        Stage primaryStage= new Stage();
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
                    if(!pl.contains(playerNameField.getText())){
                        String namePl=playerNameField.getText();
                        pl.add(namePl);
                        players=players+1;
                        String msg="";
                        lb1.setText(msg);
                        AnchorPane waitingRoom = new AnchorPane();
                        Label text = new Label("Waiting for other players...");
                        Button disconnect= new Button("Disconnect");
                        Button rules=new Button("Show rules");
                        Label welcome = new Label();
                        welcome.setText("WELCOME TO ADRENALINE");
                        Label tit= new Label("Logged Players:");
                        plOnline= new Label(pl.toString());
                        tit.setPrefWidth(150);
                        tit.setPrefHeight(20);
                        plOnline.setPrefHeight(50);
                        plOnline.setPrefWidth(400);
                        Button refresh=new Button("Refresh");
                        refresh.setPrefWidth(80);
                        refresh.setPrefHeight(25);
                        welcome.getStyleClass().add("welcome");
                        Image hourglass= new Image("/gui/hourglass.jpg");
                        ImageView hg= new ImageView(hourglass);

                        plOnline.getStyleClass().add("ply");
                        refresh.getStyleClass().add("refr");


                        welcome.setLayoutX(115);
                        welcome.setLayoutY(10);
                        text.setLayoutX(175);
                        text.setLayoutY(280);
                        disconnect.setLayoutX(40);
                        disconnect.setLayoutY(120);
                        rules.setLayoutX(430);
                        rules.setLayoutY(120);
                        tit.setLayoutX(20);
                        tit.setLayoutY(360);
                        plOnline.setLayoutX(20);
                        plOnline.setLayoutY(390);
                        refresh.setLayoutX(20);
                        refresh.setLayoutY(450);
                        hg.setLayoutX(242);
                        hg.setLayoutY(60);



                        waitingRoom.getChildren().addAll(welcome, text, disconnect, rules, plOnline, refresh, tit, hg);
                        Scene secondScene = new Scene(waitingRoom, 600, 500);

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
                                pl.remove(namePl);
                                newWindow.close();
                                primaryStage.show();
                                players=players-1;
                            }
                        });

                        refresh.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                plOnline.setText(pl.toString());
                            }
                        });



                        newWindow.initStyle(StageStyle.UNDECORATED);
                        //newWindow.initModality((Modality.WINDOW_MODAL));
                        //newWindow.initOwner(primaryStage);
                        //primaryStage.hide();
                        newWindow.setX(primaryStage.getX());
                        newWindow.setY(primaryStage.getY());
                        newWindow.setResizable(false);
                        waitingRoom.getStylesheets().addAll(this.getClass().getResource("/gui/waitingroom.css").toExternalForm());
                        newWindow.show();
                    } else {
                        String msg="Name already used";
                        lb1.setText(msg);
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
        scene.getStylesheets().addAll(LoginWindow.class.getResource("/gui/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

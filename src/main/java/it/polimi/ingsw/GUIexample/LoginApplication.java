package it.polimi.ingsw.GUIexample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApplication extends Application {

    int players=4;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Login");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setWidth(600);
        RadioButton rmi= new RadioButton("RMI");
        RadioButton sock= new RadioButton("Socket");
        ToggleGroup connectionGroup= new ToggleGroup();
        rmi.setToggleGroup(connectionGroup);
        sock.setToggleGroup(connectionGroup);
        sock.setSelected(true);
        Button loginButton= new Button("Login");

        Label nameText=new Label("Player name");
        TextField playerNameField= new TextField();
        TextField mottoField= new TextField();
        Label mottoText= new Label("Motto");
        Label lb0= new Label("");
        Label lb1= new Label("");
        lb1.setStyle("-fx-text-fill: red");
        GridPane grid= new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getChildren().add(nameText);
        grid.getChildren().add(mottoText);
        grid.getChildren().add(playerNameField);
        grid.getChildren().add(mottoField);
        grid.getChildren().add(rmi);
        grid.getChildren().add(sock);
        grid.getChildren().add(loginButton);
        grid.getChildren().add(lb0);
        grid.getChildren().add(lb1);

        GridPane.setColumnIndex(nameText,0);
        GridPane.setRowIndex(nameText, 0);
        GridPane.setColumnIndex(mottoText, 0);
        GridPane.setRowIndex(mottoText, 1);
        GridPane.setColumnIndex(playerNameField, 1);
        GridPane.setColumnIndex(mottoField, 1);
        GridPane.setRowIndex(playerNameField, 0);
        GridPane.setRowIndex(mottoField, 1);
        GridPane.setColumnIndex(loginButton, 2);
        GridPane.setRowIndex(loginButton, 2);
        GridPane.setColumnIndex(lb0, 1);
        GridPane.setRowIndex(lb0, 4);
        GridPane.setColumnIndex(lb1,1);
        GridPane.setRowIndex(lb1, 2);
        GridPane.setColumnIndex(rmi, 0);
        GridPane.setRowIndex(rmi, 3);
        GridPane.setColumnIndex(sock, 1);
        GridPane.setRowIndex(sock, 3);
        GridPane.setHgrow(rmi, Priority.ALWAYS);
        GridPane.setHgrow(sock, Priority.ALWAYS);
        GridPane.setHgrow(loginButton, Priority.ALWAYS);
        GridPane.setHgrow(nameText, Priority.ALWAYS);
        GridPane.setHgrow(playerNameField, Priority.ALWAYS);
        GridPane.setHgrow(mottoText, Priority.ALWAYS);
        GridPane.setHgrow(mottoField, Priority.ALWAYS);
        GridPane.setHgrow(lb0, Priority.ALWAYS);
        GridPane.setHgrow(lb1, Priority.ALWAYS);
        GridPane.setVgrow(rmi, Priority.ALWAYS);
        GridPane.setVgrow(sock, Priority.ALWAYS);
        GridPane.setVgrow(loginButton, Priority.ALWAYS);
        GridPane.setVgrow(nameText, Priority.ALWAYS);
        GridPane.setVgrow(playerNameField, Priority.ALWAYS);
        GridPane.setVgrow(mottoText, Priority.ALWAYS);
        GridPane.setVgrow(mottoField, Priority.ALWAYS);
        GridPane.setVgrow(lb0, Priority.ALWAYS);
        GridPane.setVgrow(lb1, Priority.ALWAYS);



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
                        StackPane waitingRoom = new StackPane();
                        Label text = new Label("Waiting for other players...");
                        Button disconnect= new Button("Disconnect");
                        Label welcome = new Label();
                        welcome.setText("WELCOME TO ADRENALINE");
                        waitingRoom.setAlignment(disconnect, Pos.CENTER);
                        waitingRoom.setAlignment(welcome, Pos.TOP_CENTER);
                        waitingRoom.setAlignment(text, Pos.BOTTOM_CENTER);

                        waitingRoom.getChildren().addAll(welcome, text, disconnect);
                        Scene secondScene = new Scene(waitingRoom, 300, 300);

                        Stage newWindow = new Stage();
                        newWindow.setTitle("Waiting room");
                        newWindow.setScene(secondScene);

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
                            //players=players+1;
                            //primaryStage.close();
                            //GridPane initialView = new GridPane();
                            //initialView.getStylesheets().addAll(this.getClass().getResource("/gui/initialView.css").toExternalForm());
                            //Scene mainScene = new Scene(initialView, 500, 500);
                            //Stage mainWindow = new Stage();
                            //mainWindow.setScene(mainScene);
                            //mainWindow.show();
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
        Scene scene=new Scene(grid, 800, 500);
        scene.getStylesheets().addAll(this.getClass().getResource("/gui/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
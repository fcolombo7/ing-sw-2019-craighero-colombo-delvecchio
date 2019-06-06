package it.polimi.ingsw.GUI;

import it.polimi.ingsw.network.client.RMIServerConnection;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.client.SocketServerConnection;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;

public class LoginWindow {

    /* TO DELETE
    private  static int players=4;

    private static Label plOnline;

    private static ArrayList<String> pl= new ArrayList<String>(){

        public String printList(int c){
            int i=0;
            String playerss= new String();
            while(i<c+1){
                playerss=("\n" + pl.get(i));
            }
            return playerss;
        }
    };

     */

    private static ToggleGroup connectionGroup;
    private static RadioButton rmi;
    private static RadioButton socket;
    private static Button loginButton;
    private static TextField nicknameField;
    private static TextField mottoField;
    private static Label connLabel;
    private static Label logLabel;
    private static AnchorPane grid;

    public static void log(Stage stage){
        stage.setTitle("Login");
        stage.setMinHeight(500);
        stage.setMinWidth(700);
        stage.setHeight(500);
        stage.setWidth(700);
        setUp();


        loginButton.setOnAction(actionEvent -> {
            if(nicknameField.getText().isEmpty()){
                String msg="Please enter your name";
                logLabel.setText(msg);
            }
            else if(mottoField.getText().isEmpty()){
                String msg="Please enter your motto";
                logLabel.setText(msg);
            }
            else{
                login();

                /*
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
                }*/
            }
        });

        rmi.setOnAction(actionEvent -> {
            String msg="";
            if(rmi.isSelected()){
                msg= "RMI connection selected";
            }
            connLabel.setText(msg);
        });

        socket.setOnAction(actionEvent -> {
            String msg="";
            if(socket.isSelected()){
                msg= "Socket connection selected";
            }
            connLabel.setText(msg);
        });

        stage.setResizable(false);
        Scene scene=new Scene(grid, 700, 500);
        scene.getStylesheets().addAll(LoginWindow.class.getResource("/gui/login.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private static void login() {
        rmi.setDisable(true);
        socket.setDisable(true);
        loginButton.setDisable(true);
        try {
            ServerConnection connection;
            if (connectionGroup.getSelectedToggle().equals(rmi))
                connection = new RMIServerConnection(MainWindow.getHostname(), MainWindow.getNetworkHandler());
            else
                connection = new SocketServerConnection(MainWindow.getHostname(), MainWindow.getNetworkHandler());
            MainWindow.setConnection(connection);
            String nickname=nicknameField.getText();
            String motto=mottoField.getText();
            String answer=connection.login(nickname,motto);
            if(answer.equalsIgnoreCase(Constants.MSG_SERVER_NEGATIVE_ANSWER)){
                logLabel.setText("Invalid nickname. Try again.");
                nicknameField.setText("");
                rmi.setDisable(false);
                socket.setDisable(false);
                loginButton.setDisable(false);
            }
            else{
                MainWindow.setLogNickname(nickname);
                logLabel.setText("Login completed.");
                MainWindow.onLoginCompleted();
            }

        } catch (IOException | URISyntaxException | NotBoundException e) {
            logLabel.setText("Network error.");
            Logger.logErr(e.getMessage());
            rmi.setDisable(false);
            socket.setDisable(false);
            loginButton.setDisable(false);
        }
    }

    private static void setUp() {
        connectionGroup= new ToggleGroup();
        rmi= new RadioButton("RMI");
        socket= new RadioButton("Socket");
        rmi.setToggleGroup(connectionGroup);
        socket.setToggleGroup(connectionGroup);
        socket.setSelected(true);

        loginButton= new Button("Login");

        Label nameText=new Label("Player name");
        nicknameField= new TextField();

        Label mottoText= new Label("Motto");
        mottoField= new TextField();

        connLabel= new Label("");
        logLabel= new Label("");

        nicknameField.setPrefWidth(250);
        mottoField.setPrefWidth(250);

        connLabel.setPrefWidth(300);
        logLabel.setPrefWidth(300);
        logLabel.setStyle("-fx-text-fill: red");

        Label title= new Label("");
        title.setPrefWidth(700);
        title.setPrefHeight(122);
        title.getStyleClass().add("title");
        grid= new AnchorPane();
        grid.getChildren().add(nameText);
        grid.getChildren().add(mottoText);
        grid.getChildren().add(nicknameField);
        grid.getChildren().add(mottoField);
        grid.getChildren().add(rmi);
        grid.getChildren().add(socket);
        grid.getChildren().add(loginButton);
        grid.getChildren().add(connLabel);
        grid.getChildren().add(logLabel);
        grid.getChildren().add(title);

        nameText.setLayoutX(20);
        nameText.setLayoutY(170);
        mottoText.setLayoutX(20);
        mottoText.setLayoutY(210);
        nicknameField.setLayoutX(200);
        nicknameField.setLayoutY(170);
        mottoField.setLayoutX(200);
        mottoField.setLayoutY(210);
        logLabel.setLayoutX(200);
        logLabel.setLayoutY(300);
        loginButton.setLayoutX(500);
        loginButton.setLayoutY(240);
        rmi.setLayoutX(20);
        rmi.setLayoutY(370);
        socket.setLayoutX(150);
        socket.setLayoutY(370);
        connLabel.setLayoutX(20);
        connLabel.setLayoutY(410);
        title.setLayoutX(0);
        title.setLayoutY(0);
    }
}

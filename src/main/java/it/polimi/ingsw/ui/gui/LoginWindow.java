package it.polimi.ingsw.ui.gui;

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

/**
 * This class represents the login window
 */
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

    /**
     * This attribute represents the group of radio buttons
     */
    private static ToggleGroup connectionGroup;
    /**
     * This attribute represents the button rmi
     */
    private static RadioButton rmi;
    /**
     * This attribute represents the button socket
     */
    private static RadioButton socket;
    /**
     * This attribute represents the button login
     */
    private static Button loginButton;
    /**
     * This attribute represents the field for the nickname
     */
    private static TextField nicknameField;
    /**
     * This attribute represents the field for the motto
     */
    private static TextField mottoField;
    /**
     * This attribute represents the label for connection message
     */
    private static Label connLabel;
    /**
     * This attribute represents the label for login message
     */
    private static Label logLabel;
    /**
     * This attribute represents the pane
     */
    private static AnchorPane grid;

    /**
     * This method open the login window
     * @param stage represents the stage for the window
     */
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
        stage.setFullScreen(false);
        Scene scene=new Scene(grid, 700, 500);
        scene.getStylesheets().addAll(LoginWindow.class.getResource("/gui/login.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method handles the login of a player
     */
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

    /**
     * This method sets up the login window
     */
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

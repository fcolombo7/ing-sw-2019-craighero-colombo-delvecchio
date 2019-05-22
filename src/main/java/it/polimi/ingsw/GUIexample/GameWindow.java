package it.polimi.ingsw.GUIexample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameWindow {


    public static void open(Image image){
        Stage s= new Stage();
        s.setTitle("ADRENALINE");
        ImageView map=new ImageView(image);
        map.setFitWidth(600);
        map.setFitHeight(450);
        Image playerBoard= new Image("/gui/playerBoard.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600);
        plB.setPreserveRatio(true);
        Button shoot= new Button("Shoot");
        Button grab= new Button("Grab");
        Button move= new Button("Move");


        GridPane gp= new GridPane();
        gp.getChildren().addAll(map, plB, shoot, grab, move);

        GridPane.setColumnIndex(map, 0);
        GridPane.setRowIndex(map, 0);
        GridPane.setColumnIndex(plB, 0);
        GridPane.setRowIndex(plB, 1);
        GridPane.setColumnIndex(shoot, 1);
        GridPane.setRowIndex(shoot, 0);
        GridPane.setColumnIndex(grab, 2);
        GridPane.setRowIndex(grab, 0);
        GridPane.setColumnIndex(move, 3);
        GridPane.setRowIndex(move,0 );

        Scene scene=new Scene(gp, 800, 600);
        scene.getStylesheets().addAll(GameWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.show();

    }
}

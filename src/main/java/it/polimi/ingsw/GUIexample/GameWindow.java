package it.polimi.ingsw.GUIexample;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameWindow {


    public static void open(){
        Stage s= new Stage();
        s.setTitle("ADRENALINE");
        GridPane gp= new GridPane();

        Scene scene=new Scene(gp, 600, 600);
        scene.getStylesheets().addAll(GameWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        s.setScene(scene);
        s.show();

    }
}

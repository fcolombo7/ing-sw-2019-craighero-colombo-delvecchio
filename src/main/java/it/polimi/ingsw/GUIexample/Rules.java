package it.polimi.ingsw.GUIexample;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Rules {
    public static void showRules(){
        Stage stg=new Stage();
        stg.setTitle("THERE ARE NO RULES");
        AnchorPane ap=new AnchorPane();
        Scene scenerules= new Scene(ap, 500, 500);
        scenerules.getStylesheets().addAll(Rules.class.getResource("/gui/rules.css").toExternalForm());
        stg.setScene(scenerules);
        stg.show();
    }
}

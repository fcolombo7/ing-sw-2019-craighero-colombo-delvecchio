package it.polimi.ingsw.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PlBs {
    public static void showPlBs(){
        Stage st=new Stage();
        Label lab=new Label("TO DO");
        AnchorPane g=new AnchorPane();
        lab.setStyle("-fx-background-color: red");
        lab.setLayoutX(100);
        g.getChildren().add(lab);
        Scene sce=new Scene(g, 300, 100);
        st.setScene(sce);
        st.show();
    }
}

package it.polimi.ingsw.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Bye {
    public static void byebye(){
        Stage st=new Stage();
        Label lab=new Label("GOODBYE LOSER");
        AnchorPane g=new AnchorPane();
        lab.setStyle("-fx-background-color: yellow");
        lab.setLayoutX(100);
        g.getChildren().add(lab);
        Scene sce=new Scene(g, 300, 100);
        st.setScene(sce);
        sce.getStylesheets().addAll(Bye.class.getResource("/gui/bye.css").toExternalForm());
        st.show();
    }
}

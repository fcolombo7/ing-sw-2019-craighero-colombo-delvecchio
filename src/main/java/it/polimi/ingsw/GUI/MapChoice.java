package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;


public class MapChoice {

    public static void display() {
        Stage stage= new Stage();
        stage.setTitle("Map choice");
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        RadioButton first= new RadioButton("First map");
        RadioButton second= new RadioButton("Second map");
        RadioButton third= new RadioButton("Third map");
        RadioButton fourth= new RadioButton("Fourth map");
        final ToggleGroup group= new ToggleGroup();
        first.setToggleGroup(group);
        second.setToggleGroup(group);
        third.setToggleGroup(group);
        fourth.setToggleGroup(group);
        first.setSelected(true);
        Button vote= new Button("Vote this map");
        Image img= new Image("/gui/map1.png");
        ImageView image= new ImageView();
        image.setImage(img);
        image.setFitWidth(400);
        image.setFitHeight(303);
        Image img2= new Image("/gui/map2.png");
        Image img3= new Image("/gui/map3.png");
        Image img4= new Image("/gui/map4.png");

        ArrayList<Image> votes= new ArrayList<Image>();

        AnchorPane grid= new AnchorPane();
        grid.getChildren().addAll(first, second, third, fourth, vote, image);
        //GridPane.setColumnIndex(first, 1);
        //GridPane.setRowIndex(first, 1);
        //GridPane.setColumnIndex(second, 1);
        //GridPane.setRowIndex(second, 2);
        //GridPane.setColumnIndex(third, 1);
        //GridPane.setRowIndex(third, 3);
        //GridPane.setColumnIndex(fourth, 1);
        //GridPane.setRowIndex(fourth, 4);
        //GridPane.setColumnIndex(vote, 2);
        //GridPane.setRowIndex(vote, 5);
        //GridPane.setColumnIndex(image, 2);
        //GridPane.setRowIndex(image, 7);

        first.setLayoutX(10);
        first.setLayoutY(5);
        second.setLayoutX(10);
        second.setLayoutY(30);
        third.setLayoutX(10);
        third.setLayoutY(55);
        fourth.setLayoutX(10);
        fourth.setLayoutY(80);
        image.setLayoutX(100);
        image.setLayoutY(150);
        vote.setLayoutX(220);
        vote.setLayoutY(90);

        first.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                image.setImage(img);
            }
        });

        second.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                image.setImage(img2);
            }
        });

        third.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                image.setImage(img3);
            }
        });

        fourth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                image.setImage(img4);
            }
        });


        vote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                votes.add(image.getImage());
                Image bestmap=votes.get(0);
                String urlmap= bestmap.getUrl();
                GameWindow.open(bestmap, urlmap);
            }
        });

        Scene scene= new Scene(grid, 600, 500);
        scene.getStylesheets().addAll(MapChoice.class.getResource("/gui/mapChoice.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

}
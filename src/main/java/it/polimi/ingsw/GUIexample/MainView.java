package it.polimi.ingsw.GUIexample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainView {

    public static void display() {
        Stage stage= new Stage();
        stage.setTitle("Map choice");
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        RadioButton first= new RadioButton("First map");
        RadioButton second= new RadioButton("Second map");
        RadioButton third= new RadioButton("Third map");
        final ToggleGroup group= new ToggleGroup();
        first.setToggleGroup(group);
        second.setToggleGroup(group);
        third.setToggleGroup(group);
        first.setSelected(true);
        Button vote= new Button("Vote this map");
        Label map= new Label("MAPPA");
        map.setStyle("-fx-background-color: blue");
        GridPane grid= new GridPane();
        grid.getChildren().addAll(first, second, third, vote, map);
        GridPane.setColumnIndex(first, 1);
        GridPane.setRowIndex(first, 1);
        GridPane.setColumnIndex(second, 1);
        GridPane.setRowIndex(second, 2);
        GridPane.setColumnIndex(third, 1);
        GridPane.setRowIndex(third, 3);
        GridPane.setColumnIndex(map, 1);
        GridPane.setRowIndex(map, 0);
        GridPane.setColumnIndex(vote, 2);
        GridPane.setRowIndex(vote, 4);

        first.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                map.setStyle("-fx-background-color: blue");
            }
        });

        second.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                map.setStyle("-fx-background-color: red");
            }
        });

        third.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                map.setStyle("-fx-background-color: yellow");
            }
        });

        Scene scene= new Scene(grid, 400, 400);
        stage.setScene(scene);
        stage.show();


    }

}
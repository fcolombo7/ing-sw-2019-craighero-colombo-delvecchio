package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameWindow {


    public static void open(Image image){
        Stage s= new Stage();
        s.setTitle("ADRENALINE");
        boolean mapp1=false;
        boolean mapp2=false;
        boolean mapp3=true;
        boolean mapp4=false;
        ImageView map=new ImageView(image);
        map.setFitWidth(600);
        map.setFitHeight(454);
        Image playerBoard= new Image("/gui/playerBoard.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600);
        plB.setPreserveRatio(true);
        Button shoot= new Button("Shoot");
        Button grab= new Button("Grab");
        Button move= new Button("Move");
        Button loadWeapon= new Button("Load Weapon");
        Button showPlB= new Button("Show PlayerBoards");
        showPlB.setPrefWidth(150);
        showPlB.setStyle("-fx-background-color: green");
        shoot.setPrefWidth(150);
        grab.setPrefWidth(150);
        move.setPrefWidth(150);
        loadWeapon.setPrefWidth(150);

        Button a1=new Button();
        a1.setPrefWidth(57);
        a1.setPrefHeight(84);
        a1.setStyle("-fx-background-color: transparent");
        a1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a2=new Button();
        a2.setPrefWidth(57);
        a2.setPrefHeight(84);
        a2.setStyle("-fx-background-color: transparent");
        a2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a3=new Button();
        a3.setPrefWidth(57);
        a3.setPrefHeight(84);
        a3.setStyle("-fx-background-color: transparent");
        a3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a4=new Button();
        a4.setPrefWidth(84);
        a4.setPrefHeight(57);
        a4.setStyle("-fx-background-color: transparent");
        a4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a5=new Button();
        a5.setPrefWidth(84);
        a5.setPrefHeight(57);
        a5.setStyle("-fx-background-color: transparent");
        a5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a6=new Button();
        a6.setPrefWidth(84);
        a6.setPrefHeight(57);
        a6.setStyle("-fx-background-color: transparent");
        a6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a7=new Button();
        a7.setPrefWidth(84);
        a7.setPrefHeight(57);
        a7.setStyle("-fx-background-color: transparent");
        a7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a8=new Button();
        a8.setPrefWidth(84);
        a8.setPrefHeight(57);
        a8.setStyle("-fx-background-color: transparent");
        a8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });

        Button a9=new Button();
        a9.setPrefWidth(84);
        a9.setPrefHeight(57);
        a9.setStyle("-fx-background-color: transparent");
        a9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Bye.byebye();
            }
        });


        Button b1=new Button();
        if(mapp1) {
            b1.setPrefWidth(80);
            b1.setPrefHeight(83);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp2) {
            b1.setPrefWidth(85);
            b1.setPrefHeight(83);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp3) {
            b1.setPrefWidth(71);
            b1.setPrefHeight(94);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp4) {
            b1.setPrefWidth(71);
            b1.setPrefHeight(94);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }



        Button b2=new Button();
        if(mapp1) {
            b2.setPrefWidth(97);
            b2.setPrefHeight(83);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp2) {
            b2.setPrefWidth(97);
            b2.setPrefHeight(84);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp3) {
            b2.setPrefWidth(96);
            b2.setPrefHeight(82);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp4) {
            b2.setPrefWidth(97);
            b2.setPrefHeight(82);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }



        Button b3=new Button();
        if(mapp1) {
            b3.setPrefWidth(95);
            b3.setPrefHeight(80);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp2) {
            b3.setPrefWidth(93);
            b3.setPrefHeight(81);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp3) {
            b3.setPrefWidth(95);
            b3.setPrefHeight(79);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp4) {
            b3.setPrefWidth(93);
            b3.setPrefHeight(80);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        Button b4=new Button();
        if(mapp1) {
            b4.setPrefWidth(68);
            b4.setPrefHeight(65);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b4.setPrefWidth(85);
            b4.setPrefHeight(95);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b4.setPrefWidth(73);
            b4.setPrefHeight(79);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b4.setPrefWidth(68);
            b4.setPrefHeight(91);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }


        Button b5=new Button();
        if(mapp1) {
            b5.setPrefWidth(82);
            b5.setPrefHeight(84);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b5.setPrefWidth(87);
            b5.setPrefHeight(90);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b5.setPrefWidth(68);
            b5.setPrefHeight(96);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b5.setPrefWidth(87);
            b5.setPrefHeight(91);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }


        Button b6=new Button();
        if(mapp1) {
            b6.setPrefWidth(94);
            b6.setPrefHeight(86);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp2) {
            b6.setPrefWidth(100);
            b6.setPrefHeight(91);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp3) {
            b6.setPrefWidth(90);
            b6.setPrefHeight(92);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp4) {
            b6.setPrefWidth(99);
            b6.setPrefHeight(84);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }


        Button b7=new Button();
        if(mapp1) {
            b7.setPrefWidth(77);
            b7.setPrefHeight(99);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b7.setPrefWidth(67);
            b7.setPrefHeight(90);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b7.setPrefWidth(84);
            b7.setPrefHeight(94);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b7.setPrefWidth(67);
            b7.setPrefHeight(90);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }


        Button b8=new Button();
        if(mapp1) {
            b8.setPrefWidth(78);
            b8.setPrefHeight(91);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b8.setPrefWidth(98);
            b8.setPrefHeight(82);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b8.setPrefWidth(77);
            b8.setPrefHeight(90);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b8.setPrefWidth(81);
            b8.setPrefHeight(82);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }


        Button b9=new Button();
        if(mapp1) {
            b9.setPrefWidth(98);
            b9.setPrefHeight(86);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b9.setPrefWidth(94);
            b9.setPrefHeight(79);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b9.setPrefWidth(84);
            b9.setPrefHeight(79);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b9.setPrefWidth(95);
            b9.setPrefHeight(82);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }


        Button b10=new Button();
        if(mapp1) {
            b10.setPrefWidth(82);
            b10.setPrefHeight(83);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b10.setPrefWidth(72);
            b10.setPrefHeight(93);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b10.setPrefWidth(92);
            b10.setPrefHeight(84);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b10.setPrefWidth(92);
            b10.setPrefHeight(79);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }



        Button b11=new Button();
        if(mapp1) {
            b11.setPrefWidth(84);
            b11.setPrefHeight(90);
            b11.setStyle("-fx-background-color: transparent");
            b11.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        //per mapp2 bisogna eliminare il bottone

        if(mapp3) {
            b11.setPrefWidth(82);
            b11.setPrefHeight(84);
            b11.setStyle("-fx-background-color: transparent");
            b11.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b11.setPrefWidth(71);
            b11.setPrefHeight(90);
            b11.setStyle("-fx-background-color: transparent");
            b11.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }






        Button deck=new Button();
        deck.setPrefWidth(58);
        deck.setPrefHeight(97);
        deck.setStyle("-fx-background-color: transparent");
        deck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Rules.showRules();
            }
        });




        showPlB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PlBs.showPlBs();
            }
        });


        AnchorPane gp= new AnchorPane();
        if(mapp1||mapp4) {
            gp.getChildren().addAll(map, plB, shoot, grab, move, loadWeapon, showPlB, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck);
        }
        if(mapp2) {gp.getChildren().addAll(map, plB, shoot, grab, move, loadWeapon, showPlB, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck);}

        if(mapp3){
                Button b12 = new Button();
                b12.setPrefWidth(84);
                b12.setPrefHeight(91);
                b12.setStyle("-fx-background-color: transparent");
                b12.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Rules.showRules();
                    }
                });
                gp.getChildren().addAll(map, plB, shoot, grab, move, loadWeapon, showPlB, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck);
                b12.setLayoutX(405);
                b12.setLayoutY(300);
        }


        map.setLayoutX(0);
        map.setLayoutY(0);
        plB.setLayoutY(480);
        plB.setLayoutX(0);
        shoot.setLayoutX(620);
        shoot.setLayoutY(50);
        grab.setLayoutY(100);
        grab.setLayoutX(620);
        move.setLayoutX(620);
        move.setLayoutY(150);
        loadWeapon.setLayoutX(620);
        loadWeapon.setLayoutY(200);
        showPlB.setLayoutX(620);
        showPlB.setLayoutY(300);


        if(mapp1) {
            b1.setLayoutX(111);
            b1.setLayoutY(105);
            b2.setLayoutX(201);
            b2.setLayoutY(106);
            b3.setLayoutX(303);
            b3.setLayoutY(108);
            b4.setLayoutX(419);
            b4.setLayoutY(125);
            b5.setLayoutX(113);
            b5.setLayoutY(201);
            b6.setLayoutX(202);
            b6.setLayoutY(211);
            b7.setLayoutX(322);
            b7.setLayoutY(201);
            b8.setLayoutX(408);
            b8.setLayoutY(204);
            b9.setLayoutX(197);
            b9.setLayoutY(314);
            b10.setLayoutX(314);
            b10.setLayoutY(311);
            b11.setLayoutX(405);
            b11.setLayoutY(301);
        }
        if(mapp2) {
            b1.setLayoutX(110);
            b1.setLayoutY(106);
            b2.setLayoutX(202);
            b2.setLayoutY(106);
            b3.setLayoutX(304);
            b3.setLayoutY(106);
            b4.setLayoutX(110);
            b4.setLayoutY(202);
            b5.setLayoutX(203);
            b5.setLayoutY(206);
            b6.setLayoutX(300);
            b6.setLayoutY(205);
            b7.setLayoutX(420);
            b7.setLayoutY(205);
            b8.setLayoutX(198);
            b8.setLayoutY(314);
            b9.setLayoutX(304);
            b9.setLayoutY(313);
            b10.setLayoutX(418);
            b10.setLayoutY(299);
        }
        if(mapp3) {
            b1.setLayoutX(110);
            b1.setLayoutY(105);
            b2.setLayoutX(202);
            b2.setLayoutY(106);
            b3.setLayoutX(304);
            b3.setLayoutY(109);
            b4.setLayoutX(419);
            b4.setLayoutY(108);
            b5.setLayoutX(113);
            b5.setLayoutY(202);
            b6.setLayoutX(204);
            b6.setLayoutY(205);
            b7.setLayoutX(315);
            b7.setLayoutY(205);
            b8.setLayoutX(409);
            b8.setLayoutY(205);
            b9.setLayoutX(112);
            b9.setLayoutY(313);
            b10.setLayoutX(201);
            b10.setLayoutY(312);
            b11.setLayoutX(313);
            b11.setLayoutY(308);
        }
        if(mapp4) {
            b1.setLayoutX(111);
            b1.setLayoutY(104);
            b2.setLayoutX(202);
            b2.setLayoutY(106);
            b3.setLayoutX(304);
            b3.setLayoutY(108);
            b4.setLayoutX(112);
            b4.setLayoutY(203);
            b5.setLayoutX(204);
            b5.setLayoutY(205);
            b6.setLayoutX(301);
            b6.setLayoutY(204);
            b7.setLayoutX(421);
            b7.setLayoutY(205);
            b8.setLayoutX(114);
            b8.setLayoutY(314);
            b9.setLayoutX(202);
            b9.setLayoutY(313);
            b10.setLayoutX(305);
            b10.setLayoutY(312);
            b11.setLayoutX(419);
            b11.setLayoutY(301);
        }


        a1.setLayoutX(317);
        a1.setLayoutY(2);
        a2.setLayoutX(383);
        a2.setLayoutY(2);
        a3.setLayoutX(449);
        a3.setLayoutY(2);
        a4.setLayoutX(2);
        a4.setLayoutY(166);
        a5.setLayoutX(2);
        a5.setLayoutY(232);
        a6.setLayoutX(2);
        a6.setLayoutY(298);
        a7.setLayoutX(516);
        a7.setLayoutY(257);
        a8.setLayoutX(516);
        a8.setLayoutY(323);
        a9.setLayoutX(516);
        a9.setLayoutY(389);

        deck.setLayoutX(522);
        deck.setLayoutY(121);


        Scene scene=new Scene(gp, 800, 650);
        scene.getStylesheets().addAll(GameWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Bye.byebye();
                s.close();
            }
        });
        s.show();

    }
}

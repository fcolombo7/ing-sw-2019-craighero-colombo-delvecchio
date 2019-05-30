package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GameWindow {


    public static void open(Image image, String urlmap) throws IOException {
        Stage s= new Stage();
        s.setTitle("ADRENALINE");

        boolean mapp1=false;
        boolean mapp2=false;
        boolean mapp3=false;
        boolean mapp4=false;
        if(urlmap.contains("/gui/map1.png")) mapp1=true;
        if(urlmap.contains("/gui/map2.png")) mapp2=true;
        if(urlmap.contains("/gui/map3.png")) mapp3=true;
        if(urlmap.contains("/gui/map4.png")) mapp4=true;
        ImageView map=new ImageView(image);
        map.setFitWidth(600);
        map.setFitHeight(454);
        Image playerBoard= new Image("/gui/playerBoard.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600);
        plB.setPreserveRatio(true);



        Image pla1= new Image("/gui/pl1.png");
        ImageView pl1= new ImageView(pla1);
        pl1.setFitWidth(304);
        pl1.setPreserveRatio(true);
        Image pla2= new Image("/gui/pl2.png");
        ImageView pl2= new ImageView(pla2);
        pl2.setFitWidth(304);
        pl2.setPreserveRatio(true);
        Image pla3= new Image("/gui/pl3.png");
        ImageView pl3= new ImageView(pla3);
        pl3.setFitWidth(304);
        pl3.setPreserveRatio(true);
        Image pla4= new Image("/gui/pl4.png");
        ImageView pl4= new ImageView(pla4);
        pl4.setFitWidth(304);
        pl4.setPreserveRatio(true);

        Image yammo= new Image("/gui/yammo.png");
        ImageView yam= new ImageView(yammo);
        yam.setFitWidth(40);
        yam.setPreserveRatio(true);
        Image bammo= new Image("/gui/bammo.png");
        ImageView bam= new ImageView(bammo);
        bam.setFitWidth(40);
        bam.setPreserveRatio(true);
        Image rammo= new Image("/gui/rammo.png");
        ImageView ram= new ImageView(rammo);
        ram.setFitWidth(40);
        ram.setPreserveRatio(true);

        Label ny=new Label("x 2");
        Label nb=new Label("x 3");
        Label nr= new Label("x 0");

        Button inf1= new Button("Info");
        Button inf2= new Button("Info");
        Button inf3= new Button("Info");
        Button inf4= new Button("Info");
        inf1.getStyleClass().add("info");
        inf2.getStyleClass().add("info");
        inf3.getStyleClass().add("info");
        inf4.getStyleClass().add("info");

        Button shoot= new Button("Shoot");
        Button grab= new Button("Grab");
        Button move= new Button("Move");
        Button loadWeapon= new Button("Load Weapon");
        shoot.getStyleClass().add("actionbuttons");
        grab.getStyleClass().add("actionbuttons");
        move.getStyleClass().add("actionbuttons");
        loadWeapon.getStyleClass().add("actionbuttons");
        shoot.setPrefWidth(150);
        grab.setPrefWidth(150);
        move.setPrefWidth(150);
        loadWeapon.setPrefWidth(150);

        Image weap1= new Image("/gui/spadafotonica.png");
        ImageView wp1= new ImageView(weap1);
        wp1.setFitWidth(80);
        wp1.setPreserveRatio(true);
        Button myw1= new Button();
        myw1.setPrefWidth(80);
        myw1.setPrefHeight(135);
        myw1.setStyle("-fx-background-color: transparent");
        myw1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw1.setStyle("-fx-background-color: grey");

            }
        });

        Image weap2= new Image("/gui/cyberguanto.png");
        ImageView wp2= new ImageView(weap2);
        wp2.setFitWidth(80);
        wp2.setPreserveRatio(true);
        Button myw2= new Button();
        myw2.setPrefWidth(80);
        myw2.setPrefHeight(135);
        myw2.setStyle("-fx-background-color: transparent");
        myw2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw2.setStyle("-fx-background-color: grey");

            }
        });

        Image weap3= new Image("/gui/martelloionico.png");
        ImageView wp3= new ImageView(weap3);
        wp3.setFitWidth(80);
        wp3.setPreserveRatio(true);
        Button myw3= new Button();
        myw3.setPrefWidth(80);
        myw3.setPrefHeight(135);
        myw3.setStyle("-fx-background-color: transparent");
        myw3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw3.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup1= new Image("/gui/granatavenom.png");
        ImageView pu1= new ImageView(powerup1);
        pu1.setFitWidth(80);
        pu1.setPreserveRatio(true);
        Button myp1= new Button();
        myp1.setPrefWidth(80);
        myp1.setPrefHeight(135);
        myp1.setStyle("-fx-background-color: transparent");
        myp1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp1.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup2= new Image("/gui/teletrasporto.png");
        ImageView pu2= new ImageView(powerup2);
        pu2.setFitWidth(80);
        pu2.setPreserveRatio(true);
        Button myp2= new Button();
        myp2.setPrefWidth(80);
        myp2.setPrefHeight(135);
        myp2.setStyle("-fx-background-color: transparent");
        myp2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp2.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup3= new Image("/gui/raggiocinetico.png");
        ImageView pu3= new ImageView(powerup3);
        pu3.setFitWidth(80);
        pu3.setPreserveRatio(true);
        Button myp3= new Button();
        myp3.setPrefWidth(80);
        myp3.setPrefHeight(135);
        myp3.setStyle("-fx-background-color: transparent");
        myp3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp3.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup4= new Image("/gui/mirino.png");
        ImageView pu4= new ImageView(powerup4);
        pu4.setFitWidth(80);
        pu4.setPreserveRatio(true);
        Button myp4= new Button();
        myp4.setPrefWidth(80);
        myp4.setPrefHeight(135);
        myp4.setStyle("-fx-background-color: transparent");
        myp4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp4.setStyle("-fx-background-color: grey");

            }
        });


        Image wea1=new Image("/gui/cannonevortex.png");
        ImageView wa1=new ImageView(wea1);
        wa1.setFitWidth(57);
        wa1.setFitHeight(84);

        Image wea2=new Image("/gui/lanciafiamme.png");
        ImageView wa2=new ImageView(wea2);
        wa2.setFitWidth(57);
        wa2.setFitHeight(84);

        Image wea3=new Image("/gui/lanciagranate.png");
        ImageView wa3=new ImageView(wea3);
        wa3.setFitWidth(57);
        wa3.setFitHeight(84);

        Image wea4=new Image("/gui/mitragliatrice.png");
        ImageView wa4=new ImageView(wea4);
        wa4.setFitWidth(57);
        wa4.setFitHeight(84);
        wa4.setRotate(270);

        Image wea5=new Image("/gui/fucilealplasma.png");
        ImageView wa5=new ImageView(wea5);
        wa5.setFitWidth(57);
        wa5.setFitHeight(84);
        wa5.setRotate(270);

        Image wea6=new Image("/gui/fucilediprecisione.png");
        ImageView wa6=new ImageView(wea6);
        wa6.setFitWidth(57);
        wa6.setFitHeight(84);
        wa6.setRotate(270);

        Image wea7=new Image("/gui/raggiotraente.png");
        ImageView wa7=new ImageView(wea7);
        wa7.setFitWidth(57);
        wa7.setFitHeight(84);
        wa7.setRotate(90);

        Image wea8=new Image("/gui/torpedine.png");
        ImageView wa8=new ImageView(wea8);
        wa8.setFitWidth(57);
        wa8.setFitHeight(84);
        wa8.setRotate(90);

        Image wea9=new Image("/gui/raggiosolare.png");
        ImageView wa9=new ImageView(wea9);
        wa9.setFitWidth(57);
        wa9.setFitHeight(84);
        wa9.setRotate(90);


        Button a1=new Button();
        a1.setPrefWidth(57);
        a1.setPrefHeight(84);
        a1.setStyle("-fx-background-color: transparent");
        a1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea1);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a2=new Button();
        a2.setPrefWidth(57);
        a2.setPrefHeight(84);
        a2.setStyle("-fx-background-color: transparent");
        a2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea2);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a3=new Button();
        a3.setPrefWidth(57);
        a3.setPrefHeight(84);
        a3.setStyle("-fx-background-color: transparent");
        a3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea3);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a4=new Button();
        a4.setPrefWidth(84);
        a4.setPrefHeight(57);
        a4.setStyle("-fx-background-color: transparent");
        a4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea4);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a5=new Button();
        a5.setPrefWidth(84);
        a5.setPrefHeight(57);
        a5.setStyle("-fx-background-color: transparent");
        a5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea5);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a6=new Button();
        a6.setPrefWidth(84);
        a6.setPrefHeight(57);
        a6.setStyle("-fx-background-color: transparent");
        a6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea6);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a7=new Button();
        a7.setPrefWidth(84);
        a7.setPrefHeight(57);
        a7.setStyle("-fx-background-color: transparent");
        a7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea7);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a8=new Button();
        a8.setPrefWidth(84);
        a8.setPrefHeight(57);
        a8.setStyle("-fx-background-color: transparent");
        a8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea8);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
            }
        });

        Button a9=new Button();
        a9.setPrefWidth(84);
        a9.setPrefHeight(57);
        a9.setStyle("-fx-background-color: transparent");
        a9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea1);
                bigweap.setFitWidth(200);
                bigweap.setFitHeight(338);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(s);
                wshowstage.show();
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



        Image deckweap=new Image("/gui/weaponcover.png");
        ImageView dw=new ImageView(deckweap);
        dw.setFitWidth(58);
        dw.setFitHeight(97);

        Image deckpow=new Image("/gui/powerupcover.png");
        ImageView dp=new ImageView(deckpow);
        dp.setFitWidth(44);
        dp.setFitHeight(63);




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



        AnchorPane gp= new AnchorPane();
        if(mapp1) {
            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21);
            ammsq1.setPrefHeight(21);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(123);
            ammsq1.setLayoutY(115);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21);
            ammsq2.setPrefHeight(21);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(216);
            ammsq2.setLayoutY(160);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21);
            ammsq3.setPrefHeight(21);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(461);
            ammsq3.setLayoutY(160);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21);
            ammsq4.setPrefHeight(21);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(207);
            ammsq4.setLayoutY(237);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21);
            ammsq5.setPrefHeight(21);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(324);
            ammsq5.setLayoutY(268);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21);
            ammsq6.setPrefHeight(21);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(414);
            ammsq6.setLayoutY(268);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21);
            ammsq7.setPrefHeight(21);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(209);
            ammsq7.setLayoutY(354);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21);
            ammsq8.setPrefHeight(21);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(329);
            ammsq8.setLayoutY(354);


            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8);
        }

        if(mapp4){
            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21);
            ammsq1.setPrefHeight(21);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(126);
            ammsq1.setLayoutY(164);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21);
            ammsq2.setPrefHeight(21);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(215);
            ammsq2.setLayoutY(112);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21);
            ammsq3.setPrefHeight(21);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(207);
            ammsq3.setLayoutY(240);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21);
            ammsq4.setPrefHeight(21);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(324);
            ammsq4.setLayoutY(250);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21);
            ammsq5.setPrefHeight(21);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(461);
            ammsq5.setLayoutY(249);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21);
            ammsq6.setPrefHeight(21);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(126);
            ammsq6.setLayoutY(354);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21);
            ammsq7.setPrefHeight(21);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(209);
            ammsq7.setLayoutY(354);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21);
            ammsq8.setPrefHeight(21);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(313);
            ammsq8.setLayoutY(354);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8);
        }

        if(mapp2) {
            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21);
            ammsq1.setPrefHeight(21);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(123);
            ammsq1.setLayoutY(115);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21);
            ammsq2.setPrefHeight(21);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(216);
            ammsq2.setLayoutY(160);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21);
            ammsq3.setPrefHeight(21);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(207);
            ammsq3.setLayoutY(237);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21);
            ammsq4.setPrefHeight(21);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(324);
            ammsq4.setLayoutY(250);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21);
            ammsq5.setPrefHeight(21);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(461);
            ammsq5.setLayoutY(249);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21);
            ammsq6.setPrefHeight(21);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(209);
            ammsq6.setLayoutY(354);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21);
            ammsq7.setPrefHeight(21);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(313);
            ammsq7.setLayoutY(354);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7);
        }

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

            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21);
            ammsq1.setPrefHeight(21);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(126);
            ammsq1.setLayoutY(164);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21);
            ammsq2.setPrefHeight(21);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(215);
            ammsq2.setLayoutY(112);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21);
            ammsq3.setPrefHeight(21);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(461);
            ammsq3.setLayoutY(160);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21);
            ammsq4.setPrefHeight(21);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(208);
            ammsq4.setLayoutY(240);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21);
            ammsq5.setPrefHeight(21);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(324);
            ammsq5.setLayoutY(268);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21);
            ammsq6.setPrefHeight(21);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(414);
            ammsq6.setLayoutY(268);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21);
            ammsq7.setPrefHeight(21);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(126);
            ammsq7.setLayoutY(354);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21);
            ammsq8.setPrefHeight(21);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(209);
            ammsq8.setLayoutY(354);
            Button ammsq9= new Button();
            ammsq9.setPrefWidth(21);
            ammsq9.setPrefHeight(21);
            ammsq9.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq9.setLayoutX(329);
            ammsq9.setLayoutY(354);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8, ammsq9);
            b12.setLayoutX(405);
            b12.setLayoutY(300);
        }


        map.setLayoutX(0);
        map.setLayoutY(0);
        plB.setLayoutY(480);
        plB.setLayoutX(0);
        pl1.setLayoutX(630);
        pl1.setLayoutY(20);
        pl2.setLayoutX(630);
        pl2.setLayoutY(100);
        pl3.setLayoutX(630);
        pl3.setLayoutY(180);
        pl4.setLayoutX(630);
        pl4.setLayoutY(260);
        shoot.setLayoutX(1020);
        shoot.setLayoutY(150);
        grab.setLayoutX(1020);
        grab.setLayoutY(90);
        move.setLayoutX(1020);
        move.setLayoutY(30);
        loadWeapon.setLayoutX(1020);
        loadWeapon.setLayoutY(210);
        wp1.setLayoutX(630);
        wp1.setLayoutY(500);
        myw1.setLayoutX(630);
        myw1.setLayoutY(500);
        wp2.setLayoutX(720);
        wp2.setLayoutY(500);
        myw2.setLayoutX(720);
        myw2.setLayoutY(500);
        wp3.setLayoutX(810);
        wp3.setLayoutY(500);
        myw3.setLayoutX(810);
        myw3.setLayoutY(500);
        yam.setLayoutX(920);
        yam.setLayoutY(500);
        bam.setLayoutX(920);
        bam.setLayoutY(550);
        ram.setLayoutX(920);
        ram.setLayoutY(600);
        ny.setLayoutX(980);
        ny.setLayoutY(505);
        nb.setLayoutX(980);
        nb.setLayoutY(555);
        nr.setLayoutX(980);
        nr.setLayoutY(605);
        pu1.setLayoutX(630);
        pu1.setLayoutY(360);
        myp1.setLayoutX(630);
        myp1.setLayoutY(360);
        pu2.setLayoutX(720);
        pu2.setLayoutY(360);
        myp2.setLayoutX(720);
        myp2.setLayoutY(360);
        pu3.setLayoutX(810);
        pu3.setLayoutY(360);
        myp3.setLayoutX(810);
        myp3.setLayoutY(360);
        pu4.setLayoutX(900);
        pu4.setLayoutY(360);
        myp4.setLayoutX(900);
        myp4.setLayoutY(360);
        inf1.setLayoutX(940);
        inf1.setLayoutY(45);
        inf2.setLayoutX(940);
        inf2.setLayoutY(125);
        inf3.setLayoutX(940);
        inf3.setLayoutY(205);
        inf4.setLayoutX(940);
        inf4.setLayoutY(285);


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


        wa1.setLayoutX(317);
        wa1.setLayoutY(2);
        wa2.setLayoutX(383);
        wa2.setLayoutY(2);
        wa3.setLayoutX(449);
        wa3.setLayoutY(2);
        wa4.setLayoutX(15.5);
        wa4.setLayoutY(152.5);
        wa5.setLayoutX(15.5);
        wa5.setLayoutY(218.5);
        wa6.setLayoutX(15.5);
        wa6.setLayoutY(284.5);
        wa7.setLayoutX(529.5);
        wa7.setLayoutY(243.5);
        wa8.setLayoutX(529.5);
        wa8.setLayoutY(309.5);
        wa9.setLayoutX(529.5);
        wa9.setLayoutY(375.5);


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

        dw.setLayoutX(522);
        dw.setLayoutY(121);
        dp.setLayoutX(537);
        dp.setLayoutY(24);


        Scene scene=new Scene(gp, 1200, 650);
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

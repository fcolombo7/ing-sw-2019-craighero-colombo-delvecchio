package it.polimi.ingsw.ui;

import it.polimi.ingsw.GUI.Bye;
import it.polimi.ingsw.GUI.ProvaWindow;
import it.polimi.ingsw.GUI.Rules;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class AdrenalineGUI extends Application implements AdrenalineUI {

    private ServerConnection connection;

    public static void main(String []args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /*get screen dimension*/
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        System.out.println(primaryScreenBounds.getWidth() + " - " +primaryScreenBounds.getHeight());
        double widthScaleFactor=primaryScreenBounds.getWidth()/1200;
        double heightScaleFactor=primaryScreenBounds.getHeight()/675;//PER MANTENERE LE PROPORZIONI DEL FULL HD NO 650 ma 675
        System.out.println(primaryScreenBounds.getWidth()/primaryScreenBounds.getHeight() + " - " +1200.0/675.0);
        System.out.println(widthScaleFactor + " - " +heightScaleFactor);


        stage.setTitle("ADRENALINE");
        String urlmap="/gui/map1.png";
        Image image= new Image("/gui/map1.png");

        boolean mapp1=false;
        boolean mapp2=false;
        boolean mapp3=false;
        boolean mapp4=false;
        if(urlmap.contains("/gui/map1.png")) mapp1=true;
        if(urlmap.contains("/gui/map2.png")) mapp2=true;
        if(urlmap.contains("/gui/map3.png")) mapp3=true;
        if(urlmap.contains("/gui/map4.png")) mapp4=true;
        ImageView map=new ImageView(image);
        map.setFitWidth(600*widthScaleFactor);
        map.setFitHeight(454*heightScaleFactor);
        Image playerBoard= new Image("/gui/playerBoard.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600*widthScaleFactor);
        plB.setPreserveRatio(true);



        Image pla1= new Image("/gui/pl1.png");
        ImageView pl1= new ImageView(pla1);
        pl1.setFitWidth(304*widthScaleFactor);
        pl1.setPreserveRatio(true);
        Image pla2= new Image("/gui/pl2.png");
        ImageView pl2= new ImageView(pla2);
        pl2.setFitWidth(304*widthScaleFactor);
        pl2.setPreserveRatio(true);
        Image pla3= new Image("/gui/pl3.png");
        ImageView pl3= new ImageView(pla3);
        pl3.setFitWidth(304*widthScaleFactor);
        pl3.setPreserveRatio(true);
        Image pla4= new Image("/gui/pl4.png");
        ImageView pl4= new ImageView(pla4);
        pl4.setFitWidth(304*widthScaleFactor);
        pl4.setPreserveRatio(true);

        Image yammo= new Image("/gui/yammo.png");
        ImageView yam= new ImageView(yammo);
        yam.setFitWidth(40*widthScaleFactor);
        yam.setPreserveRatio(true);
        Image bammo= new Image("/gui/bammo.png");
        ImageView bam= new ImageView(bammo);
        bam.setFitWidth(40*widthScaleFactor);
        bam.setPreserveRatio(true);
        Image rammo= new Image("/gui/rammo.png");
        ImageView ram= new ImageView(rammo);
        ram.setFitWidth(40*widthScaleFactor);
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
        shoot.setPrefWidth(150*widthScaleFactor);
        grab.setPrefWidth(150*widthScaleFactor);
        move.setPrefWidth(150*widthScaleFactor);
        loadWeapon.setPrefWidth(150*widthScaleFactor);

        Image weap1= new Image("/gui/spadafotonica.png");
        ImageView wp1= new ImageView(weap1);
        wp1.setFitWidth(80*widthScaleFactor);
        wp1.setPreserveRatio(true);
        Button myw1= new Button();
        myw1.setPrefWidth(80*widthScaleFactor);
        myw1.setPrefHeight(135*heightScaleFactor);
        myw1.setStyle("-fx-background-color: transparent");
        myw1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw1.setStyle("-fx-background-color: grey");

            }
        });

        Image weap2= new Image("/gui/cyberguanto.png");
        ImageView wp2= new ImageView(weap2);
        wp2.setFitWidth(80*widthScaleFactor);
        wp2.setPreserveRatio(true);
        Button myw2= new Button();
        myw2.setPrefWidth(80*widthScaleFactor);
        myw2.setPrefHeight(135*heightScaleFactor);
        myw2.setStyle("-fx-background-color: transparent");
        myw2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw2.setStyle("-fx-background-color: grey");

            }
        });

        Image weap3= new Image("/gui/martelloionico.png");
        ImageView wp3= new ImageView(weap3);
        wp3.setFitWidth(80*widthScaleFactor);
        wp3.setPreserveRatio(true);
        Button myw3= new Button();
        myw3.setPrefWidth(80*widthScaleFactor);
        myw3.setPrefHeight(135*heightScaleFactor);
        myw3.setStyle("-fx-background-color: transparent");
        myw3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw3.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup1= new Image("/gui/granatavenom.png");
        ImageView pu1= new ImageView(powerup1);
        pu1.setFitWidth(80*widthScaleFactor);
        pu1.setPreserveRatio(true);
        Button myp1= new Button();
        myp1.setPrefWidth(80*widthScaleFactor);
        myp1.setPrefHeight(135*heightScaleFactor);
        myp1.setStyle("-fx-background-color: transparent");
        myp1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp1.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup2= new Image("/gui/teletrasporto.png");
        ImageView pu2= new ImageView(powerup2);
        pu2.setFitWidth(80*widthScaleFactor);
        pu2.setPreserveRatio(true);
        Button myp2= new Button();
        myp2.setPrefWidth(80*widthScaleFactor);
        myp2.setPrefHeight(135*heightScaleFactor);
        myp2.setStyle("-fx-background-color: transparent");
        myp2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp2.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup3= new Image("/gui/raggiocinetico.png");
        ImageView pu3= new ImageView(powerup3);
        pu3.setFitWidth(80*widthScaleFactor);
        pu3.setPreserveRatio(true);
        Button myp3= new Button();
        myp3.setPrefWidth(80*widthScaleFactor);
        myp3.setPrefHeight(135*heightScaleFactor);
        myp3.setStyle("-fx-background-color: transparent");
        myp3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp3.setStyle("-fx-background-color: grey");

            }
        });

        Image powerup4= new Image("/gui/mirino.png");
        ImageView pu4= new ImageView(powerup4);
        pu4.setFitWidth(80*widthScaleFactor);
        pu4.setPreserveRatio(true);
        Button myp4= new Button();
        myp4.setPrefWidth(80*widthScaleFactor);
        myp4.setPrefHeight(135*heightScaleFactor);
        myp4.setStyle("-fx-background-color: transparent");
        myp4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp4.setStyle("-fx-background-color: grey");

            }
        });


        Image wea1=new Image("/gui/cannonevortex.png");
        ImageView wa1=new ImageView(wea1);
        wa1.setFitWidth(57*widthScaleFactor);
        wa1.setFitHeight(84*heightScaleFactor);

        Image wea2=new Image("/gui/lanciafiamme.png");
        ImageView wa2=new ImageView(wea2);
        wa2.setFitWidth(57*widthScaleFactor);
        wa2.setFitHeight(84*heightScaleFactor);

        Image wea3=new Image("/gui/lanciagranate.png");
        ImageView wa3=new ImageView(wea3);
        wa3.setFitWidth(57*widthScaleFactor);
        wa3.setFitHeight(84*heightScaleFactor);

        Image wea4=new Image("/gui/mitragliatrice.png");
        ImageView wa4=new ImageView(wea4);
        wa4.setFitWidth(57*widthScaleFactor);
        wa4.setFitHeight(84*heightScaleFactor);
        wa4.setRotate(270);

        Image wea5=new Image("/gui/fucilealplasma.png");
        ImageView wa5=new ImageView(wea5);
        wa5.setFitWidth(57*widthScaleFactor);
        wa5.setFitHeight(84*heightScaleFactor);
        wa5.setRotate(270);

        Image wea6=new Image("/gui/fucilediprecisione.png");
        ImageView wa6=new ImageView(wea6);
        wa6.setFitWidth(57*widthScaleFactor);
        wa6.setFitHeight(84*heightScaleFactor);
        wa6.setRotate(270);

        Image wea7=new Image("/gui/raggiotraente.png");
        ImageView wa7=new ImageView(wea7);
        wa7.setFitWidth(57*widthScaleFactor);
        wa7.setFitHeight(84*heightScaleFactor);
        wa7.setRotate(90);

        Image wea8=new Image("/gui/torpedine.png");
        ImageView wa8=new ImageView(wea8);
        wa8.setFitWidth(57*widthScaleFactor);
        wa8.setFitHeight(84*heightScaleFactor);
        wa8.setRotate(90);

        Image wea9=new Image("/gui/raggiosolare.png");
        ImageView wa9=new ImageView(wea9);
        wa9.setFitWidth(57*widthScaleFactor);
        wa9.setFitHeight(84*heightScaleFactor);
        wa9.setRotate(90);


        Button a1=new Button();
        a1.setPrefWidth(57*widthScaleFactor);
        a1.setPrefHeight(84*heightScaleFactor);
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
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a2=new Button();
        a2.setPrefWidth(57*widthScaleFactor);
        a2.setPrefHeight(84*heightScaleFactor);
        a2.setStyle("-fx-background-color: transparent");
        a2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea2);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200, 338);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a3=new Button();
        a3.setPrefWidth(57*widthScaleFactor);
        a3.setPrefHeight(84*heightScaleFactor);
        a3.setStyle("-fx-background-color: transparent");
        a3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea3);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a4=new Button();
        a4.setPrefWidth(84*widthScaleFactor);
        a4.setPrefHeight(57*heightScaleFactor);
        a4.setStyle("-fx-background-color: transparent");
        a4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea4);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a5=new Button();
        a5.setPrefWidth(84*widthScaleFactor);
        a5.setPrefHeight(57*heightScaleFactor);
        a5.setStyle("-fx-background-color: transparent");
        a5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea5);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a6=new Button();
        a6.setPrefWidth(84*widthScaleFactor);
        a6.setPrefHeight(57*heightScaleFactor);
        a6.setStyle("-fx-background-color: transparent");
        a6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea6);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a7=new Button();
        a7.setPrefWidth(84*widthScaleFactor);
        a7.setPrefHeight(57*heightScaleFactor);
        a7.setStyle("-fx-background-color: transparent");
        a7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea7);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a8=new Button();
        a8.setPrefWidth(84*widthScaleFactor);
        a8.setPrefHeight(57*heightScaleFactor);
        a8.setStyle("-fx-background-color: transparent");
        a8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea8);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });

        Button a9=new Button();
        a9.setPrefWidth(84*widthScaleFactor);
        a9.setPrefHeight(57*heightScaleFactor);
        a9.setStyle("-fx-background-color: transparent");
        a9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane weapshow= new AnchorPane();
                ImageView bigweap= new ImageView(wea1);
                bigweap.setFitWidth(200*widthScaleFactor);
                bigweap.setFitHeight(338*heightScaleFactor);
                weapshow.getChildren().add(bigweap);
                Scene wshowscene= new Scene(weapshow, 200*widthScaleFactor, 338*heightScaleFactor);
                Stage wshowstage= new Stage();
                wshowstage.setResizable(false);
                wshowstage.setScene(wshowscene);
                wshowstage.initModality(Modality.WINDOW_MODAL);
                wshowstage.initOwner(stage);
                wshowstage.show();
            }
        });


        Button b1=new Button();
        if(mapp1) {
            b1.setPrefWidth(80*widthScaleFactor);
            b1.setPrefHeight(83*heightScaleFactor);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp2) {
            b1.setPrefWidth(85*widthScaleFactor);
            b1.setPrefHeight(83*heightScaleFactor);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp3) {
            b1.setPrefWidth(71*widthScaleFactor);
            b1.setPrefHeight(94*heightScaleFactor);
            b1.setStyle("-fx-background-color: transparent");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }

        if(mapp4) {
            b1.setPrefWidth(71*widthScaleFactor);
            b1.setPrefHeight(94*heightScaleFactor);
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
            b2.setPrefWidth(97*widthScaleFactor);
            b2.setPrefHeight(83*heightScaleFactor);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp2) {
            b2.setPrefWidth(97*widthScaleFactor);
            b2.setPrefHeight(84*heightScaleFactor);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp3) {
            b2.setPrefWidth(96*widthScaleFactor);
            b2.setPrefHeight(82*heightScaleFactor);
            b2.setStyle("-fx-background-color: transparent");
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }

        if(mapp4) {
            b2.setPrefWidth(97*widthScaleFactor);
            b2.setPrefHeight(82*heightScaleFactor);
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
            b3.setPrefWidth(95*widthScaleFactor);
            b3.setPrefHeight(80*heightScaleFactor);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp2) {
            b3.setPrefWidth(93*widthScaleFactor);
            b3.setPrefHeight(81*heightScaleFactor);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp3) {
            b3.setPrefWidth(95*widthScaleFactor);
            b3.setPrefHeight(79*heightScaleFactor);
            b3.setStyle("-fx-background-color: transparent");
            b3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp4) {
            b3.setPrefWidth(93*widthScaleFactor);
            b3.setPrefHeight(80*heightScaleFactor);
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
            b4.setPrefWidth(68*widthScaleFactor);
            b4.setPrefHeight(65*heightScaleFactor);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b4.setPrefWidth(85*widthScaleFactor);
            b4.setPrefHeight(95*heightScaleFactor);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b4.setPrefWidth(73*widthScaleFactor);
            b4.setPrefHeight(79*heightScaleFactor);
            b4.setStyle("-fx-background-color: transparent");
            b4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b4.setPrefWidth(68*widthScaleFactor);
            b4.setPrefHeight(91*heightScaleFactor);
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
            b5.setPrefWidth(82*widthScaleFactor);
            b5.setPrefHeight(84*heightScaleFactor);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b5.setPrefWidth(87*widthScaleFactor);
            b5.setPrefHeight(90*heightScaleFactor);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b5.setPrefWidth(68*widthScaleFactor);
            b5.setPrefHeight(96*heightScaleFactor);
            b5.setStyle("-fx-background-color: transparent");
            b5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b5.setPrefWidth(87*widthScaleFactor);
            b5.setPrefHeight(91*heightScaleFactor);
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
            b6.setPrefWidth(94*widthScaleFactor);
            b6.setPrefHeight(86*heightScaleFactor);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp2) {
            b6.setPrefWidth(100*widthScaleFactor);
            b6.setPrefHeight(91*heightScaleFactor);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp3) {
            b6.setPrefWidth(90*widthScaleFactor);
            b6.setPrefHeight(92*heightScaleFactor);
            b6.setStyle("-fx-background-color: transparent");
            b6.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Bye.byebye();
                }
            });
        }
        if(mapp4) {
            b6.setPrefWidth(99*widthScaleFactor);
            b6.setPrefHeight(84*heightScaleFactor);
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
            b7.setPrefWidth(77*widthScaleFactor);
            b7.setPrefHeight(99*heightScaleFactor);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b7.setPrefWidth(67*widthScaleFactor);
            b7.setPrefHeight(90*heightScaleFactor);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b7.setPrefWidth(84*widthScaleFactor);
            b7.setPrefHeight(94*heightScaleFactor);
            b7.setStyle("-fx-background-color: transparent");
            b7.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b7.setPrefWidth(67*widthScaleFactor);
            b7.setPrefHeight(90*heightScaleFactor);
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
            b8.setPrefWidth(78*widthScaleFactor);
            b8.setPrefHeight(91*heightScaleFactor);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b8.setPrefWidth(98*widthScaleFactor);
            b8.setPrefHeight(82*heightScaleFactor);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b8.setPrefWidth(77*widthScaleFactor);
            b8.setPrefHeight(90*heightScaleFactor);
            b8.setStyle("-fx-background-color: transparent");
            b8.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b8.setPrefWidth(81*widthScaleFactor);
            b8.setPrefHeight(82*heightScaleFactor);
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
            b9.setPrefWidth(98*widthScaleFactor);
            b9.setPrefHeight(86*heightScaleFactor);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b9.setPrefWidth(94*widthScaleFactor);
            b9.setPrefHeight(79*heightScaleFactor);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b9.setPrefWidth(84*widthScaleFactor);
            b9.setPrefHeight(79*heightScaleFactor);
            b9.setStyle("-fx-background-color: transparent");
            b9.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b9.setPrefWidth(95*widthScaleFactor);
            b9.setPrefHeight(82*heightScaleFactor);
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
            b10.setPrefWidth(82*widthScaleFactor);
            b10.setPrefHeight(83*heightScaleFactor);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp2) {
            b10.setPrefWidth(72*widthScaleFactor);
            b10.setPrefHeight(93*heightScaleFactor);
            b10.setStyle("-fx-background-color: transparent");
            b10.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp3) {
            b10.setPrefWidth(92*widthScaleFactor);
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
            b10.setPrefWidth(92*widthScaleFactor);
            b10.setPrefHeight(79*heightScaleFactor);
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
            b11.setPrefWidth(84*widthScaleFactor);
            b11.setPrefHeight(90*heightScaleFactor);
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
            b11.setPrefWidth(82*widthScaleFactor);
            b11.setPrefHeight(84*heightScaleFactor);
            b11.setStyle("-fx-background-color: transparent");
            b11.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });
        }
        if(mapp4) {
            b11.setPrefWidth(71*widthScaleFactor);
            b11.setPrefHeight(90*heightScaleFactor);
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
        dw.setFitWidth(58*widthScaleFactor);
        dw.setFitHeight(97*heightScaleFactor);

        Image deckpow=new Image("/gui/powerupcover.png");
        ImageView dp=new ImageView(deckpow);
        dp.setFitWidth(44*widthScaleFactor);
        dp.setFitHeight(63*heightScaleFactor);




        Button deck=new Button();
        deck.setPrefWidth(58*widthScaleFactor);
        deck.setPrefHeight(97*heightScaleFactor);
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
            ammsq1.setPrefWidth(21*widthScaleFactor);
            ammsq1.setPrefHeight(21*heightScaleFactor);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(123*widthScaleFactor);
            ammsq1.setLayoutY(115*heightScaleFactor);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21*widthScaleFactor);
            ammsq2.setPrefHeight(21*heightScaleFactor);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(216*widthScaleFactor);
            ammsq2.setLayoutY(160*heightScaleFactor);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21*widthScaleFactor);
            ammsq3.setPrefHeight(21*heightScaleFactor);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(461*widthScaleFactor);
            ammsq3.setLayoutY(160*heightScaleFactor);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21*widthScaleFactor);
            ammsq4.setPrefHeight(21*heightScaleFactor);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(207*widthScaleFactor);
            ammsq4.setLayoutY(237*heightScaleFactor);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21*widthScaleFactor);
            ammsq5.setPrefHeight(21*heightScaleFactor);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(324*widthScaleFactor);
            ammsq5.setLayoutY(268*heightScaleFactor);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21*widthScaleFactor);
            ammsq6.setPrefHeight(21*heightScaleFactor);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(414*widthScaleFactor);
            ammsq6.setLayoutY(268*heightScaleFactor);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21*widthScaleFactor);
            ammsq7.setPrefHeight(21*heightScaleFactor);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(209*widthScaleFactor);
            ammsq7.setLayoutY(354*heightScaleFactor);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21*widthScaleFactor);
            ammsq8.setPrefHeight(21*heightScaleFactor);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(329*widthScaleFactor);
            ammsq8.setLayoutY(354*heightScaleFactor);


            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8);
        }

        if(mapp4){
            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21*widthScaleFactor);
            ammsq1.setPrefHeight(21*heightScaleFactor);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(126*widthScaleFactor);
            ammsq1.setLayoutY(164*heightScaleFactor);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21*widthScaleFactor);
            ammsq2.setPrefHeight(21*heightScaleFactor);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(215*widthScaleFactor);
            ammsq2.setLayoutY(112*heightScaleFactor);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21*widthScaleFactor);
            ammsq3.setPrefHeight(21*heightScaleFactor);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(207*widthScaleFactor);
            ammsq3.setLayoutY(240*heightScaleFactor);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21*widthScaleFactor);
            ammsq4.setPrefHeight(21*heightScaleFactor);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(324*widthScaleFactor);
            ammsq4.setLayoutY(250*heightScaleFactor);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21*widthScaleFactor);
            ammsq5.setPrefHeight(21*heightScaleFactor);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(461*widthScaleFactor);
            ammsq5.setLayoutY(249*heightScaleFactor);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21*widthScaleFactor);
            ammsq6.setPrefHeight(21*heightScaleFactor);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(126*widthScaleFactor);
            ammsq6.setLayoutY(354*heightScaleFactor);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21*widthScaleFactor);
            ammsq7.setPrefHeight(21*heightScaleFactor);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(209*widthScaleFactor);
            ammsq7.setLayoutY(354*heightScaleFactor);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21*widthScaleFactor);
            ammsq8.setPrefHeight(21*heightScaleFactor);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(313*widthScaleFactor);
            ammsq8.setLayoutY(354*heightScaleFactor);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8);
        }

        if(mapp2) {
            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21*widthScaleFactor);
            ammsq1.setPrefHeight(21*heightScaleFactor);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(123*widthScaleFactor);
            ammsq1.setLayoutY(115*heightScaleFactor);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21*widthScaleFactor);
            ammsq2.setPrefHeight(21*heightScaleFactor);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(216*widthScaleFactor);
            ammsq2.setLayoutY(160*heightScaleFactor);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21*widthScaleFactor);
            ammsq3.setPrefHeight(21*heightScaleFactor);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(207*widthScaleFactor);
            ammsq3.setLayoutY(237*heightScaleFactor);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21*widthScaleFactor);
            ammsq4.setPrefHeight(21*heightScaleFactor);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(324*widthScaleFactor);
            ammsq4.setLayoutY(250*heightScaleFactor);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21*widthScaleFactor);
            ammsq5.setPrefHeight(21*heightScaleFactor);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(461*widthScaleFactor);
            ammsq5.setLayoutY(249*heightScaleFactor);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21*widthScaleFactor);
            ammsq6.setPrefHeight(21*heightScaleFactor);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(209*widthScaleFactor);
            ammsq6.setLayoutY(354*heightScaleFactor);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21*widthScaleFactor);
            ammsq7.setPrefHeight(21*heightScaleFactor);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(313*widthScaleFactor);
            ammsq7.setLayoutY(354*heightScaleFactor);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7);
        }

        if(mapp3){
            Button b12 = new Button();
            b12.setPrefWidth(84*widthScaleFactor);
            b12.setPrefHeight(91*heightScaleFactor);
            b12.setStyle("-fx-background-color: transparent");
            b12.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Rules.showRules();
                }
            });

            Button ammsq1= new Button();
            ammsq1.setPrefWidth(21*widthScaleFactor);
            ammsq1.setPrefHeight(21*heightScaleFactor);
            ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq1.setLayoutX(126*widthScaleFactor);
            ammsq1.setLayoutY(164*heightScaleFactor);
            Button ammsq2= new Button();
            ammsq2.setPrefWidth(21*widthScaleFactor);
            ammsq2.setPrefHeight(21*heightScaleFactor);
            ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq2.setLayoutX(215*widthScaleFactor);
            ammsq2.setLayoutY(112*heightScaleFactor);
            Button ammsq3= new Button();
            ammsq3.setPrefWidth(21*widthScaleFactor);
            ammsq3.setPrefHeight(21*heightScaleFactor);
            ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq3.setLayoutX(461*widthScaleFactor);
            ammsq3.setLayoutY(160*heightScaleFactor);
            Button ammsq4= new Button();
            ammsq4.setPrefWidth(21*widthScaleFactor);
            ammsq4.setPrefHeight(21*heightScaleFactor);
            ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq4.setLayoutX(208*widthScaleFactor);
            ammsq4.setLayoutY(240*heightScaleFactor);
            Button ammsq5= new Button();
            ammsq5.setPrefWidth(21*widthScaleFactor);
            ammsq5.setPrefHeight(21*heightScaleFactor);
            ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq5.setLayoutX(324*widthScaleFactor);
            ammsq5.setLayoutY(268*heightScaleFactor);
            Button ammsq6= new Button();
            ammsq6.setPrefWidth(21*widthScaleFactor);
            ammsq6.setPrefHeight(21*heightScaleFactor);
            ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq6.setLayoutX(414*widthScaleFactor);
            ammsq6.setLayoutY(268*heightScaleFactor);
            Button ammsq7= new Button();
            ammsq7.setPrefWidth(21*widthScaleFactor);
            ammsq7.setPrefHeight(21*heightScaleFactor);
            ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq7.setLayoutX(126*widthScaleFactor);
            ammsq7.setLayoutY(354*heightScaleFactor);
            Button ammsq8= new Button();
            ammsq8.setPrefWidth(21*widthScaleFactor);
            ammsq8.setPrefHeight(21*heightScaleFactor);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq8.setLayoutX(209*widthScaleFactor);
            ammsq8.setLayoutY(354*heightScaleFactor);
            Button ammsq9= new Button();
            ammsq9.setPrefWidth(21*widthScaleFactor);
            ammsq9.setPrefHeight(21*heightScaleFactor);
            ammsq9.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            ammsq9.setLayoutX(329*widthScaleFactor);
            ammsq9.setLayoutY(354*heightScaleFactor);

            gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8, ammsq9);
            b12.setLayoutX(405*widthScaleFactor);
            b12.setLayoutY(300*heightScaleFactor);
        }


        map.setLayoutX(0*widthScaleFactor);
        map.setLayoutY(0*heightScaleFactor);
        plB.setLayoutY(480*heightScaleFactor);
        plB.setLayoutX(0*widthScaleFactor);
        pl1.setLayoutX(630*widthScaleFactor);
        pl1.setLayoutY(20*heightScaleFactor);
        pl2.setLayoutX(630*widthScaleFactor);
        pl2.setLayoutY(100*heightScaleFactor);
        pl3.setLayoutX(630*widthScaleFactor);
        pl3.setLayoutY(180*heightScaleFactor);
        pl4.setLayoutX(630*widthScaleFactor);
        pl4.setLayoutY(260*heightScaleFactor);
        shoot.setLayoutX(1020*widthScaleFactor);
        shoot.setLayoutY(150*heightScaleFactor);
        grab.setLayoutX(1020*widthScaleFactor);
        grab.setLayoutY(90*heightScaleFactor);
        move.setLayoutX(1020*widthScaleFactor);
        move.setLayoutY(30*heightScaleFactor);
        loadWeapon.setLayoutX(1020*widthScaleFactor);
        loadWeapon.setLayoutY(210*heightScaleFactor);
        wp1.setLayoutX(630*widthScaleFactor);
        wp1.setLayoutY(500*heightScaleFactor);
        myw1.setLayoutX(630*widthScaleFactor);
        myw1.setLayoutY(500*heightScaleFactor);
        wp2.setLayoutX(720*widthScaleFactor);
        wp2.setLayoutY(500*heightScaleFactor);
        myw2.setLayoutX(720*widthScaleFactor);
        myw2.setLayoutY(500*heightScaleFactor);
        wp3.setLayoutX(810*widthScaleFactor);
        wp3.setLayoutY(500*heightScaleFactor);
        myw3.setLayoutX(810*widthScaleFactor);
        myw3.setLayoutY(500*heightScaleFactor);
        yam.setLayoutX(920*widthScaleFactor);
        yam.setLayoutY(500*heightScaleFactor);
        bam.setLayoutX(920*widthScaleFactor);
        bam.setLayoutY(550*heightScaleFactor);
        ram.setLayoutX(920*widthScaleFactor);
        ram.setLayoutY(600*heightScaleFactor);
        ny.setLayoutX(980*widthScaleFactor);
        ny.setLayoutY(505*heightScaleFactor);
        nb.setLayoutX(980*widthScaleFactor);
        nb.setLayoutY(555*heightScaleFactor);
        nr.setLayoutX(980*widthScaleFactor);
        nr.setLayoutY(605*heightScaleFactor);
        pu1.setLayoutX(630*widthScaleFactor);
        pu1.setLayoutY(360*heightScaleFactor);
        myp1.setLayoutX(630*widthScaleFactor);
        myp1.setLayoutY(360*heightScaleFactor);
        pu2.setLayoutX(720*widthScaleFactor);
        pu2.setLayoutY(360*heightScaleFactor);
        myp2.setLayoutX(720*widthScaleFactor);
        myp2.setLayoutY(360*heightScaleFactor);
        pu3.setLayoutX(810*widthScaleFactor);
        pu3.setLayoutY(360*heightScaleFactor);
        myp3.setLayoutX(810*widthScaleFactor);
        myp3.setLayoutY(360*heightScaleFactor);
        pu4.setLayoutX(900*widthScaleFactor);
        pu4.setLayoutY(360*heightScaleFactor);
        myp4.setLayoutX(900*widthScaleFactor);
        myp4.setLayoutY(360*heightScaleFactor);
        inf1.setLayoutX(940*widthScaleFactor);
        inf1.setLayoutY(45*heightScaleFactor);
        inf2.setLayoutX(940*widthScaleFactor);
        inf2.setLayoutY(125*heightScaleFactor);
        inf3.setLayoutX(940*widthScaleFactor);
        inf3.setLayoutY(205*heightScaleFactor);
        inf4.setLayoutX(940*widthScaleFactor);
        inf4.setLayoutY(285*heightScaleFactor);


        if(mapp1) {
            b1.setLayoutX(111*widthScaleFactor);
            b1.setLayoutY(105*heightScaleFactor);
            b2.setLayoutX(201*widthScaleFactor);
            b2.setLayoutY(106*heightScaleFactor);
            b3.setLayoutX(303*widthScaleFactor);
            b3.setLayoutY(108*heightScaleFactor);
            b4.setLayoutX(419*widthScaleFactor);
            b4.setLayoutY(125*heightScaleFactor);
            b5.setLayoutX(113*widthScaleFactor);
            b5.setLayoutY(201*heightScaleFactor);
            b6.setLayoutX(202*widthScaleFactor);
            b6.setLayoutY(211*heightScaleFactor);
            b7.setLayoutX(322*widthScaleFactor);
            b7.setLayoutY(201*heightScaleFactor);
            b8.setLayoutX(408*widthScaleFactor);
            b8.setLayoutY(204*heightScaleFactor);
            b9.setLayoutX(197*widthScaleFactor);
            b9.setLayoutY(314*heightScaleFactor);
            b10.setLayoutX(314*widthScaleFactor);
            b10.setLayoutY(311*heightScaleFactor);
            b11.setLayoutX(405*widthScaleFactor);
            b11.setLayoutY(301*heightScaleFactor);
        }
        if(mapp2) {
            b1.setLayoutX(110*widthScaleFactor);
            b1.setLayoutY(106*heightScaleFactor);
            b2.setLayoutX(202*widthScaleFactor);
            b2.setLayoutY(106*heightScaleFactor);
            b3.setLayoutX(304*widthScaleFactor);
            b3.setLayoutY(106*heightScaleFactor);
            b4.setLayoutX(110*widthScaleFactor);
            b4.setLayoutY(202*heightScaleFactor);
            b5.setLayoutX(203*widthScaleFactor);
            b5.setLayoutY(206*heightScaleFactor);
            b6.setLayoutX(300*widthScaleFactor);
            b6.setLayoutY(205*heightScaleFactor);
            b7.setLayoutX(420*widthScaleFactor);
            b7.setLayoutY(205*heightScaleFactor);
            b8.setLayoutX(198*widthScaleFactor);
            b8.setLayoutY(314*heightScaleFactor);
            b9.setLayoutX(304*widthScaleFactor);
            b9.setLayoutY(313*heightScaleFactor);
            b10.setLayoutX(418*widthScaleFactor);
            b10.setLayoutY(299*heightScaleFactor);
        }
        if(mapp3) {
            b1.setLayoutX(110*widthScaleFactor);
            b1.setLayoutY(105*heightScaleFactor);
            b2.setLayoutX(202*widthScaleFactor);
            b2.setLayoutY(106*heightScaleFactor);
            b3.setLayoutX(304*widthScaleFactor);
            b3.setLayoutY(109*heightScaleFactor);
            b4.setLayoutX(419*widthScaleFactor);
            b4.setLayoutY(108*heightScaleFactor);
            b5.setLayoutX(113*widthScaleFactor);
            b5.setLayoutY(202*heightScaleFactor);
            b6.setLayoutX(204*widthScaleFactor);
            b6.setLayoutY(205*heightScaleFactor);
            b7.setLayoutX(315*widthScaleFactor);
            b7.setLayoutY(205*heightScaleFactor);
            b8.setLayoutX(409*widthScaleFactor);
            b8.setLayoutY(205*heightScaleFactor);
            b9.setLayoutX(112*widthScaleFactor);
            b9.setLayoutY(313*heightScaleFactor);
            b10.setLayoutX(201*widthScaleFactor);
            b10.setLayoutY(312*heightScaleFactor);
            b11.setLayoutX(313*widthScaleFactor);
            b11.setLayoutY(308*heightScaleFactor);
        }
        if(mapp4) {
            b1.setLayoutX(111*widthScaleFactor);
            b1.setLayoutY(104*heightScaleFactor);
            b2.setLayoutX(202*widthScaleFactor);
            b2.setLayoutY(106*heightScaleFactor);
            b3.setLayoutX(304*widthScaleFactor);
            b3.setLayoutY(108*heightScaleFactor);
            b4.setLayoutX(112*widthScaleFactor);
            b4.setLayoutY(203*heightScaleFactor);
            b5.setLayoutX(204*widthScaleFactor);
            b5.setLayoutY(205*heightScaleFactor);
            b6.setLayoutX(301*widthScaleFactor);
            b6.setLayoutY(204*heightScaleFactor);
            b7.setLayoutX(421*widthScaleFactor);
            b7.setLayoutY(205*heightScaleFactor);
            b8.setLayoutX(114*widthScaleFactor);
            b8.setLayoutY(314*heightScaleFactor);
            b9.setLayoutX(202*widthScaleFactor);
            b9.setLayoutY(313*heightScaleFactor);
            b10.setLayoutX(305*widthScaleFactor);
            b10.setLayoutY(312*heightScaleFactor);
            b11.setLayoutX(419*widthScaleFactor);
            b11.setLayoutY(301*heightScaleFactor);
        }


        wa1.setLayoutX(317*widthScaleFactor);
        wa1.setLayoutY(2*heightScaleFactor);
        wa2.setLayoutX(383*widthScaleFactor);
        wa2.setLayoutY(2*heightScaleFactor);
        wa3.setLayoutX(449*widthScaleFactor);
        wa3.setLayoutY(2*heightScaleFactor);
        wa4.setLayoutX(15.5*widthScaleFactor);
        wa4.setLayoutY(152.5*heightScaleFactor);
        wa5.setLayoutX(15.5*widthScaleFactor);
        wa5.setLayoutY(218.5*heightScaleFactor);
        wa6.setLayoutX(15.5*widthScaleFactor);
        wa6.setLayoutY(284.5*heightScaleFactor);
        wa7.setLayoutX(529.5*widthScaleFactor);
        wa7.setLayoutY(243.5*heightScaleFactor);
        wa8.setLayoutX(529.5*widthScaleFactor);
        wa8.setLayoutY(309.5*heightScaleFactor);
        wa9.setLayoutX(529.5*widthScaleFactor);
        wa9.setLayoutY(375.5*heightScaleFactor);


        a1.setLayoutX(317*widthScaleFactor);
        a1.setLayoutY(2*heightScaleFactor);
        a2.setLayoutX(383*widthScaleFactor);
        a2.setLayoutY(2*heightScaleFactor);
        a3.setLayoutX(449*widthScaleFactor);
        a3.setLayoutY(2*heightScaleFactor);
        a4.setLayoutX(2*widthScaleFactor);
        a4.setLayoutY(166*heightScaleFactor);
        a5.setLayoutX(2*widthScaleFactor);
        a5.setLayoutY(232*heightScaleFactor);
        a6.setLayoutX(2*widthScaleFactor);
        a6.setLayoutY(298*heightScaleFactor);
        a7.setLayoutX(516*widthScaleFactor);
        a7.setLayoutY(257*heightScaleFactor);
        a8.setLayoutX(516*widthScaleFactor);
        a8.setLayoutY(323*heightScaleFactor);
        a9.setLayoutX(516*widthScaleFactor);
        a9.setLayoutY(389*heightScaleFactor);

        deck.setLayoutX(522*widthScaleFactor);
        deck.setLayoutY(121*heightScaleFactor);

        dw.setLayoutX(522*widthScaleFactor);
        dw.setLayoutY(121*heightScaleFactor);
        dp.setLayoutX(537*widthScaleFactor);
        dp.setLayoutY(24*heightScaleFactor);


        Scene scene=new Scene(gp, 1200, 650);
        scene.getStylesheets().addAll(ProvaWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(true);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Bye.byebye();
                stage.close();
            }
        });
        stage.show();
    }

    /*-------- ADRENALINE UI METHODS --------*/
    @Override
    public void onJoinRoomAdvise(String nickname) {

    }

    @Override
    public void onExitRoomAdvise(String nickname) {

    }

    @Override
    public void onFirstInRoomAdvise() {

    }

    @Override
    public void onPingAdvise() {

    }

    @Override
    public void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {

    }

    @Override
    public void onInvalidMessageReceived(String msg) {

    }

    @Override
    public void onBoardUpdate(SimpleBoard gameBoard) {

    }

    @Override
    public void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {

    }

    @Override
    public void onRespwanRequest(List<Card> powerups) {

    }

    @Override
    public void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {

    }

    @Override
    public void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {

    }

    @Override
    public void onGrabbedPowerup(SimplePlayer player, Card powerup) {

    }

    @Override
    public void onGrabbableWeapons(List<Card> weapons) {

    }

    @Override
    public void onDiscardWeapon(List<Card> weapons) {

    }

    @Override
    public void onGrabbedWeapon(SimplePlayer player, Card weapon) {

    }

    @Override
    public void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {

    }

    @Override
    public void onReloadableWeapons(List<Card> weapons) {

    }

    @Override
    public void onTurnActions(List<String> actions) {

    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public void onMoveAction(SimplePlayer player) {

    }

    @Override
    public void onMoveRequest(MatrixHelper matrix, String targetPlayer) {

    }

    @Override
    public void onMarkAction(String player, SimplePlayer selected, int value) {

    }

    @Override
    public void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {

    }

    @Override
    public void onDiscardedPowerup(SimplePlayer player, Card powerup) {

    }

    @Override
    public void onTurnCreation(String currentPlayer) {

    }

    @Override
    public void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {

    }

    @Override
    public void onCanUsePowerup() {

    }

    @Override
    public void onCanStopRoutine() {

    }

    @Override
    public void onUsableWeapons(List<Card> usableWeapons) {

    }

    @Override
    public void onAvailableEffects(List<String> effects) {

    }

    @Override
    public void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {

    }

    @Override
    public void onUsedCard(Card card) {

    }

    @Override
    public void onAvailablePowerups(List<Card> powerups) {

    }

    @Override
    public void onRunCompleted(SimplePlayer player, int[] newPosition) {

    }

    @Override
    public void onRunRoutine(MatrixHelper matrix) {

    }
}

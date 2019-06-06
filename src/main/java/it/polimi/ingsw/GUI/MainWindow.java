package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.ui.GUIHandler;
import it.polimi.ingsw.utils.MatrixHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//mainWind
public class MainWindow extends Application {

    /* NETWORKING ATTRIBUTES */
    private static String hostname;
    private static GUIHandler networkHandler;
    private static ServerConnection connection;
    private static String logNickname;
    private static List<String> playerInRoom;


    private static boolean init=false;
    private static Button user1;
    private static Button user2;
    private static Button user3;
    private static Button user4;
    private static Button user5;

    private static Button shoot;
    private static Button grab;
    private static Button move;
    private static Button loadWeapon;

    private static Image playerBoard;
    private static Image pla1;
    private static Image pla2;
    private static Image pla3;
    private static Image pla4;

    private static Button inf1;
    private static Button inf2;
    private static Button inf3;
    private static Button inf4;

    private static Label ny;
    private static Label nb;
    private static Label nr;

    private static Button b1;
    private static Button b2;
    private static Button b3;
    private static Button b4;
    private static Button b5;
    private static Button b6;
    private static Button b7;
    private static Button b8;
    private static Button b9;
    private static Button b10;
    private static Button b11;
    private static Button b12;

    private static Button a1;
    private static Button a2;
    private static Button a3;
    private static Button a4;
    private static Button a5;
    private static Button a6;
    private static Button a7;
    private static Button a8;
    private static Button a9;

    private static Label labelweap1;
    private static Label labelweap2;
    private static Label labelweap3;
    private static Label labelpow1;
    private static Label labelpow2;
    private static Label labelpow3;
    private static Label labelpow4;

    private static Button ammsq1;
    private static Button ammsq2;
    private static Button ammsq3;
    private static Button ammsq4;
    private static Button ammsq5;
    private static Button ammsq6;
    private static Button ammsq7;
    private static Button ammsq8;
    private static Button ammsq9;

    private static Image weap1;
    private static Image weap2;
    private static Image weap3;
    private static Image powerup1;
    private static Image powerup2;
    private static Image powerup3;
    private static Image powerup4;

    private static Image wea1;
    private static Image wea2;
    private static Image wea3;
    private static Image wea4;
    private static Image wea5;
    private static Image wea6;
    private static Image wea7;
    private static Image wea8;
    private static Image wea9;

    private static ImageView wa1;
    private static ImageView wa2;
    private static ImageView wa3;
    private static ImageView wa4;
    private static ImageView wa5;
    private static ImageView wa6;
    private static ImageView wa7;
    private static ImageView wa8;
    private static ImageView wa9;

    private static Label nfmarkdr;
    private static Label nsmarkdr;
    private static Label ntmarkdr;
    private static Label nfomarkdr;
    private static Label nfimarkdr;

    private static ImageView wp1;
    private static Button myw1;
    private static ImageView wp2;
    private static Button myw2;
    private static ImageView wp3;
    private static Button myw3;
    private static ImageView pu1;
    private static Button myp1;
    private static ImageView pu2;
    private static Button myp2;
    private static ImageView pu3;
    private static Button myp3;
    private static ImageView pu4;
    private static Button myp4;

    private static Image yammo;
    private static ImageView yam;
    private static Image bammo;
    private static ImageView bam;
    private static Image rammo;
    private static ImageView ram;

    private static ImageView pl1;
    private static ImageView pl2;
    private static ImageView pl3;
    private static ImageView pl4;

    private static Image dstructor;
    private static Image dozer;
    private static Image banshee;
    private static Image sprog;
    private static Image violet;


    private static Label mess;
    private static Button yes;
    private static Button no;

    private static Label myWeap1Label;
    private static Label myWeap2Label;
    private static Label myWeap3Label;
    private static Label myPowerup1Label;
    private static Label myPowerup2Label;
    private static Label myPowerup3Label;



    private static HashMap<Integer, String> plBhashmap= new HashMap<>();

    private static Stage zoomedImage;

    protected static String urlmap;
    protected static Image image;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws  Exception {
        MainWindow.stage=stage;

        /*INITIALIZE CONNECTION HANDLER*/
        networkHandler=new GUIHandler();
        playerInRoom=new ArrayList<>(4);

        LoginWindow.log(stage);
        //initGameWindow();
        plBhashmap.put(0, "/gui/playerBoard.png");
        plBhashmap.put(1, "/gui/pl1.png");
        plBhashmap.put(2, "/gui/pl2.png");
        plBhashmap.put(3, "/gui/pl3.png");
        plBhashmap.put(4, "/gui/pl4.png");
    }

    /**
     * This method is called when the player successfully logged in.
     * This method open the WaitingRoomWindow and close the LoginWindow
     */
    public static void onLoginCompleted() {
        stage.close();
        WaitingRoomWindow.create(stage);
        if(playerInRoom.isEmpty())
            WaitingRoomWindow.getArea().appendText("You are the first in the room! \n");
        for (String s:playerInRoom) {
            WaitingRoomWindow.getArea().appendText(s+" is in the room...\n");
        }
    }

    public static void initGameWindow(){

        stage.setTitle("ADRENALINE");

        boolean mapp1=true;
        boolean mapp2=false;
        boolean mapp3=false;
        boolean mapp4=false;
        //if(urlmap.contains("/gui/map1.png")) mapp1=true;
        //if(urlmap.contains("/gui/map2.png")) mapp2=true;
        //if(urlmap.contains("/gui/map3.png")) mapp3=true;
        //if(urlmap.contains("/gui/map4.png")) mapp4=true;
        image=new Image("/gui/map1.png");
        ImageView map=new ImageView(image);
        configImageView(map, 600, 454, 0, 0);
        playerBoard= new Image("/gui/pl4fr.png");
        ImageView plB= new ImageView(playerBoard);
        configImgv(plB, 600, 0, 480);

        AnchorPane gp= new AnchorPane();

        myWeap1Label=new Label("Usable");
        myPowerup1Label=new Label("Usable");
        setPosLabel(myWeap1Label, 640, 520);
        setPosLabel(myPowerup1Label, 640, 380);


        mess=new Label("Vuoi raccogliere?");
        setPosLabel(mess, 1030, 50);
        mess.setPrefWidth(180);

        yes=new Button("Si");
        setPosButton(yes, 1070, 100);
        yes.setStyle("-fx-background-color: green");
        yes.setDisable(false);
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                grab.setDisable(false);
                yes.setDisable(true);
                no.setDisable(true);
                //manda true
            }
        });

        no=new Button("No");
        setPosButton(no, 1100, 100);
        no.setStyle("-fx-background-color: red");
        no.setDisable(false);
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                no.setDisable(true);
                yes.setDisable(true);
                //manda false
            }
        });



        b1= new Button();
        b2=new Button();
        b3=new Button();
        b4=new Button();
        b5= new Button();
        b6= new Button();
        b7= new Button();
        b8= new Button();
        b9= new Button();
        b10= new Button();





        inf1= new Button("Info");
        inf2= new Button("Info");
        inf3= new Button("Info");
        inf4= new Button("Info");
        inf1.getStyleClass().add("info");
        inf2.getStyleClass().add("info");
        inf3.getStyleClass().add("info");
        inf4.getStyleClass().add("info");

        shoot= new Button("Shoot");
        grab= new Button("Grab");
        move= new Button("Move");
        loadWeapon= new Button("Load Weapon");
        shoot.getStyleClass().add("actionbuttons");
        grab.getStyleClass().add("actionbuttons");
        move.getStyleClass().add("actionbuttons");
        loadWeapon.getStyleClass().add("actionbuttons");
        shoot.setPrefWidth(120);
        grab.setPrefWidth(120);
        move.setPrefWidth(120);
        loadWeapon.setPrefWidth(120);
        shoot.setDisable(true);
        grab.setDisable(true);
        move.setDisable(true);
        loadWeapon.setDisable(true);
        grab.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mess.setText("Raccogli");
                grab.setDisable(true);
            }
        });





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





        ammsq1= new Button();
        ammsq2= new Button();
        ammsq3= new Button();
        ammsq4= new Button();
        ammsq5= new Button();
        ammsq6= new Button();
        ammsq7= new Button();

        if(mapp1) initMap1(gp);






        //gp.getChildren().addAll(map, plB, fmarkdr, nfmarkdr, smarkdr, nsmarkdr, tmarkdr, ntmarkdr, fomarkdr, nfomarkdr, fimarkdr, nfimarkdr, firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, pl1, pl2, pl3, pl4, fmarkdr1, nfmarkdr1, smarkdr1, nsmarkdr1, tmarkdr1, ntmarkdr1, fomarkdr1, nfomarkdr1, fimarkdr1, nfimarkdr1, fmarkdr2, nfmarkdr2, smarkdr2, nsmarkdr2, tmarkdr2, ntmarkdr2, fomarkdr2, nfomarkdr2, fimarkdr2, nfimarkdr2, fmarkdr3, nfmarkdr3, smarkdr3, nsmarkdr3, tmarkdr3, ntmarkdr3, fomarkdr3, nfomarkdr3, fimarkdr3, nfimarkdr3, fmarkdr4, nfmarkdr4, smarkdr4, nsmarkdr4, tmarkdr4, ntmarkdr4, fomarkdr4, nfomarkdr4, fimarkdr4, nfimarkdr4, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, o3sdr, o4sdr, ss2dr, s3sdr, s4sdr, ts2dr, t3sdr, t4sdr, fs2dr, f3sdr, f4sdr, fi2sdr, fi3sdr, fi4sdr, si2sdr, si3sdr, si4sdr, se2sdr, se3sdr, se4sdr, e2sdr, e3sdr, e4sdr, n2sdr, n3sdr, n4sdr, te2sdr, te3sdr, te4sdr, el2sdr, el3sdr, el4sdr, tw2sdr, tw3sdr, tw4sdr, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7);
        //aggiungere per mappe con piu cose di map2 e aggiungere users


        if(mapp1) {

            user1=new Button();
            user1.setMinSize(8,9);
            configButton(user1, 8, 9, 123, 150);
            user1.setStyle("-fx-background-image: url('/gui/user1.png')");

            user2=new Button();
            user2.setMinSize(8,9);
            configButton(user2, 8, 9, 138, 150);
            user2.setStyle("-fx-background-image: url('/gui/user2.png')");

            user3= new Button();
            user3.setMinSize(8,9);
            configButton(user3, 8, 9, 153, 150);
            user3.setStyle("-fx-background-image: url('/gui/user3.png')");

            user4= new Button();
            user4.setMinSize(8,9);
            configButton(user4, 8, 9, 168, 150);
            user4.setStyle("-fx-background-image: url('/gui/user4.png')");

            user5= new Button();
            user5.setMinSize(8,9);
            configButton(user5, 8, 9, 183, 150);
            user5.setStyle("-fx-background-image: url('/gui/user5.png')");

        }

        configPlayerBoards();
        configMyWeap();
        configMyPowerups();
        configMyAmmo();
        configMyDamages();
        configMyMarks();
        configPl1Damages();
        configPl1Marks();
        configPl2Damages();
        configPl2Marks();
        configPl3Damages();
        configPl3Marks();
        configPl4Damages();
        configPl4Marks();
        configWeapBoardImg();
        configWeapBoardButtons();
        configUserButtons();

        a1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                wp1.setImage(wea1);
                wa1.setImage(null);
                myw1.setStyle("-fx-border-color: transparent");
            }
        });

        setPosButton(shoot, 5, 640);
        setPosButton(grab, 155, 640);
        setPosButton(move, 305, 640);
        setPosButton(loadWeapon, 460, 640);


        ny.setLayoutX(980);
        ny.setLayoutY(505);
        nb.setLayoutX(980);
        nb.setLayoutY(555);
        nr.setLayoutX(980);
        nr.setLayoutY(605);


        setPosButton(inf1, 940, 45);
        setPosButton(inf2, 940, 125);
        setPosButton(inf3, 940, 205);
        setPosButton(inf4, 940, 285);

        deck.setLayoutX(522);
        deck.setLayoutY(121);

        dw.setLayoutX(522);
        dw.setLayoutY(121);
        dp.setLayoutX(537);
        dp.setLayoutY(24);



        gp.getChildren().addAll(map, plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, user1, user2, user3, user4, user5, myPowerup1Label, myWeap1Label, mess, yes, no);


        Scene scene=new Scene(gp, 1200, 650);
        scene.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
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

    public static void main(String args[]){
        launch(args);
    }

    private static void setPosButton(Button button, int x, int y){
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    private static void configButton(Button button, int prefWidth, int prefHeight, int x, int y){
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(prefHeight);
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    private static void configTranspButton(Button button, int prefWidth, int prefHeight, int x, int y){
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(prefHeight);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setStyle("-fx-background-color: transparent");
    }

    private static void configImg(ImageView img, int width, int x, int y){
        img.setFitWidth(width);
        img.setPreserveRatio(true);
        img.setLayoutX(x);
        img.setLayoutY(y);
    }

    private static void setPosLabel(Label label, int x, int y){
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    private static void configImageView(ImageView imgv, double width, double height, double x, double y){
        imgv.setFitWidth(width);
        imgv.setFitHeight(height);
        imgv.setLayoutX(x);
        imgv.setLayoutY(y);
    }

    private static void configImgv(ImageView imgv, double width, double x, double y){
        imgv.setFitWidth(width);
        imgv.setPreserveRatio(true);
        imgv.setLayoutX(x);
        imgv.setLayoutY(y);
    }


    private static Stage zoomWeapp(Image weap){
        AnchorPane weapshow = new AnchorPane();
        ImageView bigweap = new ImageView(weap);
        bigweap.setFitWidth(150);
        bigweap.setFitHeight(253);
        weapshow.getChildren().add(bigweap);
        Scene wshowscene = new Scene(weapshow, 150, 253);
        Stage wshowstage = new Stage();
        wshowstage.setResizable(false);
        wshowstage.initStyle(StageStyle.UNDECORATED);
        wshowstage.setScene(wshowscene);
        wshowstage.setX(1080);
        wshowstage.setY(390);

        wshowstage.show();
        return wshowstage;
    }

    private static Stage zoomUser(Image us){
        AnchorPane usershow = new AnchorPane();
        ImageView bigUser = new ImageView(us);
        bigUser.setFitWidth(129);
        bigUser.setFitHeight(137);
        usershow.getChildren().add(bigUser);
        Scene showscene = new Scene(usershow, 129, 137);
        Stage showstage = new Stage();
        showstage.setResizable(false);
        showstage.initStyle(StageStyle.UNDECORATED);
        showstage.setScene(showscene);
        showstage.setX(1080);
        showstage.setY(400);

        showstage.show();
        return showstage;
    }

    private static void setMyWeapAction(Button myw){
        myw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw.setStyle("-fx-background-color: grey");

            }
        });
    }


    private static void configMyWeap(){
        weap1= new Image("/gui/spadafotonica.png");
        wp1= new ImageView(weap1);
        configImgv(wp1, 80, 630, 500);
        myw1= new Button();
        configTranspButton(myw1, 80, 135, 630, 500);
        setMyWeapAction(myw1);
        myw1.setDisable(true);

        weap2= new Image("/gui/cyberguanto.png");
        wp2= new ImageView(weap2);
        configImgv(wp2, 80, 720, 500);
        myw2= new Button();
        configTranspButton(myw2, 80, 135, 720, 500);
        setMyWeapAction(myw2);

        weap3= new Image("/gui/martelloionico.png");
        wp3= new ImageView(weap3);
        configImgv(wp3, 80, 810, 500);
        myw3= new Button();
        configTranspButton(myw3, 80, 135, 810, 500);
        setMyWeapAction(myw3);
        myw3.setDisable(true);


    }

    private static void setMyPowAction(Button myp){
        myp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp.setStyle("-fx-background-color: grey");

            }
        });
    }

    private static void configMyPowerups(){
        powerup1= new Image("/gui/granatavenom.png");
        pu1= new ImageView(powerup1);
        configImgv(pu1, 80, 630, 360);
        myp1= new Button();
        configTranspButton(myp1, 80, 135, 630, 360);
        setMyPowAction(myp1);
        myp1.setDisable(true);

        powerup2= new Image("/gui/teletrasporto.png");
        pu2= new ImageView(powerup2);
        configImgv(pu2, 80, 720, 360);
        myp2= new Button();
        configTranspButton(myp2, 80, 135, 720, 360);
        myp2.setStyle("-fx-border-color: red");
        setMyPowAction(myp2);
        myp2.setDisable(true);

        powerup3= new Image("/gui/raggiocinetico.png");
        pu3= new ImageView(powerup3);
        configImgv(pu3, 80, 810, 360);
        myp3= new Button();
        configTranspButton(myp3, 80, 135, 810, 360);
        myp3.setStyle("-fx-border-color: red");
        setMyPowAction(myp3);
        myp3.setDisable(true);

        powerup4= new Image("/gui/mirino.png");
        pu4= new ImageView(powerup4);
        configImgv(pu4, 80, 900, 360);
        myp4= new Button();
        configTranspButton(myp4,80, 135, 900, 360);
        myp4.setStyle("-fx-border-color: red");
        setMyPowAction(myp4);
        myp4.setDisable(true);
    }

    private static void configMyAmmo(){
        yammo= new Image("/gui/yammo.png");
        yam= new ImageView(yammo);
        configImgv(yam, 40, 920, 500);
        bammo= new Image("/gui/bammo.png");
        bam= new ImageView(bammo);
        configImgv(bam, 40, 920, 550);
        rammo= new Image("/gui/rammo.png");
        ram= new ImageView(rammo);
        configImgv(ram, 40, 920, 600);

        ny=new Label("x 3");
        nb=new Label("x 3");
        nr= new Label("x 3");
    }

    private static void configPlayerBoards(){
        pla1= new Image("/gui/pl1.png");
        pl1= new ImageView(pla1);
        configImgv(pl1, 304, 630, 20);
        pla2= new Image("/gui/pl2.png");
        pl2= new ImageView(pla2);
        configImgv(pl2, 304, 630, 100);
        pla3= new Image("/gui/pl3.png");
        pl3= new ImageView(pla3);
        configImgv(pl3, 304, 630, 180);
        pla4= new Image("/gui/pl4.png");
        pl4= new ImageView(pla4);
        configImgv(pl4, 304, 630, 260);
    }

    private static void configMyMarks(){
        Image fmarkdrop=new Image("/gui/greydrop.png");
        ImageView fmarkdr= new ImageView(fmarkdrop);
        configImg(fmarkdr, 15, 325, 485);
        nfmarkdr=new Label("2");
        nfmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfmarkdr, 329, 488);

        Image smarkdrop=new Image("/gui/bluedrop.png");
        ImageView smarkdr= new ImageView(smarkdrop);
        configImg(smarkdr, 15, 350, 485);
        nsmarkdr=new Label("2");
        nsmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nsmarkdr, 354, 488);

        Image tmarkdrop=new Image("/gui/greendrop.png");
        ImageView tmarkdr= new ImageView(tmarkdrop);
        configImg(tmarkdr, 15, 375, 485);
        ntmarkdr=new Label("2");
        ntmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(ntmarkdr, 379, 488);

        Image fomarkdrop=new Image("/gui/purpledrop.png");
        ImageView fomarkdr= new ImageView(fomarkdrop);
        configImg(fomarkdr, 15, 400, 485);
        nfomarkdr=new Label("2");
        nfomarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfomarkdr, 404, 488);

        Image fimarkdrop=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr= new ImageView(fimarkdrop);
        configImg(fimarkdr, 15, 425, 485);
        nfimarkdr=new Label("0");
        nfimarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfimarkdr, 429, 488);
    }

    private static void configMyDamages(){
        Image secdrop=new Image("/gui/greydrop.png");
        ImageView secdr= new ImageView(secdrop);
        configImg(secdr, 19, 92, 542);

        Image firstdrop=new Image("/gui/greendrop.png");
        ImageView firstdr= new ImageView(firstdrop);
        configImg(firstdr, 19, 57, 542);

        Image thirddrop=new Image("/gui/bluedrop.png");
        ImageView thirddr= new ImageView(thirddrop);
        configImg(thirddr, 19, 129, 542);
        Image fdrop=new Image("/gui/bluedrop.png");
        ImageView fdr= new ImageView(fdrop);
        configImg(fdr, 19, 163, 542);
        Image fidrop=new Image("/gui/bluedrop.png");
        ImageView fidr= new ImageView(fidrop);
        configImg(fidr, 19, 196, 542);
        Image sdrop=new Image("/gui/bluedrop.png");
        ImageView sdr= new ImageView(sdrop);
        configImg(sdr, 19, 233, 542);
        Image sedrop=new Image("/gui/bluedrop.png");
        ImageView sedr= new ImageView(sedrop);
        configImg(sedr, 19, 266, 542);
        Image edrop=new Image("/gui/bluedrop.png");
        ImageView edr= new ImageView(edrop);
        configImg(edr, 19, 299, 542);
        Image ndrop=new Image("/gui/bluedrop.png");
        ImageView ndr= new ImageView(ndrop);
        configImg(ndr, 19, 332, 542);
        Image tdrop=new Image("/gui/bluedrop.png");
        ImageView tdr= new ImageView(tdrop);
        configImg(tdr, 19, 365, 542);
        Image eldrop=new Image("/gui/bluedrop.png");
        ImageView eldr= new ImageView(eldrop);
        configImg(eldr, 19, 398, 542);
        Image twdrop=new Image("/gui/bluedrop.png");
        ImageView twdr= new ImageView(twdrop);
        configImg(twdr, 19, 431, 542);
    }

    private static void configPl1Marks(){
        Image fmarkdrop1=new Image("/gui/greydrop.png");
        ImageView fmarkdr1= new ImageView(fmarkdrop1);
        configImg(fmarkdr1, 8, 792, 22);
        Label nfmarkdr1=new Label("2");
        nfmarkdr1.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr1, 794, 24);

        Image smarkdrop1=new Image("/gui/bluedrop.png");
        ImageView smarkdr1= new ImageView(smarkdrop1);
        configImg(smarkdr1, 8, 804, 22);
        Label nsmarkdr1=new Label("2");
        nsmarkdr1.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr1, 806, 24);

        Image tmarkdrop1=new Image("/gui/greendrop.png");
        ImageView tmarkdr1= new ImageView(tmarkdrop1);
        configImg(tmarkdr1, 8, 816, 22);
        Label ntmarkdr1=new Label("2");
        ntmarkdr1.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr1, 818, 24);

        Image fomarkdrop1=new Image("/gui/purpledrop.png");
        ImageView fomarkdr1= new ImageView(fomarkdrop1);
        configImg(fomarkdr1, 8, 828, 22);
        Label nfomarkdr1=new Label("2");
        nfomarkdr1.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr1, 830, 24);

        Image fimarkdrop1=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr1= new ImageView(fimarkdrop1);
        configImg(fimarkdr1, 8, 840, 22);
        Label nfimarkdr1=new Label("0");
        nfimarkdr1.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr1, 842, 24);
    }

    private static void configPl1Damages(){
        Image osdrop=new Image("/gui/bluedrop.png");
        ImageView osdr= new ImageView(osdrop);
        configImg(osdr, 11, 659, 49);
        Image ssdrop=new Image("/gui/bluedrop.png");
        ImageView ssdr= new ImageView(ssdrop);
        configImg(ssdr, 11, 675, 49);
        Image tsdrop=new Image("/gui/bluedrop.png");
        ImageView tsdr= new ImageView(tsdrop);
        configImg(tsdr,11, 693, 49);
        Image fsdrop=new Image("/gui/bluedrop.png");
        ImageView fsdr= new ImageView(fsdrop);
        configImg(fsdr, 11, 709, 49);
        Image fisdrop=new Image("/gui/bluedrop.png");
        ImageView fisdr= new ImageView(fisdrop);
        configImg(fisdr, 11, 726, 49);
        Image sisdrop=new Image("/gui/bluedrop.png");
        ImageView sisdr= new ImageView(sisdrop);
        configImg(sisdr, 11, 743, 49);
        Image sesdrop=new Image("/gui/bluedrop.png");
        ImageView sesdr= new ImageView(sesdrop);
        configImg(sesdr, 11, 760, 49);
        Image esdrop=new Image("/gui/bluedrop.png");
        ImageView esdr= new ImageView(esdrop);
        configImg(esdr, 11, 777, 49);
        Image nsdrop=new Image("/gui/bluedrop.png");
        ImageView nsdr= new ImageView(nsdrop);
        configImg(nsdr, 11, 794, 49);
        Image tesdrop=new Image("/gui/bluedrop.png");
        ImageView tesdr= new ImageView(tesdrop);
        configImg(tesdr, 11, 811, 49);
        Image elsdrop=new Image("/gui/bluedrop.png");
        ImageView elsdr= new ImageView(elsdrop);
        configImg(elsdr, 11, 828, 49);
        Image twsdrop=new Image("/gui/bluedrop.png");
        ImageView twsdr= new ImageView(twsdrop);
        configImg(twsdr, 11, 845, 49);
    }

    private static void configPl2Marks(){
        Image fmarkdrop2=new Image("/gui/greydrop.png");
        ImageView fmarkdr2= new ImageView(fmarkdrop2);
        configImg(fmarkdr2, 8, 792, 102);
        Label nfmarkdr2=new Label("2");
        nfmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr2, 794, 104);

        Image smarkdrop2=new Image("/gui/bluedrop.png");
        ImageView smarkdr2= new ImageView(smarkdrop2);
        configImg(smarkdr2, 8, 804, 102);
        Label nsmarkdr2=new Label("2");
        nsmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr2, 806, 104);

        Image tmarkdrop2=new Image("/gui/greendrop.png");
        ImageView tmarkdr2= new ImageView(tmarkdrop2);
        configImg(tmarkdr2, 8, 816, 102);
        Label ntmarkdr2=new Label("2");
        ntmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr2, 818, 104);

        Image fomarkdrop2=new Image("/gui/purpledrop.png");
        ImageView fomarkdr2= new ImageView(fomarkdrop2);
        configImg(fomarkdr2, 8, 828, 102);
        Label nfomarkdr2=new Label("2");
        nfomarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr2, 830, 104);

        Image fimarkdrop2=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr2= new ImageView(fimarkdrop2);
        configImg(fimarkdr2, 8, 840, 102);
        Label nfimarkdr2=new Label("0");
        nfimarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr2, 842, 104);
    }

    private static void configPl2Damages(){
        Image os2drop=new Image("/gui/bluedrop.png");
        ImageView os2dr= new ImageView(os2drop);
        configImg(os2dr, 11, 659, 129);
        Image ss2drop=new Image("/gui/bluedrop.png");
        ImageView ss2dr= new ImageView(ss2drop);
        configImg(ss2dr, 11, 675, 129);
        Image ts2drop=new Image("/gui/bluedrop.png");
        ImageView ts2dr= new ImageView(ts2drop);
        configImg(ts2dr, 11, 693, 129);
        Image fs2drop=new Image("/gui/bluedrop.png");
        ImageView fs2dr= new ImageView(fs2drop);
        configImg(fs2dr, 11, 709, 129);
        Image fi2sdrop=new Image("/gui/bluedrop.png");
        ImageView fi2sdr= new ImageView(fi2sdrop);
        configImg(fi2sdr, 11, 726, 129);
        Image si2sdrop=new Image("/gui/bluedrop.png");
        ImageView si2sdr= new ImageView(si2sdrop);
        configImg(si2sdr, 11, 743, 129);
        Image se2sdrop=new Image("/gui/bluedrop.png");
        ImageView se2sdr= new ImageView(se2sdrop);
        configImg(se2sdr, 11, 760, 129);
        Image e2sdrop=new Image("/gui/bluedrop.png");
        ImageView e2sdr= new ImageView(e2sdrop);
        configImg(e2sdr,11, 777, 129);
        Image n2sdrop=new Image("/gui/bluedrop.png");
        ImageView n2sdr= new ImageView(n2sdrop);
        configImg(n2sdr, 11, 794, 129);
        Image te2sdrop=new Image("/gui/bluedrop.png");
        ImageView te2sdr= new ImageView(te2sdrop);
        configImg(te2sdr, 11, 811, 129);
        Image el2sdrop=new Image("/gui/bluedrop.png");
        ImageView el2sdr= new ImageView(el2sdrop);
        configImg(el2sdr, 11, 828, 129);
        Image tw2sdrop=new Image("/gui/bluedrop.png");
        ImageView tw2sdr= new ImageView(tw2sdrop);
        configImg(tw2sdr, 11, 845, 129);
    }

    private static void configPl3Marks(){
        Image fmarkdrop3=new Image("/gui/greydrop.png");
        ImageView fmarkdr3= new ImageView(fmarkdrop3);
        configImg(fmarkdr3, 8, 792, 182);
        Label nfmarkdr3=new Label("2");
        nfmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr3, 794, 184);

        Image smarkdrop3=new Image("/gui/bluedrop.png");
        ImageView smarkdr3= new ImageView(smarkdrop3);
        configImg(smarkdr3, 8, 804, 182);
        Label nsmarkdr3=new Label("2");
        nsmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr3, 806, 184);

        Image tmarkdrop3=new Image("/gui/greendrop.png");
        ImageView tmarkdr3= new ImageView(tmarkdrop3);
        configImg(tmarkdr3, 8, 816, 182);
        Label ntmarkdr3=new Label("2");
        ntmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr3, 818, 184);

        Image fomarkdrop3=new Image("/gui/purpledrop.png");
        ImageView fomarkdr3= new ImageView(fomarkdrop3);
        configImg(fomarkdr3, 8, 828, 182);
        Label nfomarkdr3=new Label("2");
        nfomarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr3, 830,184);

        Image fimarkdrop3=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr3= new ImageView(fimarkdrop3);
        configImg(fimarkdr3, 8, 840, 182);
        Label nfimarkdr3=new Label("0");
        nfimarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr3, 842, 184);
    }

    private static void configPl3Damages(){
        Image o3sdrop=new Image("/gui/bluedrop.png");
        ImageView o3sdr= new ImageView(o3sdrop);
        configImg(o3sdr, 11, 659, 209);
        Image s3sdrop=new Image("/gui/bluedrop.png");
        ImageView s3sdr= new ImageView(s3sdrop);
        configImg(s3sdr, 11, 675, 209);
        Image t3sdrop=new Image("/gui/bluedrop.png");
        ImageView t3sdr= new ImageView(t3sdrop);
        configImg(t3sdr, 11, 693, 209);
        Image f3sdrop=new Image("/gui/bluedrop.png");
        ImageView f3sdr= new ImageView(f3sdrop);
        configImg(f3sdr, 11, 709, 209);
        Image fi3sdrop=new Image("/gui/bluedrop.png");
        ImageView fi3sdr= new ImageView(fi3sdrop);
        configImg(fi3sdr, 11, 726, 209);
        Image si3sdrop=new Image("/gui/bluedrop.png");
        ImageView si3sdr= new ImageView(si3sdrop);
        configImg(si3sdr, 11, 743, 209);
        Image se3sdrop=new Image("/gui/bluedrop.png");
        ImageView se3sdr= new ImageView(se3sdrop);
        configImg(se3sdr, 11, 760, 209);
        Image e3sdrop=new Image("/gui/bluedrop.png");
        ImageView e3sdr= new ImageView(e3sdrop);
        configImg(e3sdr, 11, 777, 209);
        Image n3sdrop=new Image("/gui/bluedrop.png");
        ImageView n3sdr= new ImageView(n3sdrop);
        configImg(n3sdr, 11, 794, 209);
        Image te3sdrop=new Image("/gui/bluedrop.png");
        ImageView te3sdr= new ImageView(te3sdrop);
        configImg(te3sdr, 11, 811, 209);
        Image el3sdrop=new Image("/gui/bluedrop.png");
        ImageView el3sdr= new ImageView(el3sdrop);
        configImg(el3sdr, 11, 828, 209);
        Image tw3sdrop=new Image("/gui/bluedrop.png");
        ImageView tw3sdr= new ImageView(tw3sdrop);
        configImg(tw3sdr, 11, 845, 209);
    }

    private static void configPl4Marks(){
        Image fmarkdrop4=new Image("/gui/greydrop.png");
        ImageView fmarkdr4= new ImageView(fmarkdrop4);
        configImg(fmarkdr4, 8, 792, 262);
        Label nfmarkdr4=new Label("2");
        nfmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr4, 794, 264);

        Image smarkdrop4=new Image("/gui/bluedrop.png");
        ImageView smarkdr4= new ImageView(smarkdrop4);
        configImg(smarkdr4, 8, 804, 262);
        Label nsmarkdr4=new Label("2");
        nsmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr4, 806, 264);

        Image tmarkdrop4=new Image("/gui/greendrop.png");
        ImageView tmarkdr4= new ImageView(tmarkdrop4);
        configImg(tmarkdr4, 8, 816, 262);
        Label ntmarkdr4=new Label("2");
        ntmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr4, 818, 264);

        Image fomarkdrop4=new Image("/gui/purpledrop.png");
        ImageView fomarkdr4= new ImageView(fomarkdrop4);
        configImg(fomarkdr4, 8, 828, 262);
        Label nfomarkdr4=new Label("2");
        nfomarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr4, 830, 264);

        Image fimarkdrop4=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr4= new ImageView(fimarkdrop4);
        configImg(fimarkdr4, 8, 840, 262);
        Label nfimarkdr4=new Label("0");
        nfimarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr4, 842, 264);
    }

    private static void configPl4Damages(){
        Image o4sdrop=new Image("/gui/bluedrop.png");
        ImageView o4sdr= new ImageView(o4sdrop);
        configImg(o4sdr, 11, 659, 289);
        Image s4sdrop=new Image("/gui/bluedrop.png");
        ImageView s4sdr= new ImageView(s4sdrop);
        configImg(s4sdr, 11, 675, 289);
        Image t4sdrop=new Image("/gui/bluedrop.png");
        ImageView t4sdr= new ImageView(t4sdrop);
        configImg(t4sdr, 11, 693, 289);
        Image f4sdrop=new Image("/gui/bluedrop.png");
        ImageView f4sdr= new ImageView(f4sdrop);
        configImg(f4sdr, 11, 709, 289);
        Image fi4sdrop=new Image("/gui/bluedrop.png");
        ImageView fi4sdr= new ImageView(fi4sdrop);
        configImg(fi4sdr, 11, 726, 289);
        Image si4sdrop=new Image("/gui/bluedrop.png");
        ImageView si4sdr= new ImageView(si4sdrop);
        configImg(si4sdr, 11, 743, 289);
        Image se4sdrop=new Image("/gui/bluedrop.png");
        ImageView se4sdr= new ImageView(se4sdrop);
        configImg(se4sdr, 11, 760, 289);
        Image e4sdrop=new Image("/gui/bluedrop.png");
        ImageView e4sdr= new ImageView(e4sdrop);
        configImg(e4sdr, 11, 777, 289);
        Image n4sdrop=new Image("/gui/bluedrop.png");
        ImageView n4sdr= new ImageView(n4sdrop);
        configImg(n4sdr, 11, 794, 289);
        Image te4sdrop=new Image("/gui/bluedrop.png");
        ImageView te4sdr= new ImageView(te4sdrop);
        configImg(te4sdr, 11, 811, 289);
        Image el4sdrop=new Image("/gui/bluedrop.png");
        ImageView el4sdr= new ImageView(el4sdrop);
        configImg(el4sdr, 11, 828, 289);
        Image tw4sdrop=new Image("/gui/bluedrop.png");
        ImageView tw4sdr= new ImageView(tw4sdrop);
        configImg(tw4sdr, 11, 845, 289);
    }

    private static void setActionUserButton(Button user, Image img){
        user.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(img);
            }
        });
        user.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });
    }


    private static void configUserButtons(){
        dstructor=new Image("/gui/dstructor.png");
        setActionUserButton(user1, dstructor);

        dozer= new Image("/gui/dozer.png");
        setActionUserButton(user2, dozer);

        banshee= new Image("/gui/banshee.png");
        setActionUserButton(user3, banshee);

        sprog= new Image("/gui/sprog.png");
        setActionUserButton(user4, sprog);

        violet= new Image("/gui/violet.png");
        setActionUserButton(user5, violet);
    }


    private static void configWeapBoardImg(){
        wea1=new Image("/gui/cannonevortex.png");
        wa1=new ImageView(wea1);
        configImageView(wa1, 57, 84, 317, 2);

        wea2=new Image("/gui/lanciafiamme.png");
        wa2=new ImageView(wea2);
        configImageView(wa2, 57, 84, 383, 2);

        wea3=new Image("/gui/lanciagranate.png");
        wa3=new ImageView(wea3);
        configImageView(wa3, 57, 84, 449, 2);

        wea4=new Image("/gui/mitragliatrice.png");
        wa4=new ImageView(wea4);
        configImageView(wa4, 57, 84, 15.5, 152.5);
        wa4.setRotate(270);

        wea5=new Image("/gui/fucilealplasma.png");
        wa5=new ImageView(wea5);
        configImageView(wa5, 57, 84, 15.5, 218.5);
        wa5.setRotate(270);

        wea6=new Image("/gui/fucilediprecisione.png");
        wa6=new ImageView(wea6);
        configImageView(wa6, 57, 84, 15.5, 284.5);
        wa6.setRotate(270);

        wea7=new Image("/gui/raggiotraente.png");
        wa7=new ImageView(wea7);
        configImageView(wa7, 57, 84, 529.5, 243.5);
        wa7.setRotate(90);

        wea8=new Image("/gui/torpedine.png");
        wa8=new ImageView(wea8);
        configImageView(wa8, 57, 84, 529.5, 309.5);
        wa8.setRotate(90);

        wea9=new Image("/gui/raggiosolare.png");
        wa9=new ImageView(wea9);
        configImageView(wa9, 57, 84, 529.5, 375.5);
        wa9.setRotate(90);
    }

    private static void setActionWeapBoardButton(Button a, Image img){

        a.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(img);
            }
        });
        a.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });
    }

    private static void configWeapBoardButtons(){
        a1=new Button();
        configTranspButton(a1, 57, 84, 317, 2);
        setActionWeapBoardButton(a1, wea1);

        a2=new Button();
        configTranspButton(a2, 57, 84, 383, 2);
        setActionWeapBoardButton(a2, wea2);

        a3=new Button();
        configTranspButton(a3, 57, 84, 449, 2);
        setActionWeapBoardButton(a3, wea3);

        a4=new Button();
        configTranspButton(a4, 84, 57, 2, 166);
        setActionWeapBoardButton(a4, wea4);

        a5=new Button();
        configTranspButton(a5, 84, 57, 2, 232);
        setActionWeapBoardButton(a5, wea5);

        a6=new Button();
        configTranspButton(a6, 84, 57, 2, 298);
        setActionWeapBoardButton(a6, wea6);

        a7=new Button();
        configTranspButton(a7, 84, 57, 516, 257);
        setActionWeapBoardButton(a7, wea7);

        a8=new Button();
        configTranspButton(a8, 84, 57, 516, 323);
        setActionWeapBoardButton(a8, wea8);

        a9=new Button();
        configTranspButton(a9, 84, 57, 516, 389);
        setActionWeapBoardButton(a9, wea9);
    }


    private static void initMap1(AnchorPane ap){
        configTranspButton(b1, 80, 83,111, 105);
        configTranspButton(b2, 97, 83, 201, 106);
        configTranspButton(b3, 95, 80, 303, 108);
        configTranspButton(b4, 68, 65, 419, 125);
        configTranspButton(b5, 82, 84, 113, 201);
        configTranspButton(b6, 94, 86, 202, 211);
        configTranspButton(b7, 77, 99, 322, 201);
        configTranspButton(b8, 78, 91, 408, 204);
        configTranspButton(b9, 98, 86, 197, 314);
        configTranspButton(b10, 82, 83, 314, 311);
        b11=new Button();
        configTranspButton(b11,84, 90, 405, 301);

        configButton(ammsq1, 21, 21, 123, 115);
        ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq2, 21, 21, 216, 160);
        ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        configButton(ammsq3, 21, 21, 461, 160);
        ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        configButton(ammsq4, 21, 21, 207, 237);
        ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        configButton(ammsq5, 21, 21, 324, 268);
        ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        configButton(ammsq6, 21, 21, 414, 268);
        ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        configButton(ammsq7, 21, 21, 209, 354);
        ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        ammsq8= new Button();
        configButton(ammsq8, 21, 21, 329, 354);
        ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        ap.getChildren().add(b11);
        ap.getChildren().add(ammsq8);
    }

    private static void initMap2(){
        configTranspButton(b1, 85, 83,  110, 106);
        configTranspButton(b2, 97, 84, 202, 106);
        configTranspButton(b3, 93, 81, 304, 106);
        configTranspButton(b4, 85, 95, 110, 202);
        configTranspButton(b5, 87, 90, 203, 206);
        configTranspButton(b6, 100, 91,300, 205);
        configTranspButton(b7, 67, 90, 420, 205);
        configTranspButton(b8, 98, 82, 198, 314);
        configTranspButton(b9, 97, 79, 304, 313);
        configTranspButton(b10, 72, 93, 418, 299);

        configButton(ammsq1, 21, 21, 123, 115);
        ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq2, 21, 21, 216, 160);
        ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq3, 21, 21, 207, 237);
        ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq4, 21, 21, 324, 250);
        ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq5, 21, 21, 461, 249);
        ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq6, 21, 21, 209, 354);
        ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq7, 21, 21, 313, 354);
        ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

    }

    private static void initMap3(AnchorPane ap){
        configTranspButton(b1, 71, 94, 110, 105);
        configTranspButton(b2, 96, 82, 202, 106);
        configTranspButton(b3, 95, 79,304, 109);
        configTranspButton(b4, 73, 79,419, 108);
        configTranspButton(b5, 68, 96, 113, 202);
        configTranspButton(b6, 90, 92, 204, 205);
        configTranspButton(b7, 84, 94, 315, 205);
        configTranspButton(b8, 77, 90, 409, 205);
        configTranspButton(b9, 84, 79,  112, 313);
        configTranspButton(b10, 92, 84,  201, 312);
        b11=new Button();
        configTranspButton(b11, 82, 84, 313, 308);
        b12= new Button();
        configTranspButton(b12, 84, 91, 405, 300);

        configButton(ammsq1, 21,21, 126, 164);
        ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq2, 21, 21, 215, 112);
        ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq3, 21, 21, 461, 160);
        ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq4, 21, 21, 208, 240);
        ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq5, 21, 21, 324, 268);
        ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq6, 21, 21, 414, 268);
        ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq7, 21, 21, 126, 354);
        ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        ammsq8= new Button();
        configButton(ammsq8, 21, 21, 209, 354);
        ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        ammsq9= new Button();
        configButton(ammsq9, 21, 21, 329, 354);
        ammsq9.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        ap.getChildren().add(b12);
        ap.getChildren().add(ammsq8);
        ap.getChildren().add(ammsq9);


    }

    private static void initMap4(AnchorPane ap){
        configTranspButton(b1, 71, 94, 111, 104);
        configTranspButton(b2, 97, 82, 202, 106);
        configTranspButton(b3, 93, 80, 304, 108);
        configTranspButton(b4, 68, 91,112, 203);
        configTranspButton(b5, 87, 91, 204, 205);
        configTranspButton(b6, 99, 84, 301, 204);
        configTranspButton(b7, 67, 90, 421, 205);
        configTranspButton(b8, 81, 82, 114, 314);
        configTranspButton(b9, 95, 82, 202, 313);
        configTranspButton(b10, 92, 79, 305, 312);
        b11=new Button();
        configTranspButton(b11, 71, 90, 419, 301);

        configButton(ammsq1, 21, 21, 126, 164);
        ammsq1.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq2, 21, 21, 215, 112);
        ammsq2.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq3, 21, 21, 207, 240);
        ammsq3.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq4, 21, 21, 324, 250);
        ammsq4.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq5, 21, 21, 461, 249);
        ammsq5.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq6, 21, 21, 126, 354);
        ammsq6.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        configButton(ammsq7, 21, 21, 209, 354);
        ammsq7.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
        ammsq8= new Button();
        configButton(ammsq8, 21, 21, 313, 354);
        ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

        ap.getChildren().add(b11);
        ap.getChildren().add(ammsq8);
    }


    public static void ableButtons(List<Button> ableButtons){
        //todo
    }


    /*-------- NETWORKING USEFUL METHODS --------*/
    public static void setHostname(String hostname) {
        MainWindow.hostname = hostname;
    }

    public static String getHostname() {
        return hostname;
    }

    public static void setConnection(ServerConnection connection) {
        MainWindow.connection = connection;
    }

    public static ServerConnection getConnection() {
        return connection;
    }

    public static GUIHandler getNetworkHandler() {
        return networkHandler;
    }

    public static void setLogNickname(String logNickname) {
        MainWindow.logNickname = logNickname;
    }

    public static String getLogNickname() {
        return logNickname;
    }

    /*-------- ADRENALINE UI HANDLER METHODS --------*/

    public static void onJoinRoomAdvise(String nickname) {
        playerInRoom.add(nickname);
        if(WaitingRoomWindow.getArea()!=null){
            WaitingRoomWindow.getArea().appendText(nickname+" join the room...\n");
        }
        /*
        LoginWindow.pl.add(nickname);
        LoginWindow.plOnline.setText(pl.toString());
        */

    }
    
    public static void onExitRoomAdvise(String nickname) {
        playerInRoom.remove(nickname);
        if(WaitingRoomWindow.getArea()!=null){
            WaitingRoomWindow.getArea().appendText(nickname+" left the room...\n");
        }
        /*
        LoginWindow.pl.remove(nickname);
        LoginWindow.plOnline.setText(pl.toString());
        */

    }
    
    public static void onFirstInRoomAdvise() {
        /*
        LoginWindow.plOnline.setText("You are the first player.");
         */
        if(WaitingRoomWindow.getArea()!=null){
            WaitingRoomWindow.getArea().appendText("You are the first in the room!\n");
        }
    }
    
    public static void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        String myPlayB=plBhashmap.get(playerTurnNumber);
        playerBoard= new Image(myPlayB);

        MapChoice.display();
    }
    
    public static void onInvalidMessageReceived(String msg) {
        mess.setText(msg);

    }
    
    public static void onBoardUpdate(SimpleBoard gameBoard) {
        if(init==false){
            MapChoice.stage.close();
            initGameWindow();
            init=true;
        }else{
            //aggiornare la mappa(killshot, armi/ammotile, ...)
        }

    }
    
    public static void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        if(frenzy){

        }
    }
    
    public static void onRespwanRequest(List<Card> powerups) {

    }
    
    public static void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {

    }
    
    public static void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {

    }
    
    public static void onGrabbedPowerup(SimplePlayer player, Card powerup) {

    }
    
    public static void onGrabbableWeapons(List<Card> weapons) {

    }
    
    public static void onDiscardWeapon(List<Card> weapons) {

    }
    
    public static void onGrabbedWeapon(SimplePlayer player, Card weapon) {

    }

    public static void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {

    }

    public static void onReloadableWeapons(List<Card> weapons) {

    }

    public static void onTurnActions(List<String> actions) {

    }

    public static void onTurnEnd() {

    }

    public static void onMoveAction(SimplePlayer player) {

    }

    
    public static void onMoveRequest(MatrixHelper matrix, String targetPlayer) {

    }

    
    public static void onMarkAction(String player, SimplePlayer selected, int value) {

    }

    
    public static void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {

    }

    
    public static void onDiscardedPowerup(SimplePlayer player, Card powerup) {

    }

    
    public static void onTurnCreation(String currentPlayer) {

    }

    
    public static void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {

    }

    
    public static void onCanUsePowerup() {
        mess.setText("Vuoi usare un potenziamento?");
        yes.setDisable(false);
        no.setDisable(false);
    }

    
    public static void onCanStopRoutine() {
        mess.setText("Vuoi fermarti qui?");
        yes.setDisable(false);
        no.setDisable(false);
    }

    
    public static void onUsableWeapons(List<Card> usableWeapons) {


    }

    
    public static void onAvailableEffects(List<String> effects) {

    }

    
    public static void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {

    }

    
    public static void onUsedCard(Card card) {

    }

    
    public static void onAvailablePowerups(List<Card> powerups) {

    }

    
    public static void onRunCompleted(SimplePlayer player, int[] newPosition) {

    }

    
    public static void onRunRoutine(MatrixHelper matrix) {

    }
}


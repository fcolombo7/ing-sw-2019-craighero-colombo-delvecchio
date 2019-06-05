package it.polimi.ingsw.GUI;

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

import java.io.IOException;

public class ProvaWindow {

    private static Button user1;
    private static Button user2;
    private static Button user3;
    private static Button user4;
    private static Button user5;

    public static Stage zoomedImage;


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
        Image playerBoard= new Image("/gui/D-structorBoard2.png");
        ImageView plB= new ImageView(playerBoard);
        plB.setFitWidth(600);
        plB.setPreserveRatio(true);

        Image fmarkdrop=new Image("/gui/greydrop.png");
        ImageView fmarkdr= new ImageView(fmarkdrop);
        configImg(fmarkdr, 15, 325, 485);
        Label nfmarkdr=new Label("2");
        nfmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfmarkdr, 329, 488);

        Image smarkdrop=new Image("/gui/bluedrop.png");
        ImageView smarkdr= new ImageView(smarkdrop);
        configImg(smarkdr, 15, 350, 485);
        Label nsmarkdr=new Label("2");
        nsmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nsmarkdr, 354, 488);

        Image tmarkdrop=new Image("/gui/greendrop.png");
        ImageView tmarkdr= new ImageView(tmarkdrop);
        configImg(tmarkdr, 15, 375, 485);
        Label ntmarkdr=new Label("2");
        ntmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(ntmarkdr, 379, 488);

        Image fomarkdrop=new Image("/gui/purpledrop.png");
        ImageView fomarkdr= new ImageView(fomarkdrop);
        configImg(fomarkdr, 15, 400, 485);
        Label nfomarkdr=new Label("2");
        nfomarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfomarkdr, 404, 488);

        Image fimarkdrop=new Image("/gui/yellowdrop.png");
        ImageView fimarkdr= new ImageView(fimarkdrop);
        configImg(fimarkdr, 15, 425, 485);
        Label nfimarkdr=new Label("0");
        nfimarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfimarkdr, 429, 488);


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
        shoot.setPrefWidth(120);
        grab.setPrefWidth(120);
        move.setPrefWidth(120);
        loadWeapon.setPrefWidth(120);

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
        configButton(a1, 57, 84, 317, 2);
        a1.setStyle("-fx-background-color: transparent");
        a1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea1);
            }
        });
        a1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });


        Button a2=new Button();
        configButton(a2, 57, 84, 383, 2);
        a2.setStyle("-fx-background-color: transparent");
        a2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea2);
            }
        });
        a2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a3=new Button();
        configButton(a3, 57, 84, 449, 2);
        a3.setStyle("-fx-background-color: transparent");
        a3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea3);
            }
        });
        a3.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a4=new Button();
        configButton(a4, 84, 57, 2, 166);
        a4.setStyle("-fx-background-color: transparent");
        a4.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea4);
            }
        });
        a4.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a5=new Button();
        configButton(a5, 84, 57, 2, 232);
        a5.setStyle("-fx-background-color: transparent");
        a5.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea5);
            }
        });
        a5.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a6=new Button();
        configButton(a6, 84, 57, 2, 298);
        a6.setStyle("-fx-background-color: transparent");
        a6.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea6);
            }
        });
        a6.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a7=new Button();
        configButton(a7, 84, 57, 516, 257);
        a7.setStyle("-fx-background-color: transparent");
        a7.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea7);
            }
        });
        a7.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a8=new Button();
        configButton(a8, 84, 57, 516, 323);
        a8.setStyle("-fx-background-color: transparent");
        a8.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea8);
            }
        });
        a8.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Button a9=new Button();
        configButton(a9, 84, 57, 516, 389);
        a9.setStyle("-fx-background-color: transparent");
        a9.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomWeapp(wea9);
            }
        });
        a9.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
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

        Button ammsq1= new Button();
        Button ammsq2= new Button();
        Button ammsq3= new Button();
        Button ammsq4= new Button();
        Button ammsq5= new Button();
        Button ammsq6= new Button();
        Button ammsq7= new Button();


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
            Button ammsq8= new Button();
            configButton(ammsq8, 21, 21, 329, 354);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");


            gp.getChildren().addAll(map, plB, fmarkdr, nfmarkdr, smarkdr, nsmarkdr, tmarkdr, ntmarkdr, fomarkdr, nfomarkdr, fimarkdr, nfimarkdr, firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, pl1, pl2, pl3, pl4, fmarkdr1, nfmarkdr1, smarkdr1, nsmarkdr1, tmarkdr1, ntmarkdr1, fomarkdr1, nfomarkdr1, fimarkdr1, nfimarkdr1, fmarkdr2, nfmarkdr2, smarkdr2, nsmarkdr2, tmarkdr2, ntmarkdr2, fomarkdr2, nfomarkdr2, fimarkdr2, nfimarkdr2, fmarkdr3, nfmarkdr3, smarkdr3, nsmarkdr3, tmarkdr3, ntmarkdr3, fomarkdr3, nfomarkdr3, fimarkdr3, nfimarkdr3, fmarkdr4, nfmarkdr4, smarkdr4, nsmarkdr4, tmarkdr4, ntmarkdr4, fomarkdr4, nfomarkdr4, fimarkdr4, nfimarkdr4, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, o3sdr, o4sdr, ss2dr, s3sdr, s4sdr, ts2dr, t3sdr, t4sdr, fs2dr, f3sdr, f4sdr, fi2sdr, fi3sdr, fi4sdr, si2sdr, si3sdr, si4sdr, se2sdr, se3sdr, se4sdr, e2sdr, e3sdr, e4sdr, n2sdr, n3sdr, n4sdr, te2sdr, te3sdr, te4sdr, el2sdr, el3sdr, el4sdr, tw2sdr, tw3sdr, tw4sdr, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8, user1, user2, user3, user4, user5);
        }

        if(mapp4){
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
            Button ammsq8= new Button();
            configButton(ammsq8, 21, 21, 313, 354);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

            gp.getChildren().addAll(map, plB, fmarkdr, nfmarkdr, smarkdr, nsmarkdr, tmarkdr, ntmarkdr, fomarkdr, nfomarkdr, fimarkdr, nfimarkdr, firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, pl1, pl2, pl3, pl4, fmarkdr1, nfmarkdr1, smarkdr1, nsmarkdr1, tmarkdr1, ntmarkdr1, fomarkdr1, nfomarkdr1, fimarkdr1, nfimarkdr1, fmarkdr2, nfmarkdr2, smarkdr2, nsmarkdr2, tmarkdr2, ntmarkdr2, fomarkdr2, nfomarkdr2, fimarkdr2, nfimarkdr2, fmarkdr3, nfmarkdr3, smarkdr3, nsmarkdr3, tmarkdr3, ntmarkdr3, fomarkdr3, nfomarkdr3, fimarkdr3, nfimarkdr3, fmarkdr4, nfmarkdr4, smarkdr4, nsmarkdr4, tmarkdr4, ntmarkdr4, fomarkdr4, nfomarkdr4, fimarkdr4, nfimarkdr4, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, o3sdr, o4sdr, ss2dr, s3sdr, s4sdr, ts2dr, t3sdr, t4sdr, fs2dr, f3sdr, f4sdr, fi2sdr, fi3sdr, fi4sdr, si2sdr, si3sdr, si4sdr, se2sdr, se3sdr, se4sdr, e2sdr, e3sdr, e4sdr, n2sdr, n3sdr, n4sdr, te2sdr, te3sdr, te4sdr, el2sdr, el3sdr, el4sdr, tw2sdr, tw3sdr, tw4sdr, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8);
        }

        if(mapp2) {
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

            gp.getChildren().addAll(map, plB, fmarkdr, nfmarkdr, smarkdr, nsmarkdr, tmarkdr, ntmarkdr, fomarkdr, nfomarkdr, fimarkdr, nfimarkdr, firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, pl1, pl2, pl3, pl4, fmarkdr1, nfmarkdr1, smarkdr1, nsmarkdr1, tmarkdr1, ntmarkdr1, fomarkdr1, nfomarkdr1, fimarkdr1, nfimarkdr1, fmarkdr2, nfmarkdr2, smarkdr2, nsmarkdr2, tmarkdr2, ntmarkdr2, fomarkdr2, nfomarkdr2, fimarkdr2, nfimarkdr2, fmarkdr3, nfmarkdr3, smarkdr3, nsmarkdr3, tmarkdr3, ntmarkdr3, fomarkdr3, nfomarkdr3, fimarkdr3, nfimarkdr3, fmarkdr4, nfmarkdr4, smarkdr4, nsmarkdr4, tmarkdr4, ntmarkdr4, fomarkdr4, nfomarkdr4, fimarkdr4, nfimarkdr4, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, o3sdr, o4sdr, ss2dr, s3sdr, s4sdr, ts2dr, t3sdr, t4sdr, fs2dr, f3sdr, f4sdr, fi2sdr, fi3sdr, fi4sdr, si2sdr, si3sdr, si4sdr, se2sdr, se3sdr, se4sdr, e2sdr, e3sdr, e4sdr, n2sdr, n3sdr, n4sdr, te2sdr, te3sdr, te4sdr, el2sdr, el3sdr, el4sdr, tw2sdr, tw3sdr, tw4sdr, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7);
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
            Button ammsq8= new Button();
            configButton(ammsq8, 21, 21, 209, 354);
            ammsq8.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");
            Button ammsq9= new Button();
            configButton(ammsq9, 21, 21, 329, 354);
            ammsq9.setStyle("-fx-background-image: url('/gui/ammsq1.png') ");

            gp.getChildren().addAll(map, plB, fmarkdr, nfmarkdr, smarkdr, nsmarkdr, tmarkdr, ntmarkdr, fomarkdr, nfomarkdr, fimarkdr, nfimarkdr, firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, pl1, pl2, pl3, pl4, fmarkdr1, nfmarkdr1, smarkdr1, nsmarkdr1, tmarkdr1, ntmarkdr1, fomarkdr1, nfomarkdr1, fimarkdr1, nfimarkdr1, fmarkdr2, nfmarkdr2, smarkdr2, nsmarkdr2, tmarkdr2, ntmarkdr2, fomarkdr2, nfomarkdr2, fimarkdr2, nfimarkdr2, fmarkdr3, nfmarkdr3, smarkdr3, nsmarkdr3, tmarkdr3, ntmarkdr3, fomarkdr3, nfomarkdr3, fimarkdr3, nfimarkdr3, fmarkdr4, nfmarkdr4, smarkdr4, nsmarkdr4, tmarkdr4, ntmarkdr4, fomarkdr4, nfomarkdr4, fimarkdr4, nfimarkdr4, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, o3sdr, o4sdr, ss2dr, s3sdr, s4sdr, ts2dr, t3sdr, t4sdr, fs2dr, f3sdr, f4sdr, fi2sdr, fi3sdr, fi4sdr, si2sdr, si3sdr, si4sdr, se2sdr, se3sdr, se4sdr, e2sdr, e3sdr, e4sdr, n2sdr, n3sdr, n4sdr, te2sdr, te3sdr, te4sdr, el2sdr, el3sdr, el4sdr, tw2sdr, tw3sdr, tw4sdr, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, wp1, wp2, wp3, myw1, myw2, myw3, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, deck, inf1, inf2, inf3, inf4, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8, ammsq9);
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
        setPosButton(shoot, 5, 640);
        setPosButton(grab, 155, 640);
        setPosButton(move, 305, 640);
        setPosButton(loadWeapon, 460, 640);
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
            setPosButton(b1, 111, 105);
            setPosButton(b2, 201, 106);
            setPosButton(b3, 303, 108);
            setPosButton(b4, 419, 125);
            setPosButton(b5, 113, 201);
            setPosButton(b6, 202, 211);
            setPosButton(b7, 322, 201);
            setPosButton(b8, 408, 204);
            setPosButton(b9, 197, 314);
            setPosButton(b10, 314, 311);
            setPosButton(b11, 405, 301);
        }
        if(mapp2) {
            setPosButton(b1, 110, 106);
            setPosButton(b2, 202, 106);
            setPosButton(b3, 304, 106);
            setPosButton(b4, 110, 202);
            setPosButton(b5, 203, 206);
            setPosButton(b6, 300, 205);
            setPosButton(b7, 420, 205);
            setPosButton(b8, 198, 314);
            setPosButton(b9, 304, 313);
            setPosButton(b10, 418, 299);
        }
        if(mapp3) {
            setPosButton(b1, 110, 105);
            setPosButton(b2, 202, 106);
            setPosButton(b3, 304, 109);
            setPosButton(b4, 419, 108);
            setPosButton(b5, 113, 202);
            setPosButton(b6, 204, 205);
            setPosButton(b7, 315, 205);
            setPosButton(b8, 409, 205);
            setPosButton(b9, 112, 313);
            setPosButton(b10, 201, 312);
            setPosButton(b11, 313, 308);
        }
        if(mapp4) {
            setPosButton(b1, 111, 104);
            setPosButton(b2, 202, 106);
            setPosButton(b3, 304, 108);
            setPosButton(b4, 112, 203);
            setPosButton(b5, 204, 205);
            setPosButton(b6, 301, 204);
            setPosButton(b7, 421, 205);
            setPosButton(b8, 114, 314);
            setPosButton(b9, 202, 313);
            setPosButton(b10, 305, 312);
            setPosButton(b11, 419, 301);
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



        deck.setLayoutX(522);
        deck.setLayoutY(121);

        dw.setLayoutX(522);
        dw.setLayoutY(121);
        dp.setLayoutX(537);
        dp.setLayoutY(24);

        Image dstructor=new Image("/gui/dstructor.png");
        user1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(dstructor);
            }
        });
        user1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Image dozer= new Image("/gui/dozer.png");
        user2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(dozer);
            }
        });
        user2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Image banshee= new Image("/gui/banshee.png");
        user3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(banshee);
            }
        });
        user3.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Image sprog= new Image("/gui/sprog.png");
        user4.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(sprog);
            }
        });
        user4.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });

        Image violet= new Image("/gui/violet.png");
        user5.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage=zoomUser(violet);
            }
        });
        user5.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedImage.close();
            }
        });



        Scene scene=new Scene(gp, 1200, 650);
        scene.getStylesheets().addAll(ProvaWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.setMaximized(true);
        s.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Bye.byebye();
                s.close();
            }
        });
        s.show();


    }

    private static void dimButton(Button button, int prefWidth, int prefHeight){
        button.setPrefWidth(prefWidth);
        button.setPrefHeight(prefHeight);
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



}

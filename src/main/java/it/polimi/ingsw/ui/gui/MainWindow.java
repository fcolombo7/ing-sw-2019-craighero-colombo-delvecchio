package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIServerConnection;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.ui.GUIHandler;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents the main window to play
 */
public class MainWindow extends Application {

    /* NETWORKING ATTRIBUTES */
    private static String hostname;
    private static GUIHandler networkHandler;
    private static ServerConnection connection;
    private static String logNickname;
    private static List<String> playerInRoom;


    /**
     * This attribute is used in the selectable player routine
     */
    private static int countTarget;
    private static int countPlayerList1;
    private static int countPlayerList2;
    private static int countPlayerList3;
    private static int countPlayerList4;
    private static List<List<String>> tempPlSelected=new ArrayList<>();
    private static List<List<String>> plSelected=new ArrayList<>();
    private static List<String> list0=new ArrayList<>();
    private static List<String> list1=new ArrayList<>();
    private static List<String> list2=new ArrayList<>();
    private static List<String> list3=new ArrayList<>();
    private static boolean minNumberList1=false;
    private static boolean minNumberList2=false;
    private static boolean minNumberList3=false;
    private static boolean minNumberList4=false;

    private static boolean winner=false;


    private static ImageView map;

    private static boolean init = false;

    /**
     * This attribute represents the button of the first user of the match
     */
    private static Button user1;
    /**
     * This attribute represents the button of the second user of the match
     */
    private static Button user2;
    /**
     * This attribute represents the button of the third user of the match
     */
    private static Button user3;
    /**
     * This attribute represents the button of the fourth user of the match
     */
    private static Button user4;
    /**
     * This attribute represents the button of the fifth user of the match
     */
    private static Button user5;

    /**
     * This attribute represents the button to shoot
     */
    private static Button shoot;
    /**
     * This attribute represents the button to grab
     */
    private static Button grab;
    /**
     * This attribute represents the button to move
     */
    private static Button move;
    /**
     * This attribute represents the button to load a weapon
     */
    private static Button loadWeapon;
    /**
     * This attribute represents the button to use a powerup
     */
    private static Button usePowerup;
    /**
     * This attribute represents the button to end the turn
     */
    private static Button end;

    /**
     * This attribute represents the playerboard image
     */
    private static Image playerBoard;
    private static Image pla1;
    private static Image pla2;
    private static Image pla3;
    private static Image pla4;

    private static Button inf1;
    private static Button inf2;
    private static Button inf3;
    private static Button inf4;

    /**
     * This attribute represents the label for the yellow ammos of the player
     */
    private static Label ny;

    /**
     * This attribute represents the label for the blue ammos of the player
     */
    private static Label nb;
    /**
     * This attribute represents the label for the red ammos of the player
     */
    private static Label nr;


    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a1;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a2;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a3;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a4;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a5;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a6;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a7;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a8;
    /**
     * This attribute represents the button for one of the weapons on the board
     */
    private static Button a9;




    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa1;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa2;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa3;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa4;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa5;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa6;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa7;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa8;
    /**
     * This attribute represents the ImageView for one of the weapons on the board
     */
    private static ImageView wa9;

    //labels for the marks on the playerboards

    /**
     * This attribute represents the label for the mark of the yellow player on the player's playerboard
     */
    private static Label nfmarkdr;
    private static Label nsmarkdr;
    private static Label ntmarkdr;
    private static Label nfomarkdr;
    private static Label nfimarkdr;
    private static Label nfmarkdr2;
    private static Label nsmarkdr2;
    private static Label ntmarkdr2;
    private static Label nfomarkdr2;
    private static Label nfimarkdr2;
    private static Label nfmarkdr3;
    private static Label nsmarkdr3;
    private static Label ntmarkdr3;
    private static Label nfomarkdr3;
    private static Label nfimarkdr3;
    private static Label nfmarkdr4;
    private static Label nsmarkdr4;
    private static Label ntmarkdr4;
    private static Label nfomarkdr4;
    private static Label nfimarkdr4;
    private static Label nfmarkdr5;
    private static Label nsmarkdr5;
    private static Label ntmarkdr5;
    private static Label nfomarkdr5;
    private static Label nfimarkdr5;


    //ImageViews for the damages on the player's playerboard

    /**
     * This attribute represents the ImageView for the first damage on the player's playerboard
     */
    private static ImageView firstdr;
    /**
     * This attribute represents the ImageView for the second damage on the player's playerboard
     */
    private static ImageView secdr;
    /**
     * This attribute represents the ImageView for the third damage on the player's playerboard
     */
    private static ImageView thirddr;
    /**
     * This attribute represents the ImageView for the fourth damage on the player's playerboard
     */
    private static ImageView fdr;
    /**
     * This attribute represents the ImageView for the fifth damage on the player's playerboard
     */
    private static ImageView fidr;
    /**
     * This attribute represents the ImageView for the damage 6 on the player's playerboard
     */
    private static ImageView sdr;
    /**
     * This attribute represents the ImageView for the damage 7 on the player's playerboard
     */
    private static ImageView sedr;
    /**
     * This attribute represents the ImageView for the damage 8 on the player's playerboard
     */
    private static ImageView edr;
    /**
     * This attribute represents the ImageView for the damage 9 on the player's playerboard
     */
    private static ImageView ndr;
    /**
     * This attribute represents the ImageView for the damage 10 on the player's playerboard
     */
    private static ImageView tdr;
    /**
     * This attribute represents the ImageView for the damage 11 on the player's playerboard
     */
    private static ImageView eldr;
    /**
     * This attribute represents the ImageView for the damage 12 on the player's playerboard
     */
    private static ImageView twdr;

    //ImageViews for the damages of the first enemy of the player
    private static ImageView osdr;
    private static ImageView ssdr;
    private static ImageView tsdr;
    private static ImageView fsdr;
    private static ImageView fisdr;
    private static ImageView sisdr;
    private static ImageView sesdr;
    private static ImageView esdr;
    private static ImageView nsdr;
    private static ImageView tesdr;
    private static ImageView elsdr;
    private static ImageView twsdr;

    //ImageViews for the damages of the second enemy of the player
    private static ImageView os2dr;
    private static ImageView ss2dr;
    private static ImageView ts2dr;
    private static ImageView fs2dr;
    private static ImageView fis2dr;
    private static ImageView sis2dr;
    private static ImageView ses2dr;
    private static ImageView es2dr;
    private static ImageView ns2dr;
    private static ImageView tes2dr;
    private static ImageView els2dr;
    private static ImageView tws2dr;

    //ImageViews for the damages of the third enemy of the player
    private static ImageView o3sdr;
    private static ImageView s3sdr;
    private static ImageView t3sdr;
    private static ImageView f3sdr;
    private static ImageView fi3sdr;
    private static ImageView si3sdr;
    private static ImageView se3sdr;
    private static ImageView e3sdr;
    private static ImageView n3sdr;
    private static ImageView te3sdr;
    private static ImageView el3sdr;
    private static ImageView tw3sdr;

    //ImageViews for the damages of the fifth enemy of the player
    private static ImageView o4sdr;
    private static ImageView s4sdr;
    private static ImageView t4sdr;
    private static ImageView f4sdr;
    private static ImageView fi4sdr;
    private static ImageView si4sdr;
    private static ImageView se4sdr;
    private static ImageView e4sdr;
    private static ImageView n4sdr;
    private static ImageView te4sdr;
    private static ImageView el4sdr;
    private static ImageView tw4sdr;

    //ImageViews for the marks of the player
    private static ImageView fmarkdr;
    private static ImageView smarkdr;
    private static ImageView tmarkdr;
    private static ImageView fomarkdr;
    private static ImageView fimarkdr;

    //ImageViews for the marks of the first enemy of the player
    private static ImageView fmarkdr1;
    private static ImageView smarkdr1;
    private static ImageView tmarkdr1;
    private static ImageView fomarkdr1;
    private static ImageView fimarkdr1;

    //ImageViews for the marks of the second enemy of the player
    private static ImageView fmarkdr2;
    private static ImageView smarkdr2;
    private static ImageView tmarkdr2;
    private static ImageView fomarkdr2;
    private static ImageView fimarkdr2;

    //ImageViews for the marks of the third enemy of the player
    private static ImageView fmarkdr3;
    private static ImageView smarkdr3;
    private static ImageView tmarkdr3;
    private static ImageView fomarkdr3;
    private static ImageView fimarkdr3;

    //ImageViews for the marks of the fifth enemy of the player
    private static ImageView fmarkdr4;
    private static ImageView smarkdr4;
    private static ImageView tmarkdr4;
    private static ImageView fomarkdr4;
    private static ImageView fimarkdr4;


    /**
     * This attribute represents the ImageView of the weapon 1 of the player
     */
    private static ImageView wp1;
    /**
     * This attribute represents the button of the weapon 1 of the player
     */
    private static Button myw1;
    /**
     * This attribute represents the ImageView of the weapon 2 of the player
     */
    private static ImageView wp2;
    /**
     * This attribute represents the button of the weapon 1 of the player
     */
    private static Button myw2;
    /**
     * This attribute represents the ImageView of the weapon 3 of the player
     */
    private static ImageView wp3;
    /**
     * This attribute represents the button of the weapon 1 of the player
     */
    private static Button myw3;
    /**
     * This attribute represents the ImageView of the powerup 1 of the player
     */
    private static ImageView pu1;
    /**
     * This attribute represents the button of the powerup 1 of the player
     */
    private static Button myp1;
    /**
     * This attribute represents the ImageView of the powerup 2 of the player
     */
    private static ImageView pu2;
    /**
     * This attribute represents the button of the powerup 2 of the player
     */
    private static Button myp2;
    /**
     * This attribute represents the ImageView of the powerup 3 of the player
     */
    private static ImageView pu3;
    /**
     * This attribute represents the button of the powerup 3 of the player
     */
    private static Button myp3;
    /**
     * This attribute represents the ImageView of the powerup 4 of the player
     */
    private static ImageView pu4;
    /**
     * This attribute represents the button of the powerup 4 of the player
     */
    private static Button myp4;


    //images of all the possible playerboards
    private static Image dstructorPlBImage;
    private static Image sprogPlBImage;
    private static Image dozerPlBImage;
    private static Image bansheePlBImage;
    private static Image violetPlBImage;
    private static Image dstructorFrenzyPlBImage;
    private static Image sprogFrenzyPlBImage;
    private static Image dozerFrenzyPlBImage;
    private static Image bansheeFrenzyPlBImage;
    private static Image violetFrenzyPlBImage;
    private static Image dstructorSemiFrPlBImage;
    private static Image sprogSemiFrPlBImage;
    private static Image dozerSemiFrPlBImage;
    private static Image bansheeSemiFrPlBImage;
    private static Image violetSemiFrPlBImage;


    //images and imageviews of the ammos of the player
    private static Image yammo;
    private static ImageView yam;
    private static Image bammo;
    private static ImageView bam;
    private static Image rammo;
    private static ImageView ram;


    /**
     * This attribute represents the ImageView of the playerboard of the player
     */
    private static ImageView plB;
    private static ImageView pl1;
    private static ImageView pl2;
    private static ImageView pl3;
    private static ImageView pl4;
    private static Label labPlayer1;
    private static Label labPlayer2;
    private static Label labPlayer3;
    private static Label labPlayer4;



    private static Image dstructor;
    private static Image dozer;
    private static Image banshee;
    private static Image sprog;
    private static Image violet;

    /**
     * This attribute represents the label in which are written the messages during the match
     */
    private static Label mess;
    private static Button yes;
    private static Button no;
    private static Button ok;
    private static Button okay;

    /**
     * This attribute represents the label of the first weapon of the player that says if the weapon is loaded/unloaded, usable,..
     */
    private static Label myWeap1Label;
    private static Label myWeap2Label;
    private static Label myWeap3Label;
    private static Label myPowerup1Label;
    private static Label myPowerup2Label;
    private static Label myPowerup3Label;
    private static Label myPowerup4Label;

    private static List<ImageView> killshotTrack=new ArrayList<>();

    private static ImageView skull1;
    private static Image skull1img;
    private static ImageView skull2;
    private static ImageView skull3;
    private static ImageView skull4;
    private static ImageView skull5;
    private static ImageView skull6;
    private static ImageView skull7;
    private static ImageView skull8;
    private static ImageView skull9;

    private static ImageView zoomedimg;
    private static ImageView zoomedUser;

    private static Label numbKill1;
    private static Label numbKill2;
    private static Label numbKill3;
    private static Label numbKill4;
    private static Label numbKill5;
    private static Label numbKill6;
    private static Label numbKill7;
    private static Label numbKill8;
    private static Label numbKill9;

    private static List<Label> overkillTrack=new ArrayList<>();

    private static int myTurn;


    //images of the weapon cards

    /**
     * This attribute represents the image of a weapon of the game
     */
    private static Image im1;
    private static Image im2;
    private static Image im3;
    private static Image im4;
    private static Image im5;
    private static Image im6;
    private static Image im7;
    private static Image im8;
    private static Image im9;
    private static Image im10;
    private static Image im11;
    private static Image im12;
    private static Image im13;
    private static Image im14;
    private static Image im15;
    private static Image im16;
    private static Image im17;
    private static Image im18;
    private static Image im19;
    private static Image im20;
    private static Image im21;


    //images of the powerup cards
    private static Image impow1;
    private static Image impow2;
    private static Image impow3;
    private static Image impow4;
    private static Image impow5;
    private static Image impow6;
    private static Image impow7;
    private static Image impow8;
    private static Image impow9;
    private static Image impow10;
    private static Image impow11;
    private static Image impow12;



    //images of the drops corresponding to the players
    private static Image imDropBlue;
    private static Image imDropGrey;
    private static Image imDropGreen;
    private static Image imDropYellow;
    private static Image imDropPurple;

    private static Button eff1;
    private static Button eff2;
    private static Button eff3;
    private static Button eff4;



    private static String respawnPos;

    private static boolean reconnection=false;





    private static SquareWindow[][] squareMatrix= new SquareWindow[3][4];

    public static Button[][] squareButtonMatrix=new Button[3][4];



    public static double widthScaleFactor;
    public static double heightScaleFactor;

    /**
     * This attribute represents the hashmap that associates a number to a string that represents the relative path of a platerboard image
     */
    private static HashMap<Integer, String> plBhashmap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the nickname of a player to the image of the corresponding drop
     */
    private static HashMap<String, Image> damagesHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the imageview of the corresponding damage on the playerboards of the enemies
     */
    private static HashMap<Integer, ImageView> damageImageViewHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the imageview of the corresponding damage on the player playerboard
     */
    private static HashMap<Integer, ImageView> myDamagesImageViewHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the label of the corresponding marks on the player playerboard
     */
    private static HashMap<Integer, Label> myMarksLabelHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the label of the corresponding mark on the playerboards of the enemies
     */
    private static HashMap<Integer, Label> marksLabelHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the turn number to the imageview of the corresponding playerboard
     */
    private static HashMap<Integer, ImageView> plBHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to a playerboard image
     */
    private static HashMap<Integer, Image> plBImageHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the corresponding imageview of the powerup of the player
     */
    private static HashMap<Integer, ImageView> myPowerupsHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the corresponding imageview of the weapon of the player
     */
    private static HashMap<Integer, ImageView> myWeaponHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the corresponding imageview of the weapon on the board
     */
    private static HashMap<Integer, ImageView> boardWeapImVHashMap= new HashMap<>();

    private static HashMap<String, Button> boardTileButtonHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of an ammo tile to the imageview on the board in which is the tile
     */
    private static HashMap<String, ImageView> boardTileImgVHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the turn number to the user button
     */
    private static HashMap<Integer, Button> userButtonHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a weapon to a position number on the board in which the weapon is
     */
    private static HashMap<String, Integer> idWeaponPosition=new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a number to the label of the corresponding weapon of the player
     */
    private static HashMap<Integer, Label> myWeaponsLabel= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a weapon to the corresponding position number between the player's weapons
     */
    private static HashMap<String, Integer> myWeaponsPosition= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a position number to the corresponding button on the board
     */
    private static HashMap<Integer, Button> boardWeaponsButton= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a position number to the corresponding button between the player's weapons
     */
    private static HashMap<Integer, Button> myWeaponsButton= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a weapon to the corresponding image
     */
    private static HashMap<String, Image> weaponsHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a powerup to the corresponding image
     */
    private static HashMap<String, Image> powerupsHashMap=new HashMap<>();

    /**
     * This attribyte represents the hashmap that associates the turn number of a player to his nickname
     */
    private static HashMap<Integer, String> turnNicknameHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the nickname of a player to his turn number
     */
    private static HashMap<String, Integer> nicknameTurnHashMap= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a powerup to the corresponding label on the player's powerup
     */
    private static HashMap<String, Label> powerupAvailableLabel= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the id of a powerup to the position number between the player's powerup
     */
    private static HashMap<String, Integer> myPowerupsPosition= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a position number to the corresponding button of the player's powerups
     */
    private static HashMap<Integer, Button> myPowerupsButton= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the nickname of a player yo the corresponding anchorpane
     */
    private static HashMap<String, AnchorPane> infoWindowPlayer= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates the number of the player's enemy to the nickname of the player's enemy
     */
    private static HashMap<Integer, String> numbEnemyNickname= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a button to the id of the weapon under that button
     */
    private static HashMap<Button, String> weapButtonId= new HashMap<>();

    /**
     * This attribute represents the hashmap that associates a button to the id of the powerup under that button
     */
    private static HashMap<Button, String> powButtonId= new HashMap<>();

    private static String myNickname;

    private static int numPlayers;


    private static Image image;
    private static Stage stage;

    private static Stage infoStage;

    private static AnchorPane infostage1;
    private static AnchorPane infostage2;
    private static AnchorPane infostage3;
    private static AnchorPane infostage4;

    private static Scene infoscene1;
    private static Scene infoscene2;
    private static Scene infoscene3;
    private static Scene infoscene4;


    //ImageViews for the death counter of the player
    private static ImageView mydeath1;
    private static ImageView mydeath2;
    private static ImageView mydeath3;
    private static ImageView mydeath4;
    private static ImageView mydeath5;
    private static ImageView mydeath6;
    private static ImageView mydeath1fr;
    private static ImageView mydeath2fr;
    private static ImageView mydeath3fr;
    private static ImageView mydeath4fr;

    //ImageViews for the death counter of the first enemy of the player
    private static ImageView en1death1;
    private static ImageView en1death2;
    private static ImageView en1death3;
    private static ImageView en1death4;
    private static ImageView en1death5;
    private static ImageView en1death6;
    private static ImageView en1death1fr;
    private static ImageView en1death2fr;
    private static ImageView en1death3fr;
    private static ImageView en1death4fr;

    //ImageViews for the death counter of the second enemy of the player
    private static ImageView en2death1;
    private static ImageView en2death2;
    private static ImageView en2death3;
    private static ImageView en2death4;
    private static ImageView en2death5;
    private static ImageView en2death6;
    private static ImageView en2death1fr;
    private static ImageView en2death2fr;
    private static ImageView en2death3fr;
    private static ImageView en2death4fr;

    //ImageViews for the death counter of the third enemy of the player
    private static ImageView en3death1;
    private static ImageView en3death2;
    private static ImageView en3death3;
    private static ImageView en3death4;
    private static ImageView en3death5;
    private static ImageView en3death6;
    private static ImageView en3death1fr;
    private static ImageView en3death2fr;
    private static ImageView en3death3fr;
    private static ImageView en3death4fr;

    //ImageViews for the death counter of the fourth enemy of the player
    private static ImageView en4death1;
    private static ImageView en4death2;
    private static ImageView en4death3;
    private static ImageView en4death4;
    private static ImageView en4death5;
    private static ImageView en4death6;
    private static ImageView en4death1fr;
    private static ImageView en4death2fr;
    private static ImageView en4death3fr;
    private static ImageView en4death4fr;


    private static Stage finalstage;
    private static AnchorPane finalPane;
    private static Scene finalScene;


    private static int turnReconnection;




    @Override
    public void start(Stage stage) throws  Exception {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Logger.logAndPrint("SCREEN DIMENSION" +primaryScreenBounds.getWidth() + " - " +primaryScreenBounds.getHeight());
        double widthScaleFact=primaryScreenBounds.getWidth()/1200;
        double heightScaleFact=primaryScreenBounds.getHeight()/675;//PER MANTENERE LE PROPORZIONI DEL FULL HD NO 650 ma 675
        Logger.logAndPrint("CONVERSION" +primaryScreenBounds.getWidth()/primaryScreenBounds.getHeight() + " - " +1200.0/675.0);
        Logger.logAndPrint("SCALE FACTOR" +widthScaleFact + " - " +heightScaleFact);
        widthScaleFactor=widthScaleFact;
        heightScaleFactor=heightScaleFact;



        MainWindow.stage=stage;

        /*INITIALIZE CONNECTION HANDLER*/
        networkHandler=new GUIHandler();
        playerInRoom=new ArrayList<>(4);

        LoginWindow.log(stage);




    }

    /**
     * This method is called when the player successfully logged in.
     * This method open the WaitingRoomWindow and close the LoginWindow
     */
    public static void onLoginCompleted() {
        stage.close();
        WaitingRoomWindow.create(stage);
        try {
            RMIServerConnection r=(RMIServerConnection) connection;
            if (playerInRoom.isEmpty())
                WaitingRoomWindow.getArea().appendText("You are the first in the room! \n");
            for (String s : playerInRoom) {
                WaitingRoomWindow.getArea().appendText(s + " is in the room...\n");
            }
        }catch (Exception e){
            Logger.logAndPrint("OK first player");
        }
    }

    /**
     * This method is called to initialize the game window
     * @param numMap represents the chosen map
     * @param skullNumber represents the number of the skull of the current game
     */

    public static void initGameWindow(int numMap, int skullNumber) {


        stage.setTitle("ADRENALINA");


        mydeath1=new ImageView();
        mydeath2=new ImageView();
        mydeath3=new ImageView();
        mydeath4=new ImageView();
        mydeath5=new ImageView();
        mydeath6=new ImageView();
        mydeath1fr=new ImageView();
        mydeath2fr=new ImageView();
        mydeath3fr=new ImageView();
        mydeath4fr=new ImageView();
        configImageView(mydeath1, 17, 17, 130, 596);
        configImageView(mydeath2, 17, 17, 162, 596);
        configImageView(mydeath3, 17, 17, 194, 596);
        configImageView(mydeath4, 17, 17, 226, 596);
        configImageView(mydeath5, 17, 17, 258, 596);
        configImageView(mydeath6, 17, 17, 290, 596);
        configImageView(mydeath1fr, 17, 17, 171, 596);
        configImageView(mydeath2fr, 17, 17, 203, 596);
        configImageView(mydeath3fr, 17, 17, 235, 596);
        configImageView(mydeath4fr, 17, 17, 267, 596);

        en1death1=new ImageView();
        en1death2=new ImageView();
        en1death3=new ImageView();
        en1death4=new ImageView();
        en1death5=new ImageView();
        en1death6=new ImageView();
        en1death1fr=new ImageView();
        en1death2fr=new ImageView();
        en1death3fr=new ImageView();
        en1death4fr=new ImageView();
        configImageView(en1death1, 8, 8, 696, 79);
        configImageView(en1death2, 8, 8, 712, 79);
        configImageView(en1death3, 8, 8, 728, 79);
        configImageView(en1death4, 8, 8, 744, 79);
        configImageView(en1death5, 8, 8, 760, 79);
        configImageView(en1death6, 8, 8, 776, 79);
        configImageView(en1death1fr, 8, 8, 718, 79);
        configImageView(en1death2fr, 8, 8, 734, 79);
        configImageView(en1death3fr, 8, 8, 750, 79);
        configImageView(en1death4fr, 8, 8, 766, 79);

        en2death1=new ImageView();
        en2death2=new ImageView();
        en2death3=new ImageView();
        en2death4=new ImageView();
        en2death5=new ImageView();
        en2death6=new ImageView();
        en2death1fr=new ImageView();
        en2death2fr=new ImageView();
        en2death3fr=new ImageView();
        en2death4fr=new ImageView();
        configImageView(en2death1, 8, 8, 696, 159);
        configImageView(en2death2, 8, 8, 712, 159);
        configImageView(en2death3, 8, 8, 728, 159);
        configImageView(en2death4, 8, 8, 744, 159);
        configImageView(en2death5, 8, 8, 760, 159);
        configImageView(en2death6, 8, 8, 776, 159);
        configImageView(en2death1fr, 8, 8, 718, 159);
        configImageView(en2death2fr, 8, 8, 734, 159);
        configImageView(en2death3fr, 8, 8, 750, 159);
        configImageView(en2death4fr, 8, 8, 766, 159);

        en3death1=new ImageView();
        en3death2=new ImageView();
        en3death3=new ImageView();
        en3death4=new ImageView();
        en3death5=new ImageView();
        en3death6=new ImageView();
        en3death1fr=new ImageView();
        en3death2fr=new ImageView();
        en3death3fr=new ImageView();
        en3death4fr=new ImageView();
        configImageView(en3death1, 8, 8, 696, 239);
        configImageView(en3death2, 8, 8, 712, 239);
        configImageView(en3death3, 8, 8, 728, 239);
        configImageView(en3death4, 8, 8, 744, 239);
        configImageView(en3death5, 8, 8, 760, 239);
        configImageView(en3death6, 8, 8, 776, 239);
        configImageView(en3death1fr, 8, 8, 718, 239);
        configImageView(en3death2fr, 8, 8, 734, 239);
        configImageView(en3death3fr, 8, 8, 750, 239);
        configImageView(en3death4fr, 8, 8, 766, 239);

        en4death1=new ImageView();
        en4death2=new ImageView();
        en4death3=new ImageView();
        en4death4=new ImageView();
        en4death5=new ImageView();
        en4death6=new ImageView();
        en4death1fr=new ImageView();
        en4death2fr=new ImageView();
        en4death3fr=new ImageView();
        en4death4fr=new ImageView();
        configImageView(en4death1, 8, 8, 696, 319);
        configImageView(en4death2, 8, 8, 712, 319);
        configImageView(en4death3, 8, 8, 728, 319);
        configImageView(en4death4, 8, 8, 744, 319);
        configImageView(en4death5, 8, 8, 760, 319);
        configImageView(en4death6, 8, 8, 776, 319);
        configImageView(en4death1fr, 8, 8, 718, 319);
        configImageView(en4death2fr, 8, 8, 734, 319);
        configImageView(en4death3fr, 8, 8, 750, 319);
        configImageView(en4death4fr, 8, 8, 766, 319);




        AnchorPane gp = new AnchorPane();

        if (numMap == 1) {
            image = new Image("/gui/map1.png");
            map=new ImageView(image);
            configImageView(map, 600, 454, 0, 0);
            gp.getChildren().add(map) ;
            initMap1(gp);
        }
        if (numMap == 2) {
            image = new Image("/gui/map2.png");
            map=new ImageView(image);
            configImageView(map, 600, 454, 0, 0);
            gp.getChildren().add(map);
            initMap2(gp);
        }
        if (numMap == 3) {
            image = new Image("/gui/map3.png");
            map=new ImageView(image);
            configImageView(map, 600, 454, 0, 0);
            gp.getChildren().add(map);
            initMap3(gp);
        }
        if (numMap == 4) {
            image = new Image("/gui/map4.png");
            map=new ImageView(image);
            configImageView(map, 600, 454, 0, 0);
            gp.getChildren().add(map);
            initMap4(gp);
        }




        zoomedimg = new ImageView();
        configImageView(zoomedimg, 150, 253, 1030, 410);
        zoomedUser = new ImageView();
        configImageView(zoomedUser, 129, 137, 1030, 440);




        myWeap1Label = new Label();
        //myPowerup1Label = new Label();
        setPosLabel(myWeap1Label, 640, 520);
        //setPosLabel(myPowerup1Label, 640, 380);
        myWeap2Label = new Label();
        setPosLabel(myWeap2Label, 730, 520);
        myWeap3Label = new Label();
        setPosLabel(myWeap3Label, 820, 520);
        /*myPowerup2Label = new Label();
        setPosLabel(myPowerup2Label, 730, 380);
        myPowerup3Label = new Label();
        setPosLabel(myPowerup3Label, 820, 380);
        myPowerup4Label = new Label();
        setPosLabel(myPowerup4Label, 910, 380);*/

        initMyWeaponsLabel();



        eff1 = new Button("1");
        eff2 = new Button("2");
        eff3 = new Button("3");
        eff4 = new Button("4");
        eff1.setDisable(true);
        eff2.setDisable(true);
        eff3.setDisable(true);
        eff4.setDisable(true);


        yes = new Button("Si");
        setPosButton(yes, 1060, 270);
        yes.getStyleClass().add("info");
        yes.setStyle("-fx-background-color: green");
        //yes.setStyle("-fx-text-fill: white");
        yes.setDisable(true);


        no = new Button("No");
        setPosButton(no, 1120, 270);
        no.getStyleClass().add("info");
        no.setStyle("-fx-background-color: red");
        //no.setStyle("-fx-text-fill: white");
        no.setDisable(true);


        ok = new Button("Invia");
        setPosButton(ok, 1120, 310);
        ok.getStyleClass().add("info");
        ok.setStyle("-fx-background-color: green");
        //ok.setStyle("-fx-text-fill: white");
        ok.setDisable(true);

        okay = new Button("Ok");
        setPosButton(okay, 1060, 310);
        okay.getStyleClass().add("info");
        okay.setStyle("-fx-background-color: blue");
        //okay.setStyle("-fx-text-fill: white");
        okay.setDisable(true);

        setPosButton(eff1, 1030, 350);
        setPosButton(eff2, 1070, 350);
        setPosButton(eff3, 1110, 350);
        setPosButton(eff4, 1150, 350);


        infoStage=new Stage();


        infostage1 = new AnchorPane();
        infostage2 = new AnchorPane();
        infostage3 = new AnchorPane();
        infostage4 = new AnchorPane();

        initInfoWindowPlayerHashMap();


        inf1 = new Button("Info");
        inf2 = new Button("Info");
        inf3 = new Button("Info");
        inf4 = new Button("Info");
        inf1.getStyleClass().add("info");
        inf2.getStyleClass().add("info");
        inf3.getStyleClass().add("info");
        inf4.getStyleClass().add("info");
        infoscene1=new Scene(infostage1, 500*widthScaleFactor, 300*heightScaleFactor);
        infoscene2=new Scene(infostage2, 500*widthScaleFactor, 300*heightScaleFactor);
        infoscene3=new Scene(infostage3, 500*widthScaleFactor, 300*heightScaleFactor);
        infoscene4=new Scene(infostage4, 500*widthScaleFactor, 300*heightScaleFactor);
        infoscene1.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        infoscene2.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        infoscene3.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        infoscene4.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());


        inf1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setStageAp(1);

            }
        });
        inf2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setStageAp(2);
            }
        });
        inf3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setStageAp(3);
            }
        });
        inf4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setStageAp(4);
            }
        });

        if(numPlayers==2){
            inf2.setText(null);
            inf2.setDisable(true);
            inf2.setStyle(null);
            inf2.setMinSize(0, 0);
            inf2.setPrefSize(0, 0);
            inf3.setText(null);
            inf3.setDisable(true);
            inf3.setStyle(null);
            inf3.setMinSize(0, 0);
            inf3.setPrefSize(0, 0);
            inf4.setText(null);
            inf4.setDisable(true);
            inf4.setStyle(null);
            inf4.setMinSize(0, 0);
            inf4.setPrefSize(0, 0);
        }

        if(numPlayers==3){
            inf3.setText(null);
            inf3.setDisable(true);
            inf3.setStyle(null);
            inf3.setMinSize(0, 0);
            inf3.setPrefSize(0, 0);
            inf4.setText(null);
            inf4.setDisable(true);
            inf4.setStyle(null);
            inf4.setMinSize(0, 0);
            inf4.setPrefSize(0, 0);
        }

        if(numPlayers==4){
            inf4.setText(null);
            inf4.setDisable(true);
            inf4.setStyle(null);
            inf4.setMinSize(0, 0);
            inf4.setPrefSize(0, 0);
        }

        shoot = new Button("Spara");
        grab = new Button("Raccogli");
        move = new Button("Corri");
        loadWeapon = new Button("Ricarica");
        usePowerup = new Button("Potenziamento");
        end = new Button("Fine");
        shoot.getStyleClass().add("actionbuttons");
        grab.getStyleClass().add("actionbuttons");
        move.getStyleClass().add("actionbuttons");
        loadWeapon.getStyleClass().add("actionbuttons");
        usePowerup.getStyleClass().add("actionbuttons");
        end.getStyleClass().add("actionbuttons");
        end.setPrefWidth(70 * widthScaleFactor);
        usePowerup.setPrefWidth(110 * widthScaleFactor);
        shoot.setPrefWidth(110 * widthScaleFactor);
        grab.setPrefWidth(110 * widthScaleFactor);
        move.setPrefWidth(110 * widthScaleFactor);
        loadWeapon.setPrefWidth(110 * widthScaleFactor);
        shoot.setDisable(true);
        grab.setDisable(true);
        move.setDisable(true);
        loadWeapon.setDisable(true);
        usePowerup.setDisable(true);
        end.setDisable(true);
        grab.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("GRAB");
                grab.setDisable(true);
                shoot.setDisable(true);
                loadWeapon.setDisable(true);
                move.setDisable(true);
                usePowerup.setDisable(true);
                end.setDisable(true);
            }
        });
        shoot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("SHOOT");
                shoot.setDisable(true);
                grab.setDisable(true);
                loadWeapon.setDisable(true);
                move.setDisable(true);
                usePowerup.setDisable(true);
                end.setDisable(true);
            }
        });
        move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("RUN");
                move.setDisable(true);
                grab.setDisable(true);
                shoot.setDisable(true);
                loadWeapon.setDisable(true);
                usePowerup.setDisable(true);
                end.setDisable(true);
            }
        });
        loadWeapon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("RELOAD");
                loadWeapon.setDisable(true);
                grab.setDisable(true);
                shoot.setDisable(true);
                move.setDisable(true);
                usePowerup.setDisable(true);
                end.setDisable(true);
            }
        });
        usePowerup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("POWERUP");
                usePowerup.setDisable(true);
                grab.setDisable(true);
                shoot.setDisable(true);
                loadWeapon.setDisable(true);
                move.setDisable(true);
                end.setDisable(true);
            }
        });
        end.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connection.selectAction("END");
                end.setDisable(true);
                grab.setDisable(true);
                shoot.setDisable(true);
                loadWeapon.setDisable(true);
                move.setDisable(true);
                usePowerup.setDisable(true);
            }
        });


        Image deckweap = new Image("/gui/weaponcover.png");
        ImageView dw = new ImageView(deckweap);
        dw.setFitWidth(58 * widthScaleFactor);
        dw.setFitHeight(97 * heightScaleFactor);

        Image deckpow = new Image("/gui/powerupcover.png");
        ImageView dp = new ImageView(deckpow);
        dp.setFitWidth(44 * widthScaleFactor);
        dp.setFitHeight(63 * heightScaleFactor);

        user1 = new Button();
        user1.setMinSize(9, 10);
        configButton(user1, 9, 10, 200, 440,1);


        user2 = new Button();
        user2.setMinSize(9, 10);
        configButton(user2, 9, 10, 215, 440,2);

        user3 = new Button();
        user3.setMinSize(9, 10);
        configButton(user3, 9, 10, 230, 440,3);

        user4 = new Button();
        user4.setMinSize(9, 10);
        configButton(user4, 9, 10, 245, 440,4);

        user5 = new Button();
        user5.setMinSize(9, 10);
        configButton(user5, 9, 10, 260, 440,5);

        initUserButtonHashMap();


        skull1img = new Image("/gui/redskull.jpg");
        skull1 = new ImageView();
        skull2 = new ImageView();
        skull3 = new ImageView();
        skull4 = new ImageView();
        skull5 = new ImageView();
        skull6 = new ImageView();
        skull7 = new ImageView();
        skull8 = new ImageView();
        skull9 = new ImageView();
        configImageView(skull1, 18, 26, 47, 27);
        configImageView(skull2, 18, 26, 72, 27);
        configImageView(skull3, 18, 26, 97, 27);
        configImageView(skull4, 18, 26, 122, 27);
        configImageView(skull5, 18, 26, 147, 27);
        configImageView(skull6, 18, 26, 172, 27);
        configImageView(skull7, 18, 26, 197, 27);
        configImageView(skull8, 18, 26, 222, 27);
        configImageView(skull9, 18, 26, 247, 27);
        switch (skullNumber){
            case 1:
                skull1.setImage(skull1img);
                break;
            case 2:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                break;
            case 3:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                break;
            case 4:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                break;
            case 5:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                skull5.setImage(skull1img);
                break;
            case 6:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                skull5.setImage(skull1img);
                skull6.setImage(skull1img);
                break;
            case 7:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                skull5.setImage(skull1img);
                skull6.setImage(skull1img);
                skull7.setImage(skull1img);
                break;
            case 8:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                skull5.setImage(skull1img);
                skull6.setImage(skull1img);
                skull7.setImage(skull1img);
                skull8.setImage(skull1img);
                break;
            case 9:
                skull1.setImage(skull1img);
                skull2.setImage(skull1img);
                skull3.setImage(skull1img);
                skull4.setImage(skull1img);
                skull5.setImage(skull1img);
                skull6.setImage(skull1img);
                skull7.setImage(skull1img);
                skull8.setImage(skull1img);
                skull9.setImage(skull1img);
                break;

        }

        numbKill1 = new Label();
        numbKill2 = new Label();
        numbKill3 = new Label();
        numbKill4 = new Label();
        numbKill5 = new Label();
        numbKill6 = new Label();
        numbKill7 = new Label();
        numbKill8 = new Label();
        numbKill9 = new Label();


        killshotTrack.add(skull1);
        killshotTrack.add(skull2);
        killshotTrack.add(skull3);
        killshotTrack.add(skull4);
        killshotTrack.add(skull5);
        killshotTrack.add(skull6);
        killshotTrack.add(skull7);
        killshotTrack.add(skull8);
        killshotTrack.add(skull9);


        overkillTrack.add(numbKill1);
        overkillTrack.add(numbKill2);
        overkillTrack.add(numbKill3);
        overkillTrack.add(numbKill4);
        overkillTrack.add(numbKill5);
        overkillTrack.add(numbKill6);
        overkillTrack.add(numbKill7);
        overkillTrack.add(numbKill8);
        overkillTrack.add(numbKill9);



        setPosButton(shoot, 5, 670);
        setPosButton(grab, 125, 670);
        setPosButton(move, 245, 670);
        setPosButton(loadWeapon, 365, 670);
        setPosButton(usePowerup, 485, 670);
        setPosButton(end, 650, 670);


        ny.setLayoutX(980 * widthScaleFactor);
        ny.setLayoutY(505 * heightScaleFactor);
        nb.setLayoutX(980 * widthScaleFactor);
        nb.setLayoutY(555 * heightScaleFactor);
        nr.setLayoutX(980 * widthScaleFactor);
        nr.setLayoutY(605 * heightScaleFactor);


        setPosButton(inf1, 940, 45);
        setPosButton(inf2, 940, 125);
        setPosButton(inf3, 940, 205);
        setPosButton(inf4, 940, 285);

        /*deck.setLayoutX(522*widthScaleFactor);
        deck.setLayoutY(121*heightScaleFactor);*/

        dw.setLayoutX(522 * widthScaleFactor);
        dw.setLayoutY(121 * heightScaleFactor);
        dp.setLayoutX(537 * widthScaleFactor);
        dp.setLayoutY(24 * heightScaleFactor);

        /*b4.setDisable(false);
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                wa1.setImage(wea4);
            }
        });*/


        gp.getChildren().addAll(plB, pl1, pl2, pl3, pl4, dw, dp, yam, bam, ram, nb, ny, nr, pu1, pu2, pu3, pu4, myp1, myp2, myp3, myp4, shoot, grab, move, loadWeapon, usePowerup, end, wp1, wp2, wp3, myw1, myw2, myw3, wa1, wa2, wa3, wa4, wa5, wa6, wa7, wa8, wa9, a1, a2, a3, a4, a5, a6, a7, a8, a9, inf1, inf2, inf3, inf4, user1, user2, user3, user4, user5, myPowerup1Label, myPowerup2Label, myPowerup3Label, myPowerup4Label, myWeap1Label, myWeap2Label, myWeap3Label, mess, yes, no, ok, okay, eff1, eff2, eff3, eff4);
        gp.getChildren().addAll(skull1, skull2, skull3, skull4, skull5, skull6, skull7, skull8, skull9, numbKill1, numbKill2, numbKill3, numbKill4, numbKill5, numbKill6, numbKill7, numbKill8, numbKill9, zoomedimg, zoomedUser);
        gp.getChildren().addAll(fmarkdr, fmarkdr1, fmarkdr2, fmarkdr3, fmarkdr4, nfmarkdr, nfmarkdr2, nfmarkdr3, nfmarkdr4, nfmarkdr5, smarkdr, smarkdr1, smarkdr2, smarkdr3, smarkdr4, nsmarkdr, nsmarkdr2, nsmarkdr3, nsmarkdr4, nsmarkdr5, tmarkdr, tmarkdr1, tmarkdr2, tmarkdr3, tmarkdr4, ntmarkdr, ntmarkdr2, ntmarkdr3, ntmarkdr4, ntmarkdr5, fomarkdr, fomarkdr1, fomarkdr2, fomarkdr3, fomarkdr4, nfomarkdr, nfomarkdr2, nfomarkdr3, nfomarkdr4, nfomarkdr5, fimarkdr, fimarkdr1, fimarkdr2, fimarkdr3, fimarkdr4, nfimarkdr, nfimarkdr2, nfimarkdr3, nfimarkdr4, nfimarkdr5);
        gp.getChildren().addAll(firstdr, secdr, thirddr, fdr, fidr, sdr, sedr, edr, ndr, tdr, eldr, twdr, osdr, ssdr, tsdr, fsdr, fisdr, sisdr, sesdr, esdr, nsdr, tesdr, elsdr, twsdr, os2dr, ss2dr, ts2dr, fs2dr, fis2dr, sis2dr, ses2dr, es2dr, ns2dr, tes2dr, els2dr, tws2dr, o3sdr, s3sdr, t3sdr, f3sdr, fi3sdr, si3sdr, se3sdr, e3sdr, n3sdr, te3sdr, el3sdr, tw3sdr, o4sdr, s4sdr, t4sdr, f4sdr, fi4sdr, si4sdr, se4sdr, e4sdr, n4sdr, te4sdr, el4sdr, tw4sdr);
        gp.getChildren().addAll(labPlayer1, labPlayer2, labPlayer3, labPlayer4);

        gp.getChildren().addAll(mydeath1, mydeath2, mydeath3, mydeath4, mydeath5, mydeath6, mydeath1fr, mydeath2fr, mydeath3fr, mydeath4fr, en1death1, en1death2, en1death3, en1death4, en1death5, en1death6, en1death1fr, en1death2fr, en1death3fr, en1death4fr, en2death1, en2death2, en2death3, en2death4, en2death5, en2death6, en2death1fr, en2death2fr, en2death3fr, en2death4fr, en3death1, en3death2, en3death3, en3death4, en3death5, en3death6, en3death1fr, en3death2fr, en3death3fr, en3death4fr, en4death1, en4death2, en4death3, en4death4, en4death5, en4death6, en4death1fr, en4death2fr, en4death3fr, en4death4fr);

        //, ammsq1, ammsq2, ammsq3, ammsq4, ammsq5, ammsq6, ammsq7, ammsq8

        Scene scene = new Scene(gp, 1200 * widthScaleFactor, 675 * heightScaleFactor);
        scene.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        /*stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Bye.byebye();
                stage.close();
            }
        });*/

        stage.show();
    }


    public static void main(String args[]){
        launch(args);
    }

    /**
     * This method configs the info stages
     * @param numEnem represents which enemy is the player for me
     * @param player represents infos of that player
     */

    private static void config(int numEnem, SimplePlayer player){
        if(numEnem==1) {
            infostage1.getChildren().clear();
        }
        if(numEnem==2) {
            infostage2.getChildren().clear();
        }
        if(numEnem==3) {
            infostage3.getChildren().clear();
        }
        if(numEnem==4) {
            infostage4.getChildren().clear();
        }
        if(player.getNotLoadedIds().size()>0) {
            Image unWeap1 = weaponsHashMap.get(player.getNotLoadedIds().get(0));
            ImageView unlWeap1 = new ImageView(unWeap1);
            unlWeap1.setFitWidth(90*widthScaleFactor);
            unlWeap1.setPreserveRatio(true);
            unlWeap1.setLayoutX(10*widthScaleFactor);
            unlWeap1.setLayoutY(10*heightScaleFactor);
            if(numEnem==1) {
                infostage1.getChildren().add(unlWeap1);
            }
            if(numEnem==2) {
                infostage2.getChildren().add(unlWeap1);
            }
            if(numEnem==3) {
                infostage3.getChildren().add(unlWeap1);
            }
            if(numEnem==4) {
                infostage4.getChildren().add(unlWeap1);
            }

        }
        if(player.getNotLoadedIds().size()>1) {
            Image unWeap2 = weaponsHashMap.get(player.getNotLoadedIds().get(1));
            ImageView unlWeap2 = new ImageView(unWeap2);
            unlWeap2.setFitWidth(90*widthScaleFactor);
            unlWeap2.setPreserveRatio(true);
            unlWeap2.setLayoutX(120*widthScaleFactor);
            unlWeap2.setLayoutY(10*heightScaleFactor);
            if(numEnem==1) {
                infostage1.getChildren().add(unlWeap2);
            }
            if(numEnem==2) {
                infostage2.getChildren().add(unlWeap2);
            }
            if(numEnem==3) {
                infostage3.getChildren().add(unlWeap2);
            }
            if(numEnem==4) {
                infostage4.getChildren().add(unlWeap2);
            }
        }
        if(player.getNotLoadedIds().size()>2) {
            Image unWeap3 = weaponsHashMap.get(player.getNotLoadedIds().get(2));
            ImageView unlWeap3 = new ImageView(unWeap3);
            unlWeap3.setFitWidth(90*widthScaleFactor);
            unlWeap3.setPreserveRatio(true);
            unlWeap3.setLayoutX(230*widthScaleFactor);
            unlWeap3.setLayoutY(10*heightScaleFactor);
            if(numEnem==1) {
                infostage1.getChildren().add(unlWeap3);
            }
            if(numEnem==2) {
                infostage2.getChildren().add(unlWeap3);
            }
            if(numEnem==3) {
                infostage3.getChildren().add(unlWeap3);
            }
            if(numEnem==4) {
                infostage4.getChildren().add(unlWeap3);
            }
        }
        Label nyell=new Label(String.valueOf(Collections.frequency(player.getAmmos(), Color.YELLOW)) + " munizioni gialle");
        Label nblue=new Label(String.valueOf(Collections.frequency(player.getAmmos(), Color.BLUE)) +" munizioni blu");
        Label nred=new Label(String.valueOf(Collections.frequency(player.getAmmos(), Color.RED)) +" munizioni rosse");
        nyell.setLayoutX(10*widthScaleFactor);
        nyell.setLayoutY(200*heightScaleFactor);
        nblue.setLayoutX(10*widthScaleFactor);
        nblue.setLayoutY(230*heightScaleFactor);
        nred.setLayoutX(10*widthScaleFactor);
        nred.setLayoutY(260*heightScaleFactor);

        if(numEnem==1) {
            infostage1.getChildren().addAll(nyell, nblue, nred);
        }
        if(numEnem==2) {
            infostage2.getChildren().addAll(nyell, nblue, nred);
        }
        if(numEnem==3) {
            infostage3.getChildren().addAll(nyell, nblue, nred);
        }
        if(numEnem==4) {
            infostage4.getChildren().addAll(nyell, nblue, nred);
        }

    }

    /**
     * This method sets the pane of the players
     * @param numberEnemy represents which enemy is the player for me
     */

    private static void setStageAp(int numberEnemy){

        if(numberEnemy==1){
            infoStage.setScene(infoscene1);

        }
        if(numberEnemy==2){
            infoStage.setScene(infoscene2);

        }
        if(numberEnemy==3){
            infoStage.setScene(infoscene3);

        }
        if(numberEnemy==4){
            infoStage.setScene(infoscene4);

        }
        //infoStage.initModality(Modality.WINDOW_MODAL);
        infoStage.show();

    }


    /**
     * This method sets the position of a button
     * @param button represents the button
     * @param x represents the x coordinate
     * @param y represents the y coordinate
     */
    private static void setPosButton(Button button, double x, double y){
        button.setLayoutX(x*widthScaleFactor);
        button.setLayoutY(y*heightScaleFactor);
    }

    /**
     * This method configs a button
     * @param button is the button
     * @param prefWidth represents the width to set
     * @param prefHeight represents the height to set
     * @param x represents the x coordinate
     * @param y represents the y coordinate
     * @param playerNumber represents the number of the player
     */
    private static void configButton(Button button, double prefWidth, double prefHeight, double x, double y, int playerNumber) {
        button.setPrefWidth(prefWidth * widthScaleFactor);
        button.setPrefHeight(prefHeight * heightScaleFactor);
        button.setLayoutX(x * widthScaleFactor);
        button.setLayoutY(y * heightScaleFactor);
        ImageView imageView = new ImageView(new Image(MainWindow.class.getResourceAsStream("/gui/user" + playerNumber + ".png")));
        imageView.setFitHeight(prefHeight * heightScaleFactor);
        imageView.setFitWidth(prefWidth * widthScaleFactor);
        button.setGraphic(imageView);
    }

    /**
     * This method configs a transparent button
     * @param button is the button
     * @param prefWidth represents the width to set
     * @param prefHeight represents the height to set
     * @param x represents the x coordinate
     * @param y represents the y coordinate
     */
    private static void configTranspButton(Button button, double prefWidth, double prefHeight, double x, double y){
        button.setPrefWidth(prefWidth*widthScaleFactor);
        button.setPrefHeight(prefHeight*heightScaleFactor);
        button.setLayoutX(x*widthScaleFactor);
        button.setLayoutY(y*heightScaleFactor);
        button.setStyle("-fx-background-color: transparent");
        button.setDisable(true);
    }


    private static void configImg(ImageView img, double width, double x, double y){
        img.setFitWidth(width*widthScaleFactor);
        img.setPreserveRatio(true);
        img.setLayoutX(x*widthScaleFactor);
        img.setLayoutY(y*heightScaleFactor);
    }

    private static void setPosLabel(Label label, double x, double y){
        label.setLayoutX(x*widthScaleFactor);
        label.setLayoutY(y*heightScaleFactor);
    }

    private static void configImageView(ImageView imgv, double width, double height, double x, double y){
        imgv.setFitWidth(width*widthScaleFactor);
        imgv.setFitHeight(height*heightScaleFactor);
        imgv.setLayoutX(x*widthScaleFactor);
        imgv.setLayoutY(y*heightScaleFactor);
    }

    private static void configImgv(ImageView imgv, double width, double x, double y){
        imgv.setFitWidth(width*widthScaleFactor);
        imgv.setPreserveRatio(true);
        imgv.setLayoutX(x*widthScaleFactor);
        imgv.setLayoutY(y*heightScaleFactor);
    }


    /**
     * This method configs the ImagevViews of my weapons
     */
    private static void configMyWeap(){

        wp1= new ImageView();
        configImgv(wp1, 80, 630, 500);
        myw1= new Button();
        configTranspButton(myw1, 80, 135, 630, 500);
        myw1.setDisable(true);

        wp2= new ImageView();
        configImgv(wp2, 80, 720, 500);
        myw2= new Button();
        configTranspButton(myw2, 80, 135, 720, 500);
        myw2.setDisable(true);

        //weap3= new Image("/gui/martelloionico.png");
        wp3= new ImageView();
        configImgv(wp3, 80, 810, 500);
        myw3= new Button();
        configTranspButton(myw3, 80, 135, 810, 500);
        //setMyWeapAction(myw3);
        myw3.setDisable(true);


    }



    /**
     * This method configs the ImageViews of my powerups
     */
    private static void configMyPowerups(){
        //powerup1= new Image("/gui/granatavenom.png");
        pu1= new ImageView();
        configImgv(pu1, 80, 630, 360);
        myp1= new Button();
        configTranspButton(myp1, 80, 135, 630, 360);
        myp1.setDisable(true);

        pu2= new ImageView();
        configImgv(pu2, 80, 720, 360);
        myp2= new Button();
        configTranspButton(myp2, 80, 135, 720, 360);
        myp2.setDisable(true);

        pu3= new ImageView();
        configImgv(pu3, 80, 810, 360);
        myp3= new Button();
        configTranspButton(myp3, 80, 135, 810, 360);
        myp3.setDisable(true);

        pu4= new ImageView();
        configImgv(pu4, 80, 900, 360);
        myp4= new Button();
        configTranspButton(myp4,80, 135, 900, 360);
        myp4.setDisable(true);
    }

    /**
     * This method configs the ImageViews of my ammos
     */
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

        ny=new Label("1");
        nb=new Label("1");
        nr= new Label("1");
    }

    /**
     * This method configs the images and the ImageViews of the playerboards
     */
    private static void configPlayerBoards(){

        if(myTurn==1){
            playerBoard=new Image("/gui/playerBoard.png");
            pla1= new Image("/gui/pl1.png");
            pla2= new Image("/gui/pl2.png");
            pla3= new Image("/gui/pl3.png");
            pla4= new Image("/gui/pl4.png");

        }
        if(myTurn==2){
            pla1= new Image("/gui/playerBoard.png");
            playerBoard=new Image("/gui/pl1.png");
            pla2= new Image("/gui/pl2.png");
            pla3= new Image("/gui/pl3.png");
            pla4= new Image("/gui/pl4.png");

        }
        if(myTurn==3){
            pla1= new Image("/gui/playerBoard.png");
            pla2= new Image("/gui/pl1.png");
            playerBoard=new Image("/gui/pl2.png");
            pla3= new Image("/gui/pl3.png");
            pla4= new Image("/gui/pl4.png");

        }
        if(myTurn==4){
            pla1= new Image("/gui/playerBoard.png");
            pla2= new Image("/gui/pl1.png");
            pla3= new Image("/gui/pl2.png");
            playerBoard=new Image("/gui/pl3.png");
            pla4= new Image("/gui/pl4.png");

        }
        if(myTurn==5){
            pla1= new Image("/gui/playerBoard.png");
            pla2= new Image("/gui/pl1.png");
            pla3= new Image("/gui/pl2.png");
            pla4= new Image("/gui/pl3.png");
            playerBoard=new Image("/gui/pl4");

        }

        plB = new ImageView(playerBoard);
        configImgv(plB, 600, 0, 480);


        //pla1= new Image("/gui/pl1.png");
        pl1= new ImageView(pla1);
        configImgv(pl1, 304, 630, 20);
        //pla2= new Image("/gui/pl2.png");
        pl2= new ImageView(pla2);
        configImgv(pl2, 304, 630, 100);
        //pla3= new Image("/gui/pl3.png");
        pl3= new ImageView(pla3);
        configImgv(pl3, 304, 630, 180);
        //pla4= new Image("/gui/pl4.png");
        pl4= new ImageView(pla4);
        configImgv(pl4, 304, 630, 260);
    }

    /**
     * This method configs the ImageViews and the Labels of the player's marks
     */
    private static void configMyMarks(){

        fmarkdr=new ImageView(imDropYellow);
        configImg(fmarkdr, 15, 325, 485);
        nfmarkdr=new Label("0");
        nfmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfmarkdr, 329, 488);

        smarkdr=new ImageView(imDropGrey);
        configImg(smarkdr, 15, 350, 485);
        nsmarkdr=new Label("0");
        nsmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nsmarkdr, 354, 488);

        tmarkdr=new ImageView(imDropBlue);
        configImg(tmarkdr, 15, 375, 485);
        ntmarkdr=new Label("0");
        ntmarkdr.getStyleClass().add("nbigmark");
        setPosLabel(ntmarkdr, 379, 488);

        fomarkdr=new ImageView(imDropGreen);
        configImg(fomarkdr, 15, 400, 485);
        nfomarkdr=new Label("0");
        nfomarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfomarkdr, 404, 488);

        fimarkdr=new ImageView(imDropPurple);
        configImg(fimarkdr, 15, 425, 485);
        nfimarkdr=new Label("0");
        nfimarkdr.getStyleClass().add("nbigmark");
        setPosLabel(nfimarkdr, 429, 488);
    }

    /**
     * This method configs the ImageViews of the player's damages
     */
    private static void configMyDamages(){

        secdr=new ImageView();
        configImg(secdr, 19, 92, 542);
        firstdr=new ImageView();
        configImg(firstdr, 19, 57, 542);
        thirddr=new ImageView();
        configImg(thirddr, 19, 129, 542);
        fdr=new ImageView();
        configImg(fdr, 19, 163, 542);
        fidr=new ImageView();
        configImg(fidr, 19, 196, 542);
        sdr=new ImageView();
        configImg(sdr, 19, 233, 542);
        sedr=new ImageView();
        configImg(sedr, 19, 266, 542);
        edr=new ImageView();
        configImg(edr, 19, 299, 542);
        ndr=new ImageView();
        configImg(ndr, 19, 332, 542);
        tdr=new ImageView();
        configImg(tdr, 19, 365, 542);
        eldr=new ImageView();
        configImg(eldr, 19, 398, 542);
        twdr=new ImageView();
        configImg(twdr, 19, 431, 542);
    }

    /**
     * This method configs the ImageViews and the labels of the first enemy's marks
     */
    private static void configPl1Marks(){

        fmarkdr1=new ImageView(imDropYellow);
        configImg(fmarkdr1, 8, 792, 22);
        nfmarkdr2=new Label("0");
        nfmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr2, 794, 24);

        smarkdr1=new ImageView(imDropGrey);
        configImg(smarkdr1, 8, 804, 22);
        nsmarkdr2=new Label("0");
        nsmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr2, 806, 24);

        tmarkdr1=new ImageView(imDropBlue);
        configImg(tmarkdr1, 8, 816, 22);
        ntmarkdr2=new Label("0");
        ntmarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr2, 818, 24);

        fomarkdr1=new ImageView(imDropGreen);
        configImg(fomarkdr1, 8, 828, 22);
        nfomarkdr2=new Label("0");
        nfomarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr2, 830, 24);

        fimarkdr1=new ImageView(imDropPurple);
        configImg(fimarkdr1, 8, 840, 22);
        nfimarkdr2=new Label("0");
        nfimarkdr2.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr2, 842, 24);
    }

    /**
     * This method configs the ImageViews of the first enemy's damages
     */
    private static void configPl1Damages(){

        osdr=new ImageView();
        configImg(osdr, 11, 659, 49);
        ssdr=new ImageView();
        configImg(ssdr, 11, 675, 49);
        tsdr=new ImageView();
        configImg(tsdr,11, 693, 49);
        fsdr=new ImageView();
        configImg(fsdr, 11, 709, 49);
        fisdr=new ImageView();
        configImg(fisdr, 11, 726, 49);
        sisdr=new ImageView();
        configImg(sisdr, 11, 743, 49);
        sesdr=new ImageView();
        configImg(sesdr, 11, 760, 49);
        esdr=new ImageView();
        configImg(esdr, 11, 777, 49);
        nsdr=new ImageView();
        configImg(nsdr, 11, 794, 49);
        tesdr=new ImageView();
        configImg(tesdr, 11, 811, 49);
        elsdr=new ImageView();
        configImg(elsdr, 11, 828, 49);
        twsdr= new ImageView();
        configImg(twsdr, 11, 845, 49);
    }

    /**
     * This method configs the ImageViews and the labels of the second enemy's marks
     */
    private static void configPl2Marks(){

        fmarkdr2=new ImageView(imDropYellow);
        configImg(fmarkdr2, 8, 792, 102);
        //se meno giocatori togliere numero label
        nfmarkdr3=new Label("0");
        nfmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr3, 794, 104);

        smarkdr2=new ImageView(imDropGrey);
        configImg(smarkdr2, 8, 804, 102);
        nsmarkdr3=new Label("0");
        nsmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr3, 806, 104);

        tmarkdr2=new ImageView(imDropBlue);
        configImg(tmarkdr2, 8, 816, 102);
        ntmarkdr3=new Label("0");
        ntmarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr3, 818, 104);

        fomarkdr2=new ImageView(imDropGreen);
        configImg(fomarkdr2, 8, 828, 102);
        nfomarkdr3=new Label("0");
        nfomarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr3, 830, 104);

        fimarkdr2=new ImageView(imDropPurple);
        configImg(fimarkdr2, 8, 840, 102);
        nfimarkdr3=new Label("0");
        nfimarkdr3.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr3, 842, 104);

        if(numPlayers<3) {
            fmarkdr2.setImage(null);
            smarkdr2.setImage(null);
            tmarkdr2.setImage(null);
            fomarkdr2.setImage(null);
            fimarkdr2.setImage(null);
            nfmarkdr3.setText("");
            nsmarkdr3.setText("");
            ntmarkdr3.setText("");
            nfomarkdr3.setText("");
            nfimarkdr3.setText("");
        }

    }

    /**
     * This method configs the ImageViews of the second enemy's damages
     */
    private static void configPl2Damages(){

        os2dr=new ImageView();
        configImg(os2dr, 11, 659, 129);
        ss2dr=new ImageView();
        configImg(ss2dr, 11, 675, 129);
        ts2dr=new ImageView();
        configImg(ts2dr, 11, 693, 129);
        fs2dr=new ImageView();
        configImg(fs2dr, 11, 709, 129);
        fis2dr=new ImageView();
        configImg(fis2dr, 11, 726, 129);
        sis2dr=new ImageView();
        configImg(sis2dr, 11, 743, 129);
        ses2dr=new ImageView();
        configImg(ses2dr, 11, 760, 129);
        es2dr=new ImageView();
        configImg(es2dr,11, 777, 129);
        ns2dr=new ImageView();
        configImg(ns2dr, 11, 794, 129);
        tes2dr=new ImageView();
        configImg(tes2dr, 11, 811, 129);
        els2dr=new ImageView();
        configImg(els2dr, 11, 828, 129);
        tws2dr=new ImageView();
        configImg(tws2dr, 11, 845, 129);
    }

    /**
     * This method configs the ImageViews and the labels of the third enemy's marks
     */
    private static void configPl3Marks(){

        fmarkdr3= new ImageView(imDropYellow);
        configImg(fmarkdr3, 8, 792, 182);
        nfmarkdr4=new Label("0");
        nfmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr4, 794, 184);

        smarkdr3= new ImageView(imDropGrey);
        configImg(smarkdr3, 8, 804, 182);
        nsmarkdr4=new Label("0");
        nsmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr4, 806, 184);

        tmarkdr3=new ImageView(imDropBlue);
        configImg(tmarkdr3, 8, 816, 182);
        ntmarkdr4=new Label("0");
        ntmarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr4, 818, 184);

        fomarkdr3=new ImageView(imDropGreen);
        configImg(fomarkdr3, 8, 828, 182);
        nfomarkdr4=new Label("0");
        nfomarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr4, 830,184);

        fimarkdr3=new ImageView(imDropPurple);
        configImg(fimarkdr3, 8, 840, 182);
        nfimarkdr4=new Label("0");
        nfimarkdr4.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr4, 842, 184);

        if(numPlayers<4) {
            fmarkdr3.setImage(null);
            smarkdr3.setImage(null);
            tmarkdr3.setImage(null);
            fomarkdr3.setImage(null);
            fimarkdr3.setImage(null);
            nfmarkdr4.setText("");
            nsmarkdr4.setText("");
            ntmarkdr4.setText("");
            nfomarkdr4.setText("");
            nfimarkdr4.setText("");
        }
    }

    /**
     * This method configs the ImageViews of the third enemy's damages
     */
    private static void configPl3Damages(){

        o3sdr=new ImageView();
        configImg(o3sdr, 11, 659, 209);
        s3sdr=new ImageView();
        configImg(s3sdr, 11, 675, 209);
        t3sdr=new ImageView();
        configImg(t3sdr, 11, 693, 209);
        f3sdr=new ImageView();
        configImg(f3sdr, 11, 709, 209);
        fi3sdr=new ImageView();
        configImg(fi3sdr, 11, 726, 209);
        si3sdr=new ImageView();
        configImg(si3sdr, 11, 743, 209);
        se3sdr=new ImageView();
        configImg(se3sdr, 11, 760, 209);
        e3sdr=new ImageView();
        configImg(e3sdr, 11, 777, 209);
        n3sdr=new ImageView();
        configImg(n3sdr, 11, 794, 209);
        te3sdr=new ImageView();
        configImg(te3sdr, 11, 811, 209);
        el3sdr=new ImageView();
        configImg(el3sdr, 11, 828, 209);
        tw3sdr=new ImageView();
        configImg(tw3sdr, 11, 845, 209);
    }

    /**
     * This method configs the ImageViews and the labels of the fourth enemy's marks
     */
    private static void configPl4Marks(){

        fmarkdr4=new ImageView(imDropYellow);
        configImg(fmarkdr4, 8, 792, 262);
        nfmarkdr5=new Label("2");
        nfmarkdr5.getStyleClass().add("nsmallmark");
        setPosLabel(nfmarkdr5, 794, 264);

        smarkdr4=new ImageView(imDropGrey);
        configImg(smarkdr4, 8, 804, 262);
        nsmarkdr5=new Label("2");
        nsmarkdr5.getStyleClass().add("nsmallmark");
        setPosLabel(nsmarkdr5, 806, 264);

        tmarkdr4=new ImageView(imDropBlue);
        configImg(tmarkdr4, 8, 816, 262);
        ntmarkdr5=new Label("2");
        ntmarkdr5.getStyleClass().add("nsmallmark");
        setPosLabel(ntmarkdr5, 818, 264);

        fomarkdr4=new ImageView(imDropGreen);
        configImg(fomarkdr4, 8, 828, 262);
        nfomarkdr5=new Label("2");
        nfomarkdr5.getStyleClass().add("nsmallmark");
        setPosLabel(nfomarkdr5, 830, 264);

        fimarkdr4=new ImageView(imDropPurple);
        configImg(fimarkdr4, 8, 840, 262);
        nfimarkdr5=new Label("0");
        nfimarkdr5.getStyleClass().add("nsmallmark");
        setPosLabel(nfimarkdr5, 842, 264);

        if(numPlayers<5) {
            fmarkdr4.setImage(null);
            smarkdr4.setImage(null);
            tmarkdr4.setImage(null);
            fomarkdr4.setImage(null);
            fimarkdr4.setImage(null);
            nfmarkdr5.setText("");
            nsmarkdr5.setText("");
            ntmarkdr5.setText("");
            nfomarkdr5.setText("");
            nfimarkdr5.setText("");
        }
    }

    /**
     * This method configs the ImageViews of the fourth enemy's damages
     */
    private static void configPl4Damages(){

        o4sdr=new ImageView();
        configImg(o4sdr, 11, 659, 289);
        s4sdr=new ImageView();
        configImg(s4sdr, 11, 675, 289);
        t4sdr=new ImageView();
        configImg(t4sdr, 11, 693, 289);
        f4sdr=new ImageView();
        configImg(f4sdr, 11, 709, 289);
        fi4sdr=new ImageView();
        configImg(fi4sdr, 11, 726, 289);
        si4sdr=new ImageView();
        configImg(si4sdr, 11, 743, 289);
        se4sdr=new ImageView();
        configImg(se4sdr, 11, 760, 289);
        e4sdr=new ImageView();
        configImg(e4sdr, 11, 777, 289);
        n4sdr=new ImageView();
        configImg(n4sdr, 11, 794, 289);
        te4sdr=new ImageView();
        configImg(te4sdr, 11, 811, 289);
        el4sdr=new ImageView();
        configImg(el4sdr, 11, 828, 289);
        tw4sdr=new ImageView();
        configImg(tw4sdr, 11, 845, 289);
    }

    /**
     * This method sets the action of the user button when entered and exited
     * @param user represents the user button
     * @param img represents the image to show when entered
     */
    private static void setActionUserButton(Button user, Image img){
        user.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedUser.setImage(img);
            }
        });
        user.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedUser.setImage(null);
            }
        });
    }


    /**
     * This method configs the user buttons
     */
    private static void configUserButtons(){

        user1=new Button();
        user2=new Button();
        user3=new Button();
        user4=new Button();
        user5=new Button();

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


    /**
     * This method configs the ImageViews of the weapons on the board
     */
    private static void configWeapBoardImg(){
        //wea1=new Image("/gui/cannonevortex.png");
        wa1=new ImageView(im1);
        configImageView(wa1, 57, 84, 317, 2);

        //wea2=new Image("/gui/lanciafiamme.png");
        wa2=new ImageView(im2);
        configImageView(wa2, 57, 84, 383, 2);

        //wea3=new Image("/gui/lanciagranate.png");
        wa3=new ImageView(im3);
        configImageView(wa3, 57, 84, 449, 2);

        //wea4=new Image("/gui/mitragliatrice.png");
        wa4=new ImageView(im4);
        configImageView(wa4, 57, 84, 15.5, 152.5);
        wa4.setRotate(270);

        //wea5=new Image("/gui/fucilealplasma.png");
        wa5=new ImageView(im5);
        configImageView(wa5, 57, 84, 15.5, 218.5);
        wa5.setRotate(270);

        //wea6=new Image("/gui/fucilediprecisione.png");
        wa6=new ImageView(im6);
        configImageView(wa6, 57, 84, 15.5, 284.5);
        wa6.setRotate(270);

        //wea7=new Image("/gui/raggiotraente.png");
        wa7=new ImageView(im7);
        configImageView(wa7, 57, 84, 529.5, 243.5);
        wa7.setRotate(90);

        //wea8=new Image("/gui/torpedine.png");
        wa8=new ImageView(im8);
        configImageView(wa8, 57, 84, 529.5, 309.5);
        wa8.setRotate(90);

        //wea9=new Image("/gui/raggiosolare.png");
        wa9=new ImageView(im9);
        configImageView(wa9, 57, 84, 529.5, 375.5);
        wa9.setRotate(90);
    }

    /**
     * This method sets the action of the button of the weapons on the board when entered or exited
     * @param a represents the button of the weapon on the board
     * @param img represents the image to show
     */
    private static void setActionWeapBoardButton(Button a, Image img){

        a.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedimg.setImage(img);
                //zoomedImage=zoomWeapp(img);
            }
        });
        a.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomedimg.setImage(null);
                //zoomedImage.close();
            }
        });
    }

    /**
     * This method configs the buttons of the weapons on the board
     */
    private static void configWeapBoardButtons(){
        a1=new Button();
        configTranspButton(a1, 57, 84, 317, 2);
        setActionWeapBoardButton(a1, weaponsHashMap.get(weapButtonId.get(a1)));
        a1.setStyle("-fx-background-color: transparent");

        a2=new Button();
        configTranspButton(a2, 57, 84, 383, 2);
        setActionWeapBoardButton(a2, weaponsHashMap.get(weapButtonId.get(a2)));
        a2.setStyle("-fx-background-color: transparent");

        a3=new Button();
        configTranspButton(a3, 57, 84, 449, 2);
        setActionWeapBoardButton(a3, weaponsHashMap.get(weapButtonId.get(a3)));
        a3.setStyle("-fx-background-color: transparent");

        a4=new Button();
        configTranspButton(a4, 84, 57, 2, 166);
        setActionWeapBoardButton(a4, weaponsHashMap.get(weapButtonId.get(a4)));
        a4.setStyle("-fx-background-color: transparent");

        a5=new Button();
        configTranspButton(a5, 84, 57, 2, 232);
        setActionWeapBoardButton(a5, weaponsHashMap.get(weapButtonId.get(a5)));
        a5.setStyle("-fx-background-color: transparent");

        a6=new Button();
        configTranspButton(a6, 84, 57, 2, 298);
        setActionWeapBoardButton(a6, weaponsHashMap.get(weapButtonId.get(a6)));
        a6.setStyle("-fx-background-color: transparent");

        a7=new Button();
        configTranspButton(a7, 84, 57, 516, 257);
        setActionWeapBoardButton(a7, weaponsHashMap.get(weapButtonId.get(a7)));
        a7.setStyle("-fx-background-color: transparent");

        a8=new Button();
        configTranspButton(a8, 84, 57, 516, 323);
        setActionWeapBoardButton(a8, weaponsHashMap.get(weapButtonId.get(a8)));
        a8.setStyle("-fx-background-color: transparent");

        a9=new Button();
        configTranspButton(a9, 84, 57, 516, 389);
        setActionWeapBoardButton(a9, weaponsHashMap.get(weapButtonId.get(a9)));
        a9.setStyle("-fx-background-color: transparent");

    }


    /**
     * This method initializes the second map
     * @param ap represents the AnchorPane for the map
     */
    private static void initMap2(AnchorPane ap){

        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(i==2&&j==0){
                    continue;
                }else{
                    squareMatrix[i][j]= new SquareWindow(i, j, 2, widthScaleFactor, heightScaleFactor);
                    //squareButtonMatrix[i][j]=squareMatrix[i][j].getSquareButton();
                    ap.getChildren().add(squareMatrix[i][j].getSquareButton());
                    if(squareMatrix[i][j].hasAmmoPoint()) ap.getChildren().add(squareMatrix[i][j].getAmmo());
                    if(squareMatrix[i][j].hasRespawn()) ap.getChildren().addAll(squareMatrix[i][j].getWeapon1(), squareMatrix[i][j].getWeapon2(), squareMatrix[i][j].getWeapon3());

                }
            }
        }


    }

    /**
     * This method initializes the first map
     * @param ap represents the AnchorPane for the map
     */
    private static void initMap1(AnchorPane ap){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if((i==2&&j==0)||(i==0&&j==3)){
                    continue;
                }else{
                    squareMatrix[i][j]= new SquareWindow(i, j, 1, widthScaleFactor, heightScaleFactor);
                    //squareButtonMatrix[i][j]=squareMatrix[i][j].getSquareButton();
                    ap.getChildren().add(squareMatrix[i][j].getSquareButton());
                    if(squareMatrix[i][j].hasAmmoPoint()) ap.getChildren().add(squareMatrix[i][j].getAmmo());
                    if(squareMatrix[i][j].hasRespawn()) ap.getChildren().addAll(squareMatrix[i][j].getWeapon1(), squareMatrix[i][j].getWeapon2(), squareMatrix[i][j].getWeapon3());

                }
            }
        }



    }

    /**
     * This method initializes the third map
     * @param ap represents the AnchorPane for the map
     */
    private static void initMap3(AnchorPane ap){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                    squareMatrix[i][j]= new SquareWindow(i, j, 3, widthScaleFactor, heightScaleFactor);
                    //squareButtonMatrix[i][j]=squareMatrix[i][j].getSquareButton();
                    ap.getChildren().add(squareMatrix[i][j].getSquareButton());
                    if(squareMatrix[i][j].hasAmmoPoint()) ap.getChildren().add(squareMatrix[i][j].getAmmo());
                    if(squareMatrix[i][j].hasRespawn()) ap.getChildren().addAll(squareMatrix[i][j].getWeapon1(), squareMatrix[i][j].getWeapon2(), squareMatrix[i][j].getWeapon3());

            }
        }




    }

    /**
     * This method initializes the fourth map
     * @param ap represents the AnchorPane for the map
     */
    private static void initMap4(AnchorPane ap){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(i==0&&j==3){
                    continue;
                }else{
                    squareMatrix[i][j]= new SquareWindow(i, j, 4, widthScaleFactor, heightScaleFactor);
                    //squareButtonMatrix[i][j]=squareMatrix[i][j].getSquareButton();
                    ap.getChildren().add(squareMatrix[i][j].getSquareButton());
                    if(squareMatrix[i][j].hasAmmoPoint()) ap.getChildren().add(squareMatrix[i][j].getAmmo());
                    if(squareMatrix[i][j].hasRespawn()) ap.getChildren().addAll(squareMatrix[i][j].getWeapon1(), squareMatrix[i][j].getWeapon2(), squareMatrix[i][j].getWeapon3());
                }
            }
        }


    }


    /**
     * This method disables all the buttons of the squares on the map
     */
    private static void disableAllSquareButtons(){
        //devo farlo solo per i bottoni realmente esistenti e dipende da mappa scelta
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(squareMatrix[i][j]!=null) {
                    squareMatrix[i][j].getSquareButton().setDisable(true);
                    squareMatrix[i][j].getSquareButton().setStyle("-fx-background-color: transparent");
                }
            }
        }
    }

    /**
     * This method converts an id string in the relative path for the image
     * @param idcard represents the id
     * @return a string representing the relative path of the image
     */
    private static String convertIdImg(String idcard){
        String idImg="/gui/"+idcard+".png";
        return idImg;
    }


    /**
     * This method set the position of the user button of the player
     * @param pos
     */
    private static void setMyPosition(int[] pos){

        if(pos==null) return;
        //mettere a posto
        if(myTurn==1) {
            user1.setLayoutX(squareMatrix[pos[0]][pos[1]].getUs1()[0]);
            user1.setLayoutY(squareMatrix[pos[0]][pos[1]].getUs1()[1]);
        }
        if(myTurn==2) {
            user2.setLayoutX(squareMatrix[pos[0]][pos[1]].getUs2()[0]);
            user2.setLayoutY(squareMatrix[pos[0]][pos[1]].getUs2()[1]);
        }
        if(myTurn==3) {
            user3.setLayoutX(squareMatrix[pos[0]][pos[1]].getUs3()[0]);
            user3.setLayoutY(squareMatrix[pos[0]][pos[1]].getUs3()[1]);
        }
        if(myTurn==4) {
            user4.setLayoutX(squareMatrix[pos[0]][pos[1]].getUs4()[0]);
            user4.setLayoutY(squareMatrix[pos[0]][pos[1]].getUs4()[1]);
        }
        if(myTurn==5) {
            user5.setLayoutX(squareMatrix[pos[0]][pos[1]].getUs5()[0]);
            user5.setLayoutY(squareMatrix[pos[0]][pos[1]].getUs5()[1]);
        }

    }

    /**
     * This method sets the marks of the player
     * @param myMarks represents the players who marked the current player
     */
    private static void setMyMarks(List<String> myMarks){
        for(int i=0; i<5; i++){
            int t= Collections.frequency(myMarks, turnNicknameHashMap.get(i+1));
            myMarksLabelHashMap.get(i+1).setText(String.valueOf(t));
            //hashmap nickname-immagine goccia
        }

    }

    /**
     * This method sets the damages of the player
     * @param myDamages represents the players who damaged the current player
     */
    private static void setMyDamages(List<String> myDamages){
        for(int j=0; j<12; j++){
            myDamagesImageViewHashMap.get(j+1).setImage(null);
        }
        for(int i=0; i<myDamages.size(); i++){
            myDamagesImageViewHashMap.get(i+1).setImage(damagesHashMap.get(myDamages.get(i)));
        }

    }


    /**
     * This method sets player's weapons
     * @param myWeapons represents the weapon cards of the player
     */
    private static void setMyWeapons(List<Card> myWeapons){
        for(int i=0; i<myWeapons.size(); i++){
            //String idWeap=convertIdImg(myWeapons.get(i).getId());
            //Image myWeap=new Image(idWeap);
            myWeaponHashMap.get(i+1).setImage(weaponsHashMap.get(myWeapons.get(i).getId()));
            myWeaponsPosition.put(myWeapons.get(i).getId(), i+1);
        }

        switch (myWeapons.size()){
            case 0: break;
            case 1:
                weapButtonId.put(myw1, myWeapons.get(0).getId());
                break;
            case 2:
                weapButtonId.put(myw1, myWeapons.get(0).getId());
                weapButtonId.put(myw2, myWeapons.get(1).getId());
                break;
            case 3:
                weapButtonId.put(myw1, myWeapons.get(0).getId());
                weapButtonId.put(myw2, myWeapons.get(1).getId());
                weapButtonId.put(myw3, myWeapons.get(2).getId());
                break;
        }

    }

    /**
     * This method sets player's unloaded weapons
     * @param myUnloadedWeapons represents the unloaded weapons of the player
     */
    private static void setMyUnloadedWeapons(List<String> myUnloadedWeapons){
        for(int i=0; i<myUnloadedWeapons.size(); i++){

            myWeaponsLabel.get(myWeaponsPosition.get(myUnloadedWeapons.get(i))).setText("Scarica");
        }
    }

    /**
     * This methos sets the powerups of the player
     * @param myPowerups represents the powerups of the player
     */
    private static void setMyPowerups(List<Card> myPowerups) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            pu1.setImage(null);
            pu2.setImage(null);
            pu3.setImage(null);
            pu4.setImage(null);

            for (int i = 0; i < myPowerups.size(); i++) {

                myPowerupsHashMap.get(i + 1).setImage(powerupsHashMap.get(myPowerups.get(i).getId()));
                myPowerupsPosition.put(myPowerups.get(i).getId(), i + 1);
            }

            switch (myPowerups.size()) {
                case 0:
                    break;
                case 1:
                    powerupAvailableLabel.put(myPowerups.get(0).getId(), myPowerup1Label);
                    powButtonId.put(myp1, myPowerups.get(0).getId());
                    break;
                case 2:
                    powerupAvailableLabel.put(myPowerups.get(0).getId(), myPowerup1Label);
                    powerupAvailableLabel.put(myPowerups.get(1).getId(), myPowerup2Label);
                    powButtonId.put(myp1, myPowerups.get(0).getId());
                    powButtonId.put(myp2, myPowerups.get(1).getId());
                    break;
                case 3:
                    powerupAvailableLabel.put(myPowerups.get(0).getId(), myPowerup1Label);
                    powerupAvailableLabel.put(myPowerups.get(1).getId(), myPowerup2Label);
                    powerupAvailableLabel.put(myPowerups.get(2).getId(), myPowerup3Label);
                    powButtonId.put(myp1, myPowerups.get(0).getId());
                    powButtonId.put(myp2, myPowerups.get(1).getId());
                    powButtonId.put(myp3, myPowerups.get(2).getId());
                    break;
                case 4:
                    powerupAvailableLabel.put(myPowerups.get(0).getId(), myPowerup1Label);
                    powerupAvailableLabel.put(myPowerups.get(1).getId(), myPowerup2Label);
                    powerupAvailableLabel.put(myPowerups.get(2).getId(), myPowerup3Label);
                    powerupAvailableLabel.put(myPowerups.get(3).getId(), myPowerup4Label);
                    powButtonId.put(myp1, myPowerups.get(0).getId());
                    powButtonId.put(myp2, myPowerups.get(1).getId());
                    powButtonId.put(myp3, myPowerups.get(2).getId());
                    powButtonId.put(myp4, myPowerups.get(3).getId());
                    break;
            }
        });
    }

    /**
     * This method sets the death counter of the player
     * @param player represents the current player
     */
    private static void setMyDeathCounter(SimplePlayer player){
        mydeath1.setImage(null);
        mydeath2.setImage(null);
        mydeath3.setImage(null);
        mydeath4.setImage(null);
        mydeath5.setImage(null);
        mydeath6.setImage(null);
        mydeath1fr.setImage(null);
        mydeath2fr.setImage(null);
        mydeath3fr.setImage(null);
        mydeath4fr.setImage(null);

        if(player.isSwitched()){
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    mydeath1fr.setImage(skull1img);
                    break;
                case 2:
                    mydeath1fr.setImage(skull1img);
                    mydeath2fr.setImage(skull1img);
                    break;
                case 3:
                    mydeath1fr.setImage(skull1img);
                    mydeath2fr.setImage(skull1img);
                    mydeath3fr.setImage(skull1img);
                    break;
                case 4:
                    mydeath1fr.setImage(skull1img);
                    mydeath2fr.setImage(skull1img);
                    mydeath3fr.setImage(skull1img);
                    mydeath4fr.setImage(skull1img);
                    break;
            }
        }else{
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    mydeath1.setImage(skull1img);
                    break;
                case 2:
                    mydeath1.setImage(skull1img);
                    mydeath2.setImage(skull1img);
                    break;
                case 3:
                    mydeath1.setImage(skull1img);
                    mydeath2.setImage(skull1img);
                    mydeath3.setImage(skull1img);
                    break;
                case 4:
                    mydeath1.setImage(skull1img);
                    mydeath2.setImage(skull1img);
                    mydeath3.setImage(skull1img);
                    mydeath4.setImage(skull1img);
                    break;
                case 5:
                    mydeath1.setImage(skull1img);
                    mydeath2.setImage(skull1img);
                    mydeath3.setImage(skull1img);
                    mydeath4.setImage(skull1img);
                    mydeath5.setImage(skull1img);
                    break;
                case 6:
                    mydeath1.setImage(skull1img);
                    mydeath2.setImage(skull1img);
                    mydeath3.setImage(skull1img);
                    mydeath4.setImage(skull1img);
                    mydeath5.setImage(skull1img);
                    mydeath6.setImage(skull1img);
                    break;
            }
        }
        //settare con lo switch e le imageview
    }

    /**
     * This method sets the text of the usable powerups to "Usabile"
     * @param usPowerup
     */
    private static void setUsablePowerup(String usPowerup){
        powerupAvailableLabel.get(usPowerup).setText("Usabile");

    }

    /**
     * This method sets the action of the player's weapon's button
     * @param weapons represents the weapons of the player
     * @param usWeapon represents the weapon that can be used
     */
    private static void setClickWeapon(List<Card> weapons, String usWeapon){
        myWeaponsLabel.get(myWeaponsPosition.get(usWeapon)).setText("Usabile");
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setDisable(false);
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw1.setDisable(true);
                myw2.setDisable(true);
                myw3.setDisable(true);
                for(int i=0; i<weapons.size(); i++) {
                    myWeaponsLabel.get(myWeaponsPosition.get(weapons.get(i).getId())).setText("");
                }
                myWeaponsLabel.get(myWeaponsPosition.get(usWeapon)).setText("Scarica");
                String idSelWeapon=weapButtonId.get(myWeaponsButton.get(myWeaponsPosition.get(usWeapon)));
                for(int i=0; i<weapons.size(); i++){
                    if(weapons.get(i).getId().equalsIgnoreCase(idSelWeapon)){
                        connection.selectWeapon(weapons.get(i));
                        break;
                    }
                }
            }
        });
    }


    private static void setClickWeaponToDiscard(List<Card> weapons, String usWeapon){
        myWeaponsLabel.get(myWeaponsPosition.get(usWeapon)).setText("Scarta");
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setDisable(false);
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw1.setDisable(true);
                myw2.setDisable(true);
                myw3.setDisable(true);
                for(int i=0; i<weapons.size(); i++) {
                    myWeaponsLabel.get(myWeaponsPosition.get(weapons.get(i).getId())).setText("");
                }
                myWeaponHashMap.get(myWeaponsPosition.get(usWeapon)).setImage(null);
                String idSelWeapon=weapButtonId.get(myWeaponsButton.get(myWeaponsPosition.get(usWeapon)));
                for(int i=0; i<weapons.size(); i++){
                    if(weapons.get(i).getId().equalsIgnoreCase(idSelWeapon)){
                        connection.discardWeapon(weapons.get(i));
                        break;
                    }
                }
            }
        });
    }

    private static void setClickWeaponToReload(List<Card> weapons, String usWeapon){
        myWeaponsLabel.get(myWeaponsPosition.get(usWeapon)).setText("Scarica");
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setDisable(false);
        myWeaponsButton.get(myWeaponsPosition.get(usWeapon)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myw1.setDisable(true);
                myw2.setDisable(true);
                myw3.setDisable(true);
                String idSelWeapon=weapButtonId.get(myWeaponsButton.get(myWeaponsPosition.get(usWeapon)));
                for(int i=0; i<weapons.size(); i++){
                    if(weapons.get(i).getId().equalsIgnoreCase(idSelWeapon)){
                        connection.loadableWeapon(weapons.get(i));
                        break;
                    }
                }
            }
        });
    }

    private static void setClickGrabWeapon(List<Card> weapons, String usWeapon){
        boardWeaponsButton.get(idWeaponPosition.get(usWeapon)).setDisable(false);
        setActionWeapBoardButton(boardWeaponsButton.get(idWeaponPosition.get(usWeapon)), weaponsHashMap.get(usWeapon) );
        boardWeaponsButton.get(idWeaponPosition.get(usWeapon)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boardWeaponsButton.get(idWeaponPosition.get(usWeapon)).setDisable(true);
                for(int i=1; i<10; i++){
                    boardWeaponsButton.get(i).setDisable(true);
                }
                //String idSelWeapon=weapButtonId.get(boardWeaponsButton.get(idWeaponPosition.get(usWeapon)));
                for(int i=0; i<weapons.size(); i++){
                    if(weapons.get(i).getId().equalsIgnoreCase(usWeapon)){
                        connection.selectWeapon(weapons.get(i));
                        break;
                    }
                }
            }
        });
    }

    private static void setMyAmmo(SimplePlayer player){
        int redammos=Collections.frequency(player.getAmmos(), Color.RED);
        int blueammos=Collections.frequency(player.getAmmos(), Color.BLUE);
        int yellowammos=Collections.frequency(player.getAmmos(), Color.YELLOW);
        nr.setText(String.valueOf(redammos));
        nb.setText(String.valueOf(blueammos));
        ny.setText(String.valueOf(yellowammos));
    }

    private static void setMyPowerupsAction(String usPoweup, List<Card> powerups){

        myPowerupsButton.get(myPowerupsPosition.get(usPoweup)).setDisable(false);
        myPowerupsButton.get(myPowerupsPosition.get(usPoweup)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myp1.setDisable(true);
                myp2.setDisable(true);
                myp3.setDisable(true);
                myp4.setDisable(true);
                myPowerup1Label.setText(null);
                myPowerup2Label.setText(null);
                myPowerup3Label.setText(null);
                myPowerup4Label.setText(null);
                myPowerupsHashMap.get(myPowerupsPosition.get(usPoweup)).setImage(null);
                String idSelPowerup=powButtonId.get(myPowerupsButton.get(myPowerupsPosition.get(usPoweup)));
                for (int i=0; i<powerups.size(); i++){
                    if(powerups.get(i).getId().equalsIgnoreCase(idSelPowerup)){
                        connection.selectPowerup(powerups.get(i));
                        break;
                    }
                }
            }
        });
    }

    private static void setMyPowerupsActionForRespawn(String usPowerup, List<Card> powerups){

        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            myPowerupsButton.get(myPowerupsPosition.get(usPowerup)).setDisable(false);
            myPowerupsButton.get(myPowerupsPosition.get(usPowerup)).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    myPowerupsHashMap.get(myPowerupsPosition.get(usPowerup)).setImage(null);
                    myp1.setDisable(true);
                    myp2.setDisable(true);
                    myp3.setDisable(true);
                    myp4.setDisable(true);
                    String idSelPowerup = powButtonId.get(myPowerupsButton.get(myPowerupsPosition.get(usPowerup)));
                    for (int i = 0; i < powerups.size(); i++) {
                        if (powerups.get(i).getId().equalsIgnoreCase(idSelPowerup)) {
                            connection.respawnPlayer(powerups.get(i));
                            break;
                        }
                    }
                }
            });
        });
    }



    private static void setPosition(int[] pos, int turnPl){
        /*int user=0;
        for(int j=0; j<numbEnemyNickname.size(); j++){
            if(numbEnemyNickname.get(j+1).equalsIgnoreCase(turnNicknameHashMap.get(us))){
                user=j+1;
            }
        }*/
        switch (turnPl) {
            case 1:
                userButtonHashMap.get(turnPl).setLayoutX(squareMatrix[pos[0]][pos[1]].getUs1()[0]);
                userButtonHashMap.get(turnPl).setLayoutY(squareMatrix[pos[0]][pos[1]].getUs1()[1]);
                break;

            case 2:
                userButtonHashMap.get(turnPl).setLayoutX(squareMatrix[pos[0]][pos[1]].getUs2()[0]);
                userButtonHashMap.get(turnPl).setLayoutY(squareMatrix[pos[0]][pos[1]].getUs2()[1]);
                break;

            case 3:
                userButtonHashMap.get(turnPl).setLayoutX(squareMatrix[pos[0]][pos[1]].getUs3()[0]);
                userButtonHashMap.get(turnPl).setLayoutY(squareMatrix[pos[0]][pos[1]].getUs3()[1]);
                break;

            case 4:
                userButtonHashMap.get(turnPl).setLayoutX(squareMatrix[pos[0]][pos[1]].getUs4()[0]);
                userButtonHashMap.get(turnPl).setLayoutY(squareMatrix[pos[0]][pos[1]].getUs4()[1]);
                break;

            case 5:
                userButtonHashMap.get(turnPl).setLayoutX(squareMatrix[pos[0]][pos[1]].getUs5()[0]);
                userButtonHashMap.get(turnPl).setLayoutY(squareMatrix[pos[0]][pos[1]].getUs5()[1]);
                break;
        }

    }

    private static void updateMarks(List<String> marks, String nickEnemy){
        /*int user=0;
        for(int j=0; j<numPlayers-1; j++){
            if(numbEnemyNickname.get(j+1).equalsIgnoreCase(turnNicknameHashMap.get(us))){
                user=j+1;
            }
        }*/
        int numEnemy=0;
        for(int i=0; i<numbEnemyNickname.size(); i++){
            if(nickEnemy.equalsIgnoreCase(numbEnemyNickname.get(i+1))){
                numEnemy=i+1;
            }
        }

        for(int i=0; i<numPlayers; i++) {
            marksLabelHashMap.get(i+1+5*(numEnemy-1)).setText("0");
        }


        for(int i=0; i<numPlayers; i++) {
            int t = Collections.frequency(marks, turnNicknameHashMap.get(i+1));
            marksLabelHashMap.get(i+1+5*(numEnemy-1)).setText(String.valueOf(t));
        }

    }

    private static void updateDamages(List<String> damages, int us){
        int user=0;
        for(int j=0; j<numPlayers-1; j++){
            if(numbEnemyNickname.get(j+1).equalsIgnoreCase(turnNicknameHashMap.get(us))){
                user=j+1;
            }
        }
        for(int i=0; i<12; i++){
            damageImageViewHashMap.get(i+1+12*(user-1)).setImage(null);
        }

        for(int i=0; i<damages.size(); i++){
            damageImageViewHashMap.get(i+1+12*(user-1)).setImage(damagesHashMap.get(damages.get(i)));
        }

    }

    private static void updateUnloadedWeapons(SimplePlayer player, List<String> unloadedWeapons, int us){

        updatePlayerVisibility(player);


    }

    private static void updateDeathCounter1(SimplePlayer player){
        en1death1.setImage(null);
        en1death2.setImage(null);
        en1death3.setImage(null);
        en1death4.setImage(null);
        en1death5.setImage(null);
        en1death6.setImage(null);
        en1death1fr.setImage(null);
        en1death2fr.setImage(null);
        en1death3fr.setImage(null);
        en1death4fr.setImage(null);

        if(player.isSwitched()){
                switch (player.getDeathCounter()){
                    case 0:
                        break;
                    case 1:
                        en1death1fr.setImage(skull1img);
                        break;
                    case 2:
                        en1death1fr.setImage(skull1img);
                        en1death2fr.setImage(skull1img);
                        break;
                    case 3:
                        en1death1fr.setImage(skull1img);
                        en1death2fr.setImage(skull1img);
                        en1death3fr.setImage(skull1img);
                        break;
                    case 4:
                        en1death1fr.setImage(skull1img);
                        en1death2fr.setImage(skull1img);
                        en1death3fr.setImage(skull1img);
                        en1death4fr.setImage(skull1img);
                        break;
                }
            }else{
                switch (player.getDeathCounter()){
                    case 0:
                        break;
                    case 1:
                        en1death1.setImage(skull1img);
                        break;
                    case 2:
                        en1death1.setImage(skull1img);
                        en1death2.setImage(skull1img);
                        break;
                    case 3:
                        en1death1.setImage(skull1img);
                        en1death2.setImage(skull1img);
                        en1death3.setImage(skull1img);
                        break;
                    case 4:
                        en1death1.setImage(skull1img);
                        en1death2.setImage(skull1img);
                        en1death3.setImage(skull1img);
                        en1death4.setImage(skull1img);
                        break;
                    case 5:
                        en1death1.setImage(skull1img);
                        en1death2.setImage(skull1img);
                        en1death3.setImage(skull1img);
                        en1death4.setImage(skull1img);
                        en1death5.setImage(skull1img);
                        break;
                    case 6:
                        en1death1.setImage(skull1img);
                        en1death2.setImage(skull1img);
                        en1death3.setImage(skull1img);
                        en1death4.setImage(skull1img);
                        en1death5.setImage(skull1img);
                        en1death6.setImage(skull1img);
                        break;
                }
            }
    }

    private static void updateDeathCounter2(SimplePlayer player){
        en2death1.setImage(null);
        en2death2.setImage(null);
        en2death3.setImage(null);
        en2death4.setImage(null);
        en2death5.setImage(null);
        en2death6.setImage(null);
        en2death1fr.setImage(null);
        en2death2fr.setImage(null);
        en2death3fr.setImage(null);
        en2death4fr.setImage(null);

        if(player.isSwitched()){
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en2death1fr.setImage(skull1img);
                    break;
                case 2:
                    en2death1fr.setImage(skull1img);
                    en2death2fr.setImage(skull1img);
                    break;
                case 3:
                    en2death1fr.setImage(skull1img);
                    en2death2fr.setImage(skull1img);
                    en2death3fr.setImage(skull1img);
                    break;
                case 4:
                    en2death1fr.setImage(skull1img);
                    en2death2fr.setImage(skull1img);
                    en2death3fr.setImage(skull1img);
                    en2death4fr.setImage(skull1img);
                    break;
            }
        }else{
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en2death1.setImage(skull1img);
                    break;
                case 2:
                    en2death1.setImage(skull1img);
                    en2death2.setImage(skull1img);
                    break;
                case 3:
                    en2death1.setImage(skull1img);
                    en2death2.setImage(skull1img);
                    en2death3.setImage(skull1img);
                    break;
                case 4:
                    en2death1.setImage(skull1img);
                    en2death2.setImage(skull1img);
                    en2death3.setImage(skull1img);
                    en2death4.setImage(skull1img);
                    break;
                case 5:
                    en2death1.setImage(skull1img);
                    en2death2.setImage(skull1img);
                    en2death3.setImage(skull1img);
                    en2death4.setImage(skull1img);
                    en2death5.setImage(skull1img);
                    break;
                case 6:
                    en2death1.setImage(skull1img);
                    en2death2.setImage(skull1img);
                    en2death3.setImage(skull1img);
                    en2death4.setImage(skull1img);
                    en2death5.setImage(skull1img);
                    en2death6.setImage(skull1img);
                    break;
            }
        }
    }

    private static void updateDeathCounter3(SimplePlayer player){
        en3death1.setImage(null);
        en3death2.setImage(null);
        en3death3.setImage(null);
        en3death4.setImage(null);
        en3death5.setImage(null);
        en3death6.setImage(null);
        en3death1fr.setImage(null);
        en3death2fr.setImage(null);
        en3death3fr.setImage(null);
        en3death4fr.setImage(null);

        if(player.isSwitched()){
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en3death1fr.setImage(skull1img);
                    break;
                case 2:
                    en3death1fr.setImage(skull1img);
                    en3death2fr.setImage(skull1img);
                    break;
                case 3:
                    en3death1fr.setImage(skull1img);
                    en3death2fr.setImage(skull1img);
                    en3death3fr.setImage(skull1img);
                    break;
                case 4:
                    en3death1fr.setImage(skull1img);
                    en3death2fr.setImage(skull1img);
                    en3death3fr.setImage(skull1img);
                    en3death4fr.setImage(skull1img);
                    break;
            }
        }else{
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en3death1.setImage(skull1img);
                    break;
                case 2:
                    en3death1.setImage(skull1img);
                    en3death2.setImage(skull1img);
                    break;
                case 3:
                    en3death1.setImage(skull1img);
                    en3death2.setImage(skull1img);
                    en3death3.setImage(skull1img);
                    break;
                case 4:
                    en3death1.setImage(skull1img);
                    en3death2.setImage(skull1img);
                    en3death3.setImage(skull1img);
                    en3death4.setImage(skull1img);
                    break;
                case 5:
                    en3death1.setImage(skull1img);
                    en3death2.setImage(skull1img);
                    en3death3.setImage(skull1img);
                    en3death4.setImage(skull1img);
                    en3death5.setImage(skull1img);
                    break;
                case 6:
                    en3death1.setImage(skull1img);
                    en3death2.setImage(skull1img);
                    en3death3.setImage(skull1img);
                    en3death4.setImage(skull1img);
                    en3death5.setImage(skull1img);
                    en3death6.setImage(skull1img);
                    break;
            }
        }
    }

    private static void updateDeathCounter4(SimplePlayer player){
        en4death1.setImage(null);
        en4death2.setImage(null);
        en4death3.setImage(null);
        en4death4.setImage(null);
        en4death5.setImage(null);
        en4death6.setImage(null);
        en4death1fr.setImage(null);
        en4death2fr.setImage(null);
        en4death3fr.setImage(null);
        en4death4fr.setImage(null);

        if(player.isSwitched()){
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en4death1fr.setImage(skull1img);
                    break;
                case 2:
                    en4death1fr.setImage(skull1img);
                    en4death2fr.setImage(skull1img);
                    break;
                case 3:
                    en4death1fr.setImage(skull1img);
                    en4death2fr.setImage(skull1img);
                    en4death3fr.setImage(skull1img);
                    break;
                case 4:
                    en4death1fr.setImage(skull1img);
                    en4death2fr.setImage(skull1img);
                    en4death3fr.setImage(skull1img);
                    en4death4fr.setImage(skull1img);
                    break;
            }
        }else{
            switch (player.getDeathCounter()){
                case 0:
                    break;
                case 1:
                    en4death1.setImage(skull1img);
                    break;
                case 2:
                    en4death1.setImage(skull1img);
                    en4death2.setImage(skull1img);
                    break;
                case 3:
                    en4death1.setImage(skull1img);
                    en4death2.setImage(skull1img);
                    en4death3.setImage(skull1img);
                    break;
                case 4:
                    en4death1.setImage(skull1img);
                    en4death2.setImage(skull1img);
                    en4death3.setImage(skull1img);
                    en4death4.setImage(skull1img);
                    break;
                case 5:
                    en4death1.setImage(skull1img);
                    en4death2.setImage(skull1img);
                    en4death3.setImage(skull1img);
                    en4death4.setImage(skull1img);
                    en4death5.setImage(skull1img);
                    break;
                case 6:
                    en4death1.setImage(skull1img);
                    en4death2.setImage(skull1img);
                    en4death3.setImage(skull1img);
                    en4death4.setImage(skull1img);
                    en4death5.setImage(skull1img);
                    en4death6.setImage(skull1img);
                    break;
            }
        }
    }

    /**
     * This method changes the images of the playerboards when they are in frenzy mode
     * @param players represents the players in the game
     */
    private static void switchBoards(List<SimplePlayer> players){
        for(int i=0; i<players.size(); i++){
            if(players.get(i).isSwitched()) {
                System.out.println(i);
                System.out.println(myTurn);
                //mettere debug qui
                plBHashMap.get(i+1).setImage(null);
                plBHashMap.get(i+1).setImage(plBImageHashMap.get(i+11));
            }else {
                plBHashMap.get(i+1).setImage(plBImageHashMap.get(i+6));
            }
        }


    }

    /**
     * This method updates what the player can see of another player
     * @param player represents the player whose visibility is updated
     */
    private static void updatePlayerVisibility(SimplePlayer player){
        int numEn=0;
        for(int i=0; i<numbEnemyNickname.size(); i++){
            if(numbEnemyNickname.get(i+1).equalsIgnoreCase(player.getNickname())) numEn=i+1;
        }

        config(numEn, player);


    }

    /**
     * This method update the playerboard of a player
     * @param player represents the player whose playerboard is updated
     */
    private static void updatePlayerBoard(SimplePlayer player){
        /*int enemyNumb=0;
        for(int i=0; i<numbEnemyNickname.size(); i++){
            if(player.getNickname().equalsIgnoreCase(numbEnemyNickname.get(i+1))){
                enemyNumb=i+1;
            }
        }*/
        updateMarks(player.getMarks(), player.getNickname());
        updateDamages(player.getDamages(), nicknameTurnHashMap.get(player.getNickname()));
        if(numbEnemyNickname.get(1).equalsIgnoreCase(player.getNickname())) {
            updateDeathCounter1(player);
        }
        if(numPlayers==3){
            if(numbEnemyNickname.get(1).equalsIgnoreCase(player.getNickname())) updateDeathCounter1(player);
            if(numbEnemyNickname.get(2).equalsIgnoreCase(player.getNickname())) updateDeathCounter2(player);
        }
        if(numPlayers==4){
            if(numbEnemyNickname.get(1).equalsIgnoreCase(player.getNickname())) updateDeathCounter1(player);
            if(numbEnemyNickname.get(2).equalsIgnoreCase(player.getNickname())) updateDeathCounter2(player);
            if(numbEnemyNickname.get(3).equalsIgnoreCase(player.getNickname())) updateDeathCounter3(player);
        }
        if(numPlayers==5){
            if(numbEnemyNickname.get(1).equalsIgnoreCase(player.getNickname())) updateDeathCounter1(player);
            if(numbEnemyNickname.get(2).equalsIgnoreCase(player.getNickname())) updateDeathCounter2(player);
            if(numbEnemyNickname.get(3).equalsIgnoreCase(player.getNickname())) updateDeathCounter3(player);
            if(numbEnemyNickname.get(4).equalsIgnoreCase(player.getNickname())) updateDeathCounter4(player);
        }

    }


    /**
     * This method deletes an ammo tile from the board
     * @param grabbedTile represents the ammo tile to delete
     */
    private static void deleteTile(AmmoTile grabbedTile){
        //finire
        boardTileImgVHashMap.get(grabbedTile.getId()).setImage(null);
    }

    /**
     * This method deletes a weapon from the board
     * @param weapon represents the weapon to delete from the board
     */
    private static void deleteWeapon(Card weapon){
        //finire
        boardWeapImVHashMap.get(idWeaponPosition.get(weapon.getId())).setImage(null);

    }


    /**
     * This method updates the game board
     * @param gameBoard represents the game board
     */
    private static void updateBoard(SimpleBoard gameBoard){
        idWeaponPosition.clear();
        boardTileImgVHashMap.clear();
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(squareMatrix[i][j]!=null&&gameBoard.getBoard()[i][j]!=null) {
                    if (!gameBoard.getBoard()[i][j].isSpawnPoint()) {
                        if (gameBoard.getBoard()[i][j].getAmmoTile() != null) {
                            String file = convertIdImg(gameBoard.getBoard()[i][j].getAmmoTile().getId());
                            if (squareMatrix[i][j] != null) {
                                Image amm=new Image(file);
                                squareMatrix[i][j].getAmmo().setImage(amm);
                            }
                            boardTileImgVHashMap.put(gameBoard.getBoard()[i][j].getAmmoTile().getId(), squareMatrix[i][j].getAmmo());
                        } /*else {
                            squareMatrix[i][j].getAmmo().setStyle(null);
                            boardTileButtonHashMap.remove(gameBoard.getBoard()[i][j].getAmmoTile().getId());
                        }*/
                    } else {
                    /*String w1=convertIdImg(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId());
                    String w2=convertIdImg(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId());
                    String w3=convertIdImg(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId());
                    squareMatrix[i][j].getWeapon1().setStyle("-fx-image: w1");
                    squareMatrix[i][j].getWeapon2().setStyle("-fx-image: w2");
                    squareMatrix[i][j].getWeapon3().setStyle("-fx-image: w3");*/
                        if (i == 0 && j == 2) {
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>0) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId(), 1);
                                boardWeapImVHashMap.get(1).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId()));
                            }else{
                                //devo rimuovere da idWeaponPosition?
                                boardWeapImVHashMap.get(1).setImage(null);
                                boardWeapImVHashMap.get(2).setImage(null);
                                boardWeapImVHashMap.get(3).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>1) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId(), 2);
                                boardWeapImVHashMap.get(2).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId()));

                            }else{
                                boardWeapImVHashMap.get(2).setImage(null);
                                boardWeapImVHashMap.get(3).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>2) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId(), 3);
                                boardWeapImVHashMap.get(3).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId()));

                            }else{
                                boardWeapImVHashMap.get(3).setImage(null);
                            }
                        }
                        if (i == 1 && j == 0) {
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>0) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId(), 4);
                                boardWeapImVHashMap.get(4).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId()));
                            }else{
                                boardWeapImVHashMap.get(4).setImage(null);
                                boardWeapImVHashMap.get(5).setImage(null);
                                boardWeapImVHashMap.get(6).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>1) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId(), 5);
                                boardWeapImVHashMap.get(5).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId()));

                            }else{
                                boardWeapImVHashMap.get(5).setImage(null);
                                boardWeapImVHashMap.get(6).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>2) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId(), 6);
                                boardWeapImVHashMap.get(6).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId()));

                            }else{
                                boardWeapImVHashMap.get(6).setImage(null);
                            }
                        }
                        if (i == 2 && j == 3) {
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>0) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId(), 7);
                                boardWeapImVHashMap.get(7).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(0).getId()));
                            }else{
                                boardWeapImVHashMap.get(7).setImage(null);
                                boardWeapImVHashMap.get(8).setImage(null);
                                boardWeapImVHashMap.get(9).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>1) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId(), 8);
                                boardWeapImVHashMap.get(8).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(1).getId()));

                            }else{
                                boardWeapImVHashMap.get(8).setImage(null);
                                boardWeapImVHashMap.get(9).setImage(null);
                            }
                            if (gameBoard.getBoard()[i][j].getWeaponCards().size()>2) {
                                idWeaponPosition.put(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId(), 9);
                                boardWeapImVHashMap.get(9).setImage(weaponsHashMap.get(gameBoard.getBoard()[i][j].getWeaponCards().get(2).getId()));

                            }else{
                                boardWeapImVHashMap.get(9).setImage(null);
                            }
                        }
                    }
                }
            }
        }

        for(int i=0; i<gameBoard.getKillshotTrack().size(); i++){
            Image img=damagesHashMap.get(gameBoard.getKillshotTrack().get(i));
            killshotTrack.get(i).setImage(img);
        }
        for(int i=0; i<gameBoard.getKillshotTrack().size(); i++){
            if(gameBoard.getOverkillTrack().get(i)){
                overkillTrack.get(i).setText("2");
            }
        }

    }

    /**
     * This method updates the playerboards of all the players
     * @param players represents the list of all players
     * @param frenzy is true if the game is in frenzy mode
     */
    private static void updatePlayerBoards(List<SimplePlayer> players, boolean frenzy){

        if(frenzy) switchBoards(players);
        for(int i=0; i<players.size(); i++){
            if(i!=myTurn-1) {

                if(players.get(i).getPosition()!=null) setPosition(players.get(i).getPosition(), nicknameTurnHashMap.get(players.get(i).getNickname()));
                updateMarks(players.get(i).getMarks(), players.get(i).getNickname() );
                updateDamages(players.get(i).getDamages(), i+1);
                updateUnloadedWeapons(players.get(i), players.get(i).getNotLoadedIds(), i);
                if(numbEnemyNickname.get(1).equalsIgnoreCase(players.get(i).getNickname())) {
                    updateDeathCounter1(players.get(i));
                }
                if(numPlayers==3){
                    if(numbEnemyNickname.get(1).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter1(players.get(i));
                    if(numbEnemyNickname.get(2).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter2(players.get(i));
                }
                if(numPlayers==4){
                    if(numbEnemyNickname.get(1).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter1(players.get(i));
                    if(numbEnemyNickname.get(2).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter2(players.get(i));
                    if(numbEnemyNickname.get(3).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter3(players.get(i));
                }
                if(numPlayers==5){
                    if(numbEnemyNickname.get(1).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter1(players.get(i));
                    if(numbEnemyNickname.get(2).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter2(players.get(i));
                    if(numbEnemyNickname.get(3).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter3(players.get(i));
                    if(numbEnemyNickname.get(4).equalsIgnoreCase(players.get(i).getNickname())) updateDeathCounter4(players.get(i));
                }

            }
        }
        if(players.get(myTurn-1).getPosition()!=null) setMyPosition(players.get(myTurn-1).getPosition());
        setMyMarks(players.get(myTurn-1).getMarks());
        setMyDamages(players.get(myTurn-1).getDamages());
        setMyWeapons(players.get(myTurn-1).getWeaponCards());
        setMyUnloadedWeapons(players.get(myTurn-1).getNotLoadedIds());
        setMyPowerups(players.get(myTurn-1).getPowerupCards());
        setMyDeathCounter(players.get(myTurn-1));


    }

    private static void setButtonAction(Button tempButt, int x, int y){
        tempButt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                disableAllSquareButtons();
                int[] pos=new int[]{x, y};
                mess.setText("Hai selezionato\n il quadrato\n in cui spostarti");
                connection.runAction(pos);
            }
        });
    }

    private static void setButtonActionWithTarget(Button tempButt, int x, int y, String target){
        tempButt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                disableAllSquareButtons();
                int[] pos=new int[]{x, y};
                mess.setText("Hai selezionato il\n quadrato in cui spostare\n il giocatore");
                connection.movePlayer(target, pos);
            }
        });
    }



    private static void initDamageImVHashMap(){
        damageImageViewHashMap.put(1, osdr);
        damageImageViewHashMap.put(2, ssdr);
        damageImageViewHashMap.put(3, tsdr);
        damageImageViewHashMap.put(4, fsdr);
        damageImageViewHashMap.put(5, fisdr);
        damageImageViewHashMap.put(6, sisdr);
        damageImageViewHashMap.put(7, sesdr);
        damageImageViewHashMap.put(8, esdr);
        damageImageViewHashMap.put(9, nsdr);
        damageImageViewHashMap.put(10, tesdr);
        damageImageViewHashMap.put(11, elsdr);
        damageImageViewHashMap.put(12, twsdr);
        damageImageViewHashMap.put(13, os2dr);
        damageImageViewHashMap.put(14, ss2dr);
        damageImageViewHashMap.put(15, ts2dr);
        damageImageViewHashMap.put(16, fs2dr);
        damageImageViewHashMap.put(17, fis2dr);
        damageImageViewHashMap.put(18, sis2dr);
        damageImageViewHashMap.put(19, ses2dr);
        damageImageViewHashMap.put(20, es2dr);
        damageImageViewHashMap.put(21, ns2dr);
        damageImageViewHashMap.put(22, tes2dr);
        damageImageViewHashMap.put(23, els2dr);
        damageImageViewHashMap.put(24, tws2dr);
        damageImageViewHashMap.put(25, o3sdr);
        damageImageViewHashMap.put(26, s3sdr);
        damageImageViewHashMap.put(27, t3sdr);
        damageImageViewHashMap.put(28, f3sdr);
        damageImageViewHashMap.put(29, fi3sdr);
        damageImageViewHashMap.put(30, si3sdr);
        damageImageViewHashMap.put(31, se3sdr);
        damageImageViewHashMap.put(32, e3sdr);
        damageImageViewHashMap.put(33, n3sdr);
        damageImageViewHashMap.put(34, te3sdr);
        damageImageViewHashMap.put(35, el3sdr);
        damageImageViewHashMap.put(36, tw3sdr);
        damageImageViewHashMap.put(37, o4sdr);
        damageImageViewHashMap.put(38, s4sdr);
        damageImageViewHashMap.put(39, t4sdr);
        damageImageViewHashMap.put(40, f4sdr);
        damageImageViewHashMap.put(41, fi4sdr);
        damageImageViewHashMap.put(42, si4sdr);
        damageImageViewHashMap.put(43, se4sdr);
        damageImageViewHashMap.put(44, e4sdr);
        damageImageViewHashMap.put(45, n4sdr);
        damageImageViewHashMap.put(46, te4sdr);
        damageImageViewHashMap.put(47, el4sdr);
        damageImageViewHashMap.put(48, tw4sdr);
    }

    private static void initMyDamagesImVHashMap(){
        myDamagesImageViewHashMap.put(1, firstdr);
        myDamagesImageViewHashMap.put(2, secdr);
        myDamagesImageViewHashMap.put(3, thirddr);
        myDamagesImageViewHashMap.put(4, fdr);
        myDamagesImageViewHashMap.put(5, fidr);
        myDamagesImageViewHashMap.put(6, sdr);
        myDamagesImageViewHashMap.put(7, sedr);
        myDamagesImageViewHashMap.put(8, edr);
        myDamagesImageViewHashMap.put(9, ndr);
        myDamagesImageViewHashMap.put(10, tdr);
        myDamagesImageViewHashMap.put(11, eldr);
        myDamagesImageViewHashMap.put(12, twdr);

    }

    private static void initMyMarksLabelHashMap(){
        myMarksLabelHashMap.put(1, nfmarkdr);
        myMarksLabelHashMap.put(2, nsmarkdr);
        myMarksLabelHashMap.put(3, ntmarkdr);
        myMarksLabelHashMap.put(4, nfomarkdr);
        myMarksLabelHashMap.put(5, nfimarkdr);
    }

    private static void initMarkLabelHashMap(){
        marksLabelHashMap.put(1, nfmarkdr2);
        marksLabelHashMap.put(2, nsmarkdr2);
        marksLabelHashMap.put(3, ntmarkdr2);
        marksLabelHashMap.put(4, nfomarkdr2);
        marksLabelHashMap.put(5, nfimarkdr2);
        marksLabelHashMap.put(6, nfmarkdr3);
        marksLabelHashMap.put(7, nsmarkdr3);
        marksLabelHashMap.put(8, ntmarkdr3);
        marksLabelHashMap.put(9, nfomarkdr3);
        marksLabelHashMap.put(10, nfimarkdr3);
        marksLabelHashMap.put(11, nfmarkdr4);
        marksLabelHashMap.put(12, nsmarkdr4);
        marksLabelHashMap.put(13, ntmarkdr4);
        marksLabelHashMap.put(14, nfomarkdr4);
        marksLabelHashMap.put(15, nfimarkdr4);
        marksLabelHashMap.put(16, nfmarkdr5);
        marksLabelHashMap.put(17, nsmarkdr5);
        marksLabelHashMap.put(18, ntmarkdr5);
        marksLabelHashMap.put(19, nfomarkdr5);
        marksLabelHashMap.put(20, nfimarkdr5);
    }

    private static void initPlBImageHashMap(){
        plBImageHashMap.put(1, dstructorPlBImage);
        plBImageHashMap.put(2, dozerPlBImage);
        plBImageHashMap.put(3, bansheePlBImage);
        plBImageHashMap.put(4, sprogPlBImage);
        plBImageHashMap.put(5, violetPlBImage);
        plBImageHashMap.put(6, dstructorSemiFrPlBImage);
        plBImageHashMap.put(7, dozerSemiFrPlBImage);
        plBImageHashMap.put(8, bansheeSemiFrPlBImage);
        plBImageHashMap.put(9, sprogSemiFrPlBImage);
        plBImageHashMap.put(10, violetSemiFrPlBImage);
        plBImageHashMap.put(11, dstructorFrenzyPlBImage);
        plBImageHashMap.put(12, dozerFrenzyPlBImage);
        plBImageHashMap.put(13, bansheeFrenzyPlBImage);
        plBImageHashMap.put(14, sprogFrenzyPlBImage);
        plBImageHashMap.put(15, violetFrenzyPlBImage);

    }

    private static void initMyPowerupsImVHashMap(){
        myPowerupsHashMap.put(1, pu1);
        myPowerupsHashMap.put(2, pu2);
        myPowerupsHashMap.put(3, pu3);
        myPowerupsHashMap.put(4, pu4);

    }

    private static void initMyWeaponHashMap(){
        myWeaponHashMap.put(1, wp1);
        myWeaponHashMap.put(2, wp2);
        myWeaponHashMap.put(3, wp3);
    }

    private static void initBoardWeapImVHashMap(){
        boardWeapImVHashMap.put(1, wa1);
        boardWeapImVHashMap.put(2, wa2);
        boardWeapImVHashMap.put(3, wa3);
        boardWeapImVHashMap.put(4, wa4);
        boardWeapImVHashMap.put(5, wa5);
        boardWeapImVHashMap.put(6, wa6);
        boardWeapImVHashMap.put(7, wa7);
        boardWeapImVHashMap.put(8, wa8);
        boardWeapImVHashMap.put(9, wa9);

    }

    private static void initUserButtonHashMap(){
        userButtonHashMap.put(1, user1);
        userButtonHashMap.put(2, user2);
        userButtonHashMap.put(3, user3);
        userButtonHashMap.put(4, user4);
        userButtonHashMap.put(5, user5);

    }

    private static void initMyWeaponsLabel(){
        myWeaponsLabel.put(1, myWeap1Label);
        myWeaponsLabel.put(2, myWeap2Label);
        myWeaponsLabel.put(3, myWeap3Label);

    }

    /**
     * This method initializes the board weapon buttons hashmap (Integer-Button)
     */
    private static void initBoardWeaponsButton(){
        boardWeaponsButton.put(1, a1);
        boardWeaponsButton.put(2, a2);
        boardWeaponsButton.put(3, a3);
        boardWeaponsButton.put(4, a4);
        boardWeaponsButton.put(5, a5);
        boardWeaponsButton.put(6, a6);
        boardWeaponsButton.put(7, a7);
        boardWeaponsButton.put(8, a8);
        boardWeaponsButton.put(9, a9);


    }

    /**
     * This method initializes the weapon buttons hashmap (Integer-Button)
     */
    private static void initMyWeaponsButton(){
        myWeaponsButton.put(1, myw1);
        myWeaponsButton.put(2, myw2);
        myWeaponsButton.put(3, myw3);

    }

    /**
     * This method initializes the powerup buttons hashmap (Integer-Button)
     */
    private static void initMyPowerupsButton(){
        myPowerupsButton.put(1, myp1);
        myPowerupsButton.put(2, myp2);
        myPowerupsButton.put(3, myp3);
        myPowerupsButton.put(4, myp4);

    }

    /**
     * This method initializes the info windows hashmap (String-AnchorPane)
     */
    private static void initInfoWindowPlayerHashMap(){
        infoWindowPlayer.put(numbEnemyNickname.get(1), infostage1);
        infoWindowPlayer.put(numbEnemyNickname.get(2), infostage2);
        infoWindowPlayer.put(numbEnemyNickname.get(3), infostage3);
        infoWindowPlayer.put(numbEnemyNickname.get(4), infostage4);
    }

    /**
     * This method initializes the weapons hashmap (String-Image)
     */
    private static void initWeaponsHashMap(){
        weaponsHashMap.put("15", im1);
        weaponsHashMap.put("16", im2);
        weaponsHashMap.put("17", im3);
        weaponsHashMap.put("18", im4);
        weaponsHashMap.put("19", im5);
        weaponsHashMap.put("20", im6);
        weaponsHashMap.put("21", im7);
        weaponsHashMap.put("22", im8);
        weaponsHashMap.put("23", im9);
        weaponsHashMap.put("24", im10);
        weaponsHashMap.put("25", im11);
        weaponsHashMap.put("26", im12);
        weaponsHashMap.put("27", im13);
        weaponsHashMap.put("28", im14);
        weaponsHashMap.put("29", im15);
        weaponsHashMap.put("30", im16);
        weaponsHashMap.put("31", im17);
        weaponsHashMap.put("32", im18);
        weaponsHashMap.put("33", im19);
        weaponsHashMap.put("34", im20);
        weaponsHashMap.put("35", im21);


    }

    /**
     * This method initializes the powerup hashmap (String-Image)
     */
    private static void initPowerupsHashMap(){
        powerupsHashMap.put("022", impow1);
        powerupsHashMap.put("023", impow2);
        powerupsHashMap.put("024", impow3);
        powerupsHashMap.put("025", impow4);
        powerupsHashMap.put("026", impow5);
        powerupsHashMap.put("027", impow6);
        powerupsHashMap.put("028", impow7);
        powerupsHashMap.put("029", impow8);
        powerupsHashMap.put("0210", impow9);
        powerupsHashMap.put("0211", impow10);
        powerupsHashMap.put("0212", impow11);
        powerupsHashMap.put("0213", impow12);
    }




    /**
     * This method sets the function of the button 1
     * @param selectablePl represents the list of lists of selected players
     * @param target repreents the target of the effect
     */
    private static void set1Function(List<List<String>> selectablePl, SimpleTarget target){
        eff1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                countTarget++;
                eff1.setDisable(true);
                if(countTarget==target.getMaxNumber()){
                    eff2.setDisable(true);
                    eff3.setDisable(true);
                    eff4.setDisable(true);
                    okay.setDisable(false);
                }else{
                    if(countTarget>=target.getMinNumber()){
                        okay.setDisable(false);
                    }
                }
                tempPlSelected.add(selectablePl.get(0));
            }
        });
    }

    /**
     * This method sets the function of the button 2
     * @param selectablePl represents the list of lists of selected players
     * @param target repreents the target of the effect
     */
    private static void set2Function(List<List<String>> selectablePl, SimpleTarget target){
        eff2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                countTarget++;
                eff2.setDisable(true);
                if(countTarget==target.getMaxNumber()){
                    eff1.setDisable(true);
                    eff3.setDisable(true);
                    eff4.setDisable(true);
                    okay.setDisable(false);
                }else{
                    if(countTarget>=target.getMinNumber()){
                        okay.setDisable(false);
                    }
                }
                tempPlSelected.add(selectablePl.get(1));
            }
        });
    }

    /**
     * This method sets the function of the button 3
     * @param selectablePl represents the list of lists of selected players
     * @param target repreents the target of the effect
     */
    private static void set3Function(List<List<String>> selectablePl, SimpleTarget target){
        eff3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                countTarget++;
                eff3.setDisable(true);
                if(countTarget==target.getMaxNumber()){
                    eff1.setDisable(true);
                    eff2.setDisable(true);
                    eff4.setDisable(true);
                    okay.setDisable(false);
                }else{
                    if(countTarget>=target.getMinNumber()){
                        okay.setDisable(false);
                    }
                }
                tempPlSelected.add(selectablePl.get(2));
            }
        });
    }

    /**
     * This method sets the function of the button 4
     * @param selectablePl represents the list of lists of selected players
     * @param target repreents the target of the effect
     */
    private static void set4Function(List<List<String>> selectablePl, SimpleTarget target){
        eff4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                countTarget++;
                eff4.setDisable(true);
                if(countTarget==target.getMaxNumber()){
                    eff1.setDisable(true);
                    eff2.setDisable(true);
                    eff3.setDisable(true);
                    okay.setDisable(false);
                }else{
                    if(countTarget>=target.getMinNumber()){
                        okay.setDisable(false);
                    }
                }
                tempPlSelected.add(selectablePl.get(3));
            }
        });
    }

    /**
     * This method sets the function of the button okay (ok)
     * @param target represents the target of the effect
     */
    private static void setOkayFunction(SimpleTarget target){
        //mess.setText("Seleziona i giocatori che vuoi colpire");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(target.getMaxPlayerIn()==-1){
                    ok.setDisable(true);
                    user1.setDisable(true);
                    user1.setOnAction(null);
                    user2.setDisable(true);
                    user2.setOnAction(null);
                    user3.setDisable(true);
                    user3.setOnAction(null);
                    user4.setDisable(true);
                    user4.setOnAction(null);
                    user5.setDisable(true);
                    user5.setOnAction(null);
                    okay.setDisable(true);
                    minNumberList1=false;
                    minNumberList2=false;
                    minNumberList3=false;
                    minNumberList4=false;
                    plSelected=tempPlSelected;
                    connection.selectPlayers(plSelected);
                    countTarget=0;
                    countPlayerList1=0;
                    countPlayerList2=0;
                    countPlayerList3=0;
                    countPlayerList4=0;
                    list0.clear();
                    list1.clear();
                    list2.clear();
                    list3.clear();
                    plSelected.clear();
                    tempPlSelected.clear();
                }else {
                    user1.setDisable(true);
                    user2.setDisable(true);
                    user3.setDisable(true);
                    user4.setDisable(true);
                    user5.setDisable(true);
                    okay.setDisable(true);
                    mess.setText("Scegli chi colpire\n e premi Invia");
                    for (int i = 0; i < tempPlSelected.size(); i++) {
                        for (int j = 0; j < tempPlSelected.get(i).size(); j++) {
                            if (nicknameTurnHashMap.get(tempPlSelected.get(i).get(j)) == 1) {
                                setActionUserButton(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), dstructor);
                            }
                            if (nicknameTurnHashMap.get(tempPlSelected.get(i).get(j)) == 2) {
                                setActionUserButton(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), dozer);
                            }
                            if (nicknameTurnHashMap.get(tempPlSelected.get(i).get(j)) == 3) {
                                setActionUserButton(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), banshee);
                            }
                            if (nicknameTurnHashMap.get(tempPlSelected.get(i).get(j)) == 4) {
                                setActionUserButton(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), sprog);
                            }
                            if (nicknameTurnHashMap.get(tempPlSelected.get(i).get(j)) == 5) {
                                setActionUserButton(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), violet);
                            }
                            userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))).setDisable(false);
                            setUsFunction(userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(i).get(j))), tempPlSelected.get(i).get(j), target);
                        }
                    }
                }

            }
        });
    }

    /**
     * This method sets the function of the users' buttons
     * @param usButton represents the user button
     * @param nick represents the nickname
     * @param target represents the target
     */
    private static void setUsFunction(Button usButton, String nick, SimpleTarget target){
        usButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                usButton.setDisable(true);
                for(int i=0; i<tempPlSelected.size(); i++){
                    for(int j=0; j<tempPlSelected.get(i).size(); j++){
                        if(tempPlSelected.get(i).get(j).equalsIgnoreCase(nick)){
                            switch(i){
                                case 0:
                                    countPlayerList1++;
                                    list0.add(nick);
                                    break;
                                case 1:
                                    countPlayerList2++;
                                    list1.add(nick);
                                    break;
                                case 2:
                                    countPlayerList3++;
                                    list2.add(nick);
                                    break;
                                case 3:
                                    countPlayerList4++;
                                    list3.add(nick);
                                    break;
                            }
                        }
                    }
                }
                if(target.getMaxPlayerIn()!=-1&&countPlayerList1>=target.getMaxPlayerIn()){
                    for(int x=0; x<tempPlSelected.get(0).size(); x++){
                        userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(0).get(x))).setDisable(true);
                    }
                }
                if(target.getMaxPlayerIn()!=-1&&countPlayerList2>=target.getMaxPlayerIn()){
                    for(int x=0; x<tempPlSelected.get(1).size(); x++){
                        userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(1).get(x))).setDisable(true);
                    }
                }
                if(target.getMaxPlayerIn()!=-1&&countPlayerList3>=target.getMaxPlayerIn()){
                    for(int x=0; x<tempPlSelected.get(2).size(); x++){
                        userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(2).get(x))).setDisable(true);
                    }
                }
                if(target.getMaxPlayerIn()!=-1&&countPlayerList4>=target.getMaxPlayerIn()){
                    for(int x=0; x<tempPlSelected.get(3).size(); x++){
                        userButtonHashMap.get(nicknameTurnHashMap.get(tempPlSelected.get(3).get(x))).setDisable(true);
                    }
                }

                if(countPlayerList1>=target.getMinPlayerIn()) minNumberList1=true;
                if(countPlayerList2>=target.getMinPlayerIn()) minNumberList2=true;
                if(countPlayerList3>=target.getMinPlayerIn()) minNumberList3=true;
                if(countPlayerList4>=target.getMinPlayerIn()) minNumberList4=true;

                if(tempPlSelected.size()==1){
                    if(minNumberList1){
                        setOkFunction();
                    }
                }
                if(tempPlSelected.size()==2){
                    if(minNumberList1&&minNumberList2){
                        setOkFunction();
                    }
                }
                if(tempPlSelected.size()==3){
                    if(minNumberList1&&minNumberList2&&minNumberList3){
                        setOkFunction();
                    }
                }
                if(tempPlSelected.size()==4){
                    if(minNumberList1&&minNumberList2&&minNumberList3&&minNumberList4){
                        setOkFunction();
                    }
                }

            }
        });
    }

    /**
     * This method sets the function of the button ok (Invia)
     */
    private static void setOkFunction(){
        ok.setDisable(false);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                plSelected.clear();
                ok.setDisable(true);
                user1.setDisable(true);
                user1.setOnAction(null);
                user2.setDisable(true);
                user2.setOnAction(null);
                user3.setDisable(true);
                user3.setOnAction(null);
                user4.setDisable(true);
                user4.setOnAction(null);
                user5.setDisable(true);
                user5.setOnAction(null);
                minNumberList1=false;
                minNumberList2=false;
                minNumberList3=false;
                minNumberList4=false;
                if(!list0.isEmpty()) plSelected.add(list0);
                if(!list1.isEmpty()) plSelected.add(list1);
                if(!list2.isEmpty()) plSelected.add(list2);
                if(!list3.isEmpty()) plSelected.add(list3);
                connection.selectPlayers(plSelected);
                countTarget=0;
                countPlayerList1=0;
                countPlayerList2=0;
                countPlayerList3=0;
                countPlayerList4=0;
                list0.clear();
                list1.clear();
                list2.clear();
                list3.clear();
                plSelected.clear();
                tempPlSelected.clear();

            }
        });
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

    /**
     * This method is called when a player joins the waiting room of the player
     * @param nickname represents the nickname of the player who joined the room
     */
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


    /**
     * This method is called when a player exits from the waiting room of the player
     * @param nickname represents the nickname of the player who exited from the waiting room
     */
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

    /**
     * This method is called when the player is the first in his waiting room
     */
    public static void onFirstInRoomAdvise() {
        /*
        LoginWindow.plOnline.setText("You are the first player.");
         */
        if(WaitingRoomWindow.getArea()!=null){
            WaitingRoomWindow.getArea().appendText("You are the first in the room!\n");
        }
    }


    /**
     * This method is called at the creation of the match or when the player returns in game
     * @param players represents the players in the match
     * @param playerTurnNumber represents the turn of the player
     */
    public static void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {

        numPlayers=players.size();
        myTurn=playerTurnNumber;

        imDropBlue=new Image("/gui/bluedrop.png");
        imDropGreen= new Image("/gui/greendrop.png");
        imDropGrey=new Image("/gui/greydrop.png");
        imDropYellow=new Image("/gui/yellowdrop.png");
        imDropPurple= new Image("/gui/purpledrop.png");


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

        mess = new Label();
        mess.setStyle("-fx-font-size: 15");
        //mess.getStyleClass().add("mess");
        setPosLabel(mess, 1020, 50);
        mess.setPrefWidth(180 * widthScaleFactor);
        mess.setPrefWidth(200 * heightScaleFactor);
        mess.setTextAlignment(TextAlignment.CENTER);

        myPowerup1Label = new Label();
        setPosLabel(myPowerup1Label, 640, 380);

        myPowerup2Label = new Label();
        setPosLabel(myPowerup2Label, 730, 380);
        myPowerup3Label = new Label();
        setPosLabel(myPowerup3Label, 820, 380);
        myPowerup4Label = new Label();
        setPosLabel(myPowerup4Label, 910, 380);





        plBhashmap.put(0, "/gui/playerBoard.png");
        plBhashmap.put(1, "/gui/pl1.png");
        plBhashmap.put(2, "/gui/pl2.png");
        plBhashmap.put(3, "/gui/pl3.png");
        plBhashmap.put(4, "/gui/pl4.png");

        dstructorPlBImage=new Image("/gui/playerBoard.png");
        dozerPlBImage=new Image("/gui/pl1.png");
        bansheePlBImage= new Image("/gui/pl2.png");
        sprogPlBImage= new Image("/gui/pl3.png");
        violetPlBImage=new Image("/gui/pl4.png");
        dstructorFrenzyPlBImage=new Image("/gui/playerBoardfr.png");
        dozerFrenzyPlBImage=new Image("/gui/pl1fr.png");
        bansheeFrenzyPlBImage=new Image("/gui/pl2fr.png");
        sprogFrenzyPlBImage=new Image("/gui/pl3fr.png");
        violetFrenzyPlBImage=new Image("/gui/pl4fr.png");
        dstructorSemiFrPlBImage=new Image("/gui/playerBoardSemiFr.png");
        dozerSemiFrPlBImage=new Image("/gui/pl1semifr.png");
        bansheeSemiFrPlBImage=new Image("/gui/pl2semifr.png");
        sprogSemiFrPlBImage=new Image("/gui/pl3semifr.png");
        violetSemiFrPlBImage=new Image("/gui/pl4semifr.png");


        //settare le Image im1,.. im21
        im1=new Image("/gui/15.png");
        im2=new Image("/gui/16.png");
        im3=new Image("/gui/17.png");
        im4=new Image("/gui/18.png");
        im5=new Image("/gui/19.png");
        im6=new Image("/gui/20.png");
        im7=new Image("/gui/21.png");
        im8=new Image("/gui/22.png");
        im9=new Image("/gui/23.png");
        im10=new Image("/gui/24.png");
        im11=new Image("/gui/25.png");
        im12=new Image("/gui/26.png");
        im13=new Image("/gui/27.png");
        im14=new Image("/gui/28.png");
        im15=new Image("/gui/29.png");
        im16=new Image("/gui/30.png");
        im17=new Image("/gui/31.png");
        im18=new Image("/gui/32.png");
        im19=new Image("/gui/33.png");
        im20=new Image("/gui/34.png");
        im21=new Image("/gui/35.png");

        impow1=new Image("/gui/022.png");
        impow2=new Image("/gui/023.png");
        impow3=new Image("/gui/024.png");
        impow4=new Image("/gui/025.png");
        impow5=new Image("/gui/026.png");
        impow6=new Image("/gui/027.png");
        impow7=new Image("/gui/028.png");
        impow8=new Image("/gui/029.png");
        impow9=new Image("/gui/0210.png");
        impow10=new Image("/gui/0211.png");
        impow11=new Image("/gui/0212.png");
        impow12=new Image("/gui/0213.png");


        initDamageImVHashMap();
        initMyDamagesImVHashMap();
        initMyMarksLabelHashMap();
        initMarkLabelHashMap();
        initPlBImageHashMap();
        initMyPowerupsImVHashMap();
        initMyWeaponHashMap();
        initBoardWeapImVHashMap();
        //initUserButtonHashMap();
        //initMyWeaponsLabel();
        initBoardWeaponsButton();
        initMyWeaponsButton();
        initMyPowerupsButton();
        //initInfoWindowPlayerHashMap();
        initWeaponsHashMap();
        initPowerupsHashMap();



        //String myPlayB=plBhashmap.get(playerTurnNumber-1);
        //playerBoard= new Image(myPlayB);
        myTurn=playerTurnNumber;
        myNickname=players.get(myTurn-1).getNickname();
        for(int i=0; i<players.size(); i++){
            turnNicknameHashMap.put(i+1, players.get(i).getNickname() );
            nicknameTurnHashMap.put(players.get(i).getNickname(), i+1);
        }

        List<SimplePlayer> otherPlayers=new ArrayList<>();
        for(int i=0; i<players.size(); i++){
            if(i!=myTurn-1){
                otherPlayers.add(players.get(i));
            }
        }

        for(int i=0; i<otherPlayers.size(); i++){
            numbEnemyNickname.put(i+1, otherPlayers.get(i).getNickname());
        }

        if(players.size()==2) {
            pl2.setImage(null);
            pl3.setImage(null);
            pl4.setImage(null);
            damagesHashMap.put(players.get(0).getNickname(), imDropYellow);
            damagesHashMap.put(players.get(1).getNickname(), imDropGrey);
        }
        if(players.size()==3) {
            pl3.setImage(null);
            pl4.setImage(null);
            damagesHashMap.put(players.get(0).getNickname(), imDropYellow);
            damagesHashMap.put(players.get(1).getNickname(), imDropGrey);
            damagesHashMap.put(players.get(2).getNickname(), imDropBlue);
        }
        if(players.size()==4) {
            pl4.setImage(null);
            damagesHashMap.put(players.get(0).getNickname(), imDropYellow);
            damagesHashMap.put(players.get(1).getNickname(), imDropGrey);
            damagesHashMap.put(players.get(2).getNickname(), imDropBlue);
            damagesHashMap.put(players.get(3).getNickname(), imDropGreen);
        }
        if(players.size()==5) {
            damagesHashMap.put(players.get(0).getNickname(), imDropYellow);
            damagesHashMap.put(players.get(1).getNickname(), imDropGrey);
            damagesHashMap.put(players.get(2).getNickname(), imDropBlue);
            damagesHashMap.put(players.get(3).getNickname(), imDropGreen);
            damagesHashMap.put(players.get(4).getNickname(), imDropPurple);
        }

        System.out.println(myTurn);
        plBHashMap.put(myTurn, plB);
        switch (myTurn){
            case 1:
                plBHashMap.put(1, plB);
                plBHashMap.put(2, pl1);
                plBHashMap.put(3, pl2);
                plBHashMap.put(4, pl3);
                plBHashMap.put(5, pl4);
                break;
            case 2:
                plBHashMap.put(1, pl1);
                plBHashMap.put(2, plB);
                plBHashMap.put(3, pl2);
                plBHashMap.put(4, pl3);
                plBHashMap.put(5, pl4);
                break;
            case 3:
                plBHashMap.put(1, pl1);
                plBHashMap.put(2, pl2);
                plBHashMap.put(3, plB);
                plBHashMap.put(4, pl3);
                plBHashMap.put(5, pl4);
                break;
            case 4:
                plBHashMap.put(1, pl1);
                plBHashMap.put(2, pl2);
                plBHashMap.put(3, pl3);
                plBHashMap.put(4, plB);
                plBHashMap.put(5, pl4);
                break;
            case 5:
                plBHashMap.put(1, pl1);
                plBHashMap.put(2, pl2);
                plBHashMap.put(3, pl3);
                plBHashMap.put(4, pl4);
                plBHashMap.put(5, plB);
                break;
        }




        labPlayer1=new Label();
        labPlayer2=new Label();
        labPlayer3=new Label();
        labPlayer4=new Label();


        int cur=0;

        for(int i=1; i<=players.size(); i++){
            if(i!=playerTurnNumber){
                switch (cur){
                    case 0:
                        //pla1=new Image(plBhashmap.get(i));
                        labPlayer1.setText(players.get(i-1).getNickname());
                        labPlayer1.getStyleClass().add("labelnick");
                        labPlayer1.setLayoutX(655*widthScaleFactor);
                        labPlayer1.setLayoutY(25*heightScaleFactor);
                        break;
                    case 1:
                        //pla2=new Image(plBhashmap.get(i));
                        labPlayer2.setText(players.get(i-1).getNickname());
                        labPlayer2.getStyleClass().add("labelnick");
                        labPlayer2.setLayoutX(655*widthScaleFactor);
                        labPlayer2.setLayoutY(105*heightScaleFactor);
                        break;
                    case 2:
                        //pla2=new Image(plBhashmap.get(i));
                        labPlayer3.setText(players.get(i-1).getNickname());
                        labPlayer3.getStyleClass().add("labelnick");
                        labPlayer3.setLayoutX(655*widthScaleFactor);
                        labPlayer3.setLayoutY(185*heightScaleFactor);
                        break;
                    case 3:
                        //pla3=new Image(plBhashmap.get(i-1));
                        labPlayer4.setText(players.get(i-1).getNickname());
                        labPlayer3.getStyleClass().add("labelnick");
                        labPlayer4.setLayoutX(655*widthScaleFactor);
                        labPlayer4.setLayoutY(265*heightScaleFactor);
                        break;
                }
                cur++;
            }
        }







        Platform.setImplicitExit(false);
        Platform.runLater(()->{
            try {
                stage.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        if(!reconnection) {
            MapChoice.display(stage);
        }
    }

    /**
     * This method is called when the server receives an invalid message
     * @param msg represents the invalid message
     */
    public static void onInvalidMessageReceived(String msg) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText(msg);
            connection.selectAction("END");
        });

    }

    /**
     * This method is called to update the game board
     * @param gameBoard represents the game board
     */
    public static void onBoardUpdate(SimpleBoard gameBoard) {
        if(!init){
            Platform.setImplicitExit(false);
            Platform.runLater(()->{
                stage.close();
                initGameWindow(gameBoard.getSourceReference(), gameBoard.getSkullNumber());
                init=true;
                updateBoard(gameBoard);
            });
        }else{
            Platform.setImplicitExit(false);
            Platform.runLater(()-> {
                updateBoard(gameBoard);
            });
        }

    }

    /**
     * This method is called at the end of a turn to update the players and the board
     * @param players represents the players in the game
     * @param gameBoard represents the game board
     * @param frenzy is true if the game is in frenzy mode
     */
    public static void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            updateBoard(gameBoard);
            updatePlayerBoards(players, frenzy);
            setMyAmmo(players.get(myTurn-1));
            System.out.println("fine match update");
            //mess.setText("Fine update");
        });
    }

    /**
     * This method is called when a player has to respawn in his first turn after the login
     * @param powerups represents the discardable powerups to respawn
     */
    public static void onRespwanRequest(List<Card> powerups) {
        Platform.setImplicitExit(false);
        Platform.runLater(()->{
            setMyPowerups(powerups);
            mess.setText("Scarta un potenziamento\n per rigenerarti");
            for(int i=0; i<powerups.size(); i++) {
                String usPowerup=powerups.get(i).getId();
                setMyPowerupsActionForRespawn(usPowerup, powerups);
            }
        });



    }

    /**
     * This method is called when a player respawned
     * @param player represents the player who respawned
     * @param discardedPowerup represents the discarded powerup to respawn
     */
    public static void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Ti sei rigenerato");
                //System.out.println(player.getPowerupCards().get(0).getId());
                setMyPowerups(player.getPowerupCards());
                setMyPosition(player.getPosition());
                setMyDamages(player.getDamages());
                setMyMarks(player.getMarks());
                setMyDeathCounter(player);
            } else {
                mess.setText(player.getNickname() + "si è \nrigenerato");
                setPosition(player.getPosition(), nicknameTurnHashMap.get(player.getNickname()));
                updatePlayerBoard(player);
            }
        });

    }

    /**
     * This method is called when a player grabs an ammo tile
     * @param player represents the player who grabbed the tile
     * @param grabbedTile represents the tile grabbed
     */
    public static void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Hai raccolto \ndelle munizioni");
                setMyAmmo(player);
                setMyPowerups(player.getPowerupCards());
                deleteTile(grabbedTile);
            } else {
                mess.setText(player.getNickname() + " ha raccolto \ndelle munizioni");
                updatePlayerVisibility(player);
                deleteTile(grabbedTile);
            }
        });
    }

    /**
     * This method is called when a player grabbeda powerup
     * @param player represents the player who grabbed the powerup
     * @param powerup represents the powerup grabbed
     */
    public static void onGrabbedPowerup(SimplePlayer player, Card powerup) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Hai pescato \nun potenziamento");
                setMyPowerups(player.getPowerupCards());
            } else {
                mess.setText(player.getNickname() + " ha pescato \nun potenziamento");
            }
        });

    }

    /**
     * This method is called when the player chooses to grab a weapon
     * @param weapons represents the weapons that can be grabbed
     */
    public static void onGrabbableWeapons(List<Card> weapons) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Seleziona l'arma \nche vuoi raccogliere");
            for (int i = 0; i < weapons.size(); i++) {
                String grabbableWeapon = weapons.get(i).getId();
                setClickGrabWeapon(weapons, grabbableWeapon);
            }
        });

    }

    /**
     * This method is called when the player has to discard a weapon to grab another one
     * @param weapons represents the weapons of the player
     */
    public static void onDiscardWeapon(List<Card> weapons) {
        //forse è solo messaggio che uno ha scartato delle armi?
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText("Seleziona l'arma \nche vuoi scartare");
            for (int i = 0; i < weapons.size(); i++) {
                String discardableWeap = weapons.get(i).getId();
                setClickWeaponToDiscard(weapons, discardableWeap);
            }
        });

    }

    /**
     * This method is called when a weapon is grabbed
     * @param player represents the player who grabbed the weapon
     * @param weapon represents the grabbed weapon
     */
    public static void onGrabbedWeapon(SimplePlayer player, Card weapon) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Hai raccolto: \n" + weapon.getName());
                setMyAmmo(player);
                setMyWeapons(player.getWeaponCards());
                deleteWeapon(weapon);
            } else {
                mess.setText(player.getNickname() + "\n ha raccolto un'arma");
                updatePlayerVisibility(player);
                deleteWeapon(weapon);
            }
        });
    }

    /**
     * This method is called when a player reload a weapon
     * @param player represents the player who reloaded a weapon
     * @param weapon represents the weapon reloaded
     * @param discardedPowerups represents the discarded powerup if exists
     * @param totalCost represents the ammos used to reload the weapon
     */
    public static void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                myWeaponsLabel.get(myWeaponsPosition.get(weapon.getId())).setText("");
                setMyAmmo(player);
                setMyPowerups(player.getPowerupCards());
                setMyWeapons(player.getWeaponCards());
                setMyUnloadedWeapons(player.getNotLoadedIds());
            }
            mess.setText(player.getNickname() + "\n ha ricaricato un'arma");
            updatePlayerVisibility(player);
        });

    }

    /**
     * This methos is called when the player chooses to reload a weapon and sends to the server the chosen weapon
     * @param weapons represents the reloadable weapons
     */
    public static void onReloadableWeapons(List<Card> weapons) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText("Seleziona l'arma \n da ricaricare");
            for (int i = 0; i < weapons.size(); i++) {
                String reloadableWeap = weapons.get(i).getId();
                setClickWeaponToReload(weapons, reloadableWeap);
            }
        });

    }

    /**
     * This method is called to sets the possible actions that the player can do
     * @param actions represents the possible actions
     */
    public static void onTurnActions(List<String> actions) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            for (int i = 0; i < actions.size(); i++) {
                //quali nomi può avere questa stringa?
                if (actions.get(i).equalsIgnoreCase("shoot")) {
                    shoot.setDisable(false);
                }
                if (actions.get(i).equalsIgnoreCase("run")) {
                    move.setDisable(false);
                }
                if (actions.get(i).equalsIgnoreCase("grab")) {
                    grab.setDisable(false);
                }
                if (actions.get(i).equalsIgnoreCase("reload")) {
                    loadWeapon.setDisable(false);
                }
                if (actions.get(i).equalsIgnoreCase("powerup")) {
                    usePowerup.setDisable(false);
                }
                if (actions.get(i).equalsIgnoreCase("end")) {
                    end.setDisable(false);
                }
            }
        });

    }

    /**
     * This method is called at the end of a turn
     */
    public static void onTurnEnd() {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Fine Turno");
            connection.closeTurn();
        });

    }

    /**
     * This method is called when a player moved
     * @param player represents the player who moved
     */
    public static void onMoveAction(SimplePlayer player) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                setMyPosition(player.getPosition());
            } else {
                mess.setText(player.getNickname() + " si è mosso");
                setPosition(player.getPosition(), nicknameTurnHashMap.get(player.getNickname()));
            }
        });

    }


    /**
     * This method is called when the player has to move another player
     * @param matrix represents where the player can be moved
     * @param targetPlayer represents the player that has to be moved
     */
    public static void onMoveRequest(MatrixHelper matrix, String targetPlayer) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Scegli dove spostare \n il giocatore");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (matrix.toBooleanMatrix()[i][j]) {
                        squareMatrix[i][j].getSquareButton().setDisable(false);
                        squareMatrix[i][j].getSquareButton().setStyle("-fx-background-color: rgba(76,175,80,0.3)");
                        setButtonActionWithTarget(squareMatrix[i][j].getSquareButton(), i, j, targetPlayer);
                    }
                }
            }
        });


    }


    /**
     * This method is called when a player has been marked
     * @param player represents the player who marked
     * @param selected represents the player who has been marked
     * @param value represents the number of marks
     */
    public static void onMarkAction(String player, SimplePlayer selected, int value) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            if(selected.getNickname().equalsIgnoreCase(myNickname)){
                mess.setText("Hai ricevuto " +value+ "marchi da \n" + player);
                setMyMarks(selected.getMarks());
                setMyDamages(selected.getDamages());
            }else {
                if(player.equalsIgnoreCase(myNickname)){
                    mess.setText(value+ " marchi a \n" +selected.getNickname());
                }else {
                    mess.setText(player + " ha fatto \n" + value + " marchi a \n" + selected.getNickname());
                }
                updatePlayerBoard(selected);
            }
        });

    }


    /**
     * This method is called when a player receives some damages
     * @param player represents the player who damaged
     * @param selected represents the player who has been damaged
     * @param damageValue represents the number of damages
     * @param convertedMarks represents how many damages derive from marks
     */
    public static void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            if(selected.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Hai ricevuto " + damageValue + "danni da \n" + player);
                setMyMarks(selected.getMarks());
                setMyDamages(selected.getDamages());
                setMyDeathCounter(selected);
            }else {
                if(player.equalsIgnoreCase(myNickname)){
                    mess.setText(damageValue+ " danni a \n" +selected.getNickname());
                }else {
                    mess.setText(player + " ha fatto \n" + damageValue + " danni a \n" + selected.getNickname());
                }
                updatePlayerBoard(selected);
            }
        });
    }


    /**
     * This method is called when a player discards a powerup
     * @param player represents the player who discarded the powerup
     * @param powerup represents the powerup discarded
     */
    public static void onDiscardedPowerup(SimplePlayer player, Card powerup) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText("Hai scartato: \n" + powerup.getName());
                setMyPowerups(player.getPowerupCards());
            } else {
                mess.setText(player + "ha scartato \n" + powerup.getName());
            }
        });

    }


    /**
     * This method is called when a new turn starts
     * @param currentPlayer represents the player of the turn just started
     */
    public static void onTurnCreation(String currentPlayer) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (currentPlayer.equalsIgnoreCase(turnNicknameHashMap.get(myTurn))) {
                mess.setText("Inizia il tuo turno!");
            } else {
                mess.setText("Turno di: \n" + currentPlayer);
            }
        });
    }


    /**
     * This method is called when the player have to choose the players to hit and sends to the server the selected players
     * @param selectable represents the selectable players grouped by specific target
     * @param target represents the target(player, room, square, direction) of the weapon effect
     */
    public static void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            countTarget = 0;
            countPlayerList1 = 0;
            countPlayerList2 = 0;
            countPlayerList3 = 0;
            countPlayerList4 = 0;
            int numLists=selectable.size();
            if(numLists==1) {
                mess.setText(selectable.toString() + "\n \n Seleziona le liste \n e premi ok");
            }
            if(numLists==2){
                mess.setText(selectable.get(0).toString()+"\n"+selectable.get(1).toString() + "\n \n Seleziona le liste \n e premi ok");
            }
            if(numLists==3){
                mess.setText(selectable.get(0).toString()+"\n"+selectable.get(1).toString() + "\n"+ selectable.get(2).toString() + "\n \n Seleziona le liste \n e premi ok");
            }
            if(numLists==4){
                mess.setText(selectable.get(0).toString()+"\n"+selectable.get(1).toString() + "\n"+ selectable.get(2).toString() + "\n"+ selectable.get(3).toString() + "\n Seleziona le liste \n e premi ok");
            }
            if (selectable.size() == 1) {
                eff1.setDisable(false);
                eff1.setOnAction(null);
            }
            if (selectable.size() == 2) {
                eff1.setDisable(false);
                eff1.setOnAction(null);
                eff2.setDisable(true);
                eff2.setOnAction(null);

            }
            if (selectable.size() == 3) {
                eff1.setDisable(false);
                eff1.setOnAction(null);
                eff2.setDisable(true);
                eff2.setOnAction(null);
                eff3.setDisable(false);
                eff3.setOnAction(null);
            }
            if (selectable.size() == 4) {
                eff1.setDisable(false);
                eff1.setOnAction(null);
                eff2.setDisable(true);
                eff2.setOnAction(null);
                eff3.setDisable(false);
                eff3.setOnAction(null);
                eff4.setDisable(false);
                eff4.setOnAction(null);
            }

            if(target.getMaxNumber()==-1){
                mess.setText("Schiaccia ok");
                tempPlSelected=selectable;
                eff1.setDisable(true);
                eff2.setDisable(true);
                eff3.setDisable(true);
                eff4.setDisable(true);
                okay.setDisable(false);

            }

            set1Function(selectable, target);
            set2Function(selectable, target);
            set3Function(selectable, target);
            set4Function(selectable, target);
            setOkayFunction(target);

        });


    }


    /**
     * This method is called when the player can choose to use a powerup during his shoot routine
     */
    public static void onCanUsePowerup() {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText("Vuoi usare \n un potenziamento?");
            yes.setDisable(false);
            no.setDisable(false);
            yes.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    no.setOnAction(null);
                    yes.setOnAction(null);
                    no.setDisable(true);
                    yes.setDisable(true);

                    connection.usePowerup(true);
                }
            });
            no.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    yes.setOnAction(null);
                    no.setOnAction(null);
                    yes.setDisable(true);
                    no.setDisable(true);
                    connection.usePowerup(false);
                }
            });
        });
    }


    /**
     * This method is called when the player can choose to stop the shoot routine
     */
    public static void onCanStopRoutine() {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText("Vuoi fermarti qui?");
            yes.setDisable(false);
            no.setDisable(false);
            yes.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    no.setOnAction(null);
                    yes.setOnAction(null);
                    no.setDisable(true);
                    yes.setDisable(true);

                    connection.stopRoutine(true);
                }
            });
            no.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    yes.setOnAction(null);
                    no.setOnAction(null);
                    yes.setDisable(true);
                    no.setDisable(true);
                    connection.stopRoutine(false);
                }
            });
        });
    }


    /**
     * This method is called when the player have to choose which weapon to shoot with and sends to the server the selected weapon
     * @param usableWeapons represents the usable weapons
     */
    public static void onUsableWeapons(List<Card> usableWeapons) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText("Seleziona l'arma \n che vuoi usare");
            for (int i = 0; i < usableWeapons.size(); i++) {
                String usWeap = usableWeapons.get(i).getId();
                setClickWeapon(usableWeapons, usWeap);
            }
        });


    }


    /**
     * This method is called when the player have to choose which effect to use and sends to the server the selected effect
     * @param effects represents the usable effects
     */
    public static void onAvailableEffects(List<String> effects) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
                    mess.setText("Seleziona effetto \n" + effects.toString());
                    switch (effects.size()) {
                        case 1:
                            eff1.setDisable(false);
                            eff1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff1.setDisable(true);
                                    connection.selectEffect(effects.get(0));
                                }
                            });
                            break;

                        case 2:
                            eff1.setDisable(false);
                            eff2.setDisable(false);
                            eff1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff1.setDisable(true);
                                    eff2.setDisable(true);
                                    connection.selectEffect(effects.get(0));
                                }
                            });
                            eff2.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    connection.selectEffect(effects.get(1));
                                }
                            });
                            break;

                        case 3:
                            eff1.setDisable(false);
                            eff2.setDisable(false);
                            eff3.setDisable(false);
                            eff1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff1.setDisable(true);
                                    eff2.setDisable(true);
                                    eff3.setDisable(true);
                                    connection.selectEffect(effects.get(0));
                                }
                            });
                            eff2.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    eff3.setDisable(true);
                                    connection.selectEffect(effects.get(1));
                                }
                            });
                            eff3.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    eff3.setDisable(true);
                                    connection.selectEffect(effects.get(2));
                                }
                            });
                            break;

                        case 4:
                            eff1.setDisable(false);
                            eff2.setDisable(false);
                            eff3.setDisable(false);
                            eff4.setDisable(false);
                            eff1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff1.setDisable(true);
                                    eff2.setDisable(true);
                                    eff3.setDisable(true);
                                    eff4.setDisable(true);
                                    connection.selectEffect(effects.get(0));
                                }
                            });
                            eff2.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    eff3.setDisable(true);
                                    eff4.setDisable(true);
                                    connection.selectEffect(effects.get(1));
                                }
                            });
                            eff3.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    eff3.setDisable(true);
                                    eff4.setDisable(true);
                                    connection.selectEffect(effects.get(2));
                                }
                            });
                            eff4.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    eff2.setDisable(true);
                                    eff1.setDisable(true);
                                    eff3.setDisable(true);
                                    eff4.setDisable(true);
                                    connection.selectEffect(effects.get(3));
                                }
                            });
                            break;


                    }
                });



    }


    /**
     * This method is called when a player pays an effect
     * @param player represents the player who paid the effect
     * @param discardedPowerups represents the powerup discarded if there is one
     * @param usedAmmo represents the ammo used if there are
     */
    public static void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            mess.setText(player.getNickname() + " \n ha pagato un effetto");
            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                setMyAmmo(player);
                setMyPowerups(player.getPowerupCards());
            } else {
                updatePlayerVisibility(player);
            }
        });

    }


    /**
     * This method is called when a card is used
     * @param card represents the card used
     */
    public static void onUsedCard(Card card) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("La carta\n " + card.getName() + "\n è stata usata");
        });

    }


    /**
     * This method is called when the player chooses to use a powerup and sends to the server the powerup chosen
     * @param powerups represents the usable powerups
     */
    public static void onAvailablePowerups(List<Card> powerups) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Seleziona il potenziamento\n che vuoi usare");
            for (int i = 0; i < powerups.size(); i++) {
                String usPowerup = powerups.get(i).getId();
                setUsablePowerup(usPowerup);
                setMyPowerupsAction(usPowerup, powerups);

            }
        });

    }


    /**
     * This method is called when a player finishes the run routine
     * @param player represents the player who finished the run routine
     * @param newPosition represents the new position of the player that finished the run routine
     */
    public static void onRunCompleted(SimplePlayer player, int[] newPosition) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (player.getNickname().equalsIgnoreCase(myNickname)) {
                setMyPosition(newPosition);
            } else {
                mess.setText(player.getNickname() + " si è spostato");
                setPosition(newPosition, nicknameTurnHashMap.get(player.getNickname()));
            }
        });

    }


    /**
     * This method is called when the player chooses to run
     * @param matrix represents where the player can move
     */
    public static void onRunRoutine(MatrixHelper matrix) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Scegli dove spostarti");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (matrix.toBooleanMatrix()[i][j]) {
                        //squareButtonMatrix[i][j].setStyle("-fx-background-color: green");
                        //squareButtonMatrix[i][j].setDisable(false);
                        //setButtonAction(squareButtonMatrix[i][j], i, j);
                        squareMatrix[i][j].getSquareButton().setDisable(false);
                        squareMatrix[i][j].getSquareButton().setStyle("-fx-background-color: rgba(76,175,80,0.3)");
                        setButtonAction(squareMatrix[i][j].getSquareButton(), i, j);

                        //setta a true il bottone in quella posizione e nel bottone ServerConnection.runAction(i, j)
                    }
                }
            }
        });

    }

    /**
     * This method is called when a player relogs in the game
     * @param players represents the list of players in the game
     * @param gameBoard represents the game board
     * @param frenzy is true if the game is in frenzy mode
     */
    public static void onPlayerWakeUp(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            reconnection=true;
            for(int i=0; i<players.size(); i++){
                if (players.get(i).getNickname().equalsIgnoreCase(logNickname))
                    turnReconnection=i+1;
            }
            onMatchCreation(players, turnReconnection);
            //stage.close();
            Platform.setImplicitExit(false);
            Platform.runLater(()-> {
                        initGameWindow(gameBoard.getSourceReference(), gameBoard.getSkullNumber());
                    });
            init=true;
            updateBoard(gameBoard);
            updatePlayerBoards(players, frenzy);
            mess.setText("Sei di nuovo\n in partita");
            setMyAmmo(players.get(turnReconnection-1));
            setMyPowerups(players.get(turnReconnection-1).getPowerupCards());
            setMyWeapons(players.get(turnReconnection-1).getWeaponCards());
            for(int i=0; i<players.size(); i++){
                if(i!=turnReconnection-1) updatePlayerVisibility(players.get(i));

            }

        });
    }

    /**
     * This method is called when a player returns in the game
     * @param nickname represents the nickname of the player again in the game
     */
    public static void onRecoverPlayerAdvise(String nickname){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

                    if (nickname.equalsIgnoreCase(myNickname)) {
                        stage.close();
                        //MainWindow.stage.show();
                    } else {
                        mess.setText(nickname + "\n è tornato in partita");
                    }
                });
    }

    /**
     * This method is called when the player has already 3 powerups
     */
    public static void onFullOfPowerup(){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Hai già 3 \npotenziamenti");
        });

    }

    /**
     * This method is called when the player can counterattack
     */
    public static void onCanCounterAttack(){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Vuoi usare una\n granata venom?");
            yes.setDisable(false);
            no.setDisable(false);
            yes.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    no.setOnAction(null);
                    yes.setOnAction(null);
                    no.setDisable(true);
                    yes.setDisable(true);

                    connection.counterAttackAnswer(true);
                }
            });
            no.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    yes.setOnAction(null);
                    no.setOnAction(null);
                    yes.setDisable(true);
                    no.setDisable(true);
                    connection.counterAttackAnswer(false);
                }
            });
        });
    }

    /**
     * This method is called when a player counterattacked
     * @param currentPlayer represents the player that attacked
     * @param player represents the player that counterattacked
     * @param powerup represents the powerup used to counterattack
     */
    public static void onCounterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            if (currentPlayer.getNickname().equalsIgnoreCase(myNickname)) {
                mess.setText(player.getNickname() + " ti ha\n contrattaccato \ncon una " + powerup.getName());
                setMyMarks(currentPlayer.getMarks());
                setMyDamages(currentPlayer.getDamages());
            } else {
                if (player.getNickname().equalsIgnoreCase(myNickname)) {
                    mess.setText("Hai usato una\n " + powerup.getName());
                    setMyPowerups(player.getPowerupCards());
                } else {
                    mess.setText(player.getNickname() + " ha usato una\n" + powerup.getName() + "\n contro " + currentPlayer.getNickname());
                }
                updatePlayerBoard(currentPlayer);
            }
        });

    }

    /**
     * This method is called when a player don't answer in time to a canCounterAttack message
     */
    public static void onCounterAttackTimeOut(){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            yes.setDisable(true);
            no.setDisable(true);
            mess.setText("Tempo scaduto!");
        });
    }

    /**
     * This method is called where there is an error
     * @param cause represents the cause of the error
     * @param message represents the message sent for the error
     */
    public static void handleFatalError(String cause, String message){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

                    mess.setText(message + "\n" + cause);
                });
        //messagebox consiglia fil
    }

    /**
     * This method is called when a player disconnects
     */
    public static void onDisconnectionAdvise(){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            mess.setText("Sei stato\n disconnesso");
            stage.close();
            LoginWindow.log(stage);
        });
    }

    /**
     * This method is called when an end game message is received and sends to the server a confirm message of end
     * @param players represents the list of the players in the game
     */
    public static void onGameEnd(List<SimplePlayer> players){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

            numPlayers = players.size();
            int yourPoints = players.get(myTurn - 1).getScore();
            mess.setText("La partita è\n terminata.\n Hai fatto " + yourPoints + " punti");

            connection.confirmEndGame();
        });
    }

    /**
     * This method shows the ranking of the players at the end of the game
     * @param nicknames represents the nicknames of the players from the winner to the loser
     * @param points represents the points of the players
     */
    public static void onLeaderboardReceived(List<String> nicknames, List<Integer> points){
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {

                    stage.close();

                    if (nicknames.get(0).equalsIgnoreCase(myNickname)) winner = true;

                    finalstage = new Stage();

                    finalPane = new AnchorPane();

                    Label winLabel = new Label();
                    if (winner) {
                        winLabel.setText("HAI VINTO!");
                    } else {
                        winLabel.setText("HAI PERSO");
                    }
                    winLabel.getStyleClass().add("winLabel");
                    winLabel.setPrefSize(300 * widthScaleFactor, 100 * heightScaleFactor);
                    winLabel.setLayoutX(200 * widthScaleFactor);
                    winLabel.setLayoutY(50 * heightScaleFactor);
                    finalPane.getChildren().add(winLabel);

                    Label firstPlayer = new Label("Vincitore: " + nicknames.get(0) + "  " + points.get(0) + " punti");
                    firstPlayer.getStyleClass().add("plLabel");
                    firstPlayer.setPrefSize(300 * widthScaleFactor, 50 * heightScaleFactor);
                    firstPlayer.setLayoutX(50 * widthScaleFactor);
                    firstPlayer.setLayoutY(200 * heightScaleFactor);
                    finalPane.getChildren().add(firstPlayer);
                    if (nicknames.size() > 1) {
                        Label secPlayer = new Label("2. " + nicknames.get(1) + "  " + points.get(1) + " punti");
                        secPlayer.getStyleClass().add("plLabel");
                        secPlayer.setPrefSize(300 * widthScaleFactor, 50 * heightScaleFactor);
                        secPlayer.setLayoutX(50 * widthScaleFactor);
                        secPlayer.setLayoutY(250 * heightScaleFactor);
                        finalPane.getChildren().add(secPlayer);
                    }
                    if (nicknames.size() > 2) {
                        Label thirdPlayer = new Label("3. " + nicknames.get(2) + "  " + points.get(2) + " punti");
                        thirdPlayer.getStyleClass().add("plLabel");
                        thirdPlayer.setPrefSize(300 * widthScaleFactor, 50 * heightScaleFactor);
                        thirdPlayer.setLayoutX(50 * widthScaleFactor);
                        thirdPlayer.setLayoutY(300 * heightScaleFactor);
                        finalPane.getChildren().add(thirdPlayer);
                    }
                    if (nicknames.size() > 3) {
                        Label fourthPlayer = new Label("4. " + nicknames.get(3) + "  " + points.get(3) + " punti");
                        fourthPlayer.getStyleClass().add("plLabel");
                        fourthPlayer.setPrefSize(300 * widthScaleFactor, 50 * heightScaleFactor);
                        fourthPlayer.setLayoutX(50 * widthScaleFactor);
                        fourthPlayer.setLayoutY(350 * heightScaleFactor);
                        finalPane.getChildren().add(fourthPlayer);
                    }
                    if (nicknames.size() > 4) {
                        Label fifthPlayer = new Label("5. " + nicknames.get(4) + "  " + points.get(4) + " punti");
                        fifthPlayer.getStyleClass().add("plLabel");
                        fifthPlayer.setPrefSize(300 * widthScaleFactor, 50 * heightScaleFactor);
                        fifthPlayer.setLayoutX(50 * widthScaleFactor);
                        fifthPlayer.setLayoutY(400 * heightScaleFactor);
                        finalPane.getChildren().add(fifthPlayer);
                    }

                    finalScene = new Scene(finalPane, 700 * widthScaleFactor, 500 * heightScaleFactor);
                    finalScene.getStylesheets().addAll(MainWindow.class.getResource("/gui/gameWindow.css").toExternalForm());
                    finalstage.setScene(finalScene);
                    finalstage.show();
                });


    }
}


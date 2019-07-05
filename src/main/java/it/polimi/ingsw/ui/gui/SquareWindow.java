package it.polimi.ingsw.ui.gui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


/**
 * This class represents a square of the map
 */
public class SquareWindow {
    private Button squareButton;
    private double[] us1;
    private double[] us2;
    private double[] us3;
    private double[] us4;
    private double[] us5;

    private int[] pos;

    private boolean hasRespawnPoint;
    private Button weapon1;
    private Button weapon2;
    private Button weapon3;

    private boolean hasAmmo;
    private ImageView ammo;

    private double widthScaleFactor;
    private double heightScaleFactor;

    /**
     * This constructor instantiates a square
     * @param i is the x coordinate of the square
     * @param j is the y coordinate of the square
     * @param nummap is the number of the map
     * @param widthFactor is the scale factor for the width
     * @param heightFactor is the scale factor for the height
     */
    public SquareWindow(int i, int j, int nummap, double widthFactor, double heightFactor){
        squareButton=new Button();
        weapon1=new Button();
        weapon2=new Button();
        weapon3=new Button();
        ammo=new ImageView();
        pos=new int[]{i, j};
        us1=new double[]{0, 0};
        us2=new double[]{0, 0};
        us3=new double[]{0, 0};
        us4=new double[]{0, 0};
        us5=new double[]{0, 0};

        widthScaleFactor=widthFactor;
        heightScaleFactor=heightFactor;
        if(nummap==1) configMap1(i, j);
        if(nummap==2) configMap2(i, j);
        if(nummap==3) configMap3(i, j);
        if(nummap==4) configMap4(i, j);
    }

    public Button getSquareButton() {
        return squareButton;
    }

    public double[] getUs1() {
        return new double[]{us1[0], us1[1]};
    }

    public double[] getUs2() {
        return new double[]{us2[0], us2[1]};
    }

    public double[] getUs3(){
        return new double[]{us3[0], us3[1]};
    }

    public double[] getUs4() {
        return new double[]{us4[0], us4[1]};
    }

    public double[] getUs5(){
        return new double[]{us5[0], us5[1]};
    }

    public int[] getPos(){return pos;}

    /**
     * This method says if the square is a respawn point
     * @return true if it is a respawn point
     */
    public boolean hasRespawn(){return hasRespawnPoint;}

    /**
     * This method says if the square has an ammo tile
     * @return
     */
    public boolean hasAmmoPoint(){return hasAmmo;}

    public ImageView getAmmo() {
        return ammo;
    }

    public Button getWeapon1() {
        return weapon1;
    }

    public Button getWeapon2() {
        return weapon2;
    }

    public Button getWeapon3() {
        return weapon3;
    }

    /**
     * This method configs a square of the second map
     * @param i is the x coordinate of the square
     * @param j is the y coordinate of the square
     */
    private void configMap2(int i, int j){
        if(i==0&&j==0){
            configSquareButton(squareButton, 80, 83,111, 105);
            us1[0]=123*widthScaleFactor;
            us1[1]=150*heightScaleFactor;
            us2[0]=138*widthScaleFactor;
            us2[1]=150*heightScaleFactor;
            us3[0]=153*widthScaleFactor;
            us3[1]=150*heightScaleFactor;
            us4[0]=168*widthScaleFactor;
            us4[1]=150*heightScaleFactor;
            us5[0]=183*widthScaleFactor;
            us5[1]=150*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21, 21, 123, 115);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  97, 83, 201, 106);
            us1[0]=212*widthScaleFactor;
            us1[1]=139*heightScaleFactor;
            us2[0]=224*widthScaleFactor;
            us2[1]=139*heightScaleFactor;
            us3[0]=236*widthScaleFactor;
            us3[1]=139*heightScaleFactor;
            us4[0]=248*widthScaleFactor;
            us4[1]=139*heightScaleFactor;
            us5[0]=260*widthScaleFactor;
            us5[1]=139*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 216, 160);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=315*widthScaleFactor;
            us1[1]=142*heightScaleFactor;
            us2[0]=327*widthScaleFactor;
            us2[1]=142*heightScaleFactor;
            us3[0]=339*widthScaleFactor;
            us3[1]=142*heightScaleFactor;
            us4[0]=351*widthScaleFactor;
            us4[1]=142*heightScaleFactor;
            us5[0]=363*widthScaleFactor;
            us5[1]=142*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==0&&j==3){
            configSquareButton(squareButton,  68, 65, 419, 125);
            us1[0]=421*widthScaleFactor;
            us1[1]=129*heightScaleFactor;
            us2[0]=433*widthScaleFactor;
            us2[1]=129*heightScaleFactor;
            us3[0]=445*widthScaleFactor;
            us3[1]=129*heightScaleFactor;
            us4[0]=457*widthScaleFactor;
            us4[1]=129*heightScaleFactor;
            us5[0]=469*widthScaleFactor;
            us5[1]=129*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 160);
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  82, 84, 113, 201);
            us1[0]=128*widthScaleFactor;
            us1[1]=230*heightScaleFactor;
            us2[0]=140*widthScaleFactor;
            us2[1]=230*heightScaleFactor;
            us3[0]=152*widthScaleFactor;
            us3[1]=230*heightScaleFactor;
            us4[0]=164*widthScaleFactor;
            us4[1]=230*heightScaleFactor;
            us5[0]=176*widthScaleFactor;
            us5[1]=230*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  94, 86, 202, 211);
            us1[0]=233*widthScaleFactor;
            us1[1]=239*heightScaleFactor;
            us2[0]=245*widthScaleFactor;
            us2[1]=239*heightScaleFactor;
            us3[0]=257*widthScaleFactor;
            us3[1]=239*heightScaleFactor;
            us4[0]=269*widthScaleFactor;
            us4[1]=239*heightScaleFactor;
            us5[0]=281*widthScaleFactor;
            us5[1]=239*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 207, 237);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  77, 99, 322, 201);
            us1[0]=320*widthScaleFactor;
            us1[1]=239*heightScaleFactor;
            us2[0]=332*widthScaleFactor;
            us2[1]=239*heightScaleFactor;
            us3[0]=344*widthScaleFactor;
            us3[1]=239*heightScaleFactor;
            us4[0]=356*widthScaleFactor;
            us4[1]=239*heightScaleFactor;
            us5[0]=368*widthScaleFactor;
            us5[1]=239*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 268);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  78, 91, 408, 204);
            us1[0]=417*widthScaleFactor;
            us1[1]=239*heightScaleFactor;
            us2[0]=429*widthScaleFactor;
            us2[1]=239*heightScaleFactor;
            us3[0]=441*widthScaleFactor;
            us3[1]=239*heightScaleFactor;
            us4[0]=453*widthScaleFactor;
            us4[1]=239*heightScaleFactor;
            us5[0]=465*widthScaleFactor;
            us5[1]=239*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 414, 268);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=214*widthScaleFactor;
            us1[1]=331*heightScaleFactor;
            us2[0]=226*widthScaleFactor;
            us2[1]=331*heightScaleFactor;
            us3[0]=238*widthScaleFactor;
            us3[1]=331*heightScaleFactor;
            us4[0]=250*widthScaleFactor;
            us4[1]=331*heightScaleFactor;
            us5[0]=262*widthScaleFactor;
            us5[1]=331*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  82, 83, 314, 311);
            us1[0]=324*widthScaleFactor;
            us1[1]=327*heightScaleFactor;
            us2[0]=336*widthScaleFactor;
            us2[1]=327*heightScaleFactor;
            us3[0]=348*widthScaleFactor;
            us3[1]=327*heightScaleFactor;
            us4[0]=360*widthScaleFactor;
            us4[1]=327*heightScaleFactor;
            us5[0]=372*widthScaleFactor;
            us5[1]=327*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 329, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  84, 90, 405, 301);
            us1[0]=411*widthScaleFactor;
            us1[1]=306*heightScaleFactor;
            us2[0]=423*widthScaleFactor;
            us2[1]=306*heightScaleFactor;
            us3[0]=435*widthScaleFactor;
            us3[1]=306*heightScaleFactor;
            us4[0]=447*widthScaleFactor;
            us4[1]=306*heightScaleFactor;
            us5[0]=459*widthScaleFactor;
            us5[1]=306*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 516, 257);
            configSquareButton(weapon2, 84, 57, 516, 323);
            configSquareButton(weapon3, 84, 57, 516, 389);
            hasAmmo=false;
        }
    }

    private void configMap1(int i, int j){
        if(i==0&&j==0){
            configSquareButton(squareButton, 80, 83,111, 105);
            us1[0]=119*widthScaleFactor;
            us1[1]=139*heightScaleFactor;
            us2[0]=131*widthScaleFactor;
            us2[1]=139*heightScaleFactor;
            us3[0]=143*widthScaleFactor;
            us3[1]=139*heightScaleFactor;
            us4[0]=155*widthScaleFactor;
            us4[1]=139*heightScaleFactor;
            us5[0]=167*widthScaleFactor;
            us5[1]=139*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21, 21, 123, 115);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  97, 83, 201, 106);
            us1[0]=219*widthScaleFactor;
            us1[1]=139*heightScaleFactor;
            us2[0]=231*widthScaleFactor;
            us2[1]=139*heightScaleFactor;
            us3[0]=243*widthScaleFactor;
            us3[1]=139*heightScaleFactor;
            us4[0]=255*widthScaleFactor;
            us4[1]=139*heightScaleFactor;
            us5[0]=267*widthScaleFactor;
            us5[1]=139*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 216, 160);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=322*widthScaleFactor;
            us1[1]=150*heightScaleFactor;
            us2[0]=334*widthScaleFactor;
            us2[1]=150*heightScaleFactor;
            us3[0]=346*widthScaleFactor;
            us3[1]=150*heightScaleFactor;
            us4[0]=358*widthScaleFactor;
            us4[1]=150*heightScaleFactor;
            us5[0]=370*widthScaleFactor;
            us5[1]=150*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  82, 84, 113, 201);
            us1[0]=128*widthScaleFactor;
            us1[1]=230*heightScaleFactor;
            us2[0]=140*widthScaleFactor;
            us2[1]=230*heightScaleFactor;
            us3[0]=152*widthScaleFactor;
            us3[1]=230*heightScaleFactor;
            us4[0]=164*widthScaleFactor;
            us4[1]=230*heightScaleFactor;
            us5[0]=176*widthScaleFactor;
            us5[1]=230*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  94, 86, 202, 211);
            us1[0]=232*widthScaleFactor;
            us1[1]=239*heightScaleFactor;
            us2[0]=244*widthScaleFactor;
            us2[1]=239*heightScaleFactor;
            us3[0]=256*widthScaleFactor;
            us3[1]=239*heightScaleFactor;
            us4[0]=268*widthScaleFactor;
            us4[1]=239*heightScaleFactor;
            us5[0]=280*widthScaleFactor;
            us5[1]=239*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 207, 237);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  100, 91,300, 205);
            us1[0]=305*widthScaleFactor;
            us1[1]=229*heightScaleFactor;
            us2[0]=317*widthScaleFactor;
            us2[1]=229*heightScaleFactor;
            us3[0]=329*widthScaleFactor;
            us3[1]=229*heightScaleFactor;
            us4[0]=341*widthScaleFactor;
            us4[1]=229*heightScaleFactor;
            us5[0]=353*widthScaleFactor;
            us5[1]=229*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 250);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  67, 90, 420, 205);
            us1[0]=422*widthScaleFactor;
            us1[1]=274*heightScaleFactor;
            us2[0]=434*widthScaleFactor;
            us2[1]=274*heightScaleFactor;
            us3[0]=446*widthScaleFactor;
            us3[1]=274*heightScaleFactor;
            us4[0]=458*widthScaleFactor;
            us4[1]=274*heightScaleFactor;
            us5[0]=470*widthScaleFactor;
            us5[1]=274*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 249);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 82, 197, 314);
            us1[0]=214*widthScaleFactor;
            us1[1]=333*heightScaleFactor;
            us2[0]=226*widthScaleFactor;
            us2[1]=333*heightScaleFactor;
            us3[0]=238*widthScaleFactor;
            us3[1]=333*heightScaleFactor;
            us4[0]=250*widthScaleFactor;
            us4[1]=333*heightScaleFactor;
            us5[0]=262*widthScaleFactor;
            us5[1]=333*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  97, 79, 304, 313);
            us1[0]=313*widthScaleFactor;
            us1[1]=333*heightScaleFactor;
            us2[0]=325*widthScaleFactor;
            us2[1]=333*heightScaleFactor;
            us3[0]=337*widthScaleFactor;
            us3[1]=333*heightScaleFactor;
            us4[0]=349*widthScaleFactor;
            us4[1]=333*heightScaleFactor;
            us5[0]=361*widthScaleFactor;
            us5[1]=333*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 313, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  72, 93, 418, 299);
            us1[0]=421*widthScaleFactor;
            us1[1]=304*heightScaleFactor;
            us2[0]=433*widthScaleFactor;
            us2[1]=304*heightScaleFactor;
            us3[0]=445*widthScaleFactor;
            us3[1]=304*heightScaleFactor;
            us4[0]=457*widthScaleFactor;
            us4[1]=304*heightScaleFactor;
            us5[0]=469*widthScaleFactor;
            us5[1]=304*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 516, 257);
            configSquareButton(weapon2, 84, 57, 516, 323);
            configSquareButton(weapon3, 84, 57, 516, 389);
            hasAmmo=false;
        }

    }

    private void configMap3(int i, int j){
        if(i==0&&j==0){
            configSquareButton(squareButton, 71, 94, 110, 105);
            us1[0]=113*widthScaleFactor;
            us1[1]=138*heightScaleFactor;
            us2[0]=125*widthScaleFactor;
            us2[1]=138*heightScaleFactor;
            us3[0]=137*widthScaleFactor;
            us3[1]=138*heightScaleFactor;
            us4[0]=149*widthScaleFactor;
            us4[1]=138*heightScaleFactor;
            us5[0]=161*widthScaleFactor;
            us5[1]=138*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21,21, 126, 164);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  96, 82, 202, 106);
            us1[0]=224*widthScaleFactor;
            us1[1]=145*heightScaleFactor;
            us2[0]=236*widthScaleFactor;
            us2[1]=145*heightScaleFactor;
            us3[0]=248*widthScaleFactor;
            us3[1]=145*heightScaleFactor;
            us4[0]=260*widthScaleFactor;
            us4[1]=145*heightScaleFactor;
            us5[0]=272*widthScaleFactor;
            us5[1]=145*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 215, 112);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=320*widthScaleFactor;
            us1[1]=145*heightScaleFactor;
            us2[0]=332*widthScaleFactor;
            us2[1]=145*heightScaleFactor;
            us3[0]=346*widthScaleFactor;
            us3[1]=145*heightScaleFactor;
            us4[0]=358*widthScaleFactor;
            us4[1]=145*heightScaleFactor;
            us5[0]=370*widthScaleFactor;
            us5[1]=145*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==0&&j==3){
            configSquareButton(squareButton,  68, 65, 419, 125);
            us1[0]=423*widthScaleFactor;
            us1[1]=131*heightScaleFactor;
            us2[0]=435*widthScaleFactor;
            us2[1]=131*heightScaleFactor;
            us3[0]=447*widthScaleFactor;
            us3[1]=131*heightScaleFactor;
            us4[0]=459*widthScaleFactor;
            us4[1]=131*heightScaleFactor;
            us5[0]=471*widthScaleFactor;
            us5[1]=131*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 160);
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  68, 96, 113, 202);
            us1[0]=116*widthScaleFactor;
            us1[1]=213*heightScaleFactor;
            us2[0]=128*widthScaleFactor;
            us2[1]=213*heightScaleFactor;
            us3[0]=140*widthScaleFactor;
            us3[1]=213*heightScaleFactor;
            us4[0]=152*widthScaleFactor;
            us4[1]=213*heightScaleFactor;
            us5[0]=164*widthScaleFactor;
            us5[1]=213*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  90, 92, 204, 205);
            us1[0]=233*widthScaleFactor;
            us1[1]=243*heightScaleFactor;
            us2[0]=245*widthScaleFactor;
            us2[1]=243*heightScaleFactor;
            us3[0]=257*widthScaleFactor;
            us3[1]=243*heightScaleFactor;
            us4[0]=269*widthScaleFactor;
            us4[1]=243*heightScaleFactor;
            us5[0]=281*widthScaleFactor;
            us5[1]=243*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 208, 240);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  77, 99, 322, 201);
            us1[0]=327*widthScaleFactor;
            us1[1]=241*heightScaleFactor;
            us2[0]=339*widthScaleFactor;
            us2[1]=241*heightScaleFactor;
            us3[0]=351*widthScaleFactor;
            us3[1]=241*heightScaleFactor;
            us4[0]=363*widthScaleFactor;
            us4[1]=241*heightScaleFactor;
            us5[0]=375*widthScaleFactor;
            us5[1]=241*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 268);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  78, 91, 408, 204);
            us1[0]=416*widthScaleFactor;
            us1[1]=241*heightScaleFactor;
            us2[0]=428*widthScaleFactor;
            us2[1]=241*heightScaleFactor;
            us3[0]=440*widthScaleFactor;
            us3[1]=241*heightScaleFactor;
            us4[0]=452*widthScaleFactor;
            us4[1]=241*heightScaleFactor;
            us5[0]=464*widthScaleFactor;
            us5[1]=241*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 414, 268);
        }
        if(i==2&&j==0){
            configSquareButton(squareButton,  84, 79,  112, 313);
            us1[0]=124*widthScaleFactor;
            us1[1]=334*heightScaleFactor;
            us2[0]=136*widthScaleFactor;
            us2[1]=334*heightScaleFactor;
            us3[0]=148*widthScaleFactor;
            us3[1]=334*heightScaleFactor;
            us4[0]=160*widthScaleFactor;
            us4[1]=334*heightScaleFactor;
            us5[0]=172*widthScaleFactor;
            us5[1]=334*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 126, 354);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=219*widthScaleFactor;
            us1[1]=334*heightScaleFactor;
            us2[0]=231*widthScaleFactor;
            us2[1]=334*heightScaleFactor;
            us3[0]=243*widthScaleFactor;
            us3[1]=334*heightScaleFactor;
            us4[0]=255*widthScaleFactor;
            us4[1]=334*heightScaleFactor;
            us5[0]=267*widthScaleFactor;
            us5[1]=334*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  82, 83, 314, 311);
            us1[0]=326*widthScaleFactor;
            us1[1]=325*heightScaleFactor;
            us2[0]=338*widthScaleFactor;
            us2[1]=325*heightScaleFactor;
            us3[0]=350*widthScaleFactor;
            us3[1]=325*heightScaleFactor;
            us4[0]=362*widthScaleFactor;
            us4[1]=325*heightScaleFactor;
            us5[0]=374*widthScaleFactor;
            us5[1]=325*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 329, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  84, 90, 405, 301);
            us1[0]=411*widthScaleFactor;
            us1[1]=306*heightScaleFactor;
            us2[0]=423*widthScaleFactor;
            us2[1]=306*heightScaleFactor;
            us3[0]=435*widthScaleFactor;
            us3[1]=306*heightScaleFactor;
            us4[0]=447*widthScaleFactor;
            us4[1]=306*heightScaleFactor;
            us5[0]=459*widthScaleFactor;
            us5[1]=306*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 516, 257);
            configSquareButton(weapon2, 84, 57, 516, 323);
            configSquareButton(weapon3, 84, 57, 516, 389);
            hasAmmo=false;
        }

    }

    private void configMap4(int i, int j){
        if(i==0&&j==0){
            configSquareButton(squareButton, 71, 94, 110, 105);
            us1[0]=115*widthScaleFactor;
            us1[1]=138*heightScaleFactor;
            us2[0]=127*widthScaleFactor;
            us2[1]=138*heightScaleFactor;
            us3[0]=139*widthScaleFactor;
            us3[1]=138*heightScaleFactor;
            us4[0]=151*widthScaleFactor;
            us4[1]=138*heightScaleFactor;
            us5[0]=163*widthScaleFactor;
            us5[1]=138*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21,21, 126, 164);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  96, 82, 202, 106);
            us1[0]=224*widthScaleFactor;
            us1[1]=145*heightScaleFactor;
            us2[0]=236*widthScaleFactor;
            us2[1]=145*heightScaleFactor;
            us3[0]=248*widthScaleFactor;
            us3[1]=145*heightScaleFactor;
            us4[0]=260*widthScaleFactor;
            us4[1]=145*heightScaleFactor;
            us5[0]=272*widthScaleFactor;
            us5[1]=145*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 215, 112);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=322*widthScaleFactor;
            us1[1]=145*heightScaleFactor;
            us2[0]=334*widthScaleFactor;
            us2[1]=145*heightScaleFactor;
            us3[0]=346*widthScaleFactor;
            us3[1]=145*heightScaleFactor;
            us4[0]=358*widthScaleFactor;
            us4[1]=145*heightScaleFactor;
            us5[0]=370*widthScaleFactor;
            us5[1]=145*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  68, 96, 113, 202);
            us1[0]=118*widthScaleFactor;
            us1[1]=213*heightScaleFactor;
            us2[0]=130*widthScaleFactor;
            us2[1]=213*heightScaleFactor;
            us3[0]=142*widthScaleFactor;
            us3[1]=213*heightScaleFactor;
            us4[0]=154*widthScaleFactor;
            us4[1]=213*heightScaleFactor;
            us5[0]=166*widthScaleFactor;
            us5[1]=213*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  90, 92, 204, 205);
            us1[0]=231*widthScaleFactor;
            us1[1]=242*heightScaleFactor;
            us2[0]=243*widthScaleFactor;
            us2[1]=242*heightScaleFactor;
            us3[0]=255*widthScaleFactor;
            us3[1]=242*heightScaleFactor;
            us4[0]=267*widthScaleFactor;
            us4[1]=242*heightScaleFactor;
            us5[0]=279*widthScaleFactor;
            us5[1]=242*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 208, 240);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  99, 84, 301, 204);
            us1[0]=306*widthScaleFactor;
            us1[1]=224*heightScaleFactor;
            us2[0]=318*widthScaleFactor;
            us2[1]=224*heightScaleFactor;
            us3[0]=330*widthScaleFactor;
            us3[1]=224*heightScaleFactor;
            us4[0]=342*widthScaleFactor;
            us4[1]=224*heightScaleFactor;
            us5[0]=354*widthScaleFactor;
            us5[1]=224*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 250);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  67, 90, 421, 205);
            us1[0]=421*widthScaleFactor;
            us1[1]=279*heightScaleFactor;
            us2[0]=433*widthScaleFactor;
            us2[1]=279*heightScaleFactor;
            us3[0]=445*widthScaleFactor;
            us3[1]=279*heightScaleFactor;
            us4[0]=457*widthScaleFactor;
            us4[1]=279*heightScaleFactor;
            us5[0]=469*widthScaleFactor;
            us5[1]=279*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 249);
        }
        if(i==2&&j==0){
            configSquareButton(squareButton,  84, 79,  112, 313);
            us1[0]=122*widthScaleFactor;
            us1[1]=331*heightScaleFactor;
            us2[0]=134*widthScaleFactor;
            us2[1]=331*heightScaleFactor;
            us3[0]=146*widthScaleFactor;
            us3[1]=331*heightScaleFactor;
            us4[0]=158*widthScaleFactor;
            us4[1]=331*heightScaleFactor;
            us5[0]=170*widthScaleFactor;
            us5[1]=331*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 126, 354);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=215*widthScaleFactor;
            us1[1]=331*heightScaleFactor;
            us2[0]=227*widthScaleFactor;
            us2[1]=331*heightScaleFactor;
            us3[0]=239*widthScaleFactor;
            us3[1]=331*heightScaleFactor;
            us4[0]=251*widthScaleFactor;
            us4[1]=331*heightScaleFactor;
            us5[0]=263*widthScaleFactor;
            us5[1]=331*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  92, 79, 305, 312);
            us1[0]=314*widthScaleFactor;
            us1[1]=331*heightScaleFactor;
            us2[0]=326*widthScaleFactor;
            us2[1]=331*heightScaleFactor;
            us3[0]=338*widthScaleFactor;
            us3[1]=331*heightScaleFactor;
            us4[0]=350*widthScaleFactor;
            us4[1]=331*heightScaleFactor;
            us5[0]=362*widthScaleFactor;
            us5[1]=331*heightScaleFactor;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 313, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  71, 90, 419, 301);
            us1[0]=421*widthScaleFactor;
            us1[1]=305*heightScaleFactor;
            us2[0]=433*widthScaleFactor;
            us2[1]=305*heightScaleFactor;
            us3[0]=445*widthScaleFactor;
            us3[1]=305*heightScaleFactor;
            us4[0]=457*widthScaleFactor;
            us4[1]=305*heightScaleFactor;
            us5[0]=469*widthScaleFactor;
            us5[1]=305*heightScaleFactor;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 516, 257);
            configSquareButton(weapon2, 84, 57, 516, 323);
            configSquareButton(weapon3, 84, 57, 516, 389);
            hasAmmo=false;
        }

    }


    private void configSquareButton(Button button, double prefWidth, double prefHeight, double x, double y){
        //button=new Button();
        button.setPrefWidth(prefWidth*widthScaleFactor);
        button.setPrefHeight(prefHeight*heightScaleFactor);
        button.setLayoutX(x*widthScaleFactor);
        button.setLayoutY(y*heightScaleFactor);
        button.setStyle("-fx-background-color: transparent");
        button.setDisable(true);
    }

    private void configAmmoButton(ImageView imgv, double prefWidth, double prefHeight, double x, double y){
        //button=new Button();
        imgv.setFitWidth(prefWidth*widthScaleFactor);
        imgv.setFitHeight(prefHeight*heightScaleFactor);
        imgv.setX(x*widthScaleFactor);
        imgv.setY(y*heightScaleFactor);
        //button.setStyle("-fx-background-color: transparent");
        //button.setDisable(true);
    }




}

package it.polimi.ingsw.GUI;

import javafx.scene.control.Button;

//square
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
    private Button ammo;

    private double widthScaleFactor;
    private double heightScaleFactor;

    public SquareWindow(int i, int j, int nummap, double widthFactor, double heightFactor){
        squareButton=new Button();
        weapon1=new Button();
        weapon2=new Button();
        weapon3=new Button();
        ammo=new Button();
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

    public boolean hasRespawn(){return hasRespawnPoint;}

    public boolean hasAmmoPoint(){return hasAmmo;}

    public Button getAmmo() {
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

    private void configMap1(int i, int j){
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
            us1[0]=212;
            us1[1]=139;
            us2[0]=224;
            us2[1]=139;
            us3[0]=236;
            us3[1]=139;
            us4[0]=248;
            us4[1]=139;
            us5[0]=260;
            us5[1]=139;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 216, 160);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=315*widthScaleFactor;
            us1[1]=142*heightScaleFactor;
            us2[0]=327;
            us2[1]=142;
            us3[0]=339;
            us3[1]=142;
            us4[0]=351;
            us4[1]=142;
            us5[0]=363;
            us5[1]=142;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==0&&j==3){
            configSquareButton(squareButton,  68, 65, 419, 125);
            us1[0]=421;
            us1[1]=129;
            us2[0]=433;
            us2[1]=129;
            us3[0]=445;
            us3[1]=129;
            us4[0]=457;
            us4[1]=129;
            us5[0]=469;
            us5[1]=129;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 160);
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  82, 84, 113, 201);
            us1[0]=128;
            us1[1]=230;
            us2[0]=140;
            us2[1]=230;
            us3[0]=152;
            us3[1]=230;
            us4[0]=164;
            us4[1]=230;
            us5[0]=176;
            us5[1]=230;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  94, 86, 202, 211);
            us1[0]=233;
            us1[1]=239;
            us2[0]=245;
            us2[1]=239;
            us3[0]=257;
            us3[1]=239;
            us4[0]=269;
            us4[1]=239;
            us5[0]=281;
            us5[1]=239;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 207, 237);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  77, 99, 322, 201);
            us1[0]=320;
            us1[1]=239;
            us2[0]=332;
            us2[1]=239;
            us3[0]=344;
            us3[1]=239;
            us4[0]=356;
            us4[1]=239;
            us5[0]=368;
            us5[1]=239;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 268);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  78, 91, 408, 204);
            us1[0]=417;
            us1[1]=239;
            us2[0]=429;
            us2[1]=239;
            us3[0]=441;
            us3[1]=239;
            us4[0]=453;
            us4[1]=239;
            us5[0]=465;
            us5[1]=239;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 414, 268);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=214;
            us1[1]=331;
            us2[0]=226;
            us2[1]=331;
            us3[0]=238;
            us3[1]=331;
            us4[0]=250;
            us4[1]=331;
            us5[0]=262;
            us5[1]=331;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  82, 83, 314, 311);
            us1[0]=324;
            us1[1]=327;
            us2[0]=336;
            us2[1]=327;
            us3[0]=348;
            us3[1]=327;
            us4[0]=360;
            us4[1]=327;
            us5[0]=372;
            us5[1]=327;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 329, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  84, 90, 405, 301);
            us1[0]=411;
            us1[1]=306;
            us2[0]=423;
            us2[1]=306;
            us3[0]=435;
            us3[1]=306;
            us4[0]=447;
            us4[1]=306;
            us5[0]=459;
            us5[1]=306;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 516, 257);
            configSquareButton(weapon2, 84, 57, 516, 323);
            configSquareButton(weapon3, 84, 57, 516, 389);
            hasAmmo=false;
        }
    }

    private void configMap2(int i, int j){
        if(i==0&&j==0){
            configSquareButton(squareButton, 80, 83,111, 105);
            us1[0]=119;
            us1[1]=139;
            us2[0]=131;
            us2[1]=139;
            us3[0]=143;
            us3[1]=139;
            us4[0]=155;
            us4[1]=139;
            us5[0]=167;
            us5[1]=139;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21, 21, 123, 115);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  97, 83, 201, 106);
            us1[0]=219;
            us1[1]=139;
            us2[0]=231;
            us2[1]=139;
            us3[0]=243;
            us3[1]=139;
            us4[0]=255;
            us4[1]=139;
            us5[0]=267;
            us5[1]=139;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 216, 160);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=322;
            us1[1]=150;
            us2[0]=334;
            us2[1]=150;
            us3[0]=346;
            us3[1]=150;
            us4[0]=358;
            us4[1]=150;
            us5[0]=370;
            us5[1]=150;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  82, 84, 113, 201);
            us1[0]=128;
            us1[1]=230;
            us2[0]=140;
            us2[1]=230;
            us3[0]=152;
            us3[1]=230;
            us4[0]=164;
            us4[1]=230;
            us5[0]=176;
            us5[1]=230;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  94, 86, 202, 211);
            us1[0]=232;
            us1[1]=239;
            us2[0]=244;
            us2[1]=239;
            us3[0]=256;
            us3[1]=239;
            us4[0]=268;
            us4[1]=239;
            us5[0]=280;
            us5[1]=239;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 207, 237);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  100, 91,300, 205);
            us1[0]=305;
            us1[1]=229;
            us2[0]=317;
            us2[1]=229;
            us3[0]=329;
            us3[1]=229;
            us4[0]=341;
            us4[1]=229;
            us5[0]=353;
            us5[1]=229;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 250);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  67, 90, 420, 205);
            us1[0]=422;
            us1[1]=274;
            us2[0]=434;
            us2[1]=274;
            us3[0]=446;
            us3[1]=274;
            us4[0]=458;
            us4[1]=274;
            us5[0]=470;
            us5[1]=274;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 249);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 82, 197, 314);
            us1[0]=214;
            us1[1]=333;
            us2[0]=226;
            us2[1]=333;
            us3[0]=238;
            us3[1]=333;
            us4[0]=250;
            us4[1]=333;
            us5[0]=262;
            us5[1]=333;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  97, 79, 304, 313);
            us1[0]=313;
            us1[1]=333;
            us2[0]=325;
            us2[1]=333;
            us3[0]=337;
            us3[1]=333;
            us4[0]=349;
            us4[1]=333;
            us5[0]=361;
            us5[1]=333;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 313, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  72, 93, 418, 299);
            us1[0]=421;
            us1[1]=304;
            us2[0]=433;
            us2[1]=304;
            us3[0]=445;
            us3[1]=304;
            us4[0]=457;
            us4[1]=304;
            us5[0]=469;
            us5[1]=304;
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
            us1[0]=113;
            us1[1]=138;
            us2[0]=125;
            us2[1]=138;
            us3[0]=137;
            us3[1]=138;
            us4[0]=149;
            us4[1]=138;
            us5[0]=161;
            us5[1]=138;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21,21, 126, 164);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  96, 82, 202, 106);
            us1[0]=224;
            us1[1]=145;
            us2[0]=236;
            us2[1]=145;
            us3[0]=248;
            us3[1]=145;
            us4[0]=260;
            us4[1]=145;
            us5[0]=272;
            us5[1]=145;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 215, 112);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=320;
            us1[1]=145;
            us2[0]=332;
            us2[1]=145;
            us3[0]=346;
            us3[1]=145;
            us4[0]=358;
            us4[1]=145;
            us5[0]=370;
            us5[1]=145;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==0&&j==3){
            configSquareButton(squareButton,  68, 65, 419, 125);
            us1[0]=423;
            us1[1]=131;
            us2[0]=435;
            us2[1]=131;
            us3[0]=447;
            us3[1]=131;
            us4[0]=459;
            us4[1]=131;
            us5[0]=471;
            us5[1]=131;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 160);
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  68, 96, 113, 202);
            us1[0]=116;
            us1[1]=213;
            us2[0]=128;
            us2[1]=213;
            us3[0]=140;
            us3[1]=213;
            us4[0]=152;
            us4[1]=213;
            us5[0]=164;
            us5[1]=213;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  90, 92, 204, 205);
            us1[0]=233;
            us1[1]=243;
            us2[0]=245;
            us2[1]=243;
            us3[0]=257;
            us3[1]=243;
            us4[0]=269;
            us4[1]=243;
            us5[0]=281;
            us5[1]=243;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 208, 240);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  77, 99, 322, 201);
            us1[0]=327;
            us1[1]=241;
            us2[0]=339;
            us2[1]=241;
            us3[0]=351;
            us3[1]=241;
            us4[0]=363;
            us4[1]=241;
            us5[0]=375;
            us5[1]=241;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 268);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  78, 91, 408, 204);
            us1[0]=416;
            us1[1]=241;
            us2[0]=428;
            us2[1]=241;
            us3[0]=440;
            us3[1]=241;
            us4[0]=452;
            us4[1]=241;
            us5[0]=464;
            us5[1]=241;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 414, 268);
        }
        if(i==2&&j==0){
            configSquareButton(squareButton,  84, 79,  112, 313);
            us1[0]=124;
            us1[1]=334;
            us2[0]=136;
            us2[1]=334;
            us3[0]=148;
            us3[1]=334;
            us4[0]=160;
            us4[1]=334;
            us5[0]=172;
            us5[1]=334;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 126, 354);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=219;
            us1[1]=334;
            us2[0]=231;
            us2[1]=334;
            us3[0]=243;
            us3[1]=334;
            us4[0]=255;
            us4[1]=334;
            us5[0]=267;
            us5[1]=334;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  82, 83, 314, 311);
            us1[0]=326;
            us1[1]=325;
            us2[0]=338;
            us2[1]=325;
            us3[0]=350;
            us3[1]=325;
            us4[0]=362;
            us4[1]=325;
            us5[0]=374;
            us5[1]=325;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 329, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  84, 90, 405, 301);
            us1[0]=411;
            us1[1]=306;
            us2[0]=423;
            us2[1]=306;
            us3[0]=435;
            us3[1]=306;
            us4[0]=447;
            us4[1]=306;
            us5[0]=459;
            us5[1]=306;
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
            us1[0]=115;
            us1[1]=138;
            us2[0]=127;
            us2[1]=138;
            us3[0]=139;
            us3[1]=138;
            us4[0]=151;
            us4[1]=138;
            us5[0]=163;
            us5[1]=138;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo, 21,21, 126, 164);
        }
        if(i==0&&j==1){
            configSquareButton(squareButton,  96, 82, 202, 106);
            us1[0]=224;
            us1[1]=145;
            us2[0]=236;
            us2[1]=145;
            us3[0]=248;
            us3[1]=145;
            us4[0]=260;
            us4[1]=145;
            us5[0]=272;
            us5[1]=145;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,  21, 21, 215, 112);
        }
        if(i==0&&j==2){
            configSquareButton(squareButton,  95, 80, 303, 108);
            us1[0]=322;
            us1[1]=145;
            us2[0]=334;
            us2[1]=145;
            us3[0]=346;
            us3[1]=145;
            us4[0]=358;
            us4[1]=145;
            us5[0]=370;
            us5[1]=145;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 57, 84, 317, 2);
            configSquareButton(weapon2, 57, 84, 383, 2);
            configSquareButton(weapon3, 57, 84, 449, 2);
            hasAmmo=false;
        }
        if(i==1&&j==0){
            configSquareButton(squareButton,  68, 96, 113, 202);
            us1[0]=118;
            us1[1]=213;
            us2[0]=130;
            us2[1]=213;
            us3[0]=142;
            us3[1]=213;
            us4[0]=154;
            us4[1]=213;
            us5[0]=166;
            us5[1]=213;
            hasRespawnPoint=true;
            configSquareButton(weapon1, 84, 57, 2, 166);
            configSquareButton(weapon2, 84, 57, 2, 232);
            configSquareButton(weapon3, 84, 57, 2, 298);
            hasAmmo=false;
        }
        if(i==1&&j==1){
            configSquareButton(squareButton,  90, 92, 204, 205);
            us1[0]=231;
            us1[1]=242;
            us2[0]=243;
            us2[1]=242;
            us3[0]=255;
            us3[1]=242;
            us4[0]=267;
            us4[1]=242;
            us5[0]=279;
            us5[1]=242;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 208, 240);
        }
        if(i==1&&j==2){
            configSquareButton(squareButton,  99, 84, 301, 204);
            us1[0]=306;
            us1[1]=224;
            us2[0]=318;
            us2[1]=224;
            us3[0]=330;
            us3[1]=224;
            us4[0]=342;
            us4[1]=224;
            us5[0]=354;
            us5[1]=224;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 324, 250);
        }
        if(i==1&&j==3){
            configSquareButton(squareButton,  67, 90, 421, 205);
            us1[0]=421;
            us1[1]=279;
            us2[0]=433;
            us2[1]=279;
            us3[0]=445;
            us3[1]=279;
            us4[0]=457;
            us4[1]=279;
            us5[0]=469;
            us5[1]=279;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 461, 249);
        }
        if(i==2&&j==0){
            configSquareButton(squareButton,  84, 79,  112, 313);
            us1[0]=122;
            us1[1]=331;
            us2[0]=134;
            us2[1]=331;
            us3[0]=146;
            us3[1]=331;
            us4[0]=158;
            us4[1]=331;
            us5[0]=170;
            us5[1]=331;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 126, 354);
        }
        if(i==2&&j==1){
            configSquareButton(squareButton,  98, 86, 197, 314);
            us1[0]=215;
            us1[1]=331;
            us2[0]=227;
            us2[1]=331;
            us3[0]=239;
            us3[1]=331;
            us4[0]=251;
            us4[1]=331;
            us5[0]=263;
            us5[1]=331;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 209, 354);
        }
        if(i==2&&j==2){
            configSquareButton(squareButton,  92, 79, 305, 312);
            us1[0]=314;
            us1[1]=331;
            us2[0]=326;
            us2[1]=331;
            us3[0]=338;
            us3[1]=331;
            us4[0]=350;
            us4[1]=331;
            us5[0]=362;
            us5[1]=331;
            hasRespawnPoint=false;
            hasAmmo=true;
            configAmmoButton(ammo,   21, 21, 313, 354);
        }
        if(i==2&&j==3){
            configSquareButton(squareButton,  71, 90, 419, 301);
            us1[0]=421;
            us1[1]=305;
            us2[0]=433;
            us2[1]=305;
            us3[0]=445;
            us3[1]=305;
            us4[0]=457;
            us4[1]=305;
            us5[0]=469;
            us5[1]=305;
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

    private void configAmmoButton(Button button, double prefWidth, double prefHeight, double x, double y){
        //button=new Button();
        button.setPrefWidth(prefWidth*widthScaleFactor);
        button.setPrefHeight(prefHeight*heightScaleFactor);
        button.setLayoutX(x*widthScaleFactor);
        button.setLayoutY(y*heightScaleFactor);
        button.setStyle("-fx-background-color: transparent");
        button.setDisable(true);
    }




}

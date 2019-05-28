package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.RoomColor;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleSquare;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

import static it.polimi.ingsw.model.enums.Direction.*;
import static it.polimi.ingsw.utils.Constants.*;


public class Cli {

    private SimpleBoard board;
    private StringBuilder map = new StringBuilder();
    private final List<String> blankSquare = Collections.singletonList(" ");
    private SimplePlayer me;
    private StringBuilder myPlayerBoard = new StringBuilder();
    private Map<String, String> enemies = new HashMap<>();

    public Cli(SimpleBoard board, SimplePlayer me){
        this.board = board;
        this.me = me;
        buildMap();
        myPlayerBoard = buildPlayerBoard(me);
    }

    public Cli(){}

    public void printMap(){
        System.out.println(map.toString());
    }

    public void chooseBoard(){
        Scanner in = new Scanner(System.in);
        boolean flag = false;

        do {
            Logger.print(BOLD + REVERSE + "BOARD PREFERENCE" + RESET);
            Logger.print("1. Board 1 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("2. Board 2 (" + UNDERLINE + "best for 3 players" + RESET + ")");
            Logger.print("3. Board 3 (" + UNDERLINE + "best for 5 players" + RESET + ")");
            Logger.print("4. Board 4 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("Your choice: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    //send preference
                    flag=true;
                    break;
                case 2:
                    flag=true;
                    break;
                case 3:
                    flag=true;
                    break;
                case 4:
                    flag=true;
                    break;
                default:
                    Logger.log("Invalid choice, retry");
                    Logger.flush();
            }
        }while (!flag);
    }

    public void setEnemies(Map<String, String> enemies) {
        this.enemies = enemies;
    }

    public SimplePlayer getMe() {
        return me;
    }

    public void setMe(SimplePlayer me) {
        this.me = me;
    }

    public void printMarks(SimplePlayer player){
        StringBuilder marks = new StringBuilder("Marks: ");
        for(String mark : player.getMarks()){
            marks.append(enemies.get(mark) + MARK + RESET);
        }
        Logger.print(marks.toString());
    }

    public void printAmmo(SimplePlayer player){
        int redAmmo = 0;
        int blueAmmo = 0;
        int yellowAmmo = 0;

        for(Color color: player.getAmmos()){
            switch(color) {
                case YELLOW:
                    yellowAmmo++;
                    break;
                case BLUE:
                    blueAmmo++;
                    break;
                case RED:
                    redAmmo++;
                    break;
                default:
                    Logger.log("Invalid ammo color");
            }
        }

        Logger.print("Ammo: " + RED_W + SQUARE + " x " + redAmmo + RESET + " " + BLUE_W + SQUARE + " x " + blueAmmo + RESET + " " + YELLOW_W + SQUARE + " x " + yellowAmmo + "\n");
    }

    public StringBuilder buildPlayerBoard(SimplePlayer player){
        StringBuilder playerBoard = new StringBuilder();
        playerBoard.append(String.format("  |>>%s|>%s %n", HAND, GUN));
        if(player.getDamages().isEmpty())
            playerBoard.append("\u25CB \n");
        else playerBoard.append(parseDamages(player));
        playerBoard.append(String.format("%-13s%s%s%n", DROPLET, SKULL, OVERKILL));

        return playerBoard;
    }

    private String parseDamages(SimplePlayer player){
        StringBuilder damages = new StringBuilder();
        for(String droplet : player.getDamages()){
            damages.append(enemies.get(droplet) + DAMAGE + RESET);
        }
        damages.insert(20, " ");
        damages.insert(51, " ");
        damages.insert(102, " ");
        damages.insert(109, " ");
        damages.append("\n");
        return damages.toString();
    }

    private void buildMap(){
        List<List<String>> squareRow = new ArrayList<>();

        map.append(" ");
        for(int i=0; i<board.getBoard()[0].length; i++)
            map.append(String.format("|%7s%-6s", (char) (i +65), " "));
        map.append("|");
        map.append("\n");

        for(int i=0; i<board.getBoard().length; i++){
            for(int j=0; j<board.getBoard()[0].length; j++){
                if(board.getBoard()[i][j]!=null)
                    squareRow.add(buildSquare(i, j));
                else squareRow.add(blankSquare);
            }
            appendRow(squareRow, i);
            squareRow.clear();
        }
    }

    private List<String> buildSquare(int x, int y){
        SimpleSquare building = board.getBoard()[x][y];
        String color = parseColor(building.getRoomColor());
        StringBuilder squareLine = new StringBuilder();
        List<String> squareToString = new ArrayList<>();

        squareLine.append(color);
        squareLine.append(makeRow(building.hasDoor(NORTH), board.sameRoom(building.getBoardIndexes(), NORTH), true));
        squareLine.append(RESET);
        squareToString.add(squareLine.toString());
        squareLine.setLength(0);

        squareToString.addAll(makeColumns(building));

        squareLine.append(color);
        squareLine.append(makeRow(building.hasDoor(SOUTH), board.sameRoom(building.getBoardIndexes(), SOUTH), false));
        squareLine.append(RESET);
        squareToString.add(squareLine.toString());

        return squareToString;
    }

    private String makeRow(boolean door, boolean sameRoom, boolean upperSide){
        if(sameRoom){
            if(upperSide) return USAME_ROOM_SIDE;
            else return DSAME_ROOM_SIDE;
        }
        else if(door){
                if(upperSide) return UDOOR_SIDE;
                else return DDOOR_SIDE;
             }
        else {
            if (upperSide) return UCLASSIC_SIDE;
            else return DCLASSIC_SIDE;
        }
    }

    private List<String> makeColumns(SimpleSquare building) {
        if (building.hasDoor(WEST)) {
            if (building.hasDoor(EAST)) return (buildDD(building));
            else if (board.sameRoom(building.getBoardIndexes(), EAST)) return (buildDR(building));
            else return (buildDW(building));
        } else {
            if (building.hasDoor(EAST)) {
                if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildRD(building));
                else return (buildWD(building));
            } else {
                if (board.sameRoom(building.getBoardIndexes(), EAST)) {
                    if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildRR(building));
                    else return (buildWR(building));
                } else {
                    if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildRW(building));
                    else return (buildWW(building));
                }
            }
        }
    }

    private void appendRow(List<List<String>> row, int nrow){
        int span;
        int count = 0;
        while(row.get(count) == blankSquare)
            count++;
        span = row.get(count).size();

        for(int i=0; i<span; i++) {
            for (List<String> square : row) {
                if(row.indexOf(square) == 0){
                    if(i == span/2)
                        map.append(WHITE_W + nrow + " " + RESET);
                    else map.append("  ");
                }
                if (square == blankSquare)
                    map.append(String.format("%14s", ""));
                else map.append(square.get(i));
            }
            map.append("\n");
        }
    }

    private String parseColor(RoomColor c){
        switch(c){
            case RED: return RED_W;
            case BLUE: return BLUE_W;
            case GREEN: return GREEN_W;
            case WHITE: return WHITE_W;
            case PURPLE: return MAGENTA_W;
            case YELLOW: return YELLOW_W;
            default: throw new IllegalArgumentException("Room color is not valid");
        }
    }

    private String parseColor(Color c){
        switch(c){
            case RED: return RED_W;
            case BLUE: return BLUE_W;
            case YELLOW: return YELLOW_W;
            default: throw new IllegalArgumentException("Room color is not valid");
        }
    }

    private String getAmmo(SimpleSquare ammoSquare){
        StringBuilder ammoTile = new StringBuilder();
        AmmoTile a = ammoSquare.getAmmoTile();
        if(a.hasPowerup())
            ammoTile.append(BACKGROUND_WHITE + BLACK_W + "P" + RESET + parseColor(a.getAmmo(0)) + AMMO + RESET + parseColor(a.getAmmo(1)) + AMMO + RESET);
        else ammoTile.append(parseColor(a.getAmmo(0)) + AMMO + RESET + parseColor(a.getAmmo(1)) + AMMO + RESET + parseColor(a.getAmmo(2)) + AMMO + RESET);
        return ammoTile.toString();
    }

    private List<String> buildDD(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , UL_QUAD , UR_QUAD));
        col.add(String.format(COL_FORMAT, color , " " , " "));
        col.add(String.format(COL_FORMAT, color , " " , " "));
        col.add(String.format(COL_FORMAT, color , DL_QUAD , DR_QUAD));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        return col;
    }

    private List<String> buildDR(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , UL_QUAD , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , " " , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , " " , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , DL_QUAD , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        return col;
    }

    private List<String> buildDW(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , UL_QUAD , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , " " , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , " " , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , DL_QUAD , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        return col;
    }

    private List<String> buildRD(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , UR_QUAD));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , " "));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , " "));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , DR_QUAD));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        return col;
    }

    private List<String> buildRR(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK));
        return col;
    }

    private List<String> buildRW(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK));
        return col;
    }

    private List<String> buildWR(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK));
        return col;
    }

    private List<String> buildWD(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , UR_QUAD));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , " "));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , " "));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , DR_QUAD));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        return col;
    }

    private List<String> buildWW(SimpleSquare b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        col.add(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK));
        return col;
    }
}

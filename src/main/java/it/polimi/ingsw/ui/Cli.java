package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.WeaponSquare;
import it.polimi.ingsw.model.enums.RoomColor;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.enums.Direction.*;
import static it.polimi.ingsw.utils.Constants.*;


public class Cli {

    private GameBoard board;
    private StringBuilder map = new StringBuilder();
    private final List<String> blankSquare = Collections.singletonList(" ");

    public Cli(GameBoard board){
        this.board = board;
        buildMap();
    }

    public void printMap(){
        System.out.println(map.toString());
    }

    private void buildMap(){
        List<List<String>> squareRow = new ArrayList<>();

        for(int i=0; i<board.getMap()[0].length; i++)
            map.append(String.format("|%7s%-6s", (char) (i +65), " "));
        map.append("|");
        map.append("\n");

        for(int i=0; i<board.getMap().length; i++){
            for(int j=0; j<board.getMap()[0].length; j++){
                if(board.getMap()[i][j]!=null)
                    squareRow.add(buildSquare(i, j));
                else squareRow.add(blankSquare);
            }
            appendRow(squareRow);
            squareRow.clear();
        }
    }

    private List<String> buildSquare(int x, int y){
        Square building = board.getMap()[x][y];
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

    private List<String> makeColumns(Square building) {
        if (building.hasDoor(WEST)) {
            if (building.hasDoor(EAST)) return (buildDD(building));
            else if (board.sameRoom(building.getBoardIndexes(), EAST)) return (buildDR(building));
            else return (buildDW(building));
        } else {
            if (building.hasDoor(EAST)) {
                if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildRD(building));
                else return (buildRW(building));
            } else {
                if (board.sameRoom(building.getBoardIndexes(), EAST)) {
                    if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildRR(building));
                    else return (buildWR(building));
                } else {
                    if (board.sameRoom(building.getBoardIndexes(), WEST)) return (buildWD(building));
                    else return (buildWW(building));
                }
            }
        }
    }

    private void appendRow(List<List<String>> row){
        int span;
        int count = 0;
        while(row.get(count) == blankSquare)
            count++;
        span = row.get(count).size();

        for(int i=0; i<span; i++) {
            for (List<String> square : row) {
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

    private List<String> buildDD(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RH_BLOCK));
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

    private List<String> buildDR(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RT_BLOCK));
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

    private List<String> buildDW(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RH_BLOCK));
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

    private List<String> buildRD(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , AMMO , RH_BLOCK));
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

    private List<String> buildRR(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , AMMO , RT_BLOCK));
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

    private List<String> buildRW(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LT_BLOCK , AMMO , RH_BLOCK));
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

    private List<String> buildWR(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RT_BLOCK));
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

    private List<String> buildWD(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RH_BLOCK));
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

    private List<String> buildWW(Square b){
        List<String> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b instanceof WeaponSquare)
            col.add(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK));
        else col.add(String.format(AMMO_FORMAT, color , LH_BLOCK , AMMO , RH_BLOCK));
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

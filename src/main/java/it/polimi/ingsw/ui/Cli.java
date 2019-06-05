package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.RoomColor;
import it.polimi.ingsw.network.client.RMIServerConnection;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.client.SocketServerConnection;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleSquare;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.util.*;

import static it.polimi.ingsw.model.enums.Direction.*;
import static it.polimi.ingsw.utils.Constants.*;


public class Cli implements AdrenalineUI{

    private SimpleBoard board;
    private StringBuilder map = new StringBuilder();
    private final List<StringBuilder> blankSquare = Collections.singletonList(new StringBuilder(" "));
    private SimplePlayer me;
    private StringBuilder myPlayerBoard = new StringBuilder();
    private Map<String, String> enemiesColor = new HashMap<>();
    private Map<String, int[]> enemiesPosition = new HashMap<>();
    private Map<String, int[]> squareOffset = new HashMap<>(); //[row, offset]
    private List<List<List<StringBuilder>>> mapList = new ArrayList<>();
    private ServerConnection serverConnection;

    public Cli(int a){
    }

    public Cli(SimpleBoard board){
        this.board = board;
        buildMap();
    }

    public Cli(String hostname) throws IOException, NotBoundException, URISyntaxException {
        Scanner in = new Scanner(System.in);
        Logger.print("\n" +
                "      ___           ___           ___           ___           ___           ___           ___                   ___           ___     \n" +
                "     /\\  \\         /\\  \\         /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\__\\      ___        /\\__\\         /\\  \\    \n" +
                "    /::\\  \\       /::\\  \\       /::\\  \\       /::\\  \\       /::|  |       /::\\  \\       /:/  /     /\\  \\      /::|  |       /::\\  \\   \n" +
                "   /:/\\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\     /:|:|  |      /:/\\:\\  \\     /:/  /      \\:\\  \\    /:|:|  |      /:/\\:\\  \\  \n" +
                "  /::\\~\\:\\  \\   /:/  \\:\\__\\   /::\\~\\:\\  \\   /::\\~\\:\\  \\   /:/|:|  |__   /::\\~\\:\\  \\   /:/  /       /::\\__\\  /:/|:|  |__   /::\\~\\:\\  \\ \n" +
                " /:/\\:\\ \\:\\__\\ /:/__/ \\:|__| /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\ /:/ |:| /\\__\\ /:/\\:\\ \\:\\__\\ /:/__/     __/:/\\/__/ /:/ |:| /\\__\\ /:/\\:\\ \\:\\__\\\n" +
                " \\/__\\:\\/:/  / \\:\\  \\ /:/  / \\/_|::\\/:/  / \\:\\~\\:\\ \\/__/ \\/__|:|/:/  / \\/__\\:\\/:/  / \\:\\  \\    /\\/:/  /    \\/__|:|/:/  / \\/__\\:\\/:/  /\n" +
                "      \\::/  /   \\:\\  /:/  /     |:|::/  /   \\:\\ \\:\\__\\       |:/:/  /       \\::/  /   \\:\\  \\   \\::/__/         |:/:/  /       \\::/  / \n" +
                "      /:/  /     \\:\\/:/  /      |:|\\/__/     \\:\\ \\/__/       |::/  /        /:/  /     \\:\\  \\   \\:\\__\\         |::/  /        /:/  /  \n" +
                "     /:/  /       \\::/__/       |:|  |        \\:\\__\\         /:/  /        /:/  /       \\:\\__\\   \\/__/         /:/  /        /:/  /   \n" +
                "     \\/__/         ~~            \\|__|         \\/__/         \\/__/         \\/__/         \\/__/                 \\/__/         \\/__/    \n");
        Logger.print("\n" + REVERSE + BOLD + "Benevnuto in ADRENALINA!" + RESET + "\n");
        Logger.print(BOLD + "NOME: " + RESET);
        String name = in.nextLine();
        Logger.print(BOLD + "MOTTO: " + RESET);
        String motto = in.nextLine();
        Logger.print(BOLD + "Ora seleziona il tipo di connessione con cui vorresti giocare:\n" +
                            "1. RMI\n" +
                            "2. SOCKET" + RESET);
        int connection = in.nextInt();
        if(connection == 1)
            serverConnection = new RMIServerConnection(hostname,this);
        else serverConnection = new SocketServerConnection(hostname, this);
        serverConnection.login(name, motto);

        clearScreen();
        Logger.print("Sei in attesa di altri giocatori... \n");
    }

    public void printMap(){
        System.out.println(map.toString());
    }

    public void updateMap(){
        map.setLength(0);
        lettersHead();
        for(List<List<StringBuilder>> row: mapList)
            appendRow(row, mapList.indexOf(row));
    }

    private void lettersHead(){
        map.append(" ");
        for(int i=0; i<board.getBoard()[0].length; i++)
            map.append(String.format("|%7s%-6s", (char) (i +65), " "));
        map.append("|");
        map.append("\n");
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
            }
        }while (!flag);
    }

    public void setEnemiesColor(Map<String, String> enemiesColor) {
        this.enemiesColor = enemiesColor;
    }

    public void setSquareOffset(Map<String, int[]> squareOffset) {
        this.squareOffset = squareOffset;
    }

    public SimplePlayer getMe() {
        return me;
    }

    public void setMe(SimplePlayer me) {
        this.me = me;
    }

    private int[] parseCoordinates(String chess){
        int[] coordinates = new int[2];
        if(chess.length() != 2)
            throw new IllegalArgumentException("Wrong coordinates format");
        if(chess.charAt(0) < 65 || chess.charAt(0) > 122 || (chess.charAt(0) > 90 && chess.charAt(0) < 97))
            throw new IllegalArgumentException("First coordinate must be a letter");
        if((chess.charAt(0) - 97) < 0)
            coordinates[0] = (int) chess.charAt(0) - 65;
        else coordinates[0] = (int) chess.charAt(0) - 97;
        coordinates[1] = Character.getNumericValue(chess.charAt(1));
        return coordinates;
    }

    public void setPlayerPosition(SimplePlayer player, int[] coordinates){

        String playerColor = enemiesColor.get(player.getNickname());
        int [] offsets = squareOffset.get(playerColor);
        StringBuilder spaceFiller = new StringBuilder();
        for (int i = 0; i < (playerColor + PLAYER).length()/2 ; i++){spaceFiller.append(" "); }

        if(!enemiesPosition.containsKey(player.getNickname()))
            enemiesPosition.put(player.getNickname(), coordinates);
        else {
            int[] oldPos = enemiesPosition.get(player.getNickname());
            mapList.get(oldPos[0]).get(oldPos[1]).get(offsets[0]).replace(offsets[1], offsets[1]+spaceFiller.length(), " ");
            enemiesPosition.replace(player.getNickname(), coordinates);
        }
        mapList.get(coordinates[0]).get(coordinates[1]).get(offsets[0]).insert(offsets[1], " ").replace(offsets[1], offsets[1]+spaceFiller.length(), playerColor + PLAYER + RESET + parseColor(board.getBoard()[coordinates[0]][coordinates[1]].getRoomColor()));
        updateMap();
    }

    public void printMarks(SimplePlayer player){
        StringBuilder marks = new StringBuilder("Marks: ");
        for(String mark : player.getMarks()){
            marks.append(enemiesColor.get(mark) + MARK + RESET);
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

        Logger.print("Ammo: " + RED_W + SQUARE + " x " + redAmmo + RESET + " " + BLUE_W + SQUARE + " x " + blueAmmo + RESET + " " + YELLOW_W + SQUARE + " x " + yellowAmmo + RESET + "\n");
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
            damages.append(enemiesColor.get(droplet) + DAMAGE + RESET);
        }
        damages.insert(20, " ");
        damages.insert(51, " ");
        damages.insert(102, " ");
        damages.insert(113, " ");
        damages.append("\n");
        return damages.toString();
    }

    private void buildMap(){
        List<List<StringBuilder>> squareRow = new ArrayList<>();

        lettersHead();

        for(int i=0; i<board.getBoard().length; i++){
            for(int j=0; j<board.getBoard()[0].length; j++){
                if(board.getBoard()[i][j]!=null)
                    squareRow.add(buildSquare(i, j));
                else squareRow.add(blankSquare);
            }
            mapList.add(new ArrayList<>(squareRow));
            appendRow(squareRow, i);
            squareRow.clear();
        }
    }

    private List<StringBuilder> buildSquare(int x, int y){
        SimpleSquare building = board.getBoard()[x][y];
        String color = parseColor(building.getRoomColor());
        StringBuilder squareUpperLine = new StringBuilder();
        StringBuilder squareDownLine = new StringBuilder();
        List<StringBuilder> squareToString = new ArrayList<>();

        squareUpperLine.append(color);
        squareUpperLine.append(makeRow(building.hasDoor(NORTH), board.sameRoom(building.getBoardIndexes(), NORTH), true));
        squareUpperLine.append(RESET);
        squareToString.add(squareUpperLine);

        squareToString.addAll(makeColumns(building));

        squareDownLine.append(color);
        squareDownLine.append(makeRow(building.hasDoor(SOUTH), board.sameRoom(building.getBoardIndexes(), SOUTH), false));
        squareDownLine.append(RESET);
        squareToString.add(squareDownLine);

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

    private List<StringBuilder> makeColumns(SimpleSquare building) {
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

    private void appendRow(List<List<StringBuilder>> row, int nrow){
        int span;
        int count = 0;
        while(row.get(count) == blankSquare)
            count++;
        span = row.get(count).size();

        for(int i=0; i<span; i++) {
            for (List<StringBuilder> square : row) {
                if(row.indexOf(square) == 0){
                    if(i == span/2)
                        map.append(WHITE_W + nrow + " " + RESET);
                    else map.append("  ");
                }
                if (square.toString().equals(blankSquare.toString()))
                    map.append(String.format("%14s", ""));
                else map.append(square.get(i).toString());
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
            ammoTile.append(BACKGROUND_WHITE + BLACK_W + "P" + RESET + parseColor(a.getAmmo(0)) + AMMO + RESET + parseColor(a.getAmmo(1)) + AMMO + RESET + parseColor(ammoSquare.getRoomColor()));
        else ammoTile.append(parseColor(a.getAmmo(0)) + AMMO + RESET + parseColor(a.getAmmo(1)) + AMMO + RESET + parseColor(a.getAmmo(2)) + AMMO + RESET + parseColor(ammoSquare.getRoomColor()));
        return ammoTile.toString();
    }

    private List<StringBuilder> buildDD(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , UL_QUAD , UR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , DL_QUAD , DR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildDR(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , UL_QUAD , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , DL_QUAD , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildDW(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , UL_QUAD , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , " " , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , DL_QUAD , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildRD(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , UR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , DR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildRR(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RT_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RT_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildRW(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LT_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LT_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LT_BLOCK , RH_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildWR(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RT_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RT_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildWD(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , UR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , " ")));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , DR_QUAD)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        return col;
    }

    private List<StringBuilder> buildWW(SimpleSquare b){
        List<StringBuilder> col = new ArrayList<>();
        String color = parseColor(b.getRoomColor());
        if(b.isSpawnPoint())
            col.add(new StringBuilder(String.format(WEAPON_FORMAT, color , LH_BLOCK , GUN , SPAWN , RH_BLOCK)));
        else col.add(new StringBuilder(String.format(AMMO_FORMAT, color , LH_BLOCK , getAmmo(b) , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        col.add(new StringBuilder(String.format(COL_FORMAT, color , LH_BLOCK , RH_BLOCK)));
        return col;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void onJoinRoomAdvise(String nickname) {
        Logger.print(BOLD + nickname + RESET + " si è unito alla partita\n");
    }

    @Override
    public void onExitRoomAdvise(String nickname) {
        Logger.print(BOLD + nickname + RESET + " si è disconnesso\n");
    }

    @Override
    public void onFirstInRoomAdvise() {
        Logger.print(CLAP + " Complimenti! Sei il primo giocatore che avrà il privilegio di giocare il turno! " + CLAP + "\n");
        Logger.print("Questo simobolo sarà accanto alla tua plancia per ridordartelo durante il gioco " + ARROW + " " + FIRST + "\n");
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

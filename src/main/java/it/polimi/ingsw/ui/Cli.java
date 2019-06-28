package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.RoomColor;
import it.polimi.ingsw.model.enums.TargetType;
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
    private Map<String, String> playersColor = new HashMap<>();
    private Map<String, int[]> enemiesPosition = new HashMap<>();
    private Map<String, int[]> squareOffset = new HashMap<>(); //[row, offset]
    private List<List<List<StringBuilder>>> mapList = new ArrayList<>();
    private ServerConnection serverConnection;
    private SimplePlayer me;
    private String name;
    private String motto;
    private List<SimplePlayer> enemies = new ArrayList<>();
    private int playerTurnNumber;
    private boolean frenzyMode;
    private StringBuilder myPBoard;
    private List<Card> powerups = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private Scan runner = new Scan();
    private Thread scanner = new Thread(runner);
    private LimitedQueue<String> actionLog = new LimitedQueue<>(10);
    private Scanner reader = new Scanner(System.in);
    private String playingPlayer;
    private List<String> colors = new ArrayList<>();
    private List<int[]> offsets = new ArrayList<>();

    public Cli(int a){
    }

    public Cli(SimpleBoard board){
        this.board = board;
        buildMap();
    }

    public Cli(String hostname) throws IOException, NotBoundException, URISyntaxException {
        colors.add(RED_W);
        colors.add(BLUE_W);
        colors.add(YELLOW_W);
        colors.add(WHITE_W);
        colors.add(GREEN_W);
        offsets.add(new int[]{3, 10});
        offsets.add(new int[]{4, 16});
        offsets.add(new int[]{6, 10});
        offsets.add(new int[]{7, 16});
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
        name = in.nextLine();
        Logger.print(BOLD + "MOTTO: " + RESET);
        motto = in.nextLine();
        Logger.print(BOLD + "Ora seleziona il tipo di connessione con cui vorresti giocare:\n" +
                            "1. RMI\n" +
                            "2. SOCKET" + RESET);
        int connection = in.nextInt();
        if(connection == 1)
            serverConnection = new RMIServerConnection(hostname,this);
        else serverConnection = new SocketServerConnection(hostname, this);
        serverConnection.login(name, motto);

        Logger.print("Sei in attesa di altri giocatori... \n");
    }

    public void printInterface(){
        updateMap();
        printMap();
        Logger.print(playersColor.get(me.getNickname()) + PLAYER + RESET + " POINTS: " + me.getScore() + "\n");
        printMarks(me);
        printMyPlayerBoard();
        printAmmo(me);
        Logger.print("Armi: " + this.weapons);
        Logger.print("    Potenziamenti: " + this.powerups + "\n");
        printActionLog();
        Logger.print("\n\n");
    }

    private void printActionLog(){
        for(String action: actionLog)
            Logger.print(action);
    }

    private void printMyPlayerBoard(){
        myPBoard = buildPlayerBoard(me);
        Logger.print(myPBoard.toString() + "\n");
    }

    public void printMap(){
        System.out.println(map.toString());
    }

    private void updateMap(){
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

    private void chooseBoard(){
        scanner.start();
        boolean flag = false;

        do {
            Logger.print(BOLD + REVERSE + "BOARD PREFERENCE" + RESET);
            Logger.print("1. Board 1 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("2. Board 2 (" + UNDERLINE + "best for 3 players" + RESET + ")");
            Logger.print("3. Board 3 (" + UNDERLINE + "best for 5 players" + RESET + ")");
            Logger.print("4. Board 4 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("Your choice: ");
            int choice = reader.nextInt();

            switch (choice) {
                case 1:
                    serverConnection.boardPreference(1);
                    flag=true;
                    break;
                case 2:
                    serverConnection.boardPreference(2);
                    flag=true;
                    break;
                case 3:
                    serverConnection.boardPreference(3);
                    flag=true;
                    break;
                case 4:
                    serverConnection.boardPreference(4);
                    flag=true;
                    break;
                default:
                    Logger.log("Invalid choice, retry");
            }
        }while (!flag);
    }

    public void setPlayersColor(Map<String, String> playersColor) {
        this.playersColor = playersColor;
    }

    public void setSquareOffset(Map<String, int[]> squareOffset) {
        this.squareOffset = squareOffset;
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

        String playerColor = playersColor.get(player.getNickname());
        int [] offsetsEx = squareOffset.get(playerColor);
        StringBuilder spaceFiller = new StringBuilder();
        for (int i = 0; i < (playerColor + PLAYER).length()/2 ; i++){spaceFiller.append(" "); }

        if(!enemiesPosition.containsKey(player.getNickname()))
            enemiesPosition.put(player.getNickname(), coordinates);
        else {
            int[] oldPos = enemiesPosition.get(player.getNickname());
            mapList.get(oldPos[0]).get(oldPos[1]).get(offsetsEx[0]).replace(offsetsEx[1], offsetsEx[1]+spaceFiller.length(), " ");
            enemiesPosition.replace(player.getNickname(), coordinates);
        }

        if(playerColor.equals(BLUE_W))
            mapList.get(coordinates[0]).get(coordinates[1]).get(offsetsEx[0]).insert(offsetsEx[1], " ").replace(offsetsEx[1], offsetsEx[1]+spaceFiller.length()-1, playerColor + PLAYER + RESET + parseColor(board.getBoard()[coordinates[0]][coordinates[1]].getRoomColor()));
        else mapList.get(coordinates[0]).get(coordinates[1]).get(offsetsEx[0]).insert(offsetsEx[1], " ").replace(offsetsEx[1], offsetsEx[1]+spaceFiller.length(), playerColor + PLAYER + RESET + parseColor(board.getBoard()[coordinates[0]][coordinates[1]].getRoomColor()));
        updateMap();
    }

    public void printMarks(SimplePlayer player){
        StringBuilder marks = new StringBuilder("Marks: ");
        for(String mark : player.getMarks()){
            marks.append(playersColor.get(mark) + MARK + RESET);
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
        playerBoard.append("\n");
        playerBoard.append(deathAndPoints(player));

        return playerBoard;
    }

    private String deathAndPoints(SimplePlayer player){
        StringBuilder deaths = new StringBuilder();
        int[] points = new int[]{8, 6, 4, 2, 1, 1};
        for(int i=0; i<player.getDeathCounter(); i++)
            deaths.append(RED_W + CROSS + " ");
        deaths.append(BOLD);
        for(int i=player.getDeathCounter(); i<points.length; i++) {
            deaths.append(points[i]);
            deaths.append(" ");
        }
        deaths.append(RESET);
        return deaths.toString();
    }


    private String parseDamages(SimplePlayer player){
        StringBuilder damages = new StringBuilder();
        for(String droplet : player.getDamages()){
            damages.append(playersColor.get(droplet) + DAMAGE + RESET);
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

    /*private String powerupBuilder(Powerup powerup){
        return parseColor(powerup.getColor()) + powerup.getName() + RESET;
    }*/

    /*private String weaponBuilder(Weapon weapon, boolean effects){
        StringBuilder w = new StringBuilder(parseColor(weapon.getAmmo().get(0)) + weapon.getName() + ":\n" );
        if(effects) {
            for (Effect e : weapon.getEffects())
                w.append(DAMAGE + " " + e.getName() + "\n");
        }
        w.append(RESET);
        return w.toString();
    }*/

    /*private Weapon choosingWeapon(){
        for(Weapon weapon: weapons)
            Logger.print(weapons.indexOf(weapon)+1 + ". "  + parseColor(weapon.getAmmo().get(0)) + weapon.getName() + "\n");
        return weapons.get(reader.nextInt() + 1);
    }*/

    private Card choosingPowerup(){
        for(Card powerup: powerups)
            Logger.print((powerups.indexOf(powerup)+1) + ". " + powerup.getName() + "\n");
        return powerups.get(reader.nextInt() - 1);
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
    public void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        stopScan();
        for(SimplePlayer player: players){
            if(player.getNickname().equals(name))
                me=player;
            else enemies.add(player);
        }
        Map<String, String> colorsCreation = new HashMap<>();
        Map<String, int[]> offsetsCreation = new HashMap<>();
        for(SimplePlayer player: players){
            colorsCreation.put(player.getNickname(), colors.get(players.indexOf(player)));
            if(!player.getNickname().equals(me.getNickname()))
                offsetsCreation.put(colors.get(players.indexOf(player)), offsets.get(players.indexOf(player)));
            else offsetsCreation.put(colors.get(players.indexOf(player)), new int[]{5, 13});
        }
        this.setSquareOffset(offsetsCreation);
        this.setPlayersColor(colorsCreation);
        this.playerTurnNumber = playerTurnNumber;
        chooseBoard();
        actionLog.add("Match iniziato!");
    }

    @Override
    public void onInvalidMessageReceived(String msg) {
        stopScan();
        Logger.print("Errore ricezione messaggio\n");
        while(true);
    }

    @Override
    public void onBoardUpdate(SimpleBoard gameBoard) {
        this.board=gameBoard;
        updateMap();
        printMap();
    }

    @Override
    public void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        if(frenzy) {
            actionLog.add("FrenzyMode ON");
            updateActionLog();
        }
        this.board=gameBoard;
        me=players.get(0);
        enemies.clear();
        enemies.addAll(players.subList(1, players.size()-1));
        this.frenzyMode=frenzy;
        updateMap();
        this.myPBoard=buildPlayerBoard(me);
        printInterface();
    }

    @Override
    public void onRespwanRequest(List<Card> powerups) {
        stopScan();
        this.powerups.addAll(powerups);
        serverConnection.respawnPlayer(choosingPowerup());
    }

    @Override
    public void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {
        this.powerups.remove(discardedPowerup);
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.SPAWN);
        printInterface();
    }

    @Override
    public void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        updatePlayer(player, false);
        updateActionLog(player, grabbedTile);
        printInterface();
    }

    @Override
    public void onGrabbedPowerup(SimplePlayer player, Card powerup) {
        if(me.getNickname().equals(player.getNickname()))
            this.powerups.add(powerup);
    }

    @Override
    public void onGrabbableWeapons(List<Card> weapons) {
        stopScan();
        int i;
        for(Card weapon: weapons){
            i = weapons.indexOf(weapon);
            Logger.print(i+1 + ". " + weapon.getName() + "\n");
        }
        Logger.print("Choose weapon: ");
        scanner.start();
        i = reader.nextInt();
        serverConnection.selectWeapon(weapons.get(i-1));
    }

    @Override
    public void onDiscardWeapon(List<Card> weapons) {
        stopScan();
        int i;
        for(Card weapon: weapons)
            Logger.print(weapons.indexOf(weapon)+1 + ". " + weapon.getName() + "\n");
        Logger.print("Choose weapon to discard: ");
        scanner.start();
        i = reader.nextInt();
        serverConnection.discardWeapon(weapons.get(i-1));
    }

    @Override
    public void onGrabbedWeapon(SimplePlayer player, Card weapon) {
        if(me.getNickname().equals(player.getNickname()))
            this.weapons.add((Weapon)weapon);
        else {
            for (SimplePlayer enemy : enemies) {
                if (enemy.getNickname().equals(player.getNickname())) {
                    updateActionLog(enemy, weapon, ActionsLog.GRAB);
                    break;
                }
            }
        }
        printInterface();
    }

    @Override
    public void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        if(me.getNickname().equals(player.getNickname())) {
            me = player;
            this.powerups.removeAll(discardedPowerups);
        }
        for (SimplePlayer enemy : enemies) {
            if (enemy.getNickname().equals(player.getNickname())) {
                this.enemies.set(this.enemies.indexOf(enemy), enemy);
                updateActionLog(enemy, weapon, ActionsLog.LOAD);
                break;
            }
        }
        printInterface();
    }

    @Override
    public void onReloadableWeapons(List<Card> weapons) {
        stopScan();
        int i;
        for(Card weapon: weapons)
            Logger.print((weapons.indexOf(weapon) + 1) + ". " + weapon.getName() + "\n");
        Logger.print("Choose weapon to load: ");
        scanner.start();
        i = reader.nextInt();
        serverConnection.loadableWeapon(weapons.get(i-1));
    }

    @Override
    public void onTurnActions(List<String> actions) {
        stopScan();
        int i=0;
        int choice;
        for(String action: actions) {
            Logger.print((actions.indexOf(action) + 1) + ". " + action + "\n");
            i++;
        }
        Logger.print(i++ + "Informazioni giocatore\n");
        Logger.print(i + "Fine turno");
        scanner.start();
        choice = reader.nextInt();
        if(choice == i)
            serverConnection.closeTurn();
        if(choice == i-1);
            //TODO
        serverConnection.selectAction(actions.get(choice-1));
    }

    @Override
    public void onTurnEnd() {
        stopScan();
        Logger.print("Waiting for your turn...");
    }

    @Override
    public void onMoveAction(SimplePlayer player) {
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.MOVE);
        printInterface();
    }

    @Override
    public void onMoveRequest(MatrixHelper matrix, String targetPlayer) {
        stopScan();
        Logger.print("Scegli dove vuoi spostare " + targetPlayer + ": " + squareList(matrix));
        String coordinates = reader.nextLine();
        serverConnection.movePlayer(targetPlayer, parseCoordinates(coordinates));
    }

    @Override
    public void onMarkAction(String player, SimplePlayer selected, int value) {
        updatePlayer(selected, false);
        updateActionLog(selected, player, value, ActionsLog.MARK);
        printInterface();
    }

    @Override
    public void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        updatePlayer(selected, false);
        updateActionLog(selected, player, damageValue, ActionsLog.SHOOT);
        printInterface();
    }

    @Override
    public void onDiscardedPowerup(SimplePlayer player, Card powerup) {
        if(player.getNickname().equals(me.getNickname()))
            this.powerups.remove(powerup);
        updateActionLog(player, powerup, ActionsLog.DISCARD);
        printInterface();
    }

    @Override
    public void onTurnCreation(String currentPlayer) {
        playingPlayer=currentPlayer;
        updateActionLog(currentPlayer);
        printInterface();
    }

    @Override
    public void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        stopScan();
        List<Integer> choice;
        List<List<String>> answer = new ArrayList<>();
        int counter = 0;
        scanner.start();
        switch (target.getType()){
            case PLAYER:
                Logger.print("Scegli chi colpire dalla seguente lista di  giocatori: \n");
                for(String player: selectable.get(0))
                    Logger.print(counter++ + ". " + player + "\n");
                choice = playerHandleChoice(target);
                List<String> playerAnswer = new ArrayList<>();
                for(Integer pick: choice)
                    playerAnswer.add(selectable.get(0).get(pick));
                answer.add(playerAnswer);
                break;
            case ROOM:
                for(List<String> set: selectable){
                    Logger.print(counter++ + ". " + set.toString() + "\n");
                }
                choice = roomHandlerChoice();
                answer.add(selectable.get(choice.get(0)));
                break;
            default:
                if(target.getType()==TargetType.ME)
                    throw new IllegalArgumentException("Cannot resolve ME target");
                for(List<String> set: selectable){
                    Logger.print(counter++ + ". " + set.toString() + "\n");
                }
                answer = squareHandlerChoice(target, selectable);
        }
        serverConnection.selectPlayers(answer);
    }

    private List<List<String>> squareHandlerChoice(SimpleTarget target, List<List<String>> selectable){
        List<List<String>> choice = new ArrayList<>();
        List<Integer> squares = new ArrayList<>();
        boolean rightChoice = false;
        do {
            if(target.getType() == TargetType.SQUARE)
                Logger.print("Scegli quali quadrati vuoi colpire: \n");
            else Logger.print("Scegli quale direzione vuoi colpire: \n");
            while (reader.hasNext())
                squares.add(reader.nextInt());
            if(squares.size()>target.getMinNumber() && (squares.size()<target.getMaxNumber() || target.getMaxNumber()==-1) /*TODO checking correctness*/)
                rightChoice = true;
        }while(!rightChoice);
        if(target.getMaxPlayerIn()>0)
            choice = inSquareChoice(target, selectable, squares);
        else for(Integer square: squares)
                choice.add(selectable.get(square));
        return choice;
    }

    private List<List<String>> inSquareChoice(SimpleTarget target, List<List<String>> selectable, List<Integer> squares){
        List<List<String>> answer = new ArrayList<>();
        for(Integer square: squares) {
            if(selectable.get(square).size()>target.getMaxPlayerIn()) {
               answer.add(playerInSquare(target, selectable, square));
            }
            else answer.add(selectable.get(square));
        }
        return answer;
    }

    private List<String> playerInSquare(SimpleTarget target, List<List<String>> selectable, int square){
        List<Integer> choiceInSquare = new ArrayList<>();
        List<String> playerPicked = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choiceInSquare.clear();
            int counter = 0;
            if(target.getType() == TargetType.SQUARE)
                Logger.print("Scegli chi colpire nel quadrato [Minimo " + target.getMinPlayerIn() + " e massimo " + target.getMaxPlayerIn() + "]:\n");
            else Logger.print("Scegli chi colpire [Minimo " + target.getMinPlayerIn() + " e massimo " + target.getMaxPlayerIn() + "]:\n");
            for(String player: selectable.get(square))
                Logger.print(counter++ + ". " + player + "\n");
            while (reader.hasNext())
                choiceInSquare.add(reader.nextInt());
            if(choiceInSquare.size()>target.getMinPlayerIn() && choiceInSquare.size()<target.getMaxPlayerIn()  /*TODO checking correctness*/)
                rightChoice = true;
        } while (!rightChoice);
        for(Integer pick: choiceInSquare)
            playerPicked.add(selectable.get(square).get(pick));
        return playerPicked;
    }

    private List<Integer> playerHandleChoice(SimpleTarget target){
        List<Integer> choice = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choice.clear();
            Logger.print("[Minimo " + target.getMinPlayerIn() + "giocatori e massimo ");
            Logger.print( (target.getMaxPlayerIn()>0) ? Integer.toString(target.getMaxPlayerIn()) : "-" + "]\n");
            while (reader.hasNext())
                choice.add(reader.nextInt());
            if(choice.size()>target.getMinPlayerIn() && (choice.size()<target.getMaxPlayerIn() || target.getMaxPlayerIn()==-1) /*TODO checking correctness*/)
                rightChoice = true;
        }while(!rightChoice);
        return choice;
    }

    private List<Integer> roomHandlerChoice(){
        List<Integer> choice = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choice.clear();
            Logger.print("Scegli la stanza che vuoi colpire: \n");
            choice.add(reader.nextInt());
            //TODO if(check correctness)
            rightChoice = true;
        }while(!rightChoice);
        return choice;
    }

    @Override
    public void onCanUsePowerup() {
        stopScan();
        Logger.print("Puoi usare un powerup in base alla situazione di gioco corrente, vuoi usufruirne?\n [S/N]");
        scanner.start();
        String choice = reader.nextLine();
        serverConnection.usePowerup(choice.equals("S"));
    }

    @Override
    public void onCanStopRoutine() {
        stopScan();
        Logger.print("L'arma che stai usando ha effetti opzionali, vuoi usarli?\n [S/N]");
        scanner.start();
        String choice = reader.nextLine();
        serverConnection.stopRoutine(choice.equals("S"));
    }

    @Override
    public void onUsableWeapons(List<Card> usableWeapons) {
        serverConnection.selectWeapon(availableCard(usableWeapons));
    }

    @Override
    public void onAvailableEffects(List<String> effects) {
        stopScan();
        for(String effect: effects)
            Logger.print(effects.indexOf(effect)+1 + ". " + effect + "\n");
        Logger.print("Scegli l'effetto da usare: ");
        scanner.start();
        int i = reader.nextInt();
        serverConnection.selectEffect(effects.get(i-1));
    }

    @Override
    public void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        if(player.getNickname().equals(me.getNickname()))
            this.powerups.removeAll(discardedPowerups);
        updatePlayer(player, false);
    }

    @Override
    public void onUsedCard(Card card) {
        updateActionLog(card);
        printInterface();
    }

    @Override
    public void onAvailablePowerups(List<Card> powerups) {
        serverConnection.selectPowerup(availableCard(powerups));
    }

    @Override
    public void onRunCompleted(SimplePlayer player, int[] newPosition) {
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.MOVE);
    }

    @Override
    public void onRunRoutine(MatrixHelper matrix) {
        stopScan();
        Logger.print("Scegli dove vuoi muoverti: " + squareList(matrix));
        String coordinates = reader.nextLine();
        serverConnection.runAction(parseCoordinates(coordinates));
    }

    private void updateActionLog(SimplePlayer player, Card weapon, ActionsLog actionsLog){
        String a;
        switch(actionsLog){
            case USE:
                a=" has used ";
                break;
            case LOAD:
                a=" has loaded ";
                break;
            case GRAB:
                a=" has grabbed ";
                break;
            case DISCARD:
                a=" ha scartato ";
                break;
            default:
                a=" ";
        }
         actionLog.add(player.getNickname() + a + weapon.getName());
    }

    private void updateActionLog(SimplePlayer player, Card powerup){
        actionLog.add(player.getNickname() + " has used "+ powerup.getName());
    }

    private void updateActionLog(SimplePlayer player, String giver, int quantity, ActionsLog action){
        if(action.equals(ActionsLog.MARK))
            actionLog.add(player.getNickname() + " has been marked " + quantity + "times from " + giver);
        else actionLog.add(giver + " has dealt " + quantity + " damages to " + player.getNickname());
    }

    private void updateActionLog(){
        actionLog.add("Modalità frenesia attiva!");
    }

    private void updateActionLog(String player){
        actionLog.add(player + " has started the turn");
    }

    private void updateActionLog(Card card){
        actionLog.add(card.getName() + " è stato/a utilizzato/a");
    }

    private void updateActionLog(SimplePlayer player, ActionsLog actionsLog){
        String a;
        switch(actionsLog){
            case LOGOUT:
                a=" logged out ";
                break;
            case MOVE:
                a=" moved to " + Arrays.toString(player.getPosition());
                break;
            case SPAWN:
                a=" spawned in " + parseColor(board.getBoard()[player.getPosition()[0]][player.getPosition()[1]].getRoomColor()) + "ROOM" + RESET;
                break;
            default:
                a=" ";
        }
        actionLog.add(player.getNickname() + a);
    }

    private void updateActionLog(SimplePlayer player, AmmoTile ammoTile){
        StringBuilder a = new StringBuilder();
        if(ammoTile.hasPowerup())
            a.append(BACKGROUND_WHITE + BLACK_W + "P" + RESET + parseColor(ammoTile.getAmmo(0)) + AMMO + RESET + parseColor(ammoTile.getAmmo(1)) + AMMO + RESET );
        else a.append(parseColor(ammoTile.getAmmo(0)) + AMMO + RESET + parseColor(ammoTile.getAmmo(1)) + AMMO + RESET + parseColor(ammoTile.getAmmo(2)) + AMMO + RESET );
        actionLog.add(player.getNickname() + " has grabbed " + a.toString());
    }

    private void updatePlayer(SimplePlayer player, boolean updatePos){
        if(me.getNickname().equals(player.getNickname())){
            me=player;
            if(updatePos)
                setPlayerPosition(me, me.getPosition());
        }
        else for(SimplePlayer enemy: enemies) {
            if (enemy.getNickname().equals(player.getNickname())) {
                enemies.set(enemies.indexOf(enemy), player);
                if(updatePos)
                    setPlayerPosition(enemy, enemy.getPosition());
                break;
            }
        }
    }

    private Card availableCard(List<Card> cards){
        stopScan();
        for(Card card: cards)
            Logger.print((cards.indexOf(card)+1) + ". " + card.getName() + "\n");
        Logger.print("Scegli la carta da usare: ");
        scanner.start();
        int i = reader.nextInt();
        return cards.get(i-1);
    }

    private String squareList(MatrixHelper matrix){
        StringBuilder squareList = new StringBuilder();
        for (int i = 0; i < matrix.getRowLength(); i++)
            for (int j = 0 ; j < matrix.getColLength(); j++)
                if(matrix.toBooleanMatrix()[i][j])
                    squareList.append("" + (j+65) + i + " ");
                return squareList.toString();
    }

    private void stopScan(){
        if(scanner.isAlive())
            runner.doStop();
    }

    public class Scan implements Runnable {

        private boolean doStop = false;

        synchronized void doStop() {
            this.doStop = true;
            reader.close();
        }

        private synchronized boolean keepRunning() {
            return !this.doStop;
        }

        @Override
        public void run() {
            while(keepRunning()) {
                try {
                    Thread.sleep(3L * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

            }
        }
    }

    public class LimitedQueue<E> extends LinkedList<E> {
        private int limit;

        LimitedQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) { super.remove(); }
            return true;
        }
    }
}

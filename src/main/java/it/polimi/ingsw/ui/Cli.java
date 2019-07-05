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
import it.polimi.ingsw.utils.ConsoleInput;
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
    private boolean frenzyMode;
    private StringBuilder myPBoard;
    private List<Card> powerups = new ArrayList<>();
    private List<Card> weapons = new ArrayList<>();
    private LimitedQueue<String> actionLog = new LimitedQueue<>(10);
    private List<String> colors = new ArrayList<>();
    private List<int[]> offsets = new ArrayList<>();
    private ConsoleInput reader = new ConsoleInput();

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
        int connection = Integer.parseInt(in.nextLine());
        if(connection == 1 )
            serverConnection = new RMIServerConnection(hostname,this);
        else serverConnection = new SocketServerConnection(hostname, this);
        serverConnection.login(name, motto);

        Logger.print("Sei in attesa di altri giocatori... \n");
    }

    private synchronized void printInterface(){
        printKillsToGo();
        updateMap();
        printMap();
        printWeaponsInMap();
        Logger.print(playersColor.get(me.getNickname()) + PLAYER + RESET + " POINTS: " + me.getScore() + "   \"" + motto + "\"" + "\n");
        printMarks(me);
        printMyPlayerBoard();
        printAmmo(me);
        Logger.print("Armi: " + weaponString(me) + "\n");
        Logger.print("Potenziamenti: " + powerupString() + "\n");
        printActionLog();
        Logger.print("\n\n");
    }

    private void printWeaponsInMap(){
        List<String> weaponsList = new ArrayList<>();
        for(SimpleSquare square: this.board.getSpawnPoints()) {
            Logger.print(parseColor(square.getRoomColor()) + "ROOM: " + RESET);
            for (Card weapon : square.getWeaponCards())
                weaponsList.add(weapon.getName());
            Logger.print(weaponsList.toString() + "\n");
            weaponsList.clear();
        }
        Logger.print("\n");
    }

    private String weaponString(SimplePlayer player){
        StringBuilder weaponsList = new StringBuilder();
        for(Card weapon: player.getWeaponCards()) {
            if(player.getNotLoadedIds().contains(weapon.getId()))
                weaponsList.append(REVERSE).append(weapon.getName()).append(", " + RESET);
            else weaponsList.append(weapon.getName()).append(", ");
        }
        return weaponsList.toString();
    }

    private String powerupString(){
        StringBuilder powList = new StringBuilder();
        for(Card powerup: this.powerups)
            powList.append(powerup.getName()).append(", ");
        return powList.toString();
    }

    private void printKillsToGo(){
        if(this.frenzyMode)
            Logger.print(RED_W + REVERSE + "FRENESIA");
        for(int i=0; i<this.board.getKillshotTrack().size(); i++)
            Logger.cmd(playersColor.get(this.board.getKillshotTrack().get(i)) + DROPLET + (this.board.getOverkillTrack().get(i)? "x2 " : " ") + RESET);
        for(int i=this.board.getSkullNumber(); i>this.board.getKillshotTrack().size(); i--)
                Logger.cmd(RED_W + SKULL + " ");
        Logger.print(RESET + "\n");
    }

    private void printActionLog(){
        for(String action: actionLog)
            Logger.print(action);
    }

    private void printMyPlayerBoard(){
        myPBoard = buildPlayerBoard(me);
        Logger.print(myPBoard.toString() + "\n");
    }

    private void printMap(){
        System.out.println(map.toString());
    }

    private void updateMap(){
        map.setLength(0);
        lettersHead();
        for(List<List<StringBuilder>> row: mapList)
            appendRow(row, mapList.indexOf(row));
        for(SimplePlayer simplePlayer: this.enemies)
            updatePlayer(simplePlayer, true);
        updatePlayer(me, true);
    }

    private void lettersHead(){
        map.append(" ");
        for(int i=0; i<board.getBoard()[0].length; i++)
            map.append(String.format("|%7s%-6s", (char) (i +65), " "));
        map.append("|");
        map.append("\n");
    }

    private void chooseBoard(){

        boolean flag = false;

        do {
            Logger.print(BOLD + REVERSE + "BOARD PREFERENCE" + RESET);
            Logger.print("1. Board 1 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("2. Board 2 (" + UNDERLINE + "best for 3 players" + RESET + ")");
            Logger.print("3. Board 3 (" + UNDERLINE + "best for 5 players" + RESET + ")");
            Logger.print("4. Board 4 (" + UNDERLINE + "best for 4 players" + RESET + ")");
            Logger.print("Your choice: ");
            int choice = Integer.parseInt(reader.readLine());

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

    private void setPlayersColor(Map<String, String> playersColor) {
        this.playersColor = playersColor;
    }

    private void setSquareOffset(Map<String, int[]> squareOffset) {
        this.squareOffset = squareOffset;
    }

    private int[] parseCoordinates(String chess){
        int[] coordinates = new int[2];
        if(chess.length() != 2)
            throw new IllegalArgumentException("Wrong coordinates format");
        if(chess.charAt(0) < 65 || chess.charAt(0) > 122 || (chess.charAt(0) > 90 && chess.charAt(0) < 97))
            throw new IllegalArgumentException("First coordinate must be a letter");
        if((chess.charAt(0) - 97) < 0)
            coordinates[1] = (int) chess.charAt(0) - 65;
        else coordinates[1] = (int) chess.charAt(0) - 97;
        coordinates[0] = Character.getNumericValue(chess.charAt(1));
        return coordinates;
    }

    private void setPlayerPosition(SimplePlayer player, int[] coordinates){

        String playerColor = playersColor.get(player.getNickname());
        int[] offsetsEx = squareOffset.get(playerColor);
        StringBuilder spaceFiller = new StringBuilder();
        for (int i = 0; i < (playerColor + PLAYER).length()/2 ; i++){spaceFiller.append(" "); }

        if(!enemiesPosition.containsKey(player.getNickname()))
            enemiesPosition.put(player.getNickname(), coordinates);
        else {
            int[] oldPos = enemiesPosition.get(player.getNickname());
            mapList.get(oldPos[0]).get(oldPos[1]).get(offsetsEx[0]).replace(offsetsEx[1], offsetsEx[1]+(playerColor + PLAYER + RESET + parseColor(board.getBoard()[coordinates[0]][coordinates[1]].getRoomColor())).length(), "  ");
            enemiesPosition.replace(player.getNickname(), coordinates);
        }

        mapList.get(coordinates[0]).get(coordinates[1]).get(offsetsEx[0]).insert(offsetsEx[1], " ").replace(offsetsEx[1], offsetsEx[1]+spaceFiller.length(), playerColor + PLAYER + RESET + parseColor(board.getBoard()[coordinates[0]][coordinates[1]].getRoomColor()));
    }

    private void printMarks(SimplePlayer player){
        StringBuilder marks = new StringBuilder("Marks: ");
        for(String mark : player.getMarks()){
            marks.append(playersColor.get(mark)).append(MARK).append(RESET);
        }
        Logger.print(marks.toString());
    }

    private void printAmmo(SimplePlayer player){
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

    private void playerInfoRequest(){
        Logger.print("\n");
        int counter=1;
        String choice;
        List<String> list = new ArrayList<>();
        boolean rightChoice = false;
        do {
            for (SimplePlayer enemy : enemies) {
                if (enemy.getNickname().equals(me.getNickname()))
                    continue;
                list.add(String.valueOf(counter));
                Logger.print(counter++ + ". " + enemy.getNickname());
            }
            reader.cancel();
            choice = reader.readLine();
            if(correctInput(list, choice))
                rightChoice=true;
        }while (!rightChoice);
        printPlayerInfo(enemies.get(Integer.parseInt(choice)-1));
    }

    private void printPlayerInfo(SimplePlayer player){
        Logger.print(playersColor.get(player.getNickname()) + PLAYER + RESET + " POINTS: " + player.getScore() + "    \"" + player.getMotto() + "\"" + "\n");
        printMarks(player);
        Logger.print(buildPlayerBoard(player).toString() + "\n");
        printAmmo(player);
        Logger.print("Powerup: " + player.getPowerupCards().size());
    }

    private StringBuilder buildPlayerBoard(SimplePlayer player){
        StringBuilder playerBoard = new StringBuilder();
        if(this.frenzyMode) {
            if(player.isFirst())
                playerBoard.append(FIRST + "\n");
            if(player.getDamages().isEmpty())
                playerBoard.append("\u25CB \n");
            else playerBoard.append(parseDamages(player));
            playerBoard.append(String.format("%-13s%s%n", SKULL, OVERKILL));
            playerBoard.append("\n");
            playerBoard.append(deathAndPoints(player));
        }
        else {
            if (player.isFirst())
                playerBoard.append(FIRST + "\n");
            playerBoard.append(String.format("  |>>%s|>%s %n", HAND, GUN));
            if (player.getDamages().isEmpty())
                playerBoard.append("\u25CB \n");
            else playerBoard.append(parseDamages(player));
            playerBoard.append(String.format("%-13s%s%s%n", DROPLET, SKULL, OVERKILL));
            playerBoard.append("\n");
            playerBoard.append(deathAndPoints(player));
        }
        return playerBoard;
    }

    private String deathAndPoints(SimplePlayer player){
        StringBuilder deaths = new StringBuilder();
        int[] points;
        if(this.frenzyMode)
            points = new int[]{5, 1, 1, 1};
        else points = new int[]{8, 6, 4, 2, 1, 1};
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
            for (String droplet : player.getDamages()) {
                damages.append(playersColor.get(droplet)).append(DAMAGE).append(RESET);
            }
            if(damages.length()>20)
                damages.insert(20, " ");
            if(damages.length()>51)
                damages.insert(51, " ");
            if(damages.length()>102)
                damages.insert(102, " ");
            if(damages.length()>113)
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
                        map.append(WHITE_W).append(nrow).append(" ").append(RESET);
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
            ammoTile.append(BACKGROUND_WHITE + BLACK_W + "P" + RESET).append(parseColor(a.getAmmo(0))).append(AMMO).append(RESET).append(parseColor(a.getAmmo(1))).append(AMMO).append(RESET).append(parseColor(ammoSquare.getRoomColor()));
        else ammoTile.append(parseColor(a.getAmmo(0))).append(AMMO).append(RESET).append(parseColor(a.getAmmo(1))).append(AMMO).append(RESET).append(parseColor(a.getAmmo(2))).append(AMMO).append(RESET).append(parseColor(ammoSquare.getRoomColor()));
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

    private Card choosingPowerup(List<Card> pow, List<Color> colors){
        int choice;
        boolean rightChoice = false;
        List<String> list = new ArrayList<>();
        do {
            for (Card powerup : pow) {
                Logger.print((pow.indexOf(powerup) + 1) + ". " + parseColor(colors.get(pow.indexOf(powerup))) + powerup.getName() + RESET + "\n");
                list.add(String.valueOf(pow.indexOf(powerup) + 1));
            }
            choice = Integer.parseInt(reader.readLine()) - 1;
            if(correctInput(list, String.valueOf(choice+1)))
                rightChoice=true;
        }while (!rightChoice);
        return pow.get(choice);
    }

    @Override
    public synchronized void onJoinRoomAdvise(String nickname) {
        Logger.print(BOLD + nickname + RESET + " si è unito alla partita\n");
    }

    @Override
    public synchronized void onExitRoomAdvise(String nickname) {
        Logger.print(BOLD + nickname + RESET + " si è disconnesso\n");
    }

    @Override
    public synchronized void onFirstInRoomAdvise() {
        Logger.print(CLAP + " Complimenti! Sei il primo giocatore che avrà il privilegio di giocare il turno! " + CLAP + "\n");
        Logger.print("Questo simobolo sarà accanto alla tua plancia per ricordartelo durante il gioco " + ARROW + " " + FIRST + "\n");
    }

    @Override
    public synchronized void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        reader.cancel();
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
        actionLog.add("Match iniziato!");
        chooseBoard();
    }

    @Override
    public synchronized void onInvalidMessageReceived(String msg) {
        reader.cancel();
        Logger.print("Errore ricezione messaggio\n");
        while(true);
    }

    @Override
    public synchronized void onBoardUpdate(SimpleBoard gameBoard) {
        if(this.board==null){
            this.board=gameBoard;
            buildMap();
            printMap();
        }
        this.board=gameBoard;
    }

    @Override
    public synchronized void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        if(frenzy) {
            this.frenzyMode=true;
            actionLog.add("FrenzyMode ON");
            updateActionLog();
        }
        this.board=gameBoard;
        for(SimplePlayer player: players)
            updatePlayer(player, false);
        this.frenzyMode=frenzy;
        updateMap();
        this.myPBoard=buildPlayerBoard(me);
    }

    @Override
    public synchronized void onRespwanRequest(List<Card> powerups, List<Color> colors) {
        reader.cancel();
        this.powerups.addAll(powerups);
        Logger.print("Scegli un powerup da scartare per resuscitare: \n");
        Card powerup = choosingPowerup(powerups, colors);
        this.powerups.remove(powerup);
        serverConnection.respawnPlayer(powerup);
    }

    @Override
    public synchronized void onRespwanCompleted(SimplePlayer player, Card discardedPowerup, Color color) {
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.SPAWN);
        printInterface();
    }

    @Override
    public synchronized void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        updatePlayer(player, false);
        updateActionLog(player, grabbedTile);
        printInterface();
    }

    @Override
    public synchronized void onGrabbedPowerup(SimplePlayer player, Card powerup,Color color) {
        if(me.getNickname().equals(player.getNickname()))
            this.powerups.add(powerup);
        updatePlayer(player, false);
    }

    @Override
    public synchronized void onGrabbableWeapons(List<Card> weapons) {
        reader.cancel();
        int i;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            for (Card weapon : weapons) {
                i = weapons.indexOf(weapon);
                Logger.print((i + 1) + ". " + weapon.getName() + "\n");
                list.add(String.valueOf(i+1));
            }
            Logger.print("Choose weapon: ");

            i = Integer.parseInt(reader.readLine());
            if(correctInput(list, String.valueOf(i)))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.selectWeapon(weapons.get(i-1));
    }

    @Override
    public synchronized void onDiscardWeapon(List<Card> weapons) {
        serverConnection.discardWeapon(availableCard(weapons, ActionsLog.TO_DISCARD));
    }

    @Override
    public synchronized void onGrabbedWeapon(SimplePlayer player, Card weapon) {
        if(me.getNickname().equals(player.getNickname())) {
            this.weapons.add(weapon);
            updatePlayer(me, false);
        }
        else {
            for (SimplePlayer enemy : enemies) {
                if (enemy.getNickname().equals(player.getNickname())) {
                    updatePlayer(enemy, false);
                    updateActionLog(enemy, weapon, ActionsLog.GRAB);
                    break;
                }
            }
        }
        printInterface();
    }

    @Override
    public synchronized void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        if(me.getNickname().equals(player.getNickname())) {
            me = player;
            this.powerups.removeAll(discardedPowerups);
            updatePlayer(player, false);
        }
        for (SimplePlayer enemy : enemies) {
            if (enemy.getNickname().equals(player.getNickname())) {
                this.enemies.set(this.enemies.indexOf(enemy), enemy);
                updatePlayer(enemy, false);
                updateActionLog(enemy, weapon, ActionsLog.LOAD);
                break;
            }
        }
        printInterface();
    }

    @Override
    public synchronized void onReloadableWeapons(List<Card> weapons) {
        serverConnection.loadableWeapon(availableCard(weapons, ActionsLog.TO_LOAD));
    }

    @Override
    public synchronized void onTurnActions(List<String> actions) {
        reader.cancel();
        int i=1;
        int choice;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            Logger.print("Scegli l'azione da fare: ");
            for (String action : actions) {
                Logger.print((actions.indexOf(action) + 1) + ". " + action + "\n");
                list.add(String.valueOf(i++));
            }
            list.add(String.valueOf(i));
            Logger.print(i + ". Informazioni giocatore\n");

            choice = Integer.parseInt(reader.readLine()) - 1;
            if(choice == i-1)
                playerInfoRequest();
            else if(correctInput(list, String.valueOf(choice+1)))
                rightChoice=true;
        }while (!rightChoice);
        serverConnection.selectAction(actions.get(choice));
    }

    @Override
    public void onTurnEnd() {
        reader.cancel();
        serverConnection.closeTurn();
        Logger.print("Waiting for your turn...");
    }

    @Override
    public synchronized void onMoveAction(SimplePlayer player) {
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.MOVE);
        printInterface();
    }

    @Override
    public synchronized void onMoveRequest(MatrixHelper matrix, String targetPlayer) {
        reader.cancel();
        String coordinates;
        boolean rightChoice=false;
        do {
            Logger.print("Scegli dove vuoi spostare " + targetPlayer + "[<lettera><numero>]: " + squareList(matrix));
            coordinates = reader.readLine();
            if(correctInput(squareList(matrix), coordinates))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.movePlayer(targetPlayer, parseCoordinates(coordinates));
    }

    @Override
    public synchronized void onMarkAction(String player, SimplePlayer selected, int value) {
        updatePlayer(selected, false);
        updateActionLog(selected, player, value, ActionsLog.MARK);
        printInterface();
    }

    @Override
    public synchronized void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        updatePlayer(selected, false);
        updateActionLog(selected, player, damageValue, ActionsLog.SHOOT);
        printInterface();
    }

    @Override
    public synchronized void onDiscardedPowerup(SimplePlayer player, Card powerup) {
        if(player.getNickname().equals(me.getNickname()))
            this.powerups.remove(powerup);
        updatePlayer(player, false);
        updateActionLog(player, powerup, ActionsLog.DISCARD);
        printInterface();
    }

    @Override
    public synchronized void onTurnCreation(String currentPlayer) {
        updateActionLog(currentPlayer);
    }

    @Override
    public synchronized void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        reader.cancel();
        List<Integer> choice;
        List<String> list = new ArrayList<>();
        List<List<String>> answer = new ArrayList<>();
        int counter = 1;

        switch (target.getType()){
            case PLAYER:
                Logger.print("Scegli chi colpire dalla seguente lista di  giocatori: \n");
                for(String player: selectable.get(0)) {
                    list.add(String.valueOf(counter));
                    Logger.print(counter++ + ". " + player + "\n");
                }
                choice = playerHandleChoice(target, list);
                List<String> playerAnswer = new ArrayList<>();
                for(Integer pick: choice)
                    playerAnswer.add(selectable.get(0).get(pick-1));
                answer.add(playerAnswer);
                break;
            case ROOM:
                for(List<String> set: selectable){
                    list.add(String.valueOf(counter));
                    Logger.print(counter++ + ". " + set.toString() + "\n");
                }
                choice = roomHandlerChoice(list);
                answer.add(selectable.get(choice.get(0)-1));
                break;
            default:
                if(target.getType()==TargetType.ME)
                    throw new IllegalArgumentException("Cannot resolve ME target");
                for(List<String> set: selectable){
                    list.add(String.valueOf(counter));
                    Logger.print(counter++ + ". " + set.toString() + "\n");
                }
                answer = squareHandlerChoice(target, selectable, list);
        }
        serverConnection.selectPlayers(answer);
    }

    private List<List<String>> squareHandlerChoice(SimpleTarget target, List<List<String>> selectable, List<String> list){
        List<List<String>> choice = new ArrayList<>();
        List<Integer> squares;
        boolean rightChoice = false;
        do {
            if(target.getType() == TargetType.SQUARE)
                Logger.print("Scegli quali quadrati vuoi colpire: \n");
            else Logger.print("Scegli quale direzione vuoi colpire: \n");
            squares=integerParsing();
            if(squares.size()>=target.getMinNumber() && (squares.size()<=target.getMaxNumber() || target.getMaxNumber()==-1) && correctInput(list, squares))
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
            if(selectable.get(square-1).size()>=target.getMaxPlayerIn()) {
               answer.add(playerInSquare(target, selectable, square-1));
            }
            else answer.add(selectable.get(square-1));
        }
        return answer;
    }

    private List<String> playerInSquare(SimpleTarget target, List<List<String>> selectable, int square){
        List<Integer> choiceInSquare = new ArrayList<>();
        List<String> playerPicked = new ArrayList<>();
        List<String> list = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choiceInSquare.clear();
            int counter = 1;
            if(target.getType() == TargetType.SQUARE)
                Logger.print("Scegli chi colpire nel quadrato [Minimo " + target.getMinPlayerIn() + " e massimo " + target.getMaxPlayerIn() + "]:\n");
            else Logger.print("Scegli chi colpire [Minimo " + target.getMinPlayerIn() + " e massimo " + target.getMaxPlayerIn() + "]:\n");
            for(String player: selectable.get(square)) {
                list.add(String.valueOf(counter));
                Logger.print(counter++ + ". " + player + "\n");
            }
            choiceInSquare=integerParsing();
            if(choiceInSquare.size()>=target.getMinPlayerIn() && choiceInSquare.size()<=target.getMaxPlayerIn() && correctInput(list, choiceInSquare))
                rightChoice = true;
        } while (!rightChoice);
        for(Integer pick: choiceInSquare)
            playerPicked.add(selectable.get(square).get(pick-1));
        return playerPicked;
    }

    private List<Integer> playerHandleChoice(SimpleTarget target, List<String> list){
        List<Integer> choice = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choice.clear();
            Logger.print("[Minimo " + target.getMinPlayerIn() + " giocatori e massimo " + target.getMaxPlayerIn());
            Logger.print( ((target.getMaxPlayerIn()>0) ? Integer.toString(target.getMaxPlayerIn()) : "-" )+ "]\n");
            choice=integerParsing();
            if(choice.size()>=target.getMinPlayerIn() && (choice.size()<=target.getMaxPlayerIn() || target.getMaxPlayerIn()==-1) && correctInput(list, choice))
                rightChoice = true;
        }while(!rightChoice);
        return choice;
    }

    private List<Integer> roomHandlerChoice(List<String> list){
        List<Integer> choice = new ArrayList<>();
        boolean rightChoice = false;
        do {
            choice.clear();
            Logger.print("Scegli la stanza che vuoi colpire: \n");
            choice.add(Integer.parseInt(reader.readLine()));
            if(correctInput(list, String.valueOf(choice.get(0))))
                rightChoice = true;
        }while(!rightChoice);
        return choice;
    }

    private List<Integer> integerParsing(){
        List<Integer> choice = new ArrayList<>();
        reader.cancel();
        String[] read=(reader.readLine()).split("\\s+");
        for (String s : read) choice.add(Integer.parseInt(s));
        return choice;
    }

    @Override
    public synchronized void onCanUsePowerup() {
        reader.cancel();
        String choice;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            Logger.print("Puoi usare un powerup in base alla situazione di gioco corrente, vuoi usufruirne?\n [S/N]");
            list.add("S");
            list.add("N");

            choice = reader.readLine();
            if(correctInput(list, choice))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.usePowerup(choice.equals("S"));
    }

    @Override
    public synchronized void onCanStopRoutine() {
        reader.cancel();
        String choice;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            Logger.print("Puoi usare effetti opzionali della tua arma, vuoi usarli?\n [S/N]");
            list.add("S");
            list.add("N");

            choice = reader.readLine();
            if(correctInput(list, choice))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.stopRoutine(!choice.equals("S"));
    }

    @Override
    public synchronized void onUsableWeapons(List<Card> usableWeapons) {
        serverConnection.selectWeapon(availableCard(usableWeapons, ActionsLog.USE_WEAPON));
    }

    @Override
    public synchronized void onAvailableEffects(List<String> effects) {
        reader.cancel();
        int i;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            for (String effect : effects) {
                Logger.print((effects.indexOf(effect) + 1) + ". " + effect + "\n");
                list.add(String.valueOf(effects.indexOf(effect) + 1));
            }
            Logger.print("Scegli l'effetto da usare: ");

            i = Integer.parseInt(reader.readLine());
            if(correctInput(list, String.valueOf(i)))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.selectEffect(effects.get(i-1));
    }

    @Override
    public synchronized void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        if(player.getNickname().equals(me.getNickname()))
            this.powerups.removeAll(discardedPowerups);
        updatePlayer(player, false);
    }

    @Override
    public synchronized void onUsedCard(Card card) {
        updateActionLog(card);
        printInterface();
    }

    @Override
    public synchronized void onAvailablePowerups(List<Card> powerups, List<Color> colors) {
        Card pow = availableCard(powerups, ActionsLog.USE_POWERUP);
        this.powerups.remove(pow);
        serverConnection.selectPowerup(pow);
    }

    @Override
    public synchronized void onRunCompleted(SimplePlayer player, int[] newPosition) {
        updatePlayer(player, true);
        updateActionLog(player, ActionsLog.MOVE);
        printInterface();
    }

    @Override
    public synchronized void onRunRoutine(MatrixHelper matrix) {
        reader.cancel();
        String coordinates;
        boolean rightChoice=false;
        do {
            Logger.print("Scegli dove vuoi muoverti: " + squareList(matrix));
            coordinates = reader.readLine();
            if(correctInput(squareList(matrix), coordinates))
                rightChoice = true;
        }while (!rightChoice);
        serverConnection.runAction(parseCoordinates(coordinates));
    }

    @Override
    public synchronized void onPlayerWakeUp(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy){
        onMatchUpdate(players, gameBoard, frenzy);
    }

    @Override
    public synchronized void onRecoverPlayerAdvise(String nickname){
        actionLog.add(nickname + "ha ripreso a giocare");
    }

    @Override
    public synchronized void onFullOfPowerup(){
        Logger.print("Hai raggiunto il numero massimo di powerup\n");
    }

    @Override
    public synchronized void onCanCounterAttack(){
        reader.cancel();

        String choice;
        boolean righChoice=false;
        List<String> list = new ArrayList<>();
        do {
            Logger.print("Puoi usufruire di un Granata Venom, vuoi usarlo? [S/N]");
            list.add("S");
            list.add("N");
            choice = reader.readLine();
            if(correctInput(list, choice))
                righChoice=true;
        }while (!righChoice);
        serverConnection.counterAttackAnswer(choice.equals("S"));
    }

    @Override
    public synchronized void onCounterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup){
        updatePlayer(currentPlayer, false);
        updatePlayer(player, false);
        actionLog.add(currentPlayer + " ha usato Granata Venom su " + player);
    }

    @Override
    public synchronized void onCounterAttackTimeOut(){
        reader.cancel();
        Logger.print("E' scaduto il tempo utile per contrattaccare...\n");
    }

    @Override
    public synchronized void handleFatalError(String cause, String message){
        Logger.print("\n\n" + BOLD + RED_W +
                "Fatal error occured:" + RESET + "\n" + cause + "\n" + message + "\n");
    }

    @Override
    public void onDisconnectionAdvise(){
        reader.cancel();
        Logger.print("\n Sei stato disconnesso dalla partita...\n");
    }

    @Override
    public void onGameEnd(List<SimplePlayer> players){
        reader.cancel();
        Logger.print("\n" + BOLD + "La partita è finita!" + RESET + "\n");
        serverConnection.confirmEndGame();
    }

    @Override
    public void onLeaderboardReceived(List<String> nicknames, List<Integer> points){
        Logger.print("\n\n\n\n");
        Logger.print("________________________________________________________");
        Logger.print(REVERSE + BLUE_W + BOLD + "LA PARTITA E' FINITA\n" + RESET);
        int counter = 1;
        for(String player: nicknames) {
            if (!player.equals(nicknames.get(0)) && points.get(nicknames.indexOf(player)).equals(points.get(nicknames.indexOf(player) - 1)))
                counter++;
            Logger.print(counter + ". " + BOLD + player + GREEN_W + "  " + points.get(nicknames.indexOf(player)) + RESET + "\n");
        }
        Logger.print("________________________________________________________");
        Logger.print("\n\n\n\n");
    }

    private boolean correctInput(List<String> rightWords, String input){
        if(!rightWords.contains(input))
            Logger.print("\nErrore nell'input, riprova.\n");
        return rightWords.contains(input);
    }

    private boolean correctInput(List<String> rightWords, List<Integer> input){
        List<String> newList = new ArrayList<>(input.size());
        for (Integer myInt : input) {
            newList.add(String.valueOf(myInt));
        }
        if(!rightWords.containsAll(newList) && findDuplicate(input))
            Logger.print("\nErrore nell'input, riprova.\n");
        return rightWords.containsAll(newList);
    }

    private boolean findDuplicate(List<Integer> input){
        Set<Integer> foundNumbers = new HashSet<>();
        for (Integer num : input) {
            if(foundNumbers.contains(num)){
                return true;
            }
            foundNumbers.add(num);
        }
        return false;
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
         actionLog.add(playerColored(player) + a + weapon.getName());
    }

    private void updateActionLog(SimplePlayer player, String giver, int quantity, ActionsLog action){
        if(action.equals(ActionsLog.MARK))
            actionLog.add(playerColored(player) + " has been marked " + quantity + "times from " + giver);
        else actionLog.add(giver + " has dealt " + quantity + " damages to " + playerColored(player));
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
        actionLog.add(playerColored(player) + a);
    }

    private void updateActionLog(SimplePlayer player, AmmoTile ammoTile){
        StringBuilder a = new StringBuilder();
        if(ammoTile.hasPowerup())
            a.append(BACKGROUND_WHITE + BLACK_W + "P" + RESET).append(parseColor(ammoTile.getAmmo(0))).append(AMMO).append(RESET).append(parseColor(ammoTile.getAmmo(1))).append(AMMO).append(RESET);
        else a.append(parseColor(ammoTile.getAmmo(0))).append(AMMO).append(RESET).append(parseColor(ammoTile.getAmmo(1))).append(AMMO).append(RESET).append(parseColor(ammoTile.getAmmo(2))).append(AMMO).append(RESET);
        actionLog.add(playerColored(player) + " has grabbed " + a.toString());
    }

    private String playerColored(SimplePlayer player){
        return (playersColor.get(player.getNickname()) + player.getNickname() + RESET);
    }

    private void updatePlayer(SimplePlayer player, boolean updatePos){
        if(me.getNickname().equals(player.getNickname())){
            me=player;
            if(updatePos && me.getPosition()!=null)
                setPlayerPosition(me, player.getPosition());
        }
        else for(SimplePlayer enemy: enemies) {
            if (enemy.getNickname().equals(player.getNickname())) {
                enemies.set(enemies.indexOf(enemy), player);
                if(updatePos && player.getPosition()!=null)
                    setPlayerPosition(enemy, player.getPosition());
                break;
            }
        }
    }

    private Card availableCard(List<Card> cards, ActionsLog action){
        reader.cancel();
        int i;
        boolean rightChoice=false;
        List<String> list = new ArrayList<>();
        do {
            for (Card card : cards) {
                Logger.print((cards.indexOf(card) + 1) + ". " + card.getName() + "\n");
                list.add(String.valueOf(cards.indexOf(card) + 1));
            }
            switch (action){
                case TO_LOAD:
                    Logger.print("Scegli l'arma da ricaricare: \n");
                    break;
                case TO_DISCARD:
                    Logger.print("Scegli l'arma da scartare: \n");
                    break;
                case USE_WEAPON:
                    Logger.print("Scegli l'arma da usare: \n");
                    break;
                case USE_POWERUP:
                    Logger.print("Scegli il powerup da usare: \n");
                    break;
                default:
                    Logger.logErr("Action not recognized");
            }

            i = Integer.parseInt(reader.readLine());
            if(correctInput(list, String.valueOf(i)))
                rightChoice=true;
        }while(!rightChoice);
        return cards.get(i-1);
    }

    private List<String> squareList(MatrixHelper matrix){
        List<String> squareList = new ArrayList<>();
        for (int i = 0; i < matrix.getRowLength(); i++)
            for (int j = 0 ; j < matrix.getColLength(); j++)
                if(matrix.toBooleanMatrix()[i][j])
                    squareList.add("" + (char)(j+65) + i);
                return squareList;
    }

    private class LimitedQueue<E> extends LinkedList<E> {

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

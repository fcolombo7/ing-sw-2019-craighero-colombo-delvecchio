package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.controller.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.*;
import it.polimi.ingsw.network.controller.messages.room.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SocketClientConnection extends ClientConnection implements Runnable {
    private Socket socket;

    private Server server;

    private Scanner in;

    private PrintStream out;

    private Map<String, MessageManager> messageHandler;

    public SocketClientConnection(Socket socket, Server server) throws IOException {
        super();
        this.socket = socket;
        this.server = server;
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        loadHandler();
    }

    @Override
    public void run() {
        while(true){
            String s=in.nextLine();
            Logger.log(s);
            messageHandler.get(s).exec();
        }
    }

    private void loadHandler() {
        if (messageHandler == null) {
            messageHandler = new HashMap<>();
            messageHandler.put(Constants.MSG_CLIENT_LOGIN, this::loginRequest);
            messageHandler.put(Constants.MSG_CLIENT_CLOSE, this::closeRequest);
            messageHandler.put(Constants.BOARD_SETTING_ANSWER,this::onBoardPreferenceAnswer);
        }
    }

    private void loginRequest() {
        Logger.log("[Received a login request from socket]");
        if(isOnline()){
            out.println(Constants.MSG_SERVER_ALREADY_LOGGED);
            out.flush();
            Logger.log("Invalid request: client already logged in");
        }
        Gson gson=new Gson();
        String msg=in.nextLine();
        try{
            LoginMessage loginMessage=gson.fromJson(msg, LoginMessage.class);
            String cNickname = loginMessage.getNickname();
            String cMotto = loginMessage.getMotto();
            if (server.checkClientLogin(cNickname, this)) {
                msg = Constants.MSG_SERVER_POSITIVE_ANSWER;
                setOnline(true);
            } else {
                msg = Constants.MSG_SERVER_NEGATIVE_ANSWER;
            }

            out.println(msg);
            out.flush();
            if (msg.equalsIgnoreCase(Constants.MSG_SERVER_POSITIVE_ANSWER)) {
                setNickname(cNickname);
                setMotto(cMotto);
                server.joinAvailableRoom(this);
            }
        }catch (Exception e){
            Logger.logErr("Cannot get a correct LoginMessage from the received Json string.");
        }
    }

    private void closeRequest() {
        closeConnection();
        server.deregisterConnection(this);
    }

    @Override
    public void closeConnection() {
        try {
            out.println(Constants.MSG_SERVER_CLOSE);
            out.flush();
            socket.close();
        } catch (IOException e) {
            Logger.logErr(e.getMessage());
        }
        setOnline(false);
    }



    /*------ ROOM MESSAGES ------ */
    @Override
    public void joinRoomAdvise(String nickname) {
        sendRoomMessage(new JoinMessage(nickname));
    }

    @Override
    public void exitRoomAdvise(String nickname) {
        sendRoomMessage(new ExitMessage(nickname));
    }

    @Override
    public void firstInRoomAdvise() {
        out.println(Constants.FIRST_PLAYER);
    }

    //TODO
    @Override
    public void pingAdvise() {

    }

    private void sendRoomMessage(RoomMessage message) {
        Gson gson= new Gson();
        Logger.log("[Socket] "+message.getType()+" sending to "+getNickname());
        try {
            Logger.log(gson.toJson(message));
            out.println(message.getType());
            out.println(gson.toJson(message));
        }catch(Exception e){
            Logger.logErr(e.getMessage());
        }
    }



    /*------ MATCH MESSAGES ------ */
    private void sendMatchMessage(MatchMessage message) {
        Gson gson= new Gson();
        Logger.log("[Socket] "+message.getRequest()+" sending to "+getNickname());
        try {
            out.println(message.getRequest());
            out.println(gson.toJson(message));
        }catch(Exception e){
            Logger.logErr(e.getMessage());
        }
    }


    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        sendMatchMessage(new MatchCreationMessage(getNickname(),players,playerTurnNumber));
    }

    @Override
    public void invalidMessageReceived(String msg) {
        sendMatchMessage(new InvalidAnswerMessage(getNickname(),msg));
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {
        sendMatchMessage(new BoardUpdateMessage(gameBoard));
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        sendMatchMessage(new MatchUpdateMessage(players,gameBoard,frenzy));
    }

    @Override
    public void respwanRequest(List<Card> powerups) {
        sendMatchMessage(new RespawnRequestMessage(getNickname(),powerups));
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup) {
        sendMatchMessage(new RespawnMessage(player,discardedPowerup));
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        sendMatchMessage(new GrabbedAmmoTileMessage(player,grabbedTile));
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup) {
        sendMatchMessage(new GrabbedPowerupMessage(getNickname(),player,powerup));
    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {
        sendMatchMessage(new GrabbableWeaponsMessage(getNickname(),weapons));
    }

    @Override
    public void discardWeapon(List<Card> weapons) {
        sendMatchMessage(new DiscardWeaponMessage(getNickname(),weapons));
    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {
        sendMatchMessage(new GrabbedWeaponMessage(player,weapon));
    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        sendMatchMessage(new WeaponReloadedMessage(player,weapon,discardedPowerups,totalCost));
    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {
        sendMatchMessage(new ReloadableWeaponsMessage(getNickname(),weapons));
    }

    @Override
    public void turnActions(List<String> actions) {
        sendMatchMessage(new TurnActionsMessage(getNickname(),actions));
    }

    @Override
    public void turnEnd() {
        sendMatchMessage(new TurnEndMessage(getNickname()));
    }

    @Override
    public void moveAction(SimplePlayer player) {
        sendMatchMessage(new MoveMessage(player));
    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {
        sendMatchMessage(new MoveRequestMessage(getNickname(),targetPlayer,matrix));
    }

    @Override
    public void markAction(String player, SimplePlayer selected, int value) {
        sendMatchMessage(new MarkMessage(getNickname(),selected,value));
    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        sendMatchMessage(new DamageMessage(getNickname(),selected,damageValue,convertedMarks));
    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        sendMatchMessage(new DiscardedPowerupMessage(player,powerup));
    }

    @Override
    public void turnCreation(String currentPlayer) {
        sendMatchMessage(new TurnCreationMessage(currentPlayer));
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        sendMatchMessage(new SelectablePlayersMessage(getNickname(),target,selectable));
    }

    @Override
    public void canUsePowerup() {
        sendMatchMessage(new CanUsePowerupMessage(getNickname()));

    }

    @Override
    public void canStopRoutine() {
        sendMatchMessage(new CanStopMessage(getNickname()));
    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {
        sendMatchMessage(new UsableWeaponsMessage(getNickname(),usableWeapons));
    }

    @Override
    public void availableEffects(List<String> effects) {
        sendMatchMessage(new AvailableEffectsMessage(effects,getNickname()));
    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        sendMatchMessage(new PayEffectMessage(player,usedAmmo,discardedPowerups));
    }

    @Override
    public void usedCard(Card card) {
        sendMatchMessage(new UsedCardMessage(card));
    }

    @Override
    public void availablePowerups(List<Card> powerups) {
        sendMatchMessage(new AvailablePowerupsMessage(getNickname(),powerups));
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        sendMatchMessage(new RunCompleted(player,newPosition));
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        sendMatchMessage(new RunMessage(getNickname(),matrix));
    }

    /*------ ANSWER HANDLER ------*/
    private void onBoardPreferenceAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log("[JSON ANSWER] "+line);
        try{
            BoardPreferenceAnswer message=gson.fromJson(line, BoardPreferenceAnswer.class);
            if(!message.getAnswer().equalsIgnoreCase(Constants.BOARD_SETTING_ANSWER)) throw new IllegalArgumentException("NOT BOARD PREFERENCE");

            //TODO:check if can receive this message
            getRoom().getController().roomPreferenceManager(message.getSender(),message.getRoomReference());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
        }
    }


    @FunctionalInterface
    private interface MessageManager {
        void exec();
    }
}

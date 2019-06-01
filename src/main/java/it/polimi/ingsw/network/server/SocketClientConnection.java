package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameStatus;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.controller.messages.matchanswer.*;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
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

    private static final String ILLEGAL_STATE="ILLEGAL GAME STATUS";
    
    private static final String JSON_ANSWER="[JSON ANSWER] ";

    private Socket socket;

    private Server server;

    private Scanner in;

    private PrintStream out;

    private Map<String, MessageManager> messageHandler;

    @FunctionalInterface
    private interface MessageManager {
        void exec();
    }

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
            //Logger.log(s);
            messageHandler.get(s).exec();
        }
    }

    private void loadHandler() {
        if (messageHandler == null) {
            messageHandler = new HashMap<>();
            messageHandler.put(Constants.MSG_CLIENT_LOGIN, this::loginRequest);
            messageHandler.put(Constants.MSG_CLIENT_CLOSE, this::closeRequest);
            messageHandler.put(Constants.PONG_ANSWER,this::pongAnswer);
            messageHandler.put(Constants.BOARD_SETTING_ANSWER,this::onBoardPreferenceAnswer);
            messageHandler.put(Constants.RESPAWN_ANSWER,this::onRespawnAnswer);
            messageHandler.put(Constants.ACTION_SELECTED,this::onActionSelectedAnswer);
            messageHandler.put(Constants.EFFECT_MOVE_ANSWER,this::onEffectMoveAnswer);
            messageHandler.put(Constants.TURN_END_ANSWER,this::onTurnEndAnswer);
            messageHandler.put(Constants.RUN_ROUTINE_ANSWER,this::onRunRoutineAnswer);
            messageHandler.put(Constants.WEAPON_ANSWER,this::onWeaponAnswer);
            messageHandler.put(Constants.LOADABLE_WEAPON_SELECTED,this::onLoadableWeaponAnswer);
            messageHandler.put(Constants.EFFECT_ANSWER,this::onEffectAnswer);
            messageHandler.put(Constants.SELECTED_PLAYERS_ANSWER,this::onSelectedePlayersAnswer);
            messageHandler.put(Constants.STOP_ROUTINE_ANSWER,this::onStopRoutineAnswer);
            messageHandler.put(Constants.USE_POWERUP_ANSWER,this::onUsePowerupAnswer);
            messageHandler.put(Constants.DISCARDED_WEAPON_ANSWER,this::onDiscardWeaponAnswer);
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


    private void pongAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        try{
            PongAnswer answer=gson.fromJson(line, PongAnswer.class);
            if(!answer.getType().equalsIgnoreCase(Constants.PONG_ANSWER)) throw new IllegalArgumentException("NOT PONG ANSWER");
            Logger.log("["+getNickname()+" IS ALIVE]");
            getRoom().isAlive(this);
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }

    }

    private void closeRequest() {
        closeConnection();
        //server.deregisterConnection(this);
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
        server.deregisterConnection(this);
    }


    /*------ ROOM MESSAGES ------ */

    private void sendRoomMessage(RoomMessage message) {
        Gson gson= new Gson();
        //Logger.log("[Socket] "+message.getType()+" sending to "+getNickname());
        try {
            //Logger.log(gson.toJson(message));
            out.println(message.getType());
            out.println(gson.toJson(message));
            out.flush();
        }catch(Exception e){
            Logger.logErr(e.getMessage());
            closeConnection();
        }
    }

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
        sendRoomMessage(new FirstInMessage());
    }

    @Override
    public void keepAlive() {
        sendRoomMessage(new PingMessage());
    }

    /*------ MATCH MESSAGES ------ */

    private void sendMatchMessage(MatchMessage message) {
        Gson gson= new Gson();
        Logger.log("[Socket] "+message.getRequest()+" sending to "+getNickname());
        try {
            out.println(message.getRequest());
            out.println(gson.toJson(message));
            out.flush();
        }catch(Exception e){
            Logger.logErr(e.getMessage());
            closeConnection();
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
        Logger.log(JSON_ANSWER+line);
        try{
            BoardPreferenceAnswer answer=gson.fromJson(line, BoardPreferenceAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.BOARD_SETTING_ANSWER)) throw new IllegalArgumentException("NOT BOARD PREFERENCE");

            //check if can receive this answer
            if(!checkStatus(GameStatus.CREATED)) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().roomPreferenceManager(answer.getSender(),answer.getRoomReference());
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onDiscardWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            DiscardedWeaponAnswer answer=gson.fromJson(line, DiscardedWeaponAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.DISCARDED_WEAPON_ANSWER)) throw new IllegalArgumentException("NOT DISCARDED WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().discardWeapon(answer.getWeapon());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onUsePowerupAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            UsePowerupAnswer answer=gson.fromJson(line, UsePowerupAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.USE_POWERUP_ANSWER)) throw new IllegalArgumentException("NOT USE POWERUP ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().usePowerup(answer.wishUseIt());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onStopRoutineAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            StopRoutineAnswer answer=gson.fromJson(line, StopRoutineAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.STOP_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT STOP ROUTINE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().stopRoutine(answer.wishStop());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onSelectedePlayersAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            SelectedPlayersAnswer answer=gson.fromJson(line, SelectedPlayersAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.SELECTED_PLAYERS_ANSWER)) throw new IllegalArgumentException("NOT SELECTED PLAYERS ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().selectPlayers(answer.getSelected());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onEffectAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            EffectAnswer answer=gson.fromJson(line, EffectAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)) throw new IllegalArgumentException("NOT SELECTED EFFECT ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().selectEffect(answer.getEffectName());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onLoadableWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            LoadableWeaponSelectedAnswer answer=gson.fromJson(line, LoadableWeaponSelectedAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)) throw new IllegalArgumentException("NOT LOADABLE WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().loadableWeapon(answer.getWeapon());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            WeaponAnswer answer=gson.fromJson(line, WeaponAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.WEAPON_ANSWER)) throw new IllegalArgumentException("NOT WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().selectWeapon(answer.getWeapon());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onRunRoutineAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            RunAnswer answer=gson.fromJson(line, RunAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.RUN_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT RUN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().runAction(answer.getNewPosition());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onTurnEndAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            TurnEndAnswer answer=gson.fromJson(line, TurnEndAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.TURN_END_ANSWER)) throw new IllegalArgumentException("NOT TURN END ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().closeTurn(answer.getSender());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onEffectMoveAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            MoveAnswer answer=gson.fromJson(line, MoveAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.EFFECT_MOVE_ANSWER)) throw new IllegalArgumentException("NOT EFFECT MOVE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().movePlayer(answer.getTarget(),answer.getNewPosition());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onActionSelectedAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            ActionSelectedAnswer answer=gson.fromJson(line, ActionSelectedAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.ACTION_SELECTED)) throw new IllegalArgumentException("NOT ACTION SELECTED ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().selectAction(answer.getSelection());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void onRespawnAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.log(JSON_ANSWER+line);
        try{
            RespawnAnswer answer=gson.fromJson(line, RespawnAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.RESPAWN_ANSWER)) throw new IllegalArgumentException("NOT RESPAWN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.READY)||checkStatus(GameStatus.CLOSING_TURN))) throw new IllegalStateException(ILLEGAL_STATE);
            getRoom().getController().respawnPlayer(answer.getSender(),answer.getPowerup());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }
    
    /*------- METHODS USED TO CHECK IF CAN RECEIVE A MESSAGE-------*/
    private boolean checkTurn(){
        return getRoom().getController().isPlaying(getNickname());
    }

    private boolean checkStatus(GameStatus status){
        return (status==getRoom().getController().getGameStatus());
    }
    
    
    //TODO
    private void handleInvalidReceived() {
    }

}

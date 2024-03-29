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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the connection interface with the client using socket
 */
public class SocketClientConnection extends ClientConnection implements Runnable {

    /**
     * This attribute represents a constant for the illegal game status
     */
    private static final String ILLEGAL_STATE="ILLEGAL GAME STATUS";

    /**
     * This attribute represents represents a constant for the json answers
     */
    private static final String JSON_ANSWER="[JSON ANSWER] ";

    /**
     * This attribute represents the socket linked to the connection
     */
    private Socket socket;

    /**
     * This attribute represents the server linked to the connection
     */
    private Server server;

    /**
     * This attribute represents the scanner of the input
     */
    private Scanner in;

    /**
     * This attribute represents the printer
     */
    private PrintStream out;

    /**
     * This attribute represents the map that links message to actions
     */
    private Map<String, MessageManager> messageHandler;

    /**
     * This attribute represents the executor service that handle the threads connection
     */
    private ExecutorService pool;

    @FunctionalInterface
    private interface MessageManager {
        void exec(String content);
    }

    /**
     * This constructor sets up the socket connection
     * @param socket represents the socket linked to the connection
     * @param server represents the server linked to the connection
     * @throws IOException when an error occurs
     */
    public SocketClientConnection(Socket socket, Server server) throws IOException {
        super();
        this.socket = socket;
        this.server = server;
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        pool= Executors.newFixedThreadPool(5);
        loadHandler();
    }

    @Override
    public void run() {
        while(true){
            String socketLine = in.nextLine();
            Logger.log(socketLine);
            int pos=socketLine.indexOf('#');
            String type=socketLine.substring(0,pos);
            String content=socketLine.substring(pos+1);
            Logger.log("RECEIVED STRING\n\ttype: "+type+"\n\tcontent: "+content);
            messageHandler.get(type).exec(content);
        }
    }


    /**
     * This method creates the map that lins the messages to its action
     */
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
            messageHandler.put(Constants.SELECTED_PLAYERS_ANSWER,this::onSelectedPlayersAnswer);
            messageHandler.put(Constants.STOP_ROUTINE_ANSWER,this::onStopRoutineAnswer);
            messageHandler.put(Constants.USE_POWERUP_ANSWER,this::onUsePowerupAnswer);
            messageHandler.put(Constants.DISCARDED_WEAPON_ANSWER,this::onDiscardWeaponAnswer);
            messageHandler.put(Constants.COUNTER_ATTACK_ANSWER,this::onCounterAttackAnswer);
            messageHandler.put(Constants.POWERUP_ANSWER,this::onPowerupAnswer);
            messageHandler.put(Constants.CONFIRM_END_GAME,this::onGameEndAck);
        }
    }

    /**
     * This method handle the login requests
     * @param content represents the content of the request
     */
    private void loginRequest(String content) {
        Logger.logAndPrint("[Received a login request from socket]");
        if(isOnline()){
            out.println(Constants.MSG_SERVER_ALREADY_LOGGED);
            out.flush();
            Logger.logAndPrint("Invalid request: client already logged in");
        }
        Gson gson=new Gson();
        String msg=content;
        try{
            LoginMessage loginMessage=gson.fromJson(msg, LoginMessage.class);
            String cNickname = loginMessage.getNickname();
            String cMotto = loginMessage.getMotto();
            int response=server.checkClientLogin(cNickname, this);
            if (response==1||response==2) {
                msg = Constants.MSG_SERVER_POSITIVE_ANSWER;
                setOnline(true);
            } else if(response==0){
                msg = Constants.MSG_SERVER_NEGATIVE_ANSWER;
            }

            out.println(msg);
            out.flush();
            if (response==1||response==2) {
                setNickname(cNickname);
                setMotto(cMotto);
                if(response==1)
                    server.joinAvailableRoom(this);
                else {
                    try {
                        server.joinRecoveredRoom(this);
                        Logger.logAndPrint("OK JOIN RECOVER");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            Logger.logErr("Cannot get a correct LoginMessage from the received Json string.");
            e.printStackTrace();
        }
    }

    /**
     * This method handle the answer needed to keep alive the connection
     * @param content represents the content of the message
     */
    private void pongAnswer(String content) {
        Gson gson=new Gson();
        try{
            PongAnswer answer=gson.fromJson(content, PongAnswer.class);
            if(!answer.getType().equalsIgnoreCase(Constants.PONG_ANSWER)) throw new IllegalArgumentException("NOT PONG ANSWER");
            pool.submit(()->getRoom().isAlive(this));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            handleInvalidReceived("PONG ANSWER",e.getMessage(),content);
        }

    }

    /**
     * This method close a request of login
     * @param content represents the content of the request
     */
    private void closeRequest(String content) {
        pool.submit(()->closeConnection());
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
        if(getRoom().isPlaying())
            server.disconnectConnection(this);
        else
            server.deregisterConnection(this);
    }


    /*------ ROOM MESSAGES ------ */

    /**
     * This method send message relates to the waiting room
     * @param message represents the message sent
     */
    private void sendRoomMessage(RoomMessage message) {
        Gson gson= new Gson();
        //Logger.logAndPrint("[Socket] "+message.getType()+" sending to "+getNickname());
        try {
            //Logger.logAndPrint(gson.toJson(message));
            out.println(message.getType()+"#"+gson.toJson(message));
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
    public void recoverAdvise(String nickname) {
        sendRoomMessage(new RecoverMessage(nickname));
    }

    @Override
    public void keepAlive() {
        sendRoomMessage(new PingMessage());
    }

    /*------ MATCH MESSAGES ------ */

    private void sendMatchMessage(MatchMessage message) {
        Gson gson= new Gson();
        String msg=message.getRequest();
        if(msg.equals(Constants.TURN_ROUTINE_MESSAGE))
            msg=((TurnRoutineMessage)message).getRoutineRequest();
        Logger.logAndPrint("[Socket] "+msg+" sending to "+getNickname());
        try {
            out.println(msg+"#"+gson.toJson(message));
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
        Logger.logAndPrint(msg);
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
    public void recoveringPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        sendMatchMessage(new RecoveringPlayerMessage(getNickname(),players,gameBoard,frenzy));
    }

    @Override
    public void respwanRequest(List<Card> powerups,List<Color> colors) {
        sendMatchMessage(new RespawnRequestMessage(getNickname(),powerups,colors));
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup,Color color) {
        sendMatchMessage(new RespawnMessage(player,discardedPowerup,color));
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        sendMatchMessage(new GrabbedAmmoTileMessage(player,grabbedTile));
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup,Color color) {
        sendMatchMessage(new GrabbedPowerupMessage(getNickname(),player,powerup,color));
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
    public void fullOfPowerup() {
        sendMatchMessage(new FullOfPowerupsMessage(getNickname()));
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
    public void availablePowerups(List<Card> powerups,List<Color> colors) {
        sendMatchMessage(new AvailablePowerupsMessage(getNickname(),powerups,colors));
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        sendMatchMessage(new RunCompleted(player,newPosition));
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        sendMatchMessage(new RunMessage(getNickname(),matrix));
    }

    @Override
    public void canCounterAttack() {
        sendMatchMessage(new CanCounterAttackMessage(getNickname()));
    }

    @Override
    public void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {
        sendMatchMessage(new CounterAttackMessage(currentPlayer,player,powerup));
    }

    @Override
    public void counterAttackTimeOut() {
        sendMatchMessage(new CounterAttackTimeOut(getNickname()));
    }

    @Override
    public void notifyDisconnetion() {
        Logger.logAndPrint("[SOCKET] DISCONNECTION to "+getNickname());
        sendRoomMessage(new DisconnectionMessage());
    }

    @Override
    public void gameEnd(List<SimplePlayer> players) {
        GameEndMessage msg=new GameEndMessage();
        msg.setPlayers(players);
        sendMatchMessage(msg);
    }

    @Override
    public void sendLeaderboard(List<String> nicknames, List<Integer> points) {
        sendMatchMessage(new LeaderboardMessage(getNickname(),nicknames,points));
    }


    /*------ ANSWER HANDLER ------*/
    private void onCounterAttackAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            CounterAttackAnswer answer=gson.fromJson(content, CounterAttackAnswer .class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.COUNTER_ATTACK_ANSWER)) throw new IllegalArgumentException("NOT COUNTER ATTACK ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(true))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().counterAttackAnswer(answer.getSender(),answer.counterAttack()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("COUNTER ATTACK ANSWER",e.getMessage(),content);
        }
    }

    private void onBoardPreferenceAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            BoardPreferenceAnswer answer=gson.fromJson(content, BoardPreferenceAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.BOARD_SETTING_ANSWER)) throw new IllegalArgumentException("NOT BOARD PREFERENCE");

            //check if can receive this answer
            if(!checkStatus(GameStatus.CREATED)) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().roomPreferenceManager(answer.getSender(),answer.getRoomReference()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("BOARD PREFERENCE ANSWER",e.getMessage(),content);
        }
    }

    private void onPowerupAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            SelectedPowerupAnswer answer=gson.fromJson(content, SelectedPowerupAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.POWERUP_ANSWER)) throw new IllegalArgumentException("NOT POWERUP ANSWER");

            //check if can receive this answer
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectPowerup(answer.getPowerup()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("POWERUP ANSWER",e.getMessage(),content);
        }
    }

    private void onDiscardWeaponAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            DiscardedWeaponAnswer answer=gson.fromJson(content, DiscardedWeaponAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.DISCARDED_WEAPON_ANSWER)) throw new IllegalArgumentException("NOT DISCARDED WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().discardWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("DISCARD WEAPON ANSWER",e.getMessage(),content);
        }
    }

    private void onUsePowerupAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            UsePowerupAnswer answer=gson.fromJson(content, UsePowerupAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.USE_POWERUP_ANSWER)) throw new IllegalArgumentException("NOT USE POWERUP ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().usePowerup(answer.wishUseIt()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("USE POWERUP ANSWER",e.getMessage(),content);
        }
    }

    private void onStopRoutineAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            StopRoutineAnswer answer=gson.fromJson(content, StopRoutineAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.STOP_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT STOP ROUTINE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().stopRoutine(answer.wishStop()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("STOP ROUTINE ANSWER",e.getMessage(),content);
        }
    }

    private void onSelectedPlayersAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            SelectedPlayersAnswer answer=gson.fromJson(content, SelectedPlayersAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.SELECTED_PLAYERS_ANSWER)) throw new IllegalArgumentException("NOT SELECTED PLAYERS ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectPlayers(answer.getSelected()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("SELECTED PLAYER ANSWER",e.getMessage(),content);
        }
    }

    private void onEffectAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            EffectAnswer answer=gson.fromJson(content, EffectAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)) throw new IllegalArgumentException("NOT SELECTED EFFECT ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectEffect(answer.getEffectName()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("EFFECT ANSWER",e.getMessage(),content);
        }
    }

    private void onLoadableWeaponAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            LoadableWeaponSelectedAnswer answer=gson.fromJson(content, LoadableWeaponSelectedAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.LOADABLE_WEAPON_SELECTED)) throw new IllegalArgumentException("NOT LOADABLE WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().loadableWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("LOADABLE WEAPON ANSWER",e.getMessage(),content);
        }
    }

    private void onWeaponAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            WeaponAnswer answer=gson.fromJson(content, WeaponAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.WEAPON_ANSWER)) throw new IllegalArgumentException("NOT WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("WEAPON ANSWER",e.getMessage(),content);
        }
    }

    private void onRunRoutineAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            RunAnswer answer=gson.fromJson(content, RunAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.RUN_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT RUN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().runAction(answer.getNewPosition()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("ROUTINE ANSWER",e.getMessage(),content);
        }
    }

    private void onTurnEndAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            TurnEndAnswer answer=gson.fromJson(content, TurnEndAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.TURN_END_ANSWER)) throw new IllegalArgumentException("NOT TURN END ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->{
                getRoom().getController().closeTurn(answer.getSender());
            });
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("TURN END ANSWER",e.getMessage(),content);
        }
    }

    private void onEffectMoveAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            MoveAnswer answer=gson.fromJson(content, MoveAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.EFFECT_MOVE_ANSWER)) throw new IllegalArgumentException("NOT EFFECT MOVE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().movePlayer(answer.getTarget(),answer.getNewPosition()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("EFFECT MOVE ANSWER",e.getMessage(),content);
        }
    }

    private void onActionSelectedAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            ActionSelectedAnswer answer=gson.fromJson(content, ActionSelectedAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.ACTION_SELECTED)) throw new IllegalArgumentException("NOT ACTION SELECTED ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn(false))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectAction(answer.getSelection()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("ACTION SELECTED ANSWER",e.getMessage(),content);
        }
    }

    private void onRespawnAnswer(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            RespawnAnswer answer=gson.fromJson(content, RespawnAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.RESPAWN_ANSWER)) throw new IllegalArgumentException("NOT RESPAWN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.READY)||checkStatus(GameStatus.CLOSING_TURN))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().respawnPlayer(answer.getSender(),answer.getPowerup(),false));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("RESPAWN ANSWER",e.getMessage(),content);
        }
    }

    private void onGameEndAck(String content) {
        Gson gson=new Gson();
        Logger.logAndPrint(JSON_ANSWER+content);
        try{
            ConfirmEndGameAnswer answer=gson.fromJson(content, ConfirmEndGameAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.CONFIRM_END_GAME)) throw new IllegalArgumentException("NOT END GAME CONFIRM ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.END))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().gameEndAck(answer.getSender()));
        }catch (Exception e){
            Logger.logAndPrint(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("END GAME CONFIRM",e.getMessage(),content);
        }
    }
    
    /*------- METHODS USED TO CHECK IF CAN RECEIVE A MESSAGE-------*/
    private boolean checkTurn(boolean counterAttackAnswer){
        return counterAttackAnswer||getRoom().getController().isPlaying(getNickname());
    }

    private boolean checkStatus(GameStatus status){
        return (status==getRoom().getController().getGameStatus());
    }
    
    private void handleInvalidReceived(String cause,String message, String lastLine) {
        String builder=("An error occurred while trying to execute '")+(cause)+("':\n")+(message);
        Logger.logErr(builder);
        Logger.logErr("LAST LINE READ: "+lastLine);
        getRoom().forceDisconnection(getNickname());
    }

}

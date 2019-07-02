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
import it.polimi.ingsw.utils.ConsoleInput;
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

public class SocketClientConnection extends ClientConnection implements Runnable {

    private static final String ILLEGAL_STATE="ILLEGAL GAME STATUS";
    
    private static final String JSON_ANSWER="[JSON ANSWER] ";

    private Socket socket;

    private Server server;

    private Scanner in;

    private PrintStream out;

    private Map<String, MessageManager> messageHandler;

    private ExecutorService pool;

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
        pool= Executors.newFixedThreadPool(5);
        loadHandler();
    }

    @Override
    public void run() {
        while(true){
            String s=in.nextLine();
            //Logger.logServer(s);
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
            messageHandler.put(Constants.SELECTED_PLAYERS_ANSWER,this::onSelectedPlayersAnswer);
            messageHandler.put(Constants.STOP_ROUTINE_ANSWER,this::onStopRoutineAnswer);
            messageHandler.put(Constants.USE_POWERUP_ANSWER,this::onUsePowerupAnswer);
            messageHandler.put(Constants.DISCARDED_WEAPON_ANSWER,this::onDiscardWeaponAnswer);
            messageHandler.put(Constants.COUNTER_ATTACK_ANSWER,this::onCounterAttackAnswer);
            messageHandler.put(Constants.POWERUP_ANSWER,this::onPowerupAnswer);
            messageHandler.put(Constants.CONFIRM_END_GAME,this::onGameEndAck);
        }
    }

    private void loginRequest() {
        Logger.logServer("[Received a login request from socket]");
        if(isOnline()){
            out.println(Constants.MSG_SERVER_ALREADY_LOGGED);
            out.flush();
            Logger.logServer("Invalid request: client already logged in");
        }
        Gson gson=new Gson();
        String msg=in.nextLine();
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
                else
                    server.joinRecoveredRoom(this);
            }
        }catch (Exception e){
            Logger.logErr("Cannot get a correct LoginMessage from the received Json string.");
            e.printStackTrace();
        }
    }


    private void pongAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        try{
            PongAnswer answer=gson.fromJson(line, PongAnswer.class);
            if(!answer.getType().equalsIgnoreCase(Constants.PONG_ANSWER)) throw new IllegalArgumentException("NOT PONG ANSWER");
            pool.submit(()->getRoom().isAlive(this));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            handleInvalidReceived("PONG ANSWER",e.getMessage());
        }

    }

    private void closeRequest() {
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

    private void sendRoomMessage(RoomMessage message) {
        Gson gson= new Gson();
        //Logger.logServer("[Socket] "+message.getType()+" sending to "+getNickname());
        try {
            //Logger.logServer(gson.toJson(message));
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
        Logger.logServer("[Socket] "+msg+" sending to "+getNickname());
        try {
            out.println(msg);
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
    public void recoveringPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        sendMatchMessage(new RecoveringPlayerMessage(getNickname(),players,gameBoard,frenzy));
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
    private void onCounterAttackAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            CounterAttackAnswer answer=gson.fromJson(line, CounterAttackAnswer .class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.COUNTER_ATTACK_ANSWER)) throw new IllegalArgumentException("NOT COUNTER ATTACK ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().counterAttackAnswer(answer.getSender(),answer.counterAttack()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("COUNTER ATTACK ANSWER",e.getMessage());
        }
    }

    private void onBoardPreferenceAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            BoardPreferenceAnswer answer=gson.fromJson(line, BoardPreferenceAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.BOARD_SETTING_ANSWER)) throw new IllegalArgumentException("NOT BOARD PREFERENCE");

            //check if can receive this answer
            if(!checkStatus(GameStatus.CREATED)) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().roomPreferenceManager(answer.getSender(),answer.getRoomReference()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("BOARD PREFERENCE ANSWER",e.getMessage());
        }
    }

    private void onPowerupAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            SelectedPowerupAnswer answer=gson.fromJson(line, SelectedPowerupAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.POWERUP_ANSWER)) throw new IllegalArgumentException("NOT POWERUP ANSWER");

            //check if can receive this answer
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectPowerup(answer.getPowerup()));
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("POWERUP ANSWER",e.getMessage());
        }
    }

    private void onDiscardWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            DiscardedWeaponAnswer answer=gson.fromJson(line, DiscardedWeaponAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.DISCARDED_WEAPON_ANSWER)) throw new IllegalArgumentException("NOT DISCARDED WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().discardWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("DISCARD WEAPON ANSWER",e.getMessage());
        }
    }

    private void onUsePowerupAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            UsePowerupAnswer answer=gson.fromJson(line, UsePowerupAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.USE_POWERUP_ANSWER)) throw new IllegalArgumentException("NOT USE POWERUP ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().usePowerup(answer.wishUseIt()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("USE POWERUP ANSWER",e.getMessage());
        }
    }

    private void onStopRoutineAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            StopRoutineAnswer answer=gson.fromJson(line, StopRoutineAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.STOP_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT STOP ROUTINE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().stopRoutine(answer.wishStop()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("STOP ROUTINE ANSWER",e.getMessage());
        }
    }

    private void onSelectedPlayersAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            SelectedPlayersAnswer answer=gson.fromJson(line, SelectedPlayersAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.SELECTED_PLAYERS_ANSWER)) throw new IllegalArgumentException("NOT SELECTED PLAYERS ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectPlayers(answer.getSelected()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("SELECTED PLAYER ANSWER",e.getMessage());
        }
    }

    private void onEffectAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            EffectAnswer answer=gson.fromJson(line, EffectAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)) throw new IllegalArgumentException("NOT SELECTED EFFECT ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectEffect(answer.getEffectName()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("EFFECT ANSWER",e.getMessage());
        }
    }

    private void onLoadableWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            LoadableWeaponSelectedAnswer answer=gson.fromJson(line, LoadableWeaponSelectedAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)) throw new IllegalArgumentException("NOT LOADABLE WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().loadableWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("LOADABLE WEAPON ANSWER",e.getMessage());
        }
    }

    private void onWeaponAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            WeaponAnswer answer=gson.fromJson(line, WeaponAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.WEAPON_ANSWER)) throw new IllegalArgumentException("NOT WEAPON ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectWeapon(answer.getWeapon()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("WEAPON ANSWER",e.getMessage());
        }
    }

    private void onRunRoutineAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            RunAnswer answer=gson.fromJson(line, RunAnswer.class);
            if(!answer.getRoutineAnswer().equalsIgnoreCase(Constants.RUN_ROUTINE_ANSWER)) throw new IllegalArgumentException("NOT RUN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().runAction(answer.getNewPosition()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("ROUTINE ANSWER",e.getMessage());
        }
    }

    private void onTurnEndAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            TurnEndAnswer answer=gson.fromJson(line, TurnEndAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.TURN_END_ANSWER)) throw new IllegalArgumentException("NOT TURN END ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->{
                getRoom().getController().closeTurn(answer.getSender());
            });
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("TURN END ANSWER",e.getMessage());
        }
    }

    private void onEffectMoveAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            MoveAnswer answer=gson.fromJson(line, MoveAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.EFFECT_MOVE_ANSWER)) throw new IllegalArgumentException("NOT EFFECT MOVE ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().movePlayer(answer.getTarget(),answer.getNewPosition()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("EFFECT MOVE ANSWER",e.getMessage());
        }
    }

    private void onActionSelectedAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            ActionSelectedAnswer answer=gson.fromJson(line, ActionSelectedAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.ACTION_SELECTED)) throw new IllegalArgumentException("NOT ACTION SELECTED ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.PLAYING_TURN)&&checkTurn())) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().selectAction(answer.getSelection()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("ACTION SELECTED ANSWER",e.getMessage());
        }
    }

    private void onRespawnAnswer() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            RespawnAnswer answer=gson.fromJson(line, RespawnAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.RESPAWN_ANSWER)) throw new IllegalArgumentException("NOT RESPAWN ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.READY)||checkStatus(GameStatus.CLOSING_TURN))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().respawnPlayer(answer.getSender(),answer.getPowerup()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("RESPAWN ANSWER",e.getMessage());
        }
    }

    private void onGameEndAck() {
        Gson gson=new Gson();
        String line=in.nextLine();
        Logger.logServer(JSON_ANSWER+line);
        try{
            ConfirmEndGameAnswer answer=gson.fromJson(line, ConfirmEndGameAnswer.class);
            if(!answer.getAnswer().equalsIgnoreCase(Constants.CONFIRM_END_GAME)) throw new IllegalArgumentException("NOT END GAME CONFIRM ANSWER");

            //check if can receive this message
            if(!(checkStatus(GameStatus.END))) throw new IllegalStateException(ILLEGAL_STATE);
            pool.submit(()->getRoom().getController().gameEndAck(answer.getSender()));
        }catch (Exception e){
            Logger.logServer(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived("END GAME CONFIRM",e.getMessage());
        }
    }
    
    /*------- METHODS USED TO CHECK IF CAN RECEIVE A MESSAGE-------*/
    private boolean checkTurn(){
        return getRoom().getController().isPlaying(getNickname());
    }

    private boolean checkStatus(GameStatus status){
        return (status==getRoom().getController().getGameStatus());
    }
    
    private void handleInvalidReceived(String cause,String message) {
        String builder=("An error occurred while trying to execute '")+(cause)+("':\n")+(message);
        Logger.logErr(builder);
        getRoom().forceDisconnection(getNickname());
    }

}

package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.RespawnAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnEndAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.*;
import it.polimi.ingsw.network.controller.messages.room.*;
import it.polimi.ingsw.ui.AdrenalineUI;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerConnection extends ServerConnection {
    private static final String LOG_JSON ="[JSON ANSWER] ";
    private String ip;
    private int port;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Map<String,MessageReceiver> receiverMap;
    private ExecutorService executor;

    @FunctionalInterface
    private interface MessageReceiver{
        void handle();
    }

    public SocketServerConnection(String ip, int port, AdrenalineUI ui) throws IOException {
        super(ui);
        this.ip=ip;
        this.port=port;
        socket = new Socket(ip, port);
        socketIn= new Scanner(socket.getInputStream());
        socketOut= new PrintWriter(socket.getOutputStream());
        executor = Executors.newFixedThreadPool(2);
        initReceiverMap();
        Logger.log("SOCKET SET UP");
    }

    public SocketServerConnection(String ip, AdrenalineUI ui) throws IOException {
        this(ip,Constants.SOCKET_PORT,ui);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    private void start(){
        Runnable receiver= () -> {
            try {
                while (true) {
                    String socketLine = socketIn.nextLine();
                    //Logger.log("[SERVER] "+socketLine);
                    receiverMap.get(socketLine).handle();
                }

            }catch(NoSuchElementException e) {
                Logger.logErr("Connection closed");
            } finally {
                socketIn.close();
                socketOut.close();
                try {
                    socket.close();
                } catch (IOException e) {
                    Logger.logErr(e.getMessage());
                }
            }
        };
        executor.submit(receiver);
    }

    private void initReceiverMap() {
        receiverMap=new HashMap<>();
        receiverMap.put(Constants.PLAYER_JOIN,this::joinPlayer);
        receiverMap.put(Constants.FIRST_PLAYER,this::firstPlayer);
        receiverMap.put(Constants.PLAYER_EXIT,this::exitPlayer);
        receiverMap.put(Constants.PING_CHECK,this::ping);

        receiverMap.put(Constants.CREATION_MESSAGE,this::matchCreation);
        receiverMap.put(Constants.INVALID_ANSWER,this::invalidMessageReceived);
        receiverMap.put(Constants.BOARD_UPDATE_MESSAGE,this::boardUpdate);
        receiverMap.put(Constants.UPDATE_MESSAGE,this::matchUpdate);
        receiverMap.put(Constants.RESPAWN_REQUEST_MESSAGE,this::respwanRequest);
        receiverMap.put(Constants.RESPAWN_COMPLETED_MESSAGE,this::respwanCompleted);
        receiverMap.put(Constants.GRABBED_TILE_MESSAGE,this::grabbedTile);
        receiverMap.put(Constants.GRABBED_POWERUP,this::grabbedPowerup);
        receiverMap.put(Constants.GRABBABLE_WEAPONS_MESSAGE,this::grabbableWeapons);
        receiverMap.put(Constants.DISCARD_WEAPON_MESSAGE,this::discardWeapon);
        receiverMap.put(Constants.GRABBED_WEAPON_MESSAGE,this::grabbedWeapon);
        receiverMap.put(Constants.RELOAD_COMPLETED,this::reloadedWeapon);
        receiverMap.put(Constants.RELOADABLE_WEAPONS_MESSAGE,this::reloadableWeapons);
        receiverMap.put(Constants.TURN_CREATION_MESSAGE,this::turnCreation);
        receiverMap.put(Constants.TURN_AVAILABLE_ACTIONS,this::turnActions);
        receiverMap.put(Constants.TURN_END_MESSAGE,this::turnEnd);
        receiverMap.put(Constants.EFFECT_MOVE_MESSAGE,this::moveAction);
        receiverMap.put(Constants.EFFECT_MOVE_REQUEST_MESSAGE,this::moveRequest);
        receiverMap.put(Constants.EFFECT_MARK_MESSAGE,this::markAction);
        receiverMap.put(Constants.EFFECT_DAMAGE_MESSAGE,this::damageAction);
        receiverMap.put(Constants.DISCARDED_POWERUP_MESSAGE,this::discardedPowerup);
        receiverMap.put(Constants.SELECTABLE_PLAYERS_MESSAGE,this::selectablePlayers);
        receiverMap.put(Constants.CAN_USE_POWERUP,this::canUsePowerup);
        receiverMap.put(Constants.CAN_STOP_ROUTINE,this::canStopRoutine);
        receiverMap.put(Constants.USABLE_WEAPONS_MESSAGE,this::usableWeapons);
        receiverMap.put(Constants.AVAILABLE_EFFECTS_MESSAGE,this::availableEffects);
        receiverMap.put(Constants.PAY_EFFECT_MESSAGE,this::payEffect);
        receiverMap.put(Constants.USED_CARD_MESSAGE,this::usedCard);
        receiverMap.put(Constants.AVAILABLE_POWERUPS_MESSAGE,this::availablePowerups);
        receiverMap.put(Constants.RUN_COMPLETED,this::runCompleted);
        receiverMap.put(Constants.RUN_ROUTINE_MESSAGE,this::runRoutine);
    }

    /*------ SERVER CONNECTION METHODS ------*/
    @Override
    public String login(String nickname, String motto) {
        socketOut.println(Constants.MSG_CLIENT_LOGIN);
        Gson gson = new Gson();
        LoginMessage loginMessage= new LoginMessage(nickname,motto);
        socketOut.println(gson.toJson(loginMessage));
        socketOut.flush();

        String response= socketIn.nextLine();
        if(response.equalsIgnoreCase(Constants.MSG_SERVER_POSITIVE_ANSWER)) {
            setNickname(nickname);
            start();
            return Constants.MSG_SERVER_POSITIVE_ANSWER;
        }
        return Constants.MSG_SERVER_NEGATIVE_ANSWER;
    }

    @Override
    public void logout() {
        socketOut.println(Constants.MSG_CLIENT_CLOSE);
        socketOut.flush();
    }

    @Override
    public void boardPreference(int value) {
        socketOut.println(Constants.BOARD_SETTING_ANSWER);
        Gson gson = new Gson();
        BoardPreferenceAnswer answer= new BoardPreferenceAnswer(getNickname(),value);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void respawnPlayer(Card powerup) {
        socketOut.println(Constants.RESPAWN_ANSWER);
        Gson gson = new Gson();
        RespawnAnswer answer= new RespawnAnswer(getNickname(),powerup);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void closeTurn() {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        TurnEndAnswer answer= new TurnEndAnswer(getNickname());
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void selectAction(String action) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        TurnEndAnswer answer= new TurnEndAnswer(getNickname());
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void movePlayer(String target, int[] newPosition) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        TurnEndAnswer answer= new TurnEndAnswer(getNickname());
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void discardWeapon(Card weapon) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        TurnEndAnswer answer= new TurnEndAnswer(getNickname());
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void selectEffect(String effectName) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        TurnEndAnswer answer= new TurnEndAnswer(getNickname());
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void loadableWeapon(Card weapon) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        LoadableWeaponSelectedAnswer answer= new LoadableWeaponSelectedAnswer(getNickname(),weapon);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void runAction(int[] newPosition) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        RunAnswer answer= new RunAnswer(getNickname(),newPosition);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void selectPlayers(List<List<String>> selected) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        SelectedPlayersAnswer answer= new SelectedPlayersAnswer(getNickname(),selected);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void selectPowerup(Card powerup) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        SelectedPowerupAnswer answer= new SelectedPowerupAnswer(getNickname(),powerup);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void stopRoutine(boolean stop) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        StopRoutineAnswer answer= new StopRoutineAnswer(getNickname(),stop);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void usePowerup(boolean use) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        UsePowerupAnswer answer= new UsePowerupAnswer(getNickname(),use);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    @Override
    public void selectWeapon(Card weapon) {
        socketOut.println(Constants.TURN_END_ANSWER);
        Gson gson = new Gson();
        WeaponAnswer answer= new WeaponAnswer(getNickname(),weapon);
        socketOut.println(gson.toJson(answer));
        socketOut.flush();
    }

    /*------ MATCH RECEIVED MESSAGES ------*/
    private void runRoutine() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            RunMessage message=gson.fromJson(line, RunMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RUN_ROUTINE_MESSAGE)) throw new IllegalArgumentException("NOT RUN MESSAGE");
            getUi().onRunRoutine(message.getMatrix());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void runCompleted() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            RunCompleted message=gson.fromJson(line, RunCompleted.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RUN_COMPLETED)) throw new IllegalArgumentException("NOT RUN COMPLETED MESSAGE");
            getUi().onRunCompleted(message.getPlayer(),message.getNewPosition());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void availablePowerups() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            AvailablePowerupsMessage message=gson.fromJson(line, AvailablePowerupsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.AVAILABLE_POWERUPS_MESSAGE)) throw new IllegalArgumentException("NOT AVAILABLE POWERUPS MESSAGE");
            getUi().onAvailablePowerups(message.getPowerups());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void usedCard() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            UsedCardMessage message=gson.fromJson(line, UsedCardMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.USED_CARD_MESSAGE)) throw new IllegalArgumentException("NOT USED CARD MESSAGE");
            getUi().onUsedCard(message.getCard());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void payEffect() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            PayEffectMessage message=gson.fromJson(line, PayEffectMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.PAY_EFFECT_MESSAGE)) throw new IllegalArgumentException("NOT PAY EFFECT MESSAGE");
            getUi().onPayEffect(message.getPlayer(),message.getDiscardedPowerups(),message.getUsedAmmo());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void availableEffects() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            AvailableEffectsMessage message=gson.fromJson(line, AvailableEffectsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.AVAILABLE_EFFECTS_MESSAGE)) throw new IllegalArgumentException("NOT AVAILABLE EFFECTS MESSAGE");
            getUi().onAvailableEffects(message.getEffects());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void usableWeapons() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            UsableWeaponsMessage message=gson.fromJson(line, UsableWeaponsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.USABLE_WEAPONS_MESSAGE)) throw new IllegalArgumentException("NOT USABLE WEAPON MESSAGE");
            getUi().onUsableWeapons(message.getUsableWeapons());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void canStopRoutine() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            CanStopMessage message=gson.fromJson(line, CanStopMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.CAN_STOP_ROUTINE)) throw new IllegalArgumentException("NOT CAN STOP MESSAGE");
            getUi().onCanStopRoutine();
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void canUsePowerup() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            CanUsePowerupMessage message=gson.fromJson(line, CanUsePowerupMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.CAN_USE_POWERUP)) throw new IllegalArgumentException("NOT CAN USE POWERUP MESSAGE");
            getUi().onCanUsePowerup();
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void selectablePlayers() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            SelectablePlayersMessage message=gson.fromJson(line, SelectablePlayersMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.SELECTABLE_PLAYERS_MESSAGE)) throw new IllegalArgumentException("NOT SELECTABLE PLAYER MESSAGE");
            getUi().onSelectablePlayers(message.getSelectable(),message.getTarget());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void discardedPowerup() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            DiscardedPowerupMessage message=gson.fromJson(line, DiscardedPowerupMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.DISCARDED_POWERUP_MESSAGE)) throw new IllegalArgumentException("NOT DISCARDED POWERUP MESSAGE");
            getUi().onDiscardedPowerup(message.getPlayer(),message.getPowerup());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void damageAction() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            DamageMessage message=gson.fromJson(line, DamageMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.EFFECT_DAMAGE_MESSAGE)) throw new IllegalArgumentException("NOT DAMAGE MESSAGE");
            getUi().onDamageAction(message.getPlayer(),message.getSelected(),message.getDamageValue(),message.getConvertedMarks());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void markAction() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            MarkMessage message=gson.fromJson(line, MarkMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.EFFECT_MOVE_REQUEST_MESSAGE)) throw new IllegalArgumentException("NOT MARK MESSAGE");
            getUi().onMarkAction(message.getPlayer(),message.getSelected(),message.getValue());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void moveRequest() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            MoveRequestMessage message=gson.fromJson(line, MoveRequestMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.EFFECT_MOVE_REQUEST_MESSAGE)) throw new IllegalArgumentException("NOT MOVE REQUEST MESSAGE");
            getUi().onMoveRequest(message.getMatrix(),message.getTargetPlayer());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void moveAction() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            MoveMessage message=gson.fromJson(line, MoveMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.EFFECT_MOVE_MESSAGE)) throw new IllegalArgumentException("NOT MOVE MESSAGE");
            getUi().onMoveAction(message.getPlayer());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void turnEnd() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            TurnEndMessage message=gson.fromJson(line, TurnEndMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.TURN_END_MESSAGE)) throw new IllegalArgumentException("NOT TURN END MESSAGE");
            getUi().onTurnEnd();
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void turnActions() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            TurnActionsMessage message=gson.fromJson(line, TurnActionsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.TURN_AVAILABLE_ACTIONS)) throw new IllegalArgumentException("NOT TURN ACTIONS MESSAGE");
            getUi().onTurnActions(message.getActions());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void turnCreation() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            TurnCreationMessage message=gson.fromJson(line, TurnCreationMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.TURN_CREATION_MESSAGE)) throw new IllegalArgumentException("NOT TURN CREATION MESSAGE");
            getUi().onTurnCreation(message.getCurrentPlayer());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void reloadableWeapons() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            ReloadableWeaponsMessage message=gson.fromJson(line, ReloadableWeaponsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RELOADABLE_WEAPONS_MESSAGE)) throw new IllegalArgumentException("NOT RELOADABLE WEAPONS MESSAGE");
            getUi().onReloadableWeapons(message.getWeapons());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void reloadedWeapon() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            WeaponReloadedMessage message=gson.fromJson(line, WeaponReloadedMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RELOAD_COMPLETED)) throw new IllegalArgumentException("NOT RELOAD WEAPON MESSAGE");
            getUi().onReloadedWeapon(message.getPlayer(),message.getWeapon(),message.getDiscardedPowerups(),message.getTotalCost());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void grabbedWeapon() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            GrabbedWeaponMessage message=gson.fromJson(line, GrabbedWeaponMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.GRABBED_WEAPON_MESSAGE)) throw new IllegalArgumentException("NOT GRABBED WEAPON MESSAGE");
            getUi().onGrabbedWeapon(message.getPlayer(),message.getWeapon());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void discardWeapon() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            DiscardWeaponMessage message=gson.fromJson(line, DiscardWeaponMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.GRABBED_TILE_MESSAGE)) throw new IllegalArgumentException("NOT DISCARD WEAPON MESSAGE");
            getUi().onDiscardWeapon(message.getWeapons());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void grabbableWeapons() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            GrabbableWeaponsMessage message=gson.fromJson(line, GrabbableWeaponsMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.GRABBED_TILE_MESSAGE)) throw new IllegalArgumentException("NOT GRABBABLE WEAPON MESSAGE");
            getUi().onGrabbableWeapons(message.getWeapons());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void grabbedPowerup() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            GrabbedPowerupMessage message=gson.fromJson(line, GrabbedPowerupMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.GRABBED_TILE_MESSAGE)) throw new IllegalArgumentException("NOT GRABBED POWERUP MESSAGE");
            getUi().onGrabbedPowerup(message.getPlayer(),message.getPowerup());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void grabbedTile() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            GrabbedAmmoTileMessage message=gson.fromJson(line, GrabbedAmmoTileMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.GRABBED_TILE_MESSAGE)) throw new IllegalArgumentException("NOT GRABBED TILE MESSAGE");
            getUi().onGrabbedTile(message.getPlayer(),message.getGrabbedTile());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void respwanCompleted() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            RespawnMessage message=gson.fromJson(line, RespawnMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RESPAWN_COMPLETED_MESSAGE)) throw new IllegalArgumentException("NOT RESPAWN MESSAGE");
            getUi().onRespwanCompleted(message.getPlayer(),message.getDiscardedPowerup());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void respwanRequest() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            RespawnRequestMessage message=gson.fromJson(line, RespawnRequestMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.RESPAWN_REQUEST_MESSAGE)) throw new IllegalArgumentException("NOT RESPAWN REQUEST MESSAGE");
            getUi().onRespwanRequest(message.getPowerups());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void matchUpdate() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            MatchUpdateMessage message=gson.fromJson(line, MatchUpdateMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.UPDATE_MESSAGE)) throw new IllegalArgumentException("NOT UPDATE MESSAGE");
            getUi().onMatchUpdate(message.getPlayers(),message.getGameBoard(),message.isFrenzy());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void boardUpdate() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            BoardUpdateMessage message=gson.fromJson(line, BoardUpdateMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.BOARD_UPDATE_MESSAGE)) throw new IllegalArgumentException("NOT BOARD UPDATE MESSAGE");
            getUi().onBoardUpdate(message.getGameBoard());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void invalidMessageReceived() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            InvalidAnswerMessage message=gson.fromJson(line, InvalidAnswerMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.INVALID_ANSWER)) throw new IllegalArgumentException("NOT INVALID ANSWER MESSAGE");
            getUi().onInvalidMessageReceived(message.getMsg());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void matchCreation() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            MatchCreationMessage message=gson.fromJson(line, MatchCreationMessage.class);
            if(!message.getRequest().equalsIgnoreCase(Constants.CREATION_MESSAGE)) throw new IllegalArgumentException("NOT CREATION MESSAGE");
            getUi().onMatchCreation(message.getPlayers(),message.getPlayerTurnNumber());
        }catch (Exception e){
            Logger.logErr(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    /*------ ROOM RECEIVED MESSAGES ------*/

    private void exitPlayer() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            ExitMessage message=gson.fromJson(line, ExitMessage.class);
            if(!message.getType().equalsIgnoreCase(Constants.PLAYER_EXIT)) throw new IllegalArgumentException("NOT EXIT MESSAGE");
            getUi().onExitRoomAdvise(message.getPlayer());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void firstPlayer() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            FirstInMessage message=gson.fromJson(line, FirstInMessage.class);
            if(!message.getType().equalsIgnoreCase(Constants.FIRST_PLAYER)) throw new IllegalArgumentException("NOT FIRST IN MESSAGE");
            getUi().onFirstInRoomAdvise();
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void joinPlayer() {
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        Logger.log(LOG_JSON +line);
        try{
            JoinMessage message=gson.fromJson(line, JoinMessage.class);
            if(!message.getType().equalsIgnoreCase(Constants.PLAYER_JOIN)) throw new IllegalArgumentException("NOT JOIN MESSAGE");
            getUi().onJoinRoomAdvise(message.getPlayer());
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    private void ping(){
        Gson gson=new Gson();
        String line=socketIn.nextLine();
        //Logger.log(LOG_JSON +line);
        Logger.log("[KEEPING ALIVE (SOCKET)]");
        try{
            PingMessage message=gson.fromJson(line, PingMessage.class);
            if(!message.getType().equalsIgnoreCase(Constants.PING_CHECK)) throw new IllegalArgumentException("NOT PING MESSAGE");
            PongAnswer pong= new PongAnswer();
            socketOut.println(pong.getType());
            socketOut.println(gson.toJson(pong));
            socketOut.flush();
        }catch (Exception e){
            Logger.log(e.getMessage());
            //HANDLE ERRORS HERE
            handleInvalidReceived();
        }
    }

    //TODO
    private void handleInvalidReceived() {
    }
}
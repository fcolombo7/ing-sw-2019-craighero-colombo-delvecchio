package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ShootingRoutine;
import it.polimi.ingsw.model.enums.GameStatus;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

/**
 * This class represent the controller of the pattern MVC.
 */
public class Controller{
    /**
     * This attribute contains the model reference
     */
    private Game game;

    /**
     * This atrtibute contains the room associated to this match
     */
    private Room room;

    /**
     * This attribute contains all the disconnected players
     */
    private List<Player> disconnected;

    /**
     * This atrtibute contains the reference between players and their board selection message
     */
    private Map<String,Integer> boardPreference;

    /**
     * This attribute contains the player index used to change the current player
     */
    private int playerIndex;

    /**
     * This attribute contains the timer used for choosing the board
     */
    private Timer boardTimer;

    /**
     * This attribute contains the reference between player and their associated timers
     */
    private Map<String, Timer> timerMap;

    /**
     * This attribute contains the reference between timer and their associated functionalities (TURN/RESPAWN)
     */
    private Map<Timer,String> executionMap;

    /**
     * This attribute contains the last timer before closing the room when the match is ended
     */
    private Timer lastTimer;

    /**
     * This constructor instantiates a controller
     * @param game representing the model
     * @param room representing the room associated to the current match
     */
    public Controller(Game game, Room room) {
        this.game = game;
        this.room=room;
        playerIndex=-1;
        boardPreference=new HashMap<>();
        timerMap=new HashMap<>();
        executionMap=new HashMap<>();
    }

    /**
     * This method starts the controller: it send a match creation advise and listen to board preference form the users
     */
    public void start() {
        disconnected=new ArrayList<>(game.getPlayers().size());
        game.startMessage();
        boardTimer=new Timer();
        boardTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(game.getStatus()!=GameStatus.END) {
                    Logger.logErr("Timeout for choosing the board.");
                    setGameboard();
                }
            }
        }, Server.getQuickMoveTimer()*1000);
    }

    /*---------- METHODS USED IN CLIENT CONNECTION ----------*/

    /**
     * This method is used to handel the board preference
     * @param sender the sender of the message
     * @param boardNumber the board number chosen from the sender
     */
    public void roomPreferenceManager(String sender, int boardNumber) {
        if(game.getStatus()!=GameStatus.END) {
            if (boardPreference.containsKey(sender)) return;
            ArrayList<Integer> values=new ArrayList<>();
            values.add(1);
            values.add(2);
            values.add(3);
            values.add(4);
            for (Integer value : values) {
                if (value.equals(boardNumber)) {
                    boardPreference.put(sender, boardNumber);
                    break;
                }
            }
            if (boardPreference.size() == game.getPlayers().size()) {
                boardTimer.cancel();
                boardTimer.purge();
                setGameboard();
            }
        }
    }

    /**
     * This method is used to handle the player respawn
     * @param sender representing who will be spawned
     * @param powerup representing the discarded powerup
     * @param notAlive a boolean representing if the keep alive time is up
     */
    public synchronized void respawnPlayer(String sender, Card powerup,boolean notAlive){
        Logger.logAndPrint("RP -TRY NEW RESPAWN HANDLE "+ sender);
        if(game.getStatus()!=GameStatus.END) {
            Logger.logAndPrint("RP - GAME NOT END "+ sender);
            if (!isDisconnected(sender)&&!notAlive) {
                Logger.logAndPrint("RP - NOT DISCONNECTED"+ sender);
                Timer t = timerMap.get(sender);
                t.cancel();
                t.purge();
                timerMap.remove(sender);
                executionMap.remove(t);
            }
            for (Player p : game.getPlayers()) {
                if (p.getNickname().equals(sender)) {
                    game.respawnPlayer(p, powerup);
                    if (p.getNickname().equals(game.getCurrentPlayer().getNickname())) {
                        if (!isDisconnected(sender)&&!notAlive) {
                            Logger.logAndPrint("RP - NOT DISCONNECTED"+ sender);
                            Timer t = new Timer();
                            timerMap.put(p.getNickname(), t);
                            executionMap.put(t,"TURN");
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Logger.logErr("TURN TIMEOUT (" + p.getNickname() + ")");
                                    room.forceDisconnection(p.getNickname());
                                    executionMap.remove(t);
                                    timerMap.remove(p.getNickname());
                                    try{
                                        ((ShootingRoutine)game.getCurrentTurn().getInExecutionRoutine()).cancelCounterAttackTimers();
                                        Logger.logAndPrint("Counter attack canceled in the shooting routine .");
                                    }catch(Exception e){
                                        Logger.logAndPrint("No shooting routine in execution.");
                                    }
                                    game.getCurrentTurn().forceClosing();
                                    closeTurn(p.getNickname());
                                }
                            }, Server.getTurnTimer() * 1000);
                            game.createTurn();
                        } else
                            handleNewTurn();
                        //return;
                    }else {
                        //nextPlayer();
                        handleNewTurn();
                    }
                    return;
                }
            }
            throw new IllegalStateException("INVALID PLAYER RECEIVED");
        }
    }

    /**
     * This method handle the turn closing and start a new one
     * @param sender the player which has closed the turn
     */
    public void closeTurn(String sender){
        if(!isDisconnected(sender)){
            Timer t=timerMap.get(sender);
            if (t!=null) {
                t.cancel();
                t.purge();
                executionMap.remove(t);
            }
            timerMap.remove(sender);
            Logger.logAndPrint("CLOSE TURN OF A NON DISCONNECTED PLAYER");
        }
        if (game.getCurrentPlayer().getNickname().equals(sender)) {
            Logger.logAndPrint("CLOSE TURN OF THE CURRENT PLAYER ("+sender+")");
            game.endTurn();
            handleDeads();
        }else{
            Logger.logAndPrint("CLOSE TURN: UNHANDLE SITUATION ? ("+sender+")");
        }
    }

    /**
     * This method set the turn action
     * @param action representing the action that will be executed
     */
    public void selectAction(String action){
        game.getCurrentTurn().selectAction(action);
    }

    /**
     * This method handle the player move
     * @param target representing who is moved on the board
     * @param newPosition his new position
     */
    public void movePlayer(String target,int[] newPosition){
        game.getCurrentTurn().getCurEffect().handleMoveAnswer(game.getCurrentTurn(),target,newPosition);
    }

    /**
     * This method handle the weapon discard
     * @param weapon representing the discarded weapon
     */
    public void discardWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new DiscardedWeaponAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }

    /**
     * This method handle the effect selection
     * @param effectName the name of the effect
     */
    public void selectEffect(String effectName){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new EffectAnswer(game.getCurrentPlayer().getNickname(),effectName));
    }

    /**
     * This method handle the loadable weapon
     * @param weapon the loadable weapon
     */
    public void loadableWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new LoadableWeaponSelectedAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }

    /**
     * This method handle the run action message
     * @param newPosition representing the new poition of the current player
     */
    public void runAction(int[] newPosition){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new RunAnswer(game.getCurrentPlayer().getNickname(),newPosition));
    }

    /**
     * This method handle the selection of the players which will be the target of the effect chosen by the user
     * @param selected the player selected
     */
    public void selectPlayers(List<List<String>> selected){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(game.getCurrentPlayer().getNickname(),selected));
    }

    /**
     * This method handle powerup selection
     * @param powerup representing the powerup which is selected
     */
    public void selectPowerup(Card powerup){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new SelectedPowerupAnswer(game.getCurrentPlayer().getNickname(),powerup));
    }

    /**
     * This method handle the routine stop message
     * @param stop representing the possibility of stop the routine
     */
    public void stopRoutine(boolean stop){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(game.getCurrentPlayer().getNickname(),stop));
    }

    /**
     * This method handle the use powerup message
     * @param use representing the possibility of using the powerup
     */
    public void usePowerup(boolean use){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new UsePowerupAnswer(game.getCurrentPlayer().getNickname(),use));
    }

    /**
     * This method handle the select weapon message
     * @param weapon representing the selected weapon
     */
    public void selectWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new WeaponAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }

    /**
     * This method handle the counterattack answer
     * @param nickname who send
     * @param counterAttack if counterattack
     */
    public void counterAttackAnswer(String nickname, boolean counterAttack) {
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new CounterAttackAnswer(nickname,counterAttack));
    }

    /*-------- OTHER METHODS --------*/

    /**
     * This method returns the game status
     * @return the game status
     */
    public GameStatus getGameStatus() {
        return game.getStatus();
    }

    /**
     * This methods check if the param player is the current player
     * @param nickname who need to be checked
     * @return true if the param player is the current player
     */
    public boolean isPlaying(String nickname) {
        return game.getCurrentPlayer().getNickname().equals(nickname);
    }

    /**
     * This method is used to set the gameboard of the game
     */
    private void setGameboard() {
        int selBoard=mostCommonBoard();
        game.setGameBoard(selBoard);
        handleNewTurn();
    }

    /**
     * This method starts a new turn of the match and the relatives timer if the player does not answer in time.
     */
    private void handleNewTurn() {
        Logger.logAndPrint("ht -TRY NEW turn HANDLE ");
        if(game.getStatus()!=GameStatus.END) {
            Logger.logAndPrint("ht -NOT GAME END ");
            try {
                nextPlayer();
                if (game.getCurrentPlayer().getStatus() == PlayerStatus.FIRST_SPAWN) {
                    Timer t = new Timer();
                    timerMap.put(game.getCurrentPlayer().getNickname(), t);
                    executionMap.put(t, "RESPAWN");
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Logger.logErr("RESPAWN TIMEOUT (" + game.getCurrentPlayer().getNickname() + ")");
                            executionMap.remove(t);
                            timerMap.remove(game.getCurrentPlayer().getNickname());
                            room.forceDisconnection(game.getCurrentPlayer().getNickname());
                            respawnPlayer(game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getPowerups().get(0), false);
                        }
                    }, Server.getQuickMoveTimer() * 1000);
                    game.respawnPlayerRequest(game.getCurrentPlayer(), true);
                } else {
                    Timer t = new Timer();
                    timerMap.put(game.getCurrentPlayer().getNickname(), t);
                    executionMap.put(t, "TURN");
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Logger.logErr("TURN TIMEOUT (" + game.getCurrentPlayer().getNickname() + ")");
                            executionMap.remove(t);
                            timerMap.remove(game.getCurrentPlayer().getNickname());
                            room.forceDisconnection(game.getCurrentPlayer().getNickname());
                        }
                    }, Server.getTurnTimer() * 1000);
                    game.createTurn();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method update the current player
     */
    public void nextPlayer() {
        if(playerIndex==-1)
            playerIndex=game.getPlayers().indexOf(game.getCurrentPlayer());
        else
            incPlayerIndex();
        while(disconnected.contains(game.getPlayers().get(playerIndex))){
            incPlayerIndex();
        }
        game.setCurrentPlayer(playerIndex);
    }

    /**
     * This methos is used to update the current player
     */
    private void incPlayerIndex() {
        if(playerIndex==game.getPlayers().size()-1)
            playerIndex=0;
        else
            playerIndex++;
    }

    /**
     * This method returns the most common board chosen by the player
     * @return the most common board chosen by the player
     */
    private int mostCommonBoard() {
        if(boardPreference.isEmpty()) return 1;
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer value : boardPreference.values()) {
            Integer val = map.get(value);
            map.put(value, val == null ? 1 : val + 1);
        }

        Map.Entry<Integer, Integer> max = null;

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        return max==null?boardPreference.get(0):max.getKey();
    }

    /**
     * This method handle the in turn deads
     */
    private void handleDeads() {
        boolean found=false;
        for(Player player:game.getPlayers()){
            if(player.getStatus()==PlayerStatus.DEAD){
                found=true;
                Timer t=new Timer();
                executionMap.put(t,"RESPAWN");
                timerMap.put(player.getNickname(),t);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Logger.logErr("RESPAWN TIMEOUT ("+player.getNickname()+")");
                        executionMap.remove(t);
                        room.forceDisconnection(player.getNickname());
                        timerMap.remove(player.getNickname());
                        respawnPlayer(player.getNickname(),player.getPowerups().get(0),false);
                    }
                },Server.getQuickMoveTimer()*1000);
                game.respawnPlayerRequest(player,false);
            }
        }
        if(!found)
            handleNewTurn();
    }

    /**
     * This method add a player to the disconnected set
     * @param nickname representing the player which is disconnected
     */
    public void addDisconnected(String nickname){
        for(Player p:game.getPlayers()){
            if(p.getNickname().equals(nickname)) {
                if (!disconnected.contains(p)) {
                    disconnected.add(p);
                    debug();
                    if (game.getPlayers().size() - disconnected.size() < Server.getMinPlayerNumber()) {
                        for (Timer t : timerMap.values()) {
                            t.cancel();
                            t.purge();
                            executionMap.remove(t);
                        }
                        timerMap.clear();
                        if (game.getStatus() == GameStatus.PLAYING_TURN) {
                            try {
                                ((ShootingRoutine) game.getCurrentTurn().getInExecutionRoutine()).cancelCounterAttackTimers();
                                Logger.logAndPrint("Counter attack canceled in the shooting routine .");
                            } catch (Exception e) {
                                Logger.logAndPrint("No shooting routine in execution.");
                            }
                            game.getCurrentTurn().forceClosing();
                            closeTurn(game.getCurrentPlayer().getNickname());
                        }
                        game.forceEndGame();
                        if (lastTimer == null) {
                            lastTimer = new Timer();
                            lastTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    room.close();
                                }
                            }, Server.getQuickMoveTimer() * 1000);
                        }
                    } else if (game.getStatus() == GameStatus.PLAYING_TURN && game.getCurrentPlayer().getNickname().equals(nickname)) {
                        Timer t = timerMap.get(game.getCurrentPlayer().getNickname());
                        if (t != null) {
                            t.cancel();
                            t.purge();
                            executionMap.remove(t);
                            timerMap.remove(game.getCurrentPlayer().getNickname());
                        }
                        try {
                            ((ShootingRoutine) game.getCurrentTurn().getInExecutionRoutine()).cancelCounterAttackTimers();
                            Logger.logAndPrint("Counter attack canceled in the shooting routine .");
                        } catch (Exception e) {
                            Logger.logAndPrint("No shooting routine in execution.");
                        }
                        game.getCurrentTurn().forceClosing();
                        closeTurn(game.getCurrentPlayer().getNickname());
                    }
                    return;
                }
            }
        }
    }

    /**
     * This method check if is disconnected the parameter
     * @param nickname the player you want to check if is disconnected
     * @return true if is disconnected
     */
    public boolean isDisconnected(String nickname) {
        for(Player p:disconnected){
            if(p.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    /**
     * this method return the player to recover
     * @param nickname representing the player to recover
     * @return the player to recover
     */
    public Player getPlayerToRecover(String nickname) {
        for(Player p:disconnected){
            if(p.getNickname().equals(nickname))
                return p;
        }
        throw new IllegalStateException("Cannot found '"+nickname+"' in the disconnected players");
    }

    /**
     * This method is used to recover a player
     * @param player the player to recover
     */
    public void recoverPlayer(Player player) {
        for(Player p:disconnected){
            if(p.equals(player)){
                disconnected.remove(player);
                game.recoverPlayer(player);
                debug();
                return;
            }
        }
        throw new IllegalStateException("Cannot found '"+player.getNickname()+"' in the disconnected players");
    }

    /**
     * This method is used to cancel the timer if a keep alive time is up
     * @param nickname representing the player whose keep alive time is up
     */
    public void  cancelTimerAfterKeepAlive(String nickname){
        Timer t= timerMap.get(nickname);
        if(t!=null){
            t.cancel();
            t.purge();
            timerMap.remove(nickname);
            String str=executionMap.get(t);
            if(str.equals("RESPAWN")){
                Logger.logErr("IN EXECUTION TIMER WAS RESPAWN ("+nickname+")");
                executionMap.remove(t);
                timerMap.remove(nickname);
                for(Player p: game.getPlayers()){
                    if(p.getNickname().equals(nickname)){
                        respawnPlayer(p.getNickname(),p.getPowerups().get(0),true);
                        break;
                    }
                }
            }else if(str.equals("TURN")){
                Logger.logErr("IN EXECUTION TIMER WAS TURN (" + nickname + ")");
                executionMap.remove(t);
                timerMap.remove(nickname);
            }else throw new IllegalStateException("Execution map error");
        }
    }

    /**
     * This method is used to debug who is out and who is in the game
     */
    private void debug(){
        StringBuilder builder=new StringBuilder();
        builder.append("Offline players: ");
        for(Player p:disconnected){
            builder.append(p.getNickname()).append(" ");
        }
        Logger.logAndPrint(builder.toString());
    }

    /**
     * This method is used to receive the game end ack from the players and send him the leaderboard
     * @param sender the player who send the ack
     */
    public synchronized void gameEndAck(String sender) {
        if(lastTimer==null){
            lastTimer=new Timer();
            lastTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //TODO: need to be checked
                    room.close();
                }
            },Server.getQuickMoveTimer()*1000);
        }
        room.stopTimers(sender);
        game.sendLeaderBoard(sender,disconnected);
    }
}
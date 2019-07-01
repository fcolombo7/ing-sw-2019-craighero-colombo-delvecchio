package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GameStatus;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.exceptions.MatchConfigurationException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.util.*;

public class Controller{
    private Game game;
    private Room room;
    private List<Player> disconnected;
    private Map<String,Integer> boardPreference;
    private int playerIndex;
    private Timer boardTimer;
    private Map<String, Timer> timerMap;
    private Timer lastTimer;

    public Controller(Game game, Room room) {
        this.game = game;
        this.room=room;
        playerIndex=-1;
        boardPreference=new HashMap<>();
        timerMap=new HashMap<>();
    }

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
    public void roomPreferenceManager(String sender, int boardNumber) {
        if(game.getStatus()!=GameStatus.END) {
            if (boardPreference.containsKey(sender)) return;
            String folderName = Constants.BOARD_FOLDER;
            File folder = new File(folderName);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) throw new MatchConfigurationException("No boards in " + folderName);
            for (File file : listOfFiles) {
                if (file.getName().equalsIgnoreCase("board" + boardNumber + ".xml")) {
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

    public synchronized void respawnPlayer(String sender, Card powerup){
        if(game.getStatus()!=GameStatus.END) {
            if (!isDisconnected(sender)) {
                Timer t = timerMap.get(sender);
                t.cancel();
                t.purge();
                timerMap.remove(sender);
            }
            for (Player p : game.getPlayers()) {
                if (p.getNickname().equals(sender)) {
                    game.respawnPlayer(p, powerup);
                    if (p.getStatus() == PlayerStatus.PLAYING) {
                        if (!isDisconnected(sender)) {
                            Timer t = new Timer();
                            timerMap.put(p.getNickname(), t);
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Logger.logErr("TURN TIMEOUT (" + p.getNickname() + ")");
                                    room.forceDisconnection(p.getNickname());
                                    timerMap.remove(p.getNickname());
                                    game.getCurrentTurn().forceClosing();
                                    closeTurn(p.getNickname());
                                }
                            }, Server.getTurnTimer() * 1000);
                            game.createTurn();
                        } else
                            handleNewTurn();
                        return;
                    }
                    if (timerMap.isEmpty()) //IF IS EMPTY THEN ALL THE DEAD PLAYER HAS BEEN RESPAWNED
                        handleNewTurn();
                    return;
                }
            }
            throw new IllegalStateException("INVALID PLAYER RECEIVED");
        }
    }

    public void closeTurn(String sender){
        if(!isDisconnected(sender)){
            Timer t=timerMap.get(sender);
            t.cancel();
            t.purge();
            timerMap.remove(sender);
        }
        if (game.getCurrentPlayer().getNickname().equals(sender)) {
            game.endTurn();
            handleDeads();
            return;
        }
        //throw new IllegalStateException("INVALID PLAYER RECEIVED");
    }
    
    public void selectAction(String action){
        game.getCurrentTurn().selectAction(action);
    }

    public void movePlayer(String target,int[] newPosition){
        game.getCurrentTurn().getCurEffect().handleMoveAnswer(game.getCurrentTurn(),target,newPosition);
    }
    
    public void discardWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new DiscardedWeaponAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }

    public void selectEffect(String effectName){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new EffectAnswer(game.getCurrentPlayer().getNickname(),effectName));
    }

    public void loadableWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new LoadableWeaponSelectedAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }
    
    public void runAction(int[] newPosition){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new RunAnswer(game.getCurrentPlayer().getNickname(),newPosition));
    }

    public void selectPlayers(List<List<String>> selected){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(game.getCurrentPlayer().getNickname(),selected));
    }

    public void selectPowerup(Card powerup){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new SelectedPowerupAnswer(game.getCurrentPlayer().getNickname(),powerup));
    }

    public void stopRoutine(boolean stop){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(game.getCurrentPlayer().getNickname(),stop));
    }

    public void usePowerup(boolean use){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new UsePowerupAnswer(game.getCurrentPlayer().getNickname(),use));
    }
    
    public void selectWeapon(Card weapon){
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new WeaponAnswer(game.getCurrentPlayer().getNickname(),weapon));
    }

    public void counterAttackAnswer(String nickname, boolean counterAttack) {
        game.getCurrentTurn().getInExecutionRoutine().handleAnswer(new CounterAttackAnswer(nickname,counterAttack));
    }

    /*-------- OTHER METHODS --------*/
    public GameStatus getGameStatus() {
        return game.getStatus();
    }

    public boolean isPlaying(String nickname) {
        return game.getCurrentPlayer().getNickname().equals(nickname);
    }
    private void setGameboard() {
        int selBoard=mostCommonBoard();
        game.setGameBoard(selBoard);
        handleNewTurn();
    }

    private void handleNewTurn() {
        if(game.getStatus()!=GameStatus.END) {
            nextPlayer();
            if (game.getCurrentPlayer().getStatus() == PlayerStatus.FIRST_SPAWN) {
                Timer t = new Timer();
                timerMap.put(game.getCurrentPlayer().getNickname(), t);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Logger.logErr("RESPAWN TIMEOUT (" + game.getCurrentPlayer().getNickname() + ")");
                        room.forceDisconnection(game.getCurrentPlayer().getNickname());
                        timerMap.remove(game.getCurrentPlayer().getNickname());
                        respawnPlayer(game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getPowerups().get(0));
                    }
                }, Server.getQuickMoveTimer() * 1000);
                game.respawnPlayerRequest(game.getCurrentPlayer(), true);
            } else {
                Timer t = new Timer();
                timerMap.put(game.getCurrentPlayer().getNickname(), t);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Logger.logErr("TURN TIMEOUT (" + game.getCurrentPlayer().getNickname() + ")");
                        timerMap.remove(game.getCurrentPlayer().getNickname());
                        room.forceDisconnection(game.getCurrentPlayer().getNickname());
                    }
                }, Server.getTurnTimer() * 1000);
                game.createTurn();
            }
        }
    }

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

    private void incPlayerIndex() {
        if(playerIndex==game.getPlayers().size()-1)
            playerIndex=0;
        else
            playerIndex++;
    }

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

    private void handleDeads() {
        boolean found=false;
        for(Player player:game.getPlayers()){
            if(player.getStatus()==PlayerStatus.DEAD){
                found=true;
                Timer t=new Timer();
                timerMap.put(player.getNickname(),t);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Logger.logErr("RESPAWN TIMEOUT ("+player.getNickname()+")");
                        room.forceDisconnection(player.getNickname());
                        timerMap.remove(player.getNickname());
                        respawnPlayer(player.getNickname(),player.getPowerups().get(0));
                    }
                },Server.getQuickMoveTimer()*1000);
                game.respawnPlayerRequest(player,false);
            }
        }
        if(!found)
            handleNewTurn();
    }

    public void addDisconnected(String nickname){
        for(Player p:game.getPlayers()){
            if(p.getNickname().equals(nickname)){
                disconnected.add(p);
                debug();
                if(game.getPlayers().size()-disconnected.size()<Server.getMinPlayerNumber()){
                    for (Timer t:timerMap.values()){
                        t.cancel();
                        t.purge();
                    }
                    timerMap.clear();
                    if(game.getStatus()==GameStatus.PLAYING_TURN) {
                        game.getCurrentTurn().forceClosing();
                        closeTurn(game.getCurrentPlayer().getNickname());
                    }
                    game.forceEndGame();
                }else if(game.getStatus()==GameStatus.PLAYING_TURN&&game.getCurrentPlayer().getNickname().equals(nickname)){
                    Timer t=timerMap.get(game.getCurrentPlayer().getNickname());
                    t.cancel();
                    t.purge();
                    timerMap.remove(game.getCurrentPlayer().getNickname());
                    game.getCurrentTurn().forceClosing();
                    closeTurn(game.getCurrentPlayer().getNickname());
                    handleNewTurn();
                }
                return;
            }
        }
    }

    public boolean isDisconnected(String nickname) {
        for(Player p:disconnected){
            if(p.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    public Player getPlayerToRecover(String nickname) {
        for(Player p:disconnected){
            if(p.getNickname().equals(nickname))
                return p;
        }
        throw new IllegalStateException("Cannot found '"+nickname+"' in the disconnected players");
    }

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

    private void debug(){
        StringBuilder builder=new StringBuilder();
        builder.append("Offline players: ");
        for(Player p:disconnected){
            builder.append(p.getNickname()).append(" ");
        }
        Logger.logServer(builder.toString());
    }

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
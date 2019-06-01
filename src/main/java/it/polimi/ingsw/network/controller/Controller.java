package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GameStatus;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.exceptions.MatchConfigurationException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller{
    private Game game;
    private List<Player> suspended;
    private List<Player> disconnected;
    private List<Integer> boardPreference;

    public Controller(Game game) {
        this.game = game;
    }

    public GameStatus getGameStatus() {
        return game.getStatus();
    }

    public boolean isPlaying(String nickname) {
        return game.getCurrentPlayer().getNickname().equals(nickname);
    }

    public void start() {
        boardPreference=new ArrayList<>(game.getPlayers().size());
        suspended=new ArrayList<>(game.getPlayers().size());
        disconnected=new ArrayList<>(game.getPlayers().size());

        game.startMessage();
    }

    /*---------- METHODS USED IN CLIENT CONNECTION ----------*/

    //TODO: GESTIONE IN CASO DI STESSO PLAYER RICEVUTO
    public void roomPreferenceManager(String sender, int boardNumber) {
        String folderName= Constants.BOARD_FOLDER;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles==null) throw new MatchConfigurationException("No boards in "+folderName);
        for(File file:listOfFiles){
            if(file.getName().equalsIgnoreCase("board"+boardNumber+".xml")) {
                boardPreference.add(boardNumber);
                break;
            }
        }
        if(boardPreference.size()==game.getPlayers().size()){
            int selBoard=mostCommonBoard();
            game.setGameBoard(selBoard);
        }
    }
    private int mostCommonBoard() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer value : boardPreference) {
            Integer val = map.get(value);
            map.put(value, val == null ? 1 : val + 1);
        }

        Map.Entry<Integer, Integer> max = null;

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max==null?boardPreference.get(0):max.getValue();
    }

    //TODO: NOT FIRST SPAWN --> TIMER FOR NEW TURN && SUSPEND
    public void respawnPlayer(String sender, Card powerup){
        for(Player p:game.getPlayers()) {
            if (p.getNickname().equals(sender)) {
                game.respawnPlayer(p, powerup);
                if(p.getStatus()== PlayerStatus.FIRST_SPAWN)
                    game.createTurn();
                return;
            }
        }
        throw new IllegalStateException("INVALID PLAYER RECEIVED");
    }

    //TODO: NOT FIRST SPAWN --> TIMER FOR NEW TURN && SUSPEND
    public void closeTurn(String sender){
        for(Player p:game.getPlayers()) {
            if (p.getNickname().equals(sender)) {
                game.endTurn();
                handleDeads();
                return;
            }
        }
        throw new IllegalStateException("INVALID PLAYER RECEIVED");
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

    /*-------- OTHER METHODS --------*/

    //TODO:STARTING TIMER
    private void handleDeads() {
        for(Player player:game.getPlayers()){
            if(player.getStatus()==PlayerStatus.DEAD){
                game.respawnPlayerRequest(player,false);
            }
        }
    }

    public void addDisconnected(String nickname){
        for(Player p:game.getPlayers()){
            if(p.getNickname().equals(nickname)){
                disconnected.add(p);
                suspended.remove(p);
                return;
            }
        }
    }

}
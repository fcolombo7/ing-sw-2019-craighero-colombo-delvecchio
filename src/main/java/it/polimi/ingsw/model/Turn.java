package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

public class Turn {
    private Game game;
    private TurnStatus status;
    private Deque<Player> shotPlayer;
    private int routineNumber;
    private TurnRoutine curRoutine;
    private List<String> actions;

    public Turn(Game game){
        this.game=game;
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                routineNumber=1;
        }
        else routineNumber=2;
        status=TurnStatus.WAITING_PLAYER;
        shotPlayer=new ArrayDeque<>();
        curRoutine=null;
        actions=null;
        availableActions();
    }

    public void selectAction(ActionSelectedAnswer answer){
        for(String action:actions){
            if(answer.getSelection().equalsIgnoreCase(action)){
                if(action.equalsIgnoreCase("SHOT")) {
                    createRoutine("SHOT");
                    return;
                }else if(action.equalsIgnoreCase("RUN")) {
                    createRoutine("RUN");
                    return;
                }
                else if(action.equalsIgnoreCase("GRAB")) {
                    createRoutine("GRAB");
                    return;
                }
                else if(action.equalsIgnoreCase("RELOAD")) {
                    sendWeapons();
                    return;
                }else if(action.equalsIgnoreCase("POWERUP")) {
                    sendPowerups();
                    return;
                }
            }
        }
        Logger.log("Invalid action received");
        game.sendInvalidAction("Invalid action received");
    }

    private void availableActions() {
        actions=new ArrayList<>();
        for(TurnRoutineType type:TurnRoutineType.values()){
            if(canPerformRoutine(type.name()))
                actions.add(type.name());
        }
        if(game.getCurrentPlayer().hasTimingPowerup(status))actions.add("POWERUP");
        if(canReload())actions.add("RELOAD");
        if(actions.isEmpty()){
            status=TurnStatus.END;
            game.endTurn();
        }else
            game.sendAvailableActions(actions);
    }

    private void sendPowerups(){
        List<Card> usablePowerups=new ArrayList<>();
        for (Powerup p:game.getCurrentPlayer().getPowerups()) {
            if(p.getTiming()==status)
                usablePowerups.add(new Card(p));
        }
        game.sendUsablePowerups(usablePowerups);
    }

    private void sendWeapons(){
        List<Card> loadableWeapons=new ArrayList<>();
        List<Color> startingAmmo=game.getCurrentPlayer().getBoard().getAmmo();
        for (Powerup p:game.getCurrentPlayer().getPowerups()) {
            startingAmmo.add(p.getColor());
        }
        for (Weapon w:game.getCurrentPlayer().getWeapons()){
            if(!w.isLoaded()&&canBeReloaded(w,startingAmmo))
                loadableWeapons.add(new Card(w));
        }
        game.sendLoadableWeapons(loadableWeapons);
    }

    protected boolean canReload() {
        List<Color> startingAmmo=game.getCurrentPlayer().getBoard().getAmmo();
        for (Powerup p:game.getCurrentPlayer().getPowerups()) {
            startingAmmo.add(p.getColor());
        }
        for(Weapon w:game.getCurrentPlayer().getWeapons()){
            if(!w.isLoaded()&&canBeReloaded(w,startingAmmo))
                return true;
        }
        return false;
    }

    private boolean canBeReloaded(Weapon w, List<Color> ammo) {
        List<Color> tempAmmo=new ArrayList<>(ammo);
        for (Color color:w.getAmmo()){
            if(tempAmmo.contains(color))
                tempAmmo.remove(color);
            else
                return false;
        }
        return true;
    }

    private void createRoutine(String type){
        if(!canPerformRoutine(type)) throw new IllegalArgumentException();
        TurnRoutineFactory factory=new TurnRoutineFactory(this);
        TurnRoutineType rType=getRoutineType(type);
        curRoutine=factory.getTurnRoutine(rType);
        curRoutine.updateTurnStatus();
        routineNumber--;
    }

    private boolean canPerformRoutine(String type){
        TurnRoutineType rType=getRoutineType(type);
        if(rType!=null){
            TurnRoutineFactory factory=new TurnRoutineFactory(this);
            return factory.getTurnRoutine(rType)!=null;
        }
        return false;
    }

    private TurnRoutineType getRoutineType(String type) {
        for(TurnRoutineType t:TurnRoutineType.values()){
            if(t.name().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }

    public int getRoutineNumber() {
        return routineNumber;
    }

    public Game getGame() {
        return game;
    }

    public Deque<Player> getShotPlayer() {
        return new ArrayDeque<>(shotPlayer);
    }

    public TurnStatus getStatus() {
        return status;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public void sendRoutineMessage(TurnRoutineMessage message) {
        game.routineMessage(message);
    }

    public void endRoutine() {
        //inviare le nuove azioni eseguibili
        curRoutine=null;
    }
}
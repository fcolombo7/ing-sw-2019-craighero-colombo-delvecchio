package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.LoadableWeaponSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.PowerupSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.*;

public class Turn {
    private Game game;
    private TurnStatus status;
    private Deque<Player> shotPlayers;
    private int routineNumber;
    private TurnRoutine curRoutine;
    private List<String> actions;
    private MatrixHelper shiftableMatrix;
    private Map<String, MatrixHelper> movementMap;
    private Effect curEffect;

    public Turn(Game game){
        this.game=game;
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                routineNumber=1;
        }
        else routineNumber=2;
        status=TurnStatus.WAITING_PLAYER;
        shotPlayers =new ArrayDeque<>();
        curRoutine=null;
        actions=null;
        shiftableMatrix=null;
        curEffect=null;
        movementMap = new HashMap<>();
        availableActions(false);
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

    private void availableActions(boolean end) {
        curEffect=null;
        if(!end) {
            actions = new ArrayList<>();
            for (TurnRoutineType type : TurnRoutineType.values()) {
                if (canPerformRoutine(type.name()))
                    actions.add(type.name());
            }
            if (game.getCurrentPlayer().hasTimingPowerup(status)) actions.add("POWERUP");
        }
        if(canReload())actions.add("RELOAD");
        if(actions.isEmpty()){
            status=TurnStatus.END;
            game.endTurn();
        }else
            game.sendAvailableActions(actions);
    }

    private void sendPowerups(){
        List<Card> usablePowerups=new ArrayList<>();
        List<Player> other=game.getPlayers();
        other.remove(game.getCurrentPlayer());
        for (Powerup p:game.getCurrentPlayer().getPowerups()) {
            if(p.getTiming()==status&&p.getEffect().canUse(this))
                usablePowerups.add(new Card(p));
        }
        game.sendUsablePowerups(usablePowerups);
    }

    private void onPowerupReceived(PowerupSelectedAnswer answer){
        Card selPowerup=answer.getPowerup();
        List<Player> other=game.getPlayers();
        other.remove(game.getCurrentPlayer());
        for (Powerup powerup:game.getCurrentPlayer().getPowerups()){
            if(selPowerup.getId().equalsIgnoreCase(powerup.getId())){
                if(powerup.getTiming()==status&&powerup.getEffect().canUse(this)){
                    curEffect=powerup.getEffect();
                    game.sendSelectedPowerup(powerup);
                    curEffect.perform(null,this);
                    availableActions(false);
                }else {
                    Logger.log("Invalid powerup received");
                    game.sendInvalidAction("Cannot use the selected powerup");
                }
            }
        }
        Logger.log("Invalid powerup received");
        game.sendInvalidAction("Current player doesn't have the selected powerup");

    }

    private void sendWeapons(){
        List<Card> loadableWeapons=new ArrayList<>();
        for (Weapon w:game.getCurrentPlayer().getWeapons()){
            if(game.getCurrentPlayer().canReloadedWeapon(w))
                loadableWeapons.add(new Card(w));
        }
        game.sendLoadableWeapons(loadableWeapons);
    }

    private void onLoadableWeaponReceived(LoadableWeaponSelectedAnswer answer){
        Card weapon=answer.getWeapon();
        for(Weapon w:game.getCurrentPlayer().getWeapons()){
            if(weapon.getId().equalsIgnoreCase(w.getId())){
                if(game.getCurrentPlayer().canReloadedWeapon(w)){
                    List<Card> discardedPowerups= game.getCurrentPlayer().reloadWeapon(w);
                    game.sendReloadMessage(discardedPowerups);
                    availableActions(true);
                    return;
                }else{
                    Logger.log("Invalid weapon received");
                    game.sendInvalidAction("Cannot reload the selected weeapon");
                }
            }
        }
        Logger.log("Invalid weapon received");
        game.sendInvalidAction("Current player doesn't have the selected weapon");
    }

    private boolean canReload() {
        for(Weapon w:game.getCurrentPlayer().getWeapons()){
            if(game.getCurrentPlayer().canReloadedWeapon(w))
                return true;
        }
        return false;
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

    public Deque<Player> getShotPlayers() {
        return new ArrayDeque<>(shotPlayers);
    }

    public void addShotPlayer(Player player) {
        shotPlayers.add(player);
    }

    public void clearShotPlayers(){
        shotPlayers.clear();
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
        availableActions(false);
    }

    public void setShiftableMatrix(MatrixHelper matrix) {
        this.shiftableMatrix=new MatrixHelper(matrix.toBooleanMatrix());
    }

    public MatrixHelper getShiftableMatrix() {
        return new MatrixHelper(shiftableMatrix.toBooleanMatrix());
    }

    public void initShiftableMatrix(){
        shiftableMatrix=new MatrixHelper(game.getGameBoard().getGameboardMatrix().toBooleanMatrix());
    }

    public void clearMovementMap(){
        movementMap.clear();
    }

    public void insertPlayerMatrix(Player p, MatrixHelper mat){
        movementMap.put(p.getNickname(),mat);
    }

    public MatrixHelper getPlayerMatrix(Player p){
        return movementMap.get(p.getNickname());
    }

    public Effect getCurEffect() {
        return curEffect;
    }

    public void setCurEffect(Effect curEffect) {
        this.curEffect = curEffect;
    }
}
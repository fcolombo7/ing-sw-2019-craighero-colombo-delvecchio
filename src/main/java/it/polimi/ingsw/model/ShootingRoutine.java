package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TargetType;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.ArrayList;
import java.util.List;

public class ShootingRoutine implements TurnRoutine {

    private boolean checkLoaded;
    private Turn turn;
    private MatrixHelper moveBeforeShot;
    private List<Effect> curAvailableEffects;
    private Effect selEffect;
    private Weapon selWeapon;

    ShootingRoutine(boolean checkLoaded, MatrixHelper moveBeforeShot, Turn turn) {
        this.checkLoaded = checkLoaded;
        this.turn = turn;
        curAvailableEffects = null;
        selEffect = null;
        this.moveBeforeShot = moveBeforeShot!=null?new MatrixHelper(moveBeforeShot.toBooleanMatrix()):null;
        turn.clearSelectedPlayers();
    }

    @Override
    public void start() {
        turn.pushRoutineInStack(this);
        updateTurnStatus();
        if(this.moveBeforeShot!=null)
            startRunRoutine();
        else
            sendAvailableWeapons();
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.WEAPON_ANSWER)){
            onWeaponReceived((WeaponAnswer)answer);
        }
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.EFFECT_ANSWER)){
            onEffectReceived((EffectAnswer)answer);
        }
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.SELECTED_PLAYERS_ANSWER)){
            onSelectedPlayersReceived((SelectedPlayersAnswer)answer);
        }
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.STOP_ROUTINE_ANSWER)){
            onStopAnswer((StopRoutineAnswer)answer);
        }
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.USE_POWERUP_ANSWER)){
            onUsePowerupAnswer((UsePowerupAnswer)answer);
        }
        else{
            Logger.log("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
        }
    }

    private void onUsePowerupAnswer(UsePowerupAnswer answer) {
        if(answer.wishUseIt()) {
            TurnRoutineFactory factory= new TurnRoutineFactory(turn);
            TurnRoutine routine=factory.getTurnRoutine(TurnRoutineType.POWERUP);
            routine.start();
        }
        else
            handleCounterAttack();
    }

    private void onStopAnswer(StopRoutineAnswer answer) {
        if(!answer.isStop()){
            sendAvailableEffects();
        }
        else{
            closeShotRoutine();
        }
    }

    private void onSelectedPlayersReceived(SelectedPlayersAnswer answer) {
        if(!turn.getCurEffect().checkSelected(answer.getSelected(),turn)){
            Logger.log("[SHOOT ROUTINE]Invalid nicknames received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"[SHOOT ROUTINE]Invalid nicknames received")));
            return;
        }
        turn.getGame().notify(new UsingCardMessage(selWeapon));
        selEffect.perform(answer.getSelected(),turn);
    }

    private void onEffectReceived(EffectAnswer answer) {
        for(Effect effect:curAvailableEffects){
            if(effect.getName().equalsIgnoreCase(answer.getEffectName())){
                selEffect=effect;
                turn.setCurEffect(effect);
                payEffectCost();

                if(selEffect.getTarget().getType()== TargetType.ME){
                    turn.getGame().notify(new UsingCardMessage(selWeapon));

                    List<List<String>>selected=new ArrayList<>();
                    List<String> list=new ArrayList<>();
                    list.add(turn.getGame().getCurrentPlayer().getNickname());
                    selected.add(list);
                    effect.perform(selected,turn);
                    return;
                }

                List<List<Player>> selectable=selEffect.getShootablePlayers(turn);
                TurnRoutineMessage message= new SelectablePlayersMessage(turn.getGame().getCurrentPlayer().getNickname(), selectable, selEffect.getTarget());
                turn.getGame().notify(message);
            }
        }

    }

    private void payEffectCost() {
        if(!selEffect.getCost().isEmpty()){
            List<Card> discardedPowerups=new ArrayList<>();
            List<Color> usedAmmo=new ArrayList<>();
            for(Color color:selEffect.getCost()){
                if(turn.getGame().getCurrentPlayer().getBoard().getAmmo().contains(color)) {
                    turn.getGame().getCurrentPlayer().getBoard().removeAmmo(color);
                    usedAmmo.add(color);
                }
                else {
                    for(Powerup p: turn.getGame().getCurrentPlayer().getPowerups()) {
                        if (p.getColor() == color) {
                            discardedPowerups.add(new Card(p));
                            turn.getGame().getCurrentPlayer().popPowerup(p);
                            break;
                        }
                    }
                }
            }
            MatchMessage message=new PayEffectMessage(turn.getGame().getCurrentPlayer(),usedAmmo, discardedPowerups);
            turn.getGame().notify(message);
        }
    }

    private void onWeaponReceived(WeaponAnswer answer) {
        for(Weapon weapon:turn.getGame().getCurrentPlayer().getWeapons()){
            if(weapon.getId().equals(answer.getWeapon().getId())){
                List<Effect> usableEffects=weapon.getUsableEffects(checkLoaded,turn);
                if(!usableEffects.isEmpty()){
                    selWeapon=weapon;
                    if(weapon.isLoaded()){
                        curAvailableEffects=usableEffects;
                        if(!selWeapon.canStopCurrentRoutine())
                            sendAvailableEffects();
                        else
                            sendStopAdvise();
                        return;
                    }
                    else {
                        startReloadRoutine();
                        return;
                    }
                }
                else {
                    Logger.log("Invalid weapon received: there are no available effects");
                    turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Invalid weapon received: there are no available effects")));
                }
            }
        }
        Logger.log("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    private void sendAvailableEffects() {
        TurnRoutineMessage message=new AvailableEffectsMessage(turn.getGame().getCurrentPlayer().getNickname(), curAvailableEffects);
        turn.getGame().notify(message);
    }

    private void sendAvailableWeapons() {
        curAvailableEffects=null;
        List<Card> availableWeapons=new ArrayList<>();
        for(Weapon weapon:turn.getGame().getCurrentPlayer().getWeapons()){
            weapon.initNavigation();
            if(!weapon.getUsableEffects(checkLoaded,turn).isEmpty())
                availableWeapons.add(new Card(weapon));
        }
        TurnRoutineMessage message=new UsableWeaponsMessage(turn.getGame().getCurrentPlayer().getNickname(),availableWeapons);
        turn.getGame().notify(message);
    }

    private void startRunRoutine() {
        TurnRoutine routine=new RunningRoutine(turn, moveBeforeShot,true);
        routine.start();
    }

    private void startReloadRoutine(){
        TurnRoutine routine=new ReloadingRoutine(turn, selWeapon);
        routine.start();
    }
    @Override
    public TurnRoutineType getType() {
        return TurnRoutineType.SHOOT;
    }

    @Override
    public void updateTurnStatus() {
        turn.setStatus(TurnStatus.SHOOTING);
    }

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {
        switch(routineType){
            case RUN:
                updateTurnStatus();
                sendAvailableWeapons();
                break;
            case RELOAD:
                updateTurnStatus();
                curAvailableEffects=selWeapon.getUsableEffects(true,turn);
                if(!selWeapon.canStopCurrentRoutine())
                    sendAvailableEffects();
                else
                    sendStopAdvise();
                break;
            case POWERUP:
                updateTurnStatus();
                handleCounterAttack();
                break;
            default:
                logError(routineType);
                throw new IllegalStateException(this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]");
        }

    }

    private void sendStopAdvise() {
        TurnRoutineMessage message=new CanStopMessage(turn.getGame().getCurrentPlayer().getNickname());
        turn.getGame().notify(message);
    }

    private void closeShotRoutine() {
        turn.clearSelectedPlayers();
        turn.resetLatsDamagedPlayers();
        turn.endRoutine();
    }

    private void logError(TurnRoutineType routineType) {
        Logger.log(this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]")));
    }

    private void handleCounterAttack() {
        /*
        TODO!!!
         */
        checkNewEffects();
    }

    private void checkNewEffects() {
        curAvailableEffects=selWeapon.getUsableEffects(true,turn);
        if(!curAvailableEffects.isEmpty()) {
            if (!selWeapon.canStopCurrentRoutine())
                sendAvailableEffects();
            else
                sendStopAdvise();
        }else
            closeShotRoutine();
    }

    @Override
    public boolean isInnerRoutine() {
        return false;
    }

    @Override
    public void onEffectPerformed() {
        curAvailableEffects=null;
        selWeapon.setNavigationNode(selEffect); //update the current node in the weapon navigation tree

        TurnRoutineFactory factory= new TurnRoutineFactory(turn);
        if(factory.getTurnRoutine(TurnRoutineType.POWERUP)!=null) {
            turn.getGame().notify(new CanUsePowerupMessage(turn.getGame().getCurrentPlayer().getNickname()));
            return;
        }

        handleCounterAttack();
    }
}
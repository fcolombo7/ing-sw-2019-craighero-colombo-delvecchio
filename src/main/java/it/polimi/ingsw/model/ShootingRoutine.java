package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TargetType;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.*;

/**
 * This class represents the shootinf routine
 */
public class ShootingRoutine implements TurnRoutine {
    /**
     * this attribute contains the information used to check or not if the weapon is loaded or not
     */
    private boolean checkLoaded;

    /**
     * This attribute represents the current turn of the match
     */
    private Turn turn;

    /**
     * This attribute contains the position the player can reach before the shooting rountine
     */
    private MatrixHelper moveBeforeShot;

    /**
     * This atrtibute is used to save the available effects of the player
     */
    private List<Effect> curAvailableEffects;

    /**
     * This attribute is used to save the selected effect chosen by the current player during this routine
     */
    private Effect selEffect;

    /**
     * This attribute is used to save the selected weapon
     */
    private Weapon selWeapon;

    /**
     * This map is used to check the relation between the players who can counter attack the current player and their timer to send the answer
     */
    private Map<String, Timer> counterAttackMap;

    /**
     * This Constructor instantiates a ShootingRoutine
     * @param checkLoaded boolean representing the possibility of check or not if the weapon is loaded or not
     * @param moveBeforeShot a matrix representing where the player can run before shooting
     * @param turn representing the current turn
     */
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
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.COUNTER_ATTACK_ANSWER)){
            onCounterAttackAnswer((CounterAttackAnswer)answer);
        }
        else{
            Logger.logAndPrint("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
        }
    }

    /**
     * This method is used to handle the powerup answer during the shooting routine.
     * @param answer representing the answer given to the routine
     */
    private void onUsePowerupAnswer(UsePowerupAnswer answer) {
        if(answer.wishUseIt()) {
            TurnRoutineFactory factory= new TurnRoutineFactory(turn);
            TurnRoutine routine=factory.getTurnRoutine(TurnRoutineType.POWERUP);
            routine.start();
        }
        else
            handleCounterAttack();
    }

    /**
     * This method is used to handle the answer to the possibility of perform another effect of the current weapon in this turn
     * @param answer answer representing the answer given to the routine
     */
    private void onStopAnswer(StopRoutineAnswer answer) {
        if(!answer.wishStop()){
            sendAvailableEffects();
        }
        else{
            closeShotRoutine();
        }
    }

    /**
     * This method is used to handle the answer which contains the selected players, target of the chosen effect
     * @param answer representing the answer given to the routine
     */
    private void onSelectedPlayersReceived(SelectedPlayersAnswer answer) {
        if(!turn.getCurEffect().checkSelected(answer.getSelected(),turn)){
            Logger.logAndPrint("[SHOOT ROUTINE]Invalid nicknames received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"[SHOOT ROUTINE]Invalid nicknames received")));
            return;
        }
        turn.getGame().notify(new UsedCardMessage(selWeapon));
        selEffect.perform(answer.getSelected(),turn);
    }

    /**
     * This method is used to handle the answer which contains the chosen effect
     * @param answer representing the answer given to the routine
     */
    private void onEffectReceived(EffectAnswer answer) {
        for(Effect effect:curAvailableEffects){
            if(effect.getName().equalsIgnoreCase(answer.getEffectName())){
                selEffect=effect;
                turn.setCurEffect(effect);
                payEffectCost();

                if(selEffect.getTarget().getType()== TargetType.ME){
                    turn.getGame().notify(new UsedCardMessage(selWeapon));

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

    /**
     * This method is used to pay the effect cost of the selected weapon
     */
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
            MatchMessage message=new PayEffectMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),usedAmmo, discardedPowerups);
            turn.getGame().notify(message);
        }
    }

    /**
     * This method is used to handle the answer which contains the weapon the user want to use in this routine:
     * if the player really can choose this weapon, then the this method notify the usable effects
     * @param answer representing the answer given to the routine
     */
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
                    Logger.logAndPrint("Invalid weapon received: there are no available effects");
                    turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Invalid weapon received: there are no available effects")));
                }
            }
        }
        Logger.logAndPrint("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    /**
     * This method is used to send the available effects of the chosen weapon to the current player
     */
    private void sendAvailableEffects() {
        TurnRoutineMessage message=new AvailableEffectsMessage(turn.getGame().getCurrentPlayer().getNickname(), curAvailableEffects);
        turn.getGame().notify(message);
    }

    /**
     * This method is used to send the available weapons tho the player
     */
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

    /**
     * This method is used to start the run routine if the player can move before starting the shooting routine.
     */
    private void startRunRoutine() {
        TurnRoutine routine=new RunningRoutine(turn, moveBeforeShot,true);
        routine.start();
    }

    /**
     * This method is used to start the reload routine if the player can reload before starting the shooting routine
     */
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

    /**
     * This method is used to send to the player the advise that he can stop his shooting routine, or perform another available effect
     */
    private void sendStopAdvise() {
        TurnRoutineMessage message=new CanStopMessage(turn.getGame().getCurrentPlayer().getNickname());
        turn.getGame().notify(message);
    }

    /**
     * This method is called when the routine is completed and the turn need to be updated
     */
    private void closeShotRoutine() {
        selWeapon.unload();
        turn.clearSelectedPlayers();
        turn.resetLatsDamagedPlayers();
        turn.endRoutine();
    }

    /**
     * This method is used to send an error advise to the player if he send incorrect answers
     * @param routineType representing the routine in which there were error
     */
    private void logError(TurnRoutineType routineType) {
        Logger.logAndPrint(this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]")));
    }

    /**
     * This method is used to handle the possible counter attack received from an enemy.
     * If the enemy does not answer in a setted time, the answer will set no as default.
     */
    private void handleCounterAttack() {
        counterAttackMap=new HashMap<>();
        List<Player> canCounterAttackPlayers=getCounterAttackPlayers();
        for(Player player:canCounterAttackPlayers){
            Timer t=new Timer();
            counterAttackMap.put(player.getNickname(),t);
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Logger.logErr("COUNTER ATTACK TIME OUT ("+player.getNickname()+")");
                    turn.getGame().notify(new CounterAttackTimeOut(player.getNickname()));
                    onCounterAttackAnswer(new CounterAttackAnswer(player.getNickname(),false));
                }
            }, Server.getQuickMoveTimer()*1000);
            turn.getGame().notify(new CanCounterAttackMessage(player.getNickname()));
        }
        if(canCounterAttackPlayers.isEmpty())
            checkNewEffects();
    }

    /**
     * This method is used to handle the answer of a counter attack
     * @param answer representing the answer given to the routine
     */
    private synchronized void onCounterAttackAnswer(CounterAttackAnswer answer) {
        String nickname=answer.getSender();
        Timer t= counterAttackMap.get(nickname);
        if(t!=null){
            t.cancel();
            t.purge();
            counterAttackMap.remove(nickname);
        }
        if(answer.counterAttack()){
            for(Player p:turn.getGame().getPlayers()) {
                if(p.getNickname().equals(nickname)){
                    turn.getGame().getCurrentPlayer().getBoard().addMarks(p,1);
                    for(Powerup powerup:p.getPowerups()){
                        if(powerup.getTiming()==TurnStatus.COUNTER_ATTACK){
                            p.popPowerup(powerup);
                            turn.getGame().notify(new CounterAttackMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),new SimplePlayer(p),new Card(powerup)));
                            break;
                        }
                    }
                    break;
                }
            }
        }
        if(counterAttackMap.isEmpty()){
            checkNewEffects();
        }
    }

    /**
     * This method is used to get the players which can counterattack
     * @return the players which can counterattack
     */
    private List<Player> getCounterAttackPlayers() {
        List<Player> list=new ArrayList<>();
        for(Player player:turn.getLastDamagedPlayers()){
            if(player.hasTimingPowerup(TurnStatus.COUNTER_ATTACK))
                list.add(player);
        }
        return list;
    }

    /**
     * This method is used to check if can be performed new effects of the selected weapon
     */
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

    /**
     * This method is used to cancel the timer instantiates to handle the counter attack
     */
    public void cancelCounterAttackTimers(){
        for(String key:counterAttackMap.keySet()){
            Timer t=counterAttackMap.get(key);
            if(t!=null){
                t.cancel();
                t.purge();
            }
        }
        counterAttackMap.clear();
    }
}
package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.LoadableWeaponSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.InvalidAnswerMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.ReloadableWeaponsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.WeaponReloadedMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the ReloadingRoutine
 */
public class ReloadingRoutine implements TurnRoutine {
    /**
     * This attribute contains the current turn
     */
    private Turn turn;
    /**
     * This attribute is used to check if is a son routine
     */
    private boolean inner;
    /**
     * This attributes contains the selected weapon
     */
    private Weapon weapon;

    /**
     * This construtor instantiates a ReloadingRoutine
     * @param turn representing the current turn
     */
    ReloadingRoutine(Turn turn){
        this.turn=turn;
        inner=false;
        weapon=null;
    }

    /**
     * This constructor instantiates a reloading routine
     * @param turn representing the current turn
     * @param weapon representing the Weapon to reload
     */
    ReloadingRoutine(Turn turn, Weapon weapon){
        this.turn=turn;
        inner=true;
        this.weapon=weapon;
    }

    @Override
    public void start() {
        turn.pushRoutineInStack(this);
        updateTurnStatus();
        if(inner)
            reloadWeapon(weapon);
        else
            sendWeapons();
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.LOADABLE_WEAPON_SELECTED))
            onLoadableWeaponReceived((LoadableWeaponSelectedAnswer)answer);
        else {
            Logger.logAndPrint("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
        }
    }

    /**
     * This method is used to send the reloadable weapons to the user
     */
    private void sendWeapons(){
        List<Card> loadableWeapons=new ArrayList<>();
        for (Weapon w:turn.getGame().getCurrentPlayer().getWeapons()){
            if(turn.getGame().getCurrentPlayer().canReloadedWeapon(w))
                loadableWeapons.add(new Card(w));
        }
        turn.getGame().notify(new ReloadableWeaponsMessage(turn.getGame().getCurrentPlayer().getNickname(),loadableWeapons));
    }

    /**
     * This method is used to handle the answer which contains the weapon to realod
     * @param answer representing the answer given to the routine
     */
    private void onLoadableWeaponReceived(LoadableWeaponSelectedAnswer answer){
        Card weapon=answer.getWeapon();
        for(Weapon w:turn.getGame().getCurrentPlayer().getWeapons()){
            if(weapon.getId().equalsIgnoreCase(w.getId())){
                reloadWeapon(w);
                return;
            }
        }
        Logger.logAndPrint("Invalid weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Current player doesn't have the selected weapon")));
    }

    /**
     * This method reloads the selected weapon
     * @param weapon the weapon to reaload
     */
    private void reloadWeapon(Weapon weapon){
        if(turn.getGame().getCurrentPlayer().canReloadedWeapon(weapon)){
            List<Card> discardedPowerups= turn.getGame().getCurrentPlayer().reloadWeapon(weapon);
            turn.getGame().notify(new WeaponReloadedMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),weapon,discardedPowerups,weapon.getAmmo()));
            turn.endRoutine();
        }else{
            Logger.logAndPrint("Invalid weapon received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Cannot reload the selected weapon")));
        }
    }

    @Override
    public TurnRoutineType getType() {
        return TurnRoutineType.RELOAD;
    }

    @Override
    public void updateTurnStatus() {
        turn.setStatus(TurnStatus.RELOADING);
    }

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {
        Logger.logAndPrint("ReloadingRoutine can not start inner routine");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"ReloadingRoutine can not start inner routine")));
    }

    @Override
    public boolean isInnerRoutine() {
        return inner;
    }

    @Override
    public void onEffectPerformed() {
        Logger.logAndPrint("ReloadingRoutine can not perform an effect");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"ReloadingRoutine can not perform an effect")));
    }
}

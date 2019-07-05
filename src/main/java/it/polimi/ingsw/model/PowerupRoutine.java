package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TargetType;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.SelectedPlayersAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.SelectedPowerupAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.AvailablePowerupsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.SelectablePlayersMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.enums.Color.ANY;

/**
 * This class represents the PowerupRoutine
 */
public class PowerupRoutine implements TurnRoutine {
    /**
     * This attribute represents the current turn
     */
    private Turn turn;

    /**
     * This attribute represent the timing value in the turn
     */
    private TurnStatus timing;

    /**
     * This attribute is used to check if is an inner routine
     */
    private boolean inner;

    /**
     * This attributes is used to save the selected powerup
     */
    private Powerup selPowerup;

    /**
     * This attribute is used to save the selected effect
     */
    private Effect effect;

    /**
     * This Constructor instantiates a PowerupRoutine
     * @param turn representing the current turn
     * @param timing representing the status of the turn
     * @param inner a boolean which is true if this routine is an inner routine
     */
    PowerupRoutine(Turn turn, TurnStatus timing, boolean inner){
        this.turn=turn;
        this.timing=timing;
        this.inner=inner;
    }

    /**
     * This method is used to send the powerups to the player
     */
    private void sendPowerups(){
        List<Card> usablePowerups=new ArrayList<>();
        ArrayList<Color> colors=new ArrayList<>();
        List<Player> other=turn.getGame().getPlayers();
        other.remove(turn.getGame().getCurrentPlayer());
        for (Powerup p:turn.getGame().getCurrentPlayer().getPowerups()) {
            if(p.getTiming()==timing&&p.getEffect().canUse(turn)&&canPay(p)) {
                usablePowerups.add(new Card(p));
                colors.add(p.getColor());
            }
        }
        turn.getGame().notify(new AvailablePowerupsMessage(turn.getGame().getCurrentPlayer().getNickname(),usablePowerups,colors));
    }

    /**
     * This method is used to check if the player can pay all the necessary ammo in order to esecute the powerup effect
     * @param p representing the powerup card
     * @return true if can pay and so execute the effect
     */
    private boolean canPay(Powerup p) {
        List<Color> ammo= new ArrayList<>(turn.getGame().getCurrentPlayer().getBoard().getAmmo());
        int count=0;
        for (Color color:p.getEffect().getCost()){
            if(color==ANY)
                count++;
            else if(ammo.contains(color))
                ammo.remove(color);
            else
                return false;
        }
        return (count<=ammo.size());
    }


    @Override
    public void start() {
        turn.pushRoutineInStack(this);
        sendPowerups();
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.POWERUP_ANSWER)){
            onPowerupReceived((SelectedPowerupAnswer) answer);
        }
        else if(answer.getRoutineAnswer().equalsIgnoreCase(Constants.SELECTED_PLAYERS_ANSWER)){
            onSelectedPlayersReceived((SelectedPlayersAnswer)answer);
        }
        else {
            Logger.logAndPrint("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(), "RECEIVED A " + answer.getRoutineAnswer() + " MESSAGE")));
        }
    }

    /**
     * This method handle the answer of the client which contains the selected players on who perform the effect
     * @param answer representing the answer given to the PowerupRoutine
     */
    private void onSelectedPlayersReceived(SelectedPlayersAnswer answer) {
        if(!turn.getCurEffect().checkSelected(answer.getSelected(),turn)){
            Logger.logAndPrint("[POWERUP ROUTINE]Invalid nicknames received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"[POWERUP ROUTINE]Invalid nicknames received")));
            return;
        }
        effect=turn.getCurEffect();
        turn.getGame().notify(new UsedCardMessage(selPowerup));
        effect.perform(answer.getSelected(),turn);
    }

    /**
     * This method is used to handle the answer of the client which contains the selected players on who perform the effect
     * @param answer representing the answer given to the PowerupRoutine
     */
    private void onPowerupReceived(SelectedPowerupAnswer answer){
        Card cardPowerup=answer.getPowerup();
        List<Player> other=turn.getGame().getPlayers();
        other.remove(turn.getGame().getCurrentPlayer());
        for (Powerup powerup:turn.getGame().getCurrentPlayer().getPowerups()){
            if(cardPowerup.getId().equalsIgnoreCase(powerup.getId())){
                if(powerup.getTiming()==timing&&powerup.getEffect().canUse(turn)){
                    this.selPowerup =powerup;
                    turn.setCurEffect(powerup.getEffect());
                    effect=powerup.getEffect();
                    payEffectCost();
                    if(powerup.getEffect().getTarget().getType()== TargetType.ME){
                        turn.getGame().notify(new UsedCardMessage(selPowerup));
                        List<List<String>>selected=new ArrayList<>();
                        selected.add(new ArrayList<>());
                        selected.get(0).add(turn.getGame().getCurrentPlayer().getNickname());
                        effect.perform(selected,turn);
                        return;
                    }

                    List<List<Player>> selectable=powerup.getEffect().getShootablePlayers(turn);
                    TurnRoutineMessage message= new SelectablePlayersMessage(turn.getGame().getCurrentPlayer().getNickname(), selectable, powerup.getEffect().getTarget());
                    turn.getGame().notify(message);
                    return;
                }else {
                    Logger.logAndPrint("Invalid powerup received");
                    turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Cannot use the selected powerup")));
                }
            }
        }
        Logger.logAndPrint("Invalid powerup received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Current player doesn't have the selected powerup")));

    }

    /**
     * This method is used to pay the effect cost using the ammo and the powerups of the current player
     */
    private void payEffectCost() {
        if(!effect.getCost().isEmpty()){
            List<Card> discardedPowerups=new ArrayList<>();
            List<Color> usedAmmo=new ArrayList<>();
            int count=0;
            for(Color color:effect.getCost()){
                if(color==Color.ANY) count++;
                else{
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
            }
            while(count>0){
                Color color=turn.getGame().getCurrentPlayer().getBoard().getAmmo().get(0);
                usedAmmo.add(color);
                turn.getGame().getCurrentPlayer().getBoard().removeAmmo(color);
                count--;
            }
            MatchMessage message=new PayEffectMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),usedAmmo, discardedPowerups);
            turn.getGame().notify(message);
        }
    }

    @Override
    public TurnRoutineType getType() {
        return TurnRoutineType.POWERUP;
    }

    @Override
    public void updateTurnStatus() {
        turn.setStatus(TurnStatus.USING_POWERUP);
    }

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {
        Logger.logAndPrint("PowerupRoutine can not start inner routine");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"PowerupRoutine can not start inner routine")));
    }

    @Override
    public boolean isInnerRoutine() {
        return inner;
    }

    @Override
    public void onEffectPerformed() {
        if(effect==null){
            Logger.logAndPrint("NO EFFECT PERFORMED");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"NO EFFECT PERFORMED")));
        }else {
            turn.getGame().getCurrentPlayer().popPowerup(selPowerup);
            turn.getGame().notify(new DiscardedPowerupMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),selPowerup));
            turn.endRoutine();
        }
    }
}
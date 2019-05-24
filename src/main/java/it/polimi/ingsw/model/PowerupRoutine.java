package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TargetType;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.SelectedPlayersAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.SelectedPowerupAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.UsingCardMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.AvailablePowerupsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.InvalidAnswerMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.SelectablePlayersMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class PowerupRoutine implements TurnRoutine {
    private Turn turn;
    private TurnStatus timing;
    private boolean inner;
    private Powerup selPowerup;
    private Effect effect;

    PowerupRoutine(Turn turn, TurnStatus timing, boolean inner){
        this.turn=turn;
        this.timing=timing;
        this.inner=inner;
    }

    private void sendPowerups(){
        List<Card> usablePowerups=new ArrayList<>();
        List<Player> other=turn.getGame().getPlayers();
        other.remove(turn.getGame().getCurrentPlayer());
        for (Powerup p:turn.getGame().getCurrentPlayer().getPowerups()) {
            if(p.getTiming()==timing&&p.getEffect().canUse(turn))
                usablePowerups.add(new Card(p));
        }
        turn.getGame().notify(new AvailablePowerupsMessage(turn.getGame().getCurrentPlayer().getNickname(),usablePowerups));
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
            Logger.log("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(), "RECEIVED A " + answer.getRoutineAnswer() + " MESSAGE")));
        }
    }

    private void onSelectedPlayersReceived(SelectedPlayersAnswer answer) {
        if(!turn.getCurEffect().checkSelected(answer.getSelected(),turn)){
            Logger.log("[POWERUP ROUTINE]Invalid nicknames received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"[POWERUP ROUTINE]Invalid nicknames received")));
            return;
        }
        effect=turn.getCurEffect();
        turn.getGame().notify(new UsingCardMessage(selPowerup));
        effect.perform(answer.getSelected(),turn);
    }

    private void onPowerupReceived(SelectedPowerupAnswer answer){
        Card cardPowerup=answer.getPowerup();
        List<Player> other=turn.getGame().getPlayers();
        other.remove(turn.getGame().getCurrentPlayer());
        for (Powerup powerup:turn.getGame().getCurrentPlayer().getPowerups()){
            if(cardPowerup.getId().equalsIgnoreCase(powerup.getId())){
                if(powerup.getTiming()==timing&&powerup.getEffect().canUse(turn)){
                    this.selPowerup =powerup;
                    turn.setCurEffect(powerup.getEffect());

                    if(powerup.getEffect().getTarget().getType()== TargetType.ME){
                        effect=turn.getCurEffect();
                        turn.getGame().notify(new UsingCardMessage(selPowerup));
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
                    Logger.log("Invalid powerup received");
                    turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Cannot use the selected powerup")));
                }
            }
        }
        Logger.log("Invalid powerup received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Current player doesn't have the selected powerup")));

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
        Logger.log("PowerupRoutine can not start inner routine");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"PowerupRoutine can not start inner routine")));
    }

    @Override
    public boolean isInnerRoutine() {
        return inner;
    }

    @Override
    public void onEffectPerformed() {
        if(effect==null){
            Logger.log("NO EFFECT PERFORMED");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"NO EFFECT PERFORMED")));
        }else
            turn.endRoutine();
    }
}
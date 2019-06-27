package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.DiscardedWeaponAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.WeaponAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.DiscardWeaponMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.FullOfPowerupsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.GrabbableWeaponsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.GrabbedPowerupMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.ArrayList;
import java.util.List;

public class GrabbingRoutine implements TurnRoutine {
    private Turn turn;
    private MatrixHelper grabMatrix;
    private Weapon grabbedWeapon;
    private Powerup grabbedPowerup;
    private List<Weapon> inSquareWeapons;

    GrabbingRoutine(Turn turn, MatrixHelper grabMatrix){
        this.turn=turn;
        this.grabMatrix=new MatrixHelper(grabMatrix.toBooleanMatrix());
        grabbedWeapon=null;
    }

    @Override
    public void start() {
        turn.pushRoutineInStack(this);
        updateTurnStatus();
        startRunRoutine();
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equals(Constants.WEAPON_ANSWER)){
            onWeaponReceived((WeaponAnswer)answer);
        }
        else if(answer.getRoutineAnswer().equals(Constants.DISCARDED_WEAPON_ANSWER)){
            onDiscardedWeaponReceived((DiscardedWeaponAnswer)answer);
        }
        else{
            Logger.log("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
        }

    }

    private void onDiscardedWeaponReceived(DiscardedWeaponAnswer answer) {
        for(Weapon w:turn.getGame().getCurrentPlayer().getWeapons()){
            if(w.getId().equals(answer.getWeapon().getId())){
                turn.getGame().getCurrentPlayer().popWeapon(w);
                ((WeaponSquare)turn.getGame().getCurrentPlayer().getPosition()).removeWeapon(grabbedWeapon);
                turn.getGame().getCurrentPlayer().addWeapon(grabbedWeapon);
                payWeaponCost();
                if(!w.isLoaded()) w.load();
                ((WeaponSquare)turn.getGame().getCurrentPlayer().getPosition()).addWeapon(w);
                turn.getGame().notify(new GrabbedWeaponMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),grabbedWeapon));
                turn.getGame().notify(new BoardUpdateMessage(turn.getGame().getGameBoard()));
                turn.endRoutine();
                return;
            }
        }
        Logger.log("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    private void onWeaponReceived(WeaponAnswer answer) {
        for(Weapon weapon:inSquareWeapons){
            if(answer.getWeapon().getId().equals(weapon.getId())){
                grabbedWeapon=weapon;
                if(turn.getGame().getCurrentPlayer().getWeapons().size()<3){
                    ((WeaponSquare)turn.getGame().getCurrentPlayer().getPosition()).removeWeapon(grabbedWeapon);
                    turn.getGame().getCurrentPlayer().addWeapon(grabbedWeapon);
                    payWeaponCost();
                    turn.getGame().notify(new GrabbedWeaponMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),grabbedWeapon));
                    turn.getGame().notify(new BoardUpdateMessage(turn.getGame().getGameBoard()));
                    turn.endRoutine();
                    return;
                }else {
                    List<Card> playerWeapons=new ArrayList<>(3);
                    for(Weapon w: turn.getGame().getCurrentPlayer().getWeapons()){
                        playerWeapons.add(new Card(w));
                    }
                    turn.getGame().notify(new DiscardWeaponMessage(turn.getGame().getCurrentPlayer().getNickname(),playerWeapons));
                    return;
                }
            }
        }
        Logger.log("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    private void payWeaponCost() {
        List<Color> cost=new ArrayList<>(grabbedWeapon.getAmmo());
        cost.remove(0);
        for(Color color:cost){
            turn.getGame().getCurrentPlayer().getBoard().removeAmmo(color);
        }
    }

    private void grab() {
        Square position= turn.getGame().getCurrentPlayer().getPosition();
        if(turn.getGame().getGameBoard().isSpawnPoint(position.getBoardIndexes()[0],position.getBoardIndexes()[1])){
            //GRABBING WEAPON
            inSquareWeapons= ((WeaponSquare)position).getWeapons();
            List<Card> grabbable=new ArrayList<>();
            for (Weapon w:inSquareWeapons){
                if(canBeGrab(w))
                    grabbable.add(new Card(w));
            }
            turn.getGame().notify(new GrabbableWeaponsMessage(turn.getGame().getCurrentPlayer().getNickname(),grabbable));
        }
        else {
            //GRABBING AMMO TILE
            AmmoTile grabbedTile=((AmmoSquare)position).popAmmoTile();
            List<Color> ammo=new ArrayList<>();
            ammo.add(grabbedTile.getAmmo(0));
            ammo.add(grabbedTile.getAmmo(1));
            if(!grabbedTile.hasPowerup())
                ammo.add(grabbedTile.getAmmo(2));
            else{
                Powerup powerup=turn.getGame().drawPowerup();
                grabbedPowerup=powerup;
                if(turn.getGame().getCurrentPlayer().getPowerups().size()<3) {
                    turn.getGame().getCurrentPlayer().addPowerup(powerup);
                    turn.getGame().notify(new GrabbedPowerupMessage(turn.getGame().getCurrentPlayer().getNickname(), new SimplePlayer(turn.getGame().getCurrentPlayer()), powerup));
                }
                else{
                    turn.getGame().notify(new FullOfPowerupsMessage(turn.getGame().getCurrentPlayer().getNickname()));
                }
            }
            turn.getGame().getCurrentPlayer().getBoard().addAmmo(ammo);
            turn.getGame().notify(new GrabbedAmmoTileMessage(new SimplePlayer(turn.getGame().getCurrentPlayer()),grabbedTile));
            turn.getGame().notify(new BoardUpdateMessage(turn.getGame().getGameBoard()));
            turn.endRoutine();
        }
    }

    private boolean canBeGrab(Weapon w) {
        List<Color> cost=new ArrayList<>(w.getAmmo());
        cost.remove(0);

        List<Color> playerAmmo=new ArrayList<>(turn.getGame().getCurrentPlayer().getBoard().getAmmo());
        for(Color color:cost){
            boolean removed=false;
            for(Color single:playerAmmo){
                if(single==color) {
                    removed=true;
                    playerAmmo.remove(single);
                    break;
                }
            }
            if(!removed)
                return false;
        }
        return true;
    }

    @Override
    public TurnRoutineType getType() {
        return TurnRoutineType.GRAB;
    }

    @Override
    public void updateTurnStatus() {
        turn.setStatus(TurnStatus.GRABBING);
    }

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {
        if(routineType==TurnRoutineType.RUN) {
            updateTurnStatus();
            grab();
        }else
            logError(routineType);
    }

    private void logError(TurnRoutineType routineType) {
        Logger.log(this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]")));
    }

    private void startRunRoutine() {
        TurnRoutine routine=new RunningRoutine(turn, grabMatrix,true);
        routine.start();
    }

    @Override
    public boolean isInnerRoutine() {
        return false;
    }

    @Override
    public void onEffectPerformed() {
        Logger.log("GrabbingRoutine can not perform an effect");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"GrabbingRoutine can not perform an effect")));
    }
}

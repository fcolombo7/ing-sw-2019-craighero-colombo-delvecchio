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

/**
 * This class represents the grabbing routine
 */
public class GrabbingRoutine implements TurnRoutine {
    /**
     * This attribute represents the current turn
     */
    private Turn turn;

    /**
     * This attribute contains all the sqaure the player can reach and grab in
     */
    private MatrixHelper grabMatrix;

    /**
     * This atrtibutes contains the grabbed weapon
     */
    private Weapon grabbedWeapon;

    /**
     * This attributes contains all the weapons of the square
     */
    private List<Weapon> inSquareWeapons;

    /**
     * This constructor instantiates a GrabbingRoutine
     * @param turn represents the current turn
     * @param grabMatrix represent the where to grab matrix
     */
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
            Logger.logAndPrint("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
        }
    }

    /**
     * This method is used to handle the answer which contains the discarded weapon
     * @param answer representing the answer given to the routine
     */
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
        Logger.logAndPrint("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    /**
     * This method is used to handle the answer which contains the selected weapon
     * @param answer representing the answer given to the routine
     */
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
        Logger.logAndPrint("Not existing weapon received");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Not existing weapon received")));
    }

    /**
     * This method is used to pay the weapon cost
     */
    private void payWeaponCost() {
        List<Color> cost=new ArrayList<>(grabbedWeapon.getAmmo());
        cost.remove(0);
        for(Color color:cost){
            if(turn.getGame().getCurrentPlayer().getBoard().getAmmo().contains(color)) {
                turn.getGame().getCurrentPlayer().getBoard().removeAmmo(color);
            }
            else{
                for(Powerup p:turn.getGame().getCurrentPlayer().getPowerups()) {
                    if(p.getColor().equals(color)) {
                        turn.getGame().getCurrentPlayer().popPowerup(p);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method is used to start the grabbing routine
     */
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
                if(turn.getGame().getCurrentPlayer().getPowerups().size()<3) {
                    turn.getGame().getCurrentPlayer().addPowerup(powerup);
                    turn.getGame().notify(new GrabbedPowerupMessage(turn.getGame().getCurrentPlayer().getNickname(), new SimplePlayer(turn.getGame().getCurrentPlayer()), powerup,powerup.getColor()));
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

    /**
     * This method is used to check if the weapon can be grabbed
     * @param w representing the weapon to grab
     * @return true if the weapon can be grabbed
     */
    private boolean canBeGrab(Weapon w) {
        List<Color> cost=new ArrayList<>(w.getAmmo());
        cost.remove(0);

        List<Color> playerAmmo=new ArrayList<>(turn.getGame().getCurrentPlayer().getBoard().getAmmo());
        for(Powerup p:turn.getGame().getCurrentPlayer().getPowerups())
            playerAmmo.add(p.getColor());
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

    /**
     * This method is used to log error to the user
     * @param routineType representing the cur routine type
     */
    private void logError(TurnRoutineType routineType) {
        Logger.logAndPrint(this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),this.getClass().getSimpleName()+" can not handle this inner routine ["+routineType.name()+"]")));
    }

    /**
     * This method is used to start the routine
     */
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
        Logger.logAndPrint("GrabbingRoutine can not perform an effect");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"GrabbingRoutine can not perform an effect")));
    }
}

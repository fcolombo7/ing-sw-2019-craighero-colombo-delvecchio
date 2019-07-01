package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.DiscardedWeaponAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.RunAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.WeaponAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnActionsMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.DiscardWeaponMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.GrabbableWeaponsMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;

import java.util.ArrayDeque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GrabbingRoutineTest {

    @Test
    public void AmmoTileGrabbingRoutine(){
        Game game=new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();
        DebugView view=new DebugView(p1,collector);
        game.register(view);
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p3.setPosition(game.getGameBoard().getSquare(1,1));
        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction("GRAB");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.RUN_ROUTINE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new RunAnswer(p1.getNickname(),p1.getPosition().getBoardIndexes()));

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.GRABBED_TILE_MESSAGE));
        if(collector.size()==2){
            assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
            assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.GRABBED_POWERUP));
        }
        assertThat(collector.pop().getRequest(), is(Constants.RUN_COMPLETED));
    }

    @Test
    public void WeaponGrabbingRoutine(){
        Game game=new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();
        DebugView view=new DebugView(p1,collector);
        game.register(view);
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,0));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p3.setPosition(game.getGameBoard().getSquare(1,1));
        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Turn turn= new Turn(game);

        TurnActionsMessage msg=(TurnActionsMessage)collector.pop();
        assertThat(msg.getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        boolean ok=false;
        for(String action:msg.getActions()){
            if(action.equals("GRAB")) ok=true;
        }
        if(!ok) return;

        turn.selectAction("GRAB");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.RUN_ROUTINE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new RunAnswer(p1.getNickname(),p1.getPosition().getBoardIndexes()));

        GrabbableWeaponsMessage wMsg=(GrabbableWeaponsMessage)collector.pop();
        assertThat(wMsg.getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(wMsg.getRoutineRequest(), is(Constants.GRABBABLE_WEAPONS_MESSAGE));

        assertThat(collector.pop().getRequest(), is(Constants.RUN_COMPLETED));
        Card selected=wMsg.getWeapons().get(0);

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),selected));

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        String temp=p1.getBoard().getAmmo().size()+"";
        Logger.log(temp);
        Logger.log("Test finished");
    }

    @Test
    public void AlreadyThreeWeaponsDuringGrabRoutine(){
        Game game=new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();
        DebugView view=new DebugView(p1,collector);
        game.register(view);
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,0));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p3.setPosition(game.getGameBoard().getSquare(1,1));
        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        for(int i=0;i<3;i++){
            Weapon w=game.drawWeapon();
            w.unload();
            p1.addWeapon(w);
        }
        Turn turn= new Turn(game);

        TurnActionsMessage msg=(TurnActionsMessage)collector.pop();
        assertThat(msg.getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        boolean ok=false;
        for(String action:msg.getActions()){
            if(action.equals("GRAB")) ok=true;
        }
        if(!ok) return;

        turn.selectAction("GRAB");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.RUN_ROUTINE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new RunAnswer(p1.getNickname(),p1.getPosition().getBoardIndexes()));

        GrabbableWeaponsMessage wMsg=(GrabbableWeaponsMessage)collector.pop();
        assertThat(wMsg.getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(wMsg.getRoutineRequest(), is(Constants.GRABBABLE_WEAPONS_MESSAGE));

        assertThat(collector.pop().getRequest(), is(Constants.RUN_COMPLETED));
        Card selected=wMsg.getWeapons().get(0);

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),selected));

        DiscardWeaponMessage discardMsg=(DiscardWeaponMessage)collector.pop();
        assertThat(discardMsg.getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(discardMsg.getRoutineRequest(), is(Constants.DISCARD_WEAPON_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new DiscardedWeaponAnswer(p1.getNickname(),discardMsg.getWeapons().get(0)));

        MatchMessage lastMag=collector.pop();

        if(!lastMag.getRequest().equalsIgnoreCase(Constants.TURN_AVAILABLE_ACTIONS)&&!lastMag.getRequest().equalsIgnoreCase(Constants.TURN_END_MESSAGE))
            fail("Request was: "+lastMag.getRequest());

        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.GRABBED_WEAPON_MESSAGE));
        String temp=p1.getBoard().getAmmo().size()+"";
        Logger.log(temp);
        Logger.log("Test finished");
    }
}

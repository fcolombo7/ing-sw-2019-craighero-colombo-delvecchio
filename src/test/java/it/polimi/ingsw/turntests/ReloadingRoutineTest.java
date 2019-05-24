package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.LoadableWeaponSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.WeaponAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayDeque;

public class ReloadingRoutineTest {

    @Test
    public void CorrectReloadRoutine(){
        Game game=new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        Player p4=new Player("nickname4", "", false);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();
        DebugView view=new DebugView(p1,collector);

        game.register(view);

        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        p4.setStatus(PlayerStatus.WAITING);
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));

        Logger.log("FIRST PLAYER");
        Logger.log(p1.toString());
        Logger.log("AMMO: "+p1.getBoard().getAmmo().toString());

        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Weapon weapon=game.drawWeapon();
        weapon.unload();
        p1.addWeapon(weapon);
        while(!p1.canReloadedWeapon(weapon)){
            p1.popWeapon(weapon);
            weapon=game.drawWeapon();
            weapon.unload();
            p1.addWeapon(weapon);
        }

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"RELOAD"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.LOADABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new LoadableWeaponSelectedAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        assertThat(collector.pop().getRequest(), is(Constants.RELOAD_COMPLETED));
        Logger.log("Test Finished");
    }

    @Test
    public void InvalidMessagesReceived(){
        Game game=new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        Player p4=new Player("nickname4", "", false);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();
        DebugView view=new DebugView(p1,collector);

        game.register(view);

        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        p4.setStatus(PlayerStatus.WAITING);
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));

        Logger.log("FIRST PLAYER");
        Logger.log(p1.toString());
        Logger.log("AMMO: "+p1.getBoard().getAmmo().toString());

        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Weapon weapon=game.drawWeapon();
        weapon.unload();
        p1.addWeapon(weapon);
        while(!p1.canReloadedWeapon(weapon)){
            p1.popWeapon(weapon);
            weapon=game.drawWeapon();
            weapon.unload();
            p1.addWeapon(weapon);
        }

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"RELOAD"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.LOADABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));
        assertThat(collector.peek().getRequest(), is(Constants.INVALID_ANSWER));

        turn.getInExecutionRoutine().handleAnswer(new LoadableWeaponSelectedAnswer(p1.getNickname(),new Card(game.drawWeapon())));
        assertThat(collector.peek().getRequest(), is(Constants.INVALID_ANSWER));

        Logger.log("Test Finished");
    }
}

package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.MoveAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.SelectedPowerupAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayDeque;

public class PowerupRoutineTest {

    @Test
    public void CorrectPowerupRoutine(){
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

        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p3.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));

        Logger.log("FIRST PLAYER");
        Logger.log(p1.toString());
        Logger.log("AMMO: "+p1.getBoard().getAmmo().toString());

        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));

        Powerup powerup= new Powerup(new Card("id","nome","src/main/Resources/powerups/teletrasportor.xml"));
        p1.addPowerup(powerup);

        Logger.log("DROWN POWERUP");
        Logger.log(powerup.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction("POWERUP");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.AVAILABLE_POWERUPS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new SelectedPowerupAnswer(p1.getNickname(), powerup));

        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_REQUEST_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        turn.getCurEffect().handleMoveResponse(turn,p2.getNickname(), new int[]{1,2});
        assertThat(collector.pop().getRequest(), is(Constants.INVALID_ANSWER));

        turn.getCurEffect().handleMoveResponse(turn,p1.getNickname(), new int[]{1,2});
        assertThat(collector.pop().getRequest(), is(Constants.TURN_END_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.DISCARDED_POWERUP_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_MESSAGE));

        Logger.log("Test Finished");
    }

}

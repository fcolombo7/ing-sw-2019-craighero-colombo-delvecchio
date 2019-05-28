package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.AvailableEffectsMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ShootingRoutineTest {

    @Test
    public void CorrectShootingRoutine(){
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
        Weapon weapon=new Weapon(new Card("id","nome","src/main/Resources/weapons/distruttore.xml"));
        p1.addWeapon(weapon);
        ArrayList<Powerup> pList=new ArrayList<>();
        for(Powerup power: p1.getPowerups()){
            pList.add(power);
        }
        for(Powerup p:pList)
            p1.popPowerup(p);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"SHOOT"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECT_MESSAGE));

        Logger.log("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertEquals(2,p2.getBoard().getHealthBar().size());
        assertEquals(1,p2.getBoard().getMarks().size());

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.CAN_STOP_ROUTINE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(p1.getNickname(),true));

        Logger.log("Test Finished");
    }

    @Test
    public void EffectNavigationDuringShootingRoutine(){
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
        Weapon weapon=new Weapon(new Card("id","nome","src/main/Resources/weapons/distruttore.xml"));
        p1.addWeapon(weapon);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"SHOOT"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECT_MESSAGE));

        Logger.log("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertEquals(2,p2.getBoard().getHealthBar().size());
        assertEquals(1,p2.getBoard().getMarks().size());

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.CAN_STOP_ROUTINE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        turn.getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(p1.getNickname(), false));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg2= (AvailableEffectsMessage)collector.pop();
        assertThat(msg2.getRoutineRequest(), is(Constants.AVAILABLE_EFFECT_MESSAGE));

        Logger.log("RECEIVED EFFECTS\n"+msg2.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"SECONDO AGGANCIO"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> temp=new ArrayList<>();
        temp.add(p3.getNickname());
        List<List<String>> selected2=new ArrayList<>();
        selected2.add(temp);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected2));

        assertEquals(1,p3.getBoard().getMarks().size());

        assertThat(collector.pop().getRequest(), is(Constants.TURN_END_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.PAY_EFFECT_MESSAGE));

        Logger.log("Test Finished");
    }

    @Test
    public void InnerPowerupRoutine(){
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
        Weapon weapon=new Weapon(new Card("id","nome","src/main/Resources/weapons/distruttore.xml"));
        p1.addWeapon(weapon);
        ArrayList<Powerup> pList=new ArrayList<>();
        for(Powerup power: p1.getPowerups()){
            pList.add(power);
        }
        for(Powerup p:pList)
            p1.popPowerup(p);
        Powerup powerup=new Powerup(new Card("idp","name","src/main/Resources/powerups/mirinor.xml"));
        p1.addPowerup(powerup);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"SHOOT"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECT_MESSAGE));

        Logger.log("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertEquals(2,p2.getBoard().getHealthBar().size());
        assertEquals(1,p2.getBoard().getMarks().size());

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.CAN_USE_POWERUP));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new UsePowerupAnswer(p1.getNickname(),true));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.AVAILABLE_POWERUPS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new SelectedPowerupAnswer(p1.getNickname(), powerup));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> temp=new ArrayList<>();
        temp.add(p2.getNickname());
        List<List<String>> selected2=new ArrayList<>();
        selected2.add(temp);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected2));

        assertEquals(4,p2.getBoard().getHealthBar().size());

        assertThat(collector.pop().getRequest(), is(Constants.DISCARDED_POWERUP_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.PAY_EFFECT_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(p1.getNickname(),true));

        Logger.log("Test Finished");
    }

    @Test
    public void InnerReloadRoutine(){
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
        Weapon weapon=new Weapon(new Card("id","nome","src/main/Resources/weapons/distruttore.xml"));
        p1.addWeapon(weapon);
        weapon.unload();

        ArrayList<Powerup> pList=new ArrayList<>();
        for(Powerup power: p1.getPowerups()){
            pList.add(power);
        }
        for(Powerup p:pList)
            p1.popPowerup(p);

        ArrayList<Color> colors=new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        p1.getBoard().addAmmo(colors);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        game.setFrenzy(p3);
        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"SHOOT"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.RUN_ROUTINE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new RunAnswer(p1.getNickname(),p1.getPosition().getBoardIndexes()));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.RUN_COMPLETED));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECT_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.RELOAD_COMPLETED));

        Logger.log("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertEquals(2,p2.getBoard().getHealthBar().size());
        assertEquals(1,p2.getBoard().getMarks().size());

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.CAN_STOP_ROUTINE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(p1.getNickname(),true));

        assertEquals(p1.getBoard().getAmmo().size(), 3);

        Logger.log("Test Finished");
    }
}

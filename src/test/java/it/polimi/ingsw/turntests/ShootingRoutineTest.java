package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerStatus;
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
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/distruttore.xml"));
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

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

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
    public void raggioTraenteTest(){
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
        p1.setPosition(game.getGameBoard().getSquare(1,0));
        p2.setPosition(game.getGameBoard().getSquare(2,2));
        p3.setPosition(game.getGameBoard().getSquare(0,2));
        p4.setPosition(game.getGameBoard().getSquare(0,2));

        Logger.log("FIRST PLAYER");
        Logger.log(p1.toString());
        Logger.log("AMMO: "+p1.getBoard().getAmmo().toString());

        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/raggiotraente.xml"));
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

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

        Logger.log("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));

        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_REQUEST_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        turn.getCurEffect().handleMoveAnswer(turn,p2.getNickname(),new int[]{1,1});
        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_MESSAGE));
        assertFalse(weapon.isLoaded());
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
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/distruttore.xml"));
        p1.addWeapon(weapon);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

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
        assertThat(msg2.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

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

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
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
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/distruttore.xml"));
        p1.addWeapon(weapon);
        ArrayList<Powerup> pList=new ArrayList<>();
        for(Powerup power: p1.getPowerups()){
            pList.add(power);
        }
        for(Powerup p:pList)
            p1.popPowerup(p);
        Powerup powerup=new Powerup(new Card("idp","name","/powerups/mirinor.xml"));
        p1.addPowerup(powerup);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());

        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

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

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        assertThat(collector.pop().getRequest(), is(Constants.DISCARDED_POWERUP_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.PAY_EFFECT_MESSAGE));
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
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/distruttore.xml"));
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

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.RUN_ROUTINE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new RunAnswer(p1.getNickname(),p1.getPosition().getBoardIndexes()));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.RUN_COMPLETED));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));
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

    @Test
    public void CounterAttackTest(){
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
        ArrayDeque<MatchMessage> collector2=new ArrayDeque<>();

        DebugView view=new DebugView(p1,collector);
        DebugView view2=new DebugView(p2,collector2);

        game.register(view);
        game.register(view2);

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
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/distruttore.xml"));
        Powerup powerup=new Powerup(new Card("id","granata venom","/powerups/granatavenomb.xml"));
        p1.addWeapon(weapon);
        ArrayList<Powerup> pList=new ArrayList<>();
        for(Powerup power: p1.getPowerups()){
            pList.add(power);
        }
        for(Powerup p:pList)
            p1.popPowerup(p);

        Logger.log("DROWN WEAPON");
        Logger.log(weapon.toString());
        p2.addPowerup(powerup);



        Turn turn= new Turn(game);

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

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
        assertThat(collector2.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector2.pop()).getRoutineRequest(), is(Constants.CAN_COUNTER_ATTACK));
        collector2.clear();
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MARK_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        turn.getInExecutionRoutine().handleAnswer(new CounterAttackAnswer(p2.getNickname(),true));
        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.CAN_STOP_ROUTINE));
        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.COUNTER_ATTACK_COMPLETED));

        turn.getInExecutionRoutine().handleAnswer(new StopRoutineAnswer(p1.getNickname(),true));
        assertThat(p2.getPowerups().size(),is(0));

        Logger.log("Test Finished");
    }

    @Test
    public void spadaFotonicaTest(){
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
        p1.setPosition(game.getGameBoard().getSquare(2,2));
        p2.setPosition(game.getGameBoard().getSquare(2,3));
        p3.setPosition(game.getGameBoard().getSquare(0,0));
        p4.setPosition(game.getGameBoard().getSquare(0,0));

        Logger.log("FIRST PLAYER");
        Logger.log(p1.toString());
        Logger.log("AMMO: "+p1.getBoard().getAmmo().toString());

        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));
        Weapon weapon=new Weapon(new Card("id","nome","/weapons/spadafotonica.xml"));
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

        turn.selectAction("SHOOT");

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
        assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

        Logger.logAndPrint("RECEIVED EFFECTS\n"+msg.getEffects().toString());

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"PASSO OMBRA"));

        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_REQUEST_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));
        turn.getCurEffect().handleMoveAnswer(turn,turn.getGame().getCurrentPlayer().getNickname(),new int[]{2,3});


        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_MOVE_MESSAGE));

        turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

        assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
        assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));
        List<String> list=new ArrayList<>();
        list.add(p2.getNickname());
        List<List<String>> selected=new ArrayList<>();
        selected.add(list);

        turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

        assertEquals(2,p2.getBoard().getHealthBar().size());
        assertEquals(0,p2.getBoard().getMarks().size());

        assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));
        assertThat(collector.pop().getRequest(), is(Constants.EFFECT_DAMAGE_MESSAGE));
        assertThat(collector.pop().getRequest(), is(Constants.USED_CARD_MESSAGE));

        Logger.log("Test Finished");
    }
    @Test
    public void TestCannoneVortex(){
        try
        {
            Weapon weapon=new Weapon("weapon1","cannone vortex","/weapons/cannonevortex.xml");
            weapon.init();
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
            GameBoard board=game.getGameBoard();
            List<Color> ammo=new ArrayList<>();
            ammo.add(Color.RED);
            ammo.add(Color.RED);
            ammo.add(Color.BLUE);
            ammo.add(Color.BLUE);
            p1.getBoard().addAmmo(ammo);

            p1.addWeapon(weapon);
            p1.setPosition(board.getSquare(1,2));
            p2.setPosition(board.getSquare(2,3));
            p3.setPosition(board.getSquare(1,2));
            p4.setPosition(board.getSquare(0,0));
            p1.setStatus(PlayerStatus.PLAYING);
            p2.setStatus(PlayerStatus.WAITING);
            p3.setStatus(PlayerStatus.WAITING);
            p4.setStatus(PlayerStatus.WAITING);
            assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));

            Turn turn= new Turn(game);

            assertThat(collector.pop().getRequest(), is(Constants.TURN_AVAILABLE_ACTIONS));

            turn.selectAction("SHOOT");

            assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
            assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.USABLE_WEAPONS_MESSAGE));

            turn.getInExecutionRoutine().handleAnswer(new WeaponAnswer(p1.getNickname(),new Card(weapon)));

            assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
            AvailableEffectsMessage msg= (AvailableEffectsMessage)collector.pop();
            assertThat(msg.getRoutineRequest(), is(Constants.AVAILABLE_EFFECTS_MESSAGE));

            Logger.logAndPrint("RECEIVED EFFECTS\n"+msg.getEffects().toString());

            turn.getInExecutionRoutine().handleAnswer(new EffectAnswer(p1.getNickname(),"BASE"));

            assertThat(collector.peek().getRequest(), is(Constants.TURN_ROUTINE_MESSAGE));
            assertThat(((TurnRoutineMessage)collector.pop()).getRoutineRequest(), is(Constants.SELECTABLE_PLAYERS_MESSAGE));
            List<String> list=new ArrayList<>();
            list.add(p2.getNickname());
            List<List<String>> selected=new ArrayList<>();
            selected.add(list);

            turn.getInExecutionRoutine().handleAnswer(new SelectedPlayersAnswer(p1.getNickname(),selected));

            Logger.log("Test Finished");
        } catch (Exception e) {
            fail("Unhandled Exception has been thrown.");
        }
    }
}

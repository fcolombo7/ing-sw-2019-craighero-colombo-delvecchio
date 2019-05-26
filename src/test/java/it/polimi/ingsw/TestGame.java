package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.RespawnAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.turntests.DebugView;
import it.polimi.ingsw.utils.Constants;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestGame {

    @Test
    public void gameConstructorTest() {
        Game game = new Game();

        assertEquals(21, game.getWeaponDeck().size());
        assertEquals(36, game.getPowerupDeck().size());
        assertEquals(36, game.getAmmoTileDeck().size());
    }

    @Test
    public void playerMaxNumberTest(){
        Game game = new Game();
        game.addPlayer(new Player("a", "aaa", false));
        game.addPlayer(new Player("b", "bbb", false));
        game.addPlayer(new Player("c", "ccc", false));
        game.addPlayer(new Player("d", "ddd", false));
        game.addPlayer(new Player("e", "eee", false));
        try{
            game.addPlayer(new Player("f", "fff", false));
            fail("Expected an IndexOutOfBoundException to be thrown");
        } catch (IndexOutOfBoundsException anIndexOutOfBoundException) {
            assertEquals(5, game.getPlayers().size());
        }
    }

    @Test
    public void drawWeaponTest(){
        Game game = new Game();
        List<Weapon> drawed= new ArrayList<>();
        for(int i=0; i<game.getWeaponDeck().size(); i++)
            drawed.add(game.drawWeapon());
        for(int i=0; i<game.getWeaponDeck().size(); i++){
            assertEquals(drawed.get(i).getId(), game.getWeaponDeck().get(i).getId());
            assertEquals(drawed.get(i).getName(), game.getWeaponDeck().get(i).getName());
            assertEquals(drawed.get(i).getInitXML(), game.getWeaponDeck().get(i).getInitXML());
        }
    }

    @Test
    public void drawAmmoTileTest(){
        Game game = new Game();
        List<AmmoTile> drawed= new ArrayList<>();
        for(int j=0; j<2; j++) {
            for (int i = 0; i < game.getAmmoTileDeck().size(); i++)
                drawed.add(game.drawAmmoTile());
            for (int i = 0; i < game.getAmmoTileDeck().size(); i++) {
                assertEquals(drawed.get(i).getId(), game.getAmmoTileDeck().get(i).getId());
                assertEquals(drawed.get(i).hasPowerup(), game.getAmmoTileDeck().get(i).hasPowerup());
                assertEquals(drawed.get(i).getAmmo(0), game.getAmmoTileDeck().get(i).getAmmo(0));
                assertEquals(drawed.get(i).getAmmo(1), game.getAmmoTileDeck().get(i).getAmmo(1));
                if(!drawed.get(i).hasPowerup())
                    assertEquals(drawed.get(i).getAmmo(2), game.getAmmoTileDeck().get(i).getAmmo(2));
            }
            drawed.clear();
        }
    }

    @Test
    public void drawPowerupTest(){
        Game game = new Game();
        List<Powerup> drawed= new ArrayList<>();
        for(int j=0; j<2; j++) {
            for (int i = 0; i < game.getPowerupDeck().size(); i++)
                drawed.add(game.drawPowerup());
            for (int i = 0; i < game.getWeaponDeck().size(); i++) {
                assertEquals(drawed.get(i).getId(), game.getPowerupDeck().get(i).getId());
                assertEquals(drawed.get(i).getName(), game.getPowerupDeck().get(i).getName());
                assertEquals(drawed.get(i).getInitXML(), game.getPowerupDeck().get(i).getInitXML());
            }
            drawed.clear();
        }
    }


    @Test
    public void winnerCalculatorTest(){
        Game game = new Game();
        game.addPlayer(new Player("a", "aaa", false));
        game.addPlayer(new Player("b", "bbb", false));
        game.addPlayer(new Player("c", "ccc", false));
        game.addPlayer(new Player("d", "ddd", false));
        game.addPlayer(new Player("e", "eee", false));
        game.getPlayers().get(0).updateScore(11);
        game.getPlayers().get(1).updateScore(0);
        game.getPlayers().get(2).updateScore(2);
        game.getPlayers().get(3).updateScore(34);
        game.getPlayers().get(4).updateScore(12);
        Map<Player, Integer> example = game.calcWinner();
        List<Player> res= new ArrayList<>(example.keySet());

        /*assertThat(res.toString(), is("[Player\n" +
                "Nickname: d\n" +
                "Motto: ddd\n" +
                "Score: 34\n" +
                "Room: Nowhere\n" +
                ", Player\n" +
                "Nickname: e\n" +
                "Motto: eee\n" +
                "Score: 12\n" +
                "Room: Nowhere\n" +
                ", Player\n" +
                "Nickname: a\n" +
                "Motto: aaa\n" +
                "Score: 11\n" +
                "Room: Nowhere\n" +
                ", Player\n" +
                "Nickname: c\n" +
                "Motto: ccc\n" +
                "Score: 2\n" +
                "Room: Nowhere\n" +
                ", Player\n" +
                "Nickname: b\n" +
                "Motto: bbb\n" +
                "Score: 0\n" +
                "Room: Nowhere\n]"));

         */
    }

    @Test
    public void FillGameboardTest(){
        try {
            for(int i=1;i<5;i++){
                Game game = new Game();
                game.addPlayer(new Player("ciao", "ciao", true));
                game.setGameBoard(i);
            }
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void SkullNumberInitializationTest(){
        Game game=new Game();
        assertTrue(game.getSkullNumber()!=0);
    }

    @Test
    public void TurnCreationTest(){
        Game game=new Game();
        Player p1=new Player("p1","",true);
        Player p2=new Player("p2","",false);
        game.addPlayer(p1);
        game.addPlayer(p2);

        try{
            game.createTurn();
        }catch(IllegalStateException e){
            assertThat(e.getMessage(),is("Cannot create turn without having instantiated the Gameboard."));
        }
        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        try{
            game.createTurn();
        }catch(IllegalStateException e){
            assertThat(e.getMessage(),is("Current player need to be Spawned."));
        }
        p1.setStatus(PlayerStatus.DEAD);
        try{
            game.createTurn();
        }catch(IllegalStateException e){
            assertThat(e.getMessage(),is("Cannot create turn.[Illegal player status: " + p1.getStatus().name() + "]"));
        }
        p1.setStatus(PlayerStatus.WAITING);
        game.createTurn();
        p1.setStatus(PlayerStatus.WAITING);
        try{
            game.createTurn();
        }catch(IllegalStateException e){
            assertThat(e.getMessage(),is("Cannot create turn. [Another one is still being played]"));
        }
    }

    @Test
    public void RespawnPlayerTest(){
        Game game= new Game();
        Player p1= new Player("p1","",true);
        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();

        DebugView view= new DebugView(p1,collector);
        game.addPlayer(p1);
        game.register(view);
        game.setGameBoard(1);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));

        game.respawnPlayerRequest(p1,true);
        assertThat(collector.pop().getRequest(), is(Constants.RESPAWN_REQUEST_MESSAGE));

        game.respawnPlayer(p1,new RespawnAnswer(p1.getNickname(),p1.getPowerups().get(0)));

        assertThat(collector.pop().getRequest(), is(Constants.RESPAWN_COMPLETED_MESSAGE));

        assertTrue(p1.getPowerups().size()<2);
        assertNotNull(p1.getPosition());
    }

    @Test
    public void RespawnPlayerErrorTest(){
        Game game= new Game();
        Player p1= new Player("p1","",true);
        ArrayDeque<MatchMessage> collector=new ArrayDeque<>();

        DebugView view= new DebugView(p1,collector);
        game.addPlayer(p1);
        game.register(view);
        game.setGameBoard(1);
        assertThat(collector.pop().getRequest(), is(Constants.BOARD_UPDATE_MESSAGE));

        game.respawnPlayerRequest(p1,true);
        assertThat(collector.pop().getRequest(), is(Constants.RESPAWN_REQUEST_MESSAGE));
        p1.setStatus(PlayerStatus.WAITING);

        game.respawnPlayer(p1,new RespawnAnswer(p1.getNickname(),p1.getPowerups().get(0)));

        assertThat(collector.pop().getRequest(), is(Constants.INVALID_ANSWER));
        p1.setStatus(PlayerStatus.FIRST_SPAWN);

        try {
            game.respawnPlayer(p1, new RespawnAnswer(p1.getNickname(), game.drawPowerup()));
            assertThat(collector.pop().getRequest(), is(Constants.INVALID_ANSWER));
        }catch(Exception e){
            assertThat(e.getMessage(),is("MISSING POWERUP"));
        }

        game.respawnPlayer(p1,new RespawnAnswer(p1.getNickname(),p1.getPowerups().get(0)));
        assertThat(collector.pop().getRequest(), is(Constants.RESPAWN_COMPLETED_MESSAGE));

        assertTrue(p1.getPowerups().size()<2);
        assertNotNull(p1.getPosition());
    }

    @Test
    public void CashDamagesTest(){
        Game game= new Game();
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        Player p4=new Player("nickname4", "", false);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        game.setGameBoard(1);
        p1.setPosition(game.getGameBoard().getSquare(1,1));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p3.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));

        p1.setStatus(PlayerStatus.WAITING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        p4.setStatus(PlayerStatus.WAITING);

        game.createTurn();
        Turn turn=game.getCurrentTurn();
        p2.getBoard().addDamage(p1,6);
        p2.getBoard().addDamage(p3,5);
        p2.setStatus(PlayerStatus.ALMOST_DEAD);
        turn.selectAction(new ActionSelectedAnswer(p1.getNickname(),"END"));
        game.endTurn();

        assertEquals(9,p1.getScore());
        assertEquals(6,p3.getScore());
        assertEquals(1,p2.getBoard().getDeathCounter());
    }




}

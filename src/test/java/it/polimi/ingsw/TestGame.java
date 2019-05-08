package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Test;

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
                assertEquals(drawed.get(i).getAmmo(2), game.getAmmoTileDeck().get(i).getAmmo(2));
            }
            drawed.clear();
        }
    }

    /*@Test
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
    }*/


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
}

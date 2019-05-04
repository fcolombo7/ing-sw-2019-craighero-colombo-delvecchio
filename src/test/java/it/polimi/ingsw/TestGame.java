package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AmmoTileNotValidException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Weapon;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
        game.addPlayers(new Player("a", "aaa", false));
        game.addPlayers(new Player("b", "bbb", false));
        game.addPlayers(new Player("c", "ccc", false));
        game.addPlayers(new Player("d", "ddd", false));
        game.addPlayers(new Player("e", "eee", false));
        try{
            game.addPlayers(new Player("f", "fff", false));
            fail("Expected an IndexOutOfBoundException to be thrown");
        } catch (IndexOutOfBoundsException anIndexOutOfBoundException) {
            assertEquals(5, game.getPlayers().size());
        }
    }

    /*@Test
    public void drawWeaponTest(){
        Game game = new Game();
        if(game.getWeaponDeck().contains(game.drawWeapon())){
            for(int i=0; i<30; i++)
                Weapon w = (Weapon)game.drawWeapon();
        }
    }*/

    @Test
    public void winnerCalculatorTest(){
        Game game = new Game();
        game.addPlayers(new Player("a", "aaa", false));
        game.addPlayers(new Player("b", "bbb", false));
        game.addPlayers(new Player("c", "ccc", false));
        game.addPlayers(new Player("d", "ddd", false));
        game.addPlayers(new Player("e", "eee", false));
        game.getPlayers().get(0).updateScore(12);
        game.getPlayers().get(1).updateScore(0);
        game.getPlayers().get(2).updateScore(2);
        game.getPlayers().get(3).updateScore(34);
        game.getPlayers().get(4).updateScore(12);
        Map<Player, Integer> example = game.calcWinner();
        List<Player> res= new ArrayList<>(example.keySet());

        assertThat(res.toString(), is("[Player\n" +
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
                "Score: 12\n" +
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
    }
}

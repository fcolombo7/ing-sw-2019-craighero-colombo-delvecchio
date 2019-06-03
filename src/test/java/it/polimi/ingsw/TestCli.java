package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.ui.Cli;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.TestGameboard.parsingXMLFile;
import static it.polimi.ingsw.utils.Constants.*;

public class TestCli {

    @Test
    public void testSetPlayerPosition() throws IOException, SAXException, ParserConfigurationException {
        Node node = parsingXMLFile("src/main/Resources/boards/board" + "1" + ".xml");
        GameBoard gameb = new GameBoard(node, 5, 1);
        AmmoTile ammo1 = new AmmoTile(Color.RED, Color.RED, Color.YELLOW, false);
        AmmoTile ammo2 = new AmmoTile(Color.BLUE, Color.RED, null, true);
        for(int i=0;i<gameb.getMap().length;i++){
            for(int j=0;j<gameb.getMap()[0].length;j++){
                if(gameb.hasSquare(i,j) && !gameb.isSpawnPoint(i,j)){
                    AmmoSquare ammoSquare=(AmmoSquare)gameb.getSquare(i,j);
                    if(j%2==0)
                        ammoSquare.setAmmoTile(ammo1);
                    else ammoSquare.setAmmoTile(ammo2);
                }
            }
        }
        SimpleBoard board = new SimpleBoard(gameb);
        Cli cli = new Cli(board);
        SimplePlayer bbb = new SimplePlayer(new Player("b", "bbb", false));
        SimplePlayer ccc = new SimplePlayer(new Player("c", "ccc", false));
        SimplePlayer ddd = new SimplePlayer(new Player("d", "ddd", false));
        SimplePlayer eee = new SimplePlayer(new Player("e", "eee", false));
        Map<String, String> enemies = new HashMap<>();
        enemies.put(bbb.getNickname(), RED_W);
        enemies.put(ccc.getNickname(), WHITE_W);
        enemies.put(ddd.getNickname(), GREEN_W);
        enemies.put(eee.getNickname(), BLUE_W);
        cli.setEnemiesColor(enemies);
        Map<String, int[]> offsets = new HashMap<>();
        offsets.put(RED_W, new int[]{3, 10});
        offsets.put(WHITE_W, new int[]{3, 18});
        offsets.put(GREEN_W, new int[]{7, 10});
        offsets.put(BLUE_W, new int[]{7, 18});
        cli.setSquareOffset(offsets);
        cli.setPlayerPosition(bbb, new int[]{0, 0});
        cli.setPlayerPosition(ccc, new int[]{2, 1});
        cli.setPlayerPosition(ddd, new int[]{2, 1});
        cli.setPlayerPosition(eee, new int[]{2, 1});
        cli.printMap();
    }

    @Test
    public void testBuildMap() throws IOException, SAXException, ParserConfigurationException {
        //for(int i=1; i<5; i++) {
            Node node = parsingXMLFile("src/main/Resources/boards/board" + "1" + ".xml");
            GameBoard gameb = new GameBoard(node, 5, 1);
            AmmoTile ammo1 = new AmmoTile(Color.RED, Color.RED, Color.YELLOW, false);
            AmmoTile ammo2 = new AmmoTile(Color.BLUE, Color.RED, null, true);
            for(int i=0;i<gameb.getMap().length;i++){
                for(int j=0;j<gameb.getMap()[0].length;j++){
                    if(gameb.hasSquare(i,j) && !gameb.isSpawnPoint(i,j)){
                        AmmoSquare ammoSquare=(AmmoSquare)gameb.getSquare(i,j);
                        if(j%2==0)
                            ammoSquare.setAmmoTile(ammo1);
                        else ammoSquare.setAmmoTile(ammo2);
                    }
                }
            }
            SimpleBoard board = new SimpleBoard(gameb);
            Cli cli = new Cli(board);
            cli.printMap();
        //}
    }

    @Test
    public void testBuildPlayerBoard(){
        Cli cli = new Cli(2);
        Player a = new Player("a", "aaa", true);
        Player b = new Player("b", "bbb", false);
        Player c = new Player("c", "ccc", false);
        Player d = new Player("d", "ddd", false);
        Player e = new Player("e", "eee", false);
        a.getBoard().addDamage(b, 2);
        a.getBoard().addDamage(c, 1);
        a.getBoard().addDamage(b, 3);
        a.getBoard().addDamage(e, 2);
        a.getBoard().addDamage(d, 5);
        SimplePlayer player = new SimplePlayer(a);
        Map<String, String> enemies = new HashMap<>();
        enemies.put(b.getNickname(), RED_W);
        enemies.put(c.getNickname(), WHITE_W);
        enemies.put(d.getNickname(), GREEN_W);
        enemies.put(e.getNickname(), BLUE_W);
        cli.setEnemiesColor(enemies);
        Logger.print(cli.buildPlayerBoard(player).toString());
    }

    @Test
    public void testPrintMarks(){
        Cli cli = new Cli(2);
        Player a = new Player("a", "aaa", true);
        Player b = new Player("b", "bbb", false);
        Player c = new Player("c", "ccc", false);
        Player d = new Player("d", "ddd", false);
        Player e = new Player("e", "eee", false);
        a.getBoard().addMarks(b, 2);
        a.getBoard().addMarks(c, 1);
        a.getBoard().addMarks(b, 3);
        a.getBoard().addMarks(e, 1);
        a.getBoard().addMarks(d, 1);
        SimplePlayer player = new SimplePlayer(a);
        Map<String, String> enemies = new HashMap<>();
        enemies.put(b.getNickname(), RED_W);
        enemies.put(c.getNickname(), WHITE_W);
        enemies.put(d.getNickname(), GREEN_W);
        enemies.put(e.getNickname(), BLUE_W);
        cli.setEnemiesColor(enemies);
        cli.printMarks(player);
    }

    @Test
    public void testPrintAmmo(){
        Cli cli = new Cli(2);
        Player a = new Player("a", "aaa", true);
        List<Color> ammo= new ArrayList<>();
        ammo.add(Color.RED);
        ammo.add(Color.BLUE);
        ammo.add(Color.RED);
        a.getBoard().addAmmo(ammo);
        SimplePlayer player = new SimplePlayer(a);
        cli.printAmmo(player);
    }

    @Test
    public void testPlayerInfo(){
        testPrintMarks();
        testBuildPlayerBoard();
        testPrintAmmo();
    }
}

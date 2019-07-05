package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.turntests.DebugView;
import it.polimi.ingsw.utils.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Effect
 */
public class TestEffect {

    /**
     * Test if Effect constructor throws NullPointerException when node parameter is null
     */
    @Test
    public void FailIfNodeIsNull(){
        try{
            new Effect(null);
        }
        catch(NullPointerException ex){
            assertThat(ex.getMessage(),is("Effect constructor requires a not null node parameter."));
        }
    }

    /**
     * Test if Effect constructor doesn't throw any Exception when all the parameters are valid
     */
    @Test
    public void CorrectEffect(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test1.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);
            String msg="Effect {\n" +
                    "ref_id: 1\n" +
                    "name: nome\n" +
                    "cost: RED, BLUE, YELLOW\n" +
                    "Target: {type: PLAYER, minNumber: 1, maxNumber: 1, minPlayerIn: 0, maxPlayerIn: 0, prevConstraints: {none}\n" +
                    "requirements: {VISIBLE: TRUE}\n" +
                    "actions: {DAMAGE: 2, MARK: 1}\n" +
                    "extra: {extraName: value}\n" +
                    "}";
            assertEquals(msg,effect.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }
    }

    @Test
    public void CorrectCostEffect(){
        try{
            Weapon weapon= new Weapon("id","distruttore", "/weapons/distruttore.xml");
            weapon.init();
            Effect effect=weapon.getEffect(2);
            String msg="Effect {\n" +
                    "ref_id: 2\n" +
                    "name: SECONDO AGGANCIO\n" +
                    "cost: RED\n" +
                    "Target: {type: PLAYER, minNumber: 1, maxNumber: 1, minPlayerIn: 1, maxPlayerIn: 1, prevConstraints: {0}}\n" +
                    "requirements: {VISIBLE: TRUE}\n" +
                    "actions: {MARK: 1}\n" +
                    "extra: none\n" +
                    "}";
            assertEquals(msg,effect.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected Exception has been thrown");
        }
    }

    @Test
    public void CorrectEmptyCostEffect(){
        try{
            Weapon weapon= new Weapon("id","distruttore", "/weapons/distruttore.xml");
            weapon.init();
            Effect effect=weapon.getEffect(1);
            String msg="Effect {\n" +
                    "ref_id: 1\n" +
                    "name: BASE\n" +
                    "cost: none\n" +
                    "Target: {type: PLAYER, minNumber: 1, maxNumber: 1, minPlayerIn: 1, maxPlayerIn: 1, prevConstraints: {none}\n" +
                    "requirements: {VISIBLE: TRUE}\n" +
                    "actions: {DAMAGE: 2, MARK: 1}\n" +
                    "extra: none\n" +
                    "}";
            assertEquals(msg,effect.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected Exception has been thrown");
        }
    }

    @Test
    public void testVisible(){
        //base effect DISTRUTTORE
        try {
            Weapon weapon=new Weapon("weapon1","distruttore","/weapons/distruttore.xml");
            weapon.init();

            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(0,0));

            Effect base= weapon.getEffect("SECONDO AGGANCIO");

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: TRUE
            values.add(base.canUse(new Turn(game)));

            //SECOND: TRUE
            values.add(base.canUse(new Turn(game)));

            //THIRD: FALSE
            values.add(base.canUse(new Turn(game)));

            //FOURTH: TRUE
            values.add(base.canUse(new Turn(game)));

            assertThat(new Boolean[]{true,true,true,true},is(values.toArray()));
        } catch (Exception e) {
            fail("Unhandled Exception has been thrown.");
        }
    }

    @Test
    public void testDifferentConstraint(){
        //base effect DISTRUTTORE
        try {
            Weapon weapon=new Weapon("weapon1","distruttore","/weapons/distruttore.xml");
            weapon.init();
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);


            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();
            first.setPosition(game.getGameBoard().getSquare(1,1));
            second.setPosition(game.getGameBoard().getSquare(1,1));
            third.setPosition(game.getGameBoard().getSquare(1,1));
            fourth.setPosition(game.getGameBoard().getSquare(1,1));
            Turn turn= new Turn(game);
            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(0,0));

            Effect base= weapon.getEffect("SECONDO AGGANCIO");

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: TRUE
            turn.addSelectedPlayer(second);
            turn.addSelectedPlayer(third);
            values.add(base.canUse(turn));

            //SECOND: FALSE
            turn.clearSelectedPlayers();
            turn.addSelectedPlayer(fourth);
            turn.addSelectedPlayer(third);
            values.add(base.canUse(turn));

            //THIRD: FALSE
            turn.clearSelectedPlayers();
            values.add(base.canUse(turn));

            //FOURTH: TRUE
            turn.clearSelectedPlayers();
            values.add(base.canUse(turn));

            //EXTRA: TRUE
            turn.clearSelectedPlayers();
            turn.addSelectedPlayer(second);
            turn.addSelectedPlayer(third);
            values.add(base.canUse(turn));

            assertThat(new Boolean[]{true,true,true,true,true},is(values.toArray()));
        } catch (Exception e) {
            fail("Unhandled Exception has been thrown.");
        }
    }

    @Test
    public void testDistance(){
        try{
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test2.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(0,2));
            third.setPosition(board.getSquare(1,3));

            ArrayList<Boolean> values= new ArrayList<>();
            //FIRST: FALSE
            values.add(effect.canUse(new Turn(game)));

            assertThat(new Boolean[]{false},is(values.toArray()));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }

    }

    @Test
    public void testDirectionAndSquareTarget(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test3.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(1,2));

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: TRUE
            values.add(effect.canUse(new Turn(game)));

            assertThat(new Boolean[]{true},is(values.toArray()));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }

    }

    @Test
    public void testRoomTarget(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test4.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(0,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(1,1));
            fourth.setPosition(board.getSquare(2,3));

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: TRUE
            values.add(effect.canUse(new Turn(game)));

            assertThat(new Boolean[]{true},is(values.toArray()));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }

    }

    @Test
    public void testDirectionTarget(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test5.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);
            Player fifth=new Player("fifth","fifth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);
            game.addPlayer(fifth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(1,3));
            second.setPosition(board.getSquare(1,0));
            third.setPosition(board.getSquare(2,1));
            fourth.setPosition(board.getSquare(1,2));
            fifth.setPosition(board.getSquare(1,1));

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: TRUE
            values.add(effect.canUse(new Turn(game)));

            //SECOND: TRUE
            first.setPosition(board.getSquare(1,1));
            second.setPosition(board.getSquare(1,1));
            values.add(effect.canUse(new Turn(game)));

            List<List<Player>> list=effect.getShootablePlayers(new Turn(game));
            values.add(list.size()==2&&list.get(0).size()==3&&list.get(1).size()==3);
            assertThat(new Boolean[]{true,true,true},is(values.toArray()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }

    }

    @Test
    public void TestShiftable(){
        try {
            Weapon weapon=new Weapon("weapon1","cannonevortex","/weapons/cannonevortex.xml");
            weapon.init();
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(2,3));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(2,3));

            Effect base= weapon.getEffect(1);

            ArrayList<Boolean> values= new ArrayList<>();

            //FIRST: false
            values.add(base.canUse(new Turn(game)));
            
            //SECOND: true
            second.setPosition(board.getSquare(1,3));
            values.add(base.canUse(new Turn(game)));

            //THIRD: true
            second.setPosition(board.getSquare(1,2));
            values.add(base.canUse(new Turn(game)));

            //FOURTH: true
            second.setPosition(board.getSquare(1,0));
            values.add(base.canUse(new Turn(game)));

            assertThat(new Boolean[]{false,true,true,true},is(values.toArray()));
        } catch (Exception e) {
            fail("Unhandled exception has been thrown.");
        }
    }

    @Test
    public void TestMeTarget(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/resources/effect_test6.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect = new Effect(root);
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            GameBoard board=game.getGameBoard();

            first.setPosition(board.getSquare(1, 1));
            second.setPosition(board.getSquare(2, 3));
            third.setPosition(board.getSquare(2, 3));

            ArrayList<Boolean> values = new ArrayList<>();

            //FIRST: true
            values.add(effect.canUse(new Turn(game)));

            //SECOND: true
            values.add(effect.canUse(new Turn(game)));

            //THIRD: false
            first.setPosition(board.getSquare(0, 1));
            second.setPosition(board.getSquare(2, 1));
            third.setPosition(board.getSquare(2, 2));
            values.add(effect.canUse(new Turn(game)));

            assertThat(new Boolean[]{true,true,false},is(values.toArray()));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }
    }


    @Test
    public void PerformEffectTest(){
        Game game = new Game();
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
        p1.setStatus(PlayerStatus.PLAYING);
        p2.setStatus(PlayerStatus.WAITING);
        p3.setStatus(PlayerStatus.WAITING);
        p4.setStatus(PlayerStatus.WAITING);
        p1.setPosition(game.getGameBoard().getSquare(1,0));
        p2.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(1,1));
        p4.setPosition(game.getGameBoard().getSquare(0,1));

        Weapon weapon=new Weapon(new Card("id","VULCANIZZATORE","/weapons/vulcanizzatore.xml"));
        p1.addWeapon(weapon);

        Turn turn=new Turn(game);

        List<List<String>> selected= new ArrayList<>();
        List<String> temp=new ArrayList<>();
        temp.add(p2.getNickname());
        temp.add(p3.getNickname());
        selected.add(temp);

        assertTrue(weapon.getEffect(2).checkSelected(selected,turn));

        weapon.getEffect(2).perform(selected,turn);
        assertEquals(1, p2.getBoard().getHealthBar().size());
        assertEquals(1, p3.getBoard().getHealthBar().size());
        assertEquals(1, p2.getBoard().getMarks().size());
        assertEquals(1, p3.getBoard().getMarks().size());

        Logger.log("TEST FINISHED");
    }
}

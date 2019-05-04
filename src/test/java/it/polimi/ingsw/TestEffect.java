package it.polimi.ingsw;

import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Weapon;
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
            Document document = builder.parse(new File("src/test/Resources/effect_test1.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);
            String msg="Effect {\n" +
                    "ref_id: 1\n" +
                    "name: nome\n" +
                    "cost: none\n" +
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
    public void testVisible(){
        //base effect DISTRUTTORE
        try {
            Weapon weapon=new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            weapon.init();

            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);

            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(0,0));

            Effect base= weapon.getEffect("base");

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(3);

            //FIRST: TRUE
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, new ArrayDeque<>(), board));

            //SECOND: TRUE
            players.clear();
            players.add(first);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(second,players, new ArrayDeque<>(), board));

            //THIRD: FALSE
            players.clear();
            players.add(first);
            players.add(second);
            players.add(fourth);
            values.add(base.canUse(third,players, new ArrayDeque<>(), board));

            //FOURTH: TRUE
            players.clear();
            players.add(first);
            players.add(second);
            players.add(third);
            values.add(base.canUse(fourth,players, new ArrayDeque<>(), board));

            assertThat(new Boolean[]{true,true,false,true},is(values.toArray()));
        } catch (ParserConfigurationException e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            fail("Unhandled IOException has been thrown.");
        } catch (SAXException e) {
            fail("Unhandled SAXException has been thrown.");
        }
    }

    @Test
    public void testDifferentConstraint(){
        //base effect DISTRUTTORE
        try {
            Weapon weapon=new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            weapon.init();
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(0,0));

            Effect base= weapon.getEffect("opzionale");

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(3);
            ArrayDeque<Player> shotPlayers= new ArrayDeque<>();

            //FIRST: TRUE
            players.add(second);
            players.add(third);
            players.add(fourth);
            shotPlayers.add(second);
            shotPlayers.add(third);
            values.add(base.canUse(first,players, shotPlayers,board));

            //SECOND: FALSE
            players.clear();
            shotPlayers.clear();
            players.add(first);
            players.add(third);
            players.add(fourth);
            shotPlayers.add(first);
            shotPlayers.add(third);
            shotPlayers.add(fourth);
            values.add(base.canUse(second,players, shotPlayers, board));

            //THIRD: FALSE
            players.clear();
            shotPlayers.clear();
            players.add(first);
            players.add(second);
            players.add(fourth);
            values.add(base.canUse(third,players, shotPlayers, board));

            //FOURTH: TRUE
            players.clear();
            players.add(first);
            players.add(second);
            players.add(third);
            shotPlayers.add(second);
            shotPlayers.add(third);
            values.add(base.canUse(fourth,players, shotPlayers, board));

            //EXTRA: TRUE
            players.clear();
            shotPlayers.clear();
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, shotPlayers, board));

            assertThat(new Boolean[]{true,false,false,true,true},is(values.toArray()));
        } catch (ParserConfigurationException e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            fail("Unhandled IOException has been thrown.");
        } catch (SAXException e) {
            fail("Unhandled SAXException has been thrown.");
        }
    }

    @Test
    public void testDistance(){
        try{
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test2.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(0,2));
            third.setPosition(board.getSquare(1,3));

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(2);

            //FIRST: FALSE
            players.add(second);
            players.add(third);
            values.add(effect.canUse(first,players, new ArrayDeque<>(), board));

            //SECOND: true
            players.clear();
            players.add(first);
            players.add(third);
            values.add(effect.canUse(second,players, new ArrayDeque<>(), board));

            //THIRD: true
            players.clear();
            players.add(first);
            players.add(second);
            values.add(effect.canUse(third,players, new ArrayDeque<>(), board));

            assertThat(new Boolean[]{false,true,true},is(values.toArray()));

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
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test3.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(1,2));

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(2);
            ArrayDeque<Player> shotPlayers= new ArrayDeque<>();

            //FIRST: TRUE
            players.add(second);
            players.add(third);
            values.add(effect.canUse(first,players, shotPlayers, board));

            //SECOND: FALSE
            players.add(first);
            players.add(third);
            shotPlayers.add(first);
            values.add(effect.canUse(second,players, shotPlayers, board));

            assertThat(new Boolean[]{true,false},is(values.toArray()));

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
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test4.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            first.setPosition(board.getSquare(0,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(1,1));
            fourth.setPosition(board.getSquare(2,3));

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(2);
            ArrayDeque<Player> shotPlayers= new ArrayDeque<>();

            //FIRST: TRUE
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(effect.canUse(first,players, shotPlayers, board));

            //SECOND: FALSE
            players.add(first);
            players.add(second);
            players.add(third);
            values.add(effect.canUse(fourth,players, shotPlayers, board));

            assertThat(new Boolean[]{true,false},is(values.toArray()));

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
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test5.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect=new Effect(root);

            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);
            Player fifth=new Player("fifth","fifth_motto",false);

            first.setPosition(board.getSquare(1,3));
            second.setPosition(board.getSquare(1,0));
            third.setPosition(board.getSquare(2,1));
            fourth.setPosition(board.getSquare(1,2));
            fifth.setPosition(board.getSquare(1,1));

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(2);
            ArrayDeque<Player> shotPlayers= new ArrayDeque<>();

            //FIRST: TRUE
            players.add(first);
            players.add(second);
            players.add(third);
            players.add(fourth);
            shotPlayers.add(first);
            values.add(effect.canUse(fifth,players,shotPlayers, board));

            //SECOND: TRUE
            first.setPosition(board.getSquare(1,1));
            second.setPosition(board.getSquare(1,1));
            values.add(effect.canUse(fifth,players,new ArrayDeque<>(), board));

            List<List<Player>> list=effect.getShootablePlayers(fifth,players,new ArrayDeque<>(), board);
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
    public void TestEffectSyntaxWeapons(){
        String folderName="src/main/Resources/weapons";
        String path="";
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        try{
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(0,0));

            for (File file:listOfFiles) {
                if (file.isFile()) {
                    System.out.println("Weapon: " + file.getName());
                    path=folderName.concat("/").concat(file.getName());
                    Weapon w= new Weapon("weapon_id","name",path);
                    w.init();

                    ArrayList<Player> players= new ArrayList<>(3);
                    ArrayDeque<Player> shotPlayers= new ArrayDeque<>();

                    players.add(second);
                    players.add(third);
                    players.add(fourth);
                    w.getEffect(1).canUse(first,players, shotPlayers, board);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.("+path+")");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.("+path+")");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.("+path+")");
        }
    }

    @Test
    public void TestShiftable(){
        try {
            Weapon weapon=new Weapon("weapon1","cannonevortex","src/main/Resources/weapons/cannonevortex.xml");
            weapon.init();
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(2,3));
            third.setPosition(board.getSquare(2,3));
            fourth.setPosition(board.getSquare(2,3));

            Effect base= weapon.getEffect(1);

            ArrayList<Boolean> values= new ArrayList<>();
            ArrayList<Player> players= new ArrayList<>(3);

            //FIRST: false
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, new ArrayDeque<>(), board));

            //SECOND: true
            second.setPosition(board.getSquare(1,3));
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, new ArrayDeque<>(), board));

            //THIRD: true
            second.setPosition(board.getSquare(1,2));
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, new ArrayDeque<>(), board));

            //FOURTH: true
            second.setPosition(board.getSquare(1,0));
            players.add(second);
            players.add(third);
            players.add(fourth);
            values.add(base.canUse(first,players, new ArrayDeque<>(), board));

            assertThat(new Boolean[]{false,true,true,true},is(values.toArray()));
        } catch (ParserConfigurationException e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            fail("Unhandled IOException has been thrown.");
        } catch (SAXException e) {
            fail("Unhandled SAXException has been thrown.");
        }
    }

    @Test
    public void TestMeTarget(){
        try {
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test6.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();

            Effect effect = new Effect(root);

            Player first = new Player("first", "first_motto", true);
            Player second = new Player("second", "second_motto", false);
            Player third = new Player("third", "third_motto", false);

            first.setPosition(board.getSquare(1, 1));
            second.setPosition(board.getSquare(2, 3));
            third.setPosition(board.getSquare(2, 3));

            ArrayList<Boolean> values = new ArrayList<>();
            ArrayList<Player> players = new ArrayList<>(2);

            //FIRST: true
            players.add(second);
            players.add(third);
            values.add(effect.canUse(first, players, new ArrayDeque<>(), board));

            //SECOND: true
            second.setPosition(board.getSquare(1, 1));
            third.setPosition(board.getSquare(1, 1));
            values.add(effect.canUse(first, players, new ArrayDeque<>(), board));

            //THIRD: false
            first.setPosition(board.getSquare(0, 1));
            second.setPosition(board.getSquare(2, 1));
            third.setPosition(board.getSquare(2, 2));
            values.add(effect.canUse(first, players, new ArrayDeque<>(), board));

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
}

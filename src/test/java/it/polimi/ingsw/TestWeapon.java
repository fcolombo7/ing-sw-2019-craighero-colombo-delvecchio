package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.WeaponEffectException;
import it.polimi.ingsw.exceptions.WeaponLoadException;
import it.polimi.ingsw.model.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.TargetType.PLAYER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Weapon
 */
public class TestWeapon {

   @Test
    public void CorrectInitialization(){
        Weapon weapon=new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
        weapon.init();
        String expected="Weapon {\n" +
                "Card {id: weapon1, name: distruttore, xmlFile: src/test/Resources/weapon_test1.xml}\n" +
                "initialized: true\n" +
                "ammo: BLUE, BLUE\n" +
                "loaded: true\n" +
                "Effects {1: base, 2: opzionale}\n" +
                "effectOrder: roots {\n" +
                "0: TreeNode<Integer>: {value: 1, children {TreeNode<Integer>: {value: -1, children {none}}, TreeNode<Integer>: {value: 2, children {TreeNode<Integer>: {value: -1, children {none}}}}}}\n" +
                "}\n" +
                "}";
        assertEquals(expected,weapon.toString());
    }

    /**
     * Test if Weapon's method getEffect throws WeaponEffectException when the effect isn't in the list of the effects of the weapon
     */
    @Test
    public void GetEffectNotPresent() {
        String eName = "effetto1";
        try {
            Weapon w = new Weapon("weapon1", "distruttore", "src/test/Resources/weapon_test1.xml");
            w.init();
            w.getEffect(eName);
            fail("Expected a WeaponEffectException to be thrown.");
        } catch (WeaponEffectException aWeaponEffectException) {
            assertThat(aWeaponEffectException.getMessage(), is("Effect not valid: The weapon does not have the effect '" + eName + "'."));
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

   /**
     * Test if Weapon's method getEffect doesn't throw WeaponEffectException when the effect is in the list of the effects of the weapon
     */
    @Test
    public void GetEffectPresent(){
        try {
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            assertThat(w.getEffect("base").getName(), is("base"));
        } catch(WeaponEffectException aWeaponEffectException){
            fail("An unexpected WeaponEffectException has been thrown.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    /**
     * Test if Weapon's method load throws WeaponLoadException when I try to load a weapon already loaded
     */
    @Test
    public void LoadWeaponAlreadyLoaded(){
        try{
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            w.load();
            fail("Expected a WeaponLoadException to be thrown.");
        } catch (WeaponLoadException aWeaponLoadException){
            assertThat(aWeaponLoadException.getMessage(), is("Loaded value not valid: The weapon is already loaded."));
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    /**
     * Test if Weapon's method unload throws WeaponLoadException when I try to unload a weapon already unloaded
     */
    @Test
    public void UnloadWeaponAlreadyUnloaded(){
        try{
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            w.unload();
            w.unload();
            fail("Expected a WeaponLoadException to be thrown.");
        } catch (WeaponLoadException aWeaponLoadException){
            assertThat(aWeaponLoadException.getMessage(), is("Loaded value not valid: The weapon is already unloaded."));
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    /**
     * Test if Weapon's method load doesn't throw WeaponLoadException when I try to load an unloaded weapon
     */
    @Test
    public void LoadWeaponUnloaded(){
        try{
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            w.unload();
            w.load();
            assertTrue(w.isLoaded());
        } catch (WeaponLoadException aWeaponLoadException){
            fail("An unexpected WeaponLoadException has been thrown.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    /**
     * Test if Weapon's method load doesn't throw WeaponLoadException when I try to unload a loaded weapon
     */
    @Test
    public void UnloadWeaponLoaded(){
        try{
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            w.unload();
            assertFalse(w.isLoaded());
        } catch (WeaponLoadException aWeaponLoadException){
            fail("An unexpected WeaponLoadException has been thrown.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    @Test
    public void GetNameTest(){
        Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
        assertEquals("distruttore",w.getName());
    }

    @Test
    public void TestParsingWeapons(){
        String folderName="src/main/Resources/weapons";
        String path="";
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        try{
            for (File file:listOfFiles) {
                if (file.isFile()) {
                    System.out.println("Weapon: " + file.getName());
                    path=folderName.concat("/").concat(file.getName());
                    Weapon w= new Weapon("weapon_id","name",path);
                    w.init();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    @Test
    public void TestEffectOrder1(){
        try
        {
            Weapon weapon=new Weapon("weapon1","cannonevortex","src/main/Resources/weapons/vulcanizzatore.xml");
            weapon.init();
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5,1);
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            weapon.initNavigation();
            ArrayList<Player> players= new ArrayList<>(3);
            List<Color> ammo=new ArrayList<>();
            ammo.add(Color.RED);
            ammo.add(Color.RED);
            ammo.add(Color.BLUE);
            ammo.add(Color.BLUE);
            first.getBoard().addAmmos(ammo);

            first.setPosition(board.getSquare(1,1));
            second.setPosition(board.getSquare(2,3));
            third.setPosition(board.getSquare(2,2));
            fourth.setPosition(board.getSquare(2,2));
            players.add(second);
            players.add(third);
            players.add(fourth);

            List<Effect> effects=weapon.getUsableEffect(first,players,new ArrayDeque<>(),board,true);
            assertThat(effects.get(0).getRefId(),is(1));

            effects.clear();
            third.setPosition(board.getSquare(1,2));
            fourth.setPosition(board.getSquare(1,2));
            effects=weapon.getUsableEffect(first,players,new ArrayDeque<>(),board,true);
            assertThat(effects.get(0).getRefId(),is(2));

        } catch (ParserConfigurationException e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            fail("Unhandled IOException has been thrown.");
        } catch (SAXException e) {
            fail("Unhandled SAXException has been thrown.");
        }
    }

    @Test
    public void TestEffectOrder2(){
        try
        {
            Weapon weapon=new Weapon("weapon1","myWeapon","src/test/Resources/weapon_test2.xml");
            weapon.init();
            GameBoard board=new GameBoard(TestGameboard.parsingXMLFile("src/test/Resources/gameboard_test1.xml"),5,1);
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            weapon.initNavigation();
            ArrayList<Player> players= new ArrayList<>(3);
            List<Color> ammo=new ArrayList<>();
            ammo.add(Color.RED);
            ammo.add(Color.RED);
            ammo.add(Color.BLUE);
            ammo.add(Color.BLUE);
            first.getBoard().addAmmos(ammo);


            first.setPosition(board.getSquare(1,0));
            second.setPosition(board.getSquare(1,2));
            third.setPosition(board.getSquare(1,1));
            fourth.setPosition(board.getSquare(2,2));
            players.add(second);
            players.add(third);
            players.add(fourth);

            List<Effect> effects=weapon.getUsableEffect(first,players,new ArrayDeque<>(),board,true);
            assertThat(effects.get(0).getRefId(),is(1));

            effects.clear();
            weapon.setNavigationNode(weapon.getEffect(1));
            effects=weapon.getUsableEffect(first,players,new ArrayDeque<>(),board,true);
            assertThat(effects.get(0).getRefId(),is(2));

            effects.clear();
            third.setPosition(board.getSquare(2,2));
            effects=weapon.getUsableEffect(first,players,new ArrayDeque<>(),board,true);
            assertTrue(effects.isEmpty());

        } catch (ParserConfigurationException e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            fail("Unhandled IOException has been thrown.");
        } catch (SAXException e) {
            fail("Unhandled SAXException has been thrown.");
        }
    }
}

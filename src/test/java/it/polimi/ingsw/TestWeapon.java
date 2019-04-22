package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.WeaponEffectException;
import it.polimi.ingsw.exceptions.WeaponLoadException;
import it.polimi.ingsw.model.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.TargetType.PLAYER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Weapon
 */
public class TestWeapon {
//    TODO: tutti i test sono da rivedere a causa delle modifiche alla classe

    @Test
    public void CorrectInitialization(){
        Weapon weapon=new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
        try {
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
        }
    }

    /**
     * Test if Weapon's method getEffect throws WeaponEffectException when the effect isn't in the list of the effects of the weapon
     */
    @Test
    public void GetEffectNotPresent(){
        String eName="effetto1";
        try{
            Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
            w.init();
            w.getEffect(eName);
            fail("Expected a WeaponEffectException to be thrown.");
        } catch(WeaponEffectException aWeaponEffectException){
            assertThat(aWeaponEffectException.getMessage(), is("Effect not valid: The weapon does not have the effect '"+eName+"'."));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("An unexpected ParserConfigurationException has been thrown.");
        } catch (IOException e) {
            e.printStackTrace();
            fail("An unexpected IOException has been thrown.");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("An unexpected SAXException has been thrown.");
        }
    }

    @Test
    public void GetNameTest(){
        Weapon w= new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
        assertEquals(w.getName(),"distruttore");
    }
}

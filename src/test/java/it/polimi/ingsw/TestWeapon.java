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
    public void initWeapon(){
        Weapon weapon=new Weapon("weapon1","distruttore","src/test/Resources/weapon_test1.xml");
        try {
            weapon.init();

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
//    /**
//     * Test if Weapon's method getEffect throws WeaponEffectException when the effect isn't in the list of the effects of the weapon
//     */
//    @Test
//    public void GetEffectNotPresent(){
//        try{
//            Weapon w= new Weapon("1", "a","");
//            w.init();
//            w.getEffect("effetto1");
//            fail("Expected a WeaponEffectException to be thrown.");
//        } catch(WeaponEffectException aWeaponEffectException){
//            assertThat(aWeaponEffectException.getMessage(), is("Effect not valid: The weapon does not have this effect."));
//        }
//
//    }
//
//    /**
//     * Test if Weapon's method getEffect doesn't throw WeaponEffectException when the effect is in the list of the effects of the weapon
//     */
//    @Test
//    public void GetEffectPresent(){
//        try {
//            Effect effetto1=new Effect("effetto1", new ArrayList<Color>(), null, new ArrayList<Action>(), null);
//            Effect effetto2=new Effect("effetto2", new ArrayList<Color>(), null, new ArrayList<Action>(), null);
//            List<Effect> effectlist=new ArrayList<>();
//            effectlist.add(effetto1);
//            effectlist.add(effetto2);
//            Weapon w = new Weapon("1", "a");
//            w.init(effectlist, new ArrayList<TreeNode>(), new ArrayList<Color>());
//            assertThat(w.getEffect("effetto1").getName(), is("effetto1"));
//        } catch(WeaponEffectException aWeaponEffectException){
//            fail("An unexpected WeaponEffectException has been thrown.");
//        }
//
//
//    }
//
//
//    /**
//     * Test if Weapon's method load throws WeaponLoadException when I try to load a weapon already loaded
//     */
//    @Test
//    public void LoadWeaponAlreadyLoaded(){
//        try{
//            Weapon w=new Weapon("1", "a");
//            w.init(new ArrayList<Effect>(),new ArrayList<TreeNode>(), new ArrayList<Color>() );
//            w.load();
//            fail("Expected a WeaponLoadException to be thrown.");
//        } catch (WeaponLoadException aWeaponLoadException){
//            assertThat(aWeaponLoadException.getMessage(), is("Loaded value not valid: The weapon is already loaded."));
//
//        }
//    }
//
//    /**
//     * Test if Weapon's method unload throws WeaponLoadException when I try to unload a weapon already unloaded
//     */
//    @Test
//    public void UnloadWeaponAlreadyUnloaded(){
//        try{
//            Weapon w=new Weapon("1", "a");
//            w.init(new ArrayList<Effect>(),new ArrayList<TreeNode>(), new ArrayList<Color>() );
//            w.unload();
//            w.unload();
//            fail("Expected a WeaponLoadException to be thrown.");
//        } catch (WeaponLoadException aWeaponLoadException){
//            assertThat(aWeaponLoadException.getMessage(), is("Loaded value not valid: The weapon is already unloaded."));
//
//        }
//    }
//
//    /**
//     * Test if Weapon's method load doesn't throw WeaponLoadException when I try to load an unloaded weapon
//     */
//    @Test
//    public void LoadWeaponUnloaded(){
//        try{
//            Weapon w=new Weapon("1", "a");
//            w.init(new ArrayList<Effect>(),new ArrayList<TreeNode>(), new ArrayList<Color>() );
//            w.unload();
//            w.load();
//            assertTrue(w.isLoaded());
//        } catch (WeaponLoadException aWeaponLoadException){
//            fail("An unexpected WeaponLoadException has been thrown.");
//        }
//    }
//
//    /**
//     * Test if Weapon's method load doesn't throw WeaponLoadException when I try to unload a loaded weapon
//     */
//    @Test
//    public void UnloadWeaponLoaded(){
//        try{
//            Weapon w=new Weapon("1", "a");
//            w.init(new ArrayList<Effect>(),new ArrayList<TreeNode>(), new ArrayList<Color>() );
//            w.unload();
//            assertTrue(!(w.isLoaded()));
//        } catch (WeaponLoadException aWeaponLoadException){
//            fail("An unexpected WeaponLoadException has been thrown.");
//        }
//    }

}

package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AmmoTileNotValidException;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Color;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class AmmoTile
 */
public class TestAmmoTile {

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidException when the first parameter (a1) is null
     */
    @Test
    public void AmmoTileWithFirstAmmoNull()
    {
        try {
            new AmmoTile(null, Color.RED,Color.BLUE,false);
            fail("Expected an AmmoTileNotValidException to be thrown");
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            assertThat(anAmmoTileNotValidException.getMessage(), is("AmmoTile is not valid: AmmoTile can't have a1 or a2 as null."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidException when the second parameter (a2) is nulll
     */
    @Test
    public void AmmoTileWithSecondAmmoNull()
    {
        try {
            new AmmoTile(Color.YELLOW, null,Color.BLUE,false);
            fail("Expected an AmmoTileNotValidException to be thrown");
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            assertThat(anAmmoTileNotValidException.getMessage(), is("AmmoTile is not valid: AmmoTile can't have a1 or a2 as null."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidException when there are three valid ammos and powerup
     */
    @Test
    public void AmmoTileWithThreeAmmosAndPowerup()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, Color.BLUE,true);
            fail("Expected an AmmoTileNotValidException to be thrown");
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            assertThat(anAmmoTileNotValidException.getMessage(), is("AmmoTile is not valid: AmmoTile can't have three ammo and powerup."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidException when there are two valid ammos and no powerup
     */
    @Test
    public void AmmoTileWithTwoAmmosAndNoPowerup()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, null,false);
            fail("Expected an AmmoTileNotValidException to be thrown");
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            assertThat(anAmmoTileNotValidException.getMessage(), is("AmmoTile is not valid: AmmoTile can't have two ammo and no powerup."));
        }
    }

    /**
     * Test if AmmoTile constructor doesn't throw exceptions when all the parameters are valid
     */
    @Test
    public void AmmoTileWithTwoAmmoAndPowerup()
    {
        try {
            AmmoTile ammo=new AmmoTile(Color.YELLOW, Color.RED, null,true);
            assertEquals("AmmoTile{a1:YELLOW, a2:RED, a3:null, powerup:true}", ammo.toString());
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            fail("An unexpected AmmoTileNotValidException has been thrown");
        }
    }

    /**
     * Test if AmmoTile constructor doesn't throw exceptions when all the parameters are valid
     */
    @Test
    public void AmmoTileWithThreeAmmoAndNoPowerup()
    {
        try {
            AmmoTile ammo=new AmmoTile(Color.YELLOW, Color.RED, Color.RED,false);
            assertEquals("AmmoTile{a1:YELLOW, a2:RED, a3:RED, powerup:false}", ammo.toString());
        } catch (AmmoTileNotValidException anAmmoTileNotValidException) {
            fail("An unexpected AmmoTileNotValidException has been thrown");
        }
    }

    /**
     * Test if AmmoTile get method throws AmmoTileIndexErrorException when the index is >=3
     */
    @Test
    public void AmmoTileIndexOutOfBound()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, Color.BLUE,false).getAmmo(3);

            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertThat(e.getMessage(), is("Index out of bound."));
        }catch (AmmoTileNotValidException anAmmoTileNotValidException){
            fail("Expected an IndexOutOfBoundsException to be thrown.\n"+anAmmoTileNotValidException.getMessage());
        }
    }

    /**
     * Test if AmmoTile get method throws AmmoTileIndexErrorException when the index is 2 and powerup is true
     */
    @Test
    public void AmmoTileIndexPowerupError()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.RED, null,true).getAmmo(2);

            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertThat(e.getMessage(), is("Index out of bound (this tile has a powerup)."));
        }catch (AmmoTileNotValidException anAmmoTileNotValidException){
            fail("Expected an IndexOutOfBoundsException to be thrown.\n"+anAmmoTileNotValidException.getMessage());
        }
    }

    /**
     * Test if AmmoTile get method doesn't throw AmmoTileIndexErrorException when the index is correct
     */
    @Test
    public void AmmoTileIndexCorrectWithPowerup()
    {
        try {
            assertEquals(Color.RED,new AmmoTile(Color.YELLOW, Color.RED, null,true).getAmmo(1));
        } catch (IndexOutOfBoundsException e) {
            fail("An unexpected a IndexOutOfBoundsException has been thrown");
        }catch (AmmoTileNotValidException anAmmoTileNotValidException){
            fail("An unexpected a AmmoTileNotValidException has been thrown");
        }
    }

    /**
     * Test if AmmoTile get method doesn't throw AmmoTileIndexErrorException when the index is correct
     */
    @Test
    public void AmmoTileIndexCorrectNoPowerup()
    {
        try {
            assertEquals(Color.RED,new AmmoTile(Color.YELLOW, Color.RED, Color.RED,false).getAmmo(2));
        } catch (IndexOutOfBoundsException anAmmoTileIndexErrorException) {
            fail("An unexpected a IndexOutOfBoundsException has been thrown");
        }catch (AmmoTileNotValidException anAmmoTileNotValidException){
            fail("An unexpected a AmmoTileNotValidException has been thrown");
        }
    }
}

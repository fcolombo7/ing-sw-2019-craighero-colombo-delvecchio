package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AmmoTileIndexErrorException;
import it.polimi.ingsw.exceptions.AmmoTileNotValidExceptoin;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Color;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit test for the class AmmoTile
 */
public class TestAmmoTile {

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidExceptoin when the first parameter (a1) is nulll
     */
    @Test
    public void AmmoTileWithFirstAmmoNull()
    {
        try {
            new AmmoTile(null, Color.RED,Color.BLUE,false);
            fail("Expected an AmmoTileNotValidExceptoin to be thrown");
        } catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin) {
            assertThat(anAmmoTileNotValidExceptoin.getMessage(), is("AmmoTile is not valid: AmmoTile can't have a1 or a2 as null."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidExceptoin when the second parameter (a2) is nulll
     */
    @Test
    public void AmmoTileWithSecondAmmoNull()
    {
        try {
            new AmmoTile(Color.YELLOW, null,Color.BLUE,false);
            fail("Expected an AmmoTileNotValidExceptoin to be thrown");
        } catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin) {
            assertThat(anAmmoTileNotValidExceptoin.getMessage(), is("AmmoTile is not valid: AmmoTile can't have a1 or a2 as null."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidExceptoin when there are three valid ammos and powerup
     */
    @Test
    public void AmmoTileWithThreeAmmosAndPowerup()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, Color.BLUE,true);
            fail("Expected an AmmoTileNotValidExceptoin to be thrown");
        } catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin) {
            assertThat(anAmmoTileNotValidExceptoin.getMessage(), is("AmmoTile is not valid: AmmoTile can't have three ammos and powerup."));
        }
    }

    /**
     * Test if AmmoTile constructor throws AmmoTileNotValidExceptoin when there are two valid ammos and no powerup
     */
    @Test
    public void AmmoTileWithTwoAmmosAndNoPowerup()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, null,false);
            fail("Expected an AmmoTileNotValidExceptoin to be thrown");
        } catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin) {
            assertThat(anAmmoTileNotValidExceptoin.getMessage(), is("AmmoTile is not valid: AmmoTile can't have two ammos and no powerup."));
        }
    }

    /**
     * Test if AmmoTile get method throws AmmoTileIndexErrorExceptoin when the index is >=3
     */
    @Test
    public void AmmoTileIndexOutOfBound()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.YELLOW, Color.BLUE,false).getAmmo(3);

            fail("Expected an AmmoTileIndexErrorException to be thrown");
        } catch (AmmoTileIndexErrorException anAmmoTileIndexErrorException) {
            assertThat(anAmmoTileIndexErrorException.getMessage(), is("Index is not valid: Index out of bound."));
        }catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin){
            fail("Expected an AmmoTileIndexErrorException to be thrown.\n"+anAmmoTileNotValidExceptoin.getMessage());
        }
    }

    /**
     * Test if AmmoTile get method throws AmmoTileIndexErrorExceptoin when the index is 2 and powerup is true
     */
    @Test
    public void AmmoTileIndexPowerupError()
    {
        try {
            new AmmoTile(Color.YELLOW, Color.RED, null,true).getAmmo(2);

            fail("Expected an AmmoTileIndexErrorException to be thrown");
        } catch (AmmoTileIndexErrorException anAmmoTileIndexErrorException) {
            assertThat(anAmmoTileIndexErrorException.getMessage(), is("Index is not valid: Index out of bound (this tile has a powerup)."));
        }catch (AmmoTileNotValidExceptoin anAmmoTileNotValidExceptoin){
            fail("Expected an AmmoTileIndexErrorException to be thrown.\n"+anAmmoTileNotValidExceptoin.getMessage());
        }
    }
}

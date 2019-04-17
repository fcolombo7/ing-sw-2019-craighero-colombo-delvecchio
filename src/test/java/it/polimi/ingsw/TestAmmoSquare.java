package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AmmoTileNotValidException;
import it.polimi.ingsw.exceptions.SquareContentException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class AmmoSquare
 */
public class TestAmmoSquare {

    /**
     * Test if setAmmoTile throws SquareContentException when an ammo is already set.
     */
    @Test
    public void AlreadySetAmmo()
    {
        AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED,new boolean[]{true,true,false,false},new int[]{2,3});
        try {
            ammoSquare.setAmmoTile(new AmmoTile(Color.RED,Color.BLUE,Color.YELLOW,false));
            ammoSquare.setAmmoTile(new AmmoTile(Color.BLUE,Color.BLUE,null,true));
            fail("Expected a SquareContentException to be thrown");
        } catch (SquareContentException e) {
            assertThat(e.getMessage(), is("The AmmoSquare already contains an AmmoTile."));
        }catch (AmmoTileNotValidException e){
            fail("An unexpected AmmoTileNotValidException has been thrown");
        }
    }

    /**
     * Test if popAmmoTile() throws SquareContentException when an ammo isn't set.
     */
    @Test
    public void PopWithNoAmmo()
    {
        AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED,new boolean[]{true,true,false,false},new int[]{2,3});
        try {
            AmmoTile ammoTile=ammoSquare.popAmmoTile();
            fail("Expected a SquareContentException to be thrown");
        } catch (SquareContentException e) {
            assertThat(e.getMessage(), is("The AmmoSquare doesn't contain an AmmoTile."));
        }
    }

    /**
     * Test if the method setAmmoTile does not throw Exception when ammo tile isn't already set.
     */
    @Test
    public void NotAlreadySetAmmo()
    {
        try{
            boolean []doors=new boolean[]{true,false,false,true};
            AmmoTile ammo=new AmmoTile(Color.RED,Color.RED,null,true);
            StringBuilder msg=new StringBuilder();
            msg.append("AmmoSquare {\n");
            msg.append("Room color: ").append(RoomColor.RED.name()).append(",\n");
            msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
                    .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
                    .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
                    .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
            msg.append("Visibility Matrix: ").append("null\n");
            msg.append("Ammo: ").append(ammo.toString()).append("\n");
            msg.append("}");
            AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED,doors, new int[]{2,3});
            ammoSquare.setAmmoTile(ammo);
            assertEquals(msg.toString(),ammoSquare.toString());
        }
        catch (AmmoTileNotValidException e) {
            fail("An unexpected AmmoTileNotValidException has been thrown");
        }
    }

    /**
     * Test if the method popAmmoTile does not throw Exception when ammo tile is set.
     */
    @Test
    public void PopWithAmmo()
    {
        try{
            boolean []doors=new boolean[]{true,false,false,true};
            AmmoTile ammo=new AmmoTile(Color.RED,Color.RED,null,true);
            StringBuilder msg=new StringBuilder();
            msg.append("AmmoSquare {\n");
            msg.append("Room color: ").append(RoomColor.RED.name()).append(",\n");
            msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
                    .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
                    .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
                    .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
            msg.append("Visibility Matrix: ").append("null\n");
            msg.append("Ammo: ").append("null").append("\n");
            msg.append("}");
            AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED,doors,new int[]{2,3});
            ammoSquare.setAmmoTile(ammo);
            ammo=ammoSquare.popAmmoTile();
            assertEquals(msg.toString(),ammoSquare.toString());
        }
        catch (AmmoTileNotValidException e) {
            fail("An unexpected AmmoTileNotValidException has been thrown");
        }
    }
}

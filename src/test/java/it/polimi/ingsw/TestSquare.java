package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Direction;
import it.polimi.ingsw.model.enums.RoomColor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Square
 */
public class TestSquare {

    /**
     * Test if Square constructor throws NullPointerException when the RoomColor is null
     */
    @Test
    public void SquareColorNull()
    {
        try {
            new AmmoSquare(null,new boolean[]{true,true,false,false},new int[]{2,3});
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("The roomColor param can't have null value."));
        }
    }

    /**
     * Test if Square constructor throws NullPointerException when the doors is null
     */
    @Test
    public void SquareDoorsNull()
    {
        try {
            new AmmoSquare(RoomColor.RED,null,new int[]{2,3});
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("The doors param can't have null value."));
        }
    }

    /**
     * Test if Square constructor throws IllegalArgumentException when the doors is malformed
     */
    @Test
    public void SquareDoorsMalformed()
    {
        try {
            new AmmoSquare(RoomColor.RED,new boolean[]{true,false},new int[]{2,3});
            fail("Expected a IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("The doors param length must be 4."));
        }
    }
    /**
     * Test if the Square constructor does not throw Exception when valid parameters are inserted
     */
    @Test
    public void SquareCorrectConstructor()
    {
        boolean []doors=new boolean[]{true,false,false,true};
        StringBuilder msg=new StringBuilder();
        msg.append("AmmoSquare {\n");
        msg.append("Room color: ").append(RoomColor.RED.name()).append(",\n");
        msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
                .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
                .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
                .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
        msg.append("Visibility Matrix: ").append("null\n");
        msg.append("Ammo: ").append("null\n");
        msg.append("}");
        assertEquals(msg.toString(),new AmmoSquare(RoomColor.RED,doors,new int[]{2,3}).toString());
    }

    /**
     * Test if the Square hasDoor method does not throw Exception when valid parameters are inserted
     */
    @Test
    public void SquareDoorDirection(){
        AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED, new boolean[]{true,true,false,false},new int[]{2,3});
        assertTrue(ammoSquare.hasDoor(Direction.NORTH));
    }

    /**
     * Test if the Square hasDoor method throws NullPointerException when direction is null
     */
    @Test
    public void SquareDoorInvalidDirection(){
        AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED, new boolean[]{true,true,false,false},new int[]{2,3});
        try{
            ammoSquare.hasDoor(null);
            fail("Expected a NullPointerException to be thrown");
        }catch(NullPointerException e) {
            assertThat(e.getMessage(), is("Param 'direction' is null."));
        }
    }

    /**
     * Test if the Square hasDoor method throws NullPointerException when direction is null
     */
    @Test
    public void SquareSetVisibilityMatrixInvalid(){
        AmmoSquare ammoSquare=new AmmoSquare(RoomColor.RED, new boolean[]{true,true,false,false},new int[]{2,3});
        try{
            ammoSquare.setVisibilityMatrix(null);
            fail("Expected a NullPointerException to be thrown");
        }catch(NullPointerException e) {
            assertThat(e.getMessage(), is("Param 'visibilityMatrix' is null."));
        }
    }

}

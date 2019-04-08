package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.SquareContentException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class WeaponSquare
 */
public class TestWeaponSquare {

    /**
     * Test if addWeapon() throws SquareContentException when the WeaponSquare is already full.
     */
    @Test
    public void AlreadyFullOfWeapon()
    {
        WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,new boolean[]{true,true,false,false});
        try {
            weaponSquare.addWeapon(new Weapon("w1","weapon1"));
            weaponSquare.addWeapon(new Weapon("w2","weapon2"));
            weaponSquare.addWeapon(new Weapon("w3","weapon3"));
            weaponSquare.addWeapon(new Weapon("w4","weapon4"));
            fail("Expected a SquareContentException to be thrown");
        } catch (SquareContentException e) {
            assertThat(e.getMessage(), is("The WeaponSquare already contains three weapons."));
        }catch (Exception e){
            fail("An unexpected Exception has been thrown");
        }
    }

    /**
     * Test if addWeapon() throws SquareContentException when the WeaponSquare is already full.
     */
    @Test
    public void SquareAlreadyContainsWeapon()
    {
        WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,new boolean[]{true,true,false,false});
        try {
            weaponSquare.addWeapon(new Weapon("w1","weapon1"));
            weaponSquare.addWeapon(new Weapon("w1","weapon2"));
            weaponSquare.addWeapon(new Weapon("w3","weapon3"));
            fail("Expected a SquareContentException to be thrown");
        } catch (SquareContentException e) {
            assertThat(e.getMessage(), is("Weapon is yet contained in the WeaponSquare."));
        }catch (Exception e){
            fail("An unexpected Exception has been thrown");
        }
    }

    /**
     * Test if popWeapon() throws SquareContentException when there are not set weapon.
     */
    @Test
    public void PopWithNoAmmo()
    {
        WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,new boolean[]{true,true,false,false});
        try {
            Weapon weapon=weaponSquare.popWeapon(0);
            fail("Expected a SquareContentException to be thrown");
        } catch (SquareContentException e) {
            assertThat(e.getMessage(), is("The WeaponSquare doesn't contain any weapon."));
        }
    }

    /**
     * Test if popWeapon() throws SquareContentException when there aren no set weapon.
     */
    @Test
    public void PopWithNotValidIndex()
    {
        WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,new boolean[]{true,true,false,false});
        try {
            weaponSquare.addWeapon(new Weapon("w1","weapon1"));
            weaponSquare.addWeapon(new Weapon("w2","weapon2"));
            Weapon weapon=weaponSquare.popWeapon(2);
            fail("Expected a SquareContentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Invalid index."));
        }
    }

    /**
     * Test if the method addWeapon does not throw Exception when the parameter are valid.
     */
    @Test
    public void NotAlreadySetWeapon()
    {
        try{
            boolean []doors=new boolean[]{true,false,false,true};
            Weapon weapon=new Weapon("id","name");
            StringBuilder msg=new StringBuilder();
            msg.append("WeaponSquare {\n");
            msg.append("Room color: ").append(RoomColor.RED.name()).append(",\n");
            msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
                    .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
                    .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
                    .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
            msg.append("Visibility Matrix: ").append("null\n");
            msg.append("Weapons: \n").append(((Card)weapon).toString()).append("\n");
            msg.append("}");
            WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,doors);
            weaponSquare.addWeapon(weapon);
            assertEquals(msg.toString(),weaponSquare.toString());
        }catch(Exception e){
            fail("An unexpected exception has been thrown");
        }
    }

    /**
     * Test if the method popWeapon does not throw Exception when the parameter are valid.
     */
    @Test
    public void PopWithAmmo()
    {
        try{
            boolean []doors=new boolean[]{true,false,false,true};
            StringBuilder msg=new StringBuilder();
            Weapon weapon=new Weapon("id","name");
            msg.append("WeaponSquare {\n");
            msg.append("Room color: ").append(RoomColor.RED.name()).append(",\n");
            msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
                    .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
                    .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
                    .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
            msg.append("Visibility Matrix: ").append("null\n");
            msg.append("Weapons: empty\n").append("\n");
            msg.append("}");
            WeaponSquare weaponSquare=new WeaponSquare(RoomColor.RED,doors);
            weaponSquare.addWeapon(weapon);
            weapon=weaponSquare.popWeapon(0);
            assertEquals(msg.toString(),weaponSquare.toString());
        }catch(Exception e){
            fail("An unexpected exception has been thrown");
        }
    }
}

package it.polimi.ingsw;

import it.polimi.ingsw.model.Target;
import it.polimi.ingsw.model.TargetType;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Target
 */
public class TestTarget {

    /**
     * Test if Target constructor throws NullPointerException when the type parameter is null
     */
    @Test
    public void FailIfTypeIsNull(){
        try{
            new Target(null, 0,0,0,0,new ArrayList<String>());
            fail("Expected a NullPointerException to be thrown");
        }catch(NullPointerException ex){
            assertThat(ex.getMessage(),is("Target constructor must have a not null 'type' value."));
        }
    }

    /**
     * Test if Target constructor doesn't throw any Exception when all the parameters are valid
     */
    @Test
    public void FailIfConstraintIsNull(){
        try{
            new Target(TargetType.PLAYER, 0,0,0,0,null);
            fail("Expected a NullPointerException to be thrown");
        }catch(NullPointerException ex){
            assertThat(ex.getMessage(),is("Target constructor must have a not null 'prevConstraints' value."));
        }
    }

    @Test
    public void CorrectConstructor(){
        try{
            String msg="Target:\n{type: PLAYER, minNumber: 0, maxNumber: 0, minPlayerIn: 0, maxPlayerIn: 0, prevConstraints: { } }";
            assertEquals(msg,new Target(TargetType.PLAYER, 0,0,0,0,new ArrayList<>()).toString());
        }catch(Exception ex){
            ex.printStackTrace();
            fail("An unxpected Exception has been thrown");
        }
    }

}

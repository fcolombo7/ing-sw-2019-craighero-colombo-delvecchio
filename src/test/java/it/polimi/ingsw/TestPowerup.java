package it.polimi.ingsw;

import it.polimi.ingsw.model.Powerup;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit test for the class Powerup
 */
public class TestPowerup {

    @Test
    public void CorrectInitialization(){
        Powerup powerup=new Powerup("powerup1","Granata","src/test/Resources/powerup_test1.xml");
        try {
            powerup.init();
            String expected="Powerup {\n" +
                    "Card {id: powerup1, name: Granata, xmlFile: src/test/Resources/powerup_test1.xml}\n" +
                    "initialized: true\n" +
                    "Color: RED\n" +
                    "Effect: {1: e}\n" +
                    "Timing: COUNTERATTACK\n" +
                    "}";
            assertEquals(expected,powerup.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }
}

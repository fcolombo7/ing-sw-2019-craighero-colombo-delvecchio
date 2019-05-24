package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class Powerup
 */
public class TestPowerup {

    @Test
    public void TestParsingPowerups(){
        String folderName="src/main/Resources/powerups";
        String path="";
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        try{
            for (File file:listOfFiles) {
                if (file.isFile()) {
                    System.out.println("Powerup: " + file.getName());
                    path=folderName.concat("/").concat(file.getName());
                    Powerup p= new Powerup("powe_id","name",path);
                    p.init();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception has been thrown.");
        }
    }

    @Test
    public void TestPowerupWithDeadPlayer(){
        try {
            Powerup powerup= new Powerup("id","nome","src/main/Resources/powerups/raggiocineticor.xml");
            powerup.init();
            Game game=new Game();
            Player first=new Player("first","first_motto",true);
            Player second=new Player("second","second_motto",false);
            Player third=new Player("third","third_motto",false);
            Player fourth=new Player("fourth","fourth_motto",false);

            game.addPlayer(first);
            game.addPlayer(second);
            game.addPlayer(third);
            game.addPlayer(fourth);

            game.setGameBoard(1);
            first.setPosition(game.getGameBoard().getSquare(1,1));
            second.setPosition(null);
            third.setPosition(null);
            fourth.setPosition(null);

            assertThat(powerup.getEffect().canUse(new Turn(game)), is(false));
        } catch (Exception e) {
            fail("Unhandled ParserConfigurationException has been thrown.");
        }
    }
}

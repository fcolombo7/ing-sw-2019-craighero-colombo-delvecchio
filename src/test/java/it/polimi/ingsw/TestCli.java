package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.ui.Cli;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static it.polimi.ingsw.TestGameboard.parsingXMLFile;
import static it.polimi.ingsw.utils.Costants.*;

public class TestCli {

    public static void main(String[] arg){
        Node node = null;
        try {
            node = parsingXMLFile("src/main/Resources/boards/board1.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        GameBoard board=new GameBoard(node,5,1);
        Cli cli = new Cli(board);
        cli.printMap();
    }

    @Test
    public void testBuildMap() throws IOException, SAXException, ParserConfigurationException {
        Node node = parsingXMLFile("src/main/Resources/boards/board1.xml");
        GameBoard board=new GameBoard(node,5,1);
        Cli cli = new Cli(board);
        cli.printMap();
    }
}

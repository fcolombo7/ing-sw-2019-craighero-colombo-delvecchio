package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.MatrixHelper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestGameboard {

    static Node parsingXMLFile(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) throws SAXException {
                System.out.println("WARNING : " + e.getMessage()); // do nothing
                throw e;
            }

            @Override
            public void error(SAXParseException e) throws SAXException {
                System.out.println("ERROR : " + e.getMessage());
                throw e;
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                System.out.println("FATAL : " + e.getMessage());
                throw e;
            }
        });
        Document document = builder.parse(new File(filename));
        document.normalizeDocument();
        Element root = document.getDocumentElement();
        root.normalize();

        return root;
    }

    @Test
    public void testParsing() {
        try {
            parsingXMLFile("src/test/resources/gameboard_test1.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            fail("Unexpected IOException has been thrown");
        }
    }

    @Test
    public void getDistanceMatrixFirstTest() throws IOException, SAXException, ParserConfigurationException {
        Node node = parsingXMLFile("src/test/resources/gameboard_test1.xml");
        GameBoard board=new GameBoard(node,5,1);
        assertThat(board.getDistanceMatrix(1, 2, 2).toString(), is((new MatrixHelper(new boolean[][] {{false, true, true, false}, {true, true, true, true}, {false, true, false, true}})).toString()));
    }

    @Test
    public void getDistanceMatrixSecondTest() throws IOException, SAXException, ParserConfigurationException {
        Node node = parsingXMLFile("src/test/resources/gameboard_test1.xml");
        GameBoard board=new GameBoard(node,5,1);
        assertThat(board.getDistanceMatrix(0, 0, 2).toString(), is((new MatrixHelper(new boolean[][] {{true, true, true, false}, {true, true, false, false}, {false, false, false, false}})).toString()));
    }

    @Test
    public void getDistanceMatrixThirdTest() throws IOException, SAXException, ParserConfigurationException {
        Node node = parsingXMLFile("src/test/resources/gameboard_test1.xml");
        GameBoard board=new GameBoard(node,5,1);
        assertThat(board.getDistanceMatrix(1, 3, 0).toString(), is((new MatrixHelper(new boolean[][] {{false, false, false, false}, {false, false, false, true}, {false, false, false, false}})).toString()));
    }
}

package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.annotation.Resources;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestGameboard {

    @Test
    public void testParsing() {
        try {

            //Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                    System.out.println("WARNING : " + e.getMessage()); // do nothing
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
            //Build Document
            Document document = builder.parse(new File("src/test/Resources/gameboard_test1.xml"));
            //Normalize the XML Structure; It's just too important !!
            document.normalizeDocument();
            //Here comes the root node
            Element root = document.getDocumentElement();
            root.normalize();
            System.out.println(root.getNodeName());

            GameBoard gameBoard = new GameBoard(root, 5);
            System.out.println(root.getNodeName());

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
}

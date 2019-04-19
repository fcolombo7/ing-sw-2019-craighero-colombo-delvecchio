package it.polimi.ingsw;

import it.polimi.ingsw.model.Effect;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit test for the class Effect
 */
public class TestEffect {

    /**
     * Test if Effect constructor throws NullPointerException when node parameter is null
     */
    @Test
    public void FailIfNodeIsNull(){
        try{
            new Effect(null);
        }
        catch(NullPointerException ex){
            assertThat(ex.getMessage(),is("Effect constructor requires a not null node parameter."));
        }
    }

    /**
     * Test if Effect constructor doesn't throw any Exception when all the parameters are valid
     */
    @Test
    public void CorrectEffect(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/test/Resources/effect_test1.xml"));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();
            new Effect(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Unexpected ParserConfigurationException has been thrown");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Unexpected SAXException has been thrown");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException has been thrown");
        }
    }
}

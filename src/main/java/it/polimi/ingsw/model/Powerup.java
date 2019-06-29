package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CardNotInitializedException;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.utils.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * This class represents a power up card
 */

public class Powerup extends Card {

    /**
     * This attribute contains the time when the power up card can be used
     */
    private transient TurnStatus timing;

    /**
     * This attribute contains the color of the power up card (a power up card can be used as a substitute
     * of an ammo tile of the same color)
     */
    private transient Color color;

    /**
     * This attribute specifies if the power up card is initialized or not
     */
    private transient boolean initialized;

    /**
     * This attribute contains the effect of the power up
     */
    private transient Effect effect;

    /**
     * This constructor instantiates a power up calling the constructor of Card (Powerup extends Card)
     * @param id representing the id of the power up card
     * @param name representing the name of the power up
     */
    public Powerup(String id, String name, String initXML){
        super(id, name, initXML);
        initialized=false;
    }

    /**
     * This constructor instantiates and initialized a powerup
     * @param card representing the card object you want to instantiate
     */
    public Powerup(Card card){
        this(card.getId(),card.getName(),card.getInitXML());
        init();
    }

    /**
     * This method returns true if the power up is initialized
     * @return boolean as true if the power up is initialized
     */
    public boolean isInit(){
        return initialized;
    }

    /**
     * TODO: insert the validation with DTD
     * This method initialized a powerup
     */
    public void init(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            /*
        factory.setValidating(true);

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
        */
            Document document = builder.parse(new File(getInitXML()));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();
            /*TODO: Controllo per la verifica che id e name siano giusti?
             * String id=root.getAttribute("id");
             * if(!id.equals(getId())) throw new InvalidInitializationException;
             */
            setColor(root.getElementsByTagName("color").item(0));
            setTiming(root.getElementsByTagName("timing").item(0));
            effect = new Effect(root.getElementsByTagName("effect").item(0));

            this.initialized = true;
        } catch (ParserConfigurationException e) {
            Logger.logServer("Parsing errors occur during the initialization of the powerup:\n"+this.toString());
            Logger.logErr(e.getMessage());
        } catch (IOException e) {
            Logger.logServer("IO errors occur during the initialization of the powerup:\n"+this.toString());
            Logger.logErr(e.getMessage());
        } catch (SAXException e) {
            Logger.logServer("SAX errors occur during the initialization of the powerup:\n"+this.toString());
            Logger.logErr(e.getMessage());
        }
    }

    private void setColor(Node node) {
        String value=node.getFirstChild().getNodeValue();
        if(value==null) throw new NullPointerException("Powerup '"+getName()+"' contains a null color value.");
        if(value.trim().length()!=1) throw new IllegalArgumentException("Powerup '"+getName()+"' contains an invalid color (length).");
        char car=value.charAt(0);
        switch (car){
            case 'R': color = Color.RED; break;
            case 'B': color = Color.BLUE; break;
            case 'Y': color = Color.YELLOW; break;
            default: throw new IllegalArgumentException("Powerup '"+getName()+"' contains an invalid color.");
        }
    }

    private void setTiming(Node node) {
        String value=node.getFirstChild().getNodeValue();
        if(value==null) throw new NullPointerException("Powerup '"+getName()+"' contains a null timing value.");
        for (TurnStatus val:TurnStatus.values()) {
            if(val.name().equalsIgnoreCase(value)) {
                timing = val;
                return;
            }
        }
        throw new IllegalArgumentException("Powerup '"+getName()+"' contains an invalid timing.");
    }

    /**
     * This method returns the effect of the power up
     * @return Effect representing the effect of the power up
     * @throws CardNotInitializedException when the card is not initialized
     */
    public Effect getEffect() {
        if(!initialized) throw new CardNotInitializedException("Can't get the powerup effect: the card is not initialized.");
        return effect;
    }

    /**
     * This method returns the color of the power up card
     * @return Color representing the color of the power up card
     * @throws CardNotInitializedException when the card is not initialized
     */
    public Color getColor(){
        if(!initialized) throw new CardNotInitializedException("Can't get the powerup color: the card is not initialized.");
        return color;
    }

    /**
     * This method returns the time when the power up can be used
     * @return TurnStatus representing the time when the power up can be used
     * @throws CardNotInitializedException when the card is not initialized
     */
    public TurnStatus getTiming() {
        if(!initialized) throw new CardNotInitializedException("Can't get the powerup timing: the card is not initialized.");
        return timing;
    }

    @Override
    public String toString() {
        StringBuilder msg= new StringBuilder();
        msg.append("Powerup {\n");
        //card parameters
        msg.append(super.toString()).append("\n");
        //initialized
        msg.append("initialized: ").append(initialized).append("\n");
        if(initialized){
            //ammo
            msg.append("Color: ");
            msg.append(color.name()).append("\n");
            //effect
            msg.append("Effect: {");
            msg.append(effect.getRefId()).append(": ").append(effect.getName()).append("}\n");
            //timing
            msg.append("Timing: ");
            msg.append(timing.name()).append("\n");
        }
        msg.append("}");
        return msg.toString();
    }
}

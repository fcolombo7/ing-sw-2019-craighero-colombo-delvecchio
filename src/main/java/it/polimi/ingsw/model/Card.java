package it.polimi.ingsw.model;

/**
 * This class represents a generic card
 */
public class Card {

    /**
     * This attribute contains the path of the XML file used for the card initialization
     */
    private final String initXML;
    /**
     * This attribute contains the card id
     */
    private final String id;

    /**
     * This attribute contains the card name
     */
    private final String name;

    /**
     * This constructor instantiates a generic card
     * @param id representing the id of the card
     * @param name representing the name of the card
     * @param initXML representing the path of the XML file used for the card initialization
     */
    public Card(String id, String name, String initXML){
        if(id==null||name==null||initXML==null) throw new NullPointerException("Card constructor parameters must be initialized.");
        this.id=id;
        this.name=name;
        this.initXML=initXML;
    }

    /**
     * This method returns the id of the card
     * @return String representing the id of the card
     */
    public String getId() {
        return id;
    }

    /**
     * This method returns the name of the card
     * @return String representing the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the path of the XML file used for the initialization of the card
     * @return String representing the the path of the XML file used for the card initialization
     */
    public String getInitXML(){
        return initXML;
    }

    /**
     * This method return the String representation of the Card object instantiated
     * @return String representing the Card object instantiated
     */
    @Override
    public String toString(){
        return "Card {id: "+id+", name: "+name+"}";
    }
}

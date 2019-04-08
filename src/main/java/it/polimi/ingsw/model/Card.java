package it.polimi.ingsw.model;

/**
 * This class represents a generic card
 */
public class Card {

    /**
     * This attribute contains the card id
     */
    private String id;

    /**
     * This attribute contains the card name
     */
    private String name;

    /**
     * This constructor instantiates a generic card
     * @param id representing the id of the card
     * @param name representing the name of the card
     */
    public Card(String id, String name){
        this.id=id;
        this.name=name;
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

    @Override
    public String toString(){
        return "Card {id: "+id+", name: "+name+"}";
    }
}

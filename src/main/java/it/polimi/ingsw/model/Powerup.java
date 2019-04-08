package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardNotInitializedException;

/**
 * This class represents a power up card
 */

public class Powerup extends Card {

    /**
     * This attribute contains the time when the power up card can be used
     */
    private TurnStatus timing;

    /**
     * This attribute contains the color of the power up card (a power up card can be used as a substitute
     * of an ammo tile of the same color)
     */
    private Color color;

    /**
     * This attribute specifies if the power up card is initialized or not
     */
    private boolean initialized;

    /**
     * This attribute contains the effect of the power up
     */
    private Effect effect;

    /**
     * This constructor instantiates a power up calling the constructor of Card (Powerup extends Card)
     * @param id representing the id of the power up card
     * @param name representing the name of the power up
     */
    public Powerup(String id, String name){
        super(id, name);
        initialized=false;
    }

    /**
     * This method returns true if the power up is initialized
     * @return boolean as true if the power up is initialized
     */
    public boolean isInit(){
        return initialized;
    }

    /**
     * This method initializes the power up card
     * @param effect representing the effect of the power up
     * @param timing representing the time when the power up can be used
     * @param color representing the color of the power up card
     */
    public void init(Effect effect, TurnStatus timing, Color color){
        this.effect=effect;
        this.timing=timing;
        this.color=color;
        this.initialized=true;
    }

    /**
     * This method returns the effect of the power up
     * @return Effect representing the effect of the power up
     * @throws CardNotInitializedException when the card is not initialized
     */
    public Effect getEffect() throws CardNotInitializedException {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return effect;
    }

    /**
     * This method returns the color of the power up card
     * @return Color representing the color of the power up card
     * @throws CardNotInitializedException when the card is not initialized
     */
    public Color getColor()throws CardNotInitializedException{
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return color;
    }

    /**
     * This method returns the time when the power up can be used
     * @return TurnStatus representing the time when the power up can be used
     * @throws CardNotInitializedException when the card is not initialized
     */
    public TurnStatus getTiming()throws CardNotInitializedException {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return timing;
    }

}

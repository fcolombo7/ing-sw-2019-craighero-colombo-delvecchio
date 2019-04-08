package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardNotInitializedException;
import it.polimi.ingsw.exceptions.WeaponEffectException;
import it.polimi.ingsw.exceptions.WeaponLoadException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a weapon card
 */
public class Weapon extends Card{

    /**
     * This attribute contains a list of TreeNode that determines the tree of the possible using orders of the effects of a weapon
     */
    private List<TreeNode> effectOrder;

    /**
     * This attribute contains a list of color that determines the cost of a weapon
     */
    private List<Color> cost;

    /**
     * This attribute specifies if the weapon is initialized or not
     */
    private boolean initialized;

    /**
     * This attribute specifies the status of the weapon (loaded or unloaded)
     */
    private boolean loaded;

    /**
     * This attribute contains the list of the effects of the weapon
     */
    private List<Effect> effects;

    /**
     * This constructor instantiates a weapon calling the constructor of Card (Weapon extends Card)
     * @param id representing the id of the weapon
     * @param name representing the name of the weapon
     */
    public Weapon(String id, String name){
        super(id, name);
        initialized=false;
        loaded=false;
        this.effects=null;
        this.cost=null;
    }

    /**
     * This method returns true if the weapon is initialized
     * @return boolean as true if the weapon is initialized
     */
    public boolean isInit(){
        return initialized;
    }

    /**
     * This method initializes the weapon card
     * @param effects representing all the effects of the weapon
     * @param effectOrder representing possible using orders of the effects of the weapon
     * @param cost representing the cost of the weapon
     */
    public void init(List<Effect> effects, List<TreeNode> effectOrder, List<Color> cost){
        this.effects=new ArrayList<>(effects);
        this.effectOrder=new ArrayList<>(effectOrder);
        this.cost=new ArrayList<>(cost);
        initialized=true;
        loaded=true;
    }

    /**
     * This method returns the tree of the possible using orders of the effects of the weapon
     * @return List containing TreeNode representing the tree of the possible using orders of the effects of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     */
    public List<TreeNode> getOrder()throws CardNotInitializedException {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return effectOrder;
    }

    /**
     * This method returns an effect of the weapon
     * @param name representing  the name of the weapon
     * @return Effect representing an effect of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponEffectException when the weapon doesn't have that effect
     */
    public Effect getEffect(String name) throws WeaponEffectException, CardNotInitializedException {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        for(Effect e: effects)
            if(name.equals(e.getName()))
                return e;
        throw new WeaponEffectException ("The weapon does not have this effect.");

    }

    /**
     * This method returns all the effects of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return List containing Effect representing all the effects of the weapon
     */
    public List<Effect> getEffects() throws CardNotInitializedException{
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return effects;
    }

    /**
     * This method returns the cost of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return List containing Color representing the cost of the weapon
     */
    public List<Color> getCost() throws CardNotInitializedException{
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return cost;
    }

    /**
     * This method return the status of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return boolean as true if the weapon is loaded
     */
    public boolean isLoaded() throws CardNotInitializedException{
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        return loaded;
    }

    /**
     * This method loads the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponLoadException when the weapon is already loaded
     */
    public void load() throws WeaponLoadException, CardNotInitializedException{
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        if(isLoaded())
            throw new WeaponLoadException("The weapon is already loaded.");
        this.loaded=true;
    }

    /**
     * This method unloads the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponLoadException when the weapon is already unloaded
     */
    public void unload() throws WeaponLoadException, CardNotInitializedException {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        if(!isLoaded())
            throw new WeaponLoadException("The weapon is already unloaded.");
        this.loaded=false;
    }
}

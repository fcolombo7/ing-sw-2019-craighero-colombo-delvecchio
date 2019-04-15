package it.polimi.ingsw.model;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a single effect of a card (powerup or weapon)
 * */
public class Effect {
    /**
     * TODO
     */
    private static HashMap<String,Requirement> requirementsMap;

    /**
     * This attribute contains the effect name
     * */
    private String name;

    /**
     * This attribute contains the cost of the effect
     * */
    private List<Color> cost;

    /**
     * This attribute contains all the requirements of the effect
     * */
    private NodeList requirements;

    /**
     * This attribute contains all the actions of the effect which can be executed only if all the requirements are satisfied
     * */
    private List<Action> actions;

    /**
     * This attribute contains the target of the effect
     * */
    private Target target;

    /**
     * This constructor instantiates an Effect
     * @param name String representing the name of the effect to be instantiated
     * @param cost List<Color> representing the cost of the effect to be instantiated
     * @param requirements NodeList representing the preconditions of the effect to be instantiated
     * @param actions List<Action> representing all the actions of the effect to be instantiated
     * @param target Target of the effect to be instantiated
     */
    public Effect(String name, List<Color> cost, NodeList requirements, List<Action> actions, Target target) {
        this.name = name;
        this.cost = new ArrayList<>(cost);
        this.requirements = requirements;
        this.actions = new ArrayList<>(actions);
        this.target = target;
    }

    /**
     * This method returns the effect name
     * @return String representing the name of the effect
     * */
    public String getName() {
        return name;
    }

    /**
     * This method returns the cost of the effect
     * @return List<Color> representing the cost of the effect
     * */
    public List<Color> getCost() {
        return new ArrayList<>(cost);
    }

    /**
     * This method returns a meta-data tree node which contains all the requirements of the effect
     * @return NodeList representing all the requirements of the effect
     * */

    public NodeList getRequirements() {
        //FIXME: is this the right way of return the attributes? Does it preserve the immutability property?
        return requirements;
    }

    /**
     * This method returns all the actions performed by the current effect
     * @return List<Action> representing all the actions performed by the current effect
     * */
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    /**
     * This method returns the target of the effect
     * @return Target of the effect
     * */
    public Target getTarget() {
        return target;
    }

    /**
     * TODO
     */
    public static void initRequirements(){
        //TODO
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.ActionType;

/**
* This class represents a single action of an effect of a generic card (powerup or weapon)
* */
public class Action {

    /**
     * this attribute contains the ActionType
     * */
    private ActionType type;

    /**
     * this attribute contains the value of the action that is performed by the player
     * */
    private int value;

    /**
     * This constructor instantiates an Action
     * @param type ActionType of the Action to be instantiated
     * @param value int value of Action to be instantiated
     */
    public Action(ActionType type, int value){
        if(type==null)throw new IllegalArgumentException("Type value can't be null.");
        this.type=type;
        this.value=value;
    }

    /**
     * this method returns the ActionType of the Action
     * @return ActionType of the action
     * */
    public ActionType getActionType(){
        return type;
    }

    /**
     * this method returns the value of the Action
     * @return int representing the value of the action
     * */
    public int getValue(){
        return value;
    }
}

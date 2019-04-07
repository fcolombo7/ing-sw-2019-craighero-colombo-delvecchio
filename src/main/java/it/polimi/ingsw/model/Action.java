package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
* This class represents a single action of an effect of a generic card (powerup or weapon)
* */
public class Action {

    /**
     * this attribute contains the Action Type
     * */
    private ActionType type;

    /**
     * this attribute contains the value of the action that is performed by the player
     * */
    private int value;

    /**
     * this attribute contains all the additional conditions that must be evaluated after the execution of the main action
     * */
    private List<String[]> extra;

    /**
     * This constructur instantiates an Action
     * @param type ActionType of the Action to be instantiated
     * @param value int value of Action to be instantiated
     * @param extra List<String[]> of action representing all the additional conditions that must be evaluated after the execution of the main action
     */
    public Action(ActionType type, int value, List<String[]> extra){
        this.type=type;
        this.value=value;
        this.extra=new ArrayList<>(extra);
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

    /**
     * this method returns the additional conditions that must be evaluated after the execution of the main action
     * @return List<String> representing all the additional conditions that must be evaluated after the execution of the main action
     * */
    public List<String[]> getExtra(){
        return new ArrayList<>(extra);
    }

}

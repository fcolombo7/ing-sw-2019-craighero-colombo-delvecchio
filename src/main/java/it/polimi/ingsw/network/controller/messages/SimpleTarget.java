package it.polimi.ingsw.network.controller.messages;

import it.polimi.ingsw.model.Target;
import it.polimi.ingsw.model.enums.TargetType;

import java.io.Serializable;

public class SimpleTarget implements Serializable {

    private static final long serialVersionUID = -8413283539250971699L;

    private int minNumber;

    private int maxNumber;

    private int minPlayerIn;

    private int maxPlayerIn;

    private TargetType type;

    public SimpleTarget(Target target){
        this.maxNumber=target.getMaxNumber();
        this.minNumber=target.getMinNumber();
        this.minPlayerIn=target.getMinPlayerIn();
        this.maxPlayerIn=target.getMaxPlayerIn();
        this.type=target.getType();
    }

    public int getMinNumber() {
        return minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public int getMinPlayerIn() {
        return minPlayerIn;
    }

    public int getMaxPlayerIn() {
        return maxPlayerIn;
    }

    public TargetType getType(){
        return type;
    }
}

package it.polimi.ingsw.network.controller.messages;

import it.polimi.ingsw.model.Target;

import java.io.Serializable;

public class SimpleTarget implements Serializable {

    private static final long serialVersionUID = -8413283539250971699L;

    private int minNumber;

    private int maxNumber;

    private int minPlayerIn;

    private int maxPlayerIn;

    public SimpleTarget(Target target){
        this.maxNumber=target.getMaxNumber();
        this.minNumber=target.getMinNumber();
        this.minPlayerIn=target.getMinPlayerIn();
        this.maxPlayerIn=target.getMaxPlayerIn();
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
}

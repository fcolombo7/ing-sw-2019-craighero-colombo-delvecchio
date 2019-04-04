package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
* This class represent the target of the effect
 */
public class Target {
    /**
     * This attributes contains the target type of the effect
     */
    private TargetType type;

    /**
     * This attributes contains the minimum number of the target type of the effect
     */
    private int minNumber;

    /**
     * This attributes contains the maximum number of the target type of the effect
     */
    private int maxNumber;

    /**
     * This attributes contains all the preconditions of the selected target that must be satisfied in order to perform the effect actions on the selected target
     */
    private List<String> prevConstraints;

    /**
     * This constructur instantiates a Target
     * @param type TargetType of the Target to be instantiated
     * @param minNumber Minimum number of TargetType selected by the player
     * @param maxNumber Maximum number of TargetType selected by the player
     * @param prevConstraints List<String> that contains all the preconditions of the selected target that must be satisfied
     */
    public Target(TargetType type, int minNumber,int maxNumber, List<String> prevConstraints){
        this.type=type;
        this.maxNumber=maxNumber;
        this.minNumber=minNumber;
        this.prevConstraints=new ArrayList<>();
        this.prevConstraints.addAll(prevConstraints);
    }

    /**
     * this method returns the TargetType of the target
     * @return TargetType of the target
     * */
    public TargetType getType(){
        return type;
    }

    /**
     * this method returns the minimum target number
     * @return tnt representing the minimum target number
     * */
    public int getMinNumber(){
        return minNumber;
    }

    /**
     * this method returns the maximum target number
     * @return tnt representing the maximum target number
     * */
    public int getMaxNumber(){
        return maxNumber;
    }

    /**
     * this method returns all the preconditions of the target
     * @return List<String> representing all the preconditions of the target
     * */
    public List<String> getPrevConstraints() {
        return new ArrayList<>(prevConstraints);
    }

}

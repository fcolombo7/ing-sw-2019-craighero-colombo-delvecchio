package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TargetType;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
* This class represent the target of the effect
 */
public class Target {

    /**
     *
     */
    private static HashMap<String,Constraint> constraintHashMap=initConstraints();

    private static HashMap<String, Constraint> initConstraints() {
        HashMap<String,Constraint> constraintHashMap=new HashMap<>();
        constraintHashMap.put("DIFFERENT",(availablePlayer, shotPlayers) -> !shotPlayers.contains(availablePlayer));
        constraintHashMap.put("DIFFERENT_PREV",(availablePlayer, shotPlayers) -> !shotPlayers.isEmpty()&&shotPlayers.peek()!=availablePlayer);
        constraintHashMap.put("SAME_PREV",(availablePlayer, shotPlayers) -> !shotPlayers.isEmpty()&&shotPlayers.peek()==availablePlayer);
        constraintHashMap.put("SAME",(availablePlayer, shotPlayers) -> shotPlayers.contains(availablePlayer));
        return constraintHashMap;
    }

    public static boolean checkConstraint(String name, Player availablePlayer, Deque<Player> shotPlayers){
        return constraintHashMap.get(name).checkConstraint(availablePlayer,shotPlayers);
    }

    /**
     * This attribute contains the target type of the effect
     */
    private TargetType type;

    /**
     * This attribute contains the minimum number of the target type of the effect
     */
    private int minNumber;

    /**
     * This attribute contains the maximum number of the target type of the effect
     */
    private int maxNumber;

    /**
     * This attribute contains the minimum number of players the user can choose in the target
     */
    private int minPlayerIn;

    /**
     * This attribute contains the maximum number of players the user can choose in the target
     */
    private int maxPlayerIn;

    /**
     * This attribute contains all the preconditions of the selected target that must be satisfied in order to perform the effect actions on the selected target
     */
    private List<String> prevConstraints;

    /**
     * This constructur instantiates a Target
     * @param type TargetType of the Target to be instantiated
     * @param minNumber Minimum number of TargetType selected by the player
     * @param maxNumber Maximum number of TargetType selected by the player
     * @param prevConstraints List<String> that contains all the preconditions of the selected target that must be satisfied
     */
    public Target(TargetType type, int minNumber,int maxNumber, int minPlayerIn, int maxPlayerIn, List<String> prevConstraints){
        if(type==null) throw new NullPointerException("Target constructor must have a not null 'type' value.");
        if(prevConstraints==null) throw new NullPointerException("Target constructor must have a not null 'prevConstraints' value.");
        this.type=type;
        this.maxNumber=maxNumber;
        this.minNumber=minNumber;
        this.maxPlayerIn=maxPlayerIn;
        this.minPlayerIn=minPlayerIn;
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
     * this method returns the minimum number of players the user can choose in the target
     * @return tnt representing the minimum number of players the user can choose in the target
     */
    public int getMinPlayerIn() {
        return minPlayerIn;
    }

    /**
     * this method returns the maximum number of players the user can choose in the target
     * @return tnt representing the maximum number of players the user can choose in the target
     */
    public int getMaxPlayerIn() {
        return maxPlayerIn;
    }

    /**
     * this method returns all the preconditions of the target
     * @return List<String> representing all the preconditions of the target
     * */
    public List<String> getPrevConstraints() {
        return new ArrayList<>(prevConstraints);
    }

    /**
     * This method return the String representation of the Target object instantiated
     * @return String representing the Target object instantiated
     */
    @Override
    public String toString() {
        StringBuilder msg=new StringBuilder();
        msg.append("Target: {type: ").append(type.name()).append(", minNumber: ").append(minNumber).append(", maxNumber: ").append(maxNumber)
                .append(", minPlayerIn: ").append(minPlayerIn).append(", maxPlayerIn: ").append(maxPlayerIn).append(", prevConstraints: {");
        if(!prevConstraints.isEmpty())
        {
            for(int i=0;i<prevConstraints.size()-1;i++)
                msg.append(prevConstraints.get(i)).append(", ");
            msg.append(prevConstraints.size()-1).append("}");
        }else msg.append("none");
        msg.append("}");
        return msg.toString();
    }

    @FunctionalInterface
    private interface Constraint {
        boolean checkConstraint(Player availablePlayer, Deque<Player> shotPlayers);
    }
}

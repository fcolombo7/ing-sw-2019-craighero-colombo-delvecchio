package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
    private TargetType type;
    private int multiplicity;
    private List<String> prevConstraints;

    public Target(TargetType type, int multiplicity, List<String> prevConstraints){
        this.type=type;
        this.multiplicity=multiplicity;

        this.prevConstraints=new ArrayList<>();
        this.prevConstraints.addAll(prevConstraints);
    }

    public TargetType getType(){
        return type;
    }
    public int getMultiplicity(){
        return multiplicity;
    }
    public List<String> getPrevConstraints() {
        return prevConstraints;
    }

}

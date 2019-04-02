package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private ActionType type;
    private int value;
    private List<String[]> extra;

    public Action(ActionType type, int value, List<String[]> extra){
        this.type=type;
        this.value=value;
        this.extra.addAll(extra);
    }

    public ActionType getActionType(){
        return type;
    }

    public int getValue(){
        return value;
    }
    public List<String[]> getExtra(){
        List<String[]> temp=new ArrayList<>();
        temp.addAll(extra);
        return temp;
    }

}

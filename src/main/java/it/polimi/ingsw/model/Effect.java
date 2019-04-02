package it.polimi.ingsw.model;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Effect {
    String name;
    List<Color> cost;
    NodeList requirements;
    List<Action> actions;
    Target target;

    public Effect(String name, List<Color> cost, NodeList requirements, List<Action> actions, Target target) {
        this.name = name;
        this.cost = cost;
        this.requirements = requirements;
        this.actions = actions;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public List<Color> getCost() {
        List<Color> temp= new ArrayList<Color>();
        temp.addAll(cost);
        return temp;
    }

    public NodeList getRequirements() {
        return requirements;
    }

    public List<Action> getActions() {
        List<Action> temp= new ArrayList<Action>();
        temp.addAll(actions);
        return temp;
    }

    public Target getTarget() {
        return target;
    }
}

package it.polimi.ingsw.model;

import java.util.List;

public class Weapon extends Card{
    private TreeNode effectOrder;
    private List<Color> cost;
    private boolean initialized;
    private LoadStatus loaded;
    private List<Effect> effects;

    public Weapon(String id, String name){
        super(id, name);
    }

    public boolean isInit(){
        return initialized;
    }

    public void Init(List<Effect> effects, TreeNode effectOrder){}

    public TreeNode getOrder(){
        return effectOrder;
    }

    public Effect getEffect(String name){
        //todo
        return effects.get(0);
    }

    public List<Effect> getEffects(){
        return effects;
    }

    public LoadStatus isLoaded(){
        return loaded;
    }

    public void setLoaded(LoadStatus loaded){}
}

package it.polimi.ingsw.model;

public class Powerup extends Card {
    private TurnStatus timing;
    private Color color;
    private boolean initialized;
    private Effect effect;

    public Powerup(String id, String name){
        super(id, name);
    }

    public boolean isInit(){
        return initialized;
    }

    public void Init(Effect effect, TurnStatus timing, Color color){
        this.effect=effect;
        this.timing=timing;
        this.color=color;
    }

    public Effect getEffect(){
        return effect;
    }

    public String getColor(){
        return color();
    }

    public TurnStatus getTiming() {
        return timing;
    }

}

package it.polimi.ingsw.model;

public class AmmoTile {
    private String id;
    private Color[] ammos;
    private boolean powerup;

    public AmmoTile(Color a1, Color a2, Color a3, boolean powerup){}

    public String getId(){
        return id;
    }

    public Color[] getAmmos(){
        return ammos;
    }

    public boolean hasPowerup(){
        return powerup;
    }

}

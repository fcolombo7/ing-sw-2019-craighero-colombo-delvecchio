package it.polimi.ingsw.model;

public class AmmoSquare extends Square{
    private AmmoTile ammoTile;

    public AmmoSquare(Color color, boolean[] doors){
        super(color, doors);
    }

    public void setAmmoTile(AmmoTile ammoTile){}

    public AmmoTile getAmmoTile(){
        return ammoTile;
    }

    public AmmoTile popAmmoTile(){
        return ammoTile;
    }

}

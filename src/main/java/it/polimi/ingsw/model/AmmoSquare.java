package it.polimi.ingsw.model;

public class AmmoSquare extends Square{
    private AmmoTile ammoTile;

    public AmmoSquare(Color color, boolean[] doors){
        super(color, doors);
    }

    public void setAmmoTile(AmmoTile ammoTile){
        this.ammoTile=ammoTile;
    }

    //SERVE IL METODO GET?

    public AmmoTile popAmmoTile(){
        AmmoTile ret= new AmmoTile(ammoTile);
        ammoTile=null;
        return ret;
    }

}

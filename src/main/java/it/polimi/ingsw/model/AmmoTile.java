package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AmmoTileIndexErrorException;
import it.polimi.ingsw.exceptions.AmmoTileNotValidExceptoin;

public class AmmoTile {
    private String id;
    private Color[] ammos;
    private boolean powerup;

    public AmmoTile(Color a1, Color a2, Color a3, boolean powerup) throws AmmoTileNotValidExceptoin {
        if(a1==null||a2==null)
            throw  new AmmoTileNotValidExceptoin("AmmoTile can't have a1 or a2 as null.");
        if(a3==null&&powerup)
            throw  new AmmoTileNotValidExceptoin("AmmoTile can't have three ammos and powerup.");

        this.ammos=new Color[3];
        this.ammos[0]=a1;
        this.ammos[1]=a2;
        this.ammos[2]=a3;
        this.powerup=powerup;
    }

    public AmmoTile(AmmoTile ammoTile){
        this.id=ammoTile.id;
        this.powerup=ammoTile.powerup;
        this.ammos=new Color[3];
        this.ammos[0]=ammoTile.ammos[0];
        this.ammos[1]=ammoTile.ammos[1];
        this.ammos[2]=ammoTile.ammos[2];
    }


    public String getId(){
        return id;
    }

    public Color getAmmo(int index) throws AmmoTileIndexErrorException {
        if(index>=3)
            throw new AmmoTileIndexErrorException("Index out of bound");
        if(index==2&&powerup)
            throw  new AmmoTileIndexErrorException("Index out of bound (this tile has a powerup)");
        return ammos[index];
    }

    public boolean hasPowerup(){
        return powerup;
    }
}

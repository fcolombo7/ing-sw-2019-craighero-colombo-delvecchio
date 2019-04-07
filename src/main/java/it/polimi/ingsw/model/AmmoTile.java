package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AmmoTileIndexErrorException;
import it.polimi.ingsw.exceptions.AmmoTileNotValidExceptoin;

/**
 * This class represents an ammo tile
 * */
public class AmmoTile {

    /**
     * This attribute contains the tile id (in order to identify the associated resources)
     * */
    private String id;

    /**
     * This attribute contains the ammos
     * */
    private Color[] ammos;

    /**
     * This attribute specifies if the tile contains a powerup
     * */
    private boolean powerup;

    /**
     * This constructor instantiates an AmmoTile
     * @param a1 representing the first ammo
     * @param a2 representing the second ammo
     * @param a3 representing the third ammo
     * @param powerup representing the possibility of a tile of having a powerup
     * @throws AmmoTileNotValidExceptoin when parameters does not respect Adrenalina's rules
     */
    public AmmoTile(Color a1, Color a2, Color a3, boolean powerup) throws AmmoTileNotValidExceptoin {
        if(a1==null||a2==null)
            throw  new AmmoTileNotValidExceptoin("AmmoTile can't have a1 or a2 as null.");
        if(a3!=null&&powerup)
            throw  new AmmoTileNotValidExceptoin("AmmoTile can't have three ammos and powerup.");
        if(a3==null&&!powerup)
            throw  new AmmoTileNotValidExceptoin("AmmoTile can't have two ammos and no powerup.");

        this.ammos=new Color[3];
        this.ammos[0]=a1;
        this.ammos[1]=a2;
        this.ammos[2]=a3;
        this.powerup=powerup;
    }

    /**
     * This constructor creates a new file as a copy of another already created.
     * @param ammoTile representing the tile that is copied
     */
    public AmmoTile(AmmoTile ammoTile){
        this.id=ammoTile.id;
        this.powerup=ammoTile.powerup;
        this.ammos=new Color[3];
        this.ammos[0]=ammoTile.ammos[0];
        this.ammos[1]=ammoTile.ammos[1];
        this.ammos[2]=ammoTile.ammos[2];
    }

    /**
     * This method returns the tile id
     * @return String representing the id of the tile
     * */
    public String getId(){
        return id;
    }

    /**
     * This method returns an ammo of the tile
     * @param index int representing the position of the ammo in the rep
     * @return Color representing the ammo at index 'index'
     * */
    public Color getAmmo(int index) throws AmmoTileIndexErrorException {
        if(index>=3)
            throw new AmmoTileIndexErrorException("Index out of bound.");
        if(index==2&&powerup)
            throw  new AmmoTileIndexErrorException("Index out of bound (this tile has a powerup).");
        return ammos[index];
    }

    /**
     * This method returns true if the ammo has a powerup
     * @return Boolean as true if the ammo has a powerup
     * */
    public boolean hasPowerup(){
        return powerup;
    }
}

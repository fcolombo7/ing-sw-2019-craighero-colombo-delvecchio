package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.AmmoTileNotValidException;
import it.polimi.ingsw.model.enums.Color;

import java.io.Serializable;

/**
 * This class represents an ammo tile
 */
public class AmmoTile implements Serializable {

    private static final long serialVersionUID = 3619112453600975846L;
    /**
     * This attribute contains the tile id (in order to identify the associated resources)
     */
    private String id;

    /**
     * This attribute contains the ammo
     */
    private Color[] ammo;

    /**
     * This attribute specifies if the tile contains a powerup
     */
    private boolean powerup;

    /**
     * This constructor instantiates an AmmoTile
     * @param a1 representing the first ammo
     * @param a2 representing the second ammo
     * @param a3 representing the third ammo
     * @param powerup representing the possibility of a tile of having a powerup
     */
    public AmmoTile(Color a1, Color a2, Color a3, boolean powerup) {
        if(a1==null||a2==null)
            throw  new AmmoTileNotValidException("AmmoTile can't have a1 or a2 as null.");
        if(a3!=null&&powerup)
            throw  new AmmoTileNotValidException("AmmoTile can't have three ammo and powerup.");
        if(a3==null&&!powerup)
            throw  new AmmoTileNotValidException("AmmoTile can't have two ammo and no powerup.");

        this.ammo=new Color[3];
        this.ammo[0]=a1;
        this.ammo[1]=a2;
        this.ammo[2]=a3;
        this.powerup=powerup;
    }

    /**
     * This constructor creates a new file as a copy of another already created.
     * @param ammoTile representing the tile that is copied
     */
    public AmmoTile(AmmoTile ammoTile){
        this.id=ammoTile.id;
        this.powerup=ammoTile.powerup;
        this.ammo=new Color[3];
        this.ammo[0]=ammoTile.ammo[0];
        this.ammo[1]=ammoTile.ammo[1];
        this.ammo[2]=ammoTile.ammo[2];
    }

    /**
     * This method returns the tile id
     * @return String representing the id of the tile
     * */
    public String getId(){
        return id;
    }

    /**
     * This method set the AmmoTile id
     * @param id representing the Id of the AmmoTile
     */
    public void setId(String id){ this.id=id;}
    /**
     * This method returns an ammo of the tile
     * @param index int representing the position of the ammo in the rep
     * @return Color representing the ammo at index 'index'
     * */
    public Color getAmmo(int index) {
        if(index>=3)
            throw new IndexOutOfBoundsException("Index out of bound.");
        if(index==2&&powerup)
            throw  new IndexOutOfBoundsException("Index out of bound (this tile has a powerup).");
        return ammo[index];
    }

    /**
     * This method returns true if the ammo has a powerup
     * @return Boolean as true if the ammo has a powerup
     * */
    public boolean hasPowerup(){
        return powerup;
    }

    /**
     * This method return a String representation of the instantiated ammo
     * @return String representing the instantiated ammo
     * */
    @Override
    public String toString(){
        return "AmmoTile{a1:"+ ammo[0].name()+", a2:"+ ammo[1].name()+", a3:"+ (ammo[2]!=null?ammo[2].name():"null")+", powerup:"+powerup+"}";
    }
}

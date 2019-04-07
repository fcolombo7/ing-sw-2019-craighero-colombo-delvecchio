package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullSquareException;

/**
 * This class represents a Square that contains an AmmoTile
 */
public class AmmoSquare extends Square{
    /**
     * This attribute contains the ammoTile
     */
    private AmmoTile ammoTile;

    /**
     * This constructor instantiates an AmmoSquare
     * @param roomColor RoomColor representing the color of the room
     * @param doors  boolean[] representing the doors connected to the AmmoSquare
     */
    public AmmoSquare(RoomColor roomColor, boolean[] doors){
        super(roomColor, doors);
    }

    /**
     * This method return a true value if the user can grab the AmmoTile object from the square
     * @return boolean representing the possibility of the user to grab the AmmoTile object in the square
     */
    @Override
    public boolean canGrab() {
        return (ammoTile!=null);
    }

    /**
     * This method return a true value if the AmmoSquare is full and no other AmmoTile objects can be put on the square.
     * @return boolean representing if the AmmoSquare is full and no other AmmoTile objects can be put on the square.
     */
    @Override
    public boolean isFull(){
        return (ammoTile!=null);
    }

    /**
     * This method set the AmmoTile object the user can grab in the square
     * @param ammoTile AmmoTile representing the ammo tile
     */
    public void setAmmoTile(AmmoTile ammoTile) {
        if(this.ammoTile!=null) throw new FullSquareException("The AmmoSquare already contains an AmmoTile.");
        this.ammoTile=ammoTile;
    }

    /**
     * This method remove the AmmoTile from the square
     * @return ammoTile AmmoTile representing the ammo tile in the square
     */
    public AmmoTile popAmmoTile(){
        AmmoTile ret= new AmmoTile(ammoTile);
        ammoTile=null;
        return ret;
    }
}

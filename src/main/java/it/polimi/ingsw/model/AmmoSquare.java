package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.SquareContentException;

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
    public AmmoSquare(RoomColor roomColor, boolean[] doors, int[] indexes){
        super(roomColor, doors, indexes);
    }

    public AmmoSquare(AmmoSquare other){
        super(other.getRoomColor(),new boolean[]{other.hasDoor(Direction.NORTH),other.hasDoor(Direction.EAST),other.hasDoor(Direction.SOUTH),other.hasDoor(Direction.WEST)},other.getBoardIndexes());
        if(other.ammoTile!=null) this.ammoTile=new AmmoTile(other.ammoTile);
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
        if(this.ammoTile!=null) throw new SquareContentException("The AmmoSquare already contains an AmmoTile.");
        this.ammoTile=ammoTile;
    }

    /**
     * This method remove the AmmoTile from the square
     * @return AmmoTile representing the ammo tile removed from the square
     */
    public AmmoTile popAmmoTile(){
        if(this.ammoTile==null) throw new SquareContentException("The AmmoSquare doesn't contain an AmmoTile.");
        AmmoTile ret= new AmmoTile(ammoTile);
        ammoTile=null;
        return ret;
    }

    /**
     * This method return a String representation of the instantiated AmmoSquare
     * @return String representing the instantiated AmmoSquare
     * */
    @Override
    public String toString(){
        StringBuilder msg=new StringBuilder();
        msg.append("AmmoSquare ").append(super.toString());
        msg.append("Ammo: ");
        if(ammoTile==null) msg.append("null\n");
        else msg.append(ammoTile.toString()).append("\n");
        msg.append("}");
        return msg.toString();
    }
}

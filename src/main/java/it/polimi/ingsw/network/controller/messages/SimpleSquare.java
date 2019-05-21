package it.polimi.ingsw.network.controller.messages;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Direction;
import it.polimi.ingsw.model.enums.RoomColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleSquare implements Serializable {
    private static final long serialVersionUID = 2170818474220340919L;
    private RoomColor roomColor;
    private boolean[] doors;
    private boolean spawnPoint;
    private int xIndex;
    private int yIndex;
    private AmmoTile ammoTile;
    private List<Card> weaponCards;

    public SimpleSquare(int xIndex, int yIndex, RoomColor roomColor, boolean[] doors, boolean spawnPoint){
        this.xIndex=xIndex;
        this.yIndex=yIndex;
        this.spawnPoint=spawnPoint;
        ammoTile=null;
        weaponCards=new ArrayList<>();
        this.roomColor=roomColor;
        this.doors=new boolean[doors.length];
        System.arraycopy(doors,0,this.doors,0, doors.length);
    }

    public boolean setAmmoTile(AmmoTile ammoTile) {
        if(spawnPoint) return false;
        this.ammoTile = ammoTile;
        return true;
    }

    public boolean addWeaponCard(Card weaponCard){
        if(weaponCards.size()>=3) return false;
        for(Card c:weaponCards){
            if(c.getId().equalsIgnoreCase(weaponCard.getId()))
                return false;
        }
        weaponCards.add(weaponCard);
        return true;
    }

    /**
     * This method return a true value if there is a door connected to the square in the given direction.
     * @param direction Direction representing the position of the door you want to evaluate.
     * @return Boolean representing the presence of the door in the given direction.
     */
    public boolean hasDoor(Direction direction) {
        if(direction==null) throw new NullPointerException("Param 'direction' is null.");
        return doors[direction.ordinal()];
    }

    /**
     * This method return the x-index and y-index of this in the GameBoard map
     * @return RoomColor representing the color of the room
     */
    public int[] getBoardIndexes(){
        return new int[]{xIndex,yIndex};
    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }

    public List<Card> getWeaponCards() {
        return weaponCards;
    }

    public RoomColor getRoomColor() {
        return roomColor;
    }

    public boolean[] getDoors() {
        return doors;
    }

    public boolean isSpawnPoint() {
        return spawnPoint;
    }
}
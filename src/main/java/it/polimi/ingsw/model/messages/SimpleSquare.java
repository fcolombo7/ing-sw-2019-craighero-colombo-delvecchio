package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.RoomColor;

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

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
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
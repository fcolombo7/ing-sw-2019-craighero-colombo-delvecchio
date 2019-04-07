package it.polimi.ingsw.model;

import java.util.List;

public class WeaponSquare extends Square {
    private List<Weapon> weapons;

    public WeaponSquare(RoomColor roomColor, boolean[] doors){
        super(roomColor, doors);
    }

    @Override
    public boolean canGrab() {
        //todo
        return false;
    }

    @Override
    public boolean isFull() {
        //todo
        return false;
    }

    public void setWeapons(List<Weapon> weapons){}

    public void addWeapon(Weapon weapon){}

    public List<Weapon> getWeapons(){
        return weapons;
    }

    public Weapon popWeapon(Weapon weapon){
        return weapon;
    }
}

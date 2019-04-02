package it.polimi.ingsw.model;

import java.util.List;

public class WeaponSquare extends Square {
    private List<Weapon> weapons;

    public WeaponSquare(Color color, boolean[] doors){
        super(color, doors);
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

package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.SquareContentException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Square that contains Weapons (it is a Spawn Point in the board)
 */
public class WeaponSquare extends Square {
    /**
     * This attribute contains all the weapon contained by the WeaponSquare
     */
    private List<Weapon> weapons;

    /**
     * This constructor instantiates a WeaponSquare
     * @param roomColor RoomColor representing the color of the room
     * @param doors  boolean[] representing the doors connected to the WeaponSquare
     */
    public WeaponSquare(RoomColor roomColor, boolean[] doors, int[] indexes){
        super(roomColor, doors, indexes);
        weapons=new ArrayList<>();
    }

    /**
     * This method return a true value if the user can grab a Weapon object from the square
     * @return boolean representing the possibility of the user to grab a Weapon object from the square
     */
    @Override
    public boolean canGrab() {
        return !weapons.isEmpty();
    }

    /**
     * This method return a true value if the WeaponSquare is full and no other Weapon objects can be put on the square.
     * @return boolean representing if the WeaponSquare is full and no other Weapon objects can be put on the square.
     */
    @Override
    public boolean isFull() {
        return weapons.size()==3;
    }

    /**
     * This method add a weapon to the WeaponSquare
     * @param weapon representing the weapon you want to add to the AmmoSquare.
     */
    public void addWeapon(Weapon weapon){
        if(weapons.size()==3) throw  new SquareContentException("The WeaponSquare already contains three weapons.");
        for (Weapon w: weapons) {
            if(w.getId().equals(weapon.getId()))
                throw new SquareContentException("Weapon is yet contained in the WeaponSquare.");
        }
        weapons.add(weapon);
    }

    /**
     * This method return all the weapons contained by the WeaponSquare
     * @return List<Weapon> representing all the weapons contained by the WeaponSquare
     */
    public List<Weapon> getWeapons(){
        return new ArrayList<>(weapons);
    }

    /**
     * This method remove the weapon at index 'index' from the square
     * @return Weapon representing the removed weapon
     */
    public Weapon popWeapon(int index){
        if(weapons.isEmpty()) throw new SquareContentException("The WeaponSquare doesn't contain any weapon.");
        if(!(index>=0&&index<weapons.size())) throw new IllegalArgumentException("Invalid index.");
        Weapon weapon= weapons.get(index);
        weapons.remove(index);
        return weapon;
    }

    /**
     * This method return a String representation of the instantiated WeaponSquare
     * @return String representing the instantiated WeaponSquare
     * */
    @Override
    public String toString(){
        StringBuilder msg=new StringBuilder();
        msg.append("WeaponSquare ").append(super.toString());
        msg.append("Weapons: ");
        if(weapons.isEmpty()) msg.append("empty\n");
        else{
            for (Weapon weapon:weapons) {
                msg.append("\n").append(((Card)weapon).toString());
            }
        }
        msg.append("\n}");
        return msg.toString();
    }
}

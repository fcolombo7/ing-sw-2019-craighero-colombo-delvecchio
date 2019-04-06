package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.PlayerException.PlayerPowerupsException;
import it.polimi.ingsw.exceptions.PlayerException.PlayerWeaponsException;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.PlayerStatus.*;

/**
 * This class represent a player
 * */
public class Player {

    /**
     * This attribute contains the nickname chosen by the player
     * */
    private String nickname;

    /**
     * This attribute contains the motto chosen by the player
     * */
    private String motto;

    /**
     * This attribute contains true if the player has played the very first turn of the game
     * */
    private boolean first;

    /**
     * This attribute contains the updated score of the player
     * */
    private int score;

    /**
     * This attribute contains the status of the player
     * */
    private PlayerStatus status;

    /**
     * This attribute contains the list of weapons owned by the player
     * */
    private List<Weapon> weapons;

    /**
     * This attribute contains the list of powerups owned by the player
     * */
    private List<Powerup> powerups;

    /**
     * This attribute contains the square where the player is located
     * */
    private Square position;

    /**
     * This attribute contains the reference to the player board
     * */
    private PlayerBoard board;

    /**
     * This constructor initializes a player
     * @param nickname representing chosen nickname
     * @param motto representing chosen motto
     * @param first representing if the player plays the very first turn
     */
    public Player(String nickname, String motto, boolean first){
        this.nickname=nickname;
        this.motto=motto;
        this.first=first;
        this.score=0;
        this.status=FIRST_SPAWN;
        this.weapons=new ArrayList<>(3);
        this.powerups=new ArrayList<>(3);
        this.position=null;
        this.board=new PlayerBoard();
    }

    /**
     * This method return the player nickname
     * @return String representing the player nickname
     * */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method return the player motto
     * @return String representing the player motto
     * */
    public String getMotto() {
        return motto;
    }

    /**
     * This method return the player score
     * @return int representing the player score
     * */
    public int getScore() {
        return score;
    }

    /**
     * This method return the player status
     * @return PlayerStatus representing the player status
     * */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * This method return the player weapons
     * @return List containing Weapon representing the player weapons
     * */
    public List<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * This method return the player powerups
     * @return List containing Poweruo representing the player powerups
     * */
    public List<Powerup> getPowerups() {
        return powerups;
    }

    /**
     * This method return the player position
     * @return Square representing the player position
     * */
    public Square getPosition() {
        return position;
    }

    /**
     * This method return the player board
     * @return PlayerBoard representing the player board
     * */
    public PlayerBoard getBoard() {
        return board;
    }

    /**
     * This method return true if the player is the very first playing a turn
     * @return boolean representing if the player is the very first playing a turn
     * */
    public boolean isFirst() {
        return first;
    }

    /**
     * This method set the player status
     * @param status representing the player status
     * */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    /**
     * This method set the player position
     * @param position representing the player position
     * */
    public void setPosition(Square position) {
        this.position = position;
    }

    /**
     * This method add a weapon to the player
     * @param weapon representing the weapon to add
     * @throws PlayerWeaponsException when player already has 3 weapons
     * */
    public void addWeapon(Weapon weapon)throws PlayerWeaponsException {
        if(weapons.size()>2)
            throw new PlayerWeaponsException("Player can't have more than 3 weapons");
        else weapons.add(weapon);
    }

    /**
     * This method pop a weapon from the player weapons
     * @param card representing the card that need to be popped
     * @return Weapon representing the weapon just popped
     * @throws PlayerWeaponsException when player does not have any weapons to pop
     * */
    public Weapon popWeapon(Card card)throws PlayerWeaponsException{
        if(weapons.isEmpty()){
            throw new PlayerWeaponsException("Player does not have any weapon");
            }
        else{ Weapon weapon;
            for(Weapon w: weapons)
                if(card.getId().equals(w.getId())){
                    weapon=w;
                    weapons.remove(w);
                    return weapon;
                }
            throw new PlayerWeaponsException("Player does not own the weapon");
            }
    }

    /**
     * This method add a powerup to the player
     * @param powerup representing the powerup to add
     * @throws PlayerPowerupsException when player already has 3 powerups
     */
    public void addPowerup(Powerup powerup)throws PlayerPowerupsException {
        if(powerups.size()>2)
            throw new PlayerPowerupsException("Player can't have more than 3 powerups");
        else powerups.add(powerup);
    }

    /**
     * This method pop a powerup from the player powerups
     * @param card representing the card that need to be popped
     * @return Powerup representing the powerup just popped
     * @throws PlayerPowerupsException when player does not have any powerup to pop
     */
    public Powerup popPowerup(Card card)throws PlayerPowerupsException{
        if(powerups.isEmpty()){
            throw new PlayerPowerupsException("Player does not have any powerup");
        }
        else{ Powerup powerup;
            for(Powerup p: powerups)
                if(card.getId().equals(p.getId())){
                    powerup=p;
                    powerups.remove(p);
                    return powerup;
                }
            throw new PlayerPowerupsException("Player does not own the powerup");
        }
    }

    /**
     * This method update the player score
     * @param points representing the points to be added to player score
     */
    public void updateScore(int points){
        if(points<0)
            throw new IllegalArgumentException("Score can't decrease");
        else {
            score += points;
        }
    }

}

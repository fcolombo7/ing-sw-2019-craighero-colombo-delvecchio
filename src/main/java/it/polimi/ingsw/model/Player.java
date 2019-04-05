package it.polimi.ingsw.model;

import java.util.List;

public class Player {
    private String nickname;
    private String motto;
    private boolean first;
    private int score;
    private PlayerStatus status;
    private List<Weapon> weapons;
    private List<Powerup> powerups;
    private Square position;
    private PlayerBoard board;

    public Player(String nickname, String motto, boolean first){
        this.nickname=nickname;
        this.motto=motto;
        this.first=first;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMotto() {
        return motto;
    }

    public int getScore() {
        return score;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Powerup> getPowerups() {
        return powerups;
    }

    public Square getPosition() {
        return position;
    }

    public PlayerBoard getBoard() {
        return board;
    }

    public void isFirst(boolean first) {
        this.first = first;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void addWeapon(Weapon weapon){}

    public Weapon popWeapon(Weapon weapon){
        return weapon;
    }

    public void addPowerup(Powerup powerup){}

    public Powerup popPowerup(Powerup powerup){
        return powerup;
    }

    public void updateScore(int points){}

}

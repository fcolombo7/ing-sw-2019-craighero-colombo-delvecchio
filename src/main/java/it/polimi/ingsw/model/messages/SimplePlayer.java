package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimplePlayer implements Serializable {

    private static final long serialVersionUID = 4126654133775199977L;

    private String nickname;

    private String motto;

    private boolean first;

    private int score;

    private PlayerStatus status;

    private List<Card> weaponCards;

    private List<String> notLoadedIds;

    private List<Card> powerupCards;

    private int[] position;

    private List<String> damages;

    private List<String> marks;

    private List<Color> ammos;

    private int deathCounter;

    private boolean switched;


    public SimplePlayer(Player player){
        nickname=player.getNickname();
        motto=player.getMotto();
        first=player.isFirst();
        score=player.getScore();
        status=player.getStatus();
        Square sPosition=player.getPosition();
        position=sPosition==null?null:sPosition.getBoardIndexes();
        deathCounter=player.getBoard().getDeathCounter();
        switched=player.getBoard().isSwitched();
        ammos=player.getBoard().getAmmos();
        //weapon
        weaponCards=new ArrayList<>();
        notLoadedIds=new ArrayList<>();
        for(Weapon weapon:player.getWeapons()) {
            weaponCards.add(new Card(weapon));
            if(!weapon.isLoaded()) notLoadedIds.add(weapon.getId());
        }
        //powerup
        powerupCards=new ArrayList<>();
        for(Powerup powerup:player.getPowerups())
            weaponCards.add(new Card(powerup));
        //damages
        damages=new ArrayList<>();
        for (Player p:player.getBoard().getHealthBar()) damages.add(p.getNickname());
        //marks
        marks=new ArrayList<>();
        for (Player p:player.getBoard().getMarks()) marks.add(p.getNickname());
    }

    public String getNickname() {
        return nickname;
    }

    public String getMotto() {
        return motto;
    }

    public boolean isFirst() {
        return first;
    }

    public int getScore() {
        return score;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public List<Card> getWeaponCards() {
        return weaponCards;
    }

    public List<String> getNotLoadedIds() {
        return notLoadedIds;
    }

    public List<Card> getPowerupCards() {
        return powerupCards;
    }

    public int[] getPosition() {
        return position;
    }

    public List<String> getDamages() {
        return damages;
    }

    public List<String> getMarks() {
        return marks;
    }

    public List<Color> getAmmos() {
        return ammos;
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public boolean isSwitched() {
        return switched;
    }
}
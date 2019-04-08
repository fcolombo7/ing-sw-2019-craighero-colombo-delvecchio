package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.*;

import static it.polimi.ingsw.model.Color.*;

public class PlayerBoard {
    /*
    private Player[] damages;
    private int damageIndex;
    private List<Color> ammos;
    private int deathCounter;
    private boolean frenzyMode;
    private List<Player> marks;

    public PlayerBoard(){
        this.ammos=new ArrayList<>();
        ammos.add(RED);
        ammos.add(YELLOW);
        ammos.add(BLUE);
        deathCounter=0;
        frenzyMode=false;
        this.damages=new Player[12];
        this.marks=new ArrayList<>();
    }

    public Player[] getDamages() {
        return damages;
    }

    public int getDamageIndex() {
        return damageIndex;
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public List<Player> getMarks() {
        return new ArrayList(marks);
    }

    public List<Color> getAmmos(){
        return new ArrayList(ammos);
    }

    public void setDamageIndex(int damageIndex) {
        this.damageIndex = damageIndex;
    }

    public void addAmmos(List<Color> ammos){
        for(Color c: ammos)
            if (Collections.frequency(this.ammos, c) < 3)
                this.ammos.add(c);
    }

    public void incDeathCounter(){
        deathCounter++;
    }

    public void removeAmmos(List<Color> ammos){
        for(Color c: ammos)
            this.ammos.remove(c);
    }

    public void enableFrenzy(){
        this.frenzyMode=true;
    }

    public boolean isFrenzy(){
        return frenzyMode;
    }

    public void addDamage(Player player, int count){
        int i;
        for(i=0; i<count && damageIndex+i<13; i++)
            damages[damageIndex+i]=player;
        setDamageIndex(damageIndex+i);
    }

    public List<Player> getDamage(){
        Map<Player, Long> counters = Arrays.stream(damages).collect(Collectors.groupingBy((p -> p), Collectors.counting()));
        Map<Player, Long> sorted = new TreeMap<>(counters);
        return new ArrayList<>(sorted.keySet());
    }

    public void removeMark(Player player){
        //todo
    }

    public int getPlayerMark(Player player){
        //todo
    }

    public void clearDamage(){
        //todo
    }
    */
}

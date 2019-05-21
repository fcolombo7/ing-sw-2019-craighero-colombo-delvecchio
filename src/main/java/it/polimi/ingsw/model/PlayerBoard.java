package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;

import java.util.*;
import java.util.stream.*;

import static it.polimi.ingsw.model.enums.Color.*;

/**
 * This class represents a player board
 */
public class PlayerBoard {

    /**
     * This attribute contains the damage landed on the player board
     */
    private List<Player> damages;

    /**
     * This attribute contains the ammo owned by the player linked to the player board
     */
    private List<Color> ammo;

    /**
     * This attribute contains the number of deaths of the player linked to the player board
     */
    private int deathCounter;

    /**
     * This attribute contains which face of the player board to consider during the game
     */
    private boolean switched;

    /**
     * This attribute contains the marks landed on the player board
     */
    private List<Player> marks;

    /**
     * This constructor initializes the player board
     */
    public PlayerBoard(){
        this.ammo =new ArrayList<>();
        ammo.add(RED);
        ammo.add(YELLOW);
        ammo.add(BLUE);
        deathCounter=0;
        switched=false;
        this.damages=new ArrayList<>(12);
        this.marks=new ArrayList<>();
    }

    /**
     * This method returns the list of player that has dealt damages
     * @return List containing Player representing dealt damages to the player linked to the player board
     */
    public List<Player> getHealthBar() {
        return new ArrayList<>(damages);
    }

    /**
     * This method returns the number of deaths
     * @return int representing the number of deaths of the player linked to the player board
     */
    public int getDeathCounter() {
        return deathCounter;
    }

    /**
     * This method returns the list of marks
     * @return List containing Player representing the marks landed on the player board
     */
    public List<Player> getMarks() {
        return new ArrayList<>(marks);
    }

    /**
     * This method returns the ammo owned by the player linked to the player board
     * @return List containing Color representing the ammo owned by the player linked to the player board
     */
    public List<Color> getAmmo(){
        return new ArrayList<>(ammo);
    }

    /**
     * This method returns the killer
     * @return Player representing the killer
     */
    public Player getKiller(){
        if(damages.get(10) == null)
            throw new IndexOutOfBoundsException("The player has not been killed yet");
        return damages.get(10);
    }

    /**
     * This method returns the overkiller
     * @return Player representing the overkiller
     */
    public Player getOverKiller(){
        return damages.get(11);
    }

    /**
     * This method adds ammo to the player board
     * @param ammos representing the ammo to add
     */
    public void addAmmos(List<Color> ammos){
        for(Color c: ammos)
            if (Collections.frequency(this.ammo, c) < 3)
                this.ammo.add(c);
    }

    /**
     * This method increase the number of deaths of the player linked to the player board
     */
    public void incDeathCounter(){
        deathCounter++;
    }

    /**
     * This method removes ammo to the player board
     * @param color representing the ammo to remove
     */
    public void removeAmmo(Color color){
        this.ammo.remove(color);
    }

    /**
     * This method changes the face of the player board to the frenzy mode face
     */
    public void switchBoard(){
        this.switched=true;
    }

    /**
     * This method returns which face of the player board is on
     * @return boolean representing if the frenzy mode face of the player board is on
     */
    public boolean isSwitched(){
        return switched;
    }

    /**
     * This method adds damage to the player board
     * @param player represents the player who has dealt damage
     * @param count represents how much damages have been dealt
     */
    public void addDamage(Player player, int count){
        int i;
        for(i=0; i<count && damages.size()<12; i++)
            damages.add(player);
    }

    /**
     * This method adds marks to the player board
     * @param player represents the player who has given marks
     * @param count represents how many marks have been given
     */
    public void addMarks(Player player, int count){
        int i;
        for(i=0; i<count; i++)
            marks.add(player);
    }

    /**
     * This method returns a list of player sorted by dealt damages in descending order (in case of equals sum of dealt damages, the one who hit first appears first)
     * @return List containing Player representing the ranking of dealt damages
     */
    public List<Player> getDamage(){
        Map<Player, Integer> orderDamage = new HashMap<>();
        Integer i = 0;
        for (Player p: damages) {
            if(!orderDamage.containsKey(p)){
                orderDamage.put(p, i);
                i++;
            }
        }

        Map<Player, Long> counterDamage = damages.stream().collect(Collectors.groupingBy((p -> p), Collectors.counting()));
        TieBreakComparator tbc = new TieBreakComparator(counterDamage, orderDamage);
        TreeMap<Player, Long> sortedMap = new TreeMap<>(tbc);

        sortedMap.putAll(counterDamage);

        return new ArrayList<>(sortedMap.keySet());
    }

    /**
     *This private class is a player comparator, it compares them basing on two different maps
     */
    private class TieBreakComparator implements Comparator<Player>{

        /**
         * This attribute contains the map primary used for comparing
         */
        Map<Player, Long> base;

        /**
         * This attribute contains the map used to break the tie in case of equal value in base map
         */
        Map<Player, Integer> order;

        /**
         * This constructor initializes the maps of the comparator
         * @param base representing the base map for comparing player
         * @param order representing the map that breaks the tie
         */
        TieBreakComparator(Map<Player, Long> base, Map<Player, Integer> order){
            this.base=base;
            this.order=order;
        }

        /**
         * This method overrides the compare method of Comparator interface
         * @param a representing the first player to compare
         * @param b representing the second player to compare
         * @return int representing if parameter a is bigger/smaller than or equal to parameter b
         */
        @Override
        public int compare(Player a, Player b){
            if(base.get(a) < base.get(b))
                return 1;
            else if(base.get(a) > base.get(b))
                    return -1;
            else if(base.get(a).equals(base.get(b))){
                    if(order.get(a) < order.get(b))
                        return -1;
                    else return 1;
            }
            else return 0;
        }
    }

    /**
     * This method removes all the marks landed by the passed player
     * @param player representing the player whose marks have to be removed
     */
    public void removeMark(Player player){
        for (Player p: marks) {
            if(p.equals(player))
                marks.remove(p);
        }
    }

    /**
     * This method return the number of marks landed by the passed player
     * @param player representing the player whose marks have to be counted
     * @return int representing the amount of marks landed by the passed player
     */
    public int getPlayerMark(Player player){
        return Collections.frequency(marks, player);
    }

    /**
     * This method reset the damage array
     */
    public void clearDamage(){
        this.damages.clear();
    }


    public PlayerBoard(PlayerBoard pBoard) {
        ammo =new ArrayList<>(pBoard.ammo);
        damages=new ArrayList<>(pBoard.damages);
        marks=new ArrayList<>(pBoard.marks);
        deathCounter=pBoard.deathCounter;
        switched=pBoard.switched;
    }
}

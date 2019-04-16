package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerBoard;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TestPlayerBoard {

    /**
     * Testing correct ranking of player dealt damages (less than 12 damages)
     */
    @Test
    public void firstGetDamageTest(){
        PlayerBoard pb = new PlayerBoard();
        Player p1 = new Player("a", "aaa", true);
        Player p2 = new Player("b", "bbb", false);
        Player p4 = new Player("d", "ddd", false);
        pb.addDamage(p2, 1);
        pb.addDamage(p1, 1);
        pb.addDamage(p4, 3);

        List<Player> isList = new ArrayList<>();
        isList.add(p4);
        isList.add(p2);
        isList.add(p1);

        assertThat(pb.getDamage(), is(isList));
    }

    /**
     * Testing correct ranking of player dealt damages (all players with equal dealt damages)
     */
    @Test
    public void secondGetDamageTest(){
        PlayerBoard pb = new PlayerBoard();
        Player p1 = new Player("a", "aaa", true);
        Player p2 = new Player("b", "bbb", false);
        Player p3 = new Player("c", "ccc", false);
        Player p4 = new Player("d", "ddd", false);
        pb.addDamage(p2, 2);
        pb.addDamage(p1, 1);
        pb.addDamage(p4, 3);
        pb.addDamage(p1, 2);
        pb.addDamage(p2, 1);
        pb.addDamage(p3, 3);

        List<Player> isList = new ArrayList<>();
        isList.add(p2);
        isList.add(p1);
        isList.add(p4);
        isList.add(p3);

        assertThat(pb.getDamage(), is(isList));
    }

    /**
     * Testing correct ranking of player dealt damages (equal amount of landed damages despite a player dealt more than the other)
     */
    @Test
    public void thirdGetDamageTest(){
        PlayerBoard pb = new PlayerBoard();
        Player p1 = new Player("a", "aaa", true);
        Player p2 = new Player("b", "bbb", false);
        Player p3 = new Player("c", "ccc", false);
        Player p4 = new Player("d", "ddd", false);
        pb.addDamage(p2, 2);
        pb.addDamage(p1, 3);
        pb.addDamage(p4, 4);
        pb.addDamage(p3, 6);

        List<Player> isList = new ArrayList<>();
        isList.add(p4);
        isList.add(p1);
        isList.add(p3);
        isList.add(p2);

        assertThat(pb.getDamage(), is(isList));
    }

    /**
     * Testing that can not be landed more than 12 damages
     */
    @Test
    public void addingMoreThan12DamagesTest(){
        PlayerBoard pb = new PlayerBoard();
        Player p1 = new Player("a", "aaa", true);
        Player p2 = new Player("b", "bbb", false);
        pb.addDamage(p2, 25);
        pb.addDamage(p1, 2);

        List<Player> isList = new ArrayList<>();
        for(int i=0; i<12; i++)
            isList.add(p2);

        assertThat(pb.getDamages(), is(isList));
    }

    /**
     * Testing that can not be added more than 3 ammo of the same color
     */
    @Test
    public void addingFourEqualColorAmmoTest(){
        PlayerBoard pb = new PlayerBoard();
        List<Color> ammo = new ArrayList<>();
        List<Color> ammoRes = new ArrayList<>();

        ammo.add(Color.RED);
        ammo.add(Color.RED);
        ammo.add(Color.YELLOW);

        pb.addAmmos(ammo);
        pb.addAmmos(ammo);

        ammoRes.add(Color.RED);
        ammoRes.add(Color.YELLOW);
        ammoRes.add(Color.BLUE);
        ammoRes.add(Color.RED);
        ammoRes.add(Color.RED);
        ammoRes.add(Color.YELLOW);
        ammoRes.add(Color.YELLOW);

        assertThat(pb.getAmmos(), is(ammoRes));
    }

    /**
     * Testing that removing not existing mark will not modify marks list
     */
    @Test
    public void removeMissingMarkTest(){
        PlayerBoard pb =new PlayerBoard();
        Player p1 = new Player("a", "aaa", true);
        Player p2 = new Player("b", "bbb", false);

        pb.addMarks(p1, 2);
        pb.addMarks(p2, 3);

        pb.removeMark(new Player("c", "ccc", false));

        List<Player> isList = new ArrayList<>();
        isList.add(p1);
        isList.add(p1);
        isList.add(p2);
        isList.add(p2);
        isList.add(p2);

        assertThat(pb.getMarks(), is(isList));
    }

    /**
     * Testing that removing not existing ammo will not modify ammo list
     */
    @Test
    public void removeMissingAmmoTest(){
        PlayerBoard pb =new PlayerBoard();
        List<Color> ammoRmv = new ArrayList<>();
        List<Color> ammoRes = new ArrayList<>();

        ammoRmv.add(Color.RED);
        ammoRes.add(Color.YELLOW);
        ammoRes.add(Color.BLUE);

        pb.removeAmmos(ammoRmv);
        pb.removeAmmos(ammoRmv);

        assertThat(pb.getAmmos(), is(ammoRes));
    }
}

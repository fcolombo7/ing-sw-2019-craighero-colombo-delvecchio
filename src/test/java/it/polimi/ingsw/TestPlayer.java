package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.playerException.PlayerPowerupsException;
import it.polimi.ingsw.exceptions.playerException.PlayerWeaponsException;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Powerup;
import it.polimi.ingsw.model.Weapon;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TestPlayer {

    /**
     * Testing PlayerWeaponsException to be thrown when adding the fourth weapon to a player
     */
    @Test
    public void AddingFourthWeaponTest(){
        Weapon weapon = new Weapon("id", "name");
        Player player = new Player("","",false);
        player.addWeapon(weapon);
        player.addWeapon(weapon);
        player.addWeapon(weapon);
        try{
            player.addWeapon(weapon);
            fail("Expected a PlayerWeaponsException to be thrown");
        } catch(PlayerWeaponsException aPlayerWeaponException){
            assertThat(aPlayerWeaponException.getMessage(), is("Player weapons error: Player can't have more than 3 weapons"));
        }
    }

    /**
     * Testing PlayerWeaponException to be thrown when popping a weapon from a player without any of them
     */
    @Test
    public void PoppingEmptyWeaponsListTest(){
        try{
            Player player = new Player("", "", false);
            player.popWeapon(new Card("", ""));
            fail("Expected a PlayerWeaponsException to be thrown");
        } catch(PlayerWeaponsException aPlayerWeaponException){
            assertThat(aPlayerWeaponException.getMessage(), is("Player weapons error: Player does not have any weapon"));
        }
    }

    /**
     * Testing PlayerWeaponException to be thrown when popping a weapon the player does not own
     */
    @Test
    public void PoppingMissingWeaponTest(){
        Player player = new Player("", "", false);
        player.addWeapon(new Weapon("1", "a"));
        player.addWeapon(new Weapon("2", "b"));
        player.addWeapon(new Weapon("3", "c"));
        try{
            player.popWeapon(new Card("4", "d"));
            fail("Expected a PlayerWeaponsException to be thrown");
        } catch(PlayerWeaponsException aPlayerWeaponException){
            assertThat(aPlayerWeaponException.getMessage(), is("Player weapons error: Player does not own the weapon"));
        }
    }

    /**
     * Testing PlayerPowerupsException to be thrown when adding the fourth powerup to a player
     */
    @Test
    public void AddingFourthPowerupTest(){
        Powerup powerup = new Powerup("id", "name");
        Player player = new Player("","",false);
        player.addPowerup(powerup);
        player.addPowerup(powerup);
        player.addPowerup(powerup);
        try{
            player.addPowerup(powerup);
            fail("Expected a PlayerPowerupsException to be thrown");
        } catch(PlayerPowerupsException aPlayerPowerupException){
            assertThat(aPlayerPowerupException.getMessage(), is("Player powerups error: Player can't have more than 3 powerups"));
        }
    }

    /**
     * Testing PlayerPowerupsException to be thrown when popping a powerup from a player without any of them
     */
    @Test
    public void PoppingEmptyPowerupsListTest(){
        try{
            Player player = new Player("", "", false);
            player.popPowerup(new Card("", ""));
            fail("Expected a PlayerPowerupsException to be thrown");
        } catch(PlayerPowerupsException aPlayerPowerupsException){
            assertThat(aPlayerPowerupsException.getMessage(), is("Player powerups error: Player does not have any powerup"));
        }
    }

    /**
     * Testing PlayerPowerupsException to be thrown when popping a powerup the player does not own
     */
    @Test
    public void PoppingMissingPowerupTest(){
        Player player = new Player("", "", false);
        player.addPowerup(new Powerup("1", "a"));
        player.addPowerup(new Powerup("2", "b"));
        player.addPowerup(new Powerup("3", "c"));
        try{
            player.popPowerup(new Card("4", "d"));
            fail("Expected a PlayerPowerupsException to be thrown");
        } catch(PlayerPowerupsException aPlayerPowerupsException){
            assertThat(aPlayerPowerupsException.getMessage(), is("Player powerups error: Player does not own the powerup"));
        }
    }

    /**
     * Testing IllegalArgumentException to be thrown when passing negative integer to updateScore method
     */
    @Test
    public void NegativePointsTest(){
        try{
            Player player = new Player("", "", false);
            player.updateScore(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException anIllegalArgumentException){
            assertThat(anIllegalArgumentException.getMessage(), is("Score can't decrease"));
        }
    }

    /**
     * Testing correct adding weapon to a player weapons list
     */
    @Test
    public void CorrectAddingWeaponTest(){
        Player player = new Player("t", "ttt", false);
        Weapon weapon = new Weapon("1", "a");
        player.addWeapon(weapon);
        assertThat(player.toString(), is("Player\nNickname: " + player.getNickname() + "\nMotto: " + player.getMotto() + "\nScore: " + player.getScore() + "\nRoom: " + "Nowhere" + "\nWeapons:\n" + ((Card)weapon).toString() + "\n"));
    }

    /**
     * Testing correct adding powerup to a player powerups list
     */
    @Test
    public void CorrectAddingPowerupTest(){
        Player player = new Player("t", "ttt", false);
        Powerup powerup = new Powerup("1", "a");
        player.addPowerup(powerup);
        assertThat(player.toString(), is("Player\nNickname: " + player.getNickname() + "\nMotto: " + player.getMotto() + "\nScore: " + player.getScore() + "\nRoom: " + "Nowhere" + "\nPowerups:\n" + ((Card)powerup).toString() + "\n"));
    }

    /**
     * Testing correct popping weapon from a player weapons list
     */
    @Test
    public void CorrectPoppingWeaponTest(){
        Player player = new Player("t", "ttt", false);
        Weapon weapon1 = new Weapon("1", "a");
        Weapon weapon2 = new Weapon("2", "b");

        player.addWeapon(weapon1);
        player.addWeapon(weapon2);
        player.popWeapon(weapon1);

        assertThat(player.toString(), is("Player\nNickname: " + player.getNickname() + "\nMotto: " + player.getMotto() + "\nScore: " + player.getScore() + "\nRoom: " + "Nowhere" + "\nWeapons:\n" + ((Card)weapon2).toString() + "\n"));
    }

    /**
     * Testing correct popping powerup from a player powerups list
     */
    @Test
    public void CorrectPoppingPowerupTest(){
        Player player = new Player("t", "ttt", false);
        Powerup powerup1 = new Powerup("1", "a");
        Powerup powerup2 = new Powerup("2", "b");

        player.addPowerup(powerup1);
        player.addPowerup(powerup2);
        player.popPowerup(powerup1);

        assertThat(player.toString(), is("Player\nNickname: " + player.getNickname() + "\nMotto: " + player.getMotto() + "\nScore: " + player.getScore() + "\nRoom: " + "Nowhere" + "\nPowerups:\n" + ((Card)powerup2).toString() + "\n"));
    }

    /**
     * Testing correct updating score
     */
    @Test
    public void CorrectUpdateScoreTest(){
        Player player = new Player("t", "ttt", false);
        player.updateScore(4);
        player.updateScore(8);
        player.updateScore(1);

        assertThat(player.toString(), is("Player\nNickname: " + player.getNickname() + "\nMotto: " + player.getMotto() + "\nScore: " + "13" + "\nRoom: " + "Nowhere" + "\n"));
    }
}

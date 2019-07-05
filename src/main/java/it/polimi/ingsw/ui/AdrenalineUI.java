package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.List;

/**
 * This interface represents the method called by the server connection on the UI
 */
public interface AdrenalineUI {

    /*------- METHODS TO MANAGE MESSAGES FROM SERVER -------*/

    /*ROOM METHOD*/

    /**
     * This method is called when a player join a waiting room
     * @param nickname a String representing the nickname chosen by the player joined
     */
    void onJoinRoomAdvise(String nickname);

    /**
     * This method is called when a player leaves e waiting room
     * @param nickname a String representing the nickname chosen by the player left
     */
    void onExitRoomAdvise(String nickname);

    /**
     * This method advise the player who is the first entering the waiting room
     */
    void onFirstInRoomAdvise();

    /*MATCH METHOD*/

    /**
     * This method is called when the match started
     * @param players a List of player representing who has joined the match
     * @param playerTurnNumber an integer representing the position on turn rotation of the player
     */
    void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber);

    /**
     * This method is called when the server received a wrong message
     * @param msg a String representing the error received
     */
    void onInvalidMessageReceived(String msg);

    /**
     * This method is called when the game board has an update (ammo tiles, weapons, ecc.)
     * @param gameBoard represents the board updated
     */
    void onBoardUpdate(SimpleBoard gameBoard);

    /**
     * This method is called when there is an update on the players status and game status
     * @param players represents the players still in the match
     * @param gameBoard represents the game board updated
     * @param frenzy a boolean representing whether the match is in frenzy mode
     */
    void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    /**
     * This method is called when a player is going to spawn
     * @param powerups represents the powerups draw for the choice of where to spawn
     * @param colors represents the color of the powerups received
     */
    void onRespwanRequest(List<Card> powerups, List<Color> colors);

    /**
     * This method is called when a respawn of a player is completed
     * @param player represents the player spawned
     * @param discardedPowerup represents the powerup chosen to spawn
     * @param powerupColor represents the color of that powerup
     */
    void onRespwanCompleted(SimplePlayer player, Card discardedPowerup, Color powerupColor);

    /**
     * This method is called when an ammo tile is grabbed
     * @param player represents the player grabbing
     * @param grabbedTile represents the tile grabbed
     */
    void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile);


    /**
     * This method is called when a player grab a powerup
     * @param player represents the player grabbing
     * @param powerup represents the powerup grabbed
     * @param color represents the color of that powerup
     */
    void onGrabbedPowerup(SimplePlayer player, Card powerup, Color color);

    /**
     * This method is called when a player choose to grab a weapon
     * @param weapons represents the weapon available to choose
     */
    void onGrabbableWeapons(List<Card> weapons);

    /**
     * This method is called when a player have to discard a weapon
     * @param weapons represents the weapons the player owns
     */
    void onDiscardWeapon(List<Card> weapons);

    /**
     * This method is called when a player grab a weapon
     * @param player represents the player grabbing
     * @param weapon represents the weapon grabbed
     */
    void onGrabbedWeapon(SimplePlayer player, Card weapon);

    /**
     * This method is called when a player reloads a weapon
     * @param player represents the player reloading
     * @param weapon represents the weapon reloaded
     * @param discardedPowerups represents the powerup used to reload
     * @param totalCost represents the ammo used to reload
     */
    void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost);

    /**
     * This method is called when a player wants to reload a weapon
     * @param weapons represents the weapons reloadable
     */
    void onReloadableWeapons(List<Card> weapons);

    /**
     * This method is called when a player has to choose which action wants to do
     * @param actions represents the available actions
     */
    void onTurnActions(List<String> actions);

    /**
     * This method is called when a turn ends
     */
    void onTurnEnd();

    /**
     * This method is called when a player has changed his position
     * @param player represents the player moving
     */
    void onMoveAction(SimplePlayer player);

    /**
     * This method is called when a player is moving
     * @param matrix represents where the player can move
     * @param targetPlayer represents the player moving
     */
    void onMoveRequest(MatrixHelper matrix, String targetPlayer);

    /**
     * This method is called when some player receive marks
     * @param player represents the nickname of the player that has marked
     * @param selected represents the player marked
     * @param value represents the number of mark gotten
     */
    void onMarkAction(String player, SimplePlayer selected, int value);

    /**
     * This method is called when a player suffer damages
     * @param player represents the nickname of the player that has dealt damages
     * @param selected represents the player suffering damages
     * @param damageValue represents how much damages was dealt
     * @param convertedMarks represents how much marks are converted in damages
     */
    void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks);

    /**
     * This method is called when a player discard a powerup
     * @param player represents the player discarding
     * @param powerup represents the powerup discarded
     */
    void onDiscardedPowerup(SimplePlayer player, Card powerup);

    /**
     * This method is called when a turn starts
     * @param currentPlayer represents the player playing
     */
    void onTurnCreation(String currentPlayer);

    /**
     * This method is called when the player must choose the players to target
     * @param selectable represents the set of target hittable
     * @param target represents the target of the weapon in use
     */
    void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target);

    /**
     * This method is called when a powerup can be used
     */
    void onCanUsePowerup();

    /**
     * This method is called when an optional effect can be used
     */
    void onCanStopRoutine();

    /**
     * This method is called when a player wants to shoot
     * @param usableWeapons represents the usable weapon
     */
    void onUsableWeapons(List<Card> usableWeapons);

    /**
     * This method is called when a player have to chose the effect to use
     * @param effects represents the effects available
     */
    void onAvailableEffects(List<String> effects);

    /**
     * This method is called when an effect is used
     * @param player represents the player using the effect
     * @param discardedPowerups represents the powerup discarded to use the effect
     * @param usedAmmo represents the ammo used
     */
    void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo);

    /**
     * This method is called when a card is used
     * @param card represents the card used
     */
    void onUsedCard(Card card);

    /**
     * This method is called when a player requests using a powerup
     * @param powerups represents the powerup available
     * @param colors represents the color of the powerup available
     */
    void onAvailablePowerups(List<Card> powerups, List<Color> colors);

    /**
     * This method is called when a player ha completed a run action
     * @param player represents the player running
     * @param newPosition represents the new position of the player
     */
    void onRunCompleted(SimplePlayer player, int[] newPosition);

    /**
     * This method is called when a player request to run
     * @param matrix represents where the player can run
     */
    void onRunRoutine(MatrixHelper matrix);

    /**
     * This method is called when a player come back in the match after a disconnection
     * @param players represents the players in the match
     * @param gameBoard represents the game board updated
     * @param frenzy represents whether the frenzy mode is on
     */
    void onPlayerWakeUp(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    /**
     * This method is called when a player a player recover to advise other player
     * @param nickname represents the nickname of the player recovering
     */
    void onRecoverPlayerAdvise(String nickname);

    /**
     * This method is called when a player can not grab more powerup
     */
    void onFullOfPowerup();

    /**
     * This method is called when a player can use a powerup in the turn of another player
     */
    void onCanCounterAttack();

    /**
     * This method is called when a player chose to use a powerup in another player turn
     * @param currentPlayer represents the player using the poweruo
     * @param player represents the target player
     * @param powerup represents the powerup used
     */
    void onCounterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup);

    /**
     * This method is called when a player does not answer to a counter attack before the timeout
     */
    void onCounterAttackTimeOut();

    /**
     * This method is called when a fatal error occured
     * @param cause represents the cause of the error
     * @param message represents the message of the error
     */
    void handleFatalError(String cause, String message);

    /**
     * This method advises a player disconnected
     */
    void onDisconnectionAdvise();

    /**
     * This method is called when the match ended
     * @param players represents the player still in the game
     */
    void onGameEnd(List<SimplePlayer> players);

    /**
     * This method send the leader board to the players
     * @param nicknames represents the nickname of the player playing
     * @param points represents the points of each player
     */
    void onLeaderboardReceived(List<String> nicknames, List<Integer> points);

}

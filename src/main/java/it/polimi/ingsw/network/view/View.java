package it.polimi.ingsw.network.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.utils.Observer;

/**
 * This class is used to represent the view of the pattern MVC
 */
public abstract class View implements Observer<MatchMessage> {

    /**
     * The associated player
     */
    private Player player;

    /**
     * This constructor instantiates a View
     * @param player the associated player
     */
    protected View(Player player) {
        this.player = player;
    }

    /**
     * this method returns the associated player
     * @return the associated player
     */
    protected Player getPlayer() {
        return player;
    }

    /**
     * The method used to show a message
     * @param message
     */
    protected abstract void show(MatchMessage message);
}

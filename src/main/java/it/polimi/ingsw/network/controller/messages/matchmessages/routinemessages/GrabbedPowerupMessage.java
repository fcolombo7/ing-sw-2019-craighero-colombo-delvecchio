package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class GrabbedPowerupMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 5162279721511025952L;
    private Card powerup;
    private SimplePlayer player;
    private Color color;

    public GrabbedPowerupMessage(String recipient, SimplePlayer player, Card powerup, Color color) {
        super(recipient, Constants.GRABBED_POWERUP);
        this.powerup=powerup;
        this.player=player;
        this.color=color;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getPowerup() {
        return powerup;
    }

    public Color getColor() {
        return color;
    }
}

package it.polimi.ingsw.turntests;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.view.View;

import java.util.Deque;

public class DebugView extends View {
    private Deque deque;

    public DebugView(Player player, Deque<MatchMessage> deque) {
        super(player);
        this.deque=deque;
    }

    @Override
    protected void show(MatchMessage message) {
        deque.push(message);
    }

    @Override
    public void update(MatchMessage message) {
        String recipient=message.getRecipient();
        if(recipient==null||recipient.equalsIgnoreCase(getPlayer().getNickname()))
            show(message);
    }
}

package it.polimi.ingsw.network.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.MatchAnswer;
import it.polimi.ingsw.model.messages.MatchMessage;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observer;

public class RemoteView extends View{

    private class MessageReceiver implements Observer<MatchAnswer> {

        @Override
        public void update(MatchAnswer message) {
            Logger.log("Ricevuto " + message);
        }

    }
    private ClientConnection clientConnection;

    public RemoteView(Player player, ClientConnection client) {
        super(player);
        this.clientConnection = client;
        clientConnection.register(new MessageReceiver());
    }

    @Override
    protected void show(MatchMessage message) {
        clientConnection.sendMatchMessage(message);
    }

    @Override
    public void update(MatchMessage message) {
        Player recipient=message.getRecipient();
        if(recipient==null||recipient.equals(getPlayer()))
            show(message);
    }
}

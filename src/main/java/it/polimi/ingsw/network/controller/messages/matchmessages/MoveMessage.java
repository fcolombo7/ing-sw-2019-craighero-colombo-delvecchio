package it.polimi.ingsw.network.controller.messages.matchmessages;

public class MoveMessage extends MatchMessage {
    private static final long serialVersionUID = -6788939687094472492L;

    public MoveMessage(String player, int[] position) {
        super(null);
    }

    @Override
    public String getRequest() {
        return null;
    }
}

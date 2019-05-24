package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

public class UsingCardMessage extends MatchMessage {
    private static final long serialVersionUID = -3198670873058015523L;

    private Card card;
    public UsingCardMessage(Card card) {
        super(null,Constants.USING_CARD_MESSAGE);
        this.card=new Card(card);
    }

    public Card getCard() {
        return card;
    }

}

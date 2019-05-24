package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

public class InvalidAnswerMessage extends MatchMessage {
    private static final long serialVersionUID = -2318735885536553587L;

    private String msg;

    public  InvalidAnswerMessage(String recipient, String msg) {
        super(recipient, Constants.INVALID_ANSWER);
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

}

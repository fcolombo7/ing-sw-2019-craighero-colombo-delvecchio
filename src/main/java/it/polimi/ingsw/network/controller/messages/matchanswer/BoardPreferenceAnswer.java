package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public class BoardPreferenceAnswer extends MatchAnswer {
    int boardReference;

    public BoardPreferenceAnswer(String sender, int boardReference){
        super(sender);
        this.boardReference=boardReference;
    }

    public int getRoomReference() {
        return boardReference;
    }

    @Override
    public String getAnswer() {
        return Constants.BOARD_SETTING_ANSWER;
    }
}

package it.polimi.ingsw.model.messages.matchanswer;

import it.polimi.ingsw.utils.Costants;

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
        return Costants.BOARD_SETTING_ANSWER;
    }
}

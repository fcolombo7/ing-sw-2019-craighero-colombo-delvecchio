package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectedPlayersAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 3927756431005395220L;
    private List<List<String>> selected;
    public SelectedPlayersAnswer(String sender, List<List<String>> selected) {
        super(sender,Constants.SELECTED_PLAYERS_ANSWER);
        this.selected=new ArrayList<>();
        for(List<String> list:selected){
            this.selected.add(new ArrayList<>(list));
        }
    }

    public List<List<String>> getSelected() {
        List<List<String>> temp=new ArrayList<>();
        for(List<String> list: selected)
            temp.add(new ArrayList<>(list));
        return temp;
    }
}

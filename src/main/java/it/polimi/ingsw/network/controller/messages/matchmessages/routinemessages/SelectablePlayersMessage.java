package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Target;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectablePlayersMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 5282667812811896935L;

    private List<List<String>> selectable;

    private SimpleTarget target;

    public SelectablePlayersMessage(String receiver, List<List<Player>> selectable, Target target) {
        super(receiver,Constants.SELECTABLE_PLAYERS_MESSAGE);
        this.selectable=new ArrayList<>();
        for(List<Player> list: selectable) {
            List<String> nickList = new ArrayList<>();
            for (Player player : list)
                nickList.add(player.getNickname());
            this.selectable.add(nickList);
        }
        this.target=new SimpleTarget(target);
    }

    public SimpleTarget getTarget() {
        return target;
    }

    public List<List<String>> getSelectable() {
        List<List<String>> temp=new ArrayList<>();
        for(List<String> list: selectable)
            temp.add(new ArrayList<>(list));
        return temp;
    }

}

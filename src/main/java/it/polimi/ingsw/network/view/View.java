package it.polimi.ingsw.network.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.MatchAnswer;
import it.polimi.ingsw.model.messages.MatchMessage;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

//observable--controller, observer--model STRING NEED TO BE CHANGED
public abstract class View extends Observable<MatchAnswer> implements Observer<MatchMessage> {

    private Player player;

    View(Player player) {
        this.player = player;
    }

    protected Player getPlayer() {
        return player;
    }

    protected abstract void show(MatchMessage message);
}

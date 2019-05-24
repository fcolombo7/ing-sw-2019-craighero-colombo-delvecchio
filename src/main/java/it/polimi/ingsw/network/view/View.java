package it.polimi.ingsw.network.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

public abstract class View extends Observable<MatchAnswer> implements Observer<MatchMessage> {

    private Player player;

    protected View(Player player) {
        this.player = player;
    }

    protected Player getPlayer() {
        return player;
    }

    protected abstract void show(MatchMessage message);
}

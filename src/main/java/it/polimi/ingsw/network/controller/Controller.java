package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.messages.MatchAnswer;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observer;

public class Controller implements Observer<MatchAnswer> {
    private Game game;

    public Controller(Game game) {
        this.game = game;
    }

    @Override
    public void update(MatchAnswer message) {
        Logger.log("Received '"+message.getAnswer()+"' from "+message.getSender().getNickname());
    }
}

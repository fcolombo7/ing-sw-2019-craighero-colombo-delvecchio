package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardMessage extends MatchMessage {
    private static final long serialVersionUID = 8985896310697703433L;
    private List<String> nicknames;
    private List<Integer> points;
    public LeaderboardMessage(String nickname, List<String> nicknames, List<Integer> points) {
        super(nickname, Constants.LEADERBOARD_MESSAGE);
        this.nicknames=new ArrayList<>(nicknames);
        this.points=new ArrayList<>(points);
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<Integer> getPoints() {
        return points;
    }
}

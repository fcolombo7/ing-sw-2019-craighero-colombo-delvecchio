package it.polimi.ingsw.model;

import java.util.Deque;

public interface Constraint {
    boolean checkConstraint(Player availablePlayer, Deque<Player> shotPlayers);
}

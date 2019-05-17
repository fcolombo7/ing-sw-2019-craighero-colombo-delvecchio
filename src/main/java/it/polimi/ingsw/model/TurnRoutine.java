package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;

public interface TurnRoutine {
    void handleAnswer(TurnRoutineAnswer answer);
    TurnRoutineType getType();
    void updateTurnStatus();
}
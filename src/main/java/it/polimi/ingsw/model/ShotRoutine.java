package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;

public class ShotRoutine implements TurnRoutine {
    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {

    }

    @Override
    public TurnRoutineType getType() {
        return null;
    }

    @Override
    public void updateTurnStatus() {

    }
}

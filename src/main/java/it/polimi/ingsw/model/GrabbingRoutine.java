package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;

public class GrabbingRoutine implements TurnRoutine {
    @Override
    public void start() {

    }

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

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {

    }

    @Override
    public boolean isInnerRoutine() {
        return false;
    }

    @Override
    public void onEffectPerformed() {

    }
}

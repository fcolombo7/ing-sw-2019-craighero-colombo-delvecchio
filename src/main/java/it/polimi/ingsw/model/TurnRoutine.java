package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;

/**
 * This interface represents the interface of the Turn routine.
 * This interface defines the standard of all the Routine classes which change the model data and notify them to player view.
 */
public interface TurnRoutine {
    /**
     * This method starts the routine
     */
    void start();

    /**
     * This method handle an answer to the routine
     * @param answer representing the answer which need to be sent to the routine
     */
    void handleAnswer(TurnRoutineAnswer answer);

    /**
     * This method returns the routine type
     * @return the routine type
     */
    TurnRoutineType getType();

    /**
     * This method is used to update the turn status with the current routine status
     */
    void updateTurnStatus();

    /**
     * This method is used to execute the father routine when the son one is finished
     * @param routineType the routine type of the son
     */
    void onInnerRoutineCompleted(TurnRoutineType routineType);

    /**
     * This method return true if is a son routine
     * @return true if is a son routine
     */
    boolean isInnerRoutine();

    /**
     * This method is used when the effect of the routine is performed and the routine need to finish his computation
     */
    void onEffectPerformed();
}
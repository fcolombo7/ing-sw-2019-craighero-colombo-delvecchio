package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.RunRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

public class RunRoutine implements TurnRoutine {

    private int distance;
    private Turn turn;
    private MatrixHelper distanceMatrix;

    public RunRoutine(Turn turn, int distance){
        this.distance=distance;
        this.turn=turn;
        sendWhereToMove();
    }

    private void sendWhereToMove() {
        int[] playerPos=turn.getGame().getCurrentPlayer().getPosition().getBoardIndexes();
        distanceMatrix = turn.getGame().getGameBoard().getDistanceMatrix(playerPos[0],playerPos[1],distance);
        TurnRoutineMessage message=new RunRoutineMessage(turn.getGame().getCurrentPlayer().getNickname(),distanceMatrix);
        turn.sendRoutineMessage(message);
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equals(Constants.RUN_ROUTINE_ANSWER)){
            RunRoutineAnswer msg=(RunRoutineAnswer)answer;
            if(distanceMatrix.toBooleanMatrix()[msg.getNewPosition()[0]][msg.getNewPosition()[1]]){
                turn.getGame().getCurrentPlayer().setPosition(turn.getGame().getGameBoard().getSquare(msg.getNewPosition()[0],msg.getNewPosition()[1]));
                TurnRoutineMessage message=new RunRoutineCompleted(new SimplePlayer(turn.getGame().getCurrentPlayer()),msg.getNewPosition());
                turn.sendRoutineMessage(message);
                turn.endRoutine();
            }else{
                Logger.log("Invalid newPosition received");
                turn.getGame().sendInvalidAction("Received an unreachable position.");
            }
        }else{
            Logger.log("Invalid TurnRoutineMessage received");
            turn.getGame().sendInvalidAction("Received a "+answer.getRoutineAnswer()+" message.");
        }
    }

    @Override
    public TurnRoutineType getType() {
        return TurnRoutineType.RUN;
    }

    @Override
    public void updateTurnStatus() {
        turn.setStatus(TurnStatus.RUNNING);
    }
}

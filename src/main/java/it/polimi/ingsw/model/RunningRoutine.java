package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer.RunAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.RunCompleted;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.RunMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

/**
 * This class represents the RunningRoutine
 */
public class RunningRoutine implements TurnRoutine {

    /**
     * This attribute contains the current turn
     */
    private Turn turn;

    /**
     * This attribute contains the position the player can reach during the run rountine
     */
    private MatrixHelper runMatrix;

    /**
     * This attribute indicates if the routine is a son routine
     */
    private boolean inner;

    /**
     * This constructor instantiates a RunningRoutine
     * @param turn the current turn
     * @param distance the max distance
     */
    RunningRoutine(Turn turn, int distance){
        this.turn=turn;
        inner=false;
        int[] playerPos=turn.getGame().getCurrentPlayer().getPosition().getBoardIndexes();
        runMatrix = turn.getGame().getGameBoard().getDistanceMatrix(playerPos[0],playerPos[1],distance);
    }

    /**
     * This constructor instantiates a RunningRoutine
     * @param turn represents the current turn
     * @param runMatrix represents the where to run matrix
     * @param isInner a boolean which is true if is an inner routine
     */
    RunningRoutine(Turn turn, MatrixHelper runMatrix, boolean isInner){
        this.turn=turn;
        inner=isInner;
        this.runMatrix=new MatrixHelper(runMatrix.toBooleanMatrix());
    }

    /**
     * This method is used to send where the cur player can run to
     */
    private void sendWhereToMove() {
        TurnRoutineMessage message=new RunMessage(turn.getGame().getCurrentPlayer().getNickname(), runMatrix);
        turn.getGame().notify(message);
    }

    @Override
    public void start() {
        turn.pushRoutineInStack(this);
        updateTurnStatus();
        sendWhereToMove();
    }

    @Override
    public void handleAnswer(TurnRoutineAnswer answer) {
        if(answer.getRoutineAnswer().equals(Constants.RUN_ROUTINE_ANSWER)){
            RunAnswer msg=(RunAnswer)answer;
            if(runMatrix.toBooleanMatrix()[msg.getNewPosition()[0]][msg.getNewPosition()[1]]){
                turn.getGame().getCurrentPlayer().setPosition(turn.getGame().getGameBoard().getSquare(msg.getNewPosition()[0],msg.getNewPosition()[1]));
                MatchMessage message=new RunCompleted(new SimplePlayer(turn.getGame().getCurrentPlayer()),msg.getNewPosition());
                turn.getGame().notify(message);
                turn.endRoutine();
            }else{
                Logger.logAndPrint("UNREACHABLE NEW POSITION");
                turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"UNREACHABLE NEW POSITION")));
            }
        }else{
            Logger.logAndPrint("Invalid TurnRoutineMessage received");
            turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"Received a "+answer.getRoutineAnswer()+" message.")));
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

    @Override
    public void onInnerRoutineCompleted(TurnRoutineType routineType) {
        Logger.logAndPrint("RunningRoutine can not start inner routine");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"RunningRoutine can not start inner routine")));
    }

    @Override
    public boolean isInnerRoutine() {
        return inner;
    }

    @Override
    public void onEffectPerformed() {
        Logger.logAndPrint("RunningRoutine can not perform an effect");
        turn.getGame().notify((new InvalidAnswerMessage(turn.getGame().getCurrentPlayer().getNickname(),"RunningRoutine can not perform an effect")));
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchanswer.ActionSelectedAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.*;

public class Turn {
    private Game game;
    private TurnStatus status;

    private Deque<Player> selectedPlayers;
    private List<Player> lastDamagedPlayers;
    private Effect curEffect;
    private MatrixHelper shiftableMatrix;

    private Deque<TurnRoutine> inExecutionRoutines;
    private Map<String, MatrixHelper> movementMap;
    private List<String> actions;
    private int routineNumber;

    public Turn(Game game){
        this.game=game;
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                routineNumber=1;
        }
        else routineNumber=2;
        status=TurnStatus.WAITING_PLAYER;
        selectedPlayers =new ArrayDeque<>();
        lastDamagedPlayers=new ArrayList<>();
        inExecutionRoutines=new ArrayDeque<>();
        actions=null;
        shiftableMatrix=null;
        curEffect=null;
        movementMap = new HashMap<>();
        availableActions(false);
    }

    public void selectAction(ActionSelectedAnswer answer){
        try{
            createRoutine(answer.getSelection());
        }catch(IllegalArgumentException ex){
            Logger.log("Invalid action received");
            game.notify((new InvalidAnswerMessage(game.getCurrentPlayer().getNickname(),"Invalid action received")));
        }
    }

    private void availableActions(boolean end) {
        curEffect=null;
        actions = new ArrayList<>();
        for (TurnRoutineType type : TurnRoutineType.values()) {
            if(end){
                if(type==TurnRoutineType.RELOAD&&canPerformRoutine(type))
                    actions.add(type.name());
            }else if (canPerformRoutine(type))
                actions.add(type.name());
        }
        if(actions.isEmpty()){
            status=TurnStatus.END;
            game.endTurn();
        }else
            game.notify(new TurnActionsMessage(game.getCurrentPlayer().getNickname(),actions));
    }

    private void createRoutine(String type){
        TurnRoutineType rType=getRoutineType(type);
        if(!canPerformRoutine(rType)) throw new IllegalArgumentException();
        TurnRoutineFactory factory=new TurnRoutineFactory(this);
        TurnRoutine routine=factory.getTurnRoutine(rType);
        //decrementing the turnRoutine number
        if (rType==(TurnRoutineType.SHOOT)||rType==TurnRoutineType.RUN||rType==TurnRoutineType.GRAB)
            routineNumber--;
        routine.start();
    }

    private boolean canPerformRoutine(TurnRoutineType type){
        if(type!=null){
            TurnRoutineFactory factory=new TurnRoutineFactory(this);
            return factory.getTurnRoutine(type)!=null;
        }
        return false;
    }

    private TurnRoutineType getRoutineType(String type) {
        for(TurnRoutineType t:TurnRoutineType.values()){
            if(t.name().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }

    public void endRoutine() {
        status=TurnStatus.WAITING_PLAYER;
        TurnRoutine routine=inExecutionRoutines.pop();
        if(routine.isInnerRoutine()&&inExecutionRoutines.peek()!=null)
            inExecutionRoutines.peek().onInnerRoutineCompleted(routine.getType());
        if(inExecutionRoutines.peek()==null)
            availableActions(routine.getType()!=TurnRoutineType.RELOAD);
    }

    void pushRoutineInStack(TurnRoutine routine){
        inExecutionRoutines.push(routine);
    }

    public TurnRoutine getInExecutionRoutine(){
        return inExecutionRoutines.peek();
    }

    public int getRoutineNumber() {
        return routineNumber;
    }

    public Game getGame() {
        return game;
    }

    public Deque<Player> getSelectedPlayers() {
        return new ArrayDeque<>(selectedPlayers);
    }

    public List<Player> getLastDamagedPlayers() {
        return new ArrayList<>(lastDamagedPlayers);
    }

    public void resetLatsDamagedPlayers(){
        lastDamagedPlayers.clear();
    }

    public void newLatsDamagedPlayers(Player player){
        lastDamagedPlayers.add(player);
    }

    public void addSelectedPlayer(Player player) {
        selectedPlayers.push(player);
    }

    public void clearSelectedPlayers(){
        selectedPlayers.clear();
    }

    public TurnStatus getStatus() {
        return status;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public void setShiftableMatrix(MatrixHelper matrix) {
        this.shiftableMatrix=new MatrixHelper(matrix.toBooleanMatrix());
    }

    public MatrixHelper getShiftableMatrix() {
        return new MatrixHelper(shiftableMatrix.toBooleanMatrix());
    }

    public void initShiftableMatrix(){
        shiftableMatrix=new MatrixHelper(game.getGameBoard().getGameboardMatrix().toBooleanMatrix());
    }

    public void clearMovementMap(){
        movementMap.clear();
    }

    public void insertPlayerMatrix(Player p, MatrixHelper mat){
        movementMap.put(p.getNickname(),mat);
    }

    public MatrixHelper getPlayerMatrix(Player p){
        return movementMap.get(p.getNickname());
    }

    public Effect getCurEffect() {
        return curEffect;
    }

    public void setCurEffect(Effect curEffect) {
        this.curEffect = curEffect;
    }
}
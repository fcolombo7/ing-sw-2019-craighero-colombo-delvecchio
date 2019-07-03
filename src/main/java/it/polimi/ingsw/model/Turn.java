package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.*;

/**
 * This class is the abstract representation of a Turn of the Adrenaline match.
 */
public class Turn {
    /**
     * This attributes contains the Game in which the Turn obj is instantiates
     */
    private Game game;

    /**
     * This attribute contains the Turn status
     */
    private TurnStatus status;

    /**
     * This attribute contains the queue of the players selected during the turn
     */
    private Deque<Player> selectedPlayers;

    /**
     *  This attribute contains the list of the players damaged during the turn
     */
    private List<Player> lastDamagedPlayers;

    /**
     * This attribute contains the current effect you are performing
     */
    private Effect curEffect;

    /**
     * This attribute contains the movement Matrix computed by the effect obj, and used during the turn in order to move the player in a new correct position
     */
    private MatrixHelper shiftableMatrix;

    /**
     * This attribute contains the stack of in execution routine
     */
    private Deque<TurnRoutine> inExecutionRoutines;

    /**
     * This attribute contains all the movement matrix associated to the relative player
     */
    private Map<String, MatrixHelper> movementMap;

    /**
     * This attribute contains all the actions the current player can perform during his turn
     */
    private List<String> actions;

    /**
     * This attributes contains the number of routine the player can perform during his turn
     */
    private int routineNumber;

    /**
     * This constructor instantiates a turn
     * @param game representing the game which contains the current turn
     */
    public Turn(Game game){
        this.game=game;
        game.getCurrentPlayer().setStatus(PlayerStatus.PLAYING);
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

    /**
     * This method is used to select an action to perform during the turn.
     * @param answer representing the action chose by the current player
     */
    public void selectAction(String answer){
        try{
            if(answer.equals("END")) {
                status=TurnStatus.END;
                game.notify(new TurnEndMessage(game.getCurrentPlayer().getNickname()));
            }
            else
                createRoutine(answer);
        }catch(IllegalArgumentException ex){
            Logger.logAndPrint("Invalid action received");
            game.notify((new InvalidAnswerMessage(game.getCurrentPlayer().getNickname(),"Invalid action received")));
        }
    }

    /**
     * This method is used to compute the available action of the turn and notify them to the current player
     * @param end boolean which is true if the turn is ended, so the player can only reload his weapon
     */
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
            game.notify(new TurnEndMessage(game.getCurrentPlayer().getNickname()));
        }else {
            actions.add("END");
            game.notify(new TurnActionsMessage(game.getCurrentPlayer().getNickname(), actions));
        }
    }

    /**
     * This method create a routine.
     * @param type representing the type of the routine you want to perform
     */
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

    /**
     * This method checks if a routine can be performed
     * @param type representing the type of the routine you want to perform
     * @return true if the routine of type 'type' can be performed
     */
    private boolean canPerformRoutine(TurnRoutineType type){
        if(type!=null){
            TurnRoutineFactory factory=new TurnRoutineFactory(this);
            return factory.getTurnRoutine(type)!=null;
        }
        return false;
    }

    /**
     * This method convert a string value into a routine type
     * @param type representing the value you want to convert
     * @return the RoutinType associated to the value (parameter)
     */
    private TurnRoutineType getRoutineType(String type) {
        for(TurnRoutineType t:TurnRoutineType.values()){
            if(t.name().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }

    /**
     * This method is used to end the routine that you have yet performed
     */
    void endRoutine() {
        status=TurnStatus.WAITING_PLAYER;
        TurnRoutine routine=inExecutionRoutines.pop();
        if(routine.isInnerRoutine()&&inExecutionRoutines.peek()!=null)
            inExecutionRoutines.peek().onInnerRoutineCompleted(routine.getType());
        if(!routine.isInnerRoutine()&&inExecutionRoutines.peek()==null)
            availableActions(routine.getType()==TurnRoutineType.RELOAD);
    }

    /**
     * This method is used to push the rountine in the stack in order to guarantee the right sequence
     * @param routine representing the routine you want to push in the stack
     */
    void pushRoutineInStack(TurnRoutine routine){
        inExecutionRoutines.push(routine);
    }

    /**
     * This method is used to get the routine which is in execution
     * @return the current TurnRoutine
     */
    public TurnRoutine getInExecutionRoutine(){
        return inExecutionRoutines.peek();
    }

    /**
     * This method return the number of available routine the player can perform
     * @return the number of available routine the player can perform
     */
    int getRoutineNumber() {
        return routineNumber;
    }

    /**
     * This method is used to get the Game obj
     * @return the Game obj
     */
    public Game getGame() {
        return game;
    }

    /**
     * This method is used to get the players which have been selected in the current turn
     * @return the players which have been selected in the current turn
     */
    public Deque<Player> getSelectedPlayers() {
        return new ArrayDeque<>(selectedPlayers);
    }

    /**
     * This method is used to get the last damaged players in the current turn
     * @return the last damaged players in the current turn
     */
    public List<Player> getLastDamagedPlayers() {
        return new ArrayList<>(lastDamagedPlayers);
    }

    /**
     * This method is used to reset the last damaged players
     */
    public void resetLatsDamagedPlayers(){
        lastDamagedPlayers.clear();
    }

    /**
     * This method add a player to the last damaged players collection
     * @param player representing the player you want to add to the last damaged players
     */
    public void newLatsDamagedPlayers(Player player){
        lastDamagedPlayers.add(player);
    }

    /**
     * This method add a player to the selected players collection
     * @param player representing the player you want to the collection of selected players
     */
    public void addSelectedPlayer(Player player) {
        selectedPlayers.push(player);
    }

    /**
     * This method is used to clear the collection of selected players
     */
    public void clearSelectedPlayers(){
        selectedPlayers.clear();
    }

    /**
     * This method returns the Turn status
     * @return the turn status
     */
    public TurnStatus getStatus() {
        return status;
    }

    /**
     * This method sets the turn status
     * @param status represents the new status of the turn
     */
    protected void setStatus(TurnStatus status) {
        this.status = status;
    }

    /**
     * This method sets the movement matrix of the current player
     * @param matrix represents the movement matrix
     */
    void setShiftableMatrix(MatrixHelper matrix) {
        this.shiftableMatrix=new MatrixHelper(matrix.toBooleanMatrix());
    }

    /**
     * This method returns the movement matrix of the current player
     * @return the movement matrix of the current player
     */
    MatrixHelper getShiftableMatrix() {
        return new MatrixHelper(shiftableMatrix.toBooleanMatrix());
    }

    /**
     * This method is used to initialize the movement matrix of the current player
     */
    void initShiftableMatrix(){
        shiftableMatrix=new MatrixHelper(game.getGameBoard().getGameboardMatrix().toBooleanMatrix());
    }

    /**
     * This method clear the Map which contains players and their movement matrix
     */
    void clearMovementMap(){
        movementMap.clear();
    }

    /**
     * This method is used to insert a player and his movement matrix in the Map of movement matrices
     * @param p the player that owns the matrix
     * @param mat the matrix of the player
     */
    void insertPlayerMatrix(Player p, MatrixHelper mat){
        movementMap.put(p.getNickname(),mat);
    }

    /**
     * The matrix of the player
     * @param p representing the player you want to get his matrix
     * @return the matrix of the player
     */
    MatrixHelper getPlayerMatrix(Player p){
        return movementMap.get(p.getNickname());
    }

    /**
     * This method returns the current effect
     * @return the current effect
     */
    public Effect getCurEffect() {
        return curEffect;
    }

    /**
     * This method sets the current effect
     * @param curEffect representing the current effect
     */
    public void setCurEffect(Effect curEffect) {
        this.curEffect = curEffect;
    }

    /**
     * This method is used to force the turn closing in case of timeout
     */
    public void forceClosing() {
        status=TurnStatus.END;
    }
}
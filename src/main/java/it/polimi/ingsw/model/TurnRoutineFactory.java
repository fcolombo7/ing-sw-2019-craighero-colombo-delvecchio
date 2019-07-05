package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TargetType;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.enums.Color.ANY;

/**
 * This class represents the turn routine factory
 */
public class TurnRoutineFactory {
    /**
     * This attributes represents the current turn
     */
    private Turn turn;

    /**
     * This attributes represents the matrix used when player needs to move before shooting
     */
    private MatrixHelper moveBeforeShoot;

    /**
     * This attributes represents the matrix used when player needs to move before grabbing
     */
    private MatrixHelper whereToGrab;

    /**
     * This constructor instantiates a TurnRoutineFactory object
     * @param turn representing the current turn
     */
    public TurnRoutineFactory(Turn turn){
        this.turn=turn;
        moveBeforeShoot=null;
    }

    /**
     * This method is used to get a TurnRoutine
     * @param type represents the TurnRoutine Type you want to get
     * @return a TurnRoutine obj
     */
    public TurnRoutine getTurnRoutine(TurnRoutineType type){
        TurnRoutine routine=null;
        switch (type){
            case RELOAD:
                if(canReload()) routine= new ReloadingRoutine(turn);
                break;
            case POWERUP:
                if(canPowerup()) routine= new PowerupRoutine(turn,turn.getStatus(),turn.getInExecutionRoutine()!=null);
                break;
            case RUN:
                if(canRun()) routine=new RunningRoutine(turn, maxRunDistance());
                break;
            case SHOOT:
                if(canShoot()) routine=new ShootingRoutine(!turn.getGame().isFrenzy(),maxMoveDistanceBeforeShot()!=0?moveBeforeShoot:null,turn);
                break;
            case GRAB:
                if(canGrab()) routine= new GrabbingRoutine(turn,whereToGrab);
                break;
            default: break;
        }
        return routine;
    }

    /**
     * This method is used to check if can start a PowerupRoutine
     * @return true if can start a PowerupRoutine
     */
    private boolean canPowerup(){
        if(!turn.getGame().getCurrentPlayer().hasTimingPowerup(turn.getStatus())) return false;
        for(Powerup p:turn.getGame().getCurrentPlayer().getPowerups()){
            if(p.getTiming()==turn.getStatus()&&(p.getEffect().getCost().isEmpty()||haveAmmo(p.getEffect()))&&!(p.getEffect().getTarget().getType()== TargetType.PLAYER&&!thereArePlayersSpawned())) return true;
        }
        return false;
    }

    /**
     * This method is used to check if there are player spawned in the game in the right moment
     * @return true if there are player spawned in the game in the right moment
     */
    private boolean thereArePlayersSpawned() {
        for(Player p:turn.getGame().getPlayers()){
            if(!p.getNickname().equals(turn.getGame().getCurrentPlayer().getNickname())&&p.getPosition()!=null)
                return true;
        }
        return false;
    }

    /**
     * This method is used to check if the player has all the necessary ammo to execute the effect
     * @param effect represents the effect you want to execute
     * @return true if the player has all the necessary ammo to execute the effect
     */
    private boolean haveAmmo(Effect effect) {
        List<Color> ammo= new ArrayList<>(turn.getGame().getCurrentPlayer().getBoard().getAmmo());
        int count=0;
        for (Color color:effect.getCost()){
            if(color==ANY)
                count++;
            else if(ammo.contains(color))
                ammo.remove(color);
            else
                return false;
        }
        return (count<=ammo.size());
    }

    /**
     * This method checks if the player can reload
     * @return true if the player can reload
     */
    private boolean canReload() {
        for(Weapon w:turn.getGame().getCurrentPlayer().getWeapons()){
            if(turn.getGame().getCurrentPlayer().canReloadedWeapon(w))
                return true;
        }
        return false;
    }

    /**
     * This method is used to check if the player can run
     * @return true if the player can run
     */
    private boolean canRun(){
        if(turn.getRoutineNumber()>0){
            if(turn.getGame().isFrenzy()){
                return !(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()));
            }else return true;
        }
        return false;
    }

    /**
     * This method is used to check if the player can grab
     * @return true if the player can grab
     */
    private boolean canGrab(){
        if(turn.getRoutineNumber()>0){
            int maxDistance=maxGrabDistance();
            int []position=turn.getGame().getCurrentPlayer().getPosition().getBoardIndexes();
            MatrixHelper distMat=turn.getGame().getGameBoard().getDistanceMatrix(position[0],position[1],maxDistance);
            boolean[][] mat=distMat.toBooleanMatrix();

            int xLen=distMat.getRowLength();
            int yLen=distMat.getColLength();
            boolean[][] grabMat=new boolean[xLen][yLen];

            for(int i=0;i<xLen;i++){
                for(int j=0;j<yLen;j++)
                    grabMat[i][j]= mat[i][j] && turn.getGame().getGameBoard().hasSquare(i, j) && turn.getGame().getGameBoard().getSquare(i, j).canGrab()&&checkSpawnPoint(i,j);
            }
            whereToGrab=new MatrixHelper(grabMat);
            return !whereToGrab.bitWiseAnd(turn.getGame().getGameBoard().getGameboardMatrix()).equals(MatrixHelper.allFalseMatrix(xLen,yLen));
        }
        return false;
    }

    /**
     * This method is used to check if the index passed represent a spawn ponit
     * @param i representing the row index
     * @param j representing the col index
     * @return true if is a spawn point
     */
    private boolean checkSpawnPoint(int i, int j) {
        if(!turn.getGame().getGameBoard().isSpawnPoint(i,j))return true;
        else{
            List<Weapon> weapons=((WeaponSquare)turn.getGame().getGameBoard().getSquare(i,j)).getWeapons();
            List<Color> allColor=turn.getGame().getCurrentPlayer().getBoard().getAmmo();
            for (Powerup p:turn.getGame().getCurrentPlayer().getPowerups()) allColor.add(p.getColor());
            for(Weapon w:weapons){
                List<Color> tempColor=new ArrayList<>(allColor);
                boolean ok=true;
                for(int k=0;k<w.getAmmo().size();k++){
                    if(k==0)continue;
                    if(!tempColor.contains(w.getAmmo().get(k))){
                        ok=false;
                        break;
                    }else
                        tempColor.remove(w.getAmmo().get(k));
                }
                if(ok)
                    return true;
            }
            return false;
        }
    }

    /**
     * This method is used to check if the player can shoot
     * @return true if the player can shoot
     */
    private boolean canShoot(){
        if(turn.getRoutineNumber()>0){
            int row=turn.getGame().getGameBoard().getGameboardMatrix().getRowLength();
            int col=turn.getGame().getGameBoard().getGameboardMatrix().getColLength();
            int maxDistance= maxMoveDistanceBeforeShot();
            Player currPlayer=turn.getGame().getCurrentPlayer();
            int[]pos=currPlayer.getPosition().getBoardIndexes();
            List<Player> players = turn.getGame().getPlayers();
            players.remove(currPlayer);

            MatrixHelper distanceMatrix=turn.getGame().getGameBoard().getDistanceMatrix(pos[0],pos[1],maxDistance);
            boolean [][]mat=distanceMatrix.toBooleanMatrix();

            Square correctPosition=currPlayer.getPosition();
            boolean[][] temp=new boolean[row][col];
            for(int i=0;i<distanceMatrix.getRowLength();i++){
                for(int j=0;j<distanceMatrix.getColLength();j++){
                    if(mat[i][j]){
                        currPlayer.setPosition(turn.getGame().getGameBoard().getSquare(i,j));
                        temp[i][j]=hasAvailableWeapon(!turn.getGame().isFrenzy());
                    }else
                        temp[i][j]=false;
                }
            }
            moveBeforeShoot=new MatrixHelper(temp);
            currPlayer.setPosition(correctPosition);
            return !moveBeforeShoot.bitWiseAnd(turn.getGame().getGameBoard().getGameboardMatrix()).equals(MatrixHelper.allFalseMatrix(row,col));
        }
        return false;
    }

    /**
     * This method return the max grab distance for the player
     * @return max grab distance for the player
     */
    private int maxGrabDistance() {
        if(turn.getGame().isFrenzy()){
            if(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()))
                return 3;
            else
                return 2;
        }else
        {
            if(turn.getGame().getCurrentPlayer().getBoard().getHealthBar().size()<3) //not adrenalinic action
                return 1;
            else //adrenalinic action
                return 2;
        }
    }

    /**
     * This method return the max run distance for the player
     * @return max run distance for the player
     */
    private int maxRunDistance(){
        if(turn.getGame().isFrenzy()){
            if(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()))
                return -1;
            else
                return 4;
        }else return 3;
    }

    /**
     * This method return the max run distance before shoot for the player
     * @return max run distance before shoot distance for the player
     */
    private int maxMoveDistanceBeforeShot() {
        if(turn.getGame().isFrenzy()){
            if(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()))
                return 2;
            else
                return 1;
        }else
        {
            if(turn.getGame().getCurrentPlayer().getBoard().getHealthBar().size()<6)
                //not adrenalinic action
                return 0;

            else
                //adrenalinic action
                return 1;
        }
    }

    /**
     * This method is used to check if the player has available weapons
     * @param checkLoaded representing if the server need to check or not if a weapon is
     * @return true if the player has available weapons
     */
    private boolean hasAvailableWeapon(boolean checkLoaded) {
        for (Weapon w: turn.getGame().getCurrentPlayer().getWeapons()) {
            w.initNavigation();
            List<Effect> usableEffects=w.getUsableEffects(checkLoaded,turn);
            if(!usableEffects.isEmpty()) return true;
        }
        return false;
    }
}

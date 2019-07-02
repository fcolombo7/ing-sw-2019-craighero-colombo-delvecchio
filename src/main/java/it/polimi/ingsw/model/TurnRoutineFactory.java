package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.enums.Color.ANY;

public class TurnRoutineFactory {
    private Turn turn;
    private MatrixHelper moveBeforeShoot;
    private MatrixHelper whereToGrab;
    public TurnRoutineFactory(Turn turn){
        this.turn=turn;
        moveBeforeShoot=null;
    }

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

    private boolean canPowerup(){
        if(!turn.getGame().getCurrentPlayer().hasTimingPowerup(turn.getStatus())) return false;
        for(Powerup p:turn.getGame().getCurrentPlayer().getPowerups()){
            if(p.getTiming()==turn.getStatus()&&(p.getEffect().getCost().isEmpty()||haveAmmo(p.getEffect()))) return true;
        }
        return false;
    }

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

    private boolean canReload() {
        for(Weapon w:turn.getGame().getCurrentPlayer().getWeapons()){
            if(turn.getGame().getCurrentPlayer().canReloadedWeapon(w))
                return true;
        }
        return false;
    }

    private boolean canRun(){
        if(turn.getRoutineNumber()>0){
            if(turn.getGame().isFrenzy()){
                return !(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()));
            }else return true;
        }
        return false;
    }

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
                    grabMat[i][j]= mat[i][j] && turn.getGame().getGameBoard().hasSquare(i, j) && turn.getGame().getGameBoard().getSquare(i, j).canGrab();
            }
            whereToGrab=new MatrixHelper(grabMat);
            return !whereToGrab.bitWiseAnd(turn.getGame().getGameBoard().getGameboardMatrix()).equals(MatrixHelper.allFalseMatrix(xLen,yLen));
        }
        return false;
    }

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

    private int maxRunDistance(){
        if(turn.getGame().isFrenzy()){
            if(turn.getGame().getCurrentPlayer().isFirst()||turn.getGame().getPlayers().indexOf(turn.getGame().getLastPlayerBeforeFrenzy())>turn.getGame().getPlayers().indexOf(turn.getGame().getCurrentPlayer()))
                return -1;
            else
                return 4;
        }else return 3;
    }

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

    private boolean hasAvailableWeapon(boolean checkLoaded) {
        for (Weapon w: turn.getGame().getCurrentPlayer().getWeapons()) {
            w.initNavigation();
            List<Effect> usableEffects=w.getUsableEffects(checkLoaded,turn);
            if(!usableEffects.isEmpty()) return true;
        }
        return false;
    }
}

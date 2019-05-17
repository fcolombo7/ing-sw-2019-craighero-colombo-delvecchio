package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.TurnRoutineType;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.List;

public class TurnRoutineFactory {
    private Turn turn;
    public TurnRoutineFactory(Turn turn){this.turn=turn;}

    public TurnRoutine getTurnRoutine(TurnRoutineType type){
        TurnRoutine routine=null;

        return routine;
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
            for(int i=0;i<xLen;i++){
                for(int j=0;j<yLen;j++){
                    if(mat[i][j]&&turn.getGame().getGameBoard().hasSquare(i,j)&&turn.getGame().getGameBoard().getSquare(i,j).canGrab()) return true;
                }
            }
        }
        return false;
    }

    private boolean canShoot(){
        if(turn.getRoutineNumber()>0){
            int maxDistance=maxShotDistance();
            Player currPlayer=turn.getGame().getCurrentPlayer();
            int[]pos=currPlayer.getPosition().getBoardIndexes();
            List<Player> players = turn.getGame().getPlayers();
            players.remove(currPlayer);

            MatrixHelper distanceMatrix=turn.getGame().getGameBoard().getDistanceMatrix(pos[0],pos[1],maxDistance);
            boolean [][]mat=distanceMatrix.toBooleanMatrix();

            for(int i=0;i<distanceMatrix.getRowLength();i++){
                for(int j=0;j<distanceMatrix.getColLength();j++){
                    if(mat[i][j]){
                        Player p= new Player(currPlayer);
                        p.setPosition(turn.getGame().getGameBoard().getSquare(i,j));
                        if(canWeaponShoot(p,players,!turn.getGame().isFrenzy())) return true;
                    }
                }
            }
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

    private int maxShotDistance() {
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

    private boolean canWeaponShoot(Player p, List<Player> players, boolean checkLoaded) {
        for (Weapon w: p.getWeapons()) {
            w.initNavigation();
            List<Effect> usableEffects=w.getUsableEffect(p,players,turn.getShotPlayer(),turn.getGame().getGameBoard(),checkLoaded);
            if(!usableEffects.isEmpty()) return true;
        }
        return false;
    }
}

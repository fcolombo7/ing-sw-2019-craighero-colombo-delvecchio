package it.polimi.ingsw.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Turn {
    private Game game;
    private TurnStatus status;
    private Deque<Player> shotPlayer;
    private int routineNumber;

    public Turn(Game game){
        this.game=game;
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                routineNumber=1;
        }
        else routineNumber=2;
        status=TurnStatus.WAITING;
        shotPlayer=new ArrayDeque<>();
    }

    public void startRoutine(String type){

    }

    public boolean canRun(){
        if(routineNumber>0){
            if(game.isFrenzy()){
                return !(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()));
            }else return true;
        }
        return false;
    }

    public boolean canGrab(){
        if(routineNumber>0){
            int maxDistance=maxGrabDistance();
            int []position=game.getCurrentPlayer().getPosition().getBoardIndexes();
            MatrixHelper distMat=game.getGameBoard().getDistanceMatrix(position[0],position[1],maxDistance);
            boolean[][] mat=distMat.toBooleanMatrix();
            int xLen=distMat.getRowLength();
            int yLen=distMat.getColLength();
            for(int i=0;i<xLen;i++){
                for(int j=0;j<yLen;j++){
                    if(mat[i][j]&&game.getGameBoard().hasSquare(i,j)&&game.getGameBoard().getSquare(i,j).canGrab()) return true;
                }
            }
        }
        return false;
    }

    public boolean canShoot(){
        if(routineNumber>0){
            int maxDistance=maxShotDistance();
            Player currPlayer=game.getCurrentPlayer();
            int[]pos=currPlayer.getPosition().getBoardIndexes();
            List<Player>players = game.getPlayers();
            players.remove(currPlayer);

            MatrixHelper distanceMatrix=game.getGameBoard().getDistanceMatrix(pos[0],pos[1],maxDistance);
            boolean [][]mat=distanceMatrix.toBooleanMatrix();

            for(int i=0;i<distanceMatrix.getRowLength();i++){
                for(int j=0;j<distanceMatrix.getColLength();j++){
                    if(mat[i][j]){
                        Player p= new Player(currPlayer);
                        p.setPosition(game.getGameBoard().getSquare(i,j));
                        if(canWeaponShoot(p,players,!game.isFrenzy())) return true;
                    }
                }
            }
        }
        return false;
    }

    private int maxGrabDistance() {
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                return 3;
            else
                return 2;
        }else
        {
            if(game.getCurrentPlayer().getBoard().getHealthBar().size()<3) //not adrenalinic action
                return 1;
            else //adrenalinic action
                return 2;
        }
    }

    private int maxShotDistance() {
        if(game.isFrenzy()){
            if(game.getCurrentPlayer().isFirst()||game.getPlayers().indexOf(game.getLastPlayerBeforeFrenzy())>game.getPlayers().indexOf(game.getCurrentPlayer()))
                return 2;
            else
                return 1;
        }else
        {
            if(game.getCurrentPlayer().getBoard().getHealthBar().size()<6)
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
            List<Effect> usableEffects=w.getUsableEffect(p,players,shotPlayer,game.getGameBoard(),checkLoaded);
            if(!usableEffects.isEmpty()) return true;
        }
        return false;
    }

    public void end(){
        //punti?
        //respawn? -- no c'è anche il primo turno
        //ripopola gameboard
    }
}
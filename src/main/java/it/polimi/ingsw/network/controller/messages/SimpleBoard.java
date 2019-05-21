package it.polimi.ingsw.network.controller.messages;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Direction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleBoard implements Serializable{
    private static final long serialVersionUID = -5163203286486443654L;
    private SimpleSquare[][] board;
    private int rowLength;
    private int colLength;
    private int sourceReference;
    private List<String> killshotTrack;
    private List<Boolean> overkillTrack;
    private int skullNumber;
    private List<SimpleSquare> spawnPoints;

    public SimpleBoard(GameBoard gameBoard){
        this.sourceReference=gameBoard.getSourceReference();
        this.spawnPoints =new ArrayList<>(Color.values().length);
        this.overkillTrack=new ArrayList<>(gameBoard.getOverkillTrack());
        this.killshotTrack=new ArrayList<>();
        for(int i=0;i<gameBoard.getKillshotTrack().size();i++)
            this.killshotTrack.add(gameBoard.getKillshotTrack().get(i).getNickname());
        this.skullNumber=gameBoard.getSkullNumber();
        this.rowLength=gameBoard.getMap().length;
        this.colLength=gameBoard.getMap()[0].length;
        this.board=new SimpleSquare[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++){
                composeSquare(i,j,gameBoard);
            }
        }
    }

    private void composeSquare(int i, int j, GameBoard gameBoard) {
        if(gameBoard.hasSquare(i,j)){
            boolean[] doors = new boolean[]{
                    gameBoard.getSquare(i,j).hasDoor(Direction.NORTH),
                    gameBoard.getSquare(i,j).hasDoor(Direction.EAST),
                    gameBoard.getSquare(i,j).hasDoor(Direction.SOUTH),
                    gameBoard.getSquare(i,j).hasDoor(Direction.WEST)
            };

            board[i][j]=new SimpleSquare(i,j,gameBoard.getSquare(i,j).getRoomColor(), doors, gameBoard.isSpawnPoint(i,j));
            if(gameBoard.isSpawnPoint(i,j)){
                for (Weapon w:((WeaponSquare)gameBoard.getSquare(i,j)).getWeapons()) {
                    board[i][j].addWeaponCard(new Card(w));
                }
                spawnPoints.add(board[i][j]);
            }
            else{
                AmmoSquare as=(AmmoSquare)gameBoard.getSquare(i,j);
                if(as.canGrab()) board[i][j].setAmmoTile(as.popAmmoTile());
            }
        }
    }

    /**
     * This method check if in the direction given there is a square of the same color/room of the given one
     * @param coordinates represents the array containing the coordinates of the square given
     * @param direction represents the direction to check at
     * @return boolean representing whether the check is positive or not
     */
    public boolean sameRoom(int[] coordinates, Direction direction){
        switch(direction){
            case NORTH:
                if(coordinates[0]==0 || board[coordinates[0] - 1][coordinates[1]] == null)
                    return false;
                else return board[coordinates[0]][coordinates[1]].getRoomColor()==board[coordinates[0] - 1][coordinates[1]].getRoomColor();
            case EAST:
                if(coordinates[1]>=(board[0].length - 1) || board[coordinates[0]][coordinates[1] + 1] == null)
                    return false;
                else return board[coordinates[0]][coordinates[1]].getRoomColor()==board[coordinates[0]][coordinates[1] + 1].getRoomColor();
            case SOUTH:
                if(coordinates[0]>=(board.length - 1)|| board[coordinates[0] + 1][coordinates[1]] ==  null)
                    return false;
                else return board[coordinates[0]][coordinates[1]].getRoomColor()==board[coordinates[0] + 1][coordinates[1]].getRoomColor();
            case WEST:
                if(coordinates[1]==0 || board[coordinates[0]][coordinates[1] - 1] == null)
                    return false;
                else return board[coordinates[0]][coordinates[1]].getRoomColor()==board[coordinates[0]][coordinates[1] - 1].getRoomColor();
            default: throw new IllegalArgumentException("Direction is not valid");
        }
    }

    public SimpleSquare[][] getBoard() {
        return board;
    }

    public int getRowLength() {
        return rowLength;
    }

    public int getColLength() {
        return colLength;
    }

    public int getSourceReference() {
        return sourceReference;
    }

    public List<String> getKillshotTrack() {
        return new ArrayList<>(killshotTrack);
    }

    public List<Boolean> getOverkillTrack() {
        return new ArrayList<>(overkillTrack);
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    public List<SimpleSquare> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }
}

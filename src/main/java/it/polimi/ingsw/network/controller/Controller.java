package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.exceptions.MatchConfigurationException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.controller.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.network.controller.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller{
    private Game game;
    private List<Integer> boardPreference;

    public Controller(Game game) {
        this.game = game;
    }

    public void start() {
        boardPreference=new ArrayList<>(game.getPlayers().size());
        game.startMessage();
    }

    //non ricevono MatchAnswer ma direttamente i valori--->dispatcher in SocketClientConnection!

    //TODO: GESTIONE IN CASO DI STESSO PLAYER RICEVUTO
    public void roomPreferenceManager(String sender, int boardNumber) {
        String folderName= Constants.BOARD_FOLDER;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles==null) throw new MatchConfigurationException("No boards in "+folderName);
        for(File file:listOfFiles){
            if(file.getName().equalsIgnoreCase("board"+boardNumber+".xml")) {
                boardPreference.add(boardNumber);
                break;
            }
        }
        if(boardPreference.size()==game.getPlayers().size()){
            int selBoard=mostCommonBoard();
            game.setGameBoard(selBoard);
        }
    }
    private int mostCommonBoard() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer value : boardPreference) {
            Integer val = map.get(value);
            map.put(value, val == null ? 1 : val + 1);
        }

        Map.Entry<Integer, Integer> max = null;

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max==null?boardPreference.get(0):max.getValue();
    }
}

package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.exceptions.MatchConfigurationException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements Observer<MatchAnswer> {
    private Game game;
    private Map<String, AnswerManager> answerMap;

    private List<Integer> boardPreference;

    public Controller(Game game) {
        this.game = game;

        answerMap=new HashMap<>();
        loadAnswer();
    }

    public void start() {
        boardPreference=new ArrayList<>(game.getPlayers().size());
        game.startMessage();
    }

    @Override
    public void update(MatchAnswer message) {
        Logger.log("Received '"+message.getAnswer()+"' from "+message.getSender().getNickname());
        answerMap.get(message.getAnswer()).exec(message);
    }

    private void loadAnswer() {
        answerMap.put(Costants.BOARD_SETTING_ANSWER,this::roomPreferenceManager);
    }

    private void roomPreferenceManager(MatchAnswer message) {
        BoardPreferenceAnswer answer=(BoardPreferenceAnswer)message;
        String folderName="src/main/Resources/boards";
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles==null) throw new MatchConfigurationException("No boards in "+folderName);
        for(File file:listOfFiles){
            if(file.getName().equalsIgnoreCase("board"+answer.getRoomReference()+".xml")) {
                boardPreference.add(answer.getRoomReference());
                break;
            }
        }
        if(boardPreference.size()==game.getPlayers().size()){
            int selBoard=mostCommonBoard();
            game.setGameBoard(selBoard);
            //populate gameboard & send broadcast message
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

    @FunctionalInterface
    private interface AnswerManager {

        void exec(MatchAnswer message);
    }
}

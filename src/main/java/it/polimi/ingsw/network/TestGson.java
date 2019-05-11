package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;

public class TestGson {
    public static void main(String[] args){
        Gson gson= new Gson();
        MatchAnswer msg=new BoardPreferenceAnswer("nickname",0);
        System.out.println(gson.toJson(msg));
    }
}

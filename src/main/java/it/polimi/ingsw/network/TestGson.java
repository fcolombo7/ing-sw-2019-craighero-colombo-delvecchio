package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Weapon;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchmessages.BoardCreationMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;

import java.util.ArrayList;

public class TestGson {
    public static void main(String[] args){
        Gson gson= new Gson();
        MatchAnswer msg=new BoardPreferenceAnswer("nickname",0);
        System.out.println(gson.toJson(msg));
        Game game=new Game();
        game.addPlayer(new Player("Ciao","",true));
        game.addPlayer(new Player("Ciaooo","",false));
        game.setGameBoard(1);
        MatchMessage m=new BoardCreationMessage(game.getGameBoard());
        System.out.println(gson.toJson(m));

        ArrayList<Weapon> arrayList=new ArrayList<>();
        Weapon weapon=new Weapon("","","src/main/Resources/weapons/vulcanizzatore.xml");
        arrayList.add(weapon);
        System.out.println(gson.toJson(arrayList));
    }
}

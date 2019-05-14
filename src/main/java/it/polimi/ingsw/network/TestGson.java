package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchmessages.BoardCreationMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;

import java.util.ArrayList;

public class TestGson {
    public static void main(String[] args){
        Gson gson = new Gson();
        MatchAnswer msg=new BoardPreferenceAnswer("nickname",0);
        System.out.println(gson.toJson(msg));
        Game game=new Game();
        game.addPlayer(new Player("Ciao","",true));
        game.addPlayer(new Player("Ciaooo","",false));
        game.setGameBoard(1);
        MatchMessage m=new BoardCreationMessage(game.getGameBoard());
        //Gson gson1=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        System.out.println(gson.toJson(m));

/*
        ArrayList<Weapon> arrayList=new ArrayList<>();
        Weapon weapon=new Weapon("0","","src/main/Resources/weapons/vulcanizzatore.xml");
        Weapon weapon1=new Weapon("1","","src/main/Resources/weapons/vulcanizzatore.xml");
        Weapon weapon2=new Weapon("2","","src/main/Resources/weapons/vulcanizzatore.xml");
        arrayList.add(weapon);
        System.out.println(gson.toJson(arrayList));

        Square s= new WeaponSquare(RoomColor.RED,new boolean[]{true,false,true,false},new int[]{0,0});
        ((WeaponSquare)s).addWeapon(weapon);
        ((WeaponSquare)s).addWeapon(weapon1);
        ((WeaponSquare)s).addWeapon(weapon2);

        Square s2= new WeaponSquare(RoomColor.RED,new boolean[]{true,false,true,false},new int[]{0,0});
        ((WeaponSquare)s2).addWeapon(weapon);
        ((WeaponSquare)s2).addWeapon(weapon1);
        ((WeaponSquare)s2).addWeapon(weapon2);

        Square[][] mat=new Square[][]{{null,null,null},{null,null,null}};
        mat[0][1]=s;
        mat[1][0]=s2;
        System.out.println(gson.toJson(mat));

 */
    }
}

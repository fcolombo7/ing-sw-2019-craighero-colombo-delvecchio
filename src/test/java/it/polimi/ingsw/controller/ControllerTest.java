package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.network.controller.Controller;
import it.polimi.ingsw.network.controller.JoinRoomException;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void gameboardPreferenceTest(){
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        RemoteView rView1,rView2,rView3;
        Room room= new Room(new TestClient(new ArrayDeque<>()));
        Game game= new Game();
        Controller controller =new Controller(game,room);
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        cl1.setNickname("nickname1");
        cl2.setNickname("nickname2");
        cl3.setNickname("nickname3");
        rView1= new RemoteView(p1,cl1);
        rView2= new RemoteView(p2,cl2);
        rView3= new RemoteView(p3,cl3);
        game.register(rView1);
        game.register(rView2);
        game.register(rView3);
        cl1.setCollector(controller);
        cl2.setCollector(controller);
        cl3.setCollector(controller);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        controller.start();
        controller.roomPreferenceManager("nickname1",3);
        controller.roomPreferenceManager("nickname2",3);
        controller.roomPreferenceManager("nickname3",4);

        assertThat(game.getGameBoard().getSourceReference(),is(3));
    }

    @Test
    public void failRecoverTest(){
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        RemoteView rView1,rView2,rView3;
        Room room= new Room(new TestClient(new ArrayDeque<>()));
        Game game= new Game();
        Controller controller =new Controller(game,room);
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        cl1.setNickname("nickname1");
        cl2.setNickname("nickname2");
        cl3.setNickname("nickname3");
        rView1= new RemoteView(p1,cl1);
        rView2= new RemoteView(p2,cl2);
        rView3= new RemoteView(p3,cl3);
        game.register(rView1);
        game.register(rView2);
        game.register(rView3);
        cl1.setCollector(controller);
        cl2.setCollector(controller);
        cl3.setCollector(controller);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        controller.start();

        controller.addDisconnected("nickname1");
        try{
            controller.getPlayerToRecover("nickname2");
        }catch(IllegalStateException e){
            assertThat(e.getMessage(), is("Cannot found 'nickname2' in the disconnected players"));
        }

        try{
            controller.recoverPlayer(p2);
        }catch(IllegalStateException e){
            assertThat(e.getMessage(), is("Cannot found 'nickname2' in the disconnected players"));
        }

    }

    @Test
    public void matchCreationTest() {
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        cl1.setNickname("player1");
        cl2.setNickname("player2");
        cl3.setNickname("player3");
        Room room= new Room(cl1);
        try {
            room.joinRequest(cl2);
            room.joinRequest(cl3);
        } catch (JoinRoomException e) {
            fail("Unexpected JoinRoomException has been found");
        }
        assertThat(collector1.size(),is(3));
    }

    @Test
    public void firstSpawn(){
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        RemoteView rView1,rView2,rView3;
        Room room= new Room(new TestClient(new ArrayDeque<>()));
        Game game= new Game();
        Controller controller =new Controller(game,room);
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        cl1.setNickname("nickname1");
        cl2.setNickname("nickname2");
        cl3.setNickname("nickname3");
        rView1= new RemoteView(p1,cl1);
        rView2= new RemoteView(p2,cl2);
        rView3= new RemoteView(p3,cl3);
        game.register(rView1);
        game.register(rView2);
        game.register(rView3);
        cl1.setCollector(controller);
        cl2.setCollector(controller);
        cl3.setCollector(controller);

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        controller.start();
        controller.roomPreferenceManager("nickname1",1);
        controller.roomPreferenceManager("nickname2",2);
        controller.roomPreferenceManager("nickname3",2);
        assertThat(collector1.size(),is(5));
    }

    @Test
    public void nextPlayer(){
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        RemoteView rView1,rView2,rView3;
        Room room= new Room(new TestClient(new ArrayDeque<>()));
        Game game= new Game();
        Controller controller =new Controller(game,room);
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        cl1.setNickname("nickname1");
        cl2.setNickname("nickname2");
        cl3.setNickname("nickname3");
        rView1= new RemoteView(p1,cl1);
        rView2= new RemoteView(p2,cl2);
        rView3= new RemoteView(p3,cl3);
        game.register(rView1);
        game.register(rView2);
        game.register(rView3);
        cl1.setCollector(controller);
        cl2.setCollector(controller);
        cl3.setCollector(controller);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        controller.start();
        controller.roomPreferenceManager("nickname1",1);
        controller.roomPreferenceManager("nickname2",2);
        controller.roomPreferenceManager("nickname3",2);

        assertEquals(p1, game.getCurrentPlayer());
        controller.addDisconnected("nickname2");
        controller.nextPlayer();
        assertEquals(p3, game.getCurrentPlayer());
        controller.nextPlayer();
        assertEquals(p1, game.getCurrentPlayer());
    }

    @Test
    public void creatingAndClosingTurn(){
        ArrayDeque<String> collector1=new ArrayDeque<>();
        ArrayDeque<String> collector2=new ArrayDeque<>();
        ArrayDeque<String> collector3=new ArrayDeque<>();
        TestClient cl1,cl2,cl3;
        cl1=new TestClient(collector1);
        cl2=new TestClient(collector2);
        cl3=new TestClient(collector3);
        RemoteView rView1,rView2,rView3;
        Room room= new Room(new TestClient(new ArrayDeque<>()));
        Game game= new Game();
        Controller controller =new Controller(game,room);
        Player p1=new Player("nickname1", "", true);
        Player p2=new Player("nickname2", "", false);
        Player p3=new Player("nickname3", "", false);
        cl1.setNickname("nickname1");
        cl2.setNickname("nickname2");
        cl3.setNickname("nickname3");
        rView1= new RemoteView(p1,cl1);
        rView2= new RemoteView(p2,cl2);
        rView3= new RemoteView(p3,cl3);
        game.register(rView1);
        game.register(rView2);
        game.register(rView3);
        cl1.setCollector(controller);
        cl2.setCollector(controller);
        cl3.setCollector(controller);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        p3.getBoard().addDamage(p1,5);
        p3.getBoard().addDamage(p2,6);
        p3.setStatus(PlayerStatus.ALMOST_DEAD);
        controller.start();
        controller.roomPreferenceManager("nickname1",1);
        controller.roomPreferenceManager("nickname2",2);
        controller.roomPreferenceManager("nickname3",2);
        game.getCurrentTurn().forceClosing();
        controller.closeTurn("nickname1");
        assertNotNull(p1.getPosition());
        assertNotNull(p2.getPosition());
        assertNotNull(p3.getPosition());
    }
}

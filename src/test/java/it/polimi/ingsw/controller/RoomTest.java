package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.controller.JoinRoomException;
import it.polimi.ingsw.network.controller.Room;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void roomCreationTest(){
        ArrayDeque<String> collector=new ArrayDeque<>();
        TestClient cc= new TestClient(collector);
        cc.setNickname("n1");
        Room room= new Room(cc);
        try{
            for(int i=0;i<6;i++){
                ArrayDeque<String> coll=new ArrayDeque<>();
                TestClient ccc= new TestClient(collector);
                ccc.setNickname(""+i);
                room.joinRequest(ccc);
            }
        }
        catch (JoinRoomException e) {
            assertThat(e.getMessage(),is("full"));
        }
        assertEquals(0, room.getRoomNumber());
        assertFalse(room.canJoin());
        assertFalse(room.isPlaying());
    }

    @Test
    public void removingClient() throws InterruptedException {
        ArrayDeque<String> collector=new ArrayDeque<>();
        TestClient cc1 = new TestClient(collector);
        cc1.setNickname("n1");
        Room room= new Room(cc1);


        try {
            TestClient cc2 = new TestClient(collector);
            cc2.setNickname("n1");
            room.joinRequest(cc2);

            TestClient cc3 = new TestClient(collector);
            cc3.setNickname("n1");
            room.joinRequest(cc3);
            Thread.sleep(5000);

            room.getController().start();
            room.forceDisconnection(cc1.getNickname());
            room.isAlive(cc1);
            room.recoverClient(cc1);
        }
        catch (JoinRoomException e){
            fail(e.getMessage());
        }
    }
}

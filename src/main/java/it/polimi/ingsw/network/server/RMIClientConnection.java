package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.controller.messages.room.RoomMessage;
import it.polimi.ingsw.network.controller.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observable;

import java.rmi.RemoteException;

public class RMIClientConnection extends Observable<MatchAnswer> implements ClientConnection {
    private RMIServer server;
    private boolean online;
    private RMIClientHandler clientStub;
    private String nickname;
    private String motto;
    private Room room;
    private String session;

    public RMIClientConnection(RMIClientHandler clientStub,RMIServer server){
        this.clientStub=clientStub;
        this.server=server;
        online=true;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setSession(String session) {
        this.session = session;
    }

    synchronized void getMatchAnswer(MatchAnswer message){
        notify(message);
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void sendMatchMessage(MatchMessage message) {
        try {
            clientStub.sendMatchMessage(message);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown.");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void sendRoomMessage(RoomMessage message) {
        try{
            clientStub.sendRoomMessage(message);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown.");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        server.deregister(session);
    }
}

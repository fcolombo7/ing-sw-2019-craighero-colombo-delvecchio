package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.messages.MatchAnswer;
import it.polimi.ingsw.model.messages.MatchMessage;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observable;

import java.rmi.RemoteException;

public class RMIClientConnection extends Observable<MatchAnswer> implements ClientConnection {
    private boolean online;
    private RMIClientHandler clientStub;
    private String nickname;
    private String motto;
    private Room room;

    public RMIClientConnection(RMIClientHandler clientStub){
        this.clientStub=clientStub;
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
}

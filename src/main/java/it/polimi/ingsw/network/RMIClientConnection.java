package it.polimi.ingsw.network;

import it.polimi.ingsw.network.server.RMIClientHandler;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Observable;

public class RMIClientConnection extends Observable<String> implements ClientConnection {
    private Server server;
    private boolean online;
    private RMIClientHandler clientStub;
    private String nickname;
    private String motto;

    public RMIClientConnection(Server server, RMIClientHandler clientStub){
        this.server=server;
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

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public void setRoom(Room room) {

    }

    @Override
    public Room getRoom() {
        return null;
    }

    @Override
    public void asyncAction(String message) {

    }
}

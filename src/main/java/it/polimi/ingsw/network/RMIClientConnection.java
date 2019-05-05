package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observable;

import java.rmi.RemoteException;

public class RMIClientConnection  extends Observable<String> implements ClientConnection,Runnable  {
    private RMIClientHandler stub;

    private Server server;

    private boolean active = true;

    public RMIClientConnection(RMIClientHandler stub, Server server) {
        this.stub = stub;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    @Override
    public synchronized void closeConnection() {
        server.deregisterConnection(this);
        try {
            stub.actionRequest("CLOSE_CONNECTION");
            //RMI close?
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        active = false;
    }

    @Override
    public void asyncAction(String message) {
        new Thread(()->{
            try {
                stub.actionRequest(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        try{
            while(isActive()){
                if(stub.hasAnswered()) {
                    String msg = stub.actionAnswer();
                    notify(msg);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
    }

}

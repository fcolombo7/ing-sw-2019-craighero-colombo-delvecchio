package it.polimi.ingsw.network;

import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.room.ExitMessage;
import it.polimi.ingsw.network.controller.messages.room.JoinMessage;
import it.polimi.ingsw.network.controller.messages.room.RoomMessage;
import it.polimi.ingsw.network.server.RMIClientHandler;
import it.polimi.ingsw.network.server.RMIServerHandler;
import it.polimi.ingsw.utils.Constants;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TestRMIClient implements RMIClientHandler {

    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        Scanner stdin = new Scanner(System.in);
        Registry registry = LocateRegistry.getRegistry(Constants.RMI_PORT);

        System.out.print("RMI registry bindings: ");
        String[] e = registry.list();
        for (int i = 0; i < e.length; i++)
            System.out.println(e[i]);

        System.out.print("nickname: ");
        String nickname=stdin.nextLine();
        System.out.print("motto: ");
        String motto=stdin.nextLine();

        TestRMIClient rmiClient=new TestRMIClient();
        RMIClientHandler stub= (RMIClientHandler) UnicastRemoteObject.exportObject(rmiClient, Constants.RMI_PORT+1);
        registry.bind(nickname,stub);


        String remoteObjectName = Constants.RMI_SERVER_NAME;
        RMIServerHandler serverRMI = (RMIServerHandler) registry.lookup(remoteObjectName);

        String session = serverRMI.login(new LoginMessage(nickname,motto),stub);
        System.out.println(session);
    }

    @Override
    public synchronized void sendMatchMessage(MatchMessage message) throws RemoteException {
        System.out.println(message.getRequest());
    }

    @Override
    public void sendRoomMessage(RoomMessage message) throws RemoteException {
        if(message.getType().equalsIgnoreCase(Constants.PING_CHECK)){
            //
        }else if(message.getType().equalsIgnoreCase(Constants.PLAYER_EXIT)){
            System.out.println("User "+ ((ExitMessage)message).getPlayer()+" has left the room...");
        }else if(message.getType().equalsIgnoreCase(Constants.PLAYER_JOIN)){
            System.out.println("User "+ ((JoinMessage)message).getPlayer()+" has joined the room...");
        }else if(message.getType().equalsIgnoreCase(Constants.FIRST_PLAYER)){
            System.out.println("You are the first player in the room!");
        }
    }

    private static void loadMap() {

    }

    @FunctionalInterface
    private interface RoomManager {
        void exec();
    }
}

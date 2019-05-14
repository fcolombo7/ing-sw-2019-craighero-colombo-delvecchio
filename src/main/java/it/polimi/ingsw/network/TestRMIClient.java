package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.model.messages.LoginMessage;
import it.polimi.ingsw.model.messages.SimpleBoard;
import it.polimi.ingsw.model.messages.matchmessages.BoardCreationMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.server.RMIClientHandler;
import it.polimi.ingsw.network.server.RMIServerHandler;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TestRMIClient implements RMIClientHandler {

    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        Scanner stdin = new Scanner(System.in);
        Registry registry = LocateRegistry.getRegistry(Costants.RMI_PORT);

        System.out.print("RMI registry bindings: ");
        String[] e = registry.list();
        for (int i = 0; i < e.length; i++)
            System.out.println(e[i]);

        System.out.print("nickname: ");
        String nickname=stdin.nextLine();
        System.out.print("motto: ");
        String motto=stdin.nextLine();

        TestRMIClient rmiClient=new TestRMIClient();
        RMIClientHandler stub= (RMIClientHandler) UnicastRemoteObject.exportObject(rmiClient, Costants.RMI_PORT+1);
        registry.bind(nickname,stub);


        String remoteObjectName = Costants.RMI_SERVER_NAME;
        RMIServerHandler serverRMI = (RMIServerHandler) registry.lookup(remoteObjectName);

        String session = serverRMI.login(new LoginMessage(nickname,motto),stub);
        System.out.println(session);
    }

    @Override
    public synchronized void sendMatchMessage(MatchMessage message) throws RemoteException {
        System.out.println(message.getRequest());
    }
}

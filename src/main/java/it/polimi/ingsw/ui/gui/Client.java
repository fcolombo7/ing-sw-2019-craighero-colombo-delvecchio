package it.polimi.ingsw.ui.gui;


import it.polimi.ingsw.ui.Cli;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;

/**
 * This is the main client class which starts CLI or gui
 */
public class Client {
    private static int RMI_PORT= Constants.RMI_PORT;
    private static int SOCKET_PORT= Constants.SOCKET_PORT;

    public static void main(String[] args) throws IOException, NotBoundException, URISyntaxException {
        Logger.log("Client starting request...");
        if (args.length != 4) {
            Logger.logAndPrint("Proper Usage: CLI | gui hostname rmi-port socket-port");
            return;
        }
        SOCKET_PORT=Integer.parseInt(args[3]);
        RMI_PORT=Integer.parseInt(args[2]);
        if ((args[0].equalsIgnoreCase("CLI") || args[0].equalsIgnoreCase("gui")) && !args[1].equalsIgnoreCase("")) {
            System.out.println("Starting " + args[0] + "...");
            if (args[0].equalsIgnoreCase("CLI")) {
                new Cli(args[1]);
            } else {
                MainWindow.setHostname(args[1]);
                MainWindow.main(args);
            }
        }
    }

    public static int getRmiPort() {
        return RMI_PORT;
    }

    public static int getSocketPort() {
        return SOCKET_PORT;
    }
}

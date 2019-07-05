package it.polimi.ingsw.network.client;

import it.polimi.ingsw.ui.gui.MainWindow;
import it.polimi.ingsw.ui.Cli;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;

/**
 * This is the main client class which starts CLI or gui
 */
public class Client {
    public static void main(String[] args) throws IOException, NotBoundException, URISyntaxException {
        Logger.log("Client starting request...");
        if(args.length!=2){
            Logger.logAndPrint("Proper Usage: CLI | gui hostname");
            return;
        }
        if((args[0].equalsIgnoreCase("CLI")||args[0].equalsIgnoreCase("gui"))&&!args[1].equalsIgnoreCase("")) {
            System.out.println("Starting "+args[0]+"...");
            if(args[0].equalsIgnoreCase("CLI")) {
                new Cli(args[1]);
            }
            else {
                MainWindow.setHostname(args[1]);
                MainWindow.main(args);
            }
        }
    }
}
